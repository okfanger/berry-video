package cn.akfang.berry.controller;


import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.enums.FeedTypeEnum;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.MiscService;
import cn.akfang.berry.common.model.dto.FeedPage;
import cn.akfang.berry.common.model.dto.VideoSaveDTO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.request.ClientUploadTokenRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.FileUploadToken;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class VideoConsumer {

    @Autowired
    MiscService miscService;

    @Qualifier("videoProvider")
    @Autowired
    VideoProvider videoService;

    /**
     * 客户端上传
     *
     * @param request uuid
     * @return 上传凭证 upToken
     */
    @PostMapping("/upload/token")
    public BaseResponse<FileUploadToken> clientUploadToken(@Valid @RequestBody ClientUploadTokenRequest request) {
        synchronized (request.getUuid().intern()) {
            return ResultUtils.success(miscService.getUploadToken());
        }
    }

    @GetMapping("/debug")
    public BaseResponse<String> debug() {
        miscService.uploadFile(miscService.getUploadToken().getUpToken());
        return ResultUtils.success(null);
    }

    @GetMapping("/feed")
    public BaseResponse<FeedPage<VideoVO>> getFeedListByCurrentUser(
            @RequestParam(value = "type", required = false) String type) {

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
                    return videoVO;
                })
                .collect(Collectors.toList());
        FeedPage<VideoVO> videoPOFeedPage = new FeedPage<>();
        videoPOFeedPage.setRecords(collect);
        videoPOFeedPage.setLastId(offset);
        return ResultUtils.success(videoPOFeedPage);
    }

    @GetMapping("/like")
    public BaseResponse<String> doLike(@RequestParam("videoId") Long videoId) {
        return ResultUtils.success(videoService.like(videoId));
    }

    @PostMapping("/")
    public BaseResponse<Boolean> saveVideo(@RequestBody VideoSaveDTO dto) {
        Optional<VideoPO> videoPO = new LambdaQueryChainWrapper<VideoPO>(videoService.getBaseMapper())
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
}
