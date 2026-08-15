package com.ahsen.contactmanagement.user.dto;

import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PASSWORD;
import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PASSWORD_MAX;
import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PASSWORD_MESSAGE;
import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PASSWORD_MIN;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Current password is required") String currentPassword,
        @NotBlank(message = "New password is required")
                @Size(min = PASSWORD_MIN, max = PASSWORD_MAX, message = PASSWORD_MESSAGE)
                @Pattern(regexp = PASSWORD, message = PASSWORD_MESSAGE)
                String newPassword) {}
