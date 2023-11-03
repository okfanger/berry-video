package cn.akfang.berry.common.feign.client;

import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.VideoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@FeignClient(value = "berry-video-service")
public interface VideoClient {

    @GetMapping("/feign/listAll")
    List<VideoPO> listAll();

    @GetMapping("/feign/get_vo_by_ids")
    List<VideoVO> getVOByIds(@RequestParam("videoIds") List<Long> ids, @RequestParam("currentUserId") Long currentUserId);
}