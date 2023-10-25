package cn.akfang.berry.wxmp.handler;

import cn.akfang.berry.common.enums.UserGenderEnum;
import cn.akfang.berry.common.enums.UserRoleEnum;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.service.UserService;
import cn.akfang.berry.util.RedisIdGenerator;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * 消息处理器
 **/
@Component
@Slf4j
public class MessageHandler implements WxMpMessageHandler {

    @DubboReference
    UserService userService;

    @Resource
    RedisIdGenerator redisIdGenerator;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map,
                                    WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {

        log.info("wx_mp_msg: map={}", map);
        log.info("wx_mp_msg: wxMpXmlMessage={}", wxMpXmlMessage);

        String content = wxMpXmlMessage.getContent();
        if (wxMpXmlMessage.getContent().length() == 6) {
            // 检查过期
            if (!redisIdGenerator.existId(wxMpXmlMessage.getContent())) {
                content = "token过期！请重新获取！";
            } else {
                // 登录部分
                String fromUser = wxMpXmlMessage.getFromUser();
                Optional<UserPO> userByUsername = Optional.ofNullable(userService.getUserByUsername(fromUser));


                if (userByUsername.isPresent()) {
                    content = "登录成功！";
                } else {
                    boolean result = userService.save(UserPO.builder()
                            .userName(fromUser)
                            .nickName(String.format("用户%d", fromUser.hashCode()))
                            .userPassword("12345678")
                            .gender(UserGenderEnum.MALE)
                            .userAvatar("default")
                            .userRole(UserRoleEnum.USER)
                            .build()
                    );

                    if (result) {
                        content = "注册成功！";
                    } else {
                        content = "注册失败！";
                    }
                }

                redisIdGenerator.recycleId(wxMpXmlMessage.getContent());
            }
        }

        return WxMpXmlOutMessage.TEXT().content(content)
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
    }
}
