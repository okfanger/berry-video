package cn.akfang.berry.controller;

import cn.akfang.berry.common.model.entity.ChannelPO;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/channel")
@RestController
public class ChannelController {

    @Autowired
    ChannelService channelService;

    @GetMapping("/list")
    public BaseResponse<List<ChannelPO>> getChannelList() {
        return ResultUtils.success(channelService.list());
    }
}
