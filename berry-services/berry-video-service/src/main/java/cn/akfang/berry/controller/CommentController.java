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
import cn.akfang.berry.service.LikeRedisService;
import cn.akfang.berry.service.VideoService;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private VideoService videoService;

    @Autowired
    @Qualifier("commentLikeRedisService")
    private LikeRedisService<Long, Long> likeRedisService;


    @GetMapping("/doLike")
    public BaseResponse<Boolean> doLike(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                        @RequestParam("commentId") String commentIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        synchronized (userIdStr.intern()) {
            Long commentId = NumberUtil.parseLong(commentIdStr);
            if (!likeRedisService.isLiked(userId, commentId)) {
                likeRedisService.saveLiked2Redis(userId, commentId);
                likeRedisService.incrementLikedCount(commentId);
            }
            return ResultUtils.success(null);
        }
    }

    @GetMapping("/unLike")
    public BaseResponse<Boolean> doUnLike(@RequestHeader(AuthConstants.EXCHANGE_AUTH_HEADER) String userIdStr,
                                          @RequestParam("commentId") String commentIdStr) {
        Long userId = NumberUtil.parseLong(userIdStr);
        synchronized (userIdStr.intern()) {
            Long commentId = NumberUtil.parseLong(commentIdStr);
            if (likeRedisService.isLiked(userId, commentId)) {
                likeRedisService.unlikeFromRedis(userId, commentId);
                likeRedisService.decrementLikedCount(commentId);
            }
            return ResultUtils.success(null);
        }
    }

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

    @GetMapping("/feed")
    public BaseResponse<Page<CommentPO>> commentFeedList(@RequestParam("videoId") String videoId,
                                                         @RequestParam("orderBy") String orderBy, @RequestParam("current") Long currentPage) {
        currentPage = currentPage <= 0 ? currentPage : 1L;
        LambdaQueryWrapper<CommentPO> qw = new QueryWrapper<CommentPO>()
                .lambda()
                .select(CommentPO::getId, CommentPO::getAuthorId, CommentPO::getContent, CommentPO::getCreateTime)
                .eq(CommentPO::getVideoId, videoId);
        if (StrUtil.equals("time", orderBy)) {
            qw = qw.orderByDesc(CommentPO::getCreateTime);
        } else {
            qw = qw.orderByDesc(CommentPO::getLikeCounts);
        }
        Page<CommentPO> commentPOPage = commentService.page(new Page<>(currentPage, 10), qw);
        return ResultUtils.success(commentPOPage);
    }
}
