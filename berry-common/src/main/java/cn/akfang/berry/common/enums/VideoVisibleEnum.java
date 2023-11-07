package cn.akfang.berry.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum VideoVisibleEnum implements Serializable {
    PUBLIC(1, "公开"),
    PRIVATE(0, "私有"),
    PENDING_PUBLIC(2, "转码中,转码完毕后自动公开"),
    PENDING_PRIVATE(3, "转码中,转码完毕后自动私有");

    public static VideoVisibleEnum getPendingEnum(int code) {
        if (code == PENDING_PUBLIC.code) {
            return PUBLIC;
        } else if (code == PENDING_PRIVATE.code) {
            return PRIVATE;
        } else if (code == PUBLIC.code) {
            return PENDING_PUBLIC;
        } else if (code == PRIVATE.code) {
            return PENDING_PRIVATE;
        }
        return PUBLIC;
    }

    @EnumValue
    private final int code;
    private final String desc;
}
