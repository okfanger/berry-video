package cn.akfang.berry.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum LikeTypeEnum implements Serializable {
    VIDEO(1, "视频点赞", "MAP_KEY_VIDEO_LIKED", "MAP_KEY_VIDEO_LIKED_COUNT"),
    COMMENT(2, "评论点赞", "MAP_KEY_COMMENT_LIKED", "MAP_KEY_COMMENT_LIKED_COUNT"),
    FAVOR(3, "收藏点赞", "MAP_KEY_FAVOR_LIKED", "MAP_KEY_COMMENT_FAVOR_COUNT");

    @EnumValue
    private final Integer code;
    private final String desc;
    private final String redisKey;
    private final String redisCountKey;
}
