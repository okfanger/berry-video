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
 * 短视频表
 *
 * @TableName t_video
 */
@TableName(value = "t_video")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoPO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 对应用户表id，视频发布者
     */
    private Long authorId;

    /**
     * 视频封面
     */
    private String cover;

    /**
     * 默认播放地址
     */
    private String defaultUrl;

    /**
     * 文件地址
     */
    private Long fileId;

    /**
     * 标签
     */
    private String tags;

    /**
     * 视频标题
     */
    private String content;


    /**
     * 点赞总数
     */
    private Integer likeCount;

    /**
     * 评论总数
     */
    private Integer commentCount;

    /**
     * 分享总数
     */
    private Integer shareCount;

    /**
     * 收藏总数
     */
    private Integer favorCount;

    /**
     * 是否私密，用户可以设置私密，如此可以不公开给比人看
     * feat: 2表示转码中
     */
    private Integer visible;

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