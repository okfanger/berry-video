package cn.akfang.berry.controller;

import cn.akfang.berry.common.constants.WxConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.dto.WxLoginTmpTicketDTO;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.manager.WxManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/wx")
@RestController
@Slf4j
public class WxController {

    @Autowired
    UserClient userClient;

    @Autowired
    WxManager wxManager;

    @GetMapping("/")
    public String check(String timestamp, String nonce, String signature, String echostr) {
        if (wxManager.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        } else {
            return "";
        }
    }

    @PostMapping(produces = "application/xml; charset=UTF-8", value = "/")
    public String receiveMessage(@RequestBody String requestBody,
                                 @RequestParam("signature") String signature,
                                 @RequestParam("timestamp") String timestamp,
                                 @RequestParam("nonce") String nonce,
                                 @RequestParam("openid") String openid,
                                 @RequestParam(name = "encrypt_type", required = false) String encType,
                                 @RequestParam(name = "msg_signature", required = false) String msgSignature
    ) throws BerryRpcException {
        log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

        // 校验消息签名，判断是否为公众平台发的消息
        if (!wxManager.checkSignature(timestamp, nonce, signature)) {
            log.error("receiveMessage: 非法请求, time={}, no={}, sign={}", timestamp, nonce, signature);
            throw new BerryRpcException(ErrorCode.PARAMS_ERROR);
        }
        return wxManager.reply(requestBody);
    }


    @GetMapping("/ticket")
    public BaseResponse<WxLoginTmpTicketDTO> getWxTicket() {
        String clientId = userClient.generateWxLoginClientId();
        WxLoginTmpTicketDTO ticketInfo = wxManager.getWxLoginTicketInfo(clientId, WxConstants.EXPIRE_TIME);
        return ResultUtils.success(ticketInfo);
    }
}
