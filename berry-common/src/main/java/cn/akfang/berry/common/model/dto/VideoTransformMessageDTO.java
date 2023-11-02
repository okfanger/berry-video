package cn.akfang.berry.common.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoTransformMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sourceKey;
    private Long fileId;
    private String cover;
    private String defaultKey;
}
