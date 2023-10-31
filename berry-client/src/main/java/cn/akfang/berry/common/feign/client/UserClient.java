package cn.akfang.berry.common.feign.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "berry-user-service")
public interface UserClient {
    @GetMapping("/feign/generate_wx_login_client_id")
    String generateWxLoginClientId();

    @GetMapping("/feign/update_avatar")
    Boolean updateAvatar(@RequestParam("userId") Long userId, @RequestParam("ossKey") String ossKey);
}
