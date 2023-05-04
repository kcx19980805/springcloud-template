/*
 Navicat Premium Data Transfer

 Source Server         : LocalMysql_Windows
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : springcloud-template2

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 04/05/2023 10:34:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for client_user
-- ----------------------------
DROP TABLE IF EXISTS `client_user`;
CREATE TABLE `client_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `oid` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '分布式唯一id（分库分表后唯一）',
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '123456' COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '盐',
  `head_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'http://kcxbucket.oss-cn-shenzhen.aliyuncs.com/user1608771586000.png' COMMENT '头像',
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '昵称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态  0：正常   1：禁用',
  `is_del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否删除  0正常,  1删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_oid`(`oid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户账号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of client_user
-- ----------------------------
INSERT INTO `client_user` VALUES (1, '1653342883341795328', '17365560614', '3e87ceed7301da94f0844dc54dccc2ce65a47318cf6a144f3a50c1711b186bc9', 'ZhZNzsnbOytuTXIsriWS', 'http://kcxbucket.oss-cn-shenzhen.aliyuncs.com/user1608771586000.png', 'aaaa', '', '2023-05-02 18:17:15', '2023-05-03 23:08:36', '0', '0');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `oid` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '分布式唯一id（分库分表后唯一）',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `is_default` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否默认（0是 1否）',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建者id(sys_user表id)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_user_id` bigint NULL DEFAULT NULL COMMENT '更新者id(sys_user表id)',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_oid`(`oid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, '1213115615', 0, '系统折扣', '1', 'discount', '0', '0', NULL, '2022-04-24 22:08:53', NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (2, '6545465454', 0, '订单抽成', '0.1', 'extract', '0', '0', NULL, '2022-04-24 22:11:53', NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (3, '156454464546', 0, '订单抽成2', '0.1', 'extract', '0', '0', NULL, '2022-04-24 22:11:53', NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (4, '6545465454666', 0, '订单抽成3', '0.1', 'extract', '0', '0', NULL, '2022-04-24 22:11:53', NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (5, '65454654547565', 0, '订单抽成4', '0.1', 'extract', '0', '0', NULL, '2022-04-24 22:11:53', NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (6, '6545465454636363', 0, '订单抽成5', '0.1', 'extract', '0', '0', NULL, '2022-04-24 22:11:53', NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (7, '654546545445646', 0, '订单抽成6', '0.1', 'extract', '0', '0', NULL, '2022-04-24 22:11:53', NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (8, '654546545412345', 0, '订单抽成7', '0.1', 'extract', '0', '0', NULL, '2022-04-24 22:11:53', NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (9, '65454654544546', 0, '订单抽成8', '0.1', 'extract', '0', '0', NULL, '2022-04-24 22:11:53', NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (10, '654546545431345', 0, '订单抽成9', '0.1', 'extract', '0', '0', NULL, '2022-04-24 22:11:53', NULL, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (11, '654546545475567678', 0, '订单抽成10', '0.1', 'extract', '0', '0', NULL, '2022-04-24 22:11:53', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `oid` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '分布式唯一id（分库分表后唯一）',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `operUserId` bigint NULL DEFAULT 0 COMMENT '操作人员账号id',
  `operUserAccount` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作人员账号',
  `operUserType` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '操作人员类型(0平台 1分公司 2厂家 3用户)',
  `oper_ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_param` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '返回参数',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时长(毫秒)',
  `status` int NULL DEFAULT 0 COMMENT '操作状态（0正常/成功 1异常/错误）',
  `error_msg` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_oid`(`oid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `oid` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '分布式唯一id（分库分表后唯一）',
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '123456' COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '盐',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态  0：正常   1：禁用',
  `is_del` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否删除  0正常,  1删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_oid`(`oid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员账号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '1653323124327972864', 'admin2', '5c2482104fc39808862c70d900685b626e1e7cfdf8b641d6daa508a6e15efd5c', 'BfiLoFBKqbVRtGvzmTFx', '2023-05-02 16:57:43', '2023-05-04 10:02:19', '0', '0');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'seata分布式事务重做日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
