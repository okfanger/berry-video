package cn.akfang.berry.consumer;


import cn.akfang.berry.common.enums.FeedTypeEnum;
import cn.akfang.berry.common.model.dto.FeedPage;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.request.ClientUploadTokenRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.FileUploadToken;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.MediaService;
import cn.akfang.berry.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class VideoConsumer {
    @DubboReference
    MediaService mediaService;

    @DubboReference
    VideoService videoService;

    /**
     * 客户端上传
     *
     * @param request uuid
     * @return 上传凭证 upToken
     */
    @PostMapping("/upload/token")
    public BaseResponse<FileUploadToken> clientUploadToken(@Valid @RequestBody ClientUploadTokenRequest request) {
        synchronized (request.getUuid().intern()) {
            return ResultUtils.success(mediaService.getUploadToken());
        }
    }

    @GetMapping("/debug")
    public BaseResponse<String> debug() {
        mediaService.uploadFile(mediaService.getUploadToken().getUpToken());
        return ResultUtils.success(null);
    }

    @GetMapping("/feed")
    public BaseResponse<FeedPage<VideoPO>> getFeedListByCurrentUser(
            @RequestParam(value = "type", required = false) String type) {

        FeedTypeEnum feedTypeEnum = Optional.ofNullable(type)
                .filter(StringUtils::hasText)
                .map(FeedTypeEnum::valueOf)
                .orElse(FeedTypeEnum.PRODUCE);

        // fake data
        long offset = 5;
        List<VideoPO> collect = videoService.list()
                .stream()
                .limit(offset)
                .collect(Collectors.toList());
        FeedPage<VideoPO> videoPOFeedPage = new FeedPage<>();
        videoPOFeedPage.setRecords(collect);
        videoPOFeedPage.setLastId(offset);
        return ResultUtils.success(videoPOFeedPage);
    }
}
