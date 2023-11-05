package cn.akfang.berry.common.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoActionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long videoId;
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;
    private Integer favorCount;
    private Boolean favored;
    private Boolean liked;
}
