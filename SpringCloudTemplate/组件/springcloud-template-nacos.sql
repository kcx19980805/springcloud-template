/*
 Navicat Premium Data Transfer

 Source Server         : LocalMysql_Windows
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : localhost:3306
 Source Schema         : springcloud-template-nacos

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 04/05/2023 00:31:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'api-admin-sentinel', 'DEFAULT_GROUP', '[\n    {\n        \"resource\": \"com.kcx.api.admin.sys.controller.SysUserController:userLogin(com.kcx.api.admin.sys.requestVo.ReqUserLoginVO)\",\n        \"limitApp\": \"default\",\n        \"grade\": 1,\n        \"count\": 1,\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]', 'd9dba160ce346b0df3b7b206948bfc35', '2023-05-03 11:00:06', '2023-05-03 11:07:16', NULL, '0:0:0:0:0:0:0:1', '', '', 'api-admin项目sentinel限流持久配置', '', '', 'json', '');
INSERT INTO `config_info` VALUES (161, 'transport.type', 'SEATA_GROUP', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2023-05-03 15:34:35', '2023-05-03 15:34:35', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (162, 'transport.server', 'SEATA_GROUP', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2023-05-03 15:34:35', '2023-05-03 15:34:35', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (163, 'transport.heartbeat', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 15:34:35', '2023-05-03 15:34:35', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (164, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 15:34:35', '2023-05-03 15:34:35', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (165, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2023-05-03 15:34:35', '2023-05-03 15:34:35', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (166, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (167, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (168, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (169, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (170, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (171, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (172, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (173, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (174, 'transport.shutdown.wait', 'SEATA_GROUP', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (175, 'service.vgroupMapping.kcx_tx_group', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (176, 'service.default.grouplist', 'SEATA_GROUP', '127.0.0.1:8091', 'c32ce0d3e264525dcdada751f98143a3', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (177, 'service.enableDegrade', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (178, 'service.disableGlobalTransaction', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (179, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '10000', 'b7a782741f667201b54880c925faec4b', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (180, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (181, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (182, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (183, 'client.rm.reportRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (184, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (185, 'client.rm.sqlParserType', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (186, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (187, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 15:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (188, 'client.tm.commitRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (189, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (190, 'client.tm.degradeCheck', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (191, 'client.tm.degradeCheckAllowTimes', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (192, 'client.tm.degradeCheckPeriod', 'SEATA_GROUP', '2000', '08f90c1a417155361a5c4b8d297e0d78', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (193, 'store.mode', 'SEATA_GROUP', 'db', 'd77d5e503ad1439f585ac494268b351b', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (194, 'store.file.dir', 'SEATA_GROUP', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (195, 'store.file.maxBranchSessionSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (196, 'store.file.maxGlobalSessionSize', 'SEATA_GROUP', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (197, 'store.file.fileWriteBufferCacheSize', 'SEATA_GROUP', '16384', 'c76fe1d8e08462434d800487585be217', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (198, 'store.file.flushDiskMode', 'SEATA_GROUP', 'async', '0df93e34273b367bb63bad28c94c78d5', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (199, 'store.file.sessionReloadReadSize', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (200, 'store.db.datasource', 'SEATA_GROUP', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (201, 'store.db.dbType', 'SEATA_GROUP', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (202, 'store.db.driverClassName', 'SEATA_GROUP', 'com.mysql.cj.jdbc.Driver', '33763409bb7f4838bde4fae9540433e4', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (203, 'store.db.url', 'SEATA_GROUP', 'jdbc:mysql://127.0.0.1:3306/springcloud-template-seata?serverTimezone=UTC', '3e0a78f589e1e4c850495e796b217c28', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (204, 'store.db.user', 'SEATA_GROUP', 'root', '63a9f0ea7bb98050796b649e85481845', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (205, 'store.db.password', 'SEATA_GROUP', '19980805kcx', 'fede0776869e3a0abd07f4187c990c56', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (206, 'store.db.minConn', 'SEATA_GROUP', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 15:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (207, 'store.db.maxConn', 'SEATA_GROUP', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (208, 'store.db.globalTable', 'SEATA_GROUP', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (209, 'store.db.branchTable', 'SEATA_GROUP', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (210, 'store.db.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (211, 'store.db.lockTable', 'SEATA_GROUP', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (212, 'store.db.maxWait', 'SEATA_GROUP', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (213, 'store.redis.host', 'SEATA_GROUP', '127.0.0.1', 'f528764d624db129b32c21fbca0cb8d6', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (214, 'store.redis.port', 'SEATA_GROUP', '6379', '92c3b916311a5517d9290576e3ea37ad', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (215, 'store.redis.maxConn', 'SEATA_GROUP', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (216, 'store.redis.minConn', 'SEATA_GROUP', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (217, 'store.redis.database', 'SEATA_GROUP', '0', 'cfcd208495d565ef66e7dff9f98764da', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (218, 'store.redis.password', 'SEATA_GROUP', 'null', '37a6259cc0c1dae299a7866489dff0bd', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (219, 'store.redis.queryLimit', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (220, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (221, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (222, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (223, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (224, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (225, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (226, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 15:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (227, 'client.undo.dataValidation', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (228, 'client.undo.logSerialization', 'SEATA_GROUP', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (229, 'client.undo.onlyCareUpdateColumns', 'SEATA_GROUP', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (230, 'server.undo.logSaveDays', 'SEATA_GROUP', '7', '8f14e45fceea167a5a36dedd4bea2543', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (231, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (232, 'client.undo.logTable', 'SEATA_GROUP', 'undo_log', '2842d229c24afe9e61437135e8306614', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (233, 'client.log.exceptionRate', 'SEATA_GROUP', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (234, 'transport.serialization', 'SEATA_GROUP', 'seata', 'b943081c423b9a5416a706524ee05d40', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (235, 'transport.compressor', 'SEATA_GROUP', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (236, 'metrics.enabled', 'SEATA_GROUP', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (237, 'metrics.registryType', 'SEATA_GROUP', 'compact', '7cf74ca49c304df8150205fc915cd465', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (238, 'metrics.exporterList', 'SEATA_GROUP', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);
INSERT INTO `config_info` VALUES (239, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2023-05-03 15:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'text', NULL);

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL,
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 1, 'api-admin-sentinel', 'DEFAULT_GROUP', '', '[\r\n    {\r\n        \"resource\": \"com.kcx.api.admin.sys.controller.SysUserController:userLogin(com.kcx.api.admin.sys.requestVo.ReqUserLoginVO)\",\r\n        \"limitApp\": \"default\",\r\n        \"grade\": 1,\r\n        \"count\": 1,\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0,\r\n        \"clusterMode\": false\r\n    }\r\n]', 'ebbc780987825f9d3c6d361000df258e', '2023-05-03 19:00:05', '2023-05-03 11:00:06', NULL, '0:0:0:0:0:0:0:1', 'I', '');
INSERT INTO `his_config_info` VALUES (1, 2, 'api-admin-sentinel', 'DEFAULT_GROUP', '', '[\r\n    {\r\n        \"resource\": \"com.kcx.api.admin.sys.controller.SysUserController:userLogin(com.kcx.api.admin.sys.requestVo.ReqUserLoginVO)\",\r\n        \"limitApp\": \"default\",\r\n        \"grade\": 1,\r\n        \"count\": 1,\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0,\r\n        \"clusterMode\": false\r\n    }\r\n]', 'ebbc780987825f9d3c6d361000df258e', '2023-05-03 19:07:15', '2023-05-03 11:07:16', NULL, '0:0:0:0:0:0:0:1', 'U', '');
INSERT INTO `his_config_info` VALUES (0, 3, 'transport.type', 'SEATA_GROUP', '', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2023-05-03 20:24:11', '2023-05-03 12:24:11', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 4, 'transport.server', 'SEATA_GROUP', '', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2023-05-03 20:24:11', '2023-05-03 12:24:11', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 5, 'transport.heartbeat', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 20:24:11', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 6, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:24:11', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 7, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', '', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2023-05-03 20:24:11', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 8, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', '', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2023-05-03 20:24:11', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 9, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', '', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2023-05-03 20:24:11', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 10, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:24:11', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 11, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', '', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2023-05-03 20:24:11', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 12, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 20:24:11', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 13, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', '', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2023-05-03 20:24:11', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 14, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 20:24:12', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 15, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', '', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-05-03 20:24:12', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 16, 'transport.shutdown.wait', 'SEATA_GROUP', '', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2023-05-03 20:24:12', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 17, 'service.vgroupMapping.my_test_tx_group', 'SEATA_GROUP', '', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-05-03 20:24:12', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 18, 'service.default.grouplist', 'SEATA_GROUP', '', '127.0.0.1:8091', 'c32ce0d3e264525dcdada751f98143a3', '2023-05-03 20:24:12', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 19, 'service.enableDegrade', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:24:12', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 20, 'service.disableGlobalTransaction', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:24:12', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 21, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '', '10000', 'b7a782741f667201b54880c925faec4b', '2023-05-03 20:24:12', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 22, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 20:24:12', '2023-05-03 12:24:12', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 23, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 24, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 25, 'client.rm.reportRetryCount', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 26, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 27, 'client.rm.sqlParserType', 'SEATA_GROUP', '', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 28, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 29, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 30, 'client.tm.commitRetryCount', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 31, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 32, 'client.tm.degradeCheck', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 33, 'client.tm.degradeCheckAllowTimes', 'SEATA_GROUP', '', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 20:24:12', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 34, 'client.tm.degradeCheckPeriod', 'SEATA_GROUP', '', '2000', '08f90c1a417155361a5c4b8d297e0d78', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 35, 'store.mode', 'SEATA_GROUP', '', 'file', '8c7dd922ad47494fc02c388e12c00eac', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 36, 'store.file.dir', 'SEATA_GROUP', '', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 37, 'store.file.maxBranchSessionSize', 'SEATA_GROUP', '', '16384', 'c76fe1d8e08462434d800487585be217', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 38, 'store.file.maxGlobalSessionSize', 'SEATA_GROUP', '', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 39, 'store.file.fileWriteBufferCacheSize', 'SEATA_GROUP', '', '16384', 'c76fe1d8e08462434d800487585be217', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 40, 'store.file.flushDiskMode', 'SEATA_GROUP', '', 'async', '0df93e34273b367bb63bad28c94c78d5', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 41, 'store.file.sessionReloadReadSize', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 42, 'store.db.datasource', 'SEATA_GROUP', '', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 43, 'store.db.dbType', 'SEATA_GROUP', '', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 44, 'store.db.driverClassName', 'SEATA_GROUP', '', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2023-05-03 20:24:13', '2023-05-03 12:24:13', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 45, 'store.db.url', 'SEATA_GROUP', '', 'jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true', 'cbb3bd573704f125fb4f2489208abaec', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 46, 'store.db.user', 'SEATA_GROUP', '', 'username', '14c4b06b824ec593239362517f538b29', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 47, 'store.db.password', 'SEATA_GROUP', '', 'password', '5f4dcc3b5aa765d61d8327deb882cf99', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 48, 'store.db.minConn', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 49, 'store.db.maxConn', 'SEATA_GROUP', '', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 50, 'store.db.globalTable', 'SEATA_GROUP', '', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 51, 'store.db.branchTable', 'SEATA_GROUP', '', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 52, 'store.db.queryLimit', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 53, 'store.db.lockTable', 'SEATA_GROUP', '', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 54, 'store.db.maxWait', 'SEATA_GROUP', '', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 55, 'store.redis.host', 'SEATA_GROUP', '', '127.0.0.1', 'f528764d624db129b32c21fbca0cb8d6', '2023-05-03 20:24:13', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 56, 'store.redis.port', 'SEATA_GROUP', '', '6379', '92c3b916311a5517d9290576e3ea37ad', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 57, 'store.redis.maxConn', 'SEATA_GROUP', '', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 58, 'store.redis.minConn', 'SEATA_GROUP', '', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 59, 'store.redis.database', 'SEATA_GROUP', '', '0', 'cfcd208495d565ef66e7dff9f98764da', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 60, 'store.redis.password', 'SEATA_GROUP', '', 'null', '37a6259cc0c1dae299a7866489dff0bd', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 61, 'store.redis.queryLimit', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 62, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 63, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 64, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 65, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 66, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-05-03 20:24:14', '2023-05-03 12:24:14', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 67, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-05-03 20:24:14', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 68, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:24:14', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 69, 'client.undo.dataValidation', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 20:24:14', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 70, 'client.undo.logSerialization', 'SEATA_GROUP', '', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2023-05-03 20:24:14', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 71, 'client.undo.onlyCareUpdateColumns', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 20:24:14', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 72, 'server.undo.logSaveDays', 'SEATA_GROUP', '', '7', '8f14e45fceea167a5a36dedd4bea2543', '2023-05-03 20:24:14', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 73, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2023-05-03 20:24:14', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 74, 'client.undo.logTable', 'SEATA_GROUP', '', 'undo_log', '2842d229c24afe9e61437135e8306614', '2023-05-03 20:24:14', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 75, 'client.log.exceptionRate', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 20:24:14', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 76, 'transport.serialization', 'SEATA_GROUP', '', 'seata', 'b943081c423b9a5416a706524ee05d40', '2023-05-03 20:24:14', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 77, 'transport.compressor', 'SEATA_GROUP', '', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2023-05-03 20:24:15', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 78, 'metrics.enabled', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:24:15', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 79, 'metrics.registryType', 'SEATA_GROUP', '', 'compact', '7cf74ca49c304df8150205fc915cd465', '2023-05-03 20:24:15', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 80, 'metrics.exporterList', 'SEATA_GROUP', '', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2023-05-03 20:24:15', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 81, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2023-05-03 20:24:15', '2023-05-03 12:24:15', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (3, 82, 'transport.type', 'SEATA_GROUP', '', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2023-05-03 20:30:56', '2023-05-03 12:30:56', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (4, 83, 'transport.server', 'SEATA_GROUP', '', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2023-05-03 20:30:56', '2023-05-03 12:30:56', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (5, 84, 'transport.heartbeat', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 20:30:56', '2023-05-03 12:30:56', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (6, 85, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:30:56', '2023-05-03 12:30:56', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (7, 86, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', '', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2023-05-03 20:30:56', '2023-05-03 12:30:56', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (8, 87, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', '', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2023-05-03 20:30:56', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (9, 88, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', '', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2023-05-03 20:30:56', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (10, 89, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:30:56', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (11, 90, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', '', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2023-05-03 20:30:56', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (12, 91, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 20:30:56', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (13, 92, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', '', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2023-05-03 20:30:56', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (14, 93, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 20:30:56', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (15, 94, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', '', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-05-03 20:30:56', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (16, 95, 'transport.shutdown.wait', 'SEATA_GROUP', '', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2023-05-03 20:30:56', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (17, 96, 'service.vgroupMapping.my_test_tx_group', 'SEATA_GROUP', '', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-05-03 20:30:56', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (18, 97, 'service.default.grouplist', 'SEATA_GROUP', '', '127.0.0.1:8091', 'c32ce0d3e264525dcdada751f98143a3', '2023-05-03 20:30:57', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (19, 98, 'service.enableDegrade', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:30:57', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (20, 99, 'service.disableGlobalTransaction', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:30:57', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (21, 100, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '', '10000', 'b7a782741f667201b54880c925faec4b', '2023-05-03 20:30:57', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (22, 101, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 20:30:57', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (23, 102, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-05-03 20:30:57', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (24, 103, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 20:30:57', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (25, 104, 'client.rm.reportRetryCount', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 20:30:57', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (26, 105, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:30:57', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (27, 106, 'client.rm.sqlParserType', 'SEATA_GROUP', '', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-05-03 20:30:57', '2023-05-03 12:30:57', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (28, 107, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:30:57', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (29, 108, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:30:57', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (30, 109, 'client.tm.commitRetryCount', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 20:30:57', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (31, 110, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 20:30:57', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (32, 111, 'client.tm.degradeCheck', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:30:57', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (33, 112, 'client.tm.degradeCheckAllowTimes', 'SEATA_GROUP', '', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 20:30:57', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (34, 113, 'client.tm.degradeCheckPeriod', 'SEATA_GROUP', '', '2000', '08f90c1a417155361a5c4b8d297e0d78', '2023-05-03 20:30:57', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (35, 114, 'store.mode', 'SEATA_GROUP', '', 'file', '8c7dd922ad47494fc02c388e12c00eac', '2023-05-03 20:30:57', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (36, 115, 'store.file.dir', 'SEATA_GROUP', '', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2', '2023-05-03 20:30:57', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (37, 116, 'store.file.maxBranchSessionSize', 'SEATA_GROUP', '', '16384', 'c76fe1d8e08462434d800487585be217', '2023-05-03 20:30:57', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (38, 117, 'store.file.maxGlobalSessionSize', 'SEATA_GROUP', '', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f', '2023-05-03 20:30:58', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (39, 118, 'store.file.fileWriteBufferCacheSize', 'SEATA_GROUP', '', '16384', 'c76fe1d8e08462434d800487585be217', '2023-05-03 20:30:58', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (40, 119, 'store.file.flushDiskMode', 'SEATA_GROUP', '', 'async', '0df93e34273b367bb63bad28c94c78d5', '2023-05-03 20:30:58', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (41, 120, 'store.file.sessionReloadReadSize', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 20:30:58', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (42, 121, 'store.db.datasource', 'SEATA_GROUP', '', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-05-03 20:30:58', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (43, 122, 'store.db.dbType', 'SEATA_GROUP', '', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2023-05-03 20:30:58', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (44, 123, 'store.db.driverClassName', 'SEATA_GROUP', '', 'com.mysql.jdbc.Driver', '683cf0c3a5a56cec94dfac94ca16d760', '2023-05-03 20:30:58', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (45, 124, 'store.db.url', 'SEATA_GROUP', '', 'jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true', 'cbb3bd573704f125fb4f2489208abaec', '2023-05-03 20:30:58', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (46, 125, 'store.db.user', 'SEATA_GROUP', '', 'username', '14c4b06b824ec593239362517f538b29', '2023-05-03 20:30:58', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (47, 126, 'store.db.password', 'SEATA_GROUP', '', 'password', '5f4dcc3b5aa765d61d8327deb882cf99', '2023-05-03 20:30:58', '2023-05-03 12:30:58', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (48, 127, 'store.db.minConn', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 20:30:58', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (49, 128, 'store.db.maxConn', 'SEATA_GROUP', '', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-05-03 20:30:58', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (50, 129, 'store.db.globalTable', 'SEATA_GROUP', '', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2023-05-03 20:30:58', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (51, 130, 'store.db.branchTable', 'SEATA_GROUP', '', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2023-05-03 20:30:58', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (52, 131, 'store.db.queryLimit', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 20:30:58', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (53, 132, 'store.db.lockTable', 'SEATA_GROUP', '', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2023-05-03 20:30:58', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (54, 133, 'store.db.maxWait', 'SEATA_GROUP', '', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2023-05-03 20:30:58', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (55, 134, 'store.redis.host', 'SEATA_GROUP', '', '127.0.0.1', 'f528764d624db129b32c21fbca0cb8d6', '2023-05-03 20:30:58', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (56, 135, 'store.redis.port', 'SEATA_GROUP', '', '6379', '92c3b916311a5517d9290576e3ea37ad', '2023-05-03 20:30:59', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (57, 136, 'store.redis.maxConn', 'SEATA_GROUP', '', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 20:30:59', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (58, 137, 'store.redis.minConn', 'SEATA_GROUP', '', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 20:30:59', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (59, 138, 'store.redis.database', 'SEATA_GROUP', '', '0', 'cfcd208495d565ef66e7dff9f98764da', '2023-05-03 20:30:59', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (60, 139, 'store.redis.password', 'SEATA_GROUP', '', 'null', '37a6259cc0c1dae299a7866489dff0bd', '2023-05-03 20:30:59', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (61, 140, 'store.redis.queryLimit', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 20:30:59', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (62, 141, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 20:30:59', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (63, 142, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 20:30:59', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (64, 143, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 20:30:59', '2023-05-03 12:30:59', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (65, 144, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 20:30:59', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (66, 145, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-05-03 20:30:59', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (67, 146, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-05-03 20:30:59', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (68, 147, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:30:59', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (69, 148, 'client.undo.dataValidation', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 20:30:59', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (70, 149, 'client.undo.logSerialization', 'SEATA_GROUP', '', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2023-05-03 20:30:59', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (71, 150, 'client.undo.onlyCareUpdateColumns', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 20:30:59', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (72, 151, 'server.undo.logSaveDays', 'SEATA_GROUP', '', '7', '8f14e45fceea167a5a36dedd4bea2543', '2023-05-03 20:30:59', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (73, 152, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2023-05-03 20:30:59', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (74, 153, 'client.undo.logTable', 'SEATA_GROUP', '', 'undo_log', '2842d229c24afe9e61437135e8306614', '2023-05-03 20:30:59', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (75, 154, 'client.log.exceptionRate', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 20:31:00', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (76, 155, 'transport.serialization', 'SEATA_GROUP', '', 'seata', 'b943081c423b9a5416a706524ee05d40', '2023-05-03 20:31:00', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (77, 156, 'transport.compressor', 'SEATA_GROUP', '', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2023-05-03 20:31:00', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (78, 157, 'metrics.enabled', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 20:31:00', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (79, 158, 'metrics.registryType', 'SEATA_GROUP', '', 'compact', '7cf74ca49c304df8150205fc915cd465', '2023-05-03 20:31:00', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (80, 159, 'metrics.exporterList', 'SEATA_GROUP', '', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2023-05-03 20:31:00', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (81, 160, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2023-05-03 20:31:00', '2023-05-03 12:31:00', NULL, '127.0.0.1', 'U', '');
INSERT INTO `his_config_info` VALUES (0, 161, 'transport.type', 'SEATA_GROUP', '', 'TCP', 'b136ef5f6a01d816991fe3cf7a6ac763', '2023-05-03 23:34:35', '2023-05-03 15:34:35', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 162, 'transport.server', 'SEATA_GROUP', '', 'NIO', 'b6d9dfc0fb54277321cebc0fff55df2f', '2023-05-03 23:34:35', '2023-05-03 15:34:35', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 163, 'transport.heartbeat', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 23:34:35', '2023-05-03 15:34:35', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 164, 'transport.enableClientBatchSendRequest', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 23:34:35', '2023-05-03 15:34:35', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 165, 'transport.threadFactory.bossThreadPrefix', 'SEATA_GROUP', '', 'NettyBoss', '0f8db59a3b7f2823f38a70c308361836', '2023-05-03 23:34:35', '2023-05-03 15:34:35', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 166, 'transport.threadFactory.workerThreadPrefix', 'SEATA_GROUP', '', 'NettyServerNIOWorker', 'a78ec7ef5d1631754c4e72ae8a3e9205', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 167, 'transport.threadFactory.serverExecutorThreadPrefix', 'SEATA_GROUP', '', 'NettyServerBizHandler', '11a36309f3d9df84fa8b59cf071fa2da', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 168, 'transport.threadFactory.shareBossWorker', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 169, 'transport.threadFactory.clientSelectorThreadPrefix', 'SEATA_GROUP', '', 'NettyClientSelector', 'cd7ec5a06541e75f5a7913752322c3af', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 170, 'transport.threadFactory.clientSelectorThreadSize', 'SEATA_GROUP', '', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 171, 'transport.threadFactory.clientWorkerThreadPrefix', 'SEATA_GROUP', '', 'NettyClientWorkerThread', '61cf4e69a56354cf72f46dc86414a57e', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 172, 'transport.threadFactory.bossThreadSize', 'SEATA_GROUP', '', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 173, 'transport.threadFactory.workerThreadSize', 'SEATA_GROUP', '', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 174, 'transport.shutdown.wait', 'SEATA_GROUP', '', '3', 'eccbc87e4b5ce2fe28308fd9f2a7baf3', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 175, 'service.vgroupMapping.kcx_tx_group', 'SEATA_GROUP', '', 'default', 'c21f969b5f03d33d43e04f8f136e7682', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 176, 'service.default.grouplist', 'SEATA_GROUP', '', '127.0.0.1:8091', 'c32ce0d3e264525dcdada751f98143a3', '2023-05-03 23:34:35', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 177, 'service.enableDegrade', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 178, 'service.disableGlobalTransaction', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 179, 'client.rm.asyncCommitBufferLimit', 'SEATA_GROUP', '', '10000', 'b7a782741f667201b54880c925faec4b', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 180, 'client.rm.lock.retryInterval', 'SEATA_GROUP', '', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 181, 'client.rm.lock.retryTimes', 'SEATA_GROUP', '', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 182, 'client.rm.lock.retryPolicyBranchRollbackOnConflict', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 183, 'client.rm.reportRetryCount', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 184, 'client.rm.tableMetaCheckEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 185, 'client.rm.sqlParserType', 'SEATA_GROUP', '', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 186, 'client.rm.reportSuccessEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 187, 'client.rm.sagaBranchRegisterEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 23:34:36', '2023-05-03 15:34:36', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 188, 'client.tm.commitRetryCount', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 23:34:36', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 189, 'client.tm.rollbackRetryCount', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 23:34:36', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 190, 'client.tm.degradeCheck', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 23:34:36', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 191, 'client.tm.degradeCheckAllowTimes', 'SEATA_GROUP', '', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 23:34:36', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 192, 'client.tm.degradeCheckPeriod', 'SEATA_GROUP', '', '2000', '08f90c1a417155361a5c4b8d297e0d78', '2023-05-03 23:34:36', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 193, 'store.mode', 'SEATA_GROUP', '', 'db', 'd77d5e503ad1439f585ac494268b351b', '2023-05-03 23:34:36', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 194, 'store.file.dir', 'SEATA_GROUP', '', 'file_store/data', '6a8dec07c44c33a8a9247cba5710bbb2', '2023-05-03 23:34:36', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 195, 'store.file.maxBranchSessionSize', 'SEATA_GROUP', '', '16384', 'c76fe1d8e08462434d800487585be217', '2023-05-03 23:34:36', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 196, 'store.file.maxGlobalSessionSize', 'SEATA_GROUP', '', '512', '10a7cdd970fe135cf4f7bb55c0e3b59f', '2023-05-03 23:34:36', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 197, 'store.file.fileWriteBufferCacheSize', 'SEATA_GROUP', '', '16384', 'c76fe1d8e08462434d800487585be217', '2023-05-03 23:34:36', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 198, 'store.file.flushDiskMode', 'SEATA_GROUP', '', 'async', '0df93e34273b367bb63bad28c94c78d5', '2023-05-03 23:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 199, 'store.file.sessionReloadReadSize', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 23:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 200, 'store.db.datasource', 'SEATA_GROUP', '', 'druid', '3d650fb8a5df01600281d48c47c9fa60', '2023-05-03 23:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 201, 'store.db.dbType', 'SEATA_GROUP', '', 'mysql', '81c3b080dad537de7e10e0987a4bf52e', '2023-05-03 23:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 202, 'store.db.driverClassName', 'SEATA_GROUP', '', 'com.mysql.cj.jdbc.Driver', '33763409bb7f4838bde4fae9540433e4', '2023-05-03 23:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 203, 'store.db.url', 'SEATA_GROUP', '', 'jdbc:mysql://127.0.0.1:3306/springcloud-template-seata?serverTimezone=UTC', '3e0a78f589e1e4c850495e796b217c28', '2023-05-03 23:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 204, 'store.db.user', 'SEATA_GROUP', '', 'root', '63a9f0ea7bb98050796b649e85481845', '2023-05-03 23:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 205, 'store.db.password', 'SEATA_GROUP', '', '19980805kcx', 'fede0776869e3a0abd07f4187c990c56', '2023-05-03 23:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 206, 'store.db.minConn', 'SEATA_GROUP', '', '5', 'e4da3b7fbbce2345d7772b0674a318d5', '2023-05-03 23:34:37', '2023-05-03 15:34:37', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 207, 'store.db.maxConn', 'SEATA_GROUP', '', '30', '34173cb38f07f89ddbebc2ac9128303f', '2023-05-03 23:34:37', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 208, 'store.db.globalTable', 'SEATA_GROUP', '', 'global_table', '8b28fb6bb4c4f984df2709381f8eba2b', '2023-05-03 23:34:37', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 209, 'store.db.branchTable', 'SEATA_GROUP', '', 'branch_table', '54bcdac38cf62e103fe115bcf46a660c', '2023-05-03 23:34:37', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 210, 'store.db.queryLimit', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 23:34:37', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 211, 'store.db.lockTable', 'SEATA_GROUP', '', 'lock_table', '55e0cae3b6dc6696b768db90098b8f2f', '2023-05-03 23:34:37', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 212, 'store.db.maxWait', 'SEATA_GROUP', '', '5000', 'a35fe7f7fe8217b4369a0af4244d1fca', '2023-05-03 23:34:37', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 213, 'store.redis.host', 'SEATA_GROUP', '', '127.0.0.1', 'f528764d624db129b32c21fbca0cb8d6', '2023-05-03 23:34:37', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 214, 'store.redis.port', 'SEATA_GROUP', '', '6379', '92c3b916311a5517d9290576e3ea37ad', '2023-05-03 23:34:37', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 215, 'store.redis.maxConn', 'SEATA_GROUP', '', '10', 'd3d9446802a44259755d38e6d163e820', '2023-05-03 23:34:37', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 216, 'store.redis.minConn', 'SEATA_GROUP', '', '1', 'c4ca4238a0b923820dcc509a6f75849b', '2023-05-03 23:34:37', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 217, 'store.redis.database', 'SEATA_GROUP', '', '0', 'cfcd208495d565ef66e7dff9f98764da', '2023-05-03 23:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 218, 'store.redis.password', 'SEATA_GROUP', '', 'null', '37a6259cc0c1dae299a7866489dff0bd', '2023-05-03 23:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 219, 'store.redis.queryLimit', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 23:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 220, 'server.recovery.committingRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 23:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 221, 'server.recovery.asynCommittingRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 23:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 222, 'server.recovery.rollbackingRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 23:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 223, 'server.recovery.timeoutRetryPeriod', 'SEATA_GROUP', '', '1000', 'a9b7ba70783b617e9998dc4dd82eb3c5', '2023-05-03 23:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 224, 'server.maxCommitRetryTimeout', 'SEATA_GROUP', '', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-05-03 23:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 225, 'server.maxRollbackRetryTimeout', 'SEATA_GROUP', '', '-1', '6bb61e3b7bce0931da574d19d1d82c88', '2023-05-03 23:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 226, 'server.rollbackRetryTimeoutUnlockEnable', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 23:34:38', '2023-05-03 15:34:38', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 227, 'client.undo.dataValidation', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 23:34:38', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 228, 'client.undo.logSerialization', 'SEATA_GROUP', '', 'jackson', 'b41779690b83f182acc67d6388c7bac9', '2023-05-03 23:34:38', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 229, 'client.undo.onlyCareUpdateColumns', 'SEATA_GROUP', '', 'true', 'b326b5062b2f0e69046810717534cb09', '2023-05-03 23:34:38', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 230, 'server.undo.logSaveDays', 'SEATA_GROUP', '', '7', '8f14e45fceea167a5a36dedd4bea2543', '2023-05-03 23:34:38', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 231, 'server.undo.logDeletePeriod', 'SEATA_GROUP', '', '86400000', 'f4c122804fe9076cb2710f55c3c6e346', '2023-05-03 23:34:38', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 232, 'client.undo.logTable', 'SEATA_GROUP', '', 'undo_log', '2842d229c24afe9e61437135e8306614', '2023-05-03 23:34:38', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 233, 'client.log.exceptionRate', 'SEATA_GROUP', '', '100', 'f899139df5e1059396431415e770c6dd', '2023-05-03 23:34:38', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 234, 'transport.serialization', 'SEATA_GROUP', '', 'seata', 'b943081c423b9a5416a706524ee05d40', '2023-05-03 23:34:38', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 235, 'transport.compressor', 'SEATA_GROUP', '', 'none', '334c4a4c42fdb79d7ebc3e73b517e6f8', '2023-05-03 23:34:38', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 236, 'metrics.enabled', 'SEATA_GROUP', '', 'false', '68934a3e9455fa72420237eb05902327', '2023-05-03 23:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 237, 'metrics.registryType', 'SEATA_GROUP', '', 'compact', '7cf74ca49c304df8150205fc915cd465', '2023-05-03 23:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 238, 'metrics.exporterList', 'SEATA_GROUP', '', 'prometheus', 'e4f00638b8a10e6994e67af2f832d51c', '2023-05-03 23:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');
INSERT INTO `his_config_info` VALUES (0, 239, 'metrics.exporterPrometheusPort', 'SEATA_GROUP', '', '9898', '7b9dc501afe4ee11c56a4831e20cee71', '2023-05-03 23:34:39', '2023-05-03 15:34:39', NULL, '127.0.0.1', 'I', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('admin', '$2a$10$kl9S6bqypDKoBk3.SRVhVeJetoF3PR6VK0rlxqCVn/4u2Ny8zP.4e', 1);
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
