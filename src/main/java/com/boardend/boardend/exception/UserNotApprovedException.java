package com.boardend.boardend.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotApprovedException extends AuthenticationException {
    public UserNotApprovedException(String message) {
        super(message);
    }
}
