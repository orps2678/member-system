package com.example.membersystem.exception;

/**
 * 密碼錯誤異常
 */
public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException(String message) {
        super(401, message);
    }

    public InvalidPasswordException() {
        super(401, "密碼錯誤");
    }
}
