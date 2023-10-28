package cn.akfang.berry.controller;

import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.exception.BusinessException;
import cn.akfang.berry.common.feign.client.MiscService;
import cn.akfang.berry.common.model.dto.QiniuUploadDTO;
import cn.akfang.berry.common.model.dto.WxLoginTmpTicketDTO;
import cn.akfang.berry.common.model.response.FileUploadToken;
import cn.akfang.berry.manager.QiniuOSSManager;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@Slf4j
@RestController
public class MiscProvider implements MiscService {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpMessageRouter router;

    @Autowired
    QiniuOSSManager qiniuOSSManager;

    @Override
    public WxLoginTmpTicketDTO getWxLoginTicketInfo(String sceneStr, Integer expireTime) throws BerryRpcException {
        try {
            WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService()
                    .qrCodeCreateTmpTicket(sceneStr, expireTime);

            return WxLoginTmpTicketDTO.builder()
                    .url(wxMpQrCodeTicket.getUrl())
                    .expire_seconds(wxMpQrCodeTicket.getExpireSeconds())
                    .ticket(wxMpQrCodeTicket.getTicket())
                    .id(sceneStr)
                    .build();

        } catch (WxErrorException e) {
            throw new BusinessException(ErrorCode.WX_SERVER_ERROR);
        }
    }

    @Override
    public String reply(String requestXmlBody) throws BerryRpcException {
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestXmlBody);
        log.info("message content = {}", inMessage.getContent());
        return router.route(inMessage).toXml();
    }

    @Override
    public Boolean checkSignature(String timestamp, String nonce, String signature) throws BerryRpcException {
        return wxMpService.checkSignature(timestamp, nonce, signature);
    }


    @Override
    public FileUploadToken getUploadToken() {
        String uploadToken = qiniuOSSManager.getUploadToken();
        return FileUploadToken.builder()
                .upToken(uploadToken)
                .build();
    }

    @Override
    public String uploadFile(String upToken) {
        QiniuUploadDTO qiniuUploadDTO = qiniuOSSManager.uploadFile(upToken, new File("/Users/fang/test1.mov"));
        log.info("uploadFile: {}", JSONUtil.toJsonStr(qiniuUploadDTO));
        return "success";
    }
}
