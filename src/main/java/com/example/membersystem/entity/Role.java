package com.example.membersystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 角色實體類
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("role")
public class Role extends BaseEntity {

    /**
     * 角色名稱
     */
    @TableField("name")
    private String name;

    /**
     * 角色代碼
     */
    @TableField("code")
    private String code;

    /**
     * 角色描述
     */
    @TableField("description")
    private String description;

    /**
     * 狀態：1=啟用, 0=禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 角色權限列表（不對應資料庫字段）
     */
    @TableField(exist = false)
    private List<Permission> permissions;
}