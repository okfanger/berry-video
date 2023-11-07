package cn.akfang.berry.common.model.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class VideoEsSearchDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String esIdx;
    private Long videoId;
}
