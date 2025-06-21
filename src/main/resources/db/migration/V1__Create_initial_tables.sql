-- 會員管理系統初始資料表

-- 用戶表
CREATE TABLE `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用戶ID',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶表';

-- 角色表
CREATE TABLE `role` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    `name` VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名稱',
    `code` VARCHAR(50) NOT NULL UNIQUE COMMENT '角色代碼',
    `description` VARCHAR(200) COMMENT '角色描述',
    `status` TINYINT DEFAULT 1 COMMENT '狀態: 1=啟用, 0=禁用',
    `deleted` TINYINT DEFAULT 0 COMMENT '邏輯刪除: 0=未刪除, 1=已刪除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    INDEX `idx_code` (`code`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 權限表
CREATE TABLE `permission` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '權限ID',
    `name` VARCHAR(50) NOT NULL COMMENT '權限名稱',
    `code` VARCHAR(100) NOT NULL UNIQUE COMMENT '權限代碼',
    `resource` VARCHAR(200) COMMENT '資源路徑',
    `method` VARCHAR(10) COMMENT 'HTTP方法',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父權限ID',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='權限表';

-- 用戶角色關聯表
CREATE TABLE `user_role` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '關聯ID',
    `user_id` BIGINT NOT NULL COMMENT '用戶ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role_id` (`role_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`role_id`) REFERENCES `role`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶角色關聯表';

-- 角色權限關聯表
CREATE TABLE `role_permission` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '關聯ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '權限ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    INDEX `idx_role_id` (`role_id`),
    INDEX `idx_permission_id` (`permission_id`),
    FOREIGN KEY (`role_id`) REFERENCES `role`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`permission_id`) REFERENCES `permission`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色權限關聯表';

-- 會員等級表
CREATE TABLE `member_level` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '等級ID',
    `name` VARCHAR(50) NOT NULL COMMENT '等級名稱',
    `level` INT NOT NULL UNIQUE COMMENT '等級數值',
    `min_points` BIGINT DEFAULT 0 COMMENT '最小積分要求',
    `max_points` BIGINT COMMENT '最大積分限制',
    `discount_rate` DECIMAL(4,2) DEFAULT 1.00 COMMENT '折扣率',
    `description` VARCHAR(200) COMMENT '等級描述',
    `status` TINYINT DEFAULT 1 COMMENT '狀態: 1=啟用, 0=禁用',
    `deleted` TINYINT DEFAULT 0 COMMENT '邏輯刪除: 0=未刪除, 1=已刪除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
    INDEX `idx_level` (`level`),
    INDEX `idx_points_range` (`min_points`, `max_points`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='會員等級表';

-- 用戶積分記錄表
CREATE TABLE `user_points` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '記錄ID',
    `user_id` BIGINT NOT NULL COMMENT '用戶ID',
    `points` BIGINT NOT NULL COMMENT '積分變動數量 (正數為增加，負數為減少)',
    `balance` BIGINT NOT NULL COMMENT '變動後積分餘額',
    `type` TINYINT NOT NULL COMMENT '積分類型: 1=簽到, 2=消費, 3=兌換, 4=系統調整',
    `description` VARCHAR(200) COMMENT '積分變動描述',
    `ref_id` VARCHAR(100) COMMENT '關聯業務ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_create_time` (`create_time`),
    INDEX `idx_ref_id` (`ref_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用戶積分記錄表';

-- 操作日誌表
CREATE TABLE `operation_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日誌ID',
    `user_id` BIGINT COMMENT '操作用戶ID',
    `username` VARCHAR(50) COMMENT '操作用戶名',
    `operation` VARCHAR(50) NOT NULL COMMENT '操作類型',
    `method` VARCHAR(200) COMMENT '操作方法',
    `params` TEXT COMMENT '請求參數',
    `result` TEXT COMMENT '操作結果',
    `ip` VARCHAR(45) COMMENT 'IP地址',
    `user_agent` VARCHAR(500) COMMENT '用戶代理',
    `execution_time` BIGINT COMMENT '執行時長(毫秒)',
    `status` TINYINT DEFAULT 1 COMMENT '操作狀態: 1=成功, 0=失敗',
    `error_msg` TEXT COMMENT '錯誤信息',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_operation` (`operation`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`),
    INDEX `idx_ip` (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日誌表';