package cn.akfang.berry.consumer;

import cn.akfang.berry.common.model.request.UserLoginRequest;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.MediaService;
import cn.akfang.berry.service.UserService;
import cn.akfang.berry.util.RedisIdGenerator;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Slf4j
public class UserConsumer implements InitializingBean {

    @DubboReference
    UserService userService;

    @DubboReference
    MediaService mediaService;

    @Resource
    WxMpService wxMpService;

    @Resource
    WxMpMessageRouter wxMpMessageRouter;

    @PostMapping("/login")
    public BaseResponse<UserTokenResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return ResultUtils.success(userService.login(userLoginRequest));
    }

    @RequestMapping("/wx")
    public String send(@RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce) {
        // 校验签名
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            log.error("签名校验 ===> 非法请求");
            // 消息签名不正确，说明不是公众平台发过来的消息
            return null;
        }


        // 解析消息体，封装为对象
        WxMpXmlMessage xmlMessage = WxMpXmlMessage.fromXml(requestBody);
        log.info("wx: {}", xmlMessage);

        WxMpXmlOutMessage outMessage = null;
        try {
            // 将消息路由给对应的处理器，获取响应
            outMessage = wxMpMessageRouter.route(xmlMessage);
        } catch (Exception e) {
            log.error("消息路由异常", e);
        }
        // 将响应消息转换为xml格式返回
        return outMessage == null ? null : outMessage.toXml();
    }

    @Resource
    RedisIdGenerator redisIdGenerator;

    @GetMapping("/wx/code")
    public BaseResponse<String> getCode() {
        return ResultUtils.success(redisIdGenerator.generateId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        WxMenu wxMenu = new WxMenu();
//        WxMenuButton wxMenuButton = new WxMenuButton();
//        wxMenuButton.setType("click");
//        wxMenuButton.setName("获取登录码(树莓拍)");
//        wxMenuButton.setKey("BERRY_VIDEO_AUTH_CODE");
//        List<WxMenuButton> list = new ArrayList<>();
//        list.add(wxMenuButton);
//        wxMenu.setButtons(list);
//        wxMpService.getMenuService().menuCreate(wxMenu);
    }
}
