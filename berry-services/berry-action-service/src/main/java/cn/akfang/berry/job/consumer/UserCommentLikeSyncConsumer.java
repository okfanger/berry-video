package cn.akfang.berry.job.consumer;

import cn.akfang.berry.common.model.entity.UserCommentLikePO;
import cn.akfang.berry.mapper.UserCommentLikeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class UserCommentLikeSyncConsumer implements BiConsumer<UserCommentLikeMapper, UserCommentLikePO> {
    @Override
    public void accept(UserCommentLikeMapper mapper, UserCommentLikePO item) {
        LambdaQueryWrapper<UserCommentLikePO> qw = new LambdaQueryWrapper<>();

        qw.eq(UserCommentLikePO::getCommentId, item.getCommentId());
        qw.eq(UserCommentLikePO::getUserId, item.getUserId());

        if (mapper.exists(qw)) {
            mapper.update(item, qw);
        } else {
            mapper.insert(item);
        }
    }
}
