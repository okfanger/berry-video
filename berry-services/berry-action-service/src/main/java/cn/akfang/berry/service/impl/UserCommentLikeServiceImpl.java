package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.enums.LikeTypeEnum;
import cn.akfang.berry.common.model.dto.ActionDTO;
import cn.akfang.berry.common.model.entity.UserCommentLikePO;
import cn.akfang.berry.mapper.UserCommentLikeMapper;
import cn.akfang.berry.service.UserCommentLikeService;
import cn.akfang.berry.service.base.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fang
 * @description 针对表【t_user_comment_like(点赞短视频关联表)】的数据库操作Service实现
 * @createDate 2023-11-04 16:40:45
 */
@Service
public class UserCommentLikeServiceImpl extends ActionService<Long, Long, UserCommentLikeMapper, UserCommentLikePO>
        implements UserCommentLikeService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Integer getLikeCount(Long commentId) {
        return super.getActionCountFromRedis(commentId);
    }

    @Override
    public Boolean isLiked(Long userId, Long commentId) {
        return super.isAction(userId, commentId);
    }

    @Override
    public void doLike(Long userId, Long commentId) {
        super.saveActionToRedis(userId, commentId);
    }

    @Override
    public void unLike(Long userId, Long commentId) {
        super.saveUnActionToRedis(userId, commentId);
    }

    @Override
    protected RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    protected LikeTypeEnum getLikeTypeEnum() {
        return LikeTypeEnum.COMMENT;
    }

    @Override
    public UserCommentLikeMapper getBaseMapper() {
        return baseMapper;
    }

    @Override
    public List<UserCommentLikePO> convertDTOsToPOs(List<ActionDTO<Long, Long>> actionDTOs) {
        return actionDTOs.stream().map(item -> {
            UserCommentLikePO userCommentLikePO = new UserCommentLikePO();
            userCommentLikePO.setUserId(item.getFromId());
            userCommentLikePO.setCommentId(item.getToId());
            userCommentLikePO.setStatus(item.getStatus());
            return userCommentLikePO;
        }).collect(Collectors.toList());
    }
}




