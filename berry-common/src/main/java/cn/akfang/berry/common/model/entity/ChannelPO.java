package cn.akfang.berry.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 频道表
 *
 * @TableName t_channel
 */
@TableName(value = "t_channel")
@Data
public class ChannelPO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 频道名称
     */
    private String label;

    /**
     * 图标
     */
    private String icon;

    /**
     * 总数
     */
    private Long totalCount;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}