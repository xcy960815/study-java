-- 商品分类。可在已有库重复执行：已存在的分类主键会被忽略，已经手工指定分类的商品不会被改写。

CREATE TABLE IF NOT EXISTS `study_java_goods_category` (
  `category_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '上级分类ID，0为顶级',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `category_level` int NOT NULL DEFAULT '1' COMMENT '层级，从1开始',
  `order_num` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0未删除，1已删除',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`category_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类';

INSERT IGNORE INTO `study_java_goods_category`
  (`category_id`, `parent_id`, `category_name`, `category_level`, `order_num`, `create_by`)
VALUES
  (1, 0, '护肤', 1, 1, 'system'),
  (2, 0, '文具', 1, 2, 'system'),
  (3, 0, '服饰', 1, 3, 'system'),
  (4, 0, '家居', 1, 4, 'system'),
  (5, 0, '数码', 1, 5, 'system'),
  (11, 1, '化妆水', 2, 1, 'system'),
  (12, 1, '乳液', 2, 2, 'system'),
  (13, 1, '洁面', 2, 3, 'system'),
  (14, 1, '彩妆', 2, 4, 'system'),
  (21, 2, '书写', 2, 1, 'system'),
  (22, 2, '收纳', 2, 2, 'system'),
  (23, 2, '修正', 2, 3, 'system'),
  (31, 3, '睡衣', 2, 1, 'system'),
  (32, 3, '上衣', 2, 2, 'system'),
  (41, 4, '香薰', 2, 1, 'system'),
  (42, 4, '日用', 2, 2, 'system'),
  (51, 5, '手机', 2, 1, 'system'),
  (52, 5, '电脑', 2, 2, 'system'),
  (53, 5, '耳机', 2, 3, 'system');

UPDATE `study_java_goods`
SET `goods_category_id` = CASE
  WHEN `goods_name` LIKE '%睡衣%' THEN 31
  WHEN `goods_name` LIKE '%T恤%' THEN 32
  WHEN `goods_name` LIKE '%耳机%' OR `goods_name` LIKE '%AirPods%' OR `goods_name` LIKE '%EarPods%'
    OR `goods_name` LIKE '%Beats%' OR `goods_name` LIKE '%Bose%' OR `goods_name` LIKE '%索尼%'
    OR `goods_name` LIKE '%雷蛇%' OR `goods_name` LIKE '%森海塞尔%' THEN 53
  WHEN `goods_name` LIKE '%iPhone%' OR `goods_name` LIKE '%iPhonex%' OR `goods_name` LIKE '%手机%'
    OR `goods_name` LIKE '%华为%' OR `goods_name` LIKE '%荣耀%' OR `goods_name` LIKE '%Redmi%'
    OR `goods_name` LIKE '%小米%' OR `goods_name` LIKE '%红米%' OR `goods_name` LIKE '%畅享%'
    OR `goods_name` LIKE '%Mate%' OR `goods_name` LIKE '%nova%' THEN 51
  WHEN `goods_name` LIKE '%MacBook%' OR `goods_name` LIKE '%Macbook%' THEN 52
  WHEN `goods_name` LIKE '%化妆水%' OR `goods_name` LIKE '%化妆液%' THEN 11
  WHEN `goods_name` LIKE '%乳液%' THEN 12
  WHEN `goods_name` LIKE '%洁面%' THEN 13
  WHEN `goods_name` LIKE '%乳霜%' OR `goods_name` LIKE '%遮瑕%' OR `goods_name` LIKE '%唇膏%'
    OR `goods_name` LIKE '%口红%' OR `goods_name` LIKE '%散粉%' OR `goods_name` LIKE '%粉底%'
    OR `goods_name` LIKE '%隔离%' THEN 14
  WHEN `goods_name` LIKE '%修正带%' THEN 23
  WHEN `goods_name` LIKE '%圆珠笔%' OR `goods_name` LIKE '%毛笔%' OR `goods_name` LIKE '%荧光笔%'
    OR `goods_name` LIKE '%橡皮%' OR `goods_name` LIKE '%笔记%' OR `goods_name` LIKE '%订书%'
    OR `goods_name` LIKE '%笔芯%' OR `goods_name` LIKE '%铅笔%' OR `goods_name` LIKE '%碎纸%'
    OR `goods_name` LIKE '%板夹%' THEN 21
  WHEN `goods_name` LIKE '%笔盒%' OR `goods_name` LIKE '%化妆盒%' OR `goods_name` LIKE '%聚丙烯%' THEN 22
  WHEN `goods_name` LIKE '%香薰%' OR `goods_name` LIKE '%分装%' OR `goods_name` LIKE '%分裝%'
    OR `goods_name` LIKE '%香/%' THEN 41
  WHEN `goods_name` LIKE '%马桶刷%' OR `goods_name` LIKE '%玻璃%' OR `goods_name` LIKE '%指甲刀%'
    OR `goods_name` LIKE '%靠垫%' OR `goods_name` LIKE '%卷尺%' OR `goods_name` LIKE '%铝制%' THEN 42
  WHEN `goods_name` LIKE '%女式%' OR `goods_name` LIKE '%男式%' THEN 32
  ELSE `goods_category_id`
END
WHERE `is_deleted` = 0
  AND (`goods_category_id` IS NULL OR `goods_category_id` = 0);

INSERT INTO `study_java_sys_menu`
  (`id`, `parent_id`, `menu_name`, `path`, `component`, `icon`, `menu_type`, `perms`, `order_num`, `is_deleted`, `create_by`, `remark`)
SELECT 22, 5, '商品分类', '/goods/category', 'views/goods/category/index.vue', 'ListView', 1, 'goods:query', 1, 0, 'system', '商品分类'
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM `study_java_sys_menu` WHERE `id` = 22 OR `path` = '/goods/category'
);

INSERT INTO `study_java_sys_role_menus` (`role_id`, `menu_id`)
SELECT 100, 22 FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM `study_java_sys_role_menus` WHERE `role_id` = 100 AND `menu_id` = 22
);

INSERT INTO `study_java_sys_role_menus` (`role_id`, `menu_id`)
SELECT 101, 22 FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM `study_java_sys_role_menus` WHERE `role_id` = 101 AND `menu_id` = 22
);
