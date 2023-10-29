package cn.akfang.berry.common.feign.client;

import cn.akfang.berry.common.model.entity.VideoPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "berry-video-service")
public interface VideoClient {
    @PostMapping("/rpc/saveVideo")
    boolean saveVideo(@RequestBody VideoPO dto);
}