package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.MiscClient;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.feign.client.VideoClient;
import cn.akfang.berry.common.model.dto.FeedPage;
import cn.akfang.berry.common.model.dto.VideoSaveDTO;
import cn.akfang.berry.common.model.entity.FilePO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.constant.VideoMessageConstants;
import cn.akfang.berry.service.ChannelService;
import cn.akfang.berry.service.LikeRedisService;
import cn.akfang.berry.service.VideoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    @Qualifier("favorRedisService")
    @Autowired
    LikeRedisService<Long, Long> favorRedisService;

    @Qualifier("commentLikeRedisService")
    @Autowired
    LikeRedisService<Long, Long> commentRedisService;

    @Autowired
    UserClient userClient;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/my")
    public BaseResponse<Page<VideoVO>> getPersonalVideo(
            @RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
            @RequestParam(value = "current", defaultValue = "1") String pageStr,
            @RequestParam(value = "channelId", required = false) String channelIdStr
    ) {
        Long userId = NumberUtil.parseLong(userIdStr);
        int currentPage = NumberUtil.parseInt(pageStr);

        UserBaseVO userBaseVO = userClient.getUserBaseVOById(userId);

        Page<VideoVO> videoVOPage = videoService.selectVideoVOPageByAuthorId(userId, userBaseVO, new Page<>(currentPage, 10));
        return ResultUtils.success(videoVOPage);
    }

    @GetMapping("/my/{type}")
    public BaseResponse<FeedPage<VideoVO>> getFeedListByType(
            @RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
            @RequestParam(value = "channelId", required = false) String channelIdStr,
            @PathVariable(value = "type", required = false) String typeStr
    ) {
        return getFeedListByCurrentUser(userIdStr, channelIdStr);
    }


    @GetMapping("/feed")
    public BaseResponse<FeedPage<VideoVO>> getFeedListByCurrentUser(
            @RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
            @RequestParam(value = "channelId", required = false) String channelIdStr
    ) {
        Long currentUserId = NumberUtil.parseLong(userIdStr);
        QueryWrapper<VideoPO> qw = new QueryWrapper<>();
        qw.orderByDesc("createTime");
        if (StrUtil.isNotBlank(channelIdStr)) {
            Long channelId = NumberUtil.parseLong(channelIdStr);
//            qw.eq("channelId", channelId);
            List<VideoPO> videoVOS = videoService.selectVideoPOByChannelId(channelId);
            List<VideoVO> collect = videoVOS.stream()
                    .filter((item) -> ObjectUtil.equal(item.getVisible(), 1))
                    .map(item -> videoService.buildVideoVO(item, userClient.getUserBaseVOById(item.getAuthorId()), currentUserId))
                    .collect(Collectors.toList());
            FeedPage<VideoVO> videoPOFeedPage = new FeedPage<>();
            videoPOFeedPage.setRecords(collect);
            return ResultUtils.success(videoPOFeedPage);
        } else {

            List<VideoVO> collect = videoService.getBaseMapper().selectList(qw)
                    .stream()
                    .filter((item) -> ObjectUtil.equal(item.getVisible(), 1))
                    .map(item -> videoService.buildVideoVO(item, userClient.getUserBaseVOById(item.getAuthorId()), currentUserId))
                    .collect(Collectors.toList());
            FeedPage<VideoVO> videoPOFeedPage = new FeedPage<>();
            videoPOFeedPage.setRecords(collect);
            return ResultUtils.success(videoPOFeedPage);
        }
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

    @GetMapping("/doFavor")
    public BaseResponse<Boolean> doFavor(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                         @RequestParam("videoId") String videoIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        synchronized (userId.toString().intern()) {
            Long videoId = NumberUtil.parseLong(videoIdStr);
            if (!favorRedisService.isLiked(userId, videoId)) {
                favorRedisService.saveLiked2Redis(userId, videoId);
                favorRedisService.incrementLikedCount(videoId);
            }
            return ResultUtils.success(null);
        }
    }

    @GetMapping("/unFavor")
    public BaseResponse<Boolean> doUnFavor(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                          @RequestParam("videoId") String videoIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        synchronized (userId.toString().intern()) {
            Long videoId = NumberUtil.parseLong(videoIdStr);
            if (favorRedisService.isLiked(userId, videoId)) {
                favorRedisService.unlikeFromRedis(userId, videoId);
                favorRedisService.decrementLikedCount(videoId);
            }
            return ResultUtils.success(null);
        }
    }

    @PostMapping("/publish")
    public BaseResponse<Boolean> publishVideo(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId,
                                              @RequestBody VideoSaveDTO dto) {
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

    @PostMapping("/recycle")
    public BaseResponse<Boolean> deleteVideo(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId,
                                             @RequestParam("videoId") String videoIdStr) {
        // 判断是否有权限
        Long videoId = NumberUtil.parseLong(videoIdStr);
        if (!videoService.isOwner(Long.parseLong(userId), videoId)) {
            throw new BerryRpcException(ErrorCode.NO_AUTH_ERROR, "no auth to delete video.");
        } else {
            return ResultUtils.success(videoService.removeById(videoId));
        }
    }

    @GetMapping("/search/hit")
    public BaseResponse<Map<String, List<String>>> searchHit(@RequestParam("keyword") String keyword) {
        List<String> preStrs = Arrays.asList(
                "是什么意思",
                "能吃吗",
                "怎么做"
        );
        return ResultUtils.success(MapUtil.builder("hits", preStrs.stream().map((item) -> {
            return keyword + item;
        }).collect(Collectors.toList())).build());
    }


    @Override
    public List<VideoPO> listAll() {
        return videoService.list();
    }

    @Override
    public List<VideoVO> getVOByIds(List<Long> ids, Long currentId) {
        if (CollectionUtil.isEmpty(ids)) {
            return CollectionUtil.newArrayList();
        } else {
            return videoService.listByIds(ids).stream()
                    .map(item -> videoService.buildVideoVO(item,
                            userClient.getUserBaseVOById(item.getAuthorId()), currentId)).collect(Collectors.toList());
        }
    }

    @Override
    public List<Long> listIdsMinutesAgo(Long minuteNum) {
        LambdaQueryWrapper<VideoPO> qw = new LambdaQueryWrapper<>();
        // 查找更新时间在2分钟前的视频
        qw.select(VideoPO::getId)
                .geSql(VideoPO::getUpdateTime, "DATE_SUB(NOW(), INTERVAL " + minuteNum + " MINUTE)");
        return videoService.list(qw).stream().map(VideoPO::getId).collect(Collectors.toList());
    }

    @Override
    public List<VideoPO> listByIds(List<Long> ids) {
        return videoService.listByIds(ids);
    }

    @Override
    public List<Long> listAllIds() {
        QueryWrapper<VideoPO> qw = new QueryWrapper<>();
        qw.select("id");
        return videoService.list(qw)
                .stream().map(VideoPO::getId).collect(Collectors.toList());
    }
}
