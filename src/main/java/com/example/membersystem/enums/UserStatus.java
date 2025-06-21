package com.example.membersystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用戶狀態枚舉
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    /**
     * 禁用
     */
    DISABLED(0, "禁用"),

    /**
     * 正常
     */
    NORMAL(1, "正常");

    private final Integer code;
    private final String description;

    /**
     * 根據代碼獲取枚舉
     */
    public static UserStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}