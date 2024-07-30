/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : program_system

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 23/07/2024 10:32:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '答案表的主键',
  `all_option` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '当前题目所有答案的信息',
  `images` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '答案的图片路径',
  `analysis` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '答案解析',
  `question_id` int NOT NULL COMMENT '对应题目的id',
  `true_option` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '正确的选项对应的下标',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `question_id`(`question_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of answer
-- ----------------------------
INSERT INTO `answer` VALUES (1, '1,2', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-10-26/b1535亚索锐雯.jpg', '1', 5, '0');
INSERT INTO `answer` VALUES (3, '语文,数学,英语,选修课', ',', NULL, 6, '0,1,2');
INSERT INTO `answer` VALUES (10, '0,1', '', '111', 11, '0');
INSERT INTO `answer` VALUES (11, '11,16', '', '16', 12, '1');
INSERT INTO `answer` VALUES (12, '9,8', '', '9', 13, '0');
INSERT INTO `answer` VALUES (13, '4,3', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-10-27/1950327D.jpg', '4', 14, '0');
INSERT INTO `answer` VALUES (14, '18,11', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-10-27/583a91522134986336591.jpg', '18', 15, '0');
INSERT INTO `answer` VALUES (15, '1', '', '1', 16, '0');
INSERT INTO `answer` VALUES (16, '4,3', '', '4', 17, '0');
INSERT INTO `answer` VALUES (17, '1,2,3,4', ',,,', NULL, 18, '0,1');
INSERT INTO `answer` VALUES (18, '奇数,偶数', '', '奇数', 19, '0');
INSERT INTO `answer` VALUES (19, '奇数,偶数', '', '奇数', 20, '0');
INSERT INTO `answer` VALUES (20, '111,222,333', '', NULL, 21, '0,1,2');
INSERT INTO `answer` VALUES (21, '1', '', '1', 22, '0');
INSERT INTO `answer` VALUES (22, '', '', '', 23, '0');

-- ----------------------------
-- Table structure for class
-- ----------------------------
DROP TABLE IF EXISTS `class`;
CREATE TABLE `class`  (
  `id` int NOT NULL,
  `class_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级编号',
  `class_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级名字',
  `class_leader` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '负责人名字',
  `class_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class
-- ----------------------------

-- ----------------------------
-- Table structure for exam
-- ----------------------------
DROP TABLE IF EXISTS `exam`;
CREATE TABLE `exam`  (
  `exam_id` int NOT NULL AUTO_INCREMENT,
  `exam_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试名称',
  `exam_desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '考试描述',
  `type` int NOT NULL DEFAULT 1 COMMENT '1完全公开  2需要密码',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '需要密码考试的密码',
  `duration` int NOT NULL COMMENT '考试时长',
  `start_time` date NULL DEFAULT NULL COMMENT '考试开始时间',
  `end_time` date NULL DEFAULT NULL COMMENT '考试结束时间',
  `total_score` int NOT NULL COMMENT '考试总分',
  `pass_score` int NOT NULL COMMENT '考试通过线',
  `status` int NOT NULL DEFAULT 1 COMMENT '1有效 2无效',
  PRIMARY KEY (`exam_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam
-- ----------------------------
INSERT INTO `exam` VALUES (9, '小学入学考试', '对小学生做一个评估', 2, NULL, 1, NULL, NULL, 10, 6, 1);
INSERT INTO `exam` VALUES (10, '多选题练习考试', '滴滴', 1, NULL, 1, NULL, NULL, 2, 1, 1);
INSERT INTO `exam` VALUES (11, '测试123', '2113', 2, '12345', 1, '2020-11-01', NULL, 1, 1, 1);
INSERT INTO `exam` VALUES (12, '全能考试', '啥都考', 1, NULL, 4, NULL, NULL, 12, 7, 1);
INSERT INTO `exam` VALUES (13, '过期的考试', '测试过期', 1, NULL, 1, '2020-10-31', '2020-11-01', 3, 1, 1);
INSERT INTO `exam` VALUES (14, '阿达', '阿达', 1, NULL, 1, '2021-01-04', '2021-01-22', 3, 1, 1);
INSERT INTO `exam` VALUES (15, 'test', 'desc', 1, NULL, 2, '2021-01-03', '2021-01-19', 10, 1, 1);
INSERT INTO `exam` VALUES (16, '15', 'asd', 2, '15', 60, NULL, NULL, 12, 12, 1);

-- ----------------------------
-- Table structure for exam_question
-- ----------------------------
DROP TABLE IF EXISTS `exam_question`;
CREATE TABLE `exam_question`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `question_ids` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试的题目id列表',
  `exam_id` int NOT NULL COMMENT '考试的id',
  `scores` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '每一题的分数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam_question
-- ----------------------------
INSERT INTO `exam_question` VALUES (5, '12,13,15,3,6,8,18,11,19,14', 9, '1,1,1,1,1,1,1,1,1,1');
INSERT INTO `exam_question` VALUES (6, '6,18', 10, '1,1');
INSERT INTO `exam_question` VALUES (7, '3', 11, '1');
INSERT INTO `exam_question` VALUES (8, '3,6,8,11,12,13,14,15,18,19,20,21', 12, '1,1,1,1,1,1,1,1,1,1,1,1');
INSERT INTO `exam_question` VALUES (9, '18,19,15', 13, '1,1,1');
INSERT INTO `exam_question` VALUES (12, '3,21,22', 14, '1,1,1');
INSERT INTO `exam_question` VALUES (13, '11,8,3,12,13,14,18,15,19,6', 15, '1,1,1,1,1,1,1,1,1,1');
INSERT INTO `exam_question` VALUES (15, '6,8,11', 16, '10,1,1');

-- ----------------------------
-- Table structure for exam_record
-- ----------------------------
DROP TABLE IF EXISTS `exam_record`;
CREATE TABLE `exam_record`  (
  `record_id` int NOT NULL AUTO_INCREMENT COMMENT '考试记录的id',
  `user_id` int NOT NULL COMMENT '考试用户的id',
  `user_answers` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户的答案列表',
  `credit_img_url` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '考试诚信截图',
  `exam_id` int NOT NULL COMMENT '考试的id',
  `logic_score` int NULL DEFAULT NULL COMMENT '考试的逻辑得分(除简答)',
  `exam_time` datetime NOT NULL COMMENT '考试时间',
  `question_ids` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '考试的题目信息',
  `total_score` int NULL DEFAULT NULL COMMENT '考试总分数 (逻辑+简答)',
  `error_question_ids` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户考试的错题',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of exam_record
-- ----------------------------
INSERT INTO `exam_record` VALUES (1, 1, '1-1-0-1-0-1,2-1,2-1-java是全世界最好的语言.jpg-小学是童年', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-11-05/b09d7examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-11-05/ab243examTakePhoto.png', 9, 3, '2020-11-05 19:28:40', '11,13,14,12,15,6,18,19,3,8', 5, '11,13,6,18,19,14');
INSERT INTO `exam_record` VALUES (2, 1, '1', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-11-07/95c31examTakePhoto.png', 11, 0, '2020-11-07 11:34:50', '3', 1, NULL);
INSERT INTO `exam_record` VALUES (3, 1, '0-1-0-0-0-0,1,2-0,1-0,1,2-0-0-12345-56', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-11-08/2d0e2examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-11-08/bbca9examTakePhoto.png', 12, 10, '2020-11-08 09:15:16', '11,12,13,14,15,6,18,21,19,20,3,8', 12, NULL);
INSERT INTO `exam_record` VALUES (4, 2, '0-0-0-1-0-0,1,2-0,1-0-wqe-eqweq', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-11-08/411a8examTakePhoto.png', 9, 8, '2020-11-08 09:20:27', '11,13,14,12,15,6,18,19,3,8', 10, NULL);
INSERT INTO `exam_record` VALUES (5, 2, '0,1,2-1,2', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-11-08/ebdd9examTakePhoto.png', 10, 1, '2020-11-08 10:57:00', '6,18', 1, '18');
INSERT INTO `exam_record` VALUES (6, 1, '0-0-0-0-1-1-2-1-23-156', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/8c282examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/7fdf0examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/ca048examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/2cafcexamTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/bbf68examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/977b7examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/f8061examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/948c5examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/eff69examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/811aaexamTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/d9279examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/3fc0cexamTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/6ff2eexamTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/c4400examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/243b6examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/07a2fexamTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/f1176examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/07c1bexamTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/d0a45examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/b8037examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/95438examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/b185dexamTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/259b8examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/a8a37examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/08f17examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/329a5examTakePhoto.png', 9, 5, '2020-12-01 21:41:58', '11,15,13,14,12,18,6,19,3,8', NULL, '18,6,19');
INSERT INTO `exam_record` VALUES (7, 1, '0-0-0-1-0-0,1,2-0,1-0-java-大大', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/41206examTakePhoto.png', 9, 8, '2020-12-01 21:45:30', '11,13,14,12,15,6,18,19,3,8', NULL, NULL);
INSERT INTO `exam_record` VALUES (9, 1, '0-1-0-0-0-1-2-1-是-sad', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-12-01/0fe10examTakePhoto.png', 9, 3, '2020-12-01 21:48:45', '11,13,14,12,15,6,18,19,3,8', NULL, '13,12,6,18,19');
INSERT INTO `exam_record` VALUES (10, 1, '0-0-0-0-0-1,3-1,2,3-1-656', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2021-01-14/b9f75examTakePhoto.png,https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2021-01-14/bc3f7examTakePhoto.png', 9, 4, '2021-01-14 18:45:42', '12,13,15,11,14,6,18,19,3,8', 6, '12,6,18,19');
INSERT INTO `exam_record` VALUES (14, 1, '0-1-0-1-1-1--1 -', 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2021-01-15/eb067examTakePhoto.png', 9, 1, '2021-01-15 10:25:05', '12,11,14,13,15,6,18,19,3,8', NULL, '12,11,13,15,6,18,19');
INSERT INTO `exam_record` VALUES (15, 2, '1-0-0 - - - - - -', '', 9, 2, '2021-01-15 10:18:54', '12,13,15,11,14,6,18,19,3,8', 4, '15,11,14,6,18,19');
INSERT INTO `exam_record` VALUES (16, 1, '- - - - - - - - - - - -  - - - - - - - - - - - -', '', 12, 0, '2021-02-08 13:31:19', '11,12,13,14,15,6,18,21,19,20,3,8', NULL, '11,12,13,14,15,6,18,21,19,20');
INSERT INTO `exam_record` VALUES (17, 1, '0------0,1,2----java web-php web', '', 12, 1, '2021-02-08 13:38:39', '11,12,13,14,15,6,18,21,19,20,3,8', NULL, '12,13,14,15,6,18,21,19,20');
INSERT INTO `exam_record` VALUES (18, 1, '0,1,2-0,1', '', 10, 2, '2021-02-08 13:38:57', '6,18', NULL, NULL);
INSERT INTO `exam_record` VALUES (23, 1, '0-1-0-0-0-1-1-2-1-0-asd-sd', ',', 12, 6, '2024-07-22 00:17:48', '11,12,13,14,15,6,18,21,19,20,3,8', NULL, '6,18,21,19');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `n_id` int NOT NULL AUTO_INCREMENT COMMENT '系统公告id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告内容',
  `create_time` datetime NULL DEFAULT NULL COMMENT '公告创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新此公告时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '0(不是当前系统公告) 1(是当前系统公告)',
  PRIMARY KEY (`n_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '<div><br/></div><ul><ol><li>注册验证码校验</li><li>主页面小屏样式更改</li><li>题库训练弹窗在小屏样式调整</li><li>修复添加考试时选择需要密码权限时,密码为空的情况</li><li>修复更新考试时选择需要密码权限时,密码为空的情况</li><li><font color=\"#f9963b\" style=\"background-color: rgb(238, 236, 224);\">新增</font><font color=\"#f9963b\" style=\"\"><span style=\"background-color: rgb(139, 170, 74);\">公告</span><span style=\"background-color: rgb(238, 236, 224);\">功能</span></font>(管理员可改,其他用户可以查看公告)asd</li><li>修复考试提交试卷业务逻辑BUG</li></ol></ul>', '2021-02-07 15:52:45', '2024-07-22 22:54:08', 1);
INSERT INTO `notice` VALUES (4, '<p>发布公告测试aasdasd<br/></p>', '2021-02-07 17:02:07', '2024-07-22 22:38:14', 0);
INSERT INTO `notice` VALUES (5, '<p>asd新擦手动sd</p>', '2024-07-22 22:30:00', '2024-07-22 22:38:18', 0);

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `qu_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '问题内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `qu_type` int NOT NULL COMMENT '问题类型 1单选 2多选 3判断 4简答',
  `level` int NOT NULL DEFAULT 1 COMMENT '题目难度 1简单 2中等 3困难',
  `image` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '图片',
  `qu_bank_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属题库id',
  `qu_bank_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属题库名称',
  `analysis` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '解析',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (3, '实现web后端的语言', '2020-10-24 16:30:08', 'wzz', 4, 2, NULL, '1,2,5', '小学数学题库,生活小常识,java开发', '解析');
INSERT INTO `question` VALUES (6, '以下哪些语言是必修课', '2020-11-02 10:11:28', 'wzz', 2, 1, NULL, '1,5', '小学数学题库,java开发', '语文 数学 英语');
INSERT INTO `question` VALUES (8, '说说小学是什么样的?', '2020-11-02 10:12:02', 'wzz', 4, 3, NULL, '1,5', '小学数学题库,java开发', '说亲身经历即可');
INSERT INTO `question` VALUES (11, '1-1', '2020-10-27 14:35:33', 'wzz', 1, 1, NULL, '1,5', '小学数学题库,java开发', '0');
INSERT INTO `question` VALUES (12, '8+8', '2020-10-27 15:32:44', 'wzz', 1, 3, NULL, '1,5', '小学数学题库,java开发', '16');
INSERT INTO `question` VALUES (13, '1 * 9', '2020-10-27 15:13:38', 'wzz', 1, 1, NULL, '1,5', '小学数学题库,java开发', '9');
INSERT INTO `question` VALUES (14, '2+2', '2020-10-27 16:17:09', 'wzz', 1, 1, 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-10-27/e4f71d9b67b4a15ce2ec638c908b00dc506ab.png', '1,5', '小学数学题库,java开发', '4');
INSERT INTO `question` VALUES (15, '9+9', '2020-10-27 15:53:14', 'wzz', 1, 1, 'https://wangsiryun.oss-cn-beijing.aliyuncs.com/images/upload/2020-10-27/47ccdd9b67b4a15ce2ec638c908b00dc506ab.png', '1,5', '小学数学题库,java开发', '18');
INSERT INTO `question` VALUES (18, '最接近0的两个数', '2020-11-02 10:09:13', 'wzz', 2, 2, NULL, '1', '小学数学题库', '1和2');
INSERT INTO `question` VALUES (19, '1是不是奇数', '2020-10-31 14:54:09', 'wzz', 3, 1, NULL, '1', '小学数学题库', '是');
INSERT INTO `question` VALUES (20, '9是奇数还是偶数', '2020-11-02 10:21:19', 'wzz', 3, 1, NULL, '1', '小学数学题库', '奇数');
INSERT INTO `question` VALUES (21, '哪几个是三位数', '2020-11-05 14:42:56', 'wzz', 2, 1, NULL, '1,2', '小学数学题库,生活小常识', '数数');
INSERT INTO `question` VALUES (22, '测试', '2020-12-27 21:03:31', 'wzz', 1, 1, NULL, '2', '生活小常识', '测试');

-- ----------------------------
-- Table structure for question_bank
-- ----------------------------
DROP TABLE IF EXISTS `question_bank`;
CREATE TABLE `question_bank`  (
  `bank_id` int NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`bank_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question_bank
-- ----------------------------
INSERT INTO `question_bank` VALUES (1, '小学数学题库');
INSERT INTO `question_bank` VALUES (2, '生活小常识');
INSERT INTO `question_bank` VALUES (5, 'java开发');
INSERT INTO `question_bank` VALUES (6, '154');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '所属上级',
  `top_menu_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '菜单标题',
  `topIcon` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '图标',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '组件名称',
  `sort_value` int NOT NULL DEFAULT 1 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态(0:禁止,1:正常)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '产品介绍', 'el-icon-odometer', '/dashboard', 1, 1, '2024-07-23 10:22:12', '2024-07-23 10:22:12', 0);
INSERT INTO `sys_menu` VALUES (2, 0, '在线考试', 'el-icon-menu', '', 2, 1, '2024-07-23 10:22:52', '2024-07-23 10:23:28', 0);
INSERT INTO `sys_menu` VALUES (3, 2, '在线考试', 'el-icon-s-promotion', '/examOnline', 1, 1, '2024-07-23 10:23:16', '2024-07-23 10:24:18', 0);
INSERT INTO `sys_menu` VALUES (4, 2, '我的成绩', 'el-icon-trophy', '/myGrade', 2, 1, '2024-07-23 10:23:50', '2024-07-23 10:24:19', 0);
INSERT INTO `sys_menu` VALUES (5, 2, '我的题库', 'el-icon-notebook-2', '/myQuestionBank', 3, 1, '2024-07-23 10:24:10', '2024-07-23 10:24:26', 0);
INSERT INTO `sys_menu` VALUES (6, 0, '考试管理', 'el-icon-bangzhu', '', 3, 1, '2024-07-23 10:24:16', '2024-07-23 10:24:58', 0);
INSERT INTO `sys_menu` VALUES (7, 6, '题库管理', 'el-icon-aim', '/questionBankMange', 1, 1, '2024-07-23 10:25:21', '2024-07-23 10:26:35', 0);
INSERT INTO `sys_menu` VALUES (8, 6, '试题管理', 'el-icon-news', '/questionManage', 1, 1, '2024-07-23 10:25:38', '2024-07-23 10:26:37', 0);
INSERT INTO `sys_menu` VALUES (9, 6, '考试管理', 'el-icon-tickets', '/examManage', 1, 1, '2024-07-23 10:25:55', '2024-07-23 10:26:38', 0);
INSERT INTO `sys_menu` VALUES (10, 6, '阅卷管理', 'el-icon-view', '/markManage', 1, 1, '2024-07-23 10:26:17', '2024-07-23 10:26:41', 0);
INSERT INTO `sys_menu` VALUES (11, 0, '考试统计', 'el-icon-pie-chart', '', 4, 1, '2024-07-23 10:26:28', '2024-07-23 10:26:56', 0);
INSERT INTO `sys_menu` VALUES (12, 11, '统计总览', 'el-icon-data-line', '/staticOverview', 1, 1, '2024-07-23 10:27:17', '2024-07-23 10:27:22', 0);
INSERT INTO `sys_menu` VALUES (13, 0, '系统设置', 'el-icon-setting', '', 20, 1, '2024-07-23 10:27:35', '2024-07-23 10:28:20', 0);
INSERT INTO `sys_menu` VALUES (14, 13, '公告管理', 'el-icon-bell', '/noticeManage', 1, 1, '2024-07-23 10:28:04', '2024-07-23 10:31:47', 0);
INSERT INTO `sys_menu` VALUES (15, 13, '角色管理', 'el-icon-s-custom', '/roleManage', 2, 1, '2024-07-23 10:28:43', '2024-07-23 10:31:51', 0);
INSERT INTO `sys_menu` VALUES (16, 13, '用户管理', 'el-icon-user-solid', '/userManage', 3, 1, '2024-07-23 10:29:03', '2024-07-23 10:31:52', 0);
INSERT INTO `sys_menu` VALUES (17, 13, '菜单管理', 'el-icon-s-custom', '/sysMenu', 4, 1, '2024-07-23 10:29:25', '2024-07-23 10:31:54', 0);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL DEFAULT 0,
  `menu_id` bigint NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  `is_half` tinyint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id` ASC) USING BTREE,
  INDEX `idx_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 189 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (189, 1, 1, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (190, 1, 14, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (191, 1, 2, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (192, 1, 3, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (193, 1, 4, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (194, 1, 5, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (195, 1, 15, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (196, 1, 6, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (197, 1, 7, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (198, 1, 8, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (199, 1, 9, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (200, 1, 10, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (201, 1, 16, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (202, 1, 11, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (203, 1, 12, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (204, 1, 17, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);
INSERT INTO `sys_role_menu` VALUES (205, 1, 13, '2024-07-23 10:31:28', '2024-07-23 10:31:28', 0, 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL DEFAULT 1 COMMENT '1(学生) 2(教师) 3(管理员)',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `true_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `salt` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` int NOT NULL DEFAULT 1 COMMENT '用户是否被禁用 1正常 2禁用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 1, 'lin', 'linxf', 'a010e351825009f203408aa64588e685', '18355664852', 'c667d6', 1, '2020-10-22 15:05:15', '2024-07-22 21:59:24', 0);
INSERT INTO `user` VALUES (2, 2, 'w', '学生王某', '9499273223c7aca5949e3055eaa57f6f', NULL, 'c667d6', 1, '2020-10-22 10:46:25', '2024-07-22 22:13:58', 0);
INSERT INTO `user` VALUES (3, 2, 'zz', '教师', 'c45cd81a5f4f205bc6f5fbec7f42faaa', NULL, '304cba', 1, '2020-10-22 11:10:12', '2024-07-22 22:14:07', 0);
INSERT INTO `user` VALUES (4, 1, 'lx', '刘', '98d4fa4da534339cbed32b62d7b246c7', NULL, 'ccb14b', 1, '2020-10-22 18:13:20', '2024-07-22 13:54:39', 1);
INSERT INTO `user` VALUES (5, 1, 'mc', '马', '2a2176cb62f5a62d396dbb2bdeed294c', NULL, '605d1c', 1, '2020-10-22 15:51:51', '2024-07-22 13:54:49', 1);
INSERT INTO `user` VALUES (6, 1, 'amao', '阿毛', '41b8d2eff6cd26fc425be6ab343de397', NULL, '8e2880', 1, '2020-10-22 15:52:30', '2024-07-22 13:54:46', 1);
INSERT INTO `user` VALUES (8, 1, 'mq', 'sada', '6d0e86b7df3040a3de6b7dce73a7d818', NULL, '8097b9', 1, '2020-10-22 15:54:48', '2024-07-22 13:54:45', 1);
INSERT INTO `user` VALUES (9, 2, 'shepi', '蛇皮', '4ba6f54cc49fac7afc907b0b5fbfd7ac', NULL, 'c6ce5f', 1, '2020-10-22 15:55:04', '2024-07-22 22:11:16', 0);
INSERT INTO `user` VALUES (10, 1, 'zzb', '张', '62e9ce36958d0ee247a145d29caab889', NULL, '9a9d8c', 1, '2020-10-22 15:55:25', '2024-07-22 13:54:42', 1);
INSERT INTO `user` VALUES (11, 2, 'pgl', '潘', 'f7dcc6093292b30c698ec73bca37a7a4', NULL, 'd6da4b', 1, '2020-10-22 15:55:52', '2024-07-22 22:11:24', 0);
INSERT INTO `user` VALUES (12, 2, 'wjh', '王', 'c94de75b9de1b60e2c5261dfade215f2', NULL, '33641a', 1, '2020-10-23 09:54:55', '2024-07-22 22:11:19', 0);
INSERT INTO `user` VALUES (13, 3, 'll', '丽丽', '0b600b5d50de0f1207a2a045bc22debf', NULL, '0824f0', 1, '2020-10-23 10:02:09', '2024-07-22 15:34:07', 0);
INSERT INTO `user` VALUES (14, 2, 'xx', '小熊', '2b1caf8f3510fdffa7a99835255b88fe', NULL, '72505b', 1, '2020-11-30 12:25:55', '2024-07-22 15:00:24', 0);
INSERT INTO `user` VALUES (15, 1, 'asd', 'asd', 'f01fb4112c65da3882e3fbbe9e7bd35e', NULL, '92cf40', 1, '2024-07-22 13:26:17', '2024-07-22 13:51:31', 1);
INSERT INTO `user` VALUES (16, 1, 'asdasd', 'asd', 'a063101066f9af19cab7bb23f5bb7d58', NULL, '27572e', 1, '2024-07-22 13:34:46', '2024-07-22 13:51:26', 1);
INSERT INTO `user` VALUES (17, 3, 'linlin', 'LIN', 'ebb5dba4b7741f9a4889738c3d9f877a', '18377997162', '690235', 1, '2024-07-22 13:36:50', '2024-07-22 13:50:43', 1);
INSERT INTO `user` VALUES (18, 2, 'zxczx', 'zxczxc', 'cae06ad5d9a9c558aa6383350d744c5a', '', '7c6608', 1, '2024-07-22 20:53:39', '2024-07-22 22:11:35', 0);
INSERT INTO `user` VALUES (19, 4, 'zxc', 'zxc', '363ca9bbbdb5461c599933b5f370d956', '', '1c9025', 1, '2024-07-22 20:55:54', '2024-07-22 21:06:09', 1);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '1学生 2教师 3超级管理员',
  `role_name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_info` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:可用 1:已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, '超级管理员', '有系统中的全部权限', '2024-07-22 19:49:30', '2024-07-22 22:00:06', 0);
INSERT INTO `user_role` VALUES (2, '教师', '辅助学生学习', '2024-07-22 19:49:30', '2024-07-22 22:20:56', 0);
INSERT INTO `user_role` VALUES (3, '学生', '主要服务对象', '2024-07-22 19:49:30', '2024-07-22 22:00:22', 0);

SET FOREIGN_KEY_CHECKS = 1;
