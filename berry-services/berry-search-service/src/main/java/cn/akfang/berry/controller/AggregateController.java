package cn.akfang.berry.controller;

import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.EsUserService;
import cn.akfang.berry.service.EsVideoService;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/aggregate")
@RestController
public class AggregateController {

    @Autowired
    private EsUserService esUserService;

    @Autowired
    private EsVideoService esVideoService;

    @GetMapping("")
    public BaseResponse<Map> aggregateSearch(
            @RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "current", defaultValue = "1") String currentStr) {
        int current = NumberUtil.parseInt(currentStr);
        Long currentUserId = NumberUtil.parseLong(userId);

        return ResultUtils.success(MapUtil.builder()
                .put("user", esUserService.selectUserBaseVOByKeywordPage(keyword, current, currentUserId))
                .put("video", esVideoService.selectVideoVOByKeywordPage(keyword, current, currentUserId))
                .build()
        );
    }
}
