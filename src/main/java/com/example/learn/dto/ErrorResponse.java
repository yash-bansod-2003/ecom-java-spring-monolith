package com.example.learn.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private boolean success;
    private String message;
    private String error;
    private int status;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;

    public ErrorResponse(boolean success, String message, String error, int status) {
        this.success = success;
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(boolean success, String message, int status, Map<String, String> validationErrors) {
        this.success = success;
        this.message = message;
        this.status = status;
        this.validationErrors = validationErrors;
        this.timestamp = LocalDateTime.now();
    }
}

