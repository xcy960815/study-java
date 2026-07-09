ALTER TABLE `study_java_sys_user`
  MODIFY COLUMN `password_md5` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '' COMMENT '密码哈希，兼容历史MD5和BCrypt';
