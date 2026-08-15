package com.ahsen.contactmanagement.common.validation;

public final class ValidationPatterns {

    public static final String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d).{8,72}$";
    public static final String PASSWORD_MESSAGE =
            "Password must be 8-72 characters and include at least one letter and one digit";

    public static final String PHONE = "^$|^\\+?[1-9]\\d{9,14}$";
    public static final String PHONE_MESSAGE =
            "Phone must be 10-15 digits and may start with +";

    public static final int PASSWORD_MIN = 8;
    public static final int PASSWORD_MAX = 72;
    public static final int EMAIL_MAX = 255;
    public static final int PHONE_MAX = 20;

    private ValidationPatterns() {
    }
}
