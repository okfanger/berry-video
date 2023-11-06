package cn.akfang.berry.common.feign.client;


import cn.akfang.berry.common.model.dto.UserActionDTO;
import cn.akfang.berry.common.model.dto.VideoActionDTO;
import cn.akfang.berry.common.model.request.VideoActionInfoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Service
@FeignClient(value = "berry-action-service")
public interface ActionClient {

    @PostMapping("/get_video_action_info_by_ids")
    Map<Long, VideoActionDTO> getVideoActionInfoByIds(@RequestBody VideoActionInfoRequest request);

    @PostMapping("/get_user_action_info_by_ids")
    Map<Long, UserActionDTO> getUserActionInfoByIds(@RequestBody List<Long> ids, @RequestHeader("currentUserId") String currentUserId);

    @GetMapping("/get_fans_ids_by_user_id")
    List<Long> getFansIdsByUserId(@RequestParam("userId") Long userId);

    @GetMapping("/get_follows_ids_by_user_id")
    List<Long> getFollowsIdsByUserId(@RequestParam("userId") Long userId);

    @GetMapping("/get_liked_video_ids_by_user_id")
    List<Long> getLikedVideoIdsByUserId(@RequestParam("userId") Long userId);

    @GetMapping("/get_favored_video_ids_by_user_id")
    List<Long> getFavoredVideoIdsByUserId(@RequestParam("userId") Long currentUserId);
}
