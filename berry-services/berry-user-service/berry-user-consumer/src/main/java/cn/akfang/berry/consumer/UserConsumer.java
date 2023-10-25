package cn.akfang.berry.consumer;

import cn.akfang.berry.common.model.request.UserLoginRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.MediaService;
import cn.akfang.berry.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserConsumer {

    @DubboReference
    UserService userService;

    @DubboReference
    MediaService mediaService;

    @PostMapping("/login")
    public BaseResponse<UserTokenResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return ResultUtils.success(userService.login(userLoginRequest));
    }

    @PostMapping("/wx/login")
    public void wxLogin() {

    }
}
