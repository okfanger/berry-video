package cn.akfang.berry.common.model.dto.qianfan.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageSendRoleEnum {
    USER("user", "用户"),
    ASSISTANT("assistant", "助理");
    private final String code;
    private final String desc;
}
