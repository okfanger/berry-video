package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.model.entity.ChannelPO;
import cn.akfang.berry.mapper.ChannelMapper;
import cn.akfang.berry.service.ChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author fang
 * @description 针对表【t_channel(频道表)】的数据库操作Service实现
 * @createDate 2023-10-30 20:58:48
 */
@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, ChannelPO>
        implements ChannelService {
    @Override
    public ChannelPO getByVideoId(Long videoId) {
        return baseMapper.getByVideoId(videoId);
    }
}




