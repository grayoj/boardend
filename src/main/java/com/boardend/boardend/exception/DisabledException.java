package com.boardend.boardend.exception;

import org.springframework.security.core.AuthenticationException;

public class DisabledException extends AuthenticationException {
    public DisabledException(String message) {
        super(message);
    }
}
