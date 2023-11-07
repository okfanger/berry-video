package cn.akfang.berry.controller;

import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.manager.InteractManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interact")
public class InteractController {

    @Autowired
    private InteractManager interactManager;

    @PostMapping("/room")
    public BaseResponse<String> createRoom() {
        String roomId = interactManager.createRoom();
        return ResultUtils.success(roomId);
    }
}
