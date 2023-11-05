package cn.akfang.berry.common.model.dto.qianfan.chat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageSendDTO {
    private String role;
    private String content;
}
