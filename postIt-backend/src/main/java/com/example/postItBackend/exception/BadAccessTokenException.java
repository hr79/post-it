package com.example.postItBackend.exception;

import org.springframework.security.core.AuthenticationException;

public class BadAccessTokenException extends AuthenticationException {
    public BadAccessTokenException(String message) {
        super(message);
    }
}
