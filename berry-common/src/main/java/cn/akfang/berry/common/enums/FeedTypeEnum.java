package cn.akfang.berry.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum FeedTypeEnum implements Serializable {

    PRODUCE("produce", "作品"),
    UPVOTE("upvote", "点赞"),
    COLLECT("collect", "收藏");

    @EnumValue
    private final String key;
    private final String name;
}
