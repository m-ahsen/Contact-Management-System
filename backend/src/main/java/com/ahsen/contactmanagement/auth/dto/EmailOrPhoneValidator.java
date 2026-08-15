package com.ahsen.contactmanagement.auth.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailOrPhoneValidator implements ConstraintValidator<EmailOrPhoneRequired, EmailOrPhoneCredentials> {

    @Override
    public boolean isValid(EmailOrPhoneCredentials value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        boolean hasEmail = value.email() != null && !value.email().isBlank();
        boolean hasPhone = value.phone() != null && !value.phone().isBlank();
        if (hasEmail || hasPhone) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("Email or phone is required")
                .addConstraintViolation();
        return false;
    }
}
