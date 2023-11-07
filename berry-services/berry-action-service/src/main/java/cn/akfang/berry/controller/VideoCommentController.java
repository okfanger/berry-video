package cn.akfang.berry.controller;


import cn.akfang.berry.common.constants.AuthConstants;
import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.feign.client.VideoClient;
import cn.akfang.berry.common.model.base.Pair;
import cn.akfang.berry.common.model.dto.CommentAddDTO;
import cn.akfang.berry.common.model.entity.CommentPO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.model.response.CommentVo;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.common.utils.ResultUtils;
import cn.akfang.berry.service.CommentService;
import cn.akfang.berry.service.UserCommentLikeService;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/video/comment")
@Slf4j
public class VideoCommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserCommentLikeService userCommentLikeService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private VideoClient videoClient;

    @GetMapping("/feed")
    public BaseResponse<Page<CommentVo>> commentFeedList(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                                         @RequestParam("videoId") String videoId,
                                                         @RequestParam(value = "orderBy", required = false) String orderBy,
                                                         @RequestParam(value = "current", defaultValue = "1") String currentPageStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        long currentPage = NumberUtil.parseLong(currentPageStr);
        currentPage = currentPage > 0 ? currentPage : 1L;
        LambdaQueryWrapper<CommentPO> qw = new LambdaQueryWrapper<>();
        qw.eq(CommentPO::getVideoId, videoId);
        if (StrUtil.equals("time", orderBy)) {
            qw = qw.orderByDesc(CommentPO::getCreateTime);
        } else {
            qw = qw.orderByDesc(CommentPO::getLikeCounts);
        }
        Page<CommentPO> commentPOPage = commentService.page(new Page<>(currentPage, 10), qw);

        List<Pair<Long, Long>> ids = commentPOPage.getRecords().stream().map(item -> {
            Pair<Long, Long> pair = new Pair<>();
            pair.setA(item.getId());
            pair.setB(item.getCommentUserId());
            return pair;
        }).collect(Collectors.toList());

        Map<Long, UserBaseVO> userBaseVOByIds = userClient.getUserBaseVOByIds(ids, userIdStr);

        List<CommentVo> collect = commentPOPage.getRecords().stream().map(item -> commentService.buildCommonVO(item, userBaseVOByIds.get(item.getCommentUserId()), userId)).collect(Collectors.toList());

        Page<CommentVo> commentVoPage = new Page<>();
        commentVoPage.setTotal(commentPOPage.getTotal());
        commentVoPage.setCurrent(commentPOPage.getCurrent());
        commentVoPage.setSize(commentPOPage.getSize());
        commentVoPage.setRecords(collect);
        return ResultUtils.success(commentVoPage);
    }

    @GetMapping("/doLike")
    public BaseResponse<Boolean> doLike(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                        @RequestParam("commentId") String commentIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        Long commentId = NumberUtil.parseLong(commentIdStr);
        userCommentLikeService.doLike(userId, commentId);
        return ResultUtils.success(null);
    }

    @GetMapping("/unLike")
    public BaseResponse<Boolean> doUnLike(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                          @RequestParam("commentId") String commentIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        Long commentId = NumberUtil.parseLong(commentIdStr);
        userCommentLikeService.unLike(userId, commentId);
        return ResultUtils.success(null);
    }

    @PostMapping("")
    public BaseResponse<CommentVo> addComment(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userId,
                                            @RequestBody CommentAddDTO commentAddDTO) {
        Optional<VideoPO> commentedVideo = Optional.ofNullable(videoClient.getById(commentAddDTO.getVideoId()));
        if (!commentedVideo.isPresent() || commentedVideo.get().getVisible() == 0) {
            throw new BerryRpcException(ErrorCode.VIDEO_NOT_EXIST_OR_VISIBLE);
        }
        Long videoAuthorId = commentedVideo.get().getAuthorId();
        Long userIdLong = NumberUtil.parseLong(userId);
        CommentPO commentPO = commentService.buildNewCommentPO(commentedVideo.get(), userIdLong, commentAddDTO);
        boolean b = commentService.addComment(commentPO, commentedVideo.get());
        if (b) {
            UserBaseVO userBaseVO = userClient.getUserBaseVOById(userIdLong, userId);
            CommentPO byId = commentService.getById(commentPO.getId());
            CommentVo commentVo = commentService.buildCommonVO(byId, userBaseVO, userIdLong);
            return ResultUtils.success(commentVo);
        } else {
            throw new BerryRpcException(ErrorCode.SYSTEM_ERROR);
        }
    }
}
