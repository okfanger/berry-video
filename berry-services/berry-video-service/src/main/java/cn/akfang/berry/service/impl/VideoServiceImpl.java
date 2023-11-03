package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.constants.GlobalConstants;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.mapper.VideoMapper;
import cn.akfang.berry.service.ChannelService;
import cn.akfang.berry.service.LikeRedisService;
import cn.akfang.berry.service.VideoService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, VideoPO> implements VideoService {

    @Qualifier("videoLikeRedisService")
    @Autowired
    LikeRedisService<Long, Long> likeRedisService;


    @Qualifier("favorRedisService")
    @Autowired
    LikeRedisService<Long, Long> favorRedisService;

    @Qualifier("commentLikeRedisService")

    @Autowired
    LikeRedisService<Long, Long> commentRedisService;

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
    public VideoVO buildVideoVO(VideoPO item, UserBaseVO userBaseVO, Long currentUserId) {
        VideoVO videoVO = new VideoVO();
        BeanUtil.copyProperties(item, videoVO);
        videoVO.setLikeCount(likeRedisService.getLikedCount(videoVO.getId()));
        videoVO.setLiked(likeRedisService.isLiked(currentUserId, videoVO.getId()));
        videoVO.setChannelId(channelService.getByVideoId(videoVO.getId()).getId());
        videoVO.setFavorCount(favorRedisService.getLikedCount(videoVO.getId()));
        videoVO.setCommentCount(commentRedisService.getLikedCount(videoVO.getId()));
        videoVO.setFavored(favorRedisService.isLiked(currentUserId, videoVO.getId()));
        videoVO.setCover(GlobalConstants.OSS_URL + "/" + item.getCover());
        videoVO.setUrl(GlobalConstants.OSS_URL + "/" + item.getDefaultUrl());
        videoVO.setAuthor(userBaseVO);
        return videoVO;
    }

    @Override
    public Page<VideoVO> selectVideoVOPageByAuthorId(Long fromUserId, UserBaseVO toUser, Page<VideoVO> page) {
        LambdaQueryWrapper<VideoPO> qw = new LambdaQueryWrapper<>();
        qw.eq(VideoPO::getAuthorId, toUser.getAuthorId());
        Page<VideoVO> videoVOPage = new Page<>();
        BeanUtil.copyProperties(page, videoVOPage);
        Page<VideoPO> videoPOPage = baseMapper.selectPage(new Page<>(page.getCurrent(), page.getSize()), qw);
        videoVOPage.setRecords(videoPOPage.getRecords().stream()
                .map(item -> buildVideoVO(
                        item, toUser, fromUserId)
                )
                .collect(Collectors.toList()));
        return videoVOPage;
    }

    @Override
    public List<VideoPO> selectVideoPOByChannelId(Long channelId) {
        return baseMapper.selectVideoPOByChannelId(channelId);
    }

    @Override
    public List<VideoVO> selectVideoVOByChannelId(Long channelId) {
        return baseMapper.selectVideoVOByChannelId(channelId);
    }

    @Override
    public Page<VideoVO> selectVideoVOPage(Long channelId, Long authorId, Page<VideoVO> page) {
        return baseMapper.selectVideoVOPage(channelId, authorId, page);
    }
}
