package com.example.membersystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger 配置類
 * 配置 API 文檔的基本資訊和安全認證
 */
@Configuration
public class SwaggerConfig {

    /**
     * OpenAPI 配置
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 基本資訊配置
                .info(apiInfo())
                // 服務器配置
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8090/api")
                                .description("開發環境"),
                        new Server()
                                .url("https://your-domain.com/api")
                                .description("生產環境")
                ))
                // 安全配置
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", createSecurityScheme())
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    /**
     * API 基本資訊
     */
    private Info apiInfo() {
        return new Info()
                .title("會員管理系統 API")
                .description("""
                    ## 會員管理系統 REST API 文檔
                    
                    ### 功能特色
                    - 🔐 JWT 認證授權
                    - 👥 用戶註冊登入
                    - 🏷️ 角色權限管理
                    - 🎖️ 會員等級系統
                    - 💰 積分系統
                    - 📊 操作日誌記錄
                    
                    ### 技術棧
                    - Spring Boot 3.x
                    - MyBatis Plus
                    - MySQL 9.0
                    - Redis
                    - JWT
                    
                    ### 認證說明
                    大部分 API 需要在 Header 中攜帶 JWT Token：
                    ```
                    Authorization: Bearer <your-jwt-token>
                    ```
                    """)
                .version("v1.0.0")
                .contact(new Contact()
                        .name("開發團隊")
                        .email("dev@example.com")
                        .url("https://github.com/your-username/member-system")
                )
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT")
                );
    }

    /**
     * 安全認證配置
     */
    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("請輸入 JWT Token，格式：Bearer <token>");
    }
}