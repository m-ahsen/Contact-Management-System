package com.ahsen.contactmanagement.auth.service;

import com.ahsen.contactmanagement.auth.dto.AuthResponse;
import com.ahsen.contactmanagement.auth.dto.LoginRequest;
import com.ahsen.contactmanagement.auth.dto.RegisterRequest;
import com.ahsen.contactmanagement.auth.dto.RegisterResponse;
import com.ahsen.contactmanagement.exception.DuplicateEmailException;
import com.ahsen.contactmanagement.exception.DuplicatePhoneException;
import com.ahsen.contactmanagement.exception.InvalidCredentialsException;
import com.ahsen.contactmanagement.security.JwtService;
import com.ahsen.contactmanagement.user.dto.UserResponse;
import com.ahsen.contactmanagement.user.entity.User;
import com.ahsen.contactmanagement.user.repository.UserRepository;
import com.ahsen.contactmanagement.user.service.UserIdentity;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = UserIdentity.normalizeEmail(request.email());
        String phone = UserIdentity.normalizePhone(request.phone());
        log.info("Registration attempt using {}", email != null ? "email" : "phone");

        if (email != null && userRepository.existsByEmail(email)) {
            log.warn("Registration rejected: duplicate email");
            throw new DuplicateEmailException();
        }
        if (phone != null && userRepository.existsByPhone(phone)) {
            log.warn("Registration rejected: duplicate phone");
            throw new DuplicatePhoneException();
        }

        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        User saved = userRepository.save(user);
        log.info("Successful registration: userId={}", saved.getId());
        return new RegisterResponse("User registered successfully");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = UserIdentity.normalizeEmail(request.email());
        String phone = UserIdentity.normalizePhone(request.phone());
        Optional<User> found = email != null
                ? userRepository.findByEmail(email)
                : userRepository.findByPhone(phone);

        if (found.isEmpty() || !passwordEncoder.matches(request.password(), found.get().getPasswordHash())) {
            log.warn("Failed login attempt");
            throw new InvalidCredentialsException();
        }

        User user = found.get();
        String token = jwtService.generateToken(user.getId());
        log.info("Successful login: userId={}", user.getId());
        return new AuthResponse(token, toUserResponse(user));
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getPhone());
    }
}
