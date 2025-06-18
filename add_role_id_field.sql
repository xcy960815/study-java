-- 为 study_java_sys_user 表添加 role_id 字段
ALTER TABLE study_java_sys_user ADD COLUMN role_id BIGINT COMMENT '角色ID' AFTER id;

-- 为 role_id 字段添加索引（可选）
-- ALTER TABLE study_java_sys_user ADD INDEX idx_role_id (role_id);

-- 如果需要设置默认值，可以执行以下语句
-- UPDATE study_java_sys_user SET role_id = 1 WHERE role_id IS NULL; 