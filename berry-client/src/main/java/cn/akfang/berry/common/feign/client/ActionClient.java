package cn.akfang.berry.common.feign.client;


import cn.akfang.berry.common.model.dto.VideoActionDTO;
import cn.akfang.berry.common.model.request.VideoActionInfoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Service
@FeignClient(value = "berry-action-service")
public interface ActionClient {

    @PostMapping("/get_video_action_info_by_ids")
    Map<Long, VideoActionDTO> getVideoActionInfoByIds(@RequestBody VideoActionInfoRequest request);
}
