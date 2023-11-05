package cn.akfang.berry.job;

import cn.akfang.berry.common.model.dto.ActionCountDTO;
import cn.akfang.berry.common.model.dto.ActionDTO;
import cn.akfang.berry.common.model.entity.CommentPO;
import cn.akfang.berry.common.model.entity.UserCommentLikePO;
import cn.akfang.berry.common.utils.DBBatchUtil;
import cn.akfang.berry.job.consumer.UserCommentLikeCountSyncConsumer;
import cn.akfang.berry.job.consumer.UserCommentLikeSyncConsumer;
import cn.akfang.berry.service.impl.CommentServiceImpl;
import cn.akfang.berry.service.impl.UserCommentLikeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SyncCommentLikeJob {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    UserCommentLikeServiceImpl userCommentLikeService;
    @Autowired
    CommentServiceImpl commentService;

    @Autowired
    UserCommentLikeSyncConsumer userCommentLikeSyncConsumer;
    @Autowired
    UserCommentLikeCountSyncConsumer userCommentLikeCountSyncConsumer;
    private static final Integer BATCH_SIZE = 500;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {
        log.info("SyncCommentLikeJob start");
        // 3. 同步点赞评论
        List<ActionDTO<Long, Long>> commentLikeDTOs = userCommentLikeService.getActionDTOs();
        List<ActionCountDTO<Long>> commentLikeCountDTOs = userCommentLikeService.getCountDTOs();

        List<UserCommentLikePO> userCommentLikePOS = userCommentLikeService.convertDTOsToPOs(commentLikeDTOs);
        List<CommentPO> collect = commentLikeCountDTOs.stream().map(item -> {
            CommentPO commentPO = new CommentPO();
            commentPO.setId(item.getToId());
            commentPO.setLikeCounts(item.getCount());
            return commentPO;
        }).collect(Collectors.toList());
        log.info("SyncCommentLikeJob batchUpdatem, size1={}, size2={}", userCommentLikePOS.size(), collect.size());
        DBBatchUtil.batchUpdate(sqlSessionTemplate, userCommentLikePOS, userCommentLikeService.getBaseMapper(), userCommentLikeSyncConsumer, BATCH_SIZE);
        DBBatchUtil.batchUpdate(sqlSessionTemplate, collect, commentService.getBaseMapper(), userCommentLikeCountSyncConsumer, BATCH_SIZE);
        log.info("SyncCommentLikeJob end");
    }
}
