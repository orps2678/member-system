package com.example.membersystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 權限類型枚舉
 */
@Getter
@AllArgsConstructor
public enum PermissionType {

    /**
     * 菜單
     */
    MENU(1, "菜單"),

    /**
     * 按鈕
     */
    BUTTON(2, "按鈕"),

    /**
     * API
     */
    API(3, "API");

    private final Integer code;
    private final String description;

    /**
     * 根據代碼獲取枚舉
     */
    public static PermissionType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PermissionType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}