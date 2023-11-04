package cn.akfang.berry.service;

import cn.akfang.berry.common.model.entity.UserCommentLikePO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author fang
 * @description 针对表【t_user_comment_like(点赞短视频关联表)】的数据库操作Service
 * @createDate 2023-11-04 16:40:45
 */
public interface UserCommentLikeService extends IService<UserCommentLikePO> {

    Integer getLikeCount(Long commentId);

    Boolean isLiked(Long userId, Long commentId);

    void doLike(Long userId, Long commentId);

    void unLike(Long userId, Long commentId);
}
