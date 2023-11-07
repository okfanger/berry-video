package cn.akfang.berry.job.consumer;

import cn.akfang.berry.common.model.entity.CommentPO;
import cn.akfang.berry.mapper.CommentMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Component
public class UserCommentLikeCountSyncConsumer implements BiConsumer<CommentMapper, CommentPO> {
    @Override
    public void accept(CommentMapper mapper, CommentPO item) {
        QueryWrapper<CommentPO> qw = new QueryWrapper<>();
        qw.eq("videoId", item.getVideoId());
        if (mapper.exists(qw)) {
            mapper.update(item, qw);
        } else {
            mapper.insert(item);
        }
    }
}
