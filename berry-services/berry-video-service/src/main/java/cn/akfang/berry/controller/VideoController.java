package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.enums.FeedTypeEnum;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.MiscClient;
import cn.akfang.berry.common.feign.client.VideoClient;
import cn.akfang.berry.common.model.dto.FeedPage;
import cn.akfang.berry.common.model.dto.VideoSaveDTO;
import cn.akfang.berry.common.model.entity.FilePO;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.constant.VideoMessageConstants;
import cn.akfang.berry.service.ChannelService;
import cn.akfang.berry.service.LikeRedisService;
import cn.akfang.berry.service.VideoService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class VideoController implements VideoClient {

    @Autowired
    MiscClient miscClient;

    @Autowired
    VideoService videoService;

    @Autowired
    ChannelService channelService;

    @Qualifier("videoLikeRedisService")
    @Autowired
    LikeRedisService<Long, Long> likeRedisService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/feed")
    public BaseResponse<FeedPage<VideoVO>> getFeedListByCurrentUser(
            @RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
            @RequestParam(value = "type", required = false) String type) {
        FeedTypeEnum feedTypeEnum = Optional.ofNullable(type)
                .filter(StringUtils::hasText)
                .map(FeedTypeEnum::valueOf)
                .orElse(FeedTypeEnum.PRODUCE);

        // fake data
        long offset = 5;
        List<VideoVO> collect = videoService.list()
                .stream()
                .filter((item) -> ObjectUtil.equal(item.getVisible(), 1))
                .limit(offset)
                .map(item -> {
                    VideoVO videoVO = new VideoVO();
                    BeanUtil.copyProperties(item, videoVO);
                    videoVO.setLikeCount(likeRedisService.getLikedCount(videoVO.getId()));
                    videoVO.setLiked(likeRedisService.isLiked(NumberUtil.parseLong(userIdStr), videoVO.getId()));
                    videoVO.setChannelId(channelService.getByVideoId(videoVO.getId()).getId());
                    videoVO.setCover("http://berry-cdn.akfang.cn/" + item.getCover());
                    videoVO.setUrl("http://berry-cdn.akfang.cn/" + item.getDefaultUrl());
//                    videoVO.setFile(miscClient.getFileById(item.getFileId()));
                    return videoVO;
                })
                .collect(Collectors.toList());
        FeedPage<VideoVO> videoPOFeedPage = new FeedPage<>();
        videoPOFeedPage.setRecords(collect);
        videoPOFeedPage.setLastId(offset);
        return ResultUtils.success(videoPOFeedPage);
    }

    @GetMapping("/doLike")
    public BaseResponse<Boolean> doLike(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                        @RequestParam("videoId") String videoIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        synchronized (userId.toString().intern()) {
            Long videoId = NumberUtil.parseLong(videoIdStr);
            if (!likeRedisService.isLiked(userId, videoId)) {
                likeRedisService.saveLiked2Redis(userId, videoId);
                likeRedisService.incrementLikedCount(videoId);
            }
            return ResultUtils.success(null);
        }
    }

    @GetMapping("/unLike")
    public BaseResponse<Boolean> doUnLike(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                          @RequestParam("videoId") String videoIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        synchronized (userId.toString().intern()) {
            Long videoId = NumberUtil.parseLong(videoIdStr);
            if (likeRedisService.isLiked(userId, videoId)) {
                likeRedisService.unlikeFromRedis(userId, videoId);
                likeRedisService.decrementLikedCount(videoId);
            }
            return ResultUtils.success(null);
        }
    }

    @PostMapping("/publish")
    public BaseResponse<Boolean> publishVideo(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId, @RequestBody VideoSaveDTO dto) {
        FilePO fileById = miscClient.getFileById(dto.getFileId());
        if (ObjectUtil.isNull(fileById)) {
            log.error("publishVideo: fileById is null");
            throw new BerryRpcException(ErrorCode.PARAMS_ERROR, "filePO not exists.");
        } else {
            dto.setAuthorId(Long.parseLong(userId));
            Boolean o = (Boolean) rabbitTemplate.convertSendAndReceive(VideoMessageConstants.VIDEO_EXCHANGE,
                    VideoMessageConstants.VIDEO_SAVE_ROUTING_KEY, dto);
            if (ObjectUtil.isNull(o)) {
                throw new BerryRpcException(ErrorCode.SYSTEM_ERROR, "publish video failed.");
            }
            return ResultUtils.success(o);
        }
    }

    @DeleteMapping("/{videoId}")
    public BaseResponse<Boolean> deleteVideo(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId,
                                             @PathVariable("videoId") String videoIdStr) {
        // 判断是否有权限
        Long videoId = NumberUtil.parseLong(videoIdStr);
        if (!videoService.isOwner(Long.parseLong(userId), videoId)) {
            throw new BerryRpcException(ErrorCode.NO_AUTH_ERROR, "no auth to delete video.");
        } else {
            return ResultUtils.success(videoService.removeById(videoId));
        }
    }
}
