package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.FollowService;
import cn.hutool.core.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/follow")
@RestController
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserClient userClient;

    @GetMapping("/list")
    public BaseResponse<List<UserBaseVO>> listFollowedUserPage(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                                               @RequestParam(value = "current", defaultValue = "1") String currentStr) {
        Long currentUserId = NumberUtil.parseLong(userIdStr);
        List<Long> followedUserIdList = followService.getFollowsIdByUserId(currentUserId);
        List<Long> ids = followService.buildUserIds(followedUserIdList, currentStr);
        return ResultUtils.success(userClient.getUserBaseVOListByIds(ids, userIdStr));
    }

    @GetMapping("/fans")
    public BaseResponse<List<UserBaseVO>> listFansPage(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                                       @RequestParam(value = "current", defaultValue = "1") String currentStr) {
        Long currentUserId = NumberUtil.parseLong(userIdStr);
        List<Long> fansIdList = followService.getFansIdByUserId(currentUserId);
        List<Long> ids = followService.buildUserIds(fansIdList, currentStr);
        return ResultUtils.success(userClient.getUserBaseVOListByIds(ids, userIdStr));
    }

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
