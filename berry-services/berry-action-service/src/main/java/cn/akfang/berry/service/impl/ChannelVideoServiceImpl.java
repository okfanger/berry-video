package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.model.entity.ChannelVideoPO;
import cn.akfang.berry.mapper.ChannelVideoMapper;
import cn.akfang.berry.service.ChannelVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author fang
 * @description 针对表【t_channel_video(视频-频道关联表)】的数据库操作Service实现
 * @createDate 2023-10-30 20:58:48
 */
@Service
public class ChannelVideoServiceImpl extends ServiceImpl<ChannelVideoMapper, ChannelVideoPO>
        implements ChannelVideoService {
}




