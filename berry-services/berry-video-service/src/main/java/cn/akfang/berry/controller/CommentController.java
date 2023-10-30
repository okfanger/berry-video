package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.model.dto.CommentAddDTO;
import cn.akfang.berry.common.model.entity.CommentPO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.CommentService;
import cn.akfang.berry.service.VideoService;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private VideoService videoService;

    @PostMapping("")
    public BaseResponse<Boolean> addComment(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId, @RequestBody CommentAddDTO commentAddDTO) {
        VideoPO commentedVideo = new LambdaQueryChainWrapper<>(videoService.getBaseMapper())
                .select(VideoPO::getAuthorId)
                .eq(VideoPO::getVisible, 1)
                .eq(VideoPO::getId, commentAddDTO.getVideoId())
                .oneOpt().orElseThrow(() -> new BerryRpcException(ErrorCode.VIDEO_NOT_EXIST_OR_VISIBLE));
        CommentPO newComment = CommentPO.builder()
                .commentUserId(Long.valueOf(userId))
                .content(commentAddDTO.getContent())
                .videoId(commentAddDTO.getVideoId())
                .authorId(commentedVideo.getAuthorId())
                .likeCounts(0)
                .build();
        return ResultUtils.success(commentService.save(newComment));
    }

//    @GetMapping("/feed")
//    public BaseResponse<Boolean> commentFeedList() {
//        commentService.
//    }
}
