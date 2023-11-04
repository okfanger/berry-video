package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.constants.GlobalConstants;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.mapper.VideoMapper;
import cn.akfang.berry.service.*;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, VideoPO> implements VideoService {

    @Autowired
    UserVideoLikeService userVideoLikeService;
    @Autowired
    UserVideoFavorService userVideoFavorService;

    @Autowired
    CommentService commentService;

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
        videoVO.setLikeCount(userVideoLikeService.getVideoLikedCountFromRedis(videoVO.getId()));
        videoVO.setLiked(userVideoLikeService.isLiked(currentUserId, videoVO.getId()));
        videoVO.setChannelId(channelService.getByVideoId(videoVO.getId()).getId());
        videoVO.setFavorCount(userVideoFavorService.getVideoFavorCountFromRedis(videoVO.getId()));
        videoVO.setCommentCount(commentService.getCommentCountByVideoId(videoVO.getId()));
        videoVO.setFavored(userVideoFavorService.isFavored(currentUserId, videoVO.getId()));
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
