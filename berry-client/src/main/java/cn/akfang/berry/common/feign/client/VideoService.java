package cn.akfang.berry.common.feign.client;

import cn.akfang.berry.common.model.entity.VideoPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "berry-video-service")
public interface VideoService {

    @GetMapping("/like2")
    String like(@RequestParam("videoId") Long videoId);

    @PostMapping("/saveVideo")
    boolean saveVideo(@RequestBody VideoPO dto);
}