package cn.akfang.berry.common.feign.client;


import cn.akfang.berry.common.model.base.Pair;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.common.model.response.UserBaseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Service
@FeignClient(value = "berry-user-service")
public interface UserClient {
    @GetMapping("/feign/generate_wx_login_client_id")
    String generateWxLoginClientId();

    @GetMapping("/feign/update_avatar")
    Boolean updateAvatar(@RequestParam("userId") Long userId, @RequestParam("ossKey") String ossKey);

    @GetMapping("/feign/get_user_base_vo_by_id")
    UserBaseVO getUserBaseVOById(@RequestParam("authorId") Long authorId, @RequestParam("currentUserId") String currentUserIdStr);

    @PostMapping("/feign/get_user_base_vo_by_ids")
    Map<Long, UserBaseVO> getUserBaseVOByIds(@RequestBody List<Pair<Long, Long>> ids, @RequestHeader("currentUserId") String currentUserId);

    @PostMapping("/feign/get_user_base_vo_list_by_ids")
    List<UserBaseVO> getUserBaseVOListByIds(@RequestBody List<Long> ids, @RequestHeader("currentUserId") String currentUserId);

    @PostMapping("/feign/list_by_ids")
    List<UserPO> listByIds(@RequestBody List<Long> ids);


    @GetMapping("/feign/list_all_ids")
    List<Long> listAllIds();

    @GetMapping("/feign/list_ids_minutes_ago")
    List<Long> listIdsMinutesAgo(@RequestParam("minute") Long minute);

}
