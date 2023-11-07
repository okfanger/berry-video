package cn.akfang.berry.mapper;

import cn.akfang.berry.common.model.entity.ChannelPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author fang
 * @description 针对表【t_channel(频道表)】的数据库操作Mapper
 * @createDate 2023-10-30 20:58:48
 * @Entity cn.akfang.berry.common.model.entity.Channel
 */
public interface ChannelMapper extends BaseMapper<ChannelPO> {

    @Select("select * from t_channel where id = (select channelId from t_channel_video where videoId = #{videoId}) limit 1")
    ChannelPO getByVideoId(@Param("videoId") Long videoId);
}




