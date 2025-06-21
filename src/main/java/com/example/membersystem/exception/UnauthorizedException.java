package com.example.membersystem.exception;

/**
 * 未認證異常
 */
public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(401, message);
    }

    public UnauthorizedException() {
        super(401, "未認證，請先登入");
    }
}
