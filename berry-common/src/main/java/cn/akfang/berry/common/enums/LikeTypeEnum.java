package cn.akfang.berry.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum LikeTypeEnum implements Serializable {
    VIDEO(1, "视频点赞", "MAP_KEY_VIDEO_LIKED"),
    COMMENT(2, "评论点赞", "MAP_KEY_COMMENT_LIKED"),
    FAVOR(3, "收藏点赞", "MAP_KEY_FAVOR_LIKED");

    @EnumValue
    private final Integer code;
    private final String desc;
    private final String redisKey;

    public String getRedisHashCountKey() {
        return redisKey + "_COUNT";
    }

    public String getRedisZSetCountkey() {
        return redisKey + "_ZSET_COUNT";
    }
}
