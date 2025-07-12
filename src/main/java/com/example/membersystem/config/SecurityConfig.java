package com.example.membersystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 開發環境安全配置
 * 在開發環境中禁用認證以方便開發和測試
 * 生產環境中可以啟用完整的安全機制
 */
@Configuration
@EnableWebSecurity
@Profile("dev") // 只在 dev profile 中生效
public class SecurityConfig {

    /**
     * 開發環境安全配置 - 允許所有請求
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF 保護（開發環境）
                .csrf(csrf -> csrf.disable())

                // 允許所有請求無需認證
                .authorizeHttpRequests(authz -> authz
                        // Swagger UI 相關路徑
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/api-docs/**").permitAll()

                        // Actuator 監控端點
                        .requestMatchers("/actuator/**").permitAll()

                        // 所有 API 端點
                        .requestMatchers("/api/**").permitAll()

                        // 其他所有請求
                        .anyRequest().permitAll()
                )

                // 禁用表單登入
                .formLogin(form -> form.disable())

                // 禁用 HTTP Basic 認證
                .httpBasic(basic -> basic.disable())

                // 禁用 logout
                .logout(logout -> logout.disable());

        return http.build();
    }
}