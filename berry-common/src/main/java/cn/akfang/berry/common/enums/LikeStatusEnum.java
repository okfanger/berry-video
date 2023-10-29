package cn.akfang.berry.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum LikeStatusEnum implements Serializable {

    LIKE(1, "点赞"),
    UNLIKE(0, "取消点赞/未点赞");

    private final Integer code;
    private final String desc;
}
