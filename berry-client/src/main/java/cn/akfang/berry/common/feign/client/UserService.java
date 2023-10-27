package cn.akfang.berry.common.feign.client;


import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.request.WxLoginRequest;
import cn.akfang.berry.common.model.response.UserTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient(value = "berry-user-service")
public interface UserService {
    @GetMapping("/getUserByUsername")
    UserPO getUserByUsername(String username);

    @GetMapping("/generateWxLoginClientId")
    String generateWxLoginClientId();

    @PostMapping("/wxLogin")
    UserTokenResponse wxLogin(WxLoginRequest request);
}
