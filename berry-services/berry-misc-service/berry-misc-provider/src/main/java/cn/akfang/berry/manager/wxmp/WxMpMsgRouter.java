package cn.akfang.berry.manager.wxmp;

import cn.akfang.berry.manager.wxmp.handler.EventHandler;
import cn.akfang.berry.manager.wxmp.handler.MessageHandler;
import cn.akfang.berry.manager.wxmp.handler.ScanHandler;
import cn.akfang.berry.manager.wxmp.handler.SubscribeHandler;
import me.chanjar.weixin.common.api.WxConsts.EventType;
import me.chanjar.weixin.common.api.WxConsts.XmlMsgType;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 微信公众号路由
 */
@Configuration
public class WxMpMsgRouter {

    @Resource
    private WxMpService wxMpService;

    @Resource
    private EventHandler eventHandler;

    @Resource
    private MessageHandler messageHandler;

    @Resource
    private SubscribeHandler subscribeHandler;

    @Resource
    private ScanHandler scanHandler;

    @Bean
    public WxMpMessageRouter getWxMsgRouter() {
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);

        // 消息
        router.rule()
                .async(false)
                .msgType(XmlMsgType.TEXT)
                .handler(messageHandler)
                .end();
        // 关注
        router.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(EventType.SUBSCRIBE)
                .handler(subscribeHandler)
                .end();

        // 扫码登录
        router.rule()
                .async(false)
                .msgType(XmlMsgType.EVENT)
                .event(EventType.SCAN)
                .handler(scanHandler)
                .end();
        return router;
    }
}
