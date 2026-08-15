package com.ahsen.contactmanagement.auth.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EmailOrPhoneValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailOrPhoneRequired {

    String message() default "Email or phone is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
