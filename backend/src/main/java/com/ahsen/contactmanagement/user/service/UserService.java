package com.ahsen.contactmanagement.user.service;

import com.ahsen.contactmanagement.exception.IncorrectPasswordException;
import com.ahsen.contactmanagement.exception.ResourceNotFoundException;
import com.ahsen.contactmanagement.security.CurrentUserService;
import com.ahsen.contactmanagement.user.dto.ChangePasswordRequest;
import com.ahsen.contactmanagement.user.dto.MessageResponse;
import com.ahsen.contactmanagement.user.dto.UserResponse;
import com.ahsen.contactmanagement.user.entity.User;
import com.ahsen.contactmanagement.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            CurrentUserService currentUserService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        return toResponse(requireCurrentUser());
    }

    @Transactional
    public MessageResponse changePassword(ChangePasswordRequest request) {
        User user = requireCurrentUser();
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            log.warn("Password change rejected: incorrect current password userId={}", user.getId());
            throw new IncorrectPasswordException();
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        log.info("Password changed: userId={}", user.getId());
        return new MessageResponse("Password changed successfully");
    }

    private User requireCurrentUser() {
        Long userId = currentUserService.getCurrentUserId();
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getPhone());
    }
}
