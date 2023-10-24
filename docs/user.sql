CREATE TABLE `t_user`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `nickName`     varchar(256)          DEFAULT NULL COMMENT '用户昵称',
    `userName`     varchar(256) NOT NULL COMMENT '账号',
    `userAvatar`   varchar(1024)         DEFAULT NULL COMMENT '用户头像',
    `gender`       tinyint(4) DEFAULT NULL COMMENT '性别',
    `userRole`     varchar(256) NOT NULL DEFAULT 'user' COMMENT '用户角色：user / admin',
    `userPassword` varchar(512) NOT NULL COMMENT '密码',
    `createTime`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uni_userName` (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';