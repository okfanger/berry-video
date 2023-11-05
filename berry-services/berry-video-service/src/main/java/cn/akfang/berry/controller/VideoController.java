package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.ActionClient;
import cn.akfang.berry.common.feign.client.MiscClient;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.feign.client.VideoClient;
import cn.akfang.berry.common.model.base.Pair;
import cn.akfang.berry.common.model.dto.FeedPage;
import cn.akfang.berry.common.model.dto.VideoActionDTO;
import cn.akfang.berry.common.model.dto.VideoSaveDTO;
import cn.akfang.berry.common.model.entity.FilePO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.request.VideoActionInfoRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.constant.VideoMessageConstants;
import cn.akfang.berry.service.ChannelService;
import cn.akfang.berry.service.VideoService;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class VideoController implements VideoClient {

    @Autowired
    ActionClient actionClient;

    @Autowired
    MiscClient miscClient;

    @Autowired
    VideoService videoService;

    @Autowired
    ChannelService channelService;

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

        Page<VideoPO> page = videoService.page(new Page<>(currentPage, 10), new LambdaQueryWrapper<VideoPO>()
                .eq(VideoPO::getAuthorId, userId)
                .orderByDesc(VideoPO::getCreateTime));

        List<Long> videoIds = page.getRecords().stream().map(VideoPO::getId).collect(Collectors.toList());
        List<Long> authorIds = page.getRecords().stream().map(VideoPO::getAuthorId).collect(Collectors.toList());
        List<Pair<Long, Long>> pairs = page.getRecords().stream().map(item -> {
            Pair<Long, Long> pair = new Pair<>();
            pair.setA(item.getId());
            pair.setB(item.getAuthorId());
            return pair;
        }).collect(Collectors.toList());

        VideoActionInfoRequest videoActionInfoRequest = new VideoActionInfoRequest();
        videoActionInfoRequest.setVideoIds(videoIds);
        videoActionInfoRequest.setUserId(userId);

        Map<Long, VideoActionDTO> videoActionInfoByIds = actionClient.getVideoActionInfoByIds(videoActionInfoRequest);
        Map<Long, UserBaseVO> userBaseVOMap = userClient.getUserBaseVOByIds(pairs, userIdStr);

        Page<VideoVO> videoVOPage = new Page<>();

        videoVOPage.setTotal(page.getTotal());
        videoVOPage.setCurrent(page.getCurrent());
        videoVOPage.setSize(page.getSize());
        videoVOPage.setPages(page.getPages());
        videoVOPage.setRecords(videoService.buildVideoVO(page.getRecords(), videoActionInfoByIds, userBaseVOMap));

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
        if (StrUtil.isNotBlank(channelIdStr)) {
            Long channelId = NumberUtil.parseLong(channelIdStr);
            List<VideoPO> videoVOS = videoService.selectVideoPOByChannelId(channelId);
            List<Pair<Long, Long>> ids = videoVOS.stream().map(item -> {
                Pair<Long, Long> pair = new Pair<>();
                pair.setA(item.getId());
                pair.setB(item.getAuthorId());
                return pair;
            }).collect(Collectors.toList());
            VideoActionInfoRequest videoActionInfoRequest = new VideoActionInfoRequest();
            videoActionInfoRequest.setVideoIds(videoVOS.stream().map(VideoPO::getId).collect(Collectors.toList()));
            videoActionInfoRequest.setUserId(currentUserId);

            Map<Long, VideoActionDTO> videoActionInfoByIds = actionClient.getVideoActionInfoByIds(videoActionInfoRequest);
            Map<Long, UserBaseVO> userBaseVOByIds = userClient.getUserBaseVOByIds(ids, userIdStr);
            List<VideoVO> videoVOS1 = videoService.buildVideoVO(videoVOS, videoActionInfoByIds, userBaseVOByIds);
            FeedPage<VideoVO> videoPOFeedPage = new FeedPage<>();
            videoPOFeedPage.setRecords(videoVOS1);
            return ResultUtils.success(videoPOFeedPage);
        } else {
            List<VideoPO> videoVOS = videoService.list();
            List<Pair<Long, Long>> ids = videoVOS.stream().map(item -> {
                Pair<Long, Long> pair = new Pair<>();
                pair.setA(item.getId());
                pair.setB(item.getAuthorId());
                return pair;
            }).collect(Collectors.toList());
            VideoActionInfoRequest videoActionInfoRequest = new VideoActionInfoRequest();
            videoActionInfoRequest.setVideoIds(videoVOS.stream().map(VideoPO::getId).collect(Collectors.toList()));
            videoActionInfoRequest.setUserId(currentUserId);

            Map<Long, VideoActionDTO> videoActionInfoByIds = actionClient.getVideoActionInfoByIds(videoActionInfoRequest);
            Map<Long, UserBaseVO> userBaseVOByIds = userClient.getUserBaseVOByIds(ids, userIdStr);
            List<VideoVO> videoVOS1 = videoService.buildVideoVO(videoVOS, videoActionInfoByIds, userBaseVOByIds);
            FeedPage<VideoVO> videoPOFeedPage = new FeedPage<>();
            videoPOFeedPage.setRecords(videoVOS1);
            return ResultUtils.success(videoPOFeedPage);
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

    @Override
    public List<VideoPO> listAll() {
        return videoService.list();
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
