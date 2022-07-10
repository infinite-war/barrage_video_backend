
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_barrage_msg
-- ----------------------------
DROP TABLE IF EXISTS `t_barrage_msg`;
CREATE TABLE `t_barrage_msg`  (
  `id` bigint(19) NOT NULL COMMENT '主键',
  `user_id` bigint(19) NULL DEFAULT NULL COMMENT '用户ID',
  `video_id` bigint(19) NULL DEFAULT NULL COMMENT '视频ID',
  `msg_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
  `msg_position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息位置',
  `msg_color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息颜色',
  `video_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送消息的视频时间',
  `del_flag` int(1) NULL DEFAULT 0 COMMENT '删除标识 0-未删除 1-已删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_barrage_msg_sensitive
-- ----------------------------
DROP TABLE IF EXISTS `t_barrage_msg_sensitive`;
CREATE TABLE `t_barrage_msg_sensitive`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `sensitive_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '敏感词',
  `show_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '展示词',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '敏感词汇过滤和转义表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
