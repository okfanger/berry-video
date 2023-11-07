package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.UserCommentLikeService;
import cn.akfang.berry.service.UserVideoFavorService;
import cn.akfang.berry.service.UserVideoLikeService;
import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/video")
public class VideoActionController {

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

}
