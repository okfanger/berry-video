package cn.akfang.berry.berryuser.consumer;

import cn.akfang.berry.berryuser.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserConsumer {

    @DubboReference
    UserService userService;
    @GetMapping("/test")
    public String hello() {
        return userService.sayHello();
    }
}
