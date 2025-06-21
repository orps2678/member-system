package com.example.membersystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 權限實體類
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("permission")
public class Permission extends BaseEntity {

    /**
     * 權限名稱
     */
    @TableField("name")
    private String name;

    /**
     * 權限代碼
     */
    @TableField("code")
    private String code;

    /**
     * 資源路徑
     */
    @TableField("resource")
    private String resource;

    /**
     * HTTP方法
     */
    @TableField("method")
    private String method;

    /**
     * 父權限ID
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 類型：1=菜單, 2=按鈕, 3=API
     */
    @TableField("type")
    private Integer type;

    /**
     * 排序
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 狀態：1=啟用, 0=禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 子權限列表（不對應資料庫字段）
     */
    @TableField(exist = false)
    private List<Permission> children;
}