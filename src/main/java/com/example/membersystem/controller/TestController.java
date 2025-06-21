package com.example.membersystem.controller;

import com.example.membersystem.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 測試控制器
 * 用於驗證 Swagger 配置和基本功能
 */
@RestController
@RequestMapping("/test")
@Tag(name = "測試 API", description = "用於測試系統功能的 API 端點")
public class TestController {

    @GetMapping("/hello")
    @Operation(
            summary = "Hello World",
            description = "最基本的測試端點，不需要認證"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功返回問候訊息"),
            @ApiResponse(responseCode = "500", description = "服務器內部錯誤")
    })
    public ResponseEntity<Map<String, Object>> hello() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello, Member System!");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    @Operation(
            summary = "獲取用戶資訊",
            description = "根據用戶 ID 獲取用戶詳細資訊（模擬資料）"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "成功返回用戶資訊"),
            @ApiResponse(responseCode = "404", description = "用戶不存在"),
            @ApiResponse(responseCode = "500", description = "服務器內部錯誤")
    })
    public ResponseEntity<User> getUser(
            @Parameter(description = "用戶 ID (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String id
    ) {
        // 模擬用戶資料
        User user = new User()
                .setUsername("testuser")
                .setEmail("test@example.com")
                .setNickname("測試用戶")
                .setPhone("0912345678")
                .setStatus(1);

        // 正確設置 UUID 格式的 ID
        user.setId(id);  // 現在 id 是 String 類型，匹配 BaseEntity
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        return ResponseEntity.ok(user);
    }

    @PostMapping("/user")
    @Operation(
            summary = "創建用戶",
            description = "創建新用戶（模擬操作）"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "用戶創建成功"),
            @ApiResponse(responseCode = "400", description = "請求參數錯誤"),
            @ApiResponse(responseCode = "409", description = "用戶名或郵箱已存在"),
            @ApiResponse(responseCode = "500", description = "服務器內部錯誤")
    })
    public ResponseEntity<Map<String, Object>> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "用戶資訊",
                    required = true
            )
            @RequestBody User user
    ) {
        // 模擬創建用戶
        String newUserId = UUID.randomUUID().toString();
        user.setId(newUserId);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "用戶創建成功");
        response.put("userId", newUserId);
        response.put("user", user);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/protected")
    @Operation(
            summary = "受保護的端點",
            description = "需要 JWT 認證的測試端點",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "認證成功，返回受保護資源"),
            @ApiResponse(responseCode = "401", description = "未認證或 Token 無效"),
            @ApiResponse(responseCode = "403", description = "權限不足")
    })
    public ResponseEntity<Map<String, Object>> protectedEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "這是受保護的資源");
        response.put("timestamp", LocalDateTime.now());
        response.put("authenticated", true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/system-info")
    @Operation(
            summary = "系統資訊",
            description = "獲取系統基本資訊和狀態"
    )
    public ResponseEntity<Map<String, Object>> getSystemInfo() {
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("application", "Member System");
        systemInfo.put("version", "1.0.0");
        systemInfo.put("environment", "development");
        systemInfo.put("database", "MySQL 9.0");
        systemInfo.put("cache", "Redis");
        systemInfo.put("uptime", LocalDateTime.now());

        return ResponseEntity.ok(systemInfo);
    }
}