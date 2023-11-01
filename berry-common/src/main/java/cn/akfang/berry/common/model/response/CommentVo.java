package cn.akfang.berry.common.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CommentVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private UserBaseVO author;
    private String content;
    private Date createTime;
    private Integer likeCount;
    private Boolean isLiked;
}
