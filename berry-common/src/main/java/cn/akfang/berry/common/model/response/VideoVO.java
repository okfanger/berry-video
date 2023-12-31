package cn.akfang.berry.common.model.response;

import cn.akfang.berry.common.model.dto.EsVideoMetaDTO;
import cn.akfang.berry.common.model.entity.FilePO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private Long authorId;

    private UserBaseVO author;

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
    private String content;

    /**
     * 点赞总数
     */
    private Integer likeCount;

    /**
     * 收藏总数
     */
    private Integer favorCount;

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

    private Boolean liked;

    private Boolean favored;

    private Long channelId;

    /**
     * 文件地址
     */
    @JsonIgnore
    private FilePO file;

    private EsVideoMetaDTO searchMeta;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}