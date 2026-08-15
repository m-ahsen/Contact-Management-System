package com.ahsen.contactmanagement.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(
        int status, String error, String message, String path, List<FieldErrorDetail> fieldErrors) {

    public record FieldErrorDetail(String field, String message) {}
}
