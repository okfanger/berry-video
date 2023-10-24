package cn.akfang.berry.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum UserGenderEnum implements Serializable {
    MALE(0, "男性"),
    FEMALE(1, "女性");

    @EnumValue
    private final int code;
    private final String desc;
}
