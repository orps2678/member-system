package com.example.membersystem.exception;

/**
 * Token 無效異常
 */
public class InvalidTokenException extends BusinessException {
    public InvalidTokenException(String message) {
        super(401, message);
    }

    public InvalidTokenException() {
        super(401, "Token 無效或已過期");
    }
}
