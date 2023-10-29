package cn.akfang.berry.controller;

import cn.akfang.berry.common.feign.client.VideoClient;
import cn.akfang.berry.common.model.dto.QiniuTransformCallBackDTO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.request.ClientUploadTokenRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.FileUploadToken;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.manager.QiniuOSSManager;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RequestMapping("/oss")
@RestController
@Slf4j
public class OssController {

    @Autowired
    QiniuOSSManager qiniuOSSManager;

    @Autowired
    VideoClient videoClient;

    @PostMapping("/upload/callback")
    public BaseResponse<Map<String, Object>> fileUploadCallBack(@RequestBody Map<String, Object> requestBody) {
        log.debug("fileUploadCallBack: {}", JSONUtil.toJsonStr(requestBody));
//        videoService.saveVideo(requestBody);
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

    @PostMapping("/transform/callback")
    public BaseResponse<Boolean> transformCallBack(@RequestBody QiniuTransformCallBackDTO requestBody) {
        log.debug("transformCallBack: {}", JSONUtil.toJsonStr(requestBody));
        QiniuTransformCallBackDTO.Items mp4cmd = requestBody.getItems().stream()
                .filter(item -> item.getCmd().equals("avthumb/mp4")).findFirst().get();
        QiniuTransformCallBackDTO.Items m3u8cmd = requestBody.getItems().stream()
                .filter(item -> item.getCmd().equals("avthumb/m3u8/noDomain/1/segtime/15/vb/440k")).findFirst().get();

        return ResultUtils.success(videoClient.saveVideo(VideoPO.builder()
                .title(IdUtil.simpleUUID())
                .sourceKey(requestBody.getInputKey())
                .mp4Key(mp4cmd.getKey())
                .m3u8Key(m3u8cmd.getKey())
                .visible(0)
                .build()));
    }


    /**
     * 客户端上传
     *
     * @param request uuid
     * @return 上传凭证 upToken
     */
    @PostMapping("/upload/token")
    public BaseResponse<FileUploadToken> clientUploadToken(@Valid @RequestBody ClientUploadTokenRequest request) {
        synchronized (request.getUuid().intern()) {
            String uploadToken = qiniuOSSManager.getUploadToken();
            return ResultUtils.success(FileUploadToken.builder()
                    .upToken(uploadToken)
                    .build());
        }
    }

}
