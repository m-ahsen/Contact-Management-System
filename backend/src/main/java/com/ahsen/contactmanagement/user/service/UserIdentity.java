package com.ahsen.contactmanagement.user.service;

import java.util.Locale;

public final class UserIdentity {

    private UserIdentity() {
    }

    public static String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public static String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }
        return phone.trim().replace(" ", "");
    }
}
