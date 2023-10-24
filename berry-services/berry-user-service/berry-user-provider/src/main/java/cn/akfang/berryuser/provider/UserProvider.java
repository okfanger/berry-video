package cn.akfang.berryuser.provider;

import cn.akfang.berry.berryuser.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class UserProvider implements UserService {
    @Override
    public String sayHello() {
        return "hello";
    }
}
