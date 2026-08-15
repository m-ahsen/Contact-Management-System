package com.ahsen.contactmanagement.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahsen.contactmanagement.auth.dto.AuthResponse;
import com.ahsen.contactmanagement.auth.dto.LoginRequest;
import com.ahsen.contactmanagement.auth.dto.RegisterRequest;
import com.ahsen.contactmanagement.auth.dto.RegisterResponse;
import com.ahsen.contactmanagement.exception.DuplicateEmailException;
import com.ahsen.contactmanagement.exception.DuplicatePhoneException;
import com.ahsen.contactmanagement.exception.InvalidCredentialsException;
import com.ahsen.contactmanagement.security.JwtService;
import com.ahsen.contactmanagement.user.entity.User;
import com.ahsen.contactmanagement.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void registerWithEmailSucceeds() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        RegisterResponse response =
                authService.register(new RegisterRequest("user@example.com", null, "Password1"));

        assertThat(response.message()).isEqualTo("User registered successfully");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("user@example.com");
        assertThat(saved.getPhone()).isNull();
        assertThat(saved.getPasswordHash()).isNotEqualTo("Password1");
        assertThat(passwordEncoder.matches("Password1", saved.getPasswordHash())).isTrue();
    }

    @Test
    void registerWithPhoneSucceeds() {
        when(userRepository.existsByPhone("+15551234567")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L);
            return user;
        });

        RegisterResponse response =
                authService.register(new RegisterRequest(null, "+15551234567", "Password1"));

        assertThat(response.message()).isEqualTo("User registered successfully");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPhone()).isEqualTo("+15551234567");
        assertThat(captor.getValue().getEmail()).isNull();
    }

    @Test
    void registerDuplicateEmailIsRejected() {
        when(userRepository.existsByEmail("user@example.com")).thenReturn(true);

        assertThatThrownBy(
                        () -> authService.register(new RegisterRequest("user@example.com", null, "Password1")))
                .isInstanceOf(DuplicateEmailException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerDuplicatePhoneIsRejected() {
        when(userRepository.existsByPhone("+15551234567")).thenReturn(true);

        assertThatThrownBy(
                        () -> authService.register(new RegisterRequest(null, "+15551234567", "Password1")))
                .isInstanceOf(DuplicatePhoneException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void loginWithEmailSucceeds() {
        User user = storedUser(1L, "user@example.com", null, "Password1");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(1L)).thenReturn("jwt-token");

        AuthResponse response = authService.login(new LoginRequest("user@example.com", null, "Password1"));

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.user().id()).isEqualTo(1L);
        assertThat(response.user().email()).isEqualTo("user@example.com");
    }

    @Test
    void loginWithPhoneSucceeds() {
        User user = storedUser(3L, null, "+15551234567", "Password1");
        when(userRepository.findByPhone("+15551234567")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(3L)).thenReturn("jwt-token");

        AuthResponse response = authService.login(new LoginRequest(null, "+15551234567", "Password1"));

        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.user().phone()).isEqualTo("+15551234567");
    }

    @Test
    void loginWithIncorrectPasswordFails() {
        User user = storedUser(1L, "user@example.com", null, "Password1");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login(new LoginRequest("user@example.com", null, "WrongPass1")))
                .isInstanceOf(InvalidCredentialsException.class);
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void loginWithUnknownUserFails() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(
                        () -> authService.login(new LoginRequest("missing@example.com", null, "Password1")))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    private User storedUser(Long id, String email, String phone, String rawPassword) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        return user;
    }
}
