package cn.akfang.berry.wxmp.handler;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 事件处理器
 **/
@Component
@Slf4j
public class EventHandler implements WxMpMessageHandler {


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService,
                                    WxSessionManager wxSessionManager) throws WxErrorException {
        log.info("wx_mp_event: map={}", map);
        log.info("wx_mp_event: wxMpXmlMessage={}", wxMpXmlMessage);

        String eventKey = wxMpXmlMessage.getEventKey();
        if (StringUtils.equals(eventKey, "BERRY_VIDEO_AUTH_CODE")) {

            String code = IdUtil.nanoId(6);

            // 调用接口，返回验证码
            return WxMpXmlOutMessage.TEXT().content("欢迎使用树莓派👏\n以下是您的专属登录码：" + code)
                    .fromUser(wxMpXmlMessage.getToUser())
                    .toUser(wxMpXmlMessage.getFromUser())
                    .build();
        } else {
            return null;
        }
    }
}
