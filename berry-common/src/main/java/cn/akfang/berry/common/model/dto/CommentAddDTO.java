package cn.akfang.berry.common.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommentAddDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private Long videoId;
}
