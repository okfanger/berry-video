package cn.akfang.berry.job.consumer;

import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.mapper.VideoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class UserVideoLikeCountSyncConsumer implements BiConsumer<VideoMapper, VideoPO> {
    @Override
    public void accept(VideoMapper mapper, VideoPO item) {
        QueryWrapper<VideoPO> qw = new QueryWrapper<>();
        qw.eq("id", item.getId());
        if (mapper.exists(qw)) {
            mapper.update(item, qw);
        } else {
            mapper.insert(item);
        }
    }
}
