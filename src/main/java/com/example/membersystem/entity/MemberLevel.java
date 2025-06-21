package com.example.membersystem.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 會員等級實體類
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("member_level")
public class MemberLevel extends BaseEntity {

    /**
     * 等級名稱
     */
    @TableField("name")
    private String name;

    /**
     * 等級數值
     */
    @TableField("level")
    private Integer level;

    /**
     * 最小積分要求
     */
    @TableField("min_points")
    private Long minPoints;

    /**
     * 最大積分限制
     */
    @TableField("max_points")
    private Long maxPoints;

    /**
     * 折扣率
     */
    @TableField("discount_rate")
    private BigDecimal discountRate;

    /**
     * 等級描述
     */
    @TableField("description")
    private String description;

    /**
     * 狀態：1=啟用, 0=禁用
     */
    @TableField("status")
    private Integer status;
}