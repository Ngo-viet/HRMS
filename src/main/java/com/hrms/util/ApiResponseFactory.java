package com.hrms.util;

import com.hrms.dto.ApiResponse;

import java.util.Collections;
import java.util.List;

public final class ApiResponseFactory {
    private ApiResponseFactory() {}

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.of(true, 200, "OK", ClientMessageIdGenerator.generate(), data, null);
    }

    public static <T> ApiResponse<T> success(T data, String message, int status) {
        return ApiResponse.of(true, status, message, ClientMessageIdGenerator.generate(), data, null);
    }

    public static ApiResponse<Void> error(String message, int status) {
        return ApiResponse.of(false, status, message, ClientMessageIdGenerator.generate(), null, Collections.singletonList(message));
    }

    public static ApiResponse<Void> error(List<String> errors, int status) {
        String message = (errors == null || errors.isEmpty()) ? "Error" : errors.get(0);
        return ApiResponse.of(false, status, message, ClientMessageIdGenerator.generate(), null, errors);
    }
}
