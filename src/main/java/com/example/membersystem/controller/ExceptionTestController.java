package com.example.membersystem.controller;

import com.example.membersystem.common.ApiResult;
import com.example.membersystem.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 異常測試控制器
 * 用於測試全域異常處理機制
 */
@RestController
@RequestMapping("/test/exception")
@Tag(name = "異常測試 API", description = "用於測試全域異常處理機制的 API 端點")
public class ExceptionTestController {

    @GetMapping("/business")
    @Operation(summary = "測試業務異常", description = "拋出一個業務異常來測試異常處理")
    @ApiResponses({
            @ApiResponse(responseCode = "500", description = "業務異常")
    })
    public ApiResult<Void> testBusinessException() {
        throw new BusinessException("這是一個測試業務異常");
    }

    @GetMapping("/user-not-found")
    @Operation(summary = "測試用戶不存在異常", description = "拋出用戶不存在異常")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "用戶不存在")
    })
    public ApiResult<Void> testUserNotFoundException() {
        throw new UserNotFoundException("用戶 ID 123 不存在");
    }

    @GetMapping("/user-exists")
    @Operation(summary = "測試用戶已存在異常", description = "拋出用戶已存在異常")
    @ApiResponses({
            @ApiResponse(responseCode = "409", description = "用戶已存在")
    })
    public ApiResult<Void> testUserAlreadyExistsException() {
        throw new UserAlreadyExistsException("用戶名 'testuser' 已存在");
    }

    @GetMapping("/unauthorized")
    @Operation(summary = "測試未認證異常", description = "拋出未認證異常")
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "未認證")
    })
    public ApiResult<Void> testUnauthorizedException() {
        throw new UnauthorizedException("請先登入後再訪問此資源");
    }

    @GetMapping("/forbidden")
    @Operation(summary = "測試權限不足異常", description = "拋出權限不足異常")
    @ApiResponses({
            @ApiResponse(responseCode = "403", description = "權限不足")
    })
    public ApiResult<Void> testForbiddenException() {
        throw new InsufficientPermissionException("您沒有權限執行此操作");
    }

    @GetMapping("/database-error")
    @Operation(summary = "測試資料庫異常", description = "拋出資料庫異常")
    @ApiResponses({
            @ApiResponse(responseCode = "500", description = "資料庫錯誤")
    })
    public ApiResult<Void> testDatabaseException() {
        throw new DatabaseException("資料庫連接失敗");
    }

    @GetMapping("/null-pointer")
    @Operation(summary = "測試空指針異常", description = "拋出空指針異常")
    @ApiResponses({
            @ApiResponse(responseCode = "500", description = "系統內部錯誤")
    })
    public ApiResult<Void> testNullPointerException() {
        String str = null;
        // 這會拋出 NullPointerException，不需要返回具體值
        str.length();
        return ApiResult.success(); // 這行不會執行到
    }

    @GetMapping("/illegal-argument")
    @Operation(summary = "測試非法參數異常", description = "拋出非法參數異常")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "參數錯誤")
    })
    public ApiResult<Void> testIllegalArgumentException(
            @Parameter(description = "年齡（故意傳入負數測試）", example = "-1")
            @RequestParam Integer age) {

        if (age < 0) {
            throw new IllegalArgumentException("年齡不能為負數");
        }
        return ApiResult.success();
    }

    @PostMapping("/validation")
    @Operation(summary = "測試參數驗證異常", description = "測試 @Valid 參數驗證")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "參數驗證失敗"),
            @ApiResponse(responseCode = "200", description = "驗證通過")
    })
    public ApiResult<String> testValidationException(
            @Valid @RequestBody TestUserRequest request) {

        return ApiResult.success("驗證通過", request.toString());
    }

    @GetMapping("/method-not-supported")
    @Operation(summary = "測試 HTTP 方法不支持", description = "只支持 GET，用 POST 訪問會報錯")
    public ApiResult<Void> testMethodNotSupported() {
        return ApiResult.success();
    }

    /**
     * 測試用戶請求 DTO
     */
    @Data
    public static class TestUserRequest {

        @NotBlank(message = "用戶名不能為空")
        @Size(min = 3, max = 20, message = "用戶名長度必須在 3-20 個字符之間")
        private String username;

        @NotBlank(message = "郵箱不能為空")
        @Email(message = "郵箱格式不正確")
        private String email;

        @NotBlank(message = "密碼不能為空")
        @Size(min = 6, max = 20, message = "密碼長度必須在 6-20 個字符之間")
        private String password;

        @Override
        public String toString() {
            return String.format("TestUserRequest{username='%s', email='%s'}", username, email);
        }
    }
}