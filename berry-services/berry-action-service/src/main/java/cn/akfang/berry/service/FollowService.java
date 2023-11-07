package cn.akfang.berry.service;

import cn.akfang.berry.common.model.entity.FollowPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author fang
 * @description 针对表【t_follow(粉丝表】的数据库操作Service
 * @createDate 2023-11-05 23:56:30
 */
public interface FollowService extends IService<FollowPO> {

    void doFollow(Long userId, Long followedUserId);

    void unFollow(Long userId, Long followedUserId);

    Integer getFollowCountFromRedis(Long userId);

    Integer getFansCountFromRedis(Long userId);

    Boolean isFollowed(Long userId, Long followedUserId);

    Boolean isFriend(Long userId, Long followedUserId);

    Boolean isFan(Long suspectedFanUserId, Long followedUserId);

    List<Long> getFansIdByUserId(Long userId);

    List<Long> getFollowsIdByUserId(Long userId);

    List<Long> buildUserIds(List<Long> ids, String userIdStr);
}
