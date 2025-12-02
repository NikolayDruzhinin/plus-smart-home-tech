package ru.yandex.practicum.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Builder
public class ErrorResponse {
    private final HttpStatus status;
    private final String error;
    private final String message;
    private final String path;

    @Builder.Default
    private final ErrorDetails details = null;

    @Data
    @AllArgsConstructor
    public static class ErrorDetails {
        Throwable cause;
        String stackTrace;
    }
}
