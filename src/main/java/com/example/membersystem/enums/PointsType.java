package com.example.membersystem.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 積分類型枚舉
 */
@Getter
@AllArgsConstructor
public enum PointsType {

    /**
     * 簽到
     */
    CHECK_IN(1, "簽到"),

    /**
     * 消費
     */
    CONSUMPTION(2, "消費"),

    /**
     * 兌換
     */
    EXCHANGE(3, "兌換"),

    /**
     * 系統調整
     */
    SYSTEM_ADJUSTMENT(4, "系統調整");

    private final Integer code;
    private final String description;

    /**
     * 根據代碼獲取枚舉
     */
    public static PointsType getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PointsType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}