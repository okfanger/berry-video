package cn.akfang.berry.mapper;


import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.VideoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface VideoMapper extends BaseMapper<VideoPO> {
    Page<VideoVO> selectVideoVOPage(Long channelId, Long authorId, Page<VideoVO> page);

    List<VideoVO> selectVideoVOByChannelId(Long channelId);

    List<VideoPO> selectVideoPOByChannelId(Long channelId);

    List<Long> getRandomIds(int i);
//    IPage<VideoVO> selectVideoVOPage(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<VideoVO> queryWrapper);

}