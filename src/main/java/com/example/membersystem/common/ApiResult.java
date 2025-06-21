package com.example.membersystem.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 統一 API 返回結果格式
 */
@Data
@Accessors(chain = true)
@Schema(description = "統一返回結果")
public class ApiResult<T> {

    @Schema(description = "狀態碼", example = "200")
    private Integer code;

    @Schema(description = "返回訊息", example = "操作成功")
    private String message;

    @Schema(description = "返回資料")
    private T data;

    @Schema(description = "時間戳", example = "2025-06-22T12:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "請求是否成功", example = "true")
    private Boolean success;

    public ApiResult() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * 成功返回（無資料）
     */
    public static <T> ApiResult<T> success() {
        return new ApiResult<T>()
                .setCode(200)
                .setMessage("操作成功")
                .setSuccess(true);
    }

    /**
     * 成功返回（帶資料）
     */
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>()
                .setCode(200)
                .setMessage("操作成功")
                .setData(data)
                .setSuccess(true);
    }

    /**
     * 成功返回（自定義訊息）
     */
    public static <T> ApiResult<T> success(String message, T data) {
        return new ApiResult<T>()
                .setCode(200)
                .setMessage(message)
                .setData(data)
                .setSuccess(true);
    }

    /**
     * 失敗返回
     */
    public static <T> ApiResult<T> error(Integer code, String message) {
        return new ApiResult<T>()
                .setCode(code)
                .setMessage(message)
                .setSuccess(false);
    }

    /**
     * 失敗返回（預設 500 錯誤）
     */
    public static <T> ApiResult<T> error(String message) {
        return error(500, message);
    }

    /**
     * 參數錯誤
     */
    public static <T> ApiResult<T> badRequest(String message) {
        return error(400, message);
    }

    /**
     * 未授權
     */
    public static <T> ApiResult<T> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * 禁止訪問
     */
    public static <T> ApiResult<T> forbidden(String message) {
        return error(403, message);
    }

    /**
     * 資源不存在
     */
    public static <T> ApiResult<T> notFound(String message) {
        return error(404, message);
    }
}