package cn.akfang.berry.controller;

import cn.akfang.berry.common.feign.client.ActionClient;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.dto.UserActionDTO;
import cn.akfang.berry.common.model.dto.VideoActionDTO;
import cn.akfang.berry.common.model.request.VideoActionInfoRequest;
import cn.akfang.berry.service.FollowService;
import cn.akfang.berry.service.UserCommentLikeService;
import cn.akfang.berry.service.UserVideoFavorService;
import cn.akfang.berry.service.UserVideoLikeService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("")
public class ActionController implements ActionClient {
    @Autowired
    UserClient userClient;

    @Autowired
    UserVideoLikeService userVideoLikeService;

    @Autowired
    UserVideoFavorService userVideoFavorService;

    @Autowired
    UserCommentLikeService userCommentLikeService;

    @Autowired
    FollowService followService;

    @Override
    public Map<Long, VideoActionDTO> getVideoActionInfoByIds(VideoActionInfoRequest request) {
        List<Long> videoIds = request.getVideoIds();
        Map<Long, VideoActionDTO> collect = videoIds.parallelStream().map(videoId -> {
            VideoActionDTO videoActionDTO = new VideoActionDTO();
            videoActionDTO.setVideoId(videoId);
            videoActionDTO.setLikeCount(userVideoLikeService.getVideoLikedCountFromRedis(videoId));
            videoActionDTO.setFavorCount(userVideoFavorService.getVideoFavorCountFromRedis(videoId));
            videoActionDTO.setCommentCount(userCommentLikeService.getLikeCount(videoId));
            videoActionDTO.setLiked(userVideoLikeService.isLiked(request.getUserId(), videoId));
            videoActionDTO.setFavored(userVideoFavorService.isFavored(request.getUserId(), videoId));
            return videoActionDTO;
        }).collect(Collectors.toMap(VideoActionDTO::getVideoId, item -> item));
        return collect;
    }

    @Override
    public Map<Long, UserActionDTO> getUserActionInfoByIds(List<Long> ids, String currentUserIdStr) {
        Long currentUserId = NumberUtil.parseLong(currentUserIdStr);
        if (CollectionUtil.isEmpty(ids))
            return Collections.emptyMap();

        return ids.stream().distinct().map(userId -> {
            UserActionDTO userActionDTO = new UserActionDTO();
            userActionDTO.setFromUserId(currentUserId);
            userActionDTO.setToUserId(userId);
            userActionDTO.setFollowed(followService.isFollowed(currentUserId, userId));
            userActionDTO.setFriend(followService.isFriend(currentUserId, userId));
            // 以下没写反...命名问题嘿嘿
            userActionDTO.setFansCount(followService.getFollowCountFromRedis(userId));
            userActionDTO.setFollowCount(followService.getFansCountFromRedis(userId));
            return userActionDTO;
        }).collect(Collectors.toMap(UserActionDTO::getToUserId, item -> item));
    }
}
