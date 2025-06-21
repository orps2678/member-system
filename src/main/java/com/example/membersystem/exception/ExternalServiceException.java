package com.example.membersystem.exception;

/**
 * 外部服務異常
 */
public class ExternalServiceException extends BusinessException {
    public ExternalServiceException(String message) {
        super(502, message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(502, message, cause);
    }
}