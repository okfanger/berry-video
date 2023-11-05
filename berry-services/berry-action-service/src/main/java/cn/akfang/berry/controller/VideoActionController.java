package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.feign.client.ActionClient;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.dto.VideoActionDTO;
import cn.akfang.berry.common.model.request.VideoActionInfoRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.UserCommentLikeService;
import cn.akfang.berry.service.UserVideoFavorService;
import cn.akfang.berry.service.UserVideoLikeService;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController("/video")
public class VideoActionController implements ActionClient {

    @Autowired
    UserClient userClient;

    @Autowired
    UserVideoLikeService userVideoLikeService;

    @Autowired
    UserVideoFavorService userVideoFavorService;

    @Autowired
    UserCommentLikeService userCommentLikeService;

    @GetMapping("/doLike")
    public BaseResponse<Boolean> doLike(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                        @RequestParam("videoId") String videoIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        Long videoId = NumberUtil.parseLong(videoIdStr);
        userVideoLikeService.doLike(userId, videoId);
        return ResultUtils.success(null);
    }

    @GetMapping("/unLike")
    public BaseResponse<Boolean> doUnLike(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                          @RequestParam("videoId") String videoIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        Long videoId = NumberUtil.parseLong(videoIdStr);
        userVideoLikeService.unLike(userId, videoId);
        return ResultUtils.success(null);
    }

    @GetMapping("/doFavor")
    public BaseResponse<Boolean> doFavor(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                         @RequestParam("videoId") String videoIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        Long videoId = NumberUtil.parseLong(videoIdStr);
        userVideoFavorService.doFavor(userId, videoId);
        return ResultUtils.success(null);
    }

    @GetMapping("/unFavor")
    public BaseResponse<Boolean> doUnFavor(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                           @RequestParam("videoId") String videoIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        Long videoId = NumberUtil.parseLong(videoIdStr);
        userVideoFavorService.unFavor(userId, videoId);
        return ResultUtils.success(null);
    }

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
        System.out.println(collect);
        return collect;
    }
}
