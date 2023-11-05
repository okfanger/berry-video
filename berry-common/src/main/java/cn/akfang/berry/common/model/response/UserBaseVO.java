package cn.akfang.berry.common.model.response;

import cn.akfang.berry.common.model.dto.UserActionDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserBaseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long authorId;
    private String authorNickName;
    private String authorAvatar;
    private Integer gender;
    private UserActionDTO action;
}