package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.FollowService;
import cn.hutool.core.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/follow")
@RestController
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/doFollow")
    public BaseResponse<Boolean> doFollow(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                          @RequestParam("userId") String followedUserIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        Long followedUserId = NumberUtil.parseLong(followedUserIdStr);

        followService.doFollow(userId, followedUserId);
        return ResultUtils.success(null);
    }

    @GetMapping("/unFollow")
    public BaseResponse<Boolean> unFollow(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                          @RequestParam("userId") String followedUserIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        Long followedUserId = NumberUtil.parseLong(followedUserIdStr);

        followService.unFollow(userId, followedUserId);
        return ResultUtils.success(null);
    }
}
