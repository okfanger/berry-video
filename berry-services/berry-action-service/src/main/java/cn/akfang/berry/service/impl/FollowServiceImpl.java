package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.enums.LikeTypeEnum;
import cn.akfang.berry.common.model.dto.ActionDTO;
import cn.akfang.berry.common.model.entity.FollowPO;
import cn.akfang.berry.mapper.FollowMapper;
import cn.akfang.berry.service.FollowService;
import cn.akfang.berry.service.base.ActionService;
import cn.hutool.core.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fang
 * @description 针对表【t_follow(粉丝表)】的数据库操作Service实现
 * @createDate 2023-11-05 23:56:30
 */
@Service
public class FollowServiceImpl extends ActionService<Long, Long, FollowMapper, FollowPO>
        implements FollowService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    protected RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    protected LikeTypeEnum getLikeTypeEnum() {
        return LikeTypeEnum.FOLLOW;
    }

    @Override
    public FollowMapper getBaseMapper() {
        return baseMapper;
    }

    @Override
    @Deprecated
    public List<FollowPO> convertDTOsToPOs(List<ActionDTO<Long, Long>> actionDTOs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doFollow(Long userId, Long followedUserId) {
        super.saveActionToRedis(userId, followedUserId);
        redisTemplate.opsForHash().increment(
                LikeTypeEnum.FOLLOW.getRedisHashCountKey() + "_FANS",
                String.valueOf(userId),
                1
        );
    }

    @Override
    public void unFollow(Long userId, Long followedUserId) {
        super.saveUnActionToRedis(userId, followedUserId);
        redisTemplate.opsForHash().increment(
                LikeTypeEnum.FOLLOW.getRedisHashCountKey() + "_FANS",
                String.valueOf(userId),
                -1
        );
    }

    @Override
    public Integer getFollowCountFromRedis(Long userId) {
        return super.getActionCountFromRedis(userId);
    }

    @Override
    public Integer getFansCountFromRedis(Long userId) {
        return Optional.ofNullable(
                (Integer) redisTemplate.opsForHash().get(
                        LikeTypeEnum.FOLLOW.getRedisHashCountKey() + "_FANS",
                        String.valueOf(userId)
                )
        ).orElse(0);
    }

    @Override
    public Boolean isFollowed(Long userId, Long followedUserId) {
        return super.isAction(userId, followedUserId);
    }

    @Override
    public Boolean isFriend(Long userId, Long followedUserId) {
        return isFollowed(userId, followedUserId) && isFollowed(followedUserId, userId);
    }

    @Override
    public Boolean isFan(Long suspectedFanUserId, Long followedUserId) {
        return isFollowed(suspectedFanUserId, followedUserId);
    }

    @Override
    public List<Long> getFansIdByUserId(Long userId) {
        return super.getActionedFromIds(userId).stream()
                .map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public List<Long> getFollowsIdByUserId(Long userId) {
        return super.getActionedToIds(userId).stream()
                .map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public List<Long> buildUserIds(List<Long> ids, String userIdStr) {
        // 分页
        int current = NumberUtil.parseInt(userIdStr);
        if (current < 1) {
            current = 1;
        }
        int size = 10;
        int fromIndex = (current - 1) * size;
        int toIndex = current * size;
        if (ids.size() < fromIndex) {
            return Collections.emptyList();
        }
        if (ids.size() < toIndex) {
            toIndex = ids.size();
        }
        return ids.subList(fromIndex, toIndex);
    }
}




