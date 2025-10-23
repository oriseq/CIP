/*
 Navicat Premium Data Transfer

 Source Server         : 云服务器
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 127.0.0.1:3306
 Source Schema         : cip_business_demo

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 22/10/2025 13:18:27
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for delivery_unit
-- ----------------------------
DROP TABLE IF EXISTS `delivery_unit`;
CREATE TABLE `delivery_unit`
(
    `id`              bigint NOT NULL AUTO_INCREMENT,
    `name`            varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `remarks`         varchar(1600) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
    `submission_time` datetime NULL DEFAULT NULL COMMENT '提交时间',
    `update_time`     datetime NULL DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `unique_name` (`name` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 36
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of delivery_unit
-- ----------------------------
INSERT INTO `delivery_unit`
VALUES (35, '外送单位A', '测试编辑', '2025-03-13 17:16:32', '2025-03-13 17:16:39');

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`
(
    `id`                 varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `original_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原始文件名',
    `path`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问位置',
    `sample_id`          bigint NULL DEFAULT NULL COMMENT '绑定对应的样本id，标识为样本的文件。当前每一个文件都归属到某一个样本下。',
    `creation_time`      datetime NULL DEFAULT NULL COMMENT '时间',
    `attachment_type`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件类型：\r\n1：图片（用于展示形式）\r\n2：其他（用于展示形式）',
    `file_size`          bigint NULL DEFAULT NULL COMMENT '文件大小（字节）',
    `is_use`             tinyint NULL DEFAULT 0 COMMENT '是否正在使用。标记是否有被其他业务中使用该文件。若有则不能删除，没有的可能是上传文件后没被引用，可删除掉。',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file
-- ----------------------------

-- ----------------------------
-- Table structure for instrument
-- ----------------------------
DROP TABLE IF EXISTS `instrument`;
CREATE TABLE `instrument`
(
    `id`            bigint                                                        NOT NULL AUTO_INCREMENT,
    `name`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
    `remarks`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `export_fields` json NULL COMMENT '导出字段',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 25
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of instrument
-- ----------------------------
INSERT INTO `instrument`
VALUES (18, '罗氏411', NULL, '[
  \"submitNumberToday\",
\"name\",
\"sex\",
\"age\",
\"sampleUserGroupName\",
\"remarks\",
\"empty\"
]');
INSERT INTO `instrument`
VALUES (19, '血常规', NULL, '[
  \"submitNumberToday\",
\"name\",
\"sex\",
\"age\",
\"sampleUserGroupName\",
\"remarks\",
\"empty\"
]');
INSERT INTO `instrument`
VALUES (20, '精液常规', '无批量导入功能', NULL);
INSERT INTO `instrument`
VALUES (21, 'kp', '外送单位', NULL);
INSERT INTO `instrument`
VALUES (22, 'xy', '外送单位', NULL);
INSERT INTO `instrument`
VALUES (24, 'hy', '外送单位', NULL);

-- ----------------------------
-- Table structure for invitation_codes
-- ----------------------------
DROP TABLE IF EXISTS `invitation_codes`;
CREATE TABLE `invitation_codes`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'ID（key）',
    `user_group_id`   bigint                                                        NOT NULL COMMENT '用户组ID',
    `invitation_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请码',
    `expiry_date`     datetime                                                      NOT NULL COMMENT '有效时间',
    `create_time`     timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 39
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '邀请码表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of invitation_codes
-- ----------------------------

-- ----------------------------
-- Table structure for log
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log`
(
    `id`              bigint UNSIGNED NOT NULL AUTO_INCREMENT,
    `log_time`        datetime NULL DEFAULT CURRENT_TIMESTAMP,
    `class_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `method`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `message`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
    `user_id`         int NULL DEFAULT NULL,
    `ip`              varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `request_url`     varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `request_method`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `request_params`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
    `response_status` int NULL DEFAULT NULL COMMENT '0或200正常',
    `exception`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 173986
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of log
-- ----------------------------

-- ----------------------------
-- Table structure for message_notification
-- ----------------------------
DROP TABLE IF EXISTS `message_notification`;
CREATE TABLE `message_notification`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `avatar`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息头像地址',
    `title`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
    `description`    varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
    `datetime`       datetime NULL DEFAULT NULL COMMENT '发送时间',
    `type`           tinyint NULL DEFAULT NULL COMMENT '消息类型（1：通知，2：消息，3：待办）',
    `status`         tinyint NULL DEFAULT NULL COMMENT '状态  0：未读  1：已读   2: 已查阅',
    `extra`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待办类型：额外显示文本',
    `color`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '颜色',
    `user_id`        int    NOT NULL COMMENT '用户id',
    `secondary_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '二级分类',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX            ```user_id``` (`user_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2411
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message_notification
-- ----------------------------

-- ----------------------------
-- Table structure for message_type
-- ----------------------------
DROP TABLE IF EXISTS `message_type`;
CREATE TABLE `message_type`
(
    `id`       bigint NOT NULL,
    `msg_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通知二级类型',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message_type
-- ----------------------------
INSERT INTO `message_type`
VALUES (1, '样本提交');
INSERT INTO `message_type`
VALUES (2, '新合作项目申请');
INSERT INTO `message_type`
VALUES (3, '样本过户');
INSERT INTO `message_type`
VALUES (4, '样本代管');
INSERT INTO `message_type`
VALUES (5, '取消样本代管');
INSERT INTO `message_type`
VALUES (6, '样本报告全部完成');
INSERT INTO `message_type`
VALUES (7, '样本项目截止时间提醒');

-- ----------------------------
-- Table structure for msg_group
-- ----------------------------
DROP TABLE IF EXISTS `msg_group`;
CREATE TABLE `msg_group`
(
    `id`             bigint                                                        NOT NULL AUTO_INCREMENT,
    `user_ids`       json NULL COMMENT '接收通知的用户ids',
    `group_identify` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接收组标识',
    `name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
    `help_msg`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `1` (`group_identify` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of msg_group
-- ----------------------------
INSERT INTO `msg_group`
VALUES (1, '[
  1,
  15,
  42,
  2,
  41,
  63
]', 'inspection_form_submission', '送检单提交组', '送检单提交时触发通知');
INSERT INTO `msg_group`
VALUES (3, '[
  1,
  15,
  40,
  42,
  41
]', 'project_management', '项目管理组', '项目申请时触发通知');

-- ----------------------------
-- Table structure for open_permissions
-- ----------------------------
DROP TABLE IF EXISTS `open_permissions`;
CREATE TABLE `open_permissions`
(
    `id`            bigint NOT NULL AUTO_INCREMENT COMMENT '关联索引ID（key）',
    `user_id`       bigint NOT NULL COMMENT '用户ID',
    `permission_id` bigint NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1806
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '开放的权限表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for package
-- ----------------------------
DROP TABLE IF EXISTS `package`;
CREATE TABLE `package`
(
    `id`              bigint NOT NULL AUTO_INCREMENT COMMENT '套餐ID（key）',
    `package_info`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '套餐信息',
    `user_group_id`   bigint NULL DEFAULT NULL COMMENT '用户组Id',
    `project_id_list` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目ID组合',
    `user_id`         bigint NULL DEFAULT NULL COMMENT '用户id：暂用于记录特定用户的常用套餐',
    `remarks`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 93
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '套餐总表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of package
-- ----------------------------
INSERT INTO `package`
VALUES (1, '测试1', 1, '5,3,9,228,10,181,15', NULL, 'erew');
INSERT INTO `package`
VALUES (2, '测试2', 1, '4,5,6', NULL, NULL);

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`
(
    `Id`                    bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '权限ID（key）',
    `permission_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名',
    `permission_identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识  模块:功能:操作',
    `parent_id`             bigint NULL DEFAULT NULL COMMENT '父权限ID',
    `type`                  enum ('M','C','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限类型（M目录 C菜单 F按钮）',
    `route`                 varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由地址',
    `order_no`              int NULL DEFAULT NULL COMMENT '菜单的排序',
    `level`                 tinyint NULL DEFAULT NULL COMMENT '层级，用于不同用户可拥有的菜单权限区分\n1级最高：超级管理员拥有 \n\n2级：所有用户都拥有。\n如果为null表示菜单不受控制',
    `enable`                tinyint NULL DEFAULT NULL COMMENT '是否启用（只对菜单生效，1：启用，0：不启用）,为null时不做判断',
    `meta`                  json NULL COMMENT '菜单的meta属性',
    `menu_name_path`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '指定菜单名称和path，可不填，默认自动生成',
    PRIMARY KEY (`Id`) USING BTREE,
    UNIQUE INDEX `permission_name` (`permission_name` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 50
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限总表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO `permissions`
VALUES (1, '样本送检', NULL, NULL, 'M', '/sampleos/sample', 1, NULL, NULL, '{
  \"img\": \"/workbenck/InspectionForm.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"样本送检\",
\"orderNo\": 1,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (2, '送检单填写', NULL, 1, 'C', '/sampleos/sample/inspectionForm', 3, NULL, NULL, '{
  \"img\": \"/workbenck/InspectionForm.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"送检单填写\",
\"orderNo\": 3,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (3, '送检任务管理', NULL, 1, 'C', '/sampleos/sample/inspectionMission', 4, NULL, NULL, '{
  \"img\": \"/workbenck/InspectionMission.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"送检任务管理\",
\"orderNo\": 4,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (4, '代管样本管理', NULL, 1, 'C', '/sampleos/sample/escrowSample', 5, NULL, 0, '{
  \"img\": \"/workbenck/EscrowSample.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"代管样本管理\",
\"orderNo\": 5,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (5, '系统管理', NULL, NULL, 'M', '/sampleos/system', 2, 2, NULL, '{
  \"img\": \"/workbenck/Package.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"系统管理\",
\"orderNo\": 2,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (6, '套餐管理', NULL, 5, 'C', '/sampleos/system/package', 6, 2, NULL, '{
  \"img\": \"/workbenck/Package.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"套餐管理\",
\"orderNo\": 6,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (7, '合作项目管理', NULL, 5, 'C', '/sampleos/system/cooperativeProject', 8, 2, NULL, '{
  \"img\": \"/workbenck/Project.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"合作项目管理\",
\"orderNo\": 8,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (8, '用户管理', NULL, 5, 'C', '/sampleos/system/user', 10, 2, NULL, '{
  \"img\": \"/workbenck/User.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"用户管理\",
\"orderNo\": 10,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (9, '账单汇总统计', NULL, NULL, 'C', '/sampleos/system/billStatistics', 15, NULL, 1, '{
  \"img\": \"/workbenck/BillStatistics.png\",
\"icon\": \"/workbenck/BillStatistics.png\",
\"title\": \"账单汇总统计\",
\"orderNo\": 14,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (10, '用户管理查询', 'system:user:query', 8, 'F', NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (11, '用户组管理', '', 5, 'C', '/sampleos/system/userGroup', 11, 2, NULL, '{
  \"img\": \"/workbenck/User.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"用户组管理\",
\"orderNo\": 11,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (13, '样本项目管理', NULL, 1, 'C', '/sampleos/sample/projectManagement', NULL, NULL, NULL, '{
  \"img\": null,
\"icon\": \"bx:bx-home\",
\"title\": \"\",
\"orderNo\": null,
\"hideMenu\": true
}', NULL);
INSERT INTO `permissions`
VALUES (19, '合作项目列表查询', 'system:cooperateProject:query', 7, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (20, '合作项目修改', 'system:cooperateProject:update', 7, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (21, '合作项目删除', 'system:cooperateProject:delete', 7, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (22, '用户管理获取邀请码', 'system:user:invitationCode', 8, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (23, '用户管理修改', 'system:user:update', 8, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (24, '用户管理新增', 'system:user:add', 8, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (25, '用户管理删除', 'system:user:delete', 8, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (26, '说明文档', NULL, NULL, 'C', 'http://192.168.2.202:45775/', 14, NULL, 1, '{
  \"icon\": \"fxemoji:documenttextpicture\",
\"title\": \"说明文档\",
\"frameSrc\": \"http://192.168.2.202:45775/\",
\"hideMenu\": false
}', 'http://192.168.2.202:45775/');
INSERT INTO `permissions`
VALUES (30, '套餐管理查询', 'system:package:query', 6, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (31, '套餐管理新增', 'system:package:add', 6, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (32, '套餐管理修改', 'system:package:update', 6, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (33, '套餐管理删除', 'system:package:delete', 6, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (34, '用户管理权限修改', 'system:user:permission', 8, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (35, '消息管理', NULL, NULL, 'C', '/sampleos/message', 12, NULL, NULL, '{
  \"img\": \"/workbenck/InspectionMission.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"消息管理\",
\"orderNo\": 12,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (36, '项目管理', NULL, 5, 'C', '/sampleos/system/project', 7, 2, NULL, '{
  \"img\": \"/workbenck/Project.png\",
\"icon\": \"bx:bx-home\",
\"title\": \"项目管理\",
\"orderNo\": 7,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (37, '菜单测试', NULL, 5, 'C', '/sampleos/system/menu', NULL, 2, 0, '{
  \"img\": null,
\"icon\": \"bx:bx-home\",
\"title\": \"菜单测试\",
\"orderNo\": null,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (38, '个人设置', NULL, NULL, 'C', '/sampleos/personalSettings', 13, NULL, NULL, '{
  \"img\": null,
\"icon\": \"flat-color-icons:portrait-mode\",
\"title\": \"个人设置\",
\"orderNo\": null,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (39, '外送单位管理', NULL, 5, 'C', '/sampleos/system/deliveryUnit', 14, 2, NULL, '{
  \"img\": null,
\"icon\": \"flat-color-icons:department\",
\"title\": \"外送单位管理\",
\"orderNo\": null,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (41, '外送单位管理查询', 'system:deliveryUnit:query', 39, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (42, '外送单位管理新增', 'system:deliveryUnit:add', 39, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (43, '外送单位管理修改', 'system:deliveryUnit:update', 39, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (44, '外送单位管理删除', 'system:deliveryUnit:delete', 39, 'F', NULL, NULL, 2, NULL, NULL, NULL);
INSERT INTO `permissions`
VALUES (46, '账单明细统计', NULL, NULL, 'C', '/sampleos/system/itemizedBill', 16, NULL, 1, '{
  \"icon\": \"icon-park:bill\",
\"title\": \"账单明细统计\",
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (47, '归档任务管理', NULL, 1, 'C', '/sampleos/sample/archiveMission', 4, NULL, NULL, '{
  \"icon\": \"material-icon-theme:folder-docs\",
\"title\": \"归档任务管理\",
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (48, '仪器管理', NULL, 5, 'C', '/sampleos/system/instrument', NULL, 2, NULL, '{
  \"icon\": \"mingcute:instrument-line\",
\"title\": \"仪器管理\",
\"orderNo\": null,
\"hideMenu\": false
}', NULL);
INSERT INTO `permissions`
VALUES (49, '管理面板', NULL, NULL, 'C', '/sampleos/backgroundSetting', 13, NULL, NULL, '{
  \"img\": null,
\"icon\": \"flat-color-icons:portrait-mode\",
\"title\": \"管理面板\",
\"orderNo\": null,
\"hideMenu\": true
}', NULL);

-- ----------------------------
-- Table structure for project_classification
-- ----------------------------
DROP TABLE IF EXISTS `project_classification`;
CREATE TABLE `project_classification`
(
    `id`        bigint NOT NULL AUTO_INCREMENT,
    `name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `parent_id` bigint NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 39
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_classification
-- ----------------------------
INSERT INTO `project_classification`
VALUES (1, '内分泌系统检查', NULL);
INSERT INTO `project_classification`
VALUES (2, '生化检查', NULL);
INSERT INTO `project_classification`
VALUES (3, '免疫检查', NULL);
INSERT INTO `project_classification`
VALUES (4, '感染性疾病检查', NULL);
INSERT INTO `project_classification`
VALUES (5, '临检血液体液检查', NULL);
INSERT INTO `project_classification`
VALUES (6, '病理检查', NULL);
INSERT INTO `project_classification`
VALUES (7, '妇产科疾病检查', NULL);
INSERT INTO `project_classification`
VALUES (8, '流式检查', NULL);
INSERT INTO `project_classification`
VALUES (9, '药物浓度监测相关检查', NULL);
INSERT INTO `project_classification`
VALUES (10, '肿瘤系列检查', NULL);
INSERT INTO `project_classification`
VALUES (11, '遗传相关性疾病检查', NULL);
INSERT INTO `project_classification`
VALUES (12, '分子诊断', NULL);
INSERT INTO `project_classification`
VALUES (13, '性激素系列', 1);
INSERT INTO `project_classification`
VALUES (14, '甲状腺疾病系列', 1);
INSERT INTO `project_classification`
VALUES (15, '骨代谢系列', 1);
INSERT INTO `project_classification`
VALUES (16, '糖尿病系列', 1);
INSERT INTO `project_classification`
VALUES (17, '常规生化检测系列', 2);
INSERT INTO `project_classification`
VALUES (18, '感染类定性系列', 3);
INSERT INTO `project_classification`
VALUES (19, '优生优育系列', 3);
INSERT INTO `project_classification`
VALUES (20, '自身免疫性疾病系列', 3);
INSERT INTO `project_classification`
VALUES (21, '肝胆疾病系列', 4);
INSERT INTO `project_classification`
VALUES (22, '生殖道感染性疾病检测', 4);
INSERT INTO `project_classification`
VALUES (23, '微生物检测', 4);
INSERT INTO `project_classification`
VALUES (24, '体液检测系列', 5);
INSERT INTO `project_classification`
VALUES (25, '细胞病理', 6);
INSERT INTO `project_classification`
VALUES (26, '男科检查', 7);
INSERT INTO `project_classification`
VALUES (27, '流式系列', 8);
INSERT INTO `project_classification`
VALUES (28, '地中海贫血检查', 7);
INSERT INTO `project_classification`
VALUES (29, '药物基因系列', 9);
INSERT INTO `project_classification`
VALUES (30, '常规血液检测系列', 5);
INSERT INTO `project_classification`
VALUES (31, '血栓与血凝系列', 5);
INSERT INTO `project_classification`
VALUES (32, '细胞因子系列', 3);
INSERT INTO `project_classification`
VALUES (33, '肿瘤标志物系列', 10);
INSERT INTO `project_classification`
VALUES (34, '遗传病诊断系列', 11);
INSERT INTO `project_classification`
VALUES (35, '离子检测系列', 2);
INSERT INTO `project_classification`
VALUES (36, '优生优育系列', 12);
INSERT INTO `project_classification`
VALUES (37, '产前筛查', NULL);
INSERT INTO `project_classification`
VALUES (38, '产前筛查', 37);

-- ----------------------------
-- Table structure for project_master
-- ----------------------------
DROP TABLE IF EXISTS `project_master`;
CREATE TABLE `project_master`
(
    `Id`                         bigint NOT NULL AUTO_INCREMENT COMMENT '项目ID（key）',
    `project_name`               varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目名称',
    `report_date`                varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报告周期（文字记录）',
    `detect_method`              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '检测方法',
    `consumable_type`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '采样耗材类型',
    `project_category_id`        bigint NULL DEFAULT NULL COMMENT '项目分类ID',
    `price`                      decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
    `sample_requirements`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样本要求',
    `is_report_hour_minutes_end` tinyint NULL DEFAULT NULL COMMENT '时分刻度是否置末(1:是，0:否)',
    `report_day`                 int NULL DEFAULT NULL COMMENT '报告天数',
    `report_hour`                int NULL DEFAULT NULL COMMENT '报告小时',
    `disable_week`               json NULL COMMENT '排除星期：[1,2,3,4,5,6,7]',
    `remaining_hour`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '警报剩余时间（小时）',
    `remarks`                    varchar(2500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
    `delivery_unit_ids`          json NULL COMMENT '包含的外送单位ids',
    `instrument_id`              bigint NULL DEFAULT NULL,
    PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 257
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '项目总表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_master
-- ----------------------------
INSERT INTO `project_master`
VALUES (1, '促卵泡成熟激素（FSH）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清0.5ml', 0, NULL, 4, NULL, '24',
        '这是一条测试备注', '[
    1,
    2,
    3
  ]', 18);
INSERT INTO `project_master`
VALUES (2, '促黄体生成素（LH）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清0.5ml', 0, NULL, 4, NULL, '24',
        '这是一条测试备注LH', '[]', 18);
INSERT INTO `project_master`
VALUES (3, '雌二醇（E2）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清0.5ml', 0, NULL, 4, NULL, '24', NULL, NULL,
        18);
INSERT INTO `project_master`
VALUES (4, '孕酮（P）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清0.5ml', 0, NULL, 4, NULL, '24', NULL, NULL, 18);
INSERT INTO `project_master`
VALUES (5, '催乳素（PRL）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清0.5ml', 0, NULL, 4, NULL, '24', NULL, NULL,
        18);
INSERT INTO `project_master`
VALUES (6, '睾酮（T）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清0.5ml', 0, NULL, 4, NULL, '24', NULL, NULL, 18);
INSERT INTO `project_master`
VALUES (7, '性激素6项（FSH、LH、E2、P、PRL、T）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清2.0ml', 0, NULL, 4, NULL,
        '24', NULL, '[
    2
  ]', 18);
INSERT INTO `project_master`
VALUES (8, '性激素5项（FSH、LH、E2、P、PRL）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清2.0ml', 0, NULL, 4, NULL,
        '24', NULL, '[
    2
  ]', 18);
INSERT INTO `project_master`
VALUES (9, '性激素4项（FSH、LH、E2、P）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清2.0ml', 0, NULL, 4, NULL, '24',
        NULL, '[
    2
  ]', 18);
INSERT INTO `project_master`
VALUES (10, '性激素3项（LH、E2、P）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清2.0ml', 0, NULL, 4, NULL, '24',
        NULL, '[
    2
  ]', 18);
INSERT INTO `project_master`
VALUES (11, '性激素3项（E2、P、PRL）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清2.0ml', 0, NULL, 4, NULL, '24',
        NULL, '[
    2
  ]', 18);
INSERT INTO `project_master`
VALUES (12, '孕2项（HCG、P）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清2.0ml', 0, NULL, 4, NULL, '24', NULL, '[
  2
]', 18);
INSERT INTO `project_master`
VALUES (13, '孕3项（HCG、P、E2）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清2.0ml', 0, NULL, 4, NULL, '24', NULL,
        '[
          2
        ]', 18);
INSERT INTO `project_master`
VALUES (14, 'β绒毛膜促性腺激素（βHCG）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清0.5ml', 0, NULL, 4, NULL, '24',
        NULL, '[
    null
  ]', 18);
INSERT INTO `project_master`
VALUES (15, '抗缪勒氏管激素（AMH）', '4小时', '电化学发光法', '黄头管', 13, 1.00, '血清0.5ml', 0, NULL, 4, NULL, '24',
        NULL, NULL, 18);
INSERT INTO `project_master`
VALUES (16, '甲功3项（TSH、T3、T4）', '当天', '电化学发光法', '黄头管', 14, 1.00, '血清2.0ml', 1, 0, NULL, NULL, NULL, NULL,
        NULL, 18);
INSERT INTO `project_master`
VALUES (17, '促甲状腺激素（TSH）', '当天', '电化学发光法', '黄头管', 14, 1.00, '血清0.5ml', 1, 0, NULL, NULL, NULL, NULL,
        NULL, 18);
INSERT INTO `project_master`
VALUES (18, '三碘甲状原氨酸（T3）', '当天', '电化学发光法', '黄头管', 14, 1.00, '血清0.5ml', 1, 0, NULL, NULL, NULL, NULL,
        '[
          null
        ]', 18);
INSERT INTO `project_master`
VALUES (19, '甲状腺素（T4）', '当天', '电化学发光法', '黄头管', 14, 1.00, '血清0.5ml', 1, 0, NULL, NULL, NULL, NULL, NULL,
        18);
INSERT INTO `project_master`
VALUES (20, '甲功5项（TSH、T3、T4、FT3、FT4）', '1天', '电化学发光法', '黄头管', 14, 1.00, '血清2.0ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1,
    null
  ]', 18);
INSERT INTO `project_master`
VALUES (21, '甲功7项（T3、T4、TSH、FT3、FT4、TG-Ab、TPOAb）', '1天', '电化学发光法', '黄头管', 14, 1.00, '血清2.0ml', 1, 1,
        NULL, NULL, '2', NULL, '[
    1,
    null
  ]', 18);
INSERT INTO `project_master`
VALUES (22, '游离三碘甲状原氨酸（FT3）', '1天', '电化学发光法', '黄头管', 14, 1.00, '血清0.5ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 18);
INSERT INTO `project_master`
VALUES (23, '游离甲状腺素（FT4）', '1天', '电化学发光法', '黄头管', 14, 1.00, '血清0.5ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 18);
INSERT INTO `project_master`
VALUES (24, '抗甲状腺过氧化物酶抗体（TPOAb）', '1天', '电化学发光法', '黄头管', 14, 1.00, '血清0.5ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 18);
INSERT INTO `project_master`
VALUES (25, '抗甲状腺球蛋白抗体(TG-Ab)', '1天', '电化学发光法', '黄头管', 14, 1.00, '血清0.5ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 18);
INSERT INTO `project_master`
VALUES (26, '甲状腺球蛋白（TG）', '1天', '电化学发光法', '黄头管', 14, 1.00, '血清0.5ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 18);
INSERT INTO `project_master`
VALUES (27, '总25-羟基维生素D', '1天', '化学发光法', '黄头管', 15, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (28, '空腹胰岛素', '1天', '化学发光法', '黄头管', 16, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (29, '胰岛素释放试验 (空腹、餐后1h、2h)', '1天', '化学发光法', '黄头管', 16, 1.00, '血清0.6ml/管（共3管）', 1, 1,
        NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (30, '胰岛素释放试验 (空腹、餐后0.5h、餐后1h、2h、3h)', '1天', '化学发光法', '黄头管', 16, 1.00,
        '血清0.6ml/管（共5管）', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (31, 'D-二聚体', '当天', '免疫透射比浊法', '蓝头管', 17, 1.00, '1:9枸橼酸钠血浆0.5ml', 1, 0, NULL, NULL, NULL,
        NULL, '[
    2
  ]', 21);
INSERT INTO `project_master`
VALUES (32, '血清铁', '1天', '比色法', '黄头管', 17, 1.00, '血清0.3ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (33, '总胆汁酸（TBA）', '1天', '循环酶速率法', '黄头管', 17, 1.00, '血清0.3ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (34, '丙氨酸氨基转移酶（ALT）', '1天', '速率法', '黄头管', 17, 1.00, '血清0.3ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (35, '肝功3项(AST,ALT,TBIL)', '1天', '电化学发光法', '黄头管', 17, 1.00, '血清1.0ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1,
    2
  ]', 21);
INSERT INTO `project_master`
VALUES (36, '肝功5项(AST、ALT、AST/ALT、TP、ALB、GLB、GGT)', '1天', '速率法', '黄头管', 17, 1.00, '血清1.5ml', 1, 1, NULL,
        NULL, '2', NULL, '[
    1,
    2
  ]', 21);
INSERT INTO `project_master`
VALUES (37, '肝功7项(AST、ALT、AST/ALT、TP、ALB、GLB、GGT、A/G、LDH、ALP)', '1天', '/', '黄头管', 17, 1.00, '血清1.5ml', 1, 1,
        NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (38, '肝功8项(AST、ALT、AST/ALT、TP、ALB、GLB、GGT、A/G、LDH、ALP、TBA)', '1天', NULL, '黄头管', 17, 1.00, '血清1.5ml', 1,
        1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (39, '肝功10项（AST ALT TP ALB GLB  TBIL DBIL IBIL  AST/ALT 白/球蛋白比值 GGT ALP LDH )', '1天', '/', '黄头管',
        17, 1.00, '血清1.5ml', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (40, '肝功11项（AST ALT TP ALB GLB  TBIL DBIL IBIL  AST/ALT 白/球蛋白比值 GGT LDH TBA 胆碱酯酶 ）', '1天', NULL,
        '黄头管', 17, 1.00, '血清1.5ml', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (41, '肝功12项（AST ALT TP ALB GLB GGT TBIL DBIL IBIL ALP AST/ALT 白/球蛋白比值 ）', '1天', NULL, '黄头管', 17,
        1.00, '血清1.5ml', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (42, '胆红素3项（TBIL、DBIL、IBIL）', '1天', NULL, '黄头管', 17, 1.00, '血清1.0ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (43, '肾功3项（尿素、肌酐、尿酸）', '1天', NULL, '黄头管', 17, 1.00, '血清1.0ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (44, '肾功4项（葡萄糖、尿素、肌酐、尿酸）', '1天', '化学发光法', '黄+灰头管', 17, 1.00, '血清1.0ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1,
    2
  ]', 21);
INSERT INTO `project_master`
VALUES (45, '肾功4项（CO2、尿素、肌酐、尿酸）', '1天', NULL, '黄头管', 17, 1.00, '血清1.0ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (46, '肾功5项（UA、UREA、CR、Cysc、CO2）', '1天', NULL, '黄头管', 17, 1.00, '血清1.0ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (47, '肾功5项（UA、UREA、CR、Cysc、GLU）', '1天', NULL, '黄+灰头管', 17, 1.00, '血清1.0ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (48, '葡萄糖糖耐量试验（OGTT） (空腹、餐后1h、2h)', '1天', '己糖激酶法', '灰头管', 17, 1.00,
        '血清/血浆0.3ml/管（共3管）', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (49, '葡萄糖糖耐量试验 (空腹、餐后0.5h、餐后1h、2h、3h)', '1天', '己糖激酶法', '灰头管', 17, 1.00,
        '血清/血浆0.3ml/管（共5管）', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (50, '血脂4项（TCHOL、TG、HDL-C、LDL-C、LDL-C/HDL-C、TCHOL/HDL-C）', '1天', '化学发光法', '黄头管', 17, 1.00,
        '血清1.5ml', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (51, '血脂5项（TG HDL LDL TC LP(a)）', '1天', NULL, '黄头管', 17, 1.00, '血清1.5ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (52, '血脂6项（TG HDL LDL TC ApoA1 ApoB）', '1天', NULL, '黄头管', 17, 1.00, '血清1.5ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (53, '血脂7项（TG HDL LDL TC LP(a) ApoA1 ApoB）', '1天', NULL, '黄头管', 17, 1.00, '血清1.5ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (54,
        '血脂8项（TG HDL LDL TC LP(a) ApoA1 ApoB 载脂蛋白A1/载脂蛋白B 低密度脂蛋白/高密度脂蛋白比率 总胆固醇/高密度脂蛋白比率）',
        '1天', NULL, '黄头管', 17, 1.00, '血清1.5ml', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (55, '血脂8项（TG HDL LDL TC LP(a) ApoA1 ApoB HCY）', '1天', NULL, '黄头管', 17, 1.00, '血清1.5ml', 1, 1, NULL,
        NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (56, '血糖', '1天', '己糖激酶法', '灰头管', 17, 1.00, '血清/血浆0.3ml', 1, 1, NULL, NULL, '2', NULL, '[
  1,
  2
]', 21);
INSERT INTO `project_master`
VALUES (57, '同型半胱氨酸（HCY）', '1天', '循环酶法', '黄头管', 17, 1.00, '血清0.3ml(请及时分离分清)', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (58, '免疫球蛋白3项（IgA、G、M）', '1天', '化学发光法', '黄头管', 17, 1.00, '血清1.5ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (59, '免疫球蛋白5项（IgA、G、M、C3、C4）', '1天', NULL, '黄头管', 17, 1.00, '血清1.5ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (60, 'C-反应蛋白（CRP）', '1天', '免疫透射比浊法', '黄头管', 17, 1.00, '血清0.3ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (61, '降钙素原', '1天', '化学发光法', '黄头管', 17, 1.00, '血清1.0ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (62, '甲型肝炎病毒抗体IgM', '1天', '酶联免疫法', '黄头管', 18, 1.00, '血清或血浆0.6ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (63, '甲型肝炎病毒抗体IgG', '1天', '酶联免疫法', '黄头管', 18, 1.00, '血清或血浆0.6ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (64, '传染四项（乙肝两对半定性、丙型肝炎病毒抗体定性、HIV、梅毒螺旋抗体）', '当天', '胶体金法', '黄头管', 18, 1.00,
        '血清或血浆3ml', 1, 0, NULL, NULL, NULL, NULL, NULL, 19);
INSERT INTO `project_master`
VALUES (65, '传染五项（乙肝两对半定性、丙型肝炎病毒抗体定性、HIV、梅毒螺旋抗体、梅毒甲苯胺红不加热血清试验TRUST）', '当天',
        '胶体金法+凝集法', '黄头管', 18, 1.00, '血清或血浆3ml', 1, 0, NULL, NULL, NULL, NULL, '[]', 19);
INSERT INTO `project_master`
VALUES (66, '乙肝两对半定性', '1天', '胶体金法', '黄头管', 18, 1.00, '血清或血浆1ml', 1, 1, NULL, NULL, '2', NULL, '[
  2
]', 19);
INSERT INTO `project_master`
VALUES (67, '乙型肝炎病毒核心抗体IgM（HBcAb-IgM）', '1天（除周日）', '酶联免疫法', '黄头管', 18, 1.00, '血清0.5ml', 1, 1,
        NULL, '[
    7
  ]', '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (68, '乙肝两对半定量', '1天', '化学发光法', '黄头管', 18, 1.00, '血清3.0ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (69, '丙型肝炎病毒抗体定性（HCV-Ab）', '当天', '胶体金法', '黄头管', 18, 1.00, '血清或血浆1ml', 1, 0, NULL, NULL,
        NULL, NULL, NULL, 19);
INSERT INTO `project_master`
VALUES (70, '丙型肝炎病毒核心抗原（HCV-cAg）', '1天（除周日）', '酶联免疫法', '黄头管', 18, 1.00, '血清或血浆0.5ml', 1, 1,
        NULL, '[
    7
  ]', '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (71, '丙肝2项（HCV-IgG、HCV-cAg）', '1天（除周日）', '酶联免疫法', '黄头管', 18, 1.00, '血清或血浆1ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (72, '戊型肝炎病毒抗体IgM', '1天（除周日）', '酶联免疫法', '黄头管', 18, 1.00, '血清或血浆0.5ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (73, '戊型肝炎病毒抗体IgG', '1天（除周日）', '酶联免疫法', '黄头管', 18, 1.00, '血清或血浆0.5ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (74, '人免疫缺陷病毒抗体测定（HIV）', '当天', '胶体金法', '黄头管', 18, 1.00, '血清1ml', 1, 0, NULL, NULL, NULL,
        NULL, NULL, 19);
INSERT INTO `project_master`
VALUES (75, '梅毒螺旋体抗体定性（TP-Ab）', '当天', '胶体金法', '黄头管', 18, 1.00, '血清或血浆1ml', 1, 0, NULL, NULL,
        NULL, NULL, '[
    1
  ]', 19);
INSERT INTO `project_master`
VALUES (76, '梅毒螺旋体特异性抗体（TPPA）', '1（除周日）', '凝集法', '黄头管', 18, 1.00, '血清或血浆0.5ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (77, '梅毒甲苯胺红不加热血清试验（TRUST）', '当天', '凝集法', '黄头管', 18, 1.00, '血清或血浆0.5ml', 1, 0, NULL,
        NULL, NULL, NULL, '[
    2,
    1
  ]', 19);
INSERT INTO `project_master`
VALUES (78, '梅毒2项（TRUST、TP-Ab）', '当天', '胶体金法+凝集法', '黄头管', 18, 1.00, '血清或血浆1ml', 1, 0, NULL, NULL,
        NULL, NULL, '[
    1
  ]', 19);
INSERT INTO `project_master`
VALUES (79, '梅毒2项（TRUST、TPPA）', '1（除周日）', '凝集法', '黄头管', 18, 1.00, '血清或血浆1ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (80, '梅毒TRUST半定量', '1（除周日）', '凝集法', '黄头管', 18, 1.00, '血清或血浆0.5ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (81, '结核杆菌IgG抗体(TB-IgG)', '1（除周日）', '胶体\n金法', '黄头管', 18, 1.00, '血清0.5ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (82, '优生5项定性（IgM）', '1（除周日）', '酶联免疫法', '黄头管', 36, 1.00, '血清或血浆2ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (83, '优生5项定性（IgG）', '1（除周日）', '酶联免疫法', '黄头管', 36, 1.00, '血清或血浆2ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (84, '优生10项定性', '1（除周日）', '酶联免疫法', '黄头管', 36, 1.00, '血清或血浆2ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (85, 'ACA抗心磷脂抗体定性（IgM）', '1天', '酶联免疫法', '黄头管', 20, 1.00, '血清或血浆0.5mL', 1, 1, NULL, NULL,
        '2', NULL, '[
    1,
    null
  ]', 21);
INSERT INTO `project_master`
VALUES (86, 'ACA抗心磷脂抗体定性（IgA）', '1天', '酶联免疫法', '黄头管', 20, 1.00, '血清或血浆0.5mL', 1, 1, NULL, NULL,
        '2', NULL, '[
    1,
    null
  ]', 21);
INSERT INTO `project_master`
VALUES (87, 'ACA抗心磷脂抗体定性（IgG）', '1天', '酶联免疫法', '黄头管', 20, 1.00, '血清或血浆0.5mL', 1, 1, NULL, NULL,
        '2', NULL, '[
    1,
    null
  ]', 21);
INSERT INTO `project_master`
VALUES (88, '抗子宫内膜抗体定性（IgM）', '1天', '免疫层析法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (89, '抗子宫内膜抗体定性（IgG）', '1天', '胶体金法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (90, '抗精子抗体定性（IgM）', '1天', '胶体金法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (91, '抗精子抗体定性（IgG）', '1天', '胶体金法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (92, '抗卵巢抗体定性（IgM）', '1天', '免疫层析法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1,
          null
        ]', 21);
INSERT INTO `project_master`
VALUES (93, '抗卵巢抗体定性（IgG）', '1天', '胶体金法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 1, NULL, NULL, '2', NULL, '[
  1,
  null
]', 21);
INSERT INTO `project_master`
VALUES (94, '抗透明带抗体定性（IgM）', '1天', '胶体金法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (95, '抗透明带抗体定性（IgG）', '1天', '胶体金法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (96, '抗核抗体（ANA）', '3个工作日', '间接免疫荧光法', '黄头管', 20, 1.00, '血清1.0ml', 1, 3, NULL, '[
  6,
  7
]', '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (97, '抗核抗体定量（ANA）', '1天', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (98, '不孕不育4项(IgM) (EmAb、ACA、AsAb、AoAb）', '4天', '酶联免疫法', '黄头管', 36, 1.00, '血清或血浆0.5ml', 1, 4,
        NULL, NULL, '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (99, '抗核抗体谱-8S-B （风湿八）（SCL-70\nSmD1，SSA-Ro60，SSA-Ro52，SSB/La，CenpB，U1-snRNP，Jo-1）', '2天', '免疫印迹法',
        '黄头管', 20, 1.00, '血清3.0ml', 1, 2, NULL, NULL, '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (100, '抗心磷脂抗体定量（IgM）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清或血浆0.5mL', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (101, '抗心磷脂抗体定量（IgA）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清或血浆0.5mL', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (102, '抗心磷脂抗体定量（IgG）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清或血浆0.5mL', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (103, '抗心磷脂抗体3项(定量）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清或血浆0.5mL', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (104, '抗β2糖蛋白1抗体定量（IgM）', '1天', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (105, '抗β2糖蛋白1抗体定量（IgA）', '1天', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (106, '抗β2糖蛋白1抗体定量（IgG）', '1天', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (107, '抗β2-糖蛋白1抗体（β2GP1）', '1天', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (108, '抗子宫内膜抗体定量（IgM）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (109, '抗子宫内膜抗体定量（IgG）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (110, '抗透明带抗体定量（IgM）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (111, '抗透明带抗体定量（IgG）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (112, '抗卵巢抗体定量（IgM）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (113, '抗卵巢抗体定量（IgG）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (114, '抗精子抗体定量（IgM）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1,
          null
        ]', 21);
INSERT INTO `project_master`
VALUES (115, '抗精子抗体定量（IgG）', '1天', '化学发光法', '黄头管', 36, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1,
          null
        ]', 21);
INSERT INTO `project_master`
VALUES (116, '封闭抗体（APLA）', '7天', '酶联免疫法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 7, NULL, NULL, '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (117, '乙型肝炎病毒DNA定量', '3天', '荧光PCR', '黄头管', 21, 1.00, '血清/血浆 2ml', 1, 3, NULL, NULL, '24', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (118, '沙眼衣原体核酸检测', '3天', '荧光PCR', '拭子D', 22, 1.00, '脱落细胞、分泌物', 1, 3, NULL, NULL, '24', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (119, '淋球菌核酸检测', '3天', '荧光PCR', '拭子D', 22, 1.00, '脱落细胞、分泌物', 1, 3, NULL, NULL, '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (120, '解脲脲原体核酸检测', '3天', '荧光PCR', '拭子D', 22, 1.00, '脱落细胞、分泌物', 1, 3, NULL, NULL, '24', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (121, '人型支原体核酸检测', '3天', '荧光PCR', '拭子D', 22, 1.00, '脱落细胞、分泌物', 1, 3, NULL, NULL, '24', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (122, 'HPV分型（37种）', '3天', '导流杂交', '专用采样拭子', 22, 1.00, '脱落细胞', 1, 3, NULL, NULL, '24', NULL, '[
  2
]', 21);
INSERT INTO `project_master`
VALUES (123, '淋球菌培养', '7天', '质谱法或培养法', '拭子', 23, 1.00, '各种分泌物、脓液         ', 1, 7, NULL, NULL,
        '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (124, '支原体培养+药敏', '7天', 'MIC法', '拭子C', 23, 1.00,
        '生殖器分泌物 、阴道分泌物、宫颈分泌物  、精液 （无菌拭子）', 1, 7, NULL, NULL, '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (125, '白带常规', '1天', '人工显微镜法', '拭子A', 24, 1.00, '阴道分泌物', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 19);
INSERT INTO `project_master`
VALUES (126, '细菌性阴道病（BV）', '1天', '唾液酸酶法', '拭子B', 24, 1.00, '阴道分泌物', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (127, '白带常规+BV', '1天', '人工显微镜法/唾液酸酶法', '拭子', 24, 1.00, '阴道分泌物', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (128, '液基薄层细胞制片术（TCT）', '4天', '巴氏染色/电脑自动制片/显微镜检查', '专用采样拭子', 25, 1.00,
        '专用采样器及保存瓶', 1, 4, NULL, NULL, '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (129, 'Y染色体微缺失', '3天', '荧光PCR', '紫头管', 26, 1.00, 'EDTA抗凝全血2ml', 1, 3, NULL, NULL, '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (130, 'Y染色体微缺失（15位点）', '3个工作日', '毛细管电泳法', '紫头管', 26, 1.00, 'EDTA抗凝全血2ml', 1, 3, NULL, '[
  6,
  7
]', '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (131, '精液常规', '当天', '仪器法', '取精杯', 26, 1.00, '新鲜精液（）', 1, 0, NULL, NULL, NULL, NULL, '[
  2
]', 19);
INSERT INTO `project_master`
VALUES (132, '精子DNA完整性检测', '7天', '流式细胞术', '无菌容器', 27, 1.00, '新鲜或\n冷冻的\n精液', 1, 7, NULL, NULL,
        '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (133, '地中海贫血基因检测(全套）', '2天', '导流杂交', '紫头管', 28, 1.00, 'EDTA抗凝全血2ml', 1, 2, NULL, NULL,
        '24', NULL, '[
    2
  ]', 24);
INSERT INTO `project_master`
VALUES (134, '地贫常规组合', '3天', '电泳+杂交', '紫头管', 28, 1.00, 'EDTA抗凝全血2ml', 1, 3, NULL, NULL, '24', NULL, '[
  2
]', 24);
INSERT INTO `project_master`
VALUES (135, '叶酸基因检测', '3天', '荧光PCR', '紫头管', 29, 1.00, 'EDTA抗凝全血2.0ml', 1, 3, NULL, NULL, '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (136, '血常规', '当天', '仪器法', '紫头管', 30, 1.00, 'EDTA抗凝全血2ml', 1, 0, NULL, NULL, NULL, NULL, NULL, 19);
INSERT INTO `project_master`
VALUES (137, 'ABO+RhD血型检测', '当天', '凝集法', '紫头管', 30, 1.00, 'EDTA抗凝全血2ml', 1, 0, NULL, NULL, NULL, NULL,
        '[
          2
        ]', 19);
INSERT INTO `project_master`
VALUES (138, '糖化血红蛋白（HbA1c）', '1天', '色谱法', '紫头管', 30, 1.00, 'EDTA抗凝全血2ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (139, '红细胞渗透脆性试验', '2天', '比色法', '紫头管', 30, 1.00, 'EDTA抗凝全血2ml', 1, 2, NULL, NULL, '24', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (140, '葡萄糖6-磷酸脱氢酶(G-6-PD)缺陷筛查', '1天', NULL, '紫头管', 17, 1.00, 'EDTA全血2ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (141, '血红蛋白电泳', '2天', '电泳法', '紫头管', 30, 1.00, 'EDTA抗凝全血2ml', 1, 2, NULL, NULL, '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (142, '地贫三项（MDST,G6PD,血红蛋白电泳）', '4天', '比色法/电泳法', '紫头管', 30, 1.00, 'EDTA抗凝全血2ml', 1, 4,
        NULL, NULL, '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (143, '贫血三项(FER、FOL、VB12)', '1天', '化学发光法', '黄头管', 30, 1.00, '血清1.2ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (144, '凝血四项', '1天', '凝固法', '蓝头管', 31, 1.00, '1:9枸橼酸钠抗凝全血2.0ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          2
        ]', 19);
INSERT INTO `project_master`
VALUES (145, '抗凝血酶III活性测定', '4天', '发色底物法', '蓝头管', 31, 1.00, '1:9枸橼酸钠抗凝全血2.0ml', 1, 4, NULL,
        NULL, '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (146, '凝血酶抗凝血酶复合物TAT', '1天', '化学发光法', '蓝头管', 31, 1.00,
        '1:9枸橼酸钠抗凝全血2.0ml(及时分离血浆)', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (147, '蛋白C测定', '4天', '发色底物法', '蓝头管', 31, 1.00, '1:9枸橼酸钠抗凝全血2.0ml', 1, 4, NULL, NULL, '24',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (148, '蛋白S测定', '4天', '凝固法', '蓝头管', 31, 1.00, '1:9枸橼酸钠抗凝全血2.0ml', 1, 4, NULL, NULL, '24', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (149, '狼疮样抗凝物质筛查（LA1、LA2）', '3天', '凝固法', '蓝头管', 31, 1.00, '1:9枸橼酸钠抗凝全血2.0ml', 1, 3, NULL,
        NULL, '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (150, '肿瘤坏死因子α（TNF-α）', '1天', '化学发光法', '黄头管', 32, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (151, '癌胚抗原（CEA）', '1天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (152, '甲胎蛋白(AFP)', '1天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (153, '人附睾蛋白4（HE4)', '1天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (154, '糖类抗原125（CA125）', '1天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (155, '糖类抗原15-3（CA153）', '1天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (156, '糖类抗原19-9（CA199）', '1天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (157, '糖类抗原50（CA50）', '2天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 2, NULL, NULL, '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (158, '糖类抗原242（CA242）', '7天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 7, NULL, NULL, '24', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (159, '糖类抗原724（CA724）', '1天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (160, '总前列腺特异性抗原(TPSA)', '1天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (161, '游离前列腺特异性抗原(FPSA)', '1天', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (162, 'T淋巴细胞亚群（CD3/CD4/CD8）', '1天', '流式细胞术', '紫头管', 27, 1.00, 'EDTA抗凝全血2ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (163, 'B 淋巴细胞检测（CD3/CD19）', '1天', '流式细胞术', '紫头管', 27, 1.00, 'EDTA抗凝全血2ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (164, 'NK细胞检测（CD3/CD16+56）', '1天', '流式细胞术', '紫头管', 27, 1.00, 'EDTA抗凝全血2ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (165, 'TBNK细胞', '1天', '流式细胞术', '紫头管', 27, 1.00, 'EDTA抗凝全血2ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (166, '外周血染色体核型分析', '7天', NULL, '绿头管', 34, 1.00, '肝素钠抗凝全血2ml', 1, 7, NULL, NULL, '24', NULL,
        '[
          2
        ]', 24);
INSERT INTO `project_master`
VALUES (167, '外周血染色体核型分析（加急）', '5天', NULL, '绿头管', 34, 1.00, '肝素钠抗凝全血2ml', 1, 5, NULL, NULL, '24',
        NULL, '[
    2
  ]', 24);
INSERT INTO `project_master`
VALUES (168, '尿常规', '1个工作日', '仪器法', '无菌容器', 24, 1.00, '尿液5ml', 1, 1, NULL, '[
  6,
  7
]', '2', NULL, '[
  1,
  null
]', 19);
INSERT INTO `project_master`
VALUES (169, '抗双链 DNA 抗体 (dsDNA)定量', '1个工作日', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, '[
  6,
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (170, '抗环瓜氨酸肽抗体（抗ccp抗体）定量', '1个工作日', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL,
        '[
          6,
          7
        ]', '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (171, '类风湿因子（RF）', '1个工作日', '胶乳免疫比浊法', '黄头管', 17, 1.00, '血清0.3ml', 1, 1, NULL, '[
  6,
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (172, '抗中性粒细胞胞浆抗体二项（cANCA、pANCA）', '2个工作日', '间接免疫荧光法', '黄头管', 20, 1.00,
        '血清或血浆0.5mL', 1, 2, NULL, '[
    6,
    7
  ]', '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (173, '血管炎二项定量(ANCA-MPO， ANCA-PR3)', '1个工作日', '化学发光法', '黄头管', 20, 1.00, '血清2.0ml', 1, 1,
        NULL, '[
    6,
    7
  ]', '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (174, '心肌酶四项（LDH、CK、CK-MB、a-HBDH）', '1个工作日', NULL, '黄头管', 17, 1.00, '血清1.5ml', 1, 1, NULL, '[
  6,
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (175, '血小板相关抗体检测（PAIgG/A/M）', '1个工作日', '流式细胞术', '紫头管', 27, 1.00, 'EDTA抗凝全血2ml', 1, 1,
        NULL, '[
    6,
    7
  ]', '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (176, '抑制素B', '1个工作日', '免疫荧光层析法', '黄头管', 36, 1.00, '血清0.5ml', 1, 1, NULL, '[
  6,
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (177, '电解质四项(K、Na、Cl、Ca)', '1个工作日', '', '黄头管', 35, 1.00, '血清1.0ml（请及时分装血清送检）', 1, 1, NULL,
        '[
          6,
          7
        ]', '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (178, '抗滋养层细胞膜抗体IgM测定(ATA-IgM)', '1个工作日', '胶体金法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 1,
        NULL, '[
    6,
    7
  ]', '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (179, '抗HCG抗体(AHCG-Ab)', '1个工作日', '胶体金法/酶联免疫法', '黄头管', 36, 1.00, '血清 0.5ml', 1, 1, NULL,
        NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (180, '胰岛素抗体', '1个工作日', '化学发光法', '黄头管', 16, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (181, '雄烯二酮', '2个工作日', '化学发光法', '黄头管', 13, 1.00, '血清2.0ml', 1, 2, NULL, '[
  6,
  7
]', '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (182, '男性肿瘤标志物常规筛查7项:(CEA、AFP、CA50、CA199，FERR、TPSA、FPSA)', '1个工作日', '化学发光法', '黄头管', 33,
        1.00, '血清2.0ml', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (183, '肿瘤常规标志物检测七项(女):(CEA、AFP、CA125、A153、CA199、FERR、B-HCG)', '1个工作日', '化学发光法', '黄头管',
        33, 1.00, '血清2.0ml', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (184, '戊型肝炎病毒RNA荧光定性', '3个工作日', '荧光PCR', '黄头管', 21, 1.00, '血清2ml', 1, 3, NULL, '[
  6,
  7
]', '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (185, '女性肿瘤标志物12项（CEA、AFP、CA50、CA125、CA153、CA199、CA242、CA724、SCC、CY21-1、FERR、β-HCG）', '1个工作日',
        '化学发光法', '黄头管', 33, 1.00, '血清3.0ml', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (186, '鳞状细胞癌相关抗原（SCC）', '1个工作日', '化学发光法', '黄头管', 33, 1.00, '血清0.6ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (187, '弓形虫抗体IgM（TOXO-IgM）定性测定', '2个工作日', '酶联免疫法', '黄头管', 36, 1.00, '血清或血浆0.5ml', 1, 2,
        NULL, '[
    6,
    7
  ]', '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (188, '单纯疱疹病毒Ⅰ型抗体IgM定性(HSVⅠ-IgM)', '2个工作日', '酶联免疫法', '黄头管', 36, 1.00, '血清或血浆0.5ml',
        1, 2, NULL, '[
    6,
    7
  ]', '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (189, '单纯疱疹病毒Ⅱ型抗体IgM定性(HSV-Ⅱ-IgM)', '2个工作日', '酶联免疫法', '黄头管', 36, 1.00, '血清或血浆0.5ml',
        1, 2, NULL, '[
    6,
    7
  ]', '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (190, '血清超敏肌钙蛋白Ⅰ（TNI-HS）', '1个工作日', '化学发光法', '黄头管', 17, 1.00, '血清1.0ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (191, '血清肌红蛋白测定(MYOGB)', '1个工作日', '化学发光法', '黄头管', 17, 1.00, '血清1.0ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (192, '风疹病毒抗体IgM定性(RV-IgM)', '1个工作日', '酶联免疫法', '黄头管', 36, 1.00, '血清或血浆0.5ml', 1, 1,
        NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (193, '巨细胞病毒抗体IgM(CMV-IgM)定性', '1个工作日', '酶联免疫法', '黄头管', 36, 1.00, '血清或血浆0.5ml', 1, 1,
        NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (194, '抗甲状腺微粒体抗体（TMAB）', '1个工作日', '化学发光法', '黄头管', 14, 1.00, '血清0.5ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (195, '抗角质蛋白抗体（AKA）', '1（除周日）', '酶联免疫法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (196, '抗肾小球基底膜抗体(GBM)', '1个工作日', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, NULL,
        '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (197, '血清白蛋白（ALB）', '1个工作日', '化学法', '黄头管', 17, 1.00, '血清0.3ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (198, 'ANA抗体谱十一项（PO、dsDNA、Nuc、Scl-70、SS-A、His、Jo-1、SS-B/La、Sm、nRNP/Sm、CENPB）', '1天', '化学发光法',
        '黄头管', 20, 1.00, '血清3.0ml', 1, 1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (199, '类风湿2项(AKA、CCP)', '1个工作日', '化学发光法+间接免疫荧光法', '黄头管', 20, 1.00, '血清或血浆0.5ml', 1,
        1, NULL, NULL, '2', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (200, '结核感染T细胞测定(T-SPOT)', '3个工作日', '免疫斑点法', '肝素钠/肝素锂抗凝管', 23, 1.00,
        '全血（成年人或 10 岁以上儿童：8m静脉血\n2-9 岁儿童：4mL 静脉血\n2 岁以下儿童：2mL 静脉血\n胸腹水（20-30ml）        （肝素钠/肝素锂抗凝管）',
        1, 3, NULL, '[
    6,
    7
  ]', '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (201, '抗ENA抗体12项', '2天', '免疫印迹法', '黄头管', 20, 1.00, '血清3.0ml', 1, 2, NULL, NULL, '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (202, '抗核抗体谱-12S（CenpB、dsDNA、rRNP、SmD1、SSB/La、SCL-70、SSA-R060、SSA-Ro52、Jo-1、U1-snRNP，His、Nuc）', '2天',
        '免疫印迹法', '黄头管', 20, 1.00, '血清3.0ml', 1, 2, NULL, NULL, '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (203,
        '抗核抗体谱-17S（CenpB、Ku、PCNA、PM-Sc\nSSB/La、Jo-1、U1-snRNP、Mi、AMA-M2\ndsDNA、rRNP、SmD1、SCL-70、SSA-Ro60\nSSA-Ro52、His、Nuc）',
        '2天', '免疫印迹法', '黄头管', 20, 1.00, '血清3.0ml', 1, 2, NULL, NULL, '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (204, '抗核周因子(APE)', '2天', '间接免疫荧光法', '黄头管', 20, 1.00, '血清1.0ml', 1, 2, NULL, NULL, '24', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (205, '抗角蛋白抗体(AKA)', '1（除周日）', '酶联免疫法', '黄头管', 20, 1.00, '血清0.5ml', 1, 1, NULL, '[
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (206, '抗角蛋白抗体(AKA)', '3个工作日', '间接免疫荧光法', '黄头管', 20, 1.00, '血清1.0ml', 1, 3, NULL, '[
  6,
  7
]', '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (207, '抗a-胞衬蛋白抗体', '7个工作日', '酶联免疫法', '黄头管', 36, 1.00, '血清0.5ml', 1, 7, NULL, '[
  6,
  7
]', '24', NULL, '[
  2
]', 24);
INSERT INTO `project_master`
VALUES (208, '抗波形蛋白/心磷脂复合物抗体', '7个工作日', '酶联免疫法', '黄头管', 36, 1.00, '血清0.5ml', 1, 7, NULL, '[
  6,
  7
]', '24', NULL, '[
  2
]', 24);
INSERT INTO `project_master`
VALUES (209, '抗磷脂酰丝氨酸/凝血酶原IgM抗体', '7个工作日', '化学发光法', '黄头管', 36, 1.00, '血清0.5ml', 1, 7, NULL,
        '[
          6,
          7
        ]', '24', NULL, '[
    2
  ]', 24);
INSERT INTO `project_master`
VALUES (210, '抗磷脂酰乙醇胺抗体', '7个工作日', '酶联免疫法', '黄头管', 36, 1.00, '血清0.5ml', 1, 7, NULL, '[
  6,
  7
]', '24', NULL, '[
  2
]', 24);
INSERT INTO `project_master`
VALUES (211, '抗膜联蛋白A5 IgG抗体', '7个工作日', '化学发光法', '黄头管', 36, 1.00, '血清0.5ml', 1, 7, NULL, '[
  6,
  7
]', '24', NULL, '[
  2
]', 24);
INSERT INTO `project_master`
VALUES (212, '抗膜联蛋白A2抗体', '7个工作日', '酶联免疫法', '黄头管', 36, 1.00, '血清0.5ml', 1, 7, NULL, '[
  6,
  7
]', '24', NULL, '[
  2
]', 24);
INSERT INTO `project_master`
VALUES (213, '抗内皮细胞抗体', '7个工作日', '酶联免疫法', '黄头管', 36, 1.00, '血清0.5ml', 1, 7, NULL, '[
  6,
  7
]', '24', NULL, '[
  2
]', 24);
INSERT INTO `project_master`
VALUES (214, '抗凝血素抗体-IgG', '7个工作日', '化学发光法', '黄头管', 36, 1.00, '血清0.5ml', 1, 7, NULL, '[
  6,
  7
]', '24', NULL, '[
  2
]', 24);
INSERT INTO `project_master`
VALUES (215, '抗凝血素抗体-IgM', '7个工作日', '化学发光法', '黄头管', 36, 1.00, '血清0.5ml', 1, 7, NULL, '[
  6,
  7
]', '24', NULL, '[
  2
]', 24);
INSERT INTO `project_master`
VALUES (216, '抗着丝点抗体（CENPB）', '1天', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (217, '血栓弹力图实验(TEG)', '2个工作日', '免疫学方法', '蓝头管', 31, 1.00, '1：9枸橼酸钠抗凝血4.0ml', 1, 2, NULL,
        '[
          6,
          7
        ]', '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (218, '血栓相关检测四项(TAT、TM、PIC、t-PAI-C)', '2个工作日', '化学发光法', '蓝头管', 31, 1.00,
        '1:9枸橼酸钠抗凝全血2.0ml(及时分离血浆)', 1, 2, NULL, '[
    6,
    7
  ]', '24', NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (219, '抗CENP-B抗体', '1天', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (220, '超敏C反应蛋白（hs-CRP）', '1天', '化学发光法', '黄头管', 20, 1.00, '血清0.6ml', 1, 1, NULL, NULL, '2', NULL,
        '[
          1
        ]', 21);
INSERT INTO `project_master`
VALUES (221, '红细胞沉降率（ESR）', '1个工作日', '魏氏法', '蓝头管', 30, 1.00, '1:4枸橼酸钠抗凝血', 1, 1, NULL, NULL, '2',
        NULL, '[
    1
  ]', 21);
INSERT INTO `project_master`
VALUES (222, '孕妇外周血胎儿游离DNA产前诊断', '7个工作日', '高通量测序法', 'cfDNA专用管', 36, 1.00, '全血7ml', 1, 7,
        NULL, '[
    6,
    7
  ]', '24', NULL, '[
    null
  ]', 22);
INSERT INTO `project_master`
VALUES (223, '孕妇外周血胎儿游离DNA产前诊断2.0', '7个工作日', '高通量测序法', 'cfDNA专用管', 36, 1.00, '全血7ml', 1, 7,
        NULL, '[
    6,
    7
  ]', '24', NULL, '[
    null
  ]', 22);
INSERT INTO `project_master`
VALUES (227, '三碘甲状腺原氨酸（T3）', '当天', '/', '蓝头管', 14, 1.00, NULL, 1, 0, NULL, NULL, NULL, NULL, '[
  2
]', 18);
INSERT INTO `project_master`
VALUES (228, 'β人绒毛膜促性腺激素（HCG）', '4小时', '/', '黄头管', 13, 1.00, NULL, 0, NULL, 4, NULL, '24', NULL, '[]',
        18);
INSERT INTO `project_master`
VALUES (229, '凝血五项', '当天', '/', '蓝头管', 31, 1.00, NULL, 1, 0, NULL, NULL, NULL, NULL, '[
  2
]', 19);
INSERT INTO `project_master`
VALUES (230, '精液常规+形态学分析', '当天', '/', '取精杯', 26, 1.00, NULL, 1, 0, NULL, NULL, NULL, NULL, '[
  2
]', 20);
INSERT INTO `project_master`
VALUES (231, '无创产前DNA检测NIPT', '7个工作日', NULL, NULL, 38, 1.00, NULL, 1, 7, NULL, '[
  6,
  7
]', '24', NULL, '[
  3
]', 22);
INSERT INTO `project_master`
VALUES (232, '扩展性无创产前DNA检测 NIPT PLUS', '7个工作日', NULL, NULL, 38, 1.00, NULL, 1, 7, NULL, '[
  6,
  7
]', '24', NULL, '[
  3
]', 22);
INSERT INTO `project_master`
VALUES (233, '沙眼衣原体抗原检测', '2个工作日', NULL, NULL, 22, 1.00, NULL, 1, 2, NULL, '[
  6,
  7
]', '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (234, '游离甲功三项', '1个工作日', NULL, NULL, 14, 1.00, NULL, 1, 1, NULL, '[
  6,
  7
]', '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (235, '抗卵巢抗体(AOAb)', '1个工作日', NULL, NULL, 36, 1.00, NULL, 1, 1, NULL, '[
  6,
  7
]', '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (236, '抗心磷脂抗体（IgG,IGM,IgA)定性（单项）', '2-4个工作日', NULL, NULL, 36, 1.00, NULL, 1, 4, NULL, '[
  6,
  7
]', '24', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (237, '血脂七项', '1个工作日', NULL, NULL, 17, 1.00, NULL, 1, 1, NULL, '[
  6,
  7
]', '2', NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (238, '梅毒螺旋体病毒抗体检测', '当天', NULL, NULL, 18, 1.00, NULL, 1, 0, NULL, NULL, '2', NULL, '[
  2
]', 21);
INSERT INTO `project_master`
VALUES (239, 'G6PD', '1天', '速率法', '紫头管', 30, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  1,
  null
]', 21);
INSERT INTO `project_master`
VALUES (241, '传染四项', '1', '胶体金法', '黄头管', 18, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  null
]', 19);
INSERT INTO `project_master`
VALUES (242, '传染五项', '1', '胶体金法+凝集法', '黄头管', 18, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  null
]', 19);
INSERT INTO `project_master`
VALUES (243, '性激素2项（E2、LH）', '1', '电化学发光法', '黄头管', 13, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  null
]', 18);
INSERT INTO `project_master`
VALUES (244, '性激素2项（E2、P）', '1', '电化学发光法', '黄头管', 13, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  null
]', 18);
INSERT INTO `project_master`
VALUES (245, '凝血五项', '1', '微流控光学法', '蓝头管', 31, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  2
]', 19);
INSERT INTO `project_master`
VALUES (247, '男性肿瘤标志物12项', '1天', '化学发光法', '黄头管', 33, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  1,
  null
]', 21);
INSERT INTO `project_master`
VALUES (248, '生化全套26项', '1天', '化学发光法', '黄头管', 17, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  1
]', 21);
INSERT INTO `project_master`
VALUES (249, '单纯疱疹病毒Ⅰ型抗体IgG定性(HSVⅠ-IgG)', '1天', '酶联免疫法', '黄头管', 36, 1.00, NULL, 1, NULL, NULL, NULL,
        NULL, NULL, '[
    1,
    null
  ]', 21);
INSERT INTO `project_master`
VALUES (250, 'G6PD基因检测', '3-5', '导流杂交', '紫头管', 34, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  1,
  null
]', 21);
INSERT INTO `project_master`
VALUES (251, '抗核小体抗体(Nus)', '3-5', '化学发光法', '黄头管', 38, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  1,
  null
]', 21);
INSERT INTO `project_master`
VALUES (252, '叶酸代谢能力基因(ITHFR)检测', '3-5', '荧光PCR', '紫头管', 38, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL,
        '[
          1,
          null
        ]', 21);
INSERT INTO `project_master`
VALUES (253, '结核感染T细胞斑点实验(T-SPOT.TB)', '3-5', '免疫斑点法', '绿头管', 38, 1.00, NULL, 1, NULL, NULL, NULL,
        NULL, NULL, '[
    1,
    null
  ]', 21);
INSERT INTO `project_master`
VALUES (254, '抗c1q抗体IgG定量测定(c1g-IgG)', '3-5', '化学发光法', '黄头管', 38, 1.00, NULL, 1, NULL, NULL, NULL, NULL,
        NULL, '[
    1,
    null
  ]', 21);
INSERT INTO `project_master`
VALUES (255, '红细胞叶酸谱十一项(AL)', '3-5', '色谱法', '紫头管', 38, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  2,
  null
]', 24);
INSERT INTO `project_master`
VALUES (256, 'NK细胞毒性检测', '3-5', '/', '紫头管', 38, 1.00, NULL, 1, NULL, NULL, NULL, NULL, NULL, '[
  2
]', 21);

-- ----------------------------
-- Table structure for project_references
-- ----------------------------
DROP TABLE IF EXISTS `project_references`;
CREATE TABLE `project_references`
(
    `id`                       bigint NOT NULL AUTO_INCREMENT COMMENT '引用索引ID（key）',
    `project_id`               bigint NOT NULL COMMENT '项目ID',
    `user_group_id`            bigint NOT NULL COMMENT '用户组ID',
    `price_coefficient`        decimal(10, 6) NULL DEFAULT 1.000000 COMMENT '折扣系数',
    `creation_time`            datetime NULL DEFAULT NULL,
    `update_time`              datetime NULL DEFAULT NULL,
    `default_delivery_unit_id` bigint NULL DEFAULT NULL COMMENT '默认外送单位id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `project_id&user_group_id` (`project_id` ASC, `user_group_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9487
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '项目引用表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_references
-- ----------------------------
INSERT INTO `project_references`
VALUES (8, 8, 2, 1.000000, '2024-11-15 14:08:07', '2025-04-10 17:07:35', 3);
INSERT INTO `project_references`
VALUES (9, 36, 2, 0.700000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (11, 16, 2, 0.700000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (12, 1, 16, 0.700000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (13, 2, 16, 0.700000, '2024-11-15 14:08:07', '2025-04-11 14:58:44', 1);
INSERT INTO `project_references`
VALUES (14, 3, 16, 0.700000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (15, 4, 16, 0.700000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (16, 5, 16, 0.700000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (17, 6, 16, 0.700000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (18, 7, 16, 0.700000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (19, 8, 16, 0.700000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (20, 22, 16, 0.800000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (21, 17, 16, 0.700000, '2024-11-15 14:08:07', '2024-11-15 14:08:07', NULL);
INSERT INTO `project_references`
VALUES (197, 3, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (198, 4, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (199, 5, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (200, 6, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (201, 7, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (202, 8, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (203, 9, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (204, 10, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (205, 11, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (206, 12, 1, 1.000000, '2024-12-12 09:50:45', '2025-04-15 16:47:42', 2);
INSERT INTO `project_references`
VALUES (207, 13, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (208, 14, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (209, 15, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (210, 16, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (211, 17, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (212, 18, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (213, 19, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (214, 20, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (215, 21, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (216, 22, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (217, 23, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (218, 24, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (219, 25, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (220, 26, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (221, 27, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (222, 28, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (223, 29, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (224, 30, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (225, 31, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (226, 32, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (227, 33, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (228, 34, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (229, 35, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (230, 36, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (231, 37, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (232, 38, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (233, 39, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (234, 40, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (235, 41, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (236, 42, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (237, 43, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (238, 44, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (239, 45, 1, 1.000000, '2024-12-12 09:50:45', NULL, NULL);
INSERT INTO `project_references`
VALUES (240, 46, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (241, 47, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (242, 48, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (243, 49, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (244, 50, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (245, 51, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (246, 52, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (247, 53, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (248, 54, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (249, 55, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (250, 56, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (251, 57, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (252, 58, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (253, 59, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (254, 60, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (255, 61, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (256, 62, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (257, 63, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (258, 64, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (259, 65, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (260, 66, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (261, 67, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (262, 68, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (263, 69, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (264, 70, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (265, 71, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (266, 93, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (267, 94, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (268, 95, 1, 1.000000, '2024-12-12 09:50:46', NULL, NULL);
INSERT INTO `project_references`
VALUES (269, 96, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (270, 97, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (271, 98, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (272, 99, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (273, 100, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (274, 101, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (275, 102, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (276, 103, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (277, 104, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (278, 105, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (279, 106, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (280, 107, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (281, 108, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (282, 109, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (283, 110, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (284, 111, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (285, 112, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (286, 113, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (287, 114, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (288, 115, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (289, 116, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (290, 117, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (291, 118, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (292, 119, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (293, 120, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (294, 121, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (295, 122, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (296, 123, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (297, 124, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (298, 125, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (299, 126, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (300, 127, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (301, 128, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (302, 129, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (303, 130, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (304, 131, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (305, 132, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (306, 133, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (307, 134, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (308, 135, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (309, 136, 1, 1.000000, '2024-12-12 09:50:47', NULL, NULL);
INSERT INTO `project_references`
VALUES (310, 137, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (311, 138, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (312, 139, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (313, 140, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (314, 141, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (315, 142, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (316, 143, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (317, 144, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (318, 145, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (319, 146, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (320, 147, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (321, 148, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (322, 149, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (323, 150, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (324, 151, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (325, 152, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (326, 153, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (327, 154, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (328, 155, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (329, 156, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (330, 157, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (331, 158, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (332, 159, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (333, 160, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (334, 161, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (335, 162, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (336, 163, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (337, 164, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (338, 165, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (339, 166, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (340, 167, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (341, 168, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (342, 169, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (343, 170, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (344, 171, 1, 1.000000, '2024-12-12 09:50:48', NULL, NULL);
INSERT INTO `project_references`
VALUES (345, 172, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (346, 173, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (347, 174, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (348, 175, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (349, 176, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (350, 177, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (351, 178, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (352, 179, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (353, 180, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (354, 181, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (355, 182, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (356, 183, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (357, 184, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (358, 185, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (359, 186, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (360, 187, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (361, 188, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (362, 189, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (363, 190, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (364, 191, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (365, 192, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (367, 194, 1, 1.000000, '2024-12-12 09:50:49', NULL, NULL);
INSERT INTO `project_references`
VALUES (395, 9, 2, 1.000000, '2024-12-18 11:29:14', NULL, NULL);
INSERT INTO `project_references`
VALUES (408, 223, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (409, 222, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (410, 215, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (411, 214, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (412, 213, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (413, 212, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (414, 211, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (415, 210, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (416, 209, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (417, 208, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (418, 207, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (419, 92, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (420, 91, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (421, 90, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (422, 89, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (423, 88, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (424, 84, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (425, 83, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (426, 82, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (427, 220, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (428, 219, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (429, 216, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (430, 206, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (431, 205, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (432, 204, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (433, 203, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (434, 202, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (435, 201, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (436, 199, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (437, 198, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (438, 196, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (439, 195, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (440, 87, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (441, 86, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (442, 85, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (443, 221, 1, 1.000000, '2024-12-20 13:06:11', NULL, NULL);
INSERT INTO `project_references`
VALUES (444, 2, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (445, 1, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (446, 197, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (447, 81, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (448, 80, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (449, 79, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (450, 78, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (451, 77, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (452, 76, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (453, 75, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (454, 74, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (455, 73, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (456, 72, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (457, 200, 1, 1.000000, '2024-12-20 13:07:20', NULL, NULL);
INSERT INTO `project_references`
VALUES (458, 218, 1, 1.000000, '2024-12-20 13:08:13', NULL, NULL);
INSERT INTO `project_references`
VALUES (459, 217, 1, 1.000000, '2024-12-20 13:08:13', NULL, NULL);
INSERT INTO `project_references`
VALUES (5687, 1, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:57', NULL);
INSERT INTO `project_references`
VALUES (5688, 1, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5689, 1, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5690, 1, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5691, 1, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5692, 1, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5693, 1, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5694, 1, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5695, 2, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:57', NULL);
INSERT INTO `project_references`
VALUES (5696, 2, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5697, 2, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5698, 2, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5699, 2, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5700, 2, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5701, 2, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5702, 2, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5703, 3, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5704, 3, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5705, 3, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5706, 3, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5707, 3, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5708, 3, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5709, 3, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5710, 3, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5711, 4, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5712, 4, 19, 0.600000, '2024-12-27 15:34:36', NULL, 1);
INSERT INTO `project_references`
VALUES (5713, 4, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5714, 4, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5715, 4, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5716, 4, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5717, 4, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5718, 4, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5719, 5, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5720, 5, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5721, 5, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5722, 5, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5723, 5, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5724, 5, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5725, 5, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5726, 5, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5727, 6, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5728, 6, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5729, 6, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5730, 6, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5731, 6, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5732, 6, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5733, 6, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5734, 6, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5735, 7, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5736, 7, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5737, 7, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5738, 7, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5739, 7, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5740, 7, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5741, 7, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5742, 7, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5743, 8, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5744, 8, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5745, 8, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5746, 8, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5747, 8, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5748, 8, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5749, 8, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5750, 8, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5751, 9, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5752, 9, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5753, 9, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5754, 9, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5755, 9, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5756, 9, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5757, 9, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5758, 9, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5759, 10, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5760, 10, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5761, 10, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5762, 10, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5763, 10, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5764, 10, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5765, 10, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5766, 10, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5767, 11, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5768, 11, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5769, 11, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5770, 11, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5771, 11, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5772, 11, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5773, 11, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5774, 11, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5775, 12, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5776, 12, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5777, 12, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5778, 12, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5779, 12, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5780, 12, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5781, 12, 24, 0.550000, NULL, '2025-04-15 16:48:22', NULL);
INSERT INTO `project_references`
VALUES (5782, 12, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5783, 13, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5784, 13, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5785, 13, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5786, 13, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5787, 13, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5788, 13, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5789, 13, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5790, 13, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5791, 15, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5792, 15, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5793, 15, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5794, 15, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5795, 15, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5796, 15, 23, 0.420000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5797, 15, 24, 0.550000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5798, 15, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5799, 16, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5800, 16, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5801, 16, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5802, 16, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5803, 16, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5804, 16, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5805, 16, 24, 0.500000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5806, 16, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5807, 17, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5808, 17, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5809, 17, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5810, 17, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5811, 17, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5812, 17, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5813, 17, 24, 0.500000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5814, 17, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5815, 19, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5816, 19, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5817, 19, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5818, 19, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5819, 19, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5820, 19, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5821, 19, 24, 0.500000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5822, 19, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5823, 22, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:57', 1);
INSERT INTO `project_references`
VALUES (5824, 22, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5825, 22, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5826, 22, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5827, 22, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5828, 22, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5829, 22, 24, 0.360000, NULL, '2025-04-27 16:50:31', 1);
INSERT INTO `project_references`
VALUES (5830, 22, 26, 0.303600, NULL, '2025-04-28 13:46:23', 1);
INSERT INTO `project_references`
VALUES (5831, 23, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:57', 1);
INSERT INTO `project_references`
VALUES (5832, 23, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5833, 23, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5834, 23, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5835, 23, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5836, 23, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5837, 23, 24, 0.360000, NULL, '2025-04-27 16:50:31', 1);
INSERT INTO `project_references`
VALUES (5838, 23, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5839, 24, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:58', 1);
INSERT INTO `project_references`
VALUES (5840, 24, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5841, 24, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5842, 24, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5843, 24, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5844, 24, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5845, 24, 24, 0.360000, NULL, '2025-04-27 16:50:31', 1);
INSERT INTO `project_references`
VALUES (5846, 24, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5847, 25, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:58', 1);
INSERT INTO `project_references`
VALUES (5848, 25, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5849, 25, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5850, 25, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5851, 25, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5852, 25, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5853, 25, 24, 0.360000, NULL, '2025-04-27 16:50:32', 1);
INSERT INTO `project_references`
VALUES (5854, 25, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5855, 26, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:58', 1);
INSERT INTO `project_references`
VALUES (5856, 26, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5857, 26, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5858, 26, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5859, 26, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5860, 26, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5861, 26, 24, 0.360000, NULL, '2025-04-27 16:50:32', 1);
INSERT INTO `project_references`
VALUES (5862, 26, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5863, 27, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:58', 1);
INSERT INTO `project_references`
VALUES (5864, 27, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5865, 27, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5866, 27, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5867, 27, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5868, 27, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5869, 27, 24, 0.360000, NULL, '2025-04-27 16:50:32', 1);
INSERT INTO `project_references`
VALUES (5870, 27, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5871, 28, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:58', 1);
INSERT INTO `project_references`
VALUES (5872, 28, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5873, 28, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5874, 28, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5875, 28, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5876, 28, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5877, 28, 24, 0.360000, NULL, '2025-04-27 16:50:32', 1);
INSERT INTO `project_references`
VALUES (5878, 28, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5879, 29, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:58', 1);
INSERT INTO `project_references`
VALUES (5880, 29, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5881, 29, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5882, 29, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5883, 29, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5884, 29, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5885, 29, 24, 0.360000, NULL, '2025-04-27 16:50:32', 1);
INSERT INTO `project_references`
VALUES (5886, 29, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5887, 30, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:58', 1);
INSERT INTO `project_references`
VALUES (5888, 30, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5889, 30, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5890, 30, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5891, 30, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5892, 30, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5893, 30, 24, 0.360000, NULL, '2025-04-27 16:50:32', 1);
INSERT INTO `project_references`
VALUES (5894, 30, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5895, 31, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:58', NULL);
INSERT INTO `project_references`
VALUES (5896, 31, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5897, 31, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5898, 31, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5899, 31, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5900, 31, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5901, 31, 24, 0.500000, NULL, '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (5902, 31, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5903, 32, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:59', 1);
INSERT INTO `project_references`
VALUES (5904, 32, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5905, 32, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5906, 32, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5907, 32, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5908, 32, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5909, 32, 24, 0.360000, NULL, '2025-04-27 16:50:32', 1);
INSERT INTO `project_references`
VALUES (5910, 32, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5911, 33, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:59', 1);
INSERT INTO `project_references`
VALUES (5912, 33, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5913, 33, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5914, 33, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5915, 33, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5916, 33, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5917, 33, 24, 0.360000, NULL, '2025-04-27 16:50:32', 1);
INSERT INTO `project_references`
VALUES (5918, 33, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5919, 34, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:59', 1);
INSERT INTO `project_references`
VALUES (5920, 34, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5921, 34, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5922, 34, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5923, 34, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5924, 34, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5925, 34, 24, 0.360000, NULL, '2025-04-27 16:50:33', 2);
INSERT INTO `project_references`
VALUES (5926, 34, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5927, 35, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:59', 1);
INSERT INTO `project_references`
VALUES (5928, 35, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5929, 35, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5930, 35, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5931, 35, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5932, 35, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5933, 35, 24, 0.360000, NULL, '2025-04-27 16:50:33', 2);
INSERT INTO `project_references`
VALUES (5934, 35, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5935, 36, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:49:59', 1);
INSERT INTO `project_references`
VALUES (5936, 36, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5937, 36, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5938, 36, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5939, 36, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5940, 36, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5941, 36, 24, 0.360000, NULL, '2025-04-27 16:50:33', 2);
INSERT INTO `project_references`
VALUES (5942, 36, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5943, 37, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:00', 1);
INSERT INTO `project_references`
VALUES (5944, 37, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5945, 37, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5946, 37, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5947, 37, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5948, 37, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5949, 37, 24, 0.360000, NULL, '2025-04-27 16:50:33', 2);
INSERT INTO `project_references`
VALUES (5950, 37, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5951, 38, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:00', 1);
INSERT INTO `project_references`
VALUES (5952, 38, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5953, 38, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5954, 38, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5955, 38, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5956, 38, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5957, 38, 24, 0.360000, NULL, '2025-04-27 16:50:34', 2);
INSERT INTO `project_references`
VALUES (5958, 38, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5959, 39, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:00', 1);
INSERT INTO `project_references`
VALUES (5960, 39, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5961, 39, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5962, 39, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5963, 39, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5964, 39, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5965, 39, 24, 0.360000, NULL, '2025-04-27 16:50:34', 2);
INSERT INTO `project_references`
VALUES (5966, 39, 26, 0.303600, NULL, '2025-04-28 13:46:24', 1);
INSERT INTO `project_references`
VALUES (5967, 40, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:00', 1);
INSERT INTO `project_references`
VALUES (5968, 40, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5969, 40, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5970, 40, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5971, 40, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5972, 40, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5973, 40, 24, 0.360000, NULL, '2025-04-27 16:50:34', 2);
INSERT INTO `project_references`
VALUES (5974, 40, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (5975, 41, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:00', 1);
INSERT INTO `project_references`
VALUES (5976, 41, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5977, 41, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5978, 41, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5979, 41, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5980, 41, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5981, 41, 24, 0.360000, NULL, '2025-04-27 16:50:34', 2);
INSERT INTO `project_references`
VALUES (5982, 41, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (5983, 42, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:00', 1);
INSERT INTO `project_references`
VALUES (5984, 42, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5985, 42, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5986, 42, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5987, 42, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5988, 42, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5989, 42, 24, 0.360000, NULL, '2025-04-27 16:50:34', 2);
INSERT INTO `project_references`
VALUES (5990, 42, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (5991, 43, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:00', 1);
INSERT INTO `project_references`
VALUES (5992, 43, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5993, 43, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5994, 43, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (5995, 43, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5996, 43, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (5997, 43, 24, 0.360000, NULL, '2025-04-27 16:50:34', 2);
INSERT INTO `project_references`
VALUES (5998, 43, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (5999, 44, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:01', 1);
INSERT INTO `project_references`
VALUES (6000, 44, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6001, 44, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6002, 44, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6003, 44, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6004, 44, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6005, 44, 24, 0.360000, NULL, '2025-04-27 16:50:34', 2);
INSERT INTO `project_references`
VALUES (6006, 44, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6007, 45, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:01', 1);
INSERT INTO `project_references`
VALUES (6008, 45, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6009, 45, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6010, 45, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6011, 45, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6012, 45, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6013, 45, 24, 0.360000, NULL, '2025-04-27 16:50:35', 2);
INSERT INTO `project_references`
VALUES (6014, 45, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6015, 46, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:01', 1);
INSERT INTO `project_references`
VALUES (6016, 46, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6017, 46, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6018, 46, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6019, 46, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6020, 46, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6021, 46, 24, 0.360000, NULL, '2025-04-27 16:50:35', 2);
INSERT INTO `project_references`
VALUES (6022, 46, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6023, 47, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:01', 1);
INSERT INTO `project_references`
VALUES (6024, 47, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6025, 47, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6026, 47, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6027, 47, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6028, 47, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6029, 47, 24, 0.360000, NULL, '2025-04-27 16:50:35', 2);
INSERT INTO `project_references`
VALUES (6030, 47, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6031, 48, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:01', 1);
INSERT INTO `project_references`
VALUES (6032, 48, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6033, 48, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6034, 48, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6035, 48, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6036, 48, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6037, 48, 24, 0.360000, NULL, '2025-04-27 16:50:35', 1);
INSERT INTO `project_references`
VALUES (6038, 48, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6039, 49, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:02', 1);
INSERT INTO `project_references`
VALUES (6040, 49, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6041, 49, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6042, 49, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6043, 49, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6044, 49, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6045, 49, 24, 0.360000, NULL, '2025-04-27 16:50:35', 1);
INSERT INTO `project_references`
VALUES (6046, 49, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6047, 50, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:02', 1);
INSERT INTO `project_references`
VALUES (6048, 50, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6049, 50, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6050, 50, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6051, 50, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6052, 50, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6053, 50, 24, 0.360000, NULL, '2025-04-27 16:50:35', 1);
INSERT INTO `project_references`
VALUES (6054, 50, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6055, 51, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:02', 1);
INSERT INTO `project_references`
VALUES (6056, 51, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6057, 51, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6058, 51, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6059, 51, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6060, 51, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6061, 51, 24, 0.360000, NULL, '2025-04-27 16:50:35', 1);
INSERT INTO `project_references`
VALUES (6062, 51, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6063, 52, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:02', 1);
INSERT INTO `project_references`
VALUES (6064, 52, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6065, 52, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6066, 52, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6067, 52, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6068, 52, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6069, 52, 24, 0.360000, NULL, '2025-04-27 16:50:35', 1);
INSERT INTO `project_references`
VALUES (6070, 52, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6071, 53, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:02', 1);
INSERT INTO `project_references`
VALUES (6072, 53, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6073, 53, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6074, 53, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6075, 53, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6076, 53, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6077, 53, 24, 0.360000, NULL, '2025-04-27 16:50:36', 1);
INSERT INTO `project_references`
VALUES (6078, 53, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6079, 54, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:02', 1);
INSERT INTO `project_references`
VALUES (6080, 54, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6081, 54, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6082, 54, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6083, 54, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6084, 54, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6085, 54, 24, 0.360000, NULL, '2025-04-27 16:50:36', 1);
INSERT INTO `project_references`
VALUES (6086, 54, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6087, 55, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:03', 1);
INSERT INTO `project_references`
VALUES (6088, 55, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6089, 55, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6090, 55, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6091, 55, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6092, 55, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6093, 55, 24, 0.360000, NULL, '2025-04-27 16:50:36', 1);
INSERT INTO `project_references`
VALUES (6094, 55, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6095, 56, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:03', 1);
INSERT INTO `project_references`
VALUES (6096, 56, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6097, 56, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6098, 56, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6099, 56, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6100, 56, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6101, 56, 24, 0.360000, NULL, '2025-04-27 16:50:36', 2);
INSERT INTO `project_references`
VALUES (6102, 56, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6103, 57, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:03', 1);
INSERT INTO `project_references`
VALUES (6104, 57, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6105, 57, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6106, 57, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6107, 57, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6108, 57, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6109, 57, 24, 0.360000, NULL, '2025-04-27 16:50:37', 1);
INSERT INTO `project_references`
VALUES (6110, 57, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6111, 58, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:03', 1);
INSERT INTO `project_references`
VALUES (6112, 58, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6113, 58, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6114, 58, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6115, 58, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6116, 58, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6117, 58, 24, 0.360000, NULL, '2025-04-27 16:50:37', 1);
INSERT INTO `project_references`
VALUES (6118, 58, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6119, 59, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:03', 1);
INSERT INTO `project_references`
VALUES (6120, 59, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6121, 59, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6122, 59, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6123, 59, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6124, 59, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6125, 59, 24, 0.360000, NULL, '2025-04-27 16:50:37', 1);
INSERT INTO `project_references`
VALUES (6126, 59, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6127, 60, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:03', 1);
INSERT INTO `project_references`
VALUES (6128, 60, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6129, 60, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6130, 60, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6131, 60, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6132, 60, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6133, 60, 24, 0.360000, NULL, '2025-04-27 16:50:37', 1);
INSERT INTO `project_references`
VALUES (6134, 60, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6135, 61, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:04', 1);
INSERT INTO `project_references`
VALUES (6136, 61, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6137, 61, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6138, 61, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6139, 61, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6140, 61, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6141, 61, 24, 0.360000, NULL, '2025-04-27 16:50:37', 1);
INSERT INTO `project_references`
VALUES (6142, 61, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6143, 62, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:04', 1);
INSERT INTO `project_references`
VALUES (6144, 62, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6145, 62, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6146, 62, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6147, 62, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6148, 62, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6149, 62, 24, 0.360000, NULL, '2025-04-27 16:50:37', 1);
INSERT INTO `project_references`
VALUES (6150, 62, 26, 0.303600, NULL, '2025-04-28 13:46:25', 1);
INSERT INTO `project_references`
VALUES (6151, 63, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:04', 1);
INSERT INTO `project_references`
VALUES (6152, 63, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6153, 63, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6154, 63, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6155, 63, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6156, 63, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6157, 63, 24, 0.360000, NULL, '2025-04-27 16:50:37', 1);
INSERT INTO `project_references`
VALUES (6158, 63, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6159, 64, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:59', NULL);
INSERT INTO `project_references`
VALUES (6160, 64, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6161, 64, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6162, 64, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6163, 64, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6164, 64, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6165, 64, 24, 0.500000, NULL, '2025-04-28 14:21:42', NULL);
INSERT INTO `project_references`
VALUES (6166, 64, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6167, 65, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:59', NULL);
INSERT INTO `project_references`
VALUES (6168, 65, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6169, 65, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6170, 65, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6171, 65, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6172, 65, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6173, 65, 24, 0.500000, NULL, '2025-04-28 14:21:42', NULL);
INSERT INTO `project_references`
VALUES (6174, 65, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6175, 66, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:59', NULL);
INSERT INTO `project_references`
VALUES (6176, 66, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6177, 66, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6178, 66, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6179, 66, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6180, 66, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6181, 66, 24, 0.500000, NULL, '2025-04-28 14:21:42', NULL);
INSERT INTO `project_references`
VALUES (6182, 66, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6183, 67, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:04', 1);
INSERT INTO `project_references`
VALUES (6184, 67, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6185, 67, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6186, 67, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6187, 67, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6188, 67, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6189, 67, 24, 0.360000, NULL, '2025-04-27 16:50:38', 1);
INSERT INTO `project_references`
VALUES (6190, 67, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6191, 68, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:04', 1);
INSERT INTO `project_references`
VALUES (6192, 68, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6193, 68, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6194, 68, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6195, 68, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6196, 68, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6197, 68, 24, 0.360000, NULL, '2025-04-27 16:50:38', 1);
INSERT INTO `project_references`
VALUES (6198, 68, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6199, 69, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:59', NULL);
INSERT INTO `project_references`
VALUES (6200, 69, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6201, 69, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6202, 69, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6203, 69, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6204, 69, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6205, 69, 24, 0.500000, NULL, '2025-04-28 14:21:42', NULL);
INSERT INTO `project_references`
VALUES (6206, 69, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6207, 70, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:05', 1);
INSERT INTO `project_references`
VALUES (6208, 70, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6209, 70, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6210, 70, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6211, 70, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6212, 70, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6213, 70, 24, 0.360000, NULL, '2025-04-27 16:50:38', 1);
INSERT INTO `project_references`
VALUES (6214, 70, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6215, 71, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:05', 1);
INSERT INTO `project_references`
VALUES (6216, 71, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6217, 71, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6218, 71, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6219, 71, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6220, 71, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6221, 71, 24, 0.360000, NULL, '2025-04-27 16:50:38', 1);
INSERT INTO `project_references`
VALUES (6222, 71, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6223, 72, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:05', 1);
INSERT INTO `project_references`
VALUES (6224, 72, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6225, 72, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6226, 72, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6227, 72, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6228, 72, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6229, 72, 24, 0.360000, NULL, '2025-04-27 16:50:38', 1);
INSERT INTO `project_references`
VALUES (6230, 72, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6231, 73, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:05', 1);
INSERT INTO `project_references`
VALUES (6232, 73, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6233, 73, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6234, 73, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6235, 73, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6236, 73, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6237, 73, 24, 0.360000, NULL, '2025-04-27 16:50:39', 1);
INSERT INTO `project_references`
VALUES (6238, 73, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6239, 74, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:59', NULL);
INSERT INTO `project_references`
VALUES (6240, 74, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6241, 74, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6242, 74, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6243, 74, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6244, 74, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6245, 74, 24, 0.500000, NULL, '2025-04-28 14:21:42', NULL);
INSERT INTO `project_references`
VALUES (6246, 74, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6247, 75, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:59', NULL);
INSERT INTO `project_references`
VALUES (6248, 75, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6249, 75, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6250, 75, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6251, 75, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6252, 75, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6253, 75, 24, 0.360000, NULL, '2025-04-28 14:21:42', NULL);
INSERT INTO `project_references`
VALUES (6254, 75, 26, 0.303600, NULL, '2025-04-28 16:10:08', NULL);
INSERT INTO `project_references`
VALUES (6255, 76, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:05', 1);
INSERT INTO `project_references`
VALUES (6256, 76, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6257, 76, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6258, 76, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6259, 76, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6260, 76, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6261, 76, 24, 0.360000, NULL, '2025-04-27 16:50:39', 1);
INSERT INTO `project_references`
VALUES (6262, 76, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6263, 77, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:59', NULL);
INSERT INTO `project_references`
VALUES (6264, 77, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6265, 77, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6266, 77, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6267, 77, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6268, 77, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6269, 78, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:18:59', NULL);
INSERT INTO `project_references`
VALUES (6270, 78, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6271, 78, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6272, 78, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6273, 78, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6274, 78, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6275, 78, 24, 0.360000, NULL, '2025-04-28 14:21:42', NULL);
INSERT INTO `project_references`
VALUES (6276, 78, 26, 0.303600, NULL, '2025-04-28 16:10:08', NULL);
INSERT INTO `project_references`
VALUES (6277, 79, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:06', 1);
INSERT INTO `project_references`
VALUES (6278, 79, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6279, 79, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6280, 79, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6281, 79, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6282, 79, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6283, 79, 24, 0.360000, NULL, '2025-04-27 16:50:39', 1);
INSERT INTO `project_references`
VALUES (6284, 79, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6285, 80, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:06', 1);
INSERT INTO `project_references`
VALUES (6286, 80, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6287, 80, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6288, 80, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6289, 80, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6290, 80, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6291, 80, 24, 0.360000, NULL, '2025-04-28 14:21:42', NULL);
INSERT INTO `project_references`
VALUES (6292, 80, 26, 0.303600, NULL, '2025-04-28 16:10:09', NULL);
INSERT INTO `project_references`
VALUES (6293, 81, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:06', 1);
INSERT INTO `project_references`
VALUES (6294, 81, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6295, 81, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6296, 81, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6297, 81, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6298, 81, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6299, 81, 24, 0.360000, NULL, '2025-04-27 16:50:40', 1);
INSERT INTO `project_references`
VALUES (6300, 81, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6301, 82, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:06', 1);
INSERT INTO `project_references`
VALUES (6302, 82, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6303, 82, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6304, 82, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6305, 82, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6306, 82, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6307, 82, 24, 0.360000, NULL, '2025-04-27 16:50:40', 1);
INSERT INTO `project_references`
VALUES (6308, 82, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6309, 83, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:06', 1);
INSERT INTO `project_references`
VALUES (6310, 83, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6311, 83, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6312, 83, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6313, 83, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6314, 83, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6315, 83, 24, 0.360000, NULL, '2025-04-27 16:50:40', 1);
INSERT INTO `project_references`
VALUES (6316, 83, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6317, 84, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:06', 1);
INSERT INTO `project_references`
VALUES (6318, 84, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6319, 84, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6320, 84, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6321, 84, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6322, 84, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6323, 84, 24, 0.360000, NULL, '2025-04-27 16:50:40', 1);
INSERT INTO `project_references`
VALUES (6324, 84, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6325, 88, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:06', 1);
INSERT INTO `project_references`
VALUES (6326, 88, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6327, 88, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6328, 88, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6329, 88, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6330, 88, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6331, 88, 24, 0.360000, NULL, '2025-04-27 16:50:40', 1);
INSERT INTO `project_references`
VALUES (6332, 88, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6333, 89, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:06', 1);
INSERT INTO `project_references`
VALUES (6334, 89, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6335, 89, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6336, 89, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6337, 89, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6338, 89, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6339, 89, 24, 0.360000, NULL, '2025-04-27 16:50:40', 1);
INSERT INTO `project_references`
VALUES (6340, 89, 26, 0.303600, NULL, '2025-04-28 13:46:26', 1);
INSERT INTO `project_references`
VALUES (6341, 90, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:06', 1);
INSERT INTO `project_references`
VALUES (6342, 90, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6343, 90, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6344, 90, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6345, 90, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6346, 90, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6347, 90, 24, 0.360000, NULL, '2025-04-27 16:50:40', 1);
INSERT INTO `project_references`
VALUES (6348, 90, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6349, 91, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:07', 1);
INSERT INTO `project_references`
VALUES (6350, 91, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6351, 91, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6352, 91, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6353, 91, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6354, 91, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6355, 91, 24, 0.360000, NULL, '2025-04-27 16:50:40', 1);
INSERT INTO `project_references`
VALUES (6356, 91, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6357, 94, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:07', 1);
INSERT INTO `project_references`
VALUES (6358, 94, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6359, 94, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6360, 94, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6361, 94, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6362, 94, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6363, 94, 24, 0.360000, NULL, '2025-04-27 16:50:41', 1);
INSERT INTO `project_references`
VALUES (6364, 94, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6365, 95, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:07', 1);
INSERT INTO `project_references`
VALUES (6366, 95, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6367, 95, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6368, 95, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6369, 95, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6370, 95, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6371, 95, 24, 0.360000, NULL, '2025-04-27 16:50:41', 1);
INSERT INTO `project_references`
VALUES (6372, 95, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6373, 96, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:07', 1);
INSERT INTO `project_references`
VALUES (6374, 96, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6375, 96, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6376, 96, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6377, 96, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6378, 96, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6379, 96, 24, 0.360000, NULL, '2025-04-27 16:50:41', 1);
INSERT INTO `project_references`
VALUES (6380, 96, 26, 0.330000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6381, 97, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:07', 1);
INSERT INTO `project_references`
VALUES (6382, 97, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6383, 97, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6384, 97, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6385, 97, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6386, 97, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6387, 97, 24, 0.360000, NULL, '2025-04-27 16:50:41', 1);
INSERT INTO `project_references`
VALUES (6388, 97, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6389, 98, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:08', 1);
INSERT INTO `project_references`
VALUES (6390, 98, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6391, 98, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6392, 98, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6393, 98, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6394, 98, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6395, 98, 24, 0.360000, NULL, '2025-04-27 16:50:41', 1);
INSERT INTO `project_references`
VALUES (6396, 98, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6397, 99, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:08', 1);
INSERT INTO `project_references`
VALUES (6398, 99, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6399, 99, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6400, 99, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6401, 99, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6402, 99, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6403, 99, 24, 0.360000, NULL, '2025-04-27 16:50:41', 1);
INSERT INTO `project_references`
VALUES (6404, 99, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6405, 100, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:08', 1);
INSERT INTO `project_references`
VALUES (6406, 100, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6407, 100, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6408, 100, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6409, 100, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6410, 100, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6411, 100, 24, 0.360000, NULL, '2025-04-27 16:50:41', 1);
INSERT INTO `project_references`
VALUES (6412, 100, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6413, 101, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:08', 1);
INSERT INTO `project_references`
VALUES (6414, 101, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6415, 101, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6416, 101, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6417, 101, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6418, 101, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6419, 101, 24, 0.360000, NULL, '2025-04-27 16:50:41', 1);
INSERT INTO `project_references`
VALUES (6420, 101, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6421, 102, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:08', 1);
INSERT INTO `project_references`
VALUES (6422, 102, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6423, 102, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6424, 102, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6425, 102, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6426, 102, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6427, 102, 24, 0.360000, NULL, '2025-04-27 16:50:42', 1);
INSERT INTO `project_references`
VALUES (6428, 102, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6429, 103, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:08', 1);
INSERT INTO `project_references`
VALUES (6430, 103, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6431, 103, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6432, 103, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6433, 103, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6434, 103, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6435, 103, 24, 0.360000, NULL, '2025-04-27 16:50:42', 1);
INSERT INTO `project_references`
VALUES (6436, 103, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6437, 104, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:09', 1);
INSERT INTO `project_references`
VALUES (6438, 104, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6439, 104, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6440, 104, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6441, 104, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6442, 104, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6443, 104, 24, 0.360000, NULL, '2025-04-27 16:50:42', 1);
INSERT INTO `project_references`
VALUES (6444, 104, 26, 0.303600, NULL, '2025-04-28 13:46:27', 1);
INSERT INTO `project_references`
VALUES (6445, 105, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:09', 1);
INSERT INTO `project_references`
VALUES (6446, 105, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6447, 105, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6448, 105, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6449, 105, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6450, 105, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6451, 105, 24, 0.360000, NULL, '2025-04-27 16:50:42', 1);
INSERT INTO `project_references`
VALUES (6452, 105, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6453, 106, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:09', 1);
INSERT INTO `project_references`
VALUES (6454, 106, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6455, 106, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6456, 106, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6457, 106, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6458, 106, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6459, 106, 24, 0.360000, NULL, '2025-04-27 16:50:42', 1);
INSERT INTO `project_references`
VALUES (6460, 106, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6461, 107, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:09', 1);
INSERT INTO `project_references`
VALUES (6462, 107, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6463, 107, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6464, 107, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6465, 107, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6466, 107, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6467, 107, 24, 0.360000, NULL, '2025-04-27 16:50:42', 1);
INSERT INTO `project_references`
VALUES (6468, 107, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6469, 108, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:09', 1);
INSERT INTO `project_references`
VALUES (6470, 108, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6471, 108, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6472, 108, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6473, 108, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6474, 108, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6475, 108, 24, 0.360000, NULL, '2025-04-27 16:50:42', 1);
INSERT INTO `project_references`
VALUES (6476, 108, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6477, 109, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:10', 1);
INSERT INTO `project_references`
VALUES (6478, 109, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6479, 109, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6480, 109, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6481, 109, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6482, 109, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6483, 109, 24, 0.360000, NULL, '2025-04-27 16:50:42', 1);
INSERT INTO `project_references`
VALUES (6484, 109, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6485, 110, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:10', 1);
INSERT INTO `project_references`
VALUES (6486, 110, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6487, 110, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6488, 110, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6489, 110, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6490, 110, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6491, 110, 24, 0.360000, NULL, '2025-04-27 16:50:43', 1);
INSERT INTO `project_references`
VALUES (6492, 110, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6493, 111, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:10', 1);
INSERT INTO `project_references`
VALUES (6494, 111, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6495, 111, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6496, 111, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6497, 111, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6498, 111, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6499, 111, 24, 0.360000, NULL, '2025-04-27 16:50:43', 1);
INSERT INTO `project_references`
VALUES (6500, 111, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6501, 112, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:10', 1);
INSERT INTO `project_references`
VALUES (6502, 112, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6503, 112, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6504, 112, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6505, 112, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6506, 112, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6507, 112, 24, 0.360000, NULL, '2025-04-27 16:50:43', 1);
INSERT INTO `project_references`
VALUES (6508, 112, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6509, 113, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:10', 1);
INSERT INTO `project_references`
VALUES (6510, 113, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6511, 113, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6512, 113, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6513, 113, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6514, 113, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6515, 113, 24, 0.360000, NULL, '2025-04-27 16:50:44', 1);
INSERT INTO `project_references`
VALUES (6516, 113, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6517, 116, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:10', 1);
INSERT INTO `project_references`
VALUES (6518, 116, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6519, 116, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6520, 116, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6521, 116, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6522, 116, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6523, 116, 24, 0.360000, NULL, '2025-04-27 16:50:44', 1);
INSERT INTO `project_references`
VALUES (6524, 116, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6525, 117, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:10', 1);
INSERT INTO `project_references`
VALUES (6526, 117, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6527, 117, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6528, 117, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6529, 117, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6530, 117, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6531, 117, 24, 0.360000, NULL, '2025-04-27 16:50:44', 1);
INSERT INTO `project_references`
VALUES (6532, 117, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6533, 118, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:11', 1);
INSERT INTO `project_references`
VALUES (6534, 118, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6535, 118, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6536, 118, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6537, 118, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6538, 118, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6539, 118, 24, 0.360000, NULL, '2025-04-27 16:50:44', 1);
INSERT INTO `project_references`
VALUES (6540, 118, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6541, 119, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:11', 1);
INSERT INTO `project_references`
VALUES (6542, 119, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6543, 119, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6544, 119, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6545, 119, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6546, 119, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6547, 119, 24, 0.360000, NULL, '2025-04-27 16:50:45', 1);
INSERT INTO `project_references`
VALUES (6548, 119, 26, 0.303600, NULL, '2025-04-28 13:46:28', 1);
INSERT INTO `project_references`
VALUES (6549, 120, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:11', 1);
INSERT INTO `project_references`
VALUES (6550, 120, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6551, 120, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6552, 120, 21, 0.500000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6553, 120, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6554, 120, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6555, 120, 24, 0.360000, NULL, '2025-04-27 16:50:45', 1);
INSERT INTO `project_references`
VALUES (6556, 120, 26, 0.303600, NULL, '2025-04-28 13:46:29', 1);
INSERT INTO `project_references`
VALUES (6557, 121, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:11', 1);
INSERT INTO `project_references`
VALUES (6558, 121, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6559, 121, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6560, 121, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6561, 121, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6562, 121, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6563, 121, 24, 0.360000, NULL, '2025-04-27 16:50:45', 1);
INSERT INTO `project_references`
VALUES (6564, 121, 26, 0.303600, NULL, '2025-04-28 13:46:29', 1);
INSERT INTO `project_references`
VALUES (6565, 122, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:11', 1);
INSERT INTO `project_references`
VALUES (6566, 122, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6567, 122, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6568, 122, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6569, 122, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6570, 122, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6571, 122, 24, 0.360000, NULL, '2025-04-27 16:50:45', 1);
INSERT INTO `project_references`
VALUES (6572, 122, 26, 0.303600, NULL, '2025-04-28 13:46:29', 1);
INSERT INTO `project_references`
VALUES (6573, 123, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:12', 1);
INSERT INTO `project_references`
VALUES (6574, 123, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6575, 123, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6576, 123, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6577, 123, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6578, 123, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6579, 123, 24, 0.360000, NULL, '2025-04-27 16:50:45', 1);
INSERT INTO `project_references`
VALUES (6580, 123, 26, 0.303600, NULL, '2025-04-28 13:46:29', 1);
INSERT INTO `project_references`
VALUES (6581, 124, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:12', 1);
INSERT INTO `project_references`
VALUES (6582, 124, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6583, 124, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6584, 124, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6585, 124, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6586, 124, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6587, 124, 24, 0.360000, NULL, '2025-04-27 16:50:45', 1);
INSERT INTO `project_references`
VALUES (6588, 124, 26, 0.303600, NULL, '2025-04-28 13:46:30', 1);
INSERT INTO `project_references`
VALUES (6589, 125, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:19:00', NULL);
INSERT INTO `project_references`
VALUES (6590, 125, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6591, 125, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6592, 125, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6593, 125, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6594, 125, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6595, 125, 24, 0.360000, NULL, '2025-04-28 14:21:43', NULL);
INSERT INTO `project_references`
VALUES (6596, 125, 26, 0.303600, NULL, '2025-04-28 16:10:09', NULL);
INSERT INTO `project_references`
VALUES (6597, 126, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:12', 1);
INSERT INTO `project_references`
VALUES (6598, 126, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6599, 126, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6600, 126, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6601, 126, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6602, 126, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6603, 126, 24, 0.360000, NULL, '2025-04-27 16:50:46', 1);
INSERT INTO `project_references`
VALUES (6604, 126, 26, 0.303600, NULL, '2025-04-28 13:46:30', 1);
INSERT INTO `project_references`
VALUES (6605, 127, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:12', 1);
INSERT INTO `project_references`
VALUES (6606, 127, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6607, 127, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6608, 127, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6609, 127, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6610, 127, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6611, 127, 24, 0.360000, NULL, '2025-04-27 16:50:46', 1);
INSERT INTO `project_references`
VALUES (6612, 127, 26, 0.303600, NULL, '2025-04-28 13:46:30', 1);
INSERT INTO `project_references`
VALUES (6613, 128, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:13', 1);
INSERT INTO `project_references`
VALUES (6614, 128, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6615, 128, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6616, 128, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6617, 128, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6618, 128, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6619, 128, 24, 0.360000, NULL, '2025-04-27 16:50:46', 1);
INSERT INTO `project_references`
VALUES (6620, 128, 26, 0.303600, NULL, '2025-04-28 13:46:30', 1);
INSERT INTO `project_references`
VALUES (6621, 129, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:13', 1);
INSERT INTO `project_references`
VALUES (6622, 129, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6623, 129, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6624, 129, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6625, 129, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6626, 129, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6627, 129, 24, 0.360000, NULL, '2025-04-27 16:50:46', 1);
INSERT INTO `project_references`
VALUES (6628, 129, 26, 0.303600, NULL, '2025-04-28 13:46:30', 1);
INSERT INTO `project_references`
VALUES (6629, 130, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:13', 1);
INSERT INTO `project_references`
VALUES (6630, 130, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6631, 130, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6632, 130, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6633, 130, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6634, 130, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6635, 130, 24, 0.360000, NULL, '2025-04-27 16:50:46', 1);
INSERT INTO `project_references`
VALUES (6636, 130, 26, 0.450000, NULL, '2025-04-28 13:46:30', 1);
INSERT INTO `project_references`
VALUES (6637, 131, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:19:00', NULL);
INSERT INTO `project_references`
VALUES (6638, 131, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6639, 131, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6640, 131, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6641, 131, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6642, 131, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6643, 131, 24, 0.500000, NULL, '2025-04-28 14:21:43', NULL);
INSERT INTO `project_references`
VALUES (6644, 131, 26, NULL, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6645, 132, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:14', 1);
INSERT INTO `project_references`
VALUES (6646, 132, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6647, 132, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6648, 132, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6649, 132, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6650, 132, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6651, 132, 24, 0.360000, NULL, '2025-04-27 16:50:46', 1);
INSERT INTO `project_references`
VALUES (6652, 132, 26, 0.303600, NULL, '2025-04-28 13:46:31', 1);
INSERT INTO `project_references`
VALUES (6653, 133, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:14', 2);
INSERT INTO `project_references`
VALUES (6654, 133, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6655, 133, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6656, 133, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6657, 133, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6658, 133, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6659, 133, 24, 0.360000, NULL, '2025-04-27 16:50:46', 2);
INSERT INTO `project_references`
VALUES (6660, 133, 26, 0.350000, NULL, '2025-04-28 13:46:31', 2);
INSERT INTO `project_references`
VALUES (6661, 134, 18, 0.520000, '2024-12-27 15:34:36', '2025-04-27 16:50:14', 2);
INSERT INTO `project_references`
VALUES (6662, 134, 19, 0.550000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6663, 134, 20, 0.550000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6664, 134, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6665, 134, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6666, 134, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6667, 134, 24, 0.360000, NULL, '2025-04-27 16:50:47', 2);
INSERT INTO `project_references`
VALUES (6668, 134, 26, 0.500000, NULL, '2025-04-28 13:46:31', 2);
INSERT INTO `project_references`
VALUES (6669, 135, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:14', 1);
INSERT INTO `project_references`
VALUES (6670, 135, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6671, 135, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6672, 135, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6673, 135, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6674, 135, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6675, 135, 24, 0.360000, NULL, '2025-04-27 16:50:47', 1);
INSERT INTO `project_references`
VALUES (6676, 135, 26, 0.303600, NULL, '2025-04-28 13:46:31', 1);
INSERT INTO `project_references`
VALUES (6677, 136, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:19:01', NULL);
INSERT INTO `project_references`
VALUES (6678, 136, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6679, 136, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6680, 136, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6681, 136, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6682, 136, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6683, 136, 24, 0.500000, NULL, '2025-04-28 14:21:43', NULL);
INSERT INTO `project_references`
VALUES (6684, 136, 26, 0.450000, NULL, '2025-04-28 16:10:10', NULL);
INSERT INTO `project_references`
VALUES (6685, 137, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:19:01', NULL);
INSERT INTO `project_references`
VALUES (6686, 137, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6687, 137, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6688, 137, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6689, 137, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6690, 137, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6691, 137, 24, 0.500000, NULL, '2025-04-28 14:21:43', NULL);
INSERT INTO `project_references`
VALUES (6692, 137, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6693, 138, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:15', 1);
INSERT INTO `project_references`
VALUES (6694, 138, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6695, 138, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6696, 138, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6697, 138, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6698, 138, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6699, 138, 24, 0.360000, NULL, '2025-04-27 16:50:47', 1);
INSERT INTO `project_references`
VALUES (6700, 138, 26, 0.330000, NULL, '2025-04-28 13:46:31', 1);
INSERT INTO `project_references`
VALUES (6701, 139, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:15', 1);
INSERT INTO `project_references`
VALUES (6702, 139, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6703, 139, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6704, 139, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6705, 139, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6706, 139, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6707, 139, 24, 0.360000, NULL, '2025-04-27 16:50:47', 1);
INSERT INTO `project_references`
VALUES (6708, 139, 26, 0.303600, NULL, '2025-04-28 13:46:31', 1);
INSERT INTO `project_references`
VALUES (6709, 140, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:15', 1);
INSERT INTO `project_references`
VALUES (6710, 140, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6711, 140, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6712, 140, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6713, 140, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6714, 140, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6715, 140, 24, 0.360000, NULL, '2025-04-27 16:50:47', 1);
INSERT INTO `project_references`
VALUES (6716, 140, 26, 0.303600, NULL, '2025-04-28 13:46:32', 1);
INSERT INTO `project_references`
VALUES (6717, 141, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:15', 1);
INSERT INTO `project_references`
VALUES (6718, 141, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6719, 141, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6720, 141, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6721, 141, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6722, 141, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6723, 141, 24, 0.360000, NULL, '2025-04-27 16:50:48', 1);
INSERT INTO `project_references`
VALUES (6724, 141, 26, 0.303600, NULL, '2025-04-28 13:46:32', 1);
INSERT INTO `project_references`
VALUES (6725, 142, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:15', 1);
INSERT INTO `project_references`
VALUES (6726, 142, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6727, 142, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6728, 142, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6729, 142, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6730, 142, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6731, 142, 24, 0.360000, NULL, '2025-04-27 16:50:48', 1);
INSERT INTO `project_references`
VALUES (6732, 142, 26, 0.303600, NULL, '2025-04-28 13:46:32', 1);
INSERT INTO `project_references`
VALUES (6733, 143, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:15', 1);
INSERT INTO `project_references`
VALUES (6734, 143, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6735, 143, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6736, 143, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6737, 143, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6738, 143, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6739, 143, 24, 0.360000, NULL, '2025-04-27 16:50:48', 1);
INSERT INTO `project_references`
VALUES (6740, 143, 26, 0.303600, NULL, '2025-04-28 13:46:32', 1);
INSERT INTO `project_references`
VALUES (6741, 144, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:19:01', NULL);
INSERT INTO `project_references`
VALUES (6742, 144, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6743, 144, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6744, 144, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6745, 144, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6746, 144, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6747, 144, 24, 0.500000, NULL, '2025-04-28 14:21:43', NULL);
INSERT INTO `project_references`
VALUES (6748, 144, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6749, 145, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:16', 1);
INSERT INTO `project_references`
VALUES (6750, 145, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6751, 145, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6752, 145, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6753, 145, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6754, 145, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6755, 145, 24, 0.360000, NULL, '2025-04-27 16:50:48', 1);
INSERT INTO `project_references`
VALUES (6756, 145, 26, 0.303600, NULL, '2025-04-28 13:46:33', 1);
INSERT INTO `project_references`
VALUES (6757, 146, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:16', 1);
INSERT INTO `project_references`
VALUES (6758, 146, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6759, 146, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6760, 146, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6761, 146, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6762, 146, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6763, 146, 24, 0.360000, NULL, '2025-04-27 16:50:48', 1);
INSERT INTO `project_references`
VALUES (6764, 146, 26, 0.303600, NULL, '2025-04-28 13:46:33', 1);
INSERT INTO `project_references`
VALUES (6765, 147, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:16', 1);
INSERT INTO `project_references`
VALUES (6766, 147, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6767, 147, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6768, 147, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6769, 147, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6770, 147, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6771, 147, 24, 0.360000, NULL, '2025-04-27 16:50:48', 1);
INSERT INTO `project_references`
VALUES (6772, 147, 26, 0.303600, NULL, '2025-04-28 13:46:33', 1);
INSERT INTO `project_references`
VALUES (6773, 148, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:16', 1);
INSERT INTO `project_references`
VALUES (6774, 148, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6775, 148, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6776, 148, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6777, 148, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6778, 148, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6779, 148, 24, 0.360000, NULL, '2025-04-27 16:50:49', 1);
INSERT INTO `project_references`
VALUES (6780, 148, 26, 0.303600, NULL, '2025-04-28 13:46:33', 1);
INSERT INTO `project_references`
VALUES (6781, 149, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:16', 1);
INSERT INTO `project_references`
VALUES (6782, 149, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6783, 149, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6784, 149, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6785, 149, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6786, 149, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6787, 149, 24, 0.360000, NULL, '2025-04-27 16:50:49', 1);
INSERT INTO `project_references`
VALUES (6788, 149, 26, 0.303600, NULL, '2025-04-28 13:46:33', 1);
INSERT INTO `project_references`
VALUES (6789, 150, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:16', 1);
INSERT INTO `project_references`
VALUES (6790, 150, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6791, 150, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6792, 150, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6793, 150, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6794, 150, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6795, 150, 24, 0.360000, NULL, '2025-04-27 16:50:49', 1);
INSERT INTO `project_references`
VALUES (6796, 150, 26, 0.303600, NULL, '2025-04-28 13:46:34', 1);
INSERT INTO `project_references`
VALUES (6797, 151, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:17', 1);
INSERT INTO `project_references`
VALUES (6798, 151, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6799, 151, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6800, 151, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6801, 151, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6802, 151, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6803, 151, 24, 0.360000, NULL, '2025-04-27 16:50:49', 1);
INSERT INTO `project_references`
VALUES (6804, 151, 26, 0.303600, NULL, '2025-04-28 13:46:34', 1);
INSERT INTO `project_references`
VALUES (6805, 152, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:17', 1);
INSERT INTO `project_references`
VALUES (6806, 152, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6807, 152, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6808, 152, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6809, 152, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6810, 152, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6811, 152, 24, 0.360000, NULL, '2025-04-27 16:50:49', 1);
INSERT INTO `project_references`
VALUES (6812, 152, 26, 0.303600, NULL, '2025-04-28 13:46:34', 1);
INSERT INTO `project_references`
VALUES (6813, 153, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:17', 1);
INSERT INTO `project_references`
VALUES (6814, 153, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6815, 153, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6816, 153, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6817, 153, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6818, 153, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6819, 153, 24, 0.360000, NULL, '2025-04-27 16:50:49', 1);
INSERT INTO `project_references`
VALUES (6820, 153, 26, 0.303600, NULL, '2025-04-28 13:46:35', 1);
INSERT INTO `project_references`
VALUES (6821, 154, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:17', 1);
INSERT INTO `project_references`
VALUES (6822, 154, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6823, 154, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6824, 154, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6825, 154, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6826, 154, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6827, 154, 24, 0.360000, NULL, '2025-04-27 16:50:49', 1);
INSERT INTO `project_references`
VALUES (6828, 154, 26, 0.303600, NULL, '2025-04-28 13:46:35', 1);
INSERT INTO `project_references`
VALUES (6829, 155, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:18', 1);
INSERT INTO `project_references`
VALUES (6830, 155, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6831, 155, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6832, 155, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6833, 155, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6834, 155, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6835, 155, 24, 0.360000, NULL, '2025-04-27 16:50:49', 1);
INSERT INTO `project_references`
VALUES (6836, 155, 26, 0.303600, NULL, '2025-04-28 13:46:35', 1);
INSERT INTO `project_references`
VALUES (6837, 156, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:18', 1);
INSERT INTO `project_references`
VALUES (6838, 156, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6839, 156, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6840, 156, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6841, 156, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6842, 156, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6843, 156, 24, 0.360000, NULL, '2025-04-27 16:50:50', 1);
INSERT INTO `project_references`
VALUES (6844, 156, 26, 0.303600, NULL, '2025-04-28 13:46:35', 1);
INSERT INTO `project_references`
VALUES (6845, 157, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:18', 1);
INSERT INTO `project_references`
VALUES (6846, 157, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6847, 157, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6848, 157, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6849, 157, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6850, 157, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6851, 157, 24, 0.360000, NULL, '2025-04-27 16:50:50', 1);
INSERT INTO `project_references`
VALUES (6852, 157, 26, 0.303600, NULL, '2025-04-28 13:46:36', 1);
INSERT INTO `project_references`
VALUES (6853, 158, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:18', 1);
INSERT INTO `project_references`
VALUES (6854, 158, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6855, 158, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6856, 158, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6857, 158, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6858, 158, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6859, 158, 24, 0.360000, NULL, '2025-04-27 16:50:50', 1);
INSERT INTO `project_references`
VALUES (6860, 158, 26, 0.303600, NULL, '2025-04-28 13:46:37', 1);
INSERT INTO `project_references`
VALUES (6861, 159, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:18', 1);
INSERT INTO `project_references`
VALUES (6862, 159, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6863, 159, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6864, 159, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6865, 159, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6866, 159, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6867, 159, 24, 0.360000, NULL, '2025-04-27 16:50:50', 1);
INSERT INTO `project_references`
VALUES (6868, 159, 26, 0.303600, NULL, '2025-04-28 13:46:37', 1);
INSERT INTO `project_references`
VALUES (6869, 160, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:19', 1);
INSERT INTO `project_references`
VALUES (6870, 160, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6871, 160, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6872, 160, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6873, 160, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6874, 160, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6875, 160, 24, 0.360000, NULL, '2025-04-27 16:50:50', 1);
INSERT INTO `project_references`
VALUES (6876, 160, 26, 0.303600, NULL, '2025-04-28 13:46:37', 1);
INSERT INTO `project_references`
VALUES (6877, 161, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:19', 1);
INSERT INTO `project_references`
VALUES (6878, 161, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6879, 161, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6880, 161, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6881, 161, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6882, 161, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6883, 161, 24, 0.360000, NULL, '2025-04-27 16:50:50', 1);
INSERT INTO `project_references`
VALUES (6884, 161, 26, 0.303600, NULL, '2025-04-28 13:46:38', 1);
INSERT INTO `project_references`
VALUES (6885, 162, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:19', 1);
INSERT INTO `project_references`
VALUES (6886, 162, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6887, 162, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6888, 162, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6889, 162, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6890, 162, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6891, 162, 24, 0.360000, NULL, '2025-04-27 16:50:50', 1);
INSERT INTO `project_references`
VALUES (6892, 162, 26, 0.303600, NULL, '2025-04-28 13:46:38', 1);
INSERT INTO `project_references`
VALUES (6893, 163, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:19', 1);
INSERT INTO `project_references`
VALUES (6894, 163, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6895, 163, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6896, 163, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6897, 163, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6898, 163, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6899, 163, 24, 0.360000, NULL, '2025-04-27 16:50:50', 1);
INSERT INTO `project_references`
VALUES (6900, 163, 26, 0.330000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6901, 164, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:19', 1);
INSERT INTO `project_references`
VALUES (6902, 164, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6903, 164, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6904, 164, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6905, 164, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6906, 164, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6907, 164, 24, 0.360000, NULL, '2025-04-27 16:50:50', 1);
INSERT INTO `project_references`
VALUES (6908, 164, 26, 0.303600, NULL, '2025-04-28 13:46:38', 1);
INSERT INTO `project_references`
VALUES (6909, 165, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:20', 1);
INSERT INTO `project_references`
VALUES (6910, 165, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6911, 165, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6912, 165, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6913, 165, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6914, 165, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6915, 165, 24, 0.360000, NULL, '2025-04-27 16:50:50', 1);
INSERT INTO `project_references`
VALUES (6916, 165, 26, 0.303600, NULL, '2025-04-28 13:46:38', 1);
INSERT INTO `project_references`
VALUES (6917, 166, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:20', 2);
INSERT INTO `project_references`
VALUES (6918, 166, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6919, 166, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6920, 166, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6921, 166, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6922, 166, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6923, 166, 24, 0.360000, NULL, '2025-04-27 16:50:50', 2);
INSERT INTO `project_references`
VALUES (6924, 166, 26, 0.303600, NULL, '2025-04-28 13:46:38', 2);
INSERT INTO `project_references`
VALUES (6925, 167, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:20', 2);
INSERT INTO `project_references`
VALUES (6926, 167, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6927, 167, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6928, 167, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6929, 167, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6930, 167, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6931, 167, 24, 0.360000, NULL, '2025-04-27 16:50:51', 2);
INSERT INTO `project_references`
VALUES (6932, 167, 26, 0.350000, NULL, '2025-04-28 13:46:38', 2);
INSERT INTO `project_references`
VALUES (6933, 169, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:20', 1);
INSERT INTO `project_references`
VALUES (6934, 169, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6935, 169, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6936, 169, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6937, 169, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6938, 169, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6939, 169, 24, 0.360000, NULL, '2025-04-27 16:50:51', 1);
INSERT INTO `project_references`
VALUES (6940, 169, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (6941, 170, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:20', 1);
INSERT INTO `project_references`
VALUES (6942, 170, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6943, 170, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6944, 170, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6945, 170, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6946, 170, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6947, 170, 24, 0.360000, NULL, '2025-04-27 16:50:51', 1);
INSERT INTO `project_references`
VALUES (6948, 170, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (6949, 171, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:20', 1);
INSERT INTO `project_references`
VALUES (6950, 171, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6951, 171, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6952, 171, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6953, 171, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6954, 171, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6955, 171, 24, 0.360000, NULL, '2025-04-27 16:50:51', 1);
INSERT INTO `project_references`
VALUES (6956, 171, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (6957, 172, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:21', 1);
INSERT INTO `project_references`
VALUES (6958, 172, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6959, 172, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6960, 172, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6961, 172, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6962, 172, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6963, 172, 24, 0.360000, NULL, '2025-04-27 16:50:51', 1);
INSERT INTO `project_references`
VALUES (6964, 172, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (6965, 173, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:21', 1);
INSERT INTO `project_references`
VALUES (6966, 173, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6967, 173, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6968, 173, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6969, 173, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6970, 173, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6971, 173, 24, 0.360000, NULL, '2025-04-27 16:50:51', 1);
INSERT INTO `project_references`
VALUES (6972, 173, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (6973, 174, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:21', 1);
INSERT INTO `project_references`
VALUES (6974, 174, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6975, 174, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6976, 174, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6977, 174, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6978, 174, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6979, 174, 24, 0.360000, NULL, '2025-04-27 16:50:51', 1);
INSERT INTO `project_references`
VALUES (6980, 174, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (6981, 175, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:21', 1);
INSERT INTO `project_references`
VALUES (6982, 175, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6983, 175, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6984, 175, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6985, 175, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6986, 175, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6987, 175, 24, 0.360000, NULL, '2025-04-27 16:50:52', 1);
INSERT INTO `project_references`
VALUES (6988, 175, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (6989, 176, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:22', 1);
INSERT INTO `project_references`
VALUES (6990, 176, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6991, 176, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6992, 176, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6993, 176, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6994, 176, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (6995, 176, 24, 0.360000, NULL, '2025-04-27 16:50:52', 1);
INSERT INTO `project_references`
VALUES (6996, 176, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (6997, 177, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:22', 1);
INSERT INTO `project_references`
VALUES (6998, 177, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (6999, 177, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7000, 177, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7001, 177, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7002, 177, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7003, 177, 24, 0.360000, NULL, '2025-04-27 16:50:52', 1);
INSERT INTO `project_references`
VALUES (7004, 177, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (7005, 178, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:22', 1);
INSERT INTO `project_references`
VALUES (7006, 178, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7007, 178, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7008, 178, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7009, 178, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7010, 178, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7011, 178, 24, 0.360000, NULL, '2025-04-27 16:50:52', 1);
INSERT INTO `project_references`
VALUES (7012, 178, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (7013, 179, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:22', 1);
INSERT INTO `project_references`
VALUES (7014, 179, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7015, 179, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7016, 179, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7017, 179, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7018, 179, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7019, 179, 24, 0.360000, NULL, '2025-04-27 16:50:52', 1);
INSERT INTO `project_references`
VALUES (7020, 179, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (7021, 180, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:22', 1);
INSERT INTO `project_references`
VALUES (7022, 180, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7023, 180, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7024, 180, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7025, 180, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7026, 180, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7027, 180, 24, 0.360000, NULL, '2025-04-27 16:50:52', 1);
INSERT INTO `project_references`
VALUES (7028, 180, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (7029, 181, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:23', 1);
INSERT INTO `project_references`
VALUES (7030, 181, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7031, 181, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7032, 181, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7033, 181, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7034, 181, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7035, 181, 24, 0.360000, NULL, '2025-04-27 16:50:53', 1);
INSERT INTO `project_references`
VALUES (7036, 181, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (7037, 182, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:23', 1);
INSERT INTO `project_references`
VALUES (7038, 182, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7039, 182, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7040, 182, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7041, 182, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7042, 182, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7043, 182, 24, 0.360000, NULL, '2025-04-27 16:50:53', 1);
INSERT INTO `project_references`
VALUES (7044, 182, 26, 0.330000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7045, 183, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:23', 1);
INSERT INTO `project_references`
VALUES (7046, 183, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7047, 183, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7048, 183, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7049, 183, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7050, 183, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7051, 183, 24, 0.360000, NULL, '2025-04-27 16:50:53', 1);
INSERT INTO `project_references`
VALUES (7052, 183, 26, 0.303600, NULL, '2025-04-28 13:46:39', 1);
INSERT INTO `project_references`
VALUES (7053, 184, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:23', 1);
INSERT INTO `project_references`
VALUES (7054, 184, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7055, 184, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7056, 184, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7057, 184, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7058, 184, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7059, 184, 24, 0.360000, NULL, '2025-04-27 16:50:53', 1);
INSERT INTO `project_references`
VALUES (7060, 184, 26, 0.303600, NULL, '2025-04-28 13:46:40', 1);
INSERT INTO `project_references`
VALUES (7061, 185, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:23', 1);
INSERT INTO `project_references`
VALUES (7062, 185, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7063, 185, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7064, 185, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7065, 185, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7066, 185, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7067, 185, 24, 0.360000, NULL, '2025-04-27 16:50:53', 1);
INSERT INTO `project_references`
VALUES (7068, 185, 26, 0.303600, NULL, '2025-04-28 13:46:40', 1);
INSERT INTO `project_references`
VALUES (7069, 186, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:24', 1);
INSERT INTO `project_references`
VALUES (7070, 186, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7071, 186, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7072, 186, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7073, 186, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7074, 186, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7075, 186, 24, 0.360000, NULL, '2025-04-27 16:50:53', 1);
INSERT INTO `project_references`
VALUES (7076, 186, 26, 0.303600, NULL, '2025-04-28 13:46:40', 1);
INSERT INTO `project_references`
VALUES (7077, 187, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:24', 1);
INSERT INTO `project_references`
VALUES (7078, 187, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7079, 187, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7080, 187, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7081, 187, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7082, 187, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7083, 187, 24, 0.360000, NULL, '2025-04-27 16:50:53', 1);
INSERT INTO `project_references`
VALUES (7084, 187, 26, 0.303600, NULL, '2025-04-28 13:46:40', 1);
INSERT INTO `project_references`
VALUES (7085, 188, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:24', 1);
INSERT INTO `project_references`
VALUES (7086, 188, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7087, 188, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7088, 188, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7089, 188, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7090, 188, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7091, 188, 24, 0.360000, NULL, '2025-04-27 16:50:54', 1);
INSERT INTO `project_references`
VALUES (7092, 188, 26, 0.303600, NULL, '2025-04-28 13:46:40', 1);
INSERT INTO `project_references`
VALUES (7093, 189, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:24', 1);
INSERT INTO `project_references`
VALUES (7094, 189, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7095, 189, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7096, 189, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7097, 189, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7098, 189, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7099, 189, 24, 0.360000, NULL, '2025-04-27 16:50:54', 1);
INSERT INTO `project_references`
VALUES (7100, 189, 26, 0.303600, NULL, '2025-04-28 13:46:40', 1);
INSERT INTO `project_references`
VALUES (7101, 190, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:24', 1);
INSERT INTO `project_references`
VALUES (7102, 190, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7103, 190, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7104, 190, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7105, 190, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7106, 190, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7107, 190, 24, 0.360000, NULL, '2025-04-27 16:50:54', 1);
INSERT INTO `project_references`
VALUES (7108, 190, 26, 0.303600, NULL, '2025-04-28 13:46:40', 1);
INSERT INTO `project_references`
VALUES (7109, 191, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:24', 1);
INSERT INTO `project_references`
VALUES (7110, 191, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7111, 191, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7112, 191, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7113, 191, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7114, 191, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7115, 191, 24, 0.360000, NULL, '2025-04-27 16:50:54', 1);
INSERT INTO `project_references`
VALUES (7116, 191, 26, 0.303600, NULL, '2025-04-28 13:46:40', 1);
INSERT INTO `project_references`
VALUES (7117, 192, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:24', 1);
INSERT INTO `project_references`
VALUES (7118, 192, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7119, 192, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7120, 192, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7121, 192, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7122, 192, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7123, 192, 24, 0.360000, NULL, '2025-04-27 16:50:54', 1);
INSERT INTO `project_references`
VALUES (7124, 192, 26, 0.303600, NULL, '2025-04-28 13:46:40', 1);
INSERT INTO `project_references`
VALUES (7125, 193, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:25', 1);
INSERT INTO `project_references`
VALUES (7126, 193, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7127, 193, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7128, 193, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7129, 193, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7130, 193, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7131, 193, 24, 0.360000, NULL, '2025-04-27 16:50:54', 1);
INSERT INTO `project_references`
VALUES (7132, 193, 26, 0.303600, NULL, '2025-04-28 13:46:40', 1);
INSERT INTO `project_references`
VALUES (7133, 194, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:25', 1);
INSERT INTO `project_references`
VALUES (7134, 194, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7135, 194, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7136, 194, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7137, 194, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7138, 194, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7139, 194, 24, 0.360000, NULL, '2025-04-27 16:50:54', 1);
INSERT INTO `project_references`
VALUES (7140, 194, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7141, 195, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:25', 1);
INSERT INTO `project_references`
VALUES (7142, 195, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7143, 195, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7144, 195, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7145, 195, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7146, 195, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7147, 195, 24, 0.360000, NULL, '2025-04-27 16:50:55', 1);
INSERT INTO `project_references`
VALUES (7148, 195, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7149, 196, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:25', 1);
INSERT INTO `project_references`
VALUES (7150, 196, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7151, 196, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7152, 196, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7153, 196, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7154, 196, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7155, 196, 24, 0.360000, NULL, '2025-04-27 16:50:55', 1);
INSERT INTO `project_references`
VALUES (7156, 196, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7157, 197, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:25', 1);
INSERT INTO `project_references`
VALUES (7158, 197, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7159, 197, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7160, 197, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7161, 197, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7162, 197, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7163, 197, 24, 0.360000, NULL, '2025-04-27 16:50:55', 1);
INSERT INTO `project_references`
VALUES (7164, 197, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7165, 198, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:25', 1);
INSERT INTO `project_references`
VALUES (7166, 198, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7167, 198, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7168, 198, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7169, 198, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7170, 198, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7171, 198, 24, 0.360000, NULL, '2025-04-27 16:50:55', 1);
INSERT INTO `project_references`
VALUES (7172, 198, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7173, 199, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:26', 1);
INSERT INTO `project_references`
VALUES (7174, 199, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7175, 199, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7176, 199, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7177, 199, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7178, 199, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7179, 199, 24, 0.360000, NULL, '2025-04-27 16:50:55', 1);
INSERT INTO `project_references`
VALUES (7180, 199, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7181, 200, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:26', 1);
INSERT INTO `project_references`
VALUES (7182, 200, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7183, 200, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7184, 200, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7185, 200, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7186, 200, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7187, 200, 24, 0.360000, NULL, '2025-04-27 16:50:56', 1);
INSERT INTO `project_references`
VALUES (7188, 200, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7189, 201, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:26', 1);
INSERT INTO `project_references`
VALUES (7190, 201, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7191, 201, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7192, 201, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7193, 201, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7194, 201, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7195, 201, 24, 0.360000, NULL, '2025-04-27 16:50:56', 1);
INSERT INTO `project_references`
VALUES (7196, 201, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7197, 202, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:26', 1);
INSERT INTO `project_references`
VALUES (7198, 202, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7199, 202, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7200, 202, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7201, 202, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7202, 202, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7203, 202, 24, 0.360000, NULL, '2025-04-27 16:50:56', 1);
INSERT INTO `project_references`
VALUES (7204, 202, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7205, 203, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:26', 1);
INSERT INTO `project_references`
VALUES (7206, 203, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7207, 203, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7208, 203, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7209, 203, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7210, 203, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7211, 203, 24, 0.360000, NULL, '2025-04-27 16:50:56', 1);
INSERT INTO `project_references`
VALUES (7212, 203, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7213, 204, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:26', 1);
INSERT INTO `project_references`
VALUES (7214, 204, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7215, 204, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7216, 204, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7217, 204, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7218, 204, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7219, 204, 24, 0.360000, NULL, '2025-04-27 16:50:56', 1);
INSERT INTO `project_references`
VALUES (7220, 204, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7221, 205, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:26', 1);
INSERT INTO `project_references`
VALUES (7222, 205, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7223, 205, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7224, 205, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7225, 205, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7226, 205, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7227, 205, 24, 0.360000, NULL, '2025-04-27 16:50:56', 1);
INSERT INTO `project_references`
VALUES (7228, 205, 26, 0.303600, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7229, 207, 18, 0.850000, '2024-12-27 15:34:36', '2025-04-27 16:50:26', 1);
INSERT INTO `project_references`
VALUES (7230, 207, 19, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7231, 207, 20, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7232, 207, 21, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7233, 207, 22, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7234, 207, 23, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7235, 207, 24, 0.850000, NULL, '2025-04-27 16:50:56', 1);
INSERT INTO `project_references`
VALUES (7236, 207, 26, 0.850000, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7237, 208, 18, 0.850000, '2024-12-27 15:34:36', '2025-04-27 16:50:26', 1);
INSERT INTO `project_references`
VALUES (7238, 208, 19, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7239, 208, 20, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7240, 208, 21, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7241, 208, 22, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7242, 208, 23, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7243, 208, 24, 0.850000, NULL, '2025-04-27 16:50:56', 1);
INSERT INTO `project_references`
VALUES (7244, 208, 26, 0.850000, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7245, 209, 18, 0.850000, '2024-12-27 15:34:36', '2025-04-27 16:50:27', 1);
INSERT INTO `project_references`
VALUES (7246, 209, 19, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7247, 209, 20, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7248, 209, 21, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7249, 209, 22, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7250, 209, 23, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7251, 209, 24, 0.850000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7252, 209, 26, 0.850000, NULL, '2025-04-28 13:46:41', 1);
INSERT INTO `project_references`
VALUES (7253, 210, 18, 0.850000, '2024-12-27 15:34:36', '2025-04-27 16:50:27', 1);
INSERT INTO `project_references`
VALUES (7254, 210, 19, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7255, 210, 20, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7256, 210, 21, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7257, 210, 22, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7258, 210, 23, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7259, 210, 24, 0.850000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7260, 210, 26, 0.850000, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7261, 211, 18, 0.850000, '2024-12-27 15:34:36', '2025-04-27 16:50:27', 1);
INSERT INTO `project_references`
VALUES (7262, 211, 19, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7263, 211, 20, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7264, 211, 21, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7265, 211, 22, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7266, 211, 23, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7267, 211, 24, 0.850000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7268, 211, 26, 0.850000, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7269, 212, 18, 0.850000, '2024-12-27 15:34:36', '2025-04-27 16:50:27', 1);
INSERT INTO `project_references`
VALUES (7270, 212, 19, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7271, 212, 20, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7272, 212, 21, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7273, 212, 22, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7274, 212, 23, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7275, 212, 24, 0.850000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7276, 212, 26, 0.850000, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7277, 213, 18, 0.850000, '2024-12-27 15:34:36', '2025-04-27 16:50:27', 1);
INSERT INTO `project_references`
VALUES (7278, 213, 19, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7279, 213, 20, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7280, 213, 21, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7281, 213, 22, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7282, 213, 23, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7283, 213, 24, 0.850000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7284, 213, 26, 0.850000, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7285, 214, 18, 0.850000, '2024-12-27 15:34:36', '2025-04-27 16:50:27', 1);
INSERT INTO `project_references`
VALUES (7286, 214, 19, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7287, 214, 20, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7288, 214, 21, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7289, 214, 22, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7290, 214, 23, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7291, 214, 24, 0.850000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7292, 214, 26, 0.850000, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7293, 215, 18, 0.850000, '2024-12-27 15:34:36', '2025-04-27 16:50:27', 1);
INSERT INTO `project_references`
VALUES (7294, 215, 19, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7295, 215, 20, 0.850000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7296, 215, 21, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7297, 215, 22, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7298, 215, 23, 0.850000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7299, 215, 24, 0.850000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7300, 215, 26, 0.850000, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7301, 216, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:28', 1);
INSERT INTO `project_references`
VALUES (7302, 216, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7303, 216, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7304, 216, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7305, 216, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7306, 216, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7307, 216, 24, 0.360000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7308, 216, 26, 0.330000, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7309, 217, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:28', 1);
INSERT INTO `project_references`
VALUES (7310, 217, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7311, 217, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7312, 217, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7313, 217, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7314, 217, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7315, 217, 24, 0.360000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7316, 217, 26, 0.303600, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7317, 218, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:28', 1);
INSERT INTO `project_references`
VALUES (7318, 218, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7319, 218, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7320, 218, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7321, 218, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7322, 218, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7323, 218, 24, 0.360000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7324, 218, 26, 0.303600, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7325, 219, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:28', 1);
INSERT INTO `project_references`
VALUES (7326, 219, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7327, 219, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7328, 219, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7329, 219, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7330, 219, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7331, 219, 24, 0.360000, NULL, '2025-04-27 16:50:57', 1);
INSERT INTO `project_references`
VALUES (7332, 219, 26, 0.303600, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7333, 220, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:28', 1);
INSERT INTO `project_references`
VALUES (7334, 220, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7335, 220, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7336, 220, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7337, 220, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7338, 220, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7339, 220, 24, 0.360000, NULL, '2025-04-27 16:50:58', 1);
INSERT INTO `project_references`
VALUES (7340, 220, 26, 0.303600, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7341, 221, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:28', 1);
INSERT INTO `project_references`
VALUES (7342, 221, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7343, 221, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7344, 221, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7345, 221, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7346, 221, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7347, 221, 24, 0.360000, NULL, '2025-04-27 16:50:58', 1);
INSERT INTO `project_references`
VALUES (7348, 221, 26, 0.303600, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7349, 227, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:19:03', NULL);
INSERT INTO `project_references`
VALUES (7350, 227, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7351, 227, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7352, 227, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7353, 227, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7354, 227, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7355, 227, 24, 0.500000, NULL, '2025-04-28 14:21:45', NULL);
INSERT INTO `project_references`
VALUES (7356, 227, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7357, 228, 18, 0.600000, '2024-12-27 15:34:36', '2025-04-28 14:19:03', NULL);
INSERT INTO `project_references`
VALUES (7358, 228, 19, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7359, 228, 20, 0.600000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7360, 228, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7361, 228, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7362, 228, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7363, 228, 24, 0.550000, NULL, '2025-04-28 14:21:45', NULL);
INSERT INTO `project_references`
VALUES (7364, 228, 26, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7365, 229, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-28 14:19:03', NULL);
INSERT INTO `project_references`
VALUES (7366, 229, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7367, 229, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7368, 229, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7369, 229, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7370, 229, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7371, 229, 24, 0.500000, NULL, '2025-04-28 14:21:45', NULL);
INSERT INTO `project_references`
VALUES (7372, 229, 26, 0.450000, NULL, '2025-04-28 16:10:11', NULL);
INSERT INTO `project_references`
VALUES (7373, 230, 18, 1.000000, '2024-12-27 15:34:36', '2025-04-28 14:21:40', NULL);
INSERT INTO `project_references`
VALUES (7374, 230, 19, NULL, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7375, 230, 20, NULL, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7376, 230, 21, NULL, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7377, 230, 22, NULL, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7378, 230, 23, NULL, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7379, 230, 24, 0.500000, NULL, '2025-04-28 14:21:45', NULL);
INSERT INTO `project_references`
VALUES (7380, 230, 26, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7381, 231, 18, 1.000000, '2024-12-27 15:34:36', '2025-04-27 16:50:29', 3);
INSERT INTO `project_references`
VALUES (7382, 231, 19, NULL, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7383, 231, 20, NULL, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7384, 231, 21, NULL, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7385, 231, 22, NULL, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7386, 231, 23, NULL, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7387, 231, 24, 1.000000, NULL, '2025-04-27 16:50:58', 3);
INSERT INTO `project_references`
VALUES (7388, 231, 26, 0.944400, NULL, '2025-04-28 13:46:42', 3);
INSERT INTO `project_references`
VALUES (7389, 232, 18, 1.000000, '2024-12-27 15:34:36', '2025-04-27 16:50:29', 3);
INSERT INTO `project_references`
VALUES (7390, 232, 19, NULL, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7391, 232, 20, NULL, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7392, 232, 21, NULL, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7393, 232, 22, NULL, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7394, 232, 23, NULL, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7395, 232, 24, 1.000000, NULL, '2025-04-27 16:50:58', 3);
INSERT INTO `project_references`
VALUES (7396, 232, 26, 0.909100, NULL, '2025-04-28 13:46:42', 3);
INSERT INTO `project_references`
VALUES (7397, 233, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:29', 1);
INSERT INTO `project_references`
VALUES (7398, 233, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7399, 233, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7400, 233, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7401, 233, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7402, 233, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7403, 233, 24, 0.360000, NULL, '2025-04-27 16:50:58', 1);
INSERT INTO `project_references`
VALUES (7404, 233, 26, 0.303600, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7405, 234, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:29', 1);
INSERT INTO `project_references`
VALUES (7406, 234, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7407, 234, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7408, 234, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7409, 234, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7410, 234, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7411, 234, 24, 0.360000, NULL, '2025-04-27 16:50:58', 1);
INSERT INTO `project_references`
VALUES (7412, 234, 26, 0.303600, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7413, 235, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:29', 1);
INSERT INTO `project_references`
VALUES (7414, 235, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7415, 235, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7416, 235, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7417, 235, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7418, 235, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7419, 235, 24, 0.360000, NULL, '2025-04-27 16:50:58', 1);
INSERT INTO `project_references`
VALUES (7420, 235, 26, 0.303600, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (7421, 236, 18, 0.450000, '2024-12-27 15:34:36', '2025-04-27 16:50:29', 1);
INSERT INTO `project_references`
VALUES (7422, 236, 19, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7423, 236, 20, 0.450000, '2024-12-27 15:34:36', NULL, NULL);
INSERT INTO `project_references`
VALUES (7424, 236, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7425, 236, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7426, 236, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (7427, 236, 24, 0.360000, NULL, '2025-04-27 16:50:58', 1);
INSERT INTO `project_references`
VALUES (7428, 236, 26, 0.303600, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (9197, 237, 18, 0.450000, NULL, '2025-04-27 16:50:29', 1);
INSERT INTO `project_references`
VALUES (9198, 237, 19, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (9199, 237, 20, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (9200, 237, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (9201, 237, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (9202, 237, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (9203, 237, 24, 0.360000, NULL, '2025-04-27 16:50:59', 1);
INSERT INTO `project_references`
VALUES (9204, 237, 26, 0.303600, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (9207, 238, 18, 0.450000, NULL, '2025-04-27 16:50:30', 1);
INSERT INTO `project_references`
VALUES (9208, 238, 19, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (9209, 238, 20, 0.450000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (9210, 238, 21, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (9211, 238, 22, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (9212, 238, 23, 0.500000, NULL, NULL, NULL);
INSERT INTO `project_references`
VALUES (9213, 238, 24, 0.500000, NULL, '2025-04-27 16:50:59', 1);
INSERT INTO `project_references`
VALUES (9214, 238, 26, 0.450000, NULL, '2025-04-28 13:46:42', 1);
INSERT INTO `project_references`
VALUES (9215, 11, 16, 2.000000, '2024-12-31 10:47:02', NULL, NULL);
INSERT INTO `project_references`
VALUES (9216, 12, 16, 2.000000, '2024-12-31 10:47:02', NULL, NULL);
INSERT INTO `project_references`
VALUES (9217, 14, 16, 2.000000, '2024-12-31 10:47:02', NULL, NULL);
INSERT INTO `project_references`
VALUES (9231, 2, 2, 1.000000, '2025-02-14 10:29:19', NULL, NULL);
INSERT INTO `project_references`
VALUES (9232, 12, 2, 1.000000, '2025-02-14 10:29:19', NULL, NULL);
INSERT INTO `project_references`
VALUES (9233, 234, 1, 1.000000, '2025-02-20 09:21:12', NULL, NULL);
INSERT INTO `project_references`
VALUES (9234, 16, 16, 1.000000, '2025-03-25 15:40:52', NULL, NULL);
INSERT INTO `project_references`
VALUES (9235, 246, 1, 1.000000, '2025-04-03 10:22:26', '2025-04-03 11:22:15', 35);
INSERT INTO `project_references`
VALUES (9236, 245, 1, 1.000000, '2025-04-03 11:23:28', '2025-04-03 11:25:54', 32);
INSERT INTO `project_references`
VALUES (9237, 244, 1, 1.000000, '2025-04-03 11:31:40', NULL, 4);
INSERT INTO `project_references`
VALUES (9241, 193, 1, 1.000000, '2025-04-03 15:06:41', NULL, 2);
INSERT INTO `project_references`
VALUES (9242, 247, 1, 1.000000, '2025-04-03 15:16:53', '2025-04-03 17:08:04', 5);
INSERT INTO `project_references`
VALUES (9243, 248, 23, 1.000000, '2025-04-03 17:20:14', NULL, 3);
INSERT INTO `project_references`
VALUES (9244, 241, 1, 0.660000, '2025-04-10 16:17:00', '2025-04-11 10:32:35', NULL);
INSERT INTO `project_references`
VALUES (9245, 248, 1, 1.000000, '2025-04-11 10:33:52', '2025-04-11 10:34:02', NULL);
INSERT INTO `project_references`
VALUES (9246, 1, 2, 1.000000, '2025-04-17 09:48:35', NULL, NULL);
INSERT INTO `project_references`
VALUES (9247, 3, 2, 1.000000, '2025-04-17 09:48:59', NULL, NULL);
INSERT INTO `project_references`
VALUES (9248, 6, 2, 1.000000, '2025-04-17 09:49:21', NULL, NULL);
INSERT INTO `project_references`
VALUES (9249, 4, 2, 1.000000, '2025-04-17 09:50:54', NULL, NULL);
INSERT INTO `project_references`
VALUES (9250, 5, 2, 1.000000, '2025-04-17 09:51:34', NULL, NULL);
INSERT INTO `project_references`
VALUES (9251, 15, 16, 1.000000, '2025-04-17 09:52:41', NULL, NULL);
INSERT INTO `project_references`
VALUES (9252, 9, 16, 1.000000, '2025-04-17 09:52:41', NULL, NULL);
INSERT INTO `project_references`
VALUES (9255, 250, 1, 1.000000, '2025-04-18 13:25:35', NULL, 2);
INSERT INTO `project_references`
VALUES (9256, 181, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9257, 13, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9258, 10, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9259, 228, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9260, 246, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9261, 245, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9262, 244, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9263, 19, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9264, 26, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9265, 24, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9266, 23, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9267, 234, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9268, 227, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9269, 194, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9270, 25, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9271, 224, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9272, 21, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9273, 20, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9274, 18, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9275, 30, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9276, 180, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9277, 29, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9278, 28, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9279, 27, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9280, 120, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9281, 119, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9282, 233, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9283, 122, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9284, 121, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9285, 118, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9286, 200, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9287, 124, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9288, 123, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9289, 117, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9290, 184, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9291, 241, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9292, 79, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9293, 70, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9294, 69, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9295, 71, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9296, 64, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9297, 238, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9298, 81, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9299, 80, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9300, 78, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9301, 77, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9302, 76, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9303, 75, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9304, 74, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9305, 73, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9306, 72, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9307, 68, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9308, 67, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9309, 66, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9310, 65, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9311, 63, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9312, 62, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9313, 219, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9314, 216, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9315, 205, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9316, 203, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9317, 99, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9318, 87, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9319, 86, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9320, 220, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9321, 206, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9322, 204, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9323, 202, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9324, 201, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9325, 199, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9326, 198, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9327, 196, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9328, 195, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9329, 173, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9330, 172, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9331, 170, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9332, 169, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9333, 107, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9334, 106, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9335, 105, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9336, 104, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9337, 97, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9338, 96, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9339, 85, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9340, 242, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9341, 150, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9342, 248, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9343, 48, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9344, 171, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9345, 59, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9346, 53, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9347, 237, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9348, 226, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9349, 197, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9350, 191, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9351, 190, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9352, 174, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9353, 140, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9354, 61, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9355, 60, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9356, 58, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9357, 57, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9358, 56, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9359, 55, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9360, 54, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9361, 52, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9362, 51, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9363, 50, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9364, 49, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9365, 47, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9366, 46, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9367, 45, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9368, 44, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9369, 43, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9370, 42, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9371, 41, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9372, 40, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9373, 39, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9374, 38, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9375, 37, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9376, 36, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9377, 35, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9378, 34, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9379, 33, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9380, 32, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9381, 31, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9382, 250, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9383, 249, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9384, 177, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9385, 116, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9386, 94, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9387, 93, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9388, 236, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9389, 235, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9390, 223, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9391, 222, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9392, 215, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9393, 214, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9394, 213, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9395, 212, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9396, 211, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9397, 210, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9398, 209, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9399, 208, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9400, 207, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9401, 193, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9402, 192, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9403, 189, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9404, 188, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9405, 187, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9406, 179, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9407, 178, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9408, 176, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9409, 115, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9410, 114, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9411, 113, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9412, 112, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9413, 111, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9414, 110, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9415, 109, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9416, 108, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9417, 103, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9418, 102, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9419, 101, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9420, 100, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9421, 98, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9422, 95, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9423, 92, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9424, 91, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9425, 90, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9426, 89, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9427, 88, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9428, 84, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9429, 83, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9430, 82, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9431, 130, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9432, 129, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9433, 230, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9434, 131, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9435, 134, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9436, 133, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9437, 168, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9438, 127, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9439, 126, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9440, 125, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9441, 243, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9442, 221, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9443, 143, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9444, 142, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9445, 141, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9446, 139, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9447, 138, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9448, 137, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9449, 136, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9450, 247, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9451, 229, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9452, 218, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9453, 217, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9454, 149, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9455, 148, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9456, 147, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9457, 146, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9458, 145, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9459, 144, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9460, 232, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9461, 231, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9462, 128, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9463, 175, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9464, 165, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9465, 164, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9466, 163, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9467, 162, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9468, 132, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9469, 135, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9470, 186, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9471, 185, 16, 1.000000, '2025-04-24 10:17:17', '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (9472, 183, 16, 1.000000, '2025-04-24 10:17:17', '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (9473, 182, 16, 1.000000, '2025-04-24 10:17:17', '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (9474, 161, 16, 1.000000, '2025-04-24 10:17:17', '2025-04-28 14:21:41', NULL);
INSERT INTO `project_references`
VALUES (9475, 160, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9476, 159, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9477, 158, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9478, 157, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9479, 156, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9480, 155, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9481, 154, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9482, 153, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9483, 152, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9484, 151, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9485, 167, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);
INSERT INTO `project_references`
VALUES (9486, 166, 16, 1.000000, '2025-04-24 10:17:17', NULL, NULL);

-- ----------------------------
-- Table structure for result_type
-- ----------------------------
DROP TABLE IF EXISTS `result_type`;
CREATE TABLE `result_type`
(
    `id`   int NOT NULL,
    `text` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '报告结果类型'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of result_type
-- ----------------------------
INSERT INTO `result_type`
VALUES (1, '阳性', '阳');
INSERT INTO `result_type`
VALUES (2, '阴性', '阴');
INSERT INTO `result_type`
VALUES (3, '强阳', '阳');
INSERT INTO `result_type`
VALUES (4, '弱阴', '阴');
INSERT INTO `result_type`
VALUES (5, '+', '阳');
INSERT INTO `result_type`
VALUES (6, '++', '阳');
INSERT INTO `result_type`
VALUES (7, '+++', '阳');

-- ----------------------------
-- Table structure for sample
-- ----------------------------
DROP TABLE IF EXISTS `sample`;
CREATE TABLE `sample`
(
    `id`                        bigint                                                        NOT NULL AUTO_INCREMENT COMMENT '样本ID（key）',
    `sample_id`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样本编号（自定义填写、唯一）',
    `name`                      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
    `sex`                       int                                                           NOT NULL COMMENT '性别 1:男 2：女 3：未知',
    `age`                       int                                                           NOT NULL COMMENT '年龄',
    `birthday`                  date NULL DEFAULT NULL COMMENT '出生日期\r\n',
    `is_birth_input`            int NULL DEFAULT NULL COMMENT '出生日期记录方式（布尔值）。是否使用出生日期生成  1:是 0:否',
    `status`                    int NULL DEFAULT NULL COMMENT '样本状态\r\n 无项目:0\r\n无确认项目:1\r\n等待检测:2\r\n正在检测:3\r\n部分完成:4\r\n全部完成:5',
    `user_id`                   bigint NULL DEFAULT NULL COMMENT '用户ID\r\n',
    `user_group_id`             bigint NULL DEFAULT NULL COMMENT '用户组ID',
    `sample_user_group_id`      bigint NULL DEFAULT NULL COMMENT '样本归属用户组ID',
    `annex_file_id`             varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '附件文件ids：1,2,3',
    `submission_time`           datetime NULL DEFAULT NULL COMMENT '提交时间\r\n',
    `update_time`               datetime NULL DEFAULT NULL COMMENT '更新时间',
    `logistics_tracking_number` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流单号',
    `phone_num_last_four`       varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物流手机号后四位',
    `remarks`                   varchar(1600) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
    `report_id`                 varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报告ids(文件)：1,2,3',
    `submit_number_today`       varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '当天提交编号',
    `is_del`                    tinyint NULL DEFAULT 0 COMMENT '删除状态,0:未删除，1:已删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 113644
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '样本表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sample
-- ----------------------------
INSERT INTO `sample`
VALUES (113640, '20250922-A101', 'test-1', 1, 23, '2002-01-01', 0, 2, 1, 1, 18, '77a780180ac1e2274166f9e9a7878e38',
        '2025-09-22 15:29:58', '2025-09-22 15:37:27', NULL, NULL, NULL, '', 'A101', 0);
INSERT INTO `sample`
VALUES (113641, '20250928-A201', 'test', 1, 7, '2018-01-01', 0, 2, 1, 1, 19, NULL, '2025-09-28 10:47:06',
        '2025-09-28 10:47:06', NULL, NULL, NULL, NULL, 'A201', 0);
INSERT INTO `sample`
VALUES (113642, '20250928-A202', 'test', 1, 4, '2021-01-01', 0, 2, 1, 1, 19, NULL, '2025-09-28 10:48:02',
        '2025-09-28 10:48:02', NULL, NULL, NULL, NULL, 'A202', 0);
INSERT INTO `sample`
VALUES (113643, '20251022-B101', '张三', 3, 3, '2022-01-01', 0, 2, 40, 1, 21, NULL, '2025-10-22 10:52:16',
        '2025-10-22 10:53:09', NULL, NULL, NULL, NULL, 'B101', 0);

-- ----------------------------
-- Table structure for sample_hosting
-- ----------------------------
DROP TABLE IF EXISTS `sample_hosting`;
CREATE TABLE `sample_hosting`
(
    `id`              bigint NOT NULL AUTO_INCREMENT,
    `sample_id`       bigint NULL DEFAULT NULL COMMENT '样本ID',
    `hosting_user_id` bigint NULL DEFAULT NULL COMMENT '代管用户ID',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX             `sample_id` (`sample_id` ASC) USING BTREE,
    INDEX             `hosting_user_id` (`hosting_user_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 87
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '样本代管'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sample_hosting
-- ----------------------------

-- ----------------------------
-- Table structure for sample_project
-- ----------------------------
DROP TABLE IF EXISTS `sample_project`;
CREATE TABLE `sample_project`
(
    `id`              bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关联索引ID（key）',
    `sample_id`       bigint NOT NULL COMMENT '样本ID',
    `project_id`      bigint NOT NULL COMMENT '项目ID',
    `project_status`  int    NOT NULL COMMENT '项目状态\r\n\n\n- 取消:0\n\r\n- 待定:1\r\n\n- 确认检测:2\n\r\n- 正在检测:3\n  \r\n\n- 已完成:4\n  ',
    `file_id`         varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '报告（文件id）',
    `polarity`        varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '阴阳性：阳性、阴性、强阳、弱阴等等\r\n',
    `review_results`  varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '复核结果：正常，异常',
    `delivery_unit`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外送单位',
    `delivery_person` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外送人',
    `remarks`         varchar(1600) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
    `result_img`      varchar(1600) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结果图（存文件id：id1,di2...）',
    `deadline`        datetime NULL DEFAULT NULL COMMENT '截至时间',
    `creation_time`   datetime NULL DEFAULT NULL,
    `update_time`     datetime NULL DEFAULT NULL,
    `is_alarm_sent`   tinyint NULL DEFAULT 0 COMMENT '是否已发送告警通知（0：没发送，1：已发送）',
    `is_del`          tinyint NULL DEFAULT 0 COMMENT '是否删除(1：删除，0：未删除)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX             `sample_id, project_id` (`sample_id` ASC, `project_id` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1083
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '样本项目表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sample_project
-- ----------------------------
INSERT INTO `sample_project`
VALUES (1077, 113640, 243, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-09-22 23:59:58', '2025-09-22 15:29:58',
        '2025-09-22 15:35:45', 0, 0);
INSERT INTO `sample_project`
VALUES (1078, 113641, 5, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-09-28 14:47:06', '2025-09-28 10:47:06',
        '2025-09-28 10:52:44', 1, 0);
INSERT INTO `sample_project`
VALUES (1079, 113642, 6, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-09-28 14:48:02', '2025-09-28 10:48:02',
        '2025-09-28 10:52:44', 1, 0);
INSERT INTO `sample_project`
VALUES (1080, 113643, 244, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-10-22 23:59:16', '2025-10-22 10:52:16',
        NULL, 0, 0);
INSERT INTO `sample_project`
VALUES (1081, 113643, 15, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-10-22 14:52:16', '2025-10-22 10:52:16',
        '2025-10-22 10:53:43', 1, 0);
INSERT INTO `sample_project`
VALUES (1082, 113643, 14, 2, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2025-10-22 14:52:16', '2025-10-22 10:52:16',
        '2025-10-22 10:53:43', 1, 0);

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT,
    `config_key`   varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键，使用点号分隔的多级名称，例如：login.basic.mainTitle',
    `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '配置值',
    `value_type`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'string' COMMENT '配置类型：string、boolean、number、json',
    `description`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置描述',
    `create_time`  datetime NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`  datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `config_key` (`config_key` ASC) USING BTREE,
    INDEX          `idx_config_key` (`config_key` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config`
VALUES (1, 'login.basic.mainTitle', 'CIP系统', 'string', '登录页主标题', '2025-09-18 15:15:35', '2025-09-18 15:15:35');
INSERT INTO `sys_config`
VALUES (2, 'login.basic.subTitle', '管理样本', 'string', '登录页副标题', '2025-09-18 15:15:35', '2025-09-18 15:15:35');
INSERT INTO `sys_config`
VALUES (3, 'login.register.allowRegister', 'true', 'boolean', '是否允许新用户注册', '2025-09-18 15:15:35',
        '2025-09-18 15:15:35');
INSERT INTO `sys_config`
VALUES (4, 'login.sms.enabled', 'false', 'boolean', '是否启用短信登录', '2025-09-18 15:15:35', '2025-09-18 15:15:35');
INSERT INTO `sys_config`
VALUES (5, 'login.security.sliderCaptcha', 'false', 'boolean', '是否启用验证滑块', '2025-09-18 15:15:35',
        '2025-09-18 15:15:35');
INSERT INTO `sys_config`
VALUES (6, 'site.basic.title', '管理系统ASD', 'string', '主页标题', '2025-09-18 15:15:35', '2025-09-19 10:47:25');
INSERT INTO `sys_config`
VALUES (7, 'site.browser.favicon', 'b12436b1cc12e95a0a81f2d7298ac72d', 'string', '浏览器标签图标路径',
        '2025-09-18 15:15:35', '2025-09-29 16:24:35');
INSERT INTO `sys_config`
VALUES (8, 'notify.basic.use', '[1]', 'json', '启用的通知事件', '2025-09-19 10:50:12', '2025-09-19 13:38:08');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`              bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '用户ID（key）',
    `username`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
    `real_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '真实姓名',
    `phone_number`    varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
    `mail`            varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
    `password`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码（加密）',
    `user_group_id`   bigint NULL DEFAULT NULL COMMENT '用户组ID',
    `is_super`        int NULL DEFAULT 0 COMMENT 'is_super 0不是，1是',
    `is_group_super`  int NULL DEFAULT 0 COMMENT 'is_group_super',
    `avail`           tinyint(1) NULL DEFAULT 1 COMMENT '可用状态  1：可用，2：禁用',
    `creation_time`   datetime NULL DEFAULT NULL COMMENT '创建时间',
    `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后一次登录时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `phone_number` (`phone_number` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 83
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (1, 'admin', '真实姓名', '14715923805', '2linudvnx@qq.linux',
        '$2a$10$tRxZO5484u/WNvVnVLg2guD7VDa6nYLuR.YJfOJ3lJQlSEI/44.TO', 1, 1, 1, 1, '2024-05-29 13:48:16',
        '2025-10-22 11:40:56');

-- ----------------------------
-- Table structure for user_group
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group`
(
    `id`                bigint NOT NULL AUTO_INCREMENT COMMENT '用户组ID',
    `group_name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组别名',
    `creation_time`     datetime NULL DEFAULT NULL COMMENT '创建时间',
    `amount_of_users`   bigint NULL DEFAULT 0 COMMENT '用户数量',
    `avail_status`      tinyint(1) NULL DEFAULT 1 COMMENT '用户组可用状态 1可用 0禁止',
    `is_internal_group` tinyint(1) NULL DEFAULT 0 COMMENT '是否内部组（0：否，1：是）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 28
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户组别表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_group
-- ----------------------------
INSERT INTO `user_group`
VALUES (1, '序源', NULL, 17, 1, 1);
INSERT INTO `user_group`
VALUES (2, '客户单位A', NULL, 5, 1, 0);

SET
FOREIGN_KEY_CHECKS = 1;
