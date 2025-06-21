package com.example.membersystem.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分頁請求參數
 */
@Data
@Schema(description = "分頁請求參數")
public class PageRequest {

    @Schema(description = "頁碼，從 1 開始", example = "1")
    @Min(value = 1, message = "頁碼必須大於 0")
    private Long current = 1L;

    @Schema(description = "每頁大小", example = "10")
    @Min(value = 1, message = "每頁大小必須大於 0")
    @Max(value = 100, message = "每頁大小不能超過 100")
    private Long size = 10L;

    @Schema(description = "排序字段", example = "create_time")
    private String sortField;

    @Schema(description = "排序方向：asc 或 desc", example = "desc")
    private String sortOrder = "desc";

    /**
     * 轉換為 MyBatis Plus 的 Page 對象
     */
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<Object> toPage() {
        return new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, size);
    }
}