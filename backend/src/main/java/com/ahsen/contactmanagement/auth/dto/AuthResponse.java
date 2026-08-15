package com.ahsen.contactmanagement.auth.dto;

import com.ahsen.contactmanagement.common.ApiConstants;
import com.ahsen.contactmanagement.user.dto.UserResponse;

public record AuthResponse(String token, String tokenType, UserResponse user) {

    public AuthResponse(String token, UserResponse user) {
        this(token, ApiConstants.BEARER, user);
    }
}
