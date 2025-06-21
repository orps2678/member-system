package com.example.membersystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 操作日誌實體類
 */
@Data
@Accessors(chain = true)
@TableName("operation_log")
public class OperationLog {

    /**
     * 日誌ID (UUID)
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 操作用戶ID (UUID)
     */
    @TableField("user_id")
    private String userId;

    /**
     * 操作用戶名
     */
    @TableField("username")
    private String username;

    /**
     * 操作類型
     */
    @TableField("operation")
    private String operation;

    /**
     * 操作方法
     */
    @TableField("method")
    private String method;

    /**
     * 請求參數
     */
    @TableField("params")
    private String params;

    /**
     * 操作結果
     */
    @TableField("result")
    private String result;

    /**
     * IP地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 用戶代理
     */
    @TableField("user_agent")
    private String userAgent;

    /**
     * 執行時長(毫秒)
     */
    @TableField("execution_time")
    private Long executionTime;

    /**
     * 操作狀態：1=成功, 0=失敗
     */
    @TableField("status")
    private Integer status;

    /**
     * 錯誤信息
     */
    @TableField("error_msg")
    private String errorMsg;

    /**
     * 創建時間
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}