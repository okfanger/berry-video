package cn.akfang.berry.manager.wxmp.handler;

import cn.akfang.berry.common.enums.WxConstants;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 事件处理器
 **/
@Component
@Slf4j
public class ScanHandler implements WxMpMessageHandler {

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService,
                                    WxSessionManager wxSessionManager) throws WxErrorException {
        log.info("wx_mp_event: map={}", map);
        log.info("wx_mp_event: wxMpXmlMessage={}", wxMpXmlMessage);

        String eventKey = wxMpXmlMessage.getEventKey();
        String code = (String) redisTemplate.opsForValue().get(WxConstants.KEY + ":" + eventKey);
        redisTemplate.opsForValue().set(
                WxConstants.FROM_USER_OPENID2CLIENT_ID_KEY + ":" + eventKey, wxMpXmlMessage.getFromUser(),
                WxConstants.EXPIRE_TIME, TimeUnit.SECONDS
        );
        return WxMpXmlOutMessage.TEXT().content("欢迎使用树莓派👏\n登录成功！\n您的专属识别码：" + code)
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
    }
}
