package cn.akfang.berry.controller;

import cn.akfang.berry.common.enums.WxConstants;
import cn.akfang.berry.common.feign.client.MiscService;
import cn.akfang.berry.common.model.dto.WxLoginTmpTicketDTO;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.request.WxLoginRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.model.response.UserVo;
import cn.akfang.berry.common.utils.BerryJWTUtil;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class UserConsumer {

    @Qualifier("userProvider")
    @Autowired
    UserProvider userService;

    @Autowired
    MiscService miscService;

    @GetMapping("/wx/ticket")
    public BaseResponse<WxLoginTmpTicketDTO> getWxTicket() {
        return ResultUtils.success(
                miscService.getWxLoginTicketInfo(
                        userService.generateWxLoginClientId(), WxConstants.EXPIRE_TIME
                )
        );
    }


    @PostMapping("/wx/login")
    public BaseResponse<UserTokenResponse> wxLogin(@RequestBody WxLoginRequest request) {
        return ResultUtils.success(userService.wxLogin(request));
    }

    @GetMapping("/info")
    public BaseResponse<UserVo> userInfo(@RequestHeader(BerryJWTUtil.OUTER_HEADER_NAME) String headerToken) {
        Map map = BerryJWTUtil.parseHeaderToken(headerToken);

        UserPO byId = userService.getById(Long.valueOf(String.valueOf(map.get("id"))));
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(byId, userVo);
        return ResultUtils.success(userVo);
    }
}
