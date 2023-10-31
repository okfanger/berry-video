package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.enums.LikeStatusEnum;
import cn.akfang.berry.common.enums.LikeTypeEnum;
import cn.akfang.berry.service.LikeRedisService;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("videoLikeRedisService")
public class VideoLikeRedisServiceImpl implements LikeRedisService<Long, Long> {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private String buildKey(Long fromId, Long toId) {
        return fromId + "::" + toId;
    }

    @Override
    public void saveLiked2Redis(Long fromId, Long toId) {
        redisTemplate.opsForHash().put(
                LikeTypeEnum.VIDEO.getRedisKey(),
                buildKey(fromId, toId),
                LikeStatusEnum.LIKE.getCode()
        );
    }

    @Override
    public void unlikeFromRedis(Long fromId, Long toId) {
        redisTemplate.opsForHash().put(
                LikeTypeEnum.VIDEO.getRedisKey(),
                buildKey(fromId, toId),
                LikeStatusEnum.UNLIKE.getCode()
        );
    }

    @Override
    public void deleteLikedFromRedis(Long fromId, Long toId) {
        redisTemplate.opsForHash().delete(
                LikeTypeEnum.VIDEO.getRedisKey(),
                buildKey(fromId, toId)
        );
    }

    @Override
    public void incrementLikedCount(Long toId) {
        redisTemplate.opsForHash().increment(
                LikeTypeEnum.VIDEO.getRedisHashCountKey(),
                String.valueOf(toId),
                1
        );
    }

    @Override
    public void decrementLikedCount(Long toId) {
        redisTemplate.opsForHash().increment(
                LikeTypeEnum.VIDEO.getRedisHashCountKey(),
                String.valueOf(toId),
                -1
        );
    }

    @Override
    public Integer getLikedCount(Long toId) {
        return Optional.ofNullable(
                (Integer) redisTemplate.opsForHash().get(
                        LikeTypeEnum.VIDEO.getRedisHashCountKey(),
                        String.valueOf(toId)
                )
        ).orElse(0);
    }

    @Override
    public Boolean isLiked(Long fromId, Long toId) {
        Optional<Integer> valueOpt = Optional.ofNullable(
                (Integer) redisTemplate.opsForHash().get(
                        LikeTypeEnum.VIDEO.getRedisKey(),
                        buildKey(fromId, toId)
                )
        );
        return valueOpt.map(integer -> ObjectUtil.equal(integer, LikeStatusEnum.LIKE.getCode()))
                .orElse(false);
    }
}
