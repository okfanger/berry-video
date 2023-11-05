package cn.akfang.berry.service;

import cn.akfang.berry.common.model.entity.ChannelPO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author fang
 * @description 针对表【t_channel(频道表)】的数据库操作Service
 * @createDate 2023-10-30 20:58:48
 */
public interface ChannelService extends IService<ChannelPO> {
    ChannelPO getByVideoId(Long videoId);
}
