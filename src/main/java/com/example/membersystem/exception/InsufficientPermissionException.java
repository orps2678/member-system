package com.example.membersystem.exception;

/**
 * 權限不足異常
 */
public class InsufficientPermissionException extends BusinessException {
    public InsufficientPermissionException(String message) {
        super(403, message);
    }

    public InsufficientPermissionException() {
        super(403, "權限不足");
    }
}
