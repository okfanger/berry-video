package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.model.dto.CommentAddDTO;
import cn.akfang.berry.common.model.entity.CommentPO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.model.response.CommentVo;
import cn.akfang.berry.common.model.response.UserBaseVO;
import cn.akfang.berry.mapper.CommentMapper;
import cn.akfang.berry.service.CommentService;
import cn.akfang.berry.service.UserCommentLikeService;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fang
 * @description 针对表【t_comment(评论表)】的数据库操作Service实现
 * @createDate 2023-10-30 17:53:47
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, CommentPO>
        implements CommentService {

    @Autowired
    UserCommentLikeService userCommentLikeService;


    @Override
    public CommentVo buildCommonVO(CommentPO commentPO, UserBaseVO userBaseVO, Long userId) {
        CommentVo commentVo = new CommentVo();
        commentVo.setId(commentPO.getId());
        commentVo.setAuthor(userBaseVO);
        commentVo.setContent(commentPO.getContent());
        commentVo.setCreateTime(commentPO.getCreateTime());
        commentVo.setLikeCount(userCommentLikeService.getLikeCount(commentPO.getId()));
//        log.debug("commentId: {}, likeCount: {}", commentPO.getId(), commentVo.getLikeCount());
        commentVo.setIsLiked(userCommentLikeService.isLiked(userId, commentPO.getId()));
        return commentVo;
    }

    @Override
    public boolean addComment(VideoPO videoPO, Long userId, CommentAddDTO commentAddDTO) {
        CommentPO newComment = CommentPO.builder()
                .commentUserId(userId)
                .content(commentAddDTO.getContent())
                .videoId(commentAddDTO.getVideoId())
                .authorId(videoPO.getAuthorId())
                .likeCounts(0)
                .build();
        return save(newComment);
    }
    @Override
    public Wrapper<CommentPO> getFeedQueryWrapper(Long videoId, String orderBy) {
        LambdaQueryWrapper<CommentPO> qw = new QueryWrapper<CommentPO>()
                .lambda()
                .eq(CommentPO::getVideoId, videoId);
        if (StrUtil.equals("time", orderBy)) {
            qw = qw.orderByDesc(CommentPO::getCreateTime);
        } else {
            qw = qw.orderByDesc(CommentPO::getLikeCounts);
        }
        return qw;
    }

    @Override
    public Integer getCommentCountByVideoId(Long videoId) {
        return Math.toIntExact(new LambdaQueryChainWrapper<>(baseMapper)
                .select(CommentPO::getId)
                .eq(CommentPO::getVideoId, videoId)
                .count());
    }
}




