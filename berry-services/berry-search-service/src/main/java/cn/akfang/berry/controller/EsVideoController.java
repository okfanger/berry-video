package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.VideoVO;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.EsVideoService;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
@Slf4j
public class EsVideoController {
    @Autowired
    EsVideoService esVideoService;
    @GetMapping("")
    public BaseResponse<Page<VideoVO>> getVideoIdsByKeyword(
            @RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "current", defaultValue = "1") String currentStr) {

        Long currentUserId = NumberUtil.parseLong(userId);
        int current = NumberUtil.parseInt(currentStr);
        Page<VideoVO> userBaseVOPage = esVideoService.selectVideoVOByKeywordPage(keyword, current, currentUserId);
        return ResultUtils.success(userBaseVOPage);
    }
}
