package com.example.membersystem.exception;

/**
 * 用戶不存在異常
 */
public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String message) {
        super(404, message);
    }

    public UserNotFoundException() {
        super(404, "用戶不存在");
    }
}
