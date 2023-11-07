package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.constants.GlobalConstants;
import cn.akfang.berry.common.model.dto.VideoActionDTO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.mapper.VideoMapper;
import cn.akfang.berry.service.ChannelService;
import cn.akfang.berry.service.VideoService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, VideoPO> implements VideoService {
    @Autowired
    ChannelService channelService;

    @Override
    public Boolean isOwner(Long userId, Long videoId) {
        return new LambdaQueryChainWrapper<>(baseMapper)
                .eq(VideoPO::getAuthorId, userId)
                .eq(VideoPO::getId, videoId)
                .exists();
    }

    @Override
    public List<VideoPO> selectVideoPOByChannelId(Long channelId) {
        return baseMapper.selectVideoPOByChannelId(channelId);
    }

    @Override
    public List<VideoVO> buildVideoVO(List<VideoPO> sourceList, Map<Long, VideoActionDTO> actionInfo, Map<Long, UserBaseVO> authorInfo) {
        return sourceList.stream().map(item -> {
            VideoVO videoVO = new VideoVO();
            BeanUtil.copyProperties(item, videoVO);
            videoVO.setUrl(GlobalConstants.OSS_URL + "/" + item.getDefaultUrl());
            videoVO.setCover(GlobalConstants.OSS_URL + "/" + item.getCover());
            videoVO.setChannelId(channelService.getByVideoId(item.getId()).getId());


            videoVO.setLikeCount(actionInfo.get(item.getId()).getLikeCount());
            videoVO.setCommentCount(actionInfo.get(item.getId()).getCommentCount());
            videoVO.setFavorCount(actionInfo.get(item.getId()).getFavorCount());
            videoVO.setLiked(actionInfo.get(item.getId()).getLiked());
            videoVO.setFavored(actionInfo.get(item.getId()).getFavored());
            videoVO.setAuthor(authorInfo.get(item.getAuthorId()));
            return videoVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Long> getRandomIds(int i) {
        return baseMapper.getRandomIds(i);
    }
}
