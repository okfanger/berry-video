package cn.akfang.berry.controller;

import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.constants.GlobalConstants;
import cn.akfang.berry.common.feign.client.MiscClient;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.request.WxLoginRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.model.response.UserVo;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.UserService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class UserController implements UserClient {

    @Autowired
    UserService userService;

    @Autowired
    MiscClient miscClient;


    @GetMapping("/info")
    public BaseResponse<UserVo> userInfo(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId) {
        UserPO byId = userService.getById(Long.valueOf(userId));
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(byId, userVo);
        userVo.setUserAvatar(GlobalConstants.OSS_URL + "/" + byId.getUserAvatar());
        return ResultUtils.success(userVo);
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
}
