package cn.akfang.berry.common.feign.client;

import cn.akfang.berry.common.model.entity.VideoPO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@FeignClient(value = "berry-video-service")
public interface VideoClient {

    @GetMapping("/feign/listAll")
    List<VideoPO> listAll();
    @GetMapping("/feign/listIdsMinutesAgo")
    List<Long> listIdsMinutesAgo(@RequestParam("num") Long minuteNum);

    @PostMapping("/feign/listByIds")
    List<VideoPO> listByIds(@RequestBody List<Long> ids);

    @GetMapping("/feign/listAllIds")
    List<Long> listAllIds();
}