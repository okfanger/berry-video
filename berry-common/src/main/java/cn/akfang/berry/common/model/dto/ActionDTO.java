package cn.akfang.berry.common.model.dto;


import cn.akfang.berry.common.enums.LikeStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionDTO<F, T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private F fromId;
    private T toId;
    private Integer status = LikeStatusEnum.UNLIKE.getCode();
    private Date createTime;
    private Date updateTime;

    public ActionDTO(F fromId, T toId, Integer status) {
        this.fromId = fromId;
        this.toId = toId;
        this.status = status;
    }
}