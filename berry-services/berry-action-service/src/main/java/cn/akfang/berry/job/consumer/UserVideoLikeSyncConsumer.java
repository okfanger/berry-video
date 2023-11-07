package cn.akfang.berry.job.consumer;

import cn.akfang.berry.common.model.entity.UserVideoLikePO;
import cn.akfang.berry.mapper.UserVideoLikeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class UserVideoLikeSyncConsumer implements BiConsumer<UserVideoLikeMapper, UserVideoLikePO> {
    @Override
    public void accept(UserVideoLikeMapper mapper, UserVideoLikePO item) {
        LambdaQueryWrapper<UserVideoLikePO> qw = new LambdaQueryWrapper<>();

        qw.eq(UserVideoLikePO::getVideoId, item.getVideoId());
        qw.eq(UserVideoLikePO::getUserId, item.getUserId());

        if (mapper.exists(qw)) {
            mapper.update(item, qw);
        } else {
            mapper.insert(item);
        }
    }
}
