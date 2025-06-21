package com.example.membersystem.exception;

/**
 * 配置異常
 */
public class ConfigurationException extends BusinessException {
    public ConfigurationException(String message) {
        super(500, message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(500, message, cause);
    }
}
