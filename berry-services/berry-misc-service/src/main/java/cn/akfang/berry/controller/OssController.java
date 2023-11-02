package cn.akfang.berry.controller;

import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.dto.QiniuUploadDTO;
import cn.akfang.berry.common.model.mq.QiniuTransformCallBackBodyDTO;
import cn.akfang.berry.common.model.mq.VideoUploadedCallbackBodyDTO;
import cn.akfang.berry.common.model.request.ClientUploadTokenRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.FileUploadToken;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.constant.QiniuMessageConstants;
import cn.akfang.berry.manager.QiniuOSSManager;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequestMapping("/oss")
@RestController
@Slf4j
public class OssController {

    @Autowired
    QiniuOSSManager qiniuOSSManager;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    UserClient userClient;

    @PostMapping("/upload/callback")
    public BaseResponse<VideoUploadedCallbackBodyDTO> fileUploadCallBack(@RequestBody VideoUploadedCallbackBodyDTO requestBody) {
        log.debug("fileUploadedCallBack: {}", JSONUtil.toJsonStr(requestBody));
        Long fileId = (Long) rabbitTemplate.convertSendAndReceive(QiniuMessageConstants.QINIU_CALLBACK_EXCHANGE,
                QiniuMessageConstants.QINIU_CALLBACK_UPLOADED_ROUTING_KEY, requestBody);
        requestBody.setFileId(fileId);
        return ResultUtils.success(requestBody);
    }

    @PostMapping("/transform/callback")
    public QiniuTransformCallBackBodyDTO transformCallBack(@RequestBody QiniuTransformCallBackBodyDTO requestBody) {
        log.debug("transformCallBack: {}", JSONUtil.toJsonStr(requestBody));
        rabbitTemplate.convertAndSend(QiniuMessageConstants.QINIU_CALLBACK_EXCHANGE, QiniuMessageConstants.QINIU_CALLBACK_TRANSFORM_ROUTING_KEY, requestBody);
        return requestBody;
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
            String uploadToken = qiniuOSSManager.getVideoUploadToken();
            return ResultUtils.success(FileUploadToken.builder()
                    .upToken(uploadToken)
                    .build());
        }
    }

    @PostMapping("/upload/avatar")
    public BaseResponse<Boolean> uploadAvatar(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId,
                                              @RequestParam("file") MultipartFile file) throws Exception {
        String avatarKey = "user_" + userId + ".avatar";
        String avatarUploadToken = qiniuOSSManager.getAvatarUploadToken(avatarKey);
        QiniuUploadDTO qiniuUploadDTO = qiniuOSSManager.uploadFileByInputStream(avatarUploadToken, avatarKey
                , file.getInputStream());
        return ResultUtils.success(userClient.updateAvatar(Long.valueOf(userId), qiniuUploadDTO.getKey()));
    }
}
