package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.mapper.VideoMapper;
import cn.akfang.berry.service.VideoService;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, VideoPO> implements VideoService {
    @Override
    public Boolean isOwner(Long userId, Long videoId) {
        return new LambdaQueryChainWrapper<>(baseMapper)
                .eq(VideoPO::getAuthorId, userId)
                .eq(VideoPO::getId, videoId)
                .exists();
    }

//    @Override
//    public IPage<VideoVO> selectVideoVOPage(IPage<VideoVO> page, Long userId, Long channelId) {
//        QueryWrapper<VideoVO> qw = new QueryWrapper<>();
//        qw.eq("userId", userId);
//        qw.eq("channelId", channelId);
//        return baseMapper.selectVideoVOPage(page, );
//    }
}
