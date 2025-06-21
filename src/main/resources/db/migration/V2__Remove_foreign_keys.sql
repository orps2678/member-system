-- 移除 user_role 表的外鍵
ALTER TABLE `user_role` DROP FOREIGN KEY user_role_ibfk_1;
ALTER TABLE `user_role` DROP FOREIGN KEY user_role_ibfk_2;

-- 移除 role_permission 表的外鍵
ALTER TABLE `role_permission` DROP FOREIGN KEY role_permission_ibfk_1;
ALTER TABLE `role_permission` DROP FOREIGN KEY role_permission_ibfk_2;

-- 移除 user_points 表的外鍵
ALTER TABLE `user_points` DROP FOREIGN KEY user_points_ibfk_1;