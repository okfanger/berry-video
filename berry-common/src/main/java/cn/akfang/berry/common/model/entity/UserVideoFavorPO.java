package cn.akfang.berry.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 点赞短视频关联表
 *
 * @TableName t_user_video_favor
 */
@TableName(value = "t_user_video_favor")
@Data
public class UserVideoFavorPO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 喜欢的短视频id
     */
    private Long videoId;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     *
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}