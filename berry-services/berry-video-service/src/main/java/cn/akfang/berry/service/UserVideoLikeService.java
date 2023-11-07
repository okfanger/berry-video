package cn.akfang.berry.service;

import cn.akfang.berry.common.model.entity.UserVideoLikePO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author fang
 * @description 针对表【t_user_video_like(点赞短视频关联表)】的数据库操作Service
 * @createDate 2023-10-29 15:05:55
 */
public interface UserVideoLikeService extends IService<UserVideoLikePO> {
    void doLike(Long userId, Long videoId);

    void unLike(Long userId, Long videoId);

    Integer getVideoLikedCountFromRedis(Long videoId);

    Boolean isLiked(Long userId, Long videoId);
}
