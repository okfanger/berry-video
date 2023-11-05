package cn.akfang.berry.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum LikeTypeEnum implements Serializable {
    VIDEO(1, "视频点赞", "MAP_KEY_VIDEO_LIKED", "id", "userId", "videoId", "status"),
    COMMENT(2, "评论点赞", "MAP_KEY_VIDEO_COMMENT_LIKED", "id", "userId", "commentId", "status"),
    FAVOR(3, "收藏点赞", "MAP_KEY_VIDEO_FAVORED", "id", "userId", "videoId", "status"),
    FOLLOW(4, "关注", "MAP_KEY_USER_FOLLOW", "id", "fromId", "toId", "status");

    @EnumValue
    private final Integer code;
    private final String desc;
    private final String redisKey;
    private String dbIdColumn = "id";
    private String dbFromIdColumn = "userId";
    private String dbToIdColumn = "videoId";
    private String dbStatusColumn = "status";

    public String getRedisHashCountKey() {
        return redisKey + "_COUNT";
    }

    public String getRedisZSetCountKey() {
        return redisKey + "_ZSET_COUNT";
    }
}
