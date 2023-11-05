package cn.akfang.berry.controller;

import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.UserEsService;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class EsUserController {

    @Autowired
    private UserEsService userEsService;

    @GetMapping("")
    public BaseResponse<Page<UserBaseVO>> searchUserBaseVOByKeyword(
            @RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "current", defaultValue = "1") String currentStr) {

        int current = NumberUtil.parseInt(currentStr);
        Long userId = NumberUtil.parseLong(userIdStr);
        Page<UserBaseVO> userBaseVOPage = userEsService.selectUserBaseVOByKeywordPage(keyword, current, userId);
        return ResultUtils.success(userBaseVOPage);
    }
}
