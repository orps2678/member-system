package com.example.membersystem.exception;

import lombok.Getter;

/**
 * 業務異常基礎類
 * 用於封裝業務邏輯中的異常情況
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 錯誤代碼
     */
    private final Integer code;

    /**
     * 錯誤訊息
     */
    private final String message;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message) {
        this(500, message);
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}