package cn.akfang.berry.consumer;

import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class WxConsumer {

    @DubboReference
    private WxService wxService;


    @PostMapping(produces = "application/xml; charset=UTF-8", value = "/wx")
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
        if (!wxService.checkSignature(timestamp, nonce, signature)) {
            log.error("receiveMessage: 非法请求, time={}, no={}, sign={}", timestamp, nonce, signature);
            throw new BerryRpcException(ErrorCode.PARAMS_ERROR);
        }
        return wxService.reply(requestBody);
    }

    @GetMapping("/wx")
    public String check(String timestamp, String nonce, String signature, String echostr) {
        if (wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        } else {
            return "";
        }
    }
}
