package cn.akfang.berry.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum UserRoleEnum implements Serializable {
    ADMIN(0, "管理员"),
    USER(1, "普通用户");

    @EnumValue
    private final int code;
    private final String desc;
}
