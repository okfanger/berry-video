package cn.akfang.berry.controller;

import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.MiscService;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Slf4j
@RestController
public class MiscConsumer {

    @Autowired
    private MiscService miscService;

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
        if (!miscService.checkSignature(timestamp, nonce, signature)) {
            log.error("receiveMessage: 非法请求, time={}, no={}, sign={}", timestamp, nonce, signature);
            throw new BerryRpcException(ErrorCode.PARAMS_ERROR);
        }
        return miscService.reply(requestBody);
    }

    @GetMapping("/wx")
    public String check(String timestamp, String nonce, String signature, String echostr) {
        if (miscService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        } else {
            return "";
        }
    }

    @PostMapping("/upload/callback")
    public BaseResponse<Map<String, Object>> fileUploadCallBack(@RequestBody Map<String, Object> requestBody) {
        log.debug("fileUploadCallBack: {}", JSONUtil.toJsonStr(requestBody));
//        String accessKey = "your access key";
//        String secretKey = "your secret key";
//        /*
//         * 这两个参数就是在定义PutPolicy参数时指定的内容
//         */
////回调地址
//        String callbackUrl = "http://api.example.com/qiniu/callback";
////定义回调内容的组织格式，与上传策略中的callbackBodyType要保持一致
////String callbackBodyType = "application/x-www-form-urlencoded"; //回调鉴权的签名包括请求内容callbackBody
//        String callbackBodyType = "application/json";//回调鉴权的签名不包括请求内容
///**
// * 这两个参数根据实际所使用的HTTP框架进行获取
// */
////通过获取请求的HTTP头部Authorization字段获得
//        String callbackAuthHeader = "xxx";
////通过读取回调POST请求体获得，不要设置为null
//        byte[] callbackBody = null;
//        Auth auth = Auth.create(accessKey, secretKey);
////检查是否为合法的回调请求
//        boolean validCallback = auth.isValidCallback(callbackAuthHeader, callbackUrl, callbackBody, callbackBodyType);
//        if (validCallback) {
//            //继续处理其他业务逻辑
//        } else {
//            //这是哪里的请求，被劫持，篡改了吧？
//        }
        return ResultUtils.success(requestBody);
    }
}
