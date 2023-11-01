package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.dto.CommentAddDTO;
import cn.akfang.berry.common.model.entity.CommentPO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.CommentVo;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.CommentService;
import cn.akfang.berry.service.LikeRedisService;
import cn.akfang.berry.service.VideoService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserClient userClient;

    @Autowired
    @Qualifier("commentLikeRedisService")
    private LikeRedisService<Long, Long> likeRedisService;


    @GetMapping("/doLike")
    public BaseResponse<Boolean> doLike(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                        @RequestParam("commentId") String commentIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        Long commentId = NumberUtil.parseLong(commentIdStr);

        return ResultUtils.success(commentService.doLike(userId, commentId));
    }

    @GetMapping("/unLike")
    public BaseResponse<Boolean> doUnLike(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                          @RequestParam("commentId") String commentIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        Long commentId = NumberUtil.parseLong(commentIdStr);

        return ResultUtils.success(commentService.doUnLike(userId, commentId));
    }

    @PostMapping("")
    public BaseResponse<Boolean> addComment(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId,
                                            @RequestBody CommentAddDTO commentAddDTO) {
        Optional<VideoPO> commentedVideo = Optional.ofNullable(videoService.getById(commentAddDTO.getVideoId()));
        if (!commentedVideo.isPresent() || commentedVideo.get().getVisible() == 0) {
            throw new BerryRpcException(ErrorCode.VIDEO_NOT_EXIST_OR_VISIBLE);
        }
        return ResultUtils.success(commentService.addComment(commentedVideo.get(), NumberUtil.parseLong(userId), commentAddDTO));
    }

    @GetMapping("/feed")
    public BaseResponse<Page<CommentVo>> commentFeedList(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                                         @RequestParam("videoId") String videoIdStr,
                                                         @RequestParam(value = "orderBy", defaultValue = "time") String orderBy,
                                                         @RequestParam(value = "current", defaultValue = "1") String currentPage) {

        int current = NumberUtil.parseInt(currentPage);
        Long videoId = NumberUtil.parseLong(videoIdStr);
        Long userId = NumberUtil.parseLong(userIdStr);

        Page<CommentPO> commentPOPage = commentService.page(new Page<>(current, 10), commentService.getFeedQueryWrapper(videoId, orderBy));
        List<CommentVo> collect = commentPOPage.getRecords().stream().map(item -> {
            return commentService.buildCommonVO(item,
                    userClient.getUserBaseVOById(item.getCommentUserId()), userId);
        }).collect(Collectors.toList());
        Page<CommentVo> page = new Page<>();
        BeanUtil.copyProperties(commentPOPage, page);
        page.setRecords(collect);
        return ResultUtils.success(page);
    }
}
