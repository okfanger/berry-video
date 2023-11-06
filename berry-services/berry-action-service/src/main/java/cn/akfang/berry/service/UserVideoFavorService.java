package cn.akfang.berry.service;

import cn.akfang.berry.common.model.entity.UserVideoFavorPO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author fang
 * @description 针对表【t_user_video_favor(点赞短视频关联表)】的数据库操作Service
 * @createDate 2023-11-04 15:22:18
 */
public interface UserVideoFavorService extends IService<UserVideoFavorPO> {
    void doFavor(Long userId, Long videoId);

    void unFavor(Long userId, Long videoId);

    Integer getVideoFavorCountFromRedis(Long videoId);

    Boolean isFavored(Long userId, Long videoId);

    List<Long> getFavoredVideoIdsByUserId(Long currentUserId);
}
