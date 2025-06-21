package com.example.membersystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用戶積分記錄實體類
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_points")
public class UserPoints {

    /**
     * 記錄ID (UUID)
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 用戶ID (UUID)
     */
    @TableField("user_id")
    private String userId;

    /**
     * 積分變動數量（正數為增加，負數為減少）
     */
    @TableField("points")
    private Long points;

    /**
     * 變動後積分餘額
     */
    @TableField("balance")
    private Long balance;

    /**
     * 積分類型：1=簽到, 2=消費, 3=兌換, 4=系統調整
     */
    @TableField("type")
    private Integer type;

    /**
     * 積分變動描述
     */
    @TableField("description")
    private String description;

    /**
     * 關聯業務ID
     */
    @TableField("ref_id")
    private String refId;

    /**
     * 創建時間
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 用戶信息（不對應資料庫字段）
     */
    @TableField(exist = false)
    private User user;
}