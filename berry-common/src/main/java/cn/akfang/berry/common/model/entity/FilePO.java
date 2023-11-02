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
 * @TableName t_file
 */
@TableName(value = "t_file")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilePO implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     *
     */
    @TableField(value = "`key`")
    private String key;

    /**
     *
     */
    @TableField(value = "`hash`")
    private String hash;
    /**
     *
     */
    private String bucket;

    /**
     *
     */
    private String fsize;

    /**
     *
     */
//    @TableField(value = "metadata",
//            typeHandler = JacksonTypeHandler.class)
//    private Map<String, String> metadata;
    private String metadata;

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