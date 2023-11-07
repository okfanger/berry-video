package cn.akfang.berry.common.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserActionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long fromUserId;
    private Long toUserId;
    private Integer followCount;
    private Integer fansCount;
    private Boolean followed;
    private Boolean friend;
}
