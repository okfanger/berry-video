package cn.akfang.berry.manager.wxmp.handler;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
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

        return WxMpXmlOutMessage.TEXT().content("欢迎使用树莓派👏\n我是复读机先生\necho: " + wxMpXmlMessage.getContent())
                .build();
    }
}
