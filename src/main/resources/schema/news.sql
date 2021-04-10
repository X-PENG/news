CREATE TABLE `news_column` (
    `id` int NOT NULL AUTO_INCREMENT,
    `title` varchar(20) NOT NULL COMMENT '新闻栏目名称',
    `description` varchar(50) DEFAULT NULL COMMENT '对栏目的描述',
    `menu_order` tinyint NOT NULL DEFAULT '127' COMMENT '菜单显示顺序，默认有个最大值，用于排序',
    `module_order` tinyint DEFAULT NULL COMMENT '模块序号，决定在首页哪个模块上显示该栏目新闻列表，为null则不在首页上显示新闻列表',
    `parent_id` int DEFAULT NULL COMMENT '父栏目id',
    `enabled` tinyint NOT NULL DEFAULT '1' COMMENT '是否开启该栏目;0-false,1-true',
    `external_link` varchar(100) DEFAULT NULL COMMENT '栏目链接。如果新闻栏目是其他网站，则可以设置栏目链接用于跳转',
    `is_has_children` tinyint NOT NULL DEFAULT '0' COMMENT '是否有子栏目',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_title` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新闻栏目表';

INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (1, '要闻聚焦', NULL, 1, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', '2021-04-09 23:19:36');
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (2, '信息公告', NULL, 2, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (3, '教学资讯', NULL, 3, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (4, '科研动态', NULL, 4, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (5, '学生天地', NULL, 5, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (6, '缤纷校园', NULL, 6, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', '2021-04-10 12:11:22');
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (7, '专题报道', NULL, 7, NULL, NULL, 1, NULL, 1, '2021-04-09 15:17:58', '2021-04-09 15:49:17');
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (8, '媒体校园', NULL, 8, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (9, '交流合作', NULL, 9, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (10, '人物风采', NULL, 10, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (11, '校友动态', NULL, 11, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (12, '校史钩沉', NULL, 12, NULL, NULL, 1, NULL, 0, '2021-04-09 15:17:58', NULL);
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (13, '众志成城 抗击疫情', NULL, 1, NULL, 7, 1, NULL, 0, '2021-04-09 15:37:06', '2021-04-09 23:20:18');
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (14, '不忘初心 牢记使命', NULL, 2, NULL, 7, 1, NULL, 0, '2021-04-09 15:37:06', '2021-04-09 23:20:21');
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (15, '文明校园建设', NULL, 3, NULL, 7, 1, NULL, 0, '2021-04-09 15:37:06', '2021-04-09 23:20:19');
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (16, '40周年专题', NULL, 4, NULL, 7, 1, 'http://news.jxufe.edu.cn/news-list-40znzt.html', 0, '2021-04-09 15:37:06', '2021-04-10 00:08:53');
INSERT INTO `news_column`(`id`, `title`, `description`, `menu_order`, `module_order`, `parent_id`, `enabled`, `external_link`, `is_has_children`, `create_time`, `update_time`) VALUES (17, '校园光影', '记录校园的美好景色', 1, NULL, 7, 1, NULL, 0, '2021-04-10 11:41:15', '2021-04-10 12:11:22');

#----------------------------------------------------------------------------------------
#----------------------------------------------------------------------------------------
