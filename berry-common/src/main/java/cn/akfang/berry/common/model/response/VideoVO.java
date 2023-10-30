package cn.akfang.berry.common.model.response;

import cn.akfang.berry.common.model.entity.FilePO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoVO implements Serializable {
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
     * 转码后地址
     */
    private String url;

    /**
     * 视频封面
     */
    private String cover;

    /**
     * 视频标题，可以为空
     */
    private String title;

    /**
     * 点赞总数
     */
    private Integer likeCount;

    /**
     * 评论总数
     */
    private Integer commentCount;

    /**
     * 是否私密，用户可以设置私密，如此可以不公开给比人看
     */
    private Integer visible;

    /**
     * 创建时间
     */
    private Date createTime;

    private boolean liked;

    private Long channelId;

    /**
     * 文件地址
     */
    private FilePO file;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}