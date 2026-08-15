package com.ahsen.contactmanagement.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ahsen.contactmanagement.exception.IncorrectPasswordException;
import com.ahsen.contactmanagement.security.CurrentUserService;
import com.ahsen.contactmanagement.user.dto.ChangePasswordRequest;
import com.ahsen.contactmanagement.user.dto.MessageResponse;
import com.ahsen.contactmanagement.user.dto.UserResponse;
import com.ahsen.contactmanagement.user.entity.User;
import com.ahsen.contactmanagement.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, currentUserService, passwordEncoder);
    }

    @Test
    void getCurrentUserReturnsSafeProfile() {
        User user = storedUser();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getCurrentUser();

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("user@example.com");
        assertThat(response.phone()).isNull();
    }

    @Test
    void changePasswordSucceeds() {
        User user = storedUser();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        MessageResponse response =
                userService.changePassword(new ChangePasswordRequest("Password1", "NewPass12"));

        assertThat(response.message()).isEqualTo("Password changed successfully");
        assertThat(passwordEncoder.matches("NewPass12", user.getPasswordHash())).isTrue();
        verify(userRepository).save(user);
    }

    @Test
    void changePasswordRejectsIncorrectCurrentPassword() {
        User user = storedUser();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThatThrownBy(
                        () -> userService.changePassword(new ChangePasswordRequest("WrongPass1", "NewPass12")))
                .isInstanceOf(IncorrectPasswordException.class);
        assertThat(passwordEncoder.matches("Password1", user.getPasswordHash())).isTrue();
    }

    private User storedUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPasswordHash(passwordEncoder.encode("Password1"));
        return user;
    }
}
