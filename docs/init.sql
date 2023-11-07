/*
 Navicat Premium Data Transfer

 Source Server         : tencent-bj5
 Source Server Type    : MySQL
 Source Server Version : 80022 (8.0.22-cynos)
 Source Host           : bj-cynosdbmysql-grp-32iobaoo.sql.tencentcdb.com:28446
 Source Schema         : berryvideo-dev

 Target Server Type    : MySQL
 Target Server Version : 80022 (8.0.22-cynos)
 File Encoding         : 65001

 Date: 07/11/2023 22:49:56
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_channel`;
CREATE TABLE `t_channel`
(
    `id`         bigint                                  NOT NULL AUTO_INCREMENT COMMENT 'id',
    `label`      varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '频道名称',
    `totalCount` bigint                                  NOT NULL COMMENT '总数',
    `updateTime` datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `icon`       varchar(255) COLLATE utf8mb4_general_ci          DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='频道表';

-- ----------------------------
-- Table structure for t_channel_video
-- ----------------------------
DROP TABLE IF EXISTS `t_channel_video`;
CREATE TABLE `t_channel_video`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `channelId`  bigint   NOT NULL COMMENT '频道Id',
    `videoId`    bigint   NOT NULL COMMENT '短视频id',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='视频-频道关联表';

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `authorId`        bigint                                                        NOT NULL COMMENT '评论的视频是哪个作者的关联id',
    `fatherCommentId` bigint                                                                 DEFAULT NULL COMMENT '如果是回复留言，则本条为子留言，需要关联查询',
    `videoId`         bigint                                                        NOT NULL COMMENT '回复的那个视频id',
    `commentUserId`   bigint                                                        NOT NULL COMMENT '发布留言的用户id',
    `content`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '留言内容',
    `likeCounts`      int                                                           NOT NULL COMMENT '留言的点赞总数',
    `createTime`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`        tinyint                                                       NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='评论表';

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `key`        varchar(255) NOT NULL,
    `hash`       varchar(255) NOT NULL,
    `bucket`     varchar(255) NOT NULL,
    `fsize`      varchar(255) NOT NULL,
    `metadata`   varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    `createTime` datetime     NOT NULL                                    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime     NOT NULL                                    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint      NOT NULL                                    DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_file_key` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=1721885481407954947 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_follow
-- ----------------------------
DROP TABLE IF EXISTS `t_follow`;
CREATE TABLE `t_follow`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `fromId`     bigint   NOT NULL COMMENT '关注者',
    `toId`       bigint   NOT NULL COMMENT '被关注的人',
    `friend`     tinyint  NOT NULL COMMENT '是否为好友',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint  NOT NULL DEFAULT '0' COMMENT '是否删除',
    `status`     int               DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='粉丝表\n\n';

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `nickName`     varchar(256)          DEFAULT NULL COMMENT '用户昵称',
    `userName`     varchar(256) NOT NULL COMMENT '账号',
    `userAvatar`   varchar(1024)         DEFAULT NULL COMMENT '用户头像',
    `gender`       tinyint               DEFAULT NULL COMMENT '性别',
    `userRole`     varchar(256) NOT NULL DEFAULT 'user' COMMENT '用户角色：user / admin',
    `userPassword` varchar(512) NOT NULL COMMENT '密码',
    `createTime`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     tinyint      NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_userName` (`userName`)
) ENGINE=InnoDB AUTO_INCREMENT=1719034911647121412 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户';

-- ----------------------------
-- Table structure for t_user_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `t_user_comment_like`;
CREATE TABLE `t_user_comment_like`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `userId`     bigint   NOT NULL COMMENT '用户id',
    `commentId`  bigint   NOT NULL COMMENT '喜欢的评论id',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint  NOT NULL DEFAULT '0' COMMENT '是否删除',
    `status`     int               DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `writer_id` (`userId`,`commentId`) USING BTREE,
    KEY          `fk_my_liked_videoId` (`commentId`),
    CONSTRAINT `t_user_comment_like_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `t_user_comment_like_ibfk_2` FOREIGN KEY (`commentId`) REFERENCES `t_video` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2937 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='点赞短视频关联表';

-- ----------------------------
-- Table structure for t_user_video_favor
-- ----------------------------
DROP TABLE IF EXISTS `t_user_video_favor`;
CREATE TABLE `t_user_video_favor`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `userId`     bigint   NOT NULL COMMENT '用户id',
    `videoId`    bigint   NOT NULL COMMENT '喜欢的短视频id',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint  NOT NULL DEFAULT '0' COMMENT '是否删除',
    `status`     int               DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `writer_id` (`userId`,`videoId`) USING BTREE,
    KEY          `fk_my_liked_videoId` (`videoId`),
    CONSTRAINT `t_user_video_favor_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `t_user_video_favor_ibfk_2` FOREIGN KEY (`videoId`) REFERENCES `t_video` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='点赞短视频关联表';

-- ----------------------------
-- Table structure for t_user_video_like
-- ----------------------------
DROP TABLE IF EXISTS `t_user_video_like`;
CREATE TABLE `t_user_video_like`
(
    `id`         bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `userId`     bigint   NOT NULL COMMENT '用户id',
    `videoId`    bigint   NOT NULL COMMENT '喜欢的短视频id',
    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`   tinyint  NOT NULL DEFAULT '0' COMMENT '是否删除',
    `status`     int               DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `writer_id` (`userId`,`videoId`) USING BTREE,
    KEY          `fk_my_liked_videoId` (`videoId`),
    CONSTRAINT `t_user_video_like_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `t_user_video_like_ibfk_2` FOREIGN KEY (`videoId`) REFERENCES `t_video` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='点赞短视频关联表';

-- ----------------------------
-- Table structure for t_video
-- ----------------------------
DROP TABLE IF EXISTS `t_video`;
CREATE TABLE `t_video`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `authorId`     bigint                                                        NOT NULL COMMENT '对应用户表id，视频发布者',
    `cover`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci          DEFAULT NULL COMMENT '视频封面',
    `content`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频标题，可以为空',
    `fileId`       bigint                                                        NOT NULL,
    `likeCount`    int                                                                    DEFAULT '0' COMMENT '点赞总数',
    `commentCount` int                                                                    DEFAULT '0' COMMENT '评论总数',
    `favorCount`   int                                                                    DEFAULT NULL,
    `shareCount`   int                                                                    DEFAULT '0',
    `visible`      int                                                           NOT NULL COMMENT '是否私密，用户可以设置私密，如此可以不公开给比人看',
    `createTime`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     tinyint                                                       NOT NULL DEFAULT '0' COMMENT '是否删除',
    `defaultUrl`   varchar(1024) COLLATE utf8mb4_general_ci                               DEFAULT NULL,
    `tags`         varchar(1024) COLLATE utf8mb4_general_ci                               DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY            `uni_idx_user_video` (`id`,`authorId`),
    KEY            `idx_video_authorid` (`authorId`)
) ENGINE=InnoDB AUTO_INCREMENT=1721885727160582147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='短视频表';

SET
FOREIGN_KEY_CHECKS = 1;
