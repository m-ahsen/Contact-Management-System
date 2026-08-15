package com.ahsen.contactmanagement.exception;

public class DuplicatePhoneException extends RuntimeException {

    public DuplicatePhoneException() {
        super("Phone already registered");
    }
}
