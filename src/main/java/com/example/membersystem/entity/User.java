package com.example.membersystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用戶實體類
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user")
public class User extends BaseEntity {

    /**
     * 用戶名
     */
    @TableField("username")
    private String username;

    /**
     * 郵箱
     */
    @TableField("email")
    private String email;

    /**
     * 加密密碼
     */
    @TableField("password")
    private String password;

    /**
     * 手機號碼
     */
    @TableField("phone")
    private String phone;

    /**
     * 暱稱
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 頭像URL
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 狀態：1=正常, 0=禁用
     */
    @TableField("status")
    private Integer status;

    /**
     * 用戶角色列表（不對應資料庫字段）
     */
    @TableField(exist = false)
    private List<Role> roles;

    /**
     * 用戶權限列表（不對應資料庫字段）
     */
    @TableField(exist = false)
    private List<Permission> permissions;

    /**
     * 當前積分餘額（不對應資料庫字段）
     */
    @TableField(exist = false)
    private Long currentPoints;

    /**
     * 會員等級（不對應資料庫字段）
     */
    @TableField(exist = false)
    private MemberLevel memberLevel;
}