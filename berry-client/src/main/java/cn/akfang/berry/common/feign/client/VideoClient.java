package cn.akfang.berry.common.feign.client;

import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.VideoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/feign/listVideoVoByIds")
    List<VideoVO> listVideoVoByIds(@RequestBody List<Long> ids, @RequestHeader("currentUserId") String currentUserIdStr);

    @GetMapping("/feign/listAllIds")
    List<Long> listAllIds();

    @GetMapping("/feign/getById")
    VideoPO getById(@RequestParam("videoId") Long videoId);
}