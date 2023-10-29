package cn.akfang.berry.service.impl;

import cn.akfang.berry.common.model.entity.UserVideoLikePO;
import cn.akfang.berry.mapper.UserVideoLikeMapper;
import cn.akfang.berry.service.UserVideoLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author fang
 * @description 针对表【t_user_video_like(点赞短视频关联表)】的数据库操作Service实现
 * @createDate 2023-10-29 15:05:55
 */
@Service
public class UserVideoLikeServiceImpl extends ServiceImpl<UserVideoLikeMapper, UserVideoLikePO>
        implements UserVideoLikeService {

}




