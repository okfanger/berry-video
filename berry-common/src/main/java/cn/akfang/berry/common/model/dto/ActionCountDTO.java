package cn.akfang.berry.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionCountDTO<E> implements Serializable {
    private static final long serialVersionUID = 1L;

    private E toId;
    private Integer count;
}
