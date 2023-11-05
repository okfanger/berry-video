package cn.akfang.berry.controller;

import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.constants.GlobalConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.MiscClient;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.base.Pair;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.request.UserInfoUpdateDTO;
import cn.akfang.berry.common.model.request.WxLoginRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.model.response.UserVo;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.UserService;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class UserController implements UserClient {

    @Autowired
    UserService userService;

    @Autowired
    MiscClient miscClient;

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
    public BaseResponse<UserVo> userInfo(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        return ResultUtils.success(userService.getUserVoById(userId));
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
    public UserBaseVO getUserBaseVOById(Long authorId) {
        Optional<UserPO> userPO = Optional.ofNullable(userService.getByUserId(authorId));
        if (!userPO.isPresent()) {
            return null;
        } else {
            UserBaseVO userBaseVO = new UserBaseVO();
            userBaseVO.setAuthorAvatar(GlobalConstants.OSS_URL + "/" + userPO.get().getUserAvatar());
            userBaseVO.setAuthorId(userPO.get().getId());
            userBaseVO.setAuthorNickName(userPO.get().getNickName());
            return userBaseVO;
        }

    }

    @Override
    public Map<Long, UserBaseVO> getUserBaseVOByIds(List<Pair<Long, Long>> ids) {
        List<Long> authorIds = ids.stream().map(Pair::getB).collect(Collectors.toList());
        return userService.listByIds(authorIds).stream().map(item -> {
            UserBaseVO userBaseVO = new UserBaseVO();
            userBaseVO.setAuthorAvatar(GlobalConstants.OSS_URL + "/" + item.getUserAvatar());
            userBaseVO.setAuthorId(item.getId());
            userBaseVO.setAuthorNickName(item.getNickName());
            return userBaseVO;
        }).collect(Collectors.toMap(UserBaseVO::getAuthorId, item -> item));
    }

    @Override
    public List<UserPO> listByIds(List<Long> ids) {
        return userService.listByIds(ids);
    }

    @Override
    public List<Long> listAllIds() {
        return new LambdaQueryChainWrapper<>(userService.getBaseMapper())
                .select(UserPO::getId)
                .list()
                .stream().map(UserPO::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> listIdsMinutesAgo(Long minute) {
        return userService.listIdsMinutesAgo(minute);
    }
}
