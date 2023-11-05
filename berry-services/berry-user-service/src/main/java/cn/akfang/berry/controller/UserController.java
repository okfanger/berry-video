package cn.akfang.berry.controller;

import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.ActionClient;
import cn.akfang.berry.common.feign.client.MiscClient;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.base.Pair;
import cn.akfang.berry.common.model.dto.UserActionDTO;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.request.UserInfoUpdateDTO;
import cn.akfang.berry.common.model.request.WxLoginRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.model.response.UserVo;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.UserService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@Slf4j
public class UserController implements UserClient {

    @Autowired
    UserService userService;

    @Autowired
    MiscClient miscClient;

    @Autowired
    ActionClient actionClient;

    @PostMapping("/updateInfo")
    public BaseResponse<UserVo> updateUserInfo(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                               @RequestBody UserInfoUpdateDTO dto) {
        Long userId = NumberUtil.parseLong(userIdStr);
        UserPO userPO = userService.getByUserId(userId);
        if (ObjectUtil.isNull(userPO)) {
            throw new BerryRpcException(ErrorCode.SYSTEM_ERROR, "用户不存在");
        }
        Boolean update = userService.updateUserInfo(dto, userId);
        if (update) {
            return ResultUtils.success(userService.getUserVoById(userId));
        } else {
            throw new BerryRpcException(ErrorCode.SYSTEM_ERROR, "更新失败");
        }
    }


    @GetMapping("/info")
    public BaseResponse<UserBaseVO> userInfo(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
//        return ResultUtils.success(userService.getUserVoById(userId));
        return ResultUtils.success(getUserBaseVOById(userId, userIdStr));
    }

    @PostMapping("/wx/login")
    public BaseResponse<UserTokenResponse> wxLogin(@RequestBody WxLoginRequest request) {
        return ResultUtils.success(userService.wxLogin(request));
    }

    @Override
    public String generateWxLoginClientId() {
        return userService.generateWxLoginClientId();
    }

    @Override
    public Boolean updateAvatar(Long userId, String ossKey) {
        return new LambdaUpdateChainWrapper<>(userService.getBaseMapper())
                .eq(UserPO::getId, userId)
                .set(UserPO::getUserAvatar, ossKey)
                .update();
    }


    @Override
    public UserBaseVO getUserBaseVOById(Long authorId, String currentUserIdStr) {
        return getUserBaseVOListByIds(Arrays.asList(authorId), currentUserIdStr)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Map<Long, UserBaseVO> getUserBaseVOByIds(List<Pair<Long, Long>> ids, String currentUserId) {
        if (CollectionUtil.isEmpty(ids))
            return Collections.emptyMap();

        List<Long> authorIds = ids.stream().map(Pair::getB).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(authorIds))
            return Collections.emptyMap();
        return getUserBaseVOStream(authorIds, NumberUtil.parseLong(currentUserId))
                .collect(Collectors.toMap(UserBaseVO::getAuthorId, item -> item));
    }

    private Stream<UserBaseVO> getUserBaseVOStream(List<Long> ids, Long currentUserId) {
        Map<Long, UserActionDTO> userActionInfoMap = actionClient.getUserActionInfoByIds(ids, String.valueOf(currentUserId));
        return userService.listByIds(ids).stream()
                .map(item -> {
                    UserBaseVO userBaseVO = userService.buildUserBaseVO(item);
                    userBaseVO.setAction(userActionInfoMap.get(item.getId()));
                    return userBaseVO;
                });
    }

    @Override
    public List<UserBaseVO> getUserBaseVOListByIds(List<Long> ids, String currentUserId) {
        Long currentUserIdLong = NumberUtil.parseLong(currentUserId);
        if (CollectionUtil.isEmpty(ids))
            return Collections.emptyList();

        return getUserBaseVOStream(ids, currentUserIdLong)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserPO> listByIds(List<Long> ids) {
        return userService.listByIds(ids);
    }

    @Override
    public List<Long> listAllIds() {
        QueryWrapper<UserPO> qw = new QueryWrapper<>();
        qw.select("id");
        List<Long> collect = userService.list(qw)
                .stream()
                .map(UserPO::getId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(collect)) {
            return Collections.emptyList();
        } else {
            return collect;
        }
    }

    @Override
    public List<Long> listIdsMinutesAgo(Long minute) {
        return userService.listIdsMinutesAgo(minute);
    }
}
