# 會員管理系統 (Member Management System)

一個基於 Spring Boot 3.x 的現代化會員管理系統，提供完整的用戶註冊、認證授權、權限管理和積分系統功能。

## 🚀 功能特色

- 🔐 **JWT 認證授權** - 無狀態的安全認證
- 👥 **用戶管理** - 註冊、登入、個人資料管理
- 🏷️ **RBAC 權限系統** - 角色與權限管理
- 🎖️ **會員等級** - 多層級會員體系
- 💰 **積分系統** - 積分獲取、消費與兌換
- 📊 **操作日誌** - 完整的審計追蹤
- 📋 **API 文檔** - Swagger/OpenAPI 3.0 自動生成
- 🛡️ **全域異常處理** - 統一錯誤回應格式

## 🛠️ 技術棧

### 後端框架
- **Spring Boot 3.5.3** - 主框架
- **Spring Security 6** - 安全框架
- **Spring Web** - Web 層框架
- **Spring Cache** - 快取抽象

### 資料庫
- **MySQL 9.0** - 主資料庫
- **MyBatis Plus 3.5.12** - ORM 框架
- **Flyway** - 資料庫版本控制

### 快取
- **Redis** - 分散式快取
- **Caffeine** - 本地快取

### 工具類別
- **Lombok** - 減少樣板程式碼
- **MapStruct 1.6.3** - 物件轉換
- **Hutool 5.8.38** - Java 工具庫
- **JWT (jjwt 0.12.6)** - JSON Web Token

### 文檔 & 測試
- **SpringDoc OpenAPI 2.8.9** - API 文檔
- **Spring Boot Test** - 測試框架

## 📋 環境需求

- **Java 17+**
- **Maven 3.6+**
- **MySQL 8.0+** (或 Docker)
- **Redis 6.0+** (可選)

## 🔧 快速開始

### 1. 克隆專案
```bash
git clone https://github.com/your-username/member-system.git
cd member-system
```

### 2. 設定資料庫
```bash
# 使用 Docker 啟動 MySQL
docker run -d \
  --name mysql-container \
  -p 13306:3306 \
  -e MYSQL_ROOT_PASSWORD=your_password \
  -e MYSQL_DATABASE=member_system \
  mysql:latest

# 或手動建立資料庫
mysql -u root -p
CREATE DATABASE member_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 配置應用
編輯 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:13306/member_system?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true
    username: root
    password: your_password
```

### 4. 執行應用
```bash
mvn spring-boot:run
```

### 5. 訪問應用
- **API 文檔**: http://localhost:8090/api/swagger-ui.html
- **健康檢查**: http://localhost:8090/api/actuator/health
- **測試端點**: http://localhost:8090/api/test/hello

## 📚 API 文檔

應用啟動後，訪問 [Swagger UI](http://localhost:8090/api/swagger-ui.html) 查看完整的 API 文檔。

### 主要 API 端點

- `GET /api/test/hello` - 基本測試
- `GET /api/test/system-info` - 系統資訊
- `GET /api/test/user/{id}` - 獲取用戶（模擬）
- `POST /api/test/user` - 創建用戶（模擬）

### 異常測試端點

- `GET /api/test/exception/business` - 業務異常
- `GET /api/test/exception/user-not-found` - 用戶不存在
- `POST /api/test/exception/validation` - 參數驗證

## 🗄️ 資料庫設計

### 核心表格

- **user** - 用戶基本資訊
- **role** - 角色定義
- **permission** - 權限定義
- **user_role** - 用戶角色關聯
- **role_permission** - 角色權限關聯
- **member_level** - 會員等級
- **user_points** - 積分記錄
- **operation_log** - 操作日誌

### 特色設計

- ✅ **UUID 主鍵** - 分散式友好
- ✅ **邏輯刪除** - 軟刪除機制
- ✅ **無外鍵約束** - 微服務友好
- ✅ **自動時間戳** - 創建/更新時間

## 🧪 開發進度

### ✅ 已完成
- [x] 專案基礎建設 (Spring Boot + Maven)
- [x] 資料庫配置 (MySQL + MyBatis Plus)
- [x] 實體類設計 (Lombok + 註解)
- [x] Swagger 文檔配置
- [x] 全域異常處理機制

### 🚧 進行中
- [ ] 用戶管理 API
- [ ] 認證授權系統
- [ ] 權限管理系統

### 📅 計劃中
- [ ] 會員等級系統
- [ ] 積分系統
- [ ] 操作日誌記錄
- [ ] API 限流防護
- [ ] 單元測試
- [ ] 部署配置

## 🤝 貢獻指南

1. Fork 這個專案
2. 創建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打開一個 Pull Request

## 📄 授權條款

這個專案採用 MIT 授權條款 - 查看 [LICENSE](LICENSE) 檔案了解詳情。

## 📞 聯繫方式

- 專案連結: [https://github.com/your-username/member-system](https://github.com/your-username/member-system)
- 問題回報: [Issues](https://github.com/your-username/member-system/issues)

---

⭐ 如果這個專案對您有幫助，請給它一個星星！