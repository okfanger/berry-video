package cn.akfang.berry.consumer;

import cn.akfang.berry.common.enums.WxConstants;
import cn.akfang.berry.common.model.dto.WxLoginTmpTicketDTO;
import cn.akfang.berry.common.model.request.WxLoginRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.UserService;
import cn.akfang.berry.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserConsumer {

    @DubboReference
    UserService userService;

    @DubboReference
    WxService wxService;

    @GetMapping("/wx/ticket")
    public BaseResponse<WxLoginTmpTicketDTO> getWxTicket() {
        return ResultUtils.success(
                wxService.getWxLoginTicketInfo(
                        userService.generateWxLoginClientId(), WxConstants.EXPIRE_TIME
                )
        );
    }


    @PostMapping("/wx/login")
    public BaseResponse<UserTokenResponse> wxLogin(@RequestBody WxLoginRequest request) {
        return ResultUtils.success(userService.wxLogin(request));
    }
}
