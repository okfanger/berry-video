package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.enums.LikeTypeEnum;
import cn.akfang.berry.common.model.dto.ActionDTO;
import cn.akfang.berry.common.model.entity.UserVideoFavorPO;
import cn.akfang.berry.mapper.UserVideoFavorMapper;
import cn.akfang.berry.service.UserVideoFavorService;
import cn.akfang.berry.service.base.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fang
 * @description 针对表【t_user_video_favor(点赞短视频关联表)】的数据库操作Service实现
 * @createDate 2023-11-04 15:22:18
 */
@Service
public class UserVideoFavorServiceImpl extends ActionService<Long, Long, UserVideoFavorMapper, UserVideoFavorPO>
        implements UserVideoFavorService {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @Override
    protected RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    protected LikeTypeEnum getLikeTypeEnum() {
        return LikeTypeEnum.FAVOR;
    }

    @Override
    public UserVideoFavorMapper getBaseMapper() {
        return baseMapper;
    }

    @Override
    public List<UserVideoFavorPO> convertDTOsToPOs(List<ActionDTO<Long, Long>> actionDTOs) {
        return actionDTOs.stream().map(item -> {
            UserVideoFavorPO userVideoFavorPO = new UserVideoFavorPO();
            userVideoFavorPO.setUserId(item.getFromId());
            userVideoFavorPO.setVideoId(item.getToId());
            userVideoFavorPO.setStatus(item.getStatus());
            return userVideoFavorPO;
        }).collect(Collectors.toList());
    }

    @Override
    public void doFavor(Long userId, Long videoId) {
        super.saveActionToRedis(userId, videoId);
    }

    @Override
    public void unFavor(Long userId, Long videoId) {
        super.saveUnActionToRedis(userId, videoId);
    }

    @Override
    public Integer getVideoFavorCountFromRedis(Long videoId) {
        return super.getActionCountFromRedis(videoId);
    }

    @Override
    public Boolean isFavored(Long userId, Long videoId) {
        return super.isAction(userId, videoId);
    }
}




