package com.ahsen.contactmanagement.auth.dto;

import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.EMAIL_MAX;
import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PASSWORD;
import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PASSWORD_MAX;
import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PASSWORD_MESSAGE;
import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PASSWORD_MIN;
import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PHONE;
import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PHONE_MAX;
import static com.ahsen.contactmanagement.common.validation.ValidationPatterns.PHONE_MESSAGE;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@EmailOrPhoneRequired
public record RegisterRequest(
        @Email(message = "Email must be valid") @Size(max = EMAIL_MAX) String email,
        @Pattern(regexp = PHONE, message = PHONE_MESSAGE) @Size(max = PHONE_MAX) String phone,
        @NotBlank(message = "Password is required")
                @Size(min = PASSWORD_MIN, max = PASSWORD_MAX, message = PASSWORD_MESSAGE)
                @Pattern(regexp = PASSWORD, message = PASSWORD_MESSAGE)
                String password)
        implements EmailOrPhoneCredentials {}
