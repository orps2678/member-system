package com.example.membersystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 基礎實體類
 * 包含所有實體類共有的字段
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseEntity {
    /**
     * 主鍵ID (UUID)
     * 使用 ASSIGN_UUID 策略自動生成 UUID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 邏輯刪除標記
     * 0: 未刪除, 1: 已刪除
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;

    /**
     * 創建時間
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新時間
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
