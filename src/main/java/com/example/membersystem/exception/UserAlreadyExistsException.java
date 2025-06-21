package com.example.membersystem.exception;

/**
 * 用戶已存在異常
 */
public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException(String message) {
        super(409, message);
    }

    public UserAlreadyExistsException() {
        super(409, "用戶已存在");
    }
}
