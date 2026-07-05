package com.scamshield.backend.exception;

import org.springframework.http.HttpStatus;

/**
 * Generic application exception that carries an HTTP status code.
 * Caught by GlobalExceptionHandler and serialized as {"error": "message"}.
 */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
