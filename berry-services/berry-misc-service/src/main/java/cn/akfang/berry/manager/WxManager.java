package cn.akfang.berry.manager;

import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.exception.BusinessException;
import cn.akfang.berry.common.model.dto.WxLoginTmpTicketDTO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WxManager {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpMessageRouter router;

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

    public String reply(String requestXmlBody) throws BerryRpcException {
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestXmlBody);
        log.info("message content = {}", inMessage.getContent());
        return router.route(inMessage).toXml();
    }

    public Boolean checkSignature(String timestamp, String nonce, String signature) throws BerryRpcException {
        return wxMpService.checkSignature(timestamp, nonce, signature);
    }
}
