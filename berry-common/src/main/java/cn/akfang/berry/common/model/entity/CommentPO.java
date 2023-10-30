package cn.akfang.berry.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论表
 *
 * @TableName t_comment
 */
@TableName(value = "t_comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentPO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 评论的视频是哪个作者的关联id
     */
    private Long authorId;

    /**
     * 如果是回复留言，则本条为子留言，需要关联查询
     */
    private Long fatherCommentId;

    /**
     * 回复的那个视频id
     */
    private Long videoId;

    /**
     * 发布留言的用户id
     */
    private Long commentUserId;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 留言的点赞总数
     */
    private Integer likeCounts;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}