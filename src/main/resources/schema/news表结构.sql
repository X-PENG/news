/*
 Navicat Premium Data Transfer

 Source Server         : 云服务器mysql_PENG
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : 47.99.61.90:3306
 Source Schema         : news_stage

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 21/10/2021 23:55:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `title` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '新闻标题',
  `article_fragment_for_show` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文章部分片段，用于新闻列表显示每个新闻的部分片段',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '新闻正文',
  `column_id` int(0) NULL DEFAULT NULL COMMENT '外键，新闻栏目id，新闻发布时必须选择新闻栏目',
  `img_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片来源',
  `article_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文字来源',
  `news_status` tinyint(0) NOT NULL COMMENT '新闻状态。0-草稿，1-上传成功（备注：属于中转新闻），2-审核中，3-审核失败（备注：属于中转新闻），4-审核成功，待发布，5-已发布，6-撤销发布，7-打回修改（备注：属于中转新闻）',
  `is_carousel` tinyint(0) NOT NULL DEFAULT 0 COMMENT '新闻是否轮播发布，默认false',
  `set_carousel_time` datetime(0) NULL DEFAULT NULL COMMENT '新闻设置轮播的时机。如果很多新闻都设为轮播，则轮播顺序由该字段决定',
  `is_headlines` tinyint(0) NOT NULL DEFAULT 0 COMMENT '新闻是否作为头条，默认false',
  `set_headlines_time` datetime(0) NULL DEFAULT NULL COMMENT '新闻设置为头条的时机。如果很多新闻都设为头条，则谁作为头条由该字段决定',
  `is_image_news` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否是图片新闻',
  `is_top` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否置顶。表示是否在所在的新闻栏目置顶',
  `set_top_time` datetime(0) NULL DEFAULT NULL COMMENT '新闻设置置顶的时机。如果一个栏目多个新闻都设置了置顶，就按该时间决定先后顺序',
  `external_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外网链接，如果新闻是其他网站的链接，则设置外链',
  `inputter_id` int(0) NOT NULL COMMENT '外键，新闻稿的录入人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新闻的创建时间',
  `complete_input_time` datetime(0) NULL DEFAULT NULL COMMENT '新闻录入完成的时间',
  `editors` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与编辑的人员列表，逗号分隔',
  `reviewers` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参与审核的人员列表，逗号分隔',
  `submitter_id` int(0) NULL DEFAULT NULL COMMENT '外键，提交审核的人，即送审人id',
  `submit_time` datetime(0) NULL DEFAULT NULL COMMENT '送审时间',
  `latest_editor_id` int(0) NULL DEFAULT NULL COMMENT '外键，最近修改新闻的用户id，为了让编辑能够知道最近谁修改了新闻',
  `latest_edit_time` datetime(0) NULL DEFAULT NULL COMMENT '最近修改时间',
  `current_review_epoch` tinyint(0) NOT NULL DEFAULT 0 COMMENT '新闻当前所处的审核轮次',
  `previous_epoch_review_pass_time` datetime(0) NULL DEFAULT NULL COMMENT '当新闻通过一轮审核，就设置该时间，表示这一轮审核通过的时间，下一轮审核的人就可以看到；如果新闻处于待发布状态，则表示新闻通过终审的时间',
  `publisher_id` int(0) NULL DEFAULT NULL COMMENT '外键，新闻发布人的id',
  `real_pub_time` datetime(0) NULL DEFAULT NULL COMMENT '真实的发布时间',
  `show_pub_time` datetime(0) NULL DEFAULT NULL COMMENT '对外显示的发布时间',
  `init_reading_count` int(0) NOT NULL DEFAULT 0 COMMENT '新闻初始阅读量',
  `img_for_show_on_news_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前新闻显示在新闻列表上的图片',
  `real_reading_count` int(0) NOT NULL DEFAULT 0 COMMENT '新闻实际阅读量',
  `extra` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储额外的信息（json格式）。比如，审核失败时，保存审核意见；新闻作为轮播图发布时，保存封面图片地址；新闻被打回修改时，保存修改意见',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for news_column
-- ----------------------------
DROP TABLE IF EXISTS `news_column`;
CREATE TABLE `news_column`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '新闻栏目名称',
  `description` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对栏目的描述',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '父栏目id',
  `external_link` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '栏目链接。如果新闻栏目是其他网站，则可以设置栏目链接用于跳转',
  `is_has_children` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否有子栏目。该字段是为element-ui的树形表格设计的，当is_has_children为true时，就可以点击展开子栏目表格',
  `enabled` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否开启该栏目;0-false,1-true',
  `menu_order` tinyint(0) NOT NULL DEFAULT 127 COMMENT '菜单显示顺序，默认有个最大值，用于排序',
  `module_order` tinyint(0) NULL DEFAULT NULL COMMENT '模块序号，决定在首页哪个模块上显示该栏目新闻列表，为null则不在首页上显示新闻列表',
  `show_img_on_the_right` tinyint(0) NOT NULL DEFAULT 0 COMMENT '用于设置栏目列表的新闻图片是否在右边显示；默认false，左边显示图片',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_title`(`title`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '新闻栏目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url_pattern，也就是权限管理的对象（资源）；如果菜单不绑定资源，就为null',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '绑定的菜单名（因为资源和菜单一对一绑定了）',
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '绑定的菜单对应的路由路径',
  `component` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '绑定的菜单对应的组件',
  `icon_cls` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标class',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '父菜单id',
  `enabled` tinyint(0) NOT NULL DEFAULT 1 COMMENT '是否启用菜单',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源描述',
  `sub_menu` tinyint(0) NULL DEFAULT 0 COMMENT '是否是子菜单，该字段作为路由对象的meta信息。该字段是为前端面包屑功能服务的，对于子菜单，面包屑上不允许点击跳转',
  `hidden` tinyint(0) NOT NULL DEFAULT 0 COMMENT '1:则该路由不作为侧边栏菜单；0:表示该路由将作为侧边栏菜单显示',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_unique`(`name`) USING BTREE,
  UNIQUE INDEX `url_unique`(`url`) USING BTREE,
  INDEX `parent_id_index`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源和路由的合并表，菜单根据路由表生成（由于很多路由的目标组件上都会绑定一些后端的接口资源，所以进行了合并）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name_en` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色英文名',
  `name_zh` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色中文名',
  `description` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `is_system_admin` tinyint(0) NOT NULL DEFAULT 0 COMMENT '是否是系统管理员',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_zh_unique`(`name_zh`) USING BTREE,
  UNIQUE INDEX `name_en_unique`(`name_en`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role_resource
-- ----------------------------
DROP TABLE IF EXISTS `role_resource`;
CREATE TABLE `role_resource`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `role_id` int(0) NOT NULL,
  `resource_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role_resource_unique`(`role_id`, `resource_id`) USING BTREE,
  INDEX `role_id_index`(`role_id`) USING BTREE,
  INDEX `resource_id_index`(`resource_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 351 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色_资源对应表，表示角色能访问哪些资源（包含父子资源）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `review_level` tinyint(0) NOT NULL DEFAULT 3 COMMENT '几轮审核',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `real_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `passwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '加密后的密码',
  `gender` tinyint(0) NOT NULL COMMENT '0-女，1-男',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户备注',
  `locked` tinyint(0) NOT NULL DEFAULT 0 COMMENT '用户是否锁定；0-false;1-true',
  `deleted` tinyint(0) NOT NULL DEFAULT 0 COMMENT '用户是否被删除;0-false,1-true',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username_unique`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL,
  `role_id` int(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for role_resource_view
-- ----------------------------
DROP VIEW IF EXISTS `role_resource_view`;
CREATE ALGORITHM = UNDEFINED DEFINER = `PENG`@`%` SQL SECURITY DEFINER VIEW `role_resource_view` AS select `t1`.`role_id` AS `role_id`,`t2`.`id` AS `id`,`t2`.`url` AS `url`,`t2`.`name` AS `name`,`t2`.`path` AS `path`,`t2`.`component` AS `component`,`t2`.`icon_cls` AS `icon_cls`,`t2`.`parent_id` AS `parent_id`,`t2`.`enabled` AS `enabled`,`t2`.`description` AS `description` from (`role_resource` `t1` join `resource` `t2` on((`t1`.`resource_id` = `t2`.`id`)));

SET FOREIGN_KEY_CHECKS = 1;
