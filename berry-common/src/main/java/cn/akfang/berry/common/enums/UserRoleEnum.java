package cn.akfang.berry.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum UserRoleEnum implements Serializable {
    ADMIN("admin", "管理员"),
    USER("user", "普通用户");

    @EnumValue
    private final String code;
    private final String desc;
}
