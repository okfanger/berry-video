package cn.akfang.berry.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikedCountDTO implements Serializable {
    private String key;
    private Integer count;
}
