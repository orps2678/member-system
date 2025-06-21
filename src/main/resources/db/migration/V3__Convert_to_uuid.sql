
-- 重新建立用戶表（UUID 版本）
DROP TABLE IF EXISTS `user_new`;
CREATE TABLE `user_new` (
    `id` VARCHAR(36) PRIMARY KEY COMMENT '用戶ID (UUID)',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用戶名',
    `email` VARCHAR(100) NOT NULL UNIQUE COMMENT '郵箱',
    `password` VARCHAR(255) NOT NULL COMMENT '加密密碼',
    `phone` VARCHAR(20) COMMENT '手機號碼',
    `nickname` VARCHAR(50) COMMENT '暱稱',
    `avatar` VARCHAR(255) COMMENT '頭像URL',
    `status` TINYINT DEFAULT 1 COMMENT '狀態: 1=正常, 0=禁用',
    `deleted` TINYINT DEFAULT 0 COMMENT '邏輯刪除: 0=未刪除, 1=已刪除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),
    INDEX `idx_phone` (`phone`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶表 (UUID)';

-- 重新建立角色表
DROP TABLE IF EXISTS `role_new`;
CREATE TABLE `role_new` (
    `id` VARCHAR(36) PRIMARY KEY COMMENT '角色ID (UUID)',
    `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名稱',
    `code` VARCHAR(50) NOT NULL UNIQUE COMMENT '角色代碼',
    `description` VARCHAR(200) COMMENT '角色描述',
    `status` TINYINT DEFAULT 1 COMMENT '狀態: 1=啟用, 0=禁用',
    `deleted` TINYINT DEFAULT 0 COMMENT '邏輯刪除: 0=未刪除, 1=已刪除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    INDEX `idx_code` (`code`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表 (UUID)';

-- 重新建立權限表
DROP TABLE IF EXISTS `permission_new`;
CREATE TABLE `permission_new` (
    `id` VARCHAR(36) PRIMARY KEY COMMENT '權限ID (UUID)',
    `name` VARCHAR(50) NOT NULL COMMENT '權限名稱',
    `code` VARCHAR(100) NOT NULL UNIQUE COMMENT '權限代碼',
    `resource` VARCHAR(200) COMMENT '資源路徑',
    `method` VARCHAR(10) COMMENT 'HTTP方法',
    `parent_id` VARCHAR(36) DEFAULT NULL COMMENT '父權限ID (UUID)',
    `type` TINYINT DEFAULT 1 COMMENT '類型: 1=菜單, 2=按鈕, 3=API',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '狀態: 1=啟用, 0=禁用',
    `deleted` TINYINT DEFAULT 0 COMMENT '邏輯刪除: 0=未刪除, 1=已刪除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    INDEX `idx_code` (`code`),
    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='權限表 (UUID)';

-- 用戶角色關聯表（無外鍵約束）
DROP TABLE IF EXISTS `user_role_new`;
CREATE TABLE `user_role_new` (
    `id` VARCHAR(36) PRIMARY KEY COMMENT '關聯ID (UUID)',
    `user_id` VARCHAR(36) NOT NULL COMMENT '用戶ID',
    `role_id` VARCHAR(36) NOT NULL COMMENT '角色ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶角色關聯表 (UUID, 無FK)';

-- 角色權限關聯表（無外鍵約束）
DROP TABLE IF EXISTS `role_permission_new`;
CREATE TABLE `role_permission_new` (
    `id` VARCHAR(36) PRIMARY KEY COMMENT '關聯ID (UUID)',
    `role_id` VARCHAR(36) NOT NULL COMMENT '角色ID',
    `permission_id` VARCHAR(36) NOT NULL COMMENT '權限ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色權限關聯表 (UUID, 無FK)';

-- 刪除舊表，重命名新表
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `permission`;
DROP TABLE IF EXISTS `user_role`;
DROP TABLE IF EXISTS `role_permission`;

RENAME TABLE `user_new` TO `user`;
RENAME TABLE `role_new` TO `role`;
RENAME TABLE `permission_new` TO `permission`;
RENAME TABLE `user_role_new` TO `user_role`;
RENAME TABLE `role_permission_new` TO `role_permission`;