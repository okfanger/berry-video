package cn.akfang.berry.common.model.dto;


import cn.akfang.berry.common.enums.LikeStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLikeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String fromId;
    private String toId;
    private Integer status = LikeStatusEnum.UNLIKE.getCode();

    public UserLikeDTO(String fromId, String toId, Integer status) {
        this.fromId = fromId;
        this.toId = toId;
        this.status = status;
    }
}