package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.enums.LikeTypeEnum;
import cn.akfang.berry.common.model.dto.ActionDTO;
import cn.akfang.berry.common.model.entity.UserVideoLikePO;
import cn.akfang.berry.mapper.UserVideoLikeMapper;
import cn.akfang.berry.service.UserVideoLikeService;
import cn.akfang.berry.service.base.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fang
 * @description 针对表【t_user_video_like(点赞短视频关联表)】的数据库操作Service实现
 * @createDate 2023-10-29 15:05:55
 */
@Service
public class UserVideoLikeServiceImpl extends ActionService<Long, Long, UserVideoLikeMapper, UserVideoLikePO>
        implements UserVideoLikeService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    protected RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    protected LikeTypeEnum getLikeTypeEnum() {
        return LikeTypeEnum.VIDEO;
    }

    @Override
    public UserVideoLikeMapper getBaseMapper() {
        return baseMapper;
    }

    @Override
    public List<UserVideoLikePO> convertDTOsToPOs(List<ActionDTO<Long, Long>> actionDTOs) {
        return actionDTOs.stream().map(item -> {
            UserVideoLikePO userVideoLikePO = new UserVideoLikePO();
            userVideoLikePO.setVideoId(item.getToId());
            userVideoLikePO.setUserId(item.getFromId());
            userVideoLikePO.setStatus(item.getStatus());
            return userVideoLikePO;
        }).collect(Collectors.toList());
    }

    @Override
    public void doLike(Long userId, Long videoId) {
        super.saveActionToRedis(userId, videoId);
    }

    @Override
    public void unLike(Long userId, Long videoId) {
        super.saveUnActionToRedis(userId, videoId);
    }

    @Override
    public Integer getVideoLikedCountFromRedis(Long videoId) {
        return super.getActionCountFromRedis(videoId);
    }

    @Override
    public Boolean isLiked(Long userId, Long videoId) {
        return super.isAction(userId, videoId);
    }

    @Override
    public List<Long> getLikedVideoIdsByUserId(Long userId) {
        return super.getActionedToIds(userId).stream()
                .map(Long::parseLong).collect(Collectors.toList());
    }
}




