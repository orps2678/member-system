package com.example.membersystem.exception;

/**
 * 資料庫異常
 */
public class DatabaseException extends BusinessException {
    public DatabaseException(String message) {
        super(500, message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(500, message, cause);
    }
}
