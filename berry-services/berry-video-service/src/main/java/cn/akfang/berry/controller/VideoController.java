package cn.akfang.berry.controller;


import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.enums.FeedTypeEnum;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.MiscClient;
import cn.akfang.berry.common.feign.client.VideoClient;
import cn.akfang.berry.common.model.dto.FeedPage;
import cn.akfang.berry.common.model.dto.VideoSaveDTO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.LikeRedisService;
import cn.akfang.berry.service.VideoService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
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

    @Qualifier("videoLikeRedisService")
    @Autowired
    LikeRedisService<Long, Long> likeRedisService;

    @GetMapping("/debug")
    public BaseResponse<String> debug() {
        miscClient.uploadFile(miscClient.getUploadToken().getUpToken());
        return ResultUtils.success(null);
    }

    @GetMapping("/feed")
    public BaseResponse<FeedPage<VideoVO>> getFeedListByCurrentUser(
            @RequestParam(value = "type", required = false) String type) {

        Long currentUserId = 1717564636153798658L;

        FeedTypeEnum feedTypeEnum = Optional.ofNullable(type)
                .filter(StringUtils::hasText)
                .map(FeedTypeEnum::valueOf)
                .orElse(FeedTypeEnum.PRODUCE);

        // fake data
        long offset = 5;
        List<VideoVO> collect = videoService.list()
                .stream()
                .limit(offset)
                .map(item -> {
                    VideoVO videoVO = new VideoVO();
                    BeanUtil.copyProperties(item, videoVO);
                    videoVO.setUrl("http://berry-cdn.akfang.cn/" + item.getM3u8Key());
                    videoVO.setLikeCount(likeRedisService.getLikedCount(videoVO.getId()));
                    videoVO.setLiked(likeRedisService.isLiked(currentUserId, videoVO.getId()));
                    return videoVO;
                })
                .collect(Collectors.toList());
        FeedPage<VideoVO> videoPOFeedPage = new FeedPage<>();
        videoPOFeedPage.setRecords(collect);
        videoPOFeedPage.setLastId(offset);
        return ResultUtils.success(videoPOFeedPage);
    }

    @GetMapping("/like")
    public BaseResponse<Boolean> doLike(@RequestParam("videoId") String videoIdStr) {
        Long userId = 1717564636153798658L;
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
    public BaseResponse<Boolean> doUnLike(@RequestParam("videoId") String videoIdStr) {
        Long userId = 1717564636153798658L;
        synchronized (userId.toString().intern()) {
            Long videoId = NumberUtil.parseLong(videoIdStr);
            if (likeRedisService.isLiked(userId, videoId)) {
                likeRedisService.unlikeFromRedis(userId, videoId);
                likeRedisService.decrementLikedCount(videoId);
            }
            return ResultUtils.success(null);
        }
    }
    @PostMapping("/")
    public BaseResponse<Boolean> saveVideo(@RequestBody VideoSaveDTO dto) {
        Optional<VideoPO> videoPO = new LambdaQueryChainWrapper<>(videoService.getBaseMapper())
                .eq(VideoPO::getSourceKey, dto.getKey())
                .oneOpt();

        if (videoPO.isPresent()) {
            log.info("saveVideo: videoPO is present");
            VideoPO videoSelf = videoPO.get();
            videoSelf.setTitle(dto.getTitle());
            videoSelf.setVisible(dto.getVisible());
            return ResultUtils.success(videoService.updateById(videoSelf));
        } else {
            log.error("saveVideo: videoPO is not present");
            throw new BerryRpcException(ErrorCode.QINIU_UPLOAD_ERROR);
        }
    }

    @Override
    public boolean saveVideo(VideoPO dto) {
        return videoService.save(dto);
    }
}
