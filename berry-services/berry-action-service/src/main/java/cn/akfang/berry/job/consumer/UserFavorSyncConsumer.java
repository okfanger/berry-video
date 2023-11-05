package cn.akfang.berry.job.consumer;

import cn.akfang.berry.common.model.entity.UserVideoFavorPO;
import cn.akfang.berry.mapper.UserVideoFavorMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class UserFavorSyncConsumer implements BiConsumer<UserVideoFavorMapper, UserVideoFavorPO> {
    @Override
    public void accept(UserVideoFavorMapper mapper, UserVideoFavorPO item) {
        LambdaQueryWrapper<UserVideoFavorPO> qw = new LambdaQueryWrapper<>();

        qw.eq(UserVideoFavorPO::getVideoId, item.getVideoId());
        qw.eq(UserVideoFavorPO::getUserId, item.getUserId());

        if (mapper.exists(qw)) {
            mapper.update(item, qw);
        } else {
            mapper.insert(item);
        }
    }
}
