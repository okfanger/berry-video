package cn.akfang.berry.common.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class FollowActionDTO extends ActionDTO<Long, Long> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean friend;
}
