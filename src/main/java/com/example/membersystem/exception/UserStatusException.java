package com.example.membersystem.exception;

/**
 * 用戶狀態異常
 */
public class UserStatusException extends BusinessException {
    public UserStatusException(String message) {
        super(403, message);
    }

    public UserStatusException() {
        super(403, "用戶狀態異常");
    }
}
