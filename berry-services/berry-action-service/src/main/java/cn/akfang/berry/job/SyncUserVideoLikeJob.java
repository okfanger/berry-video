package cn.akfang.berry.job;

import cn.akfang.berry.common.model.entity.UserVideoLikePO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.utils.DBBatchUtil;
import cn.akfang.berry.job.consumer.UserVideoLikeCountSyncConsumer;
import cn.akfang.berry.job.consumer.UserVideoLikeSyncConsumer;
import cn.akfang.berry.mapper.VideoMapper;
import cn.akfang.berry.service.impl.UserVideoLikeServiceImpl;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SyncUserVideoLikeJob {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    UserVideoLikeServiceImpl userVideoLikeService;

    @Autowired
    UserVideoLikeSyncConsumer userLikeSyncConsumer;
    @Autowired
    UserVideoLikeCountSyncConsumer userLikeCountSyncConsumer;
    private static final Integer BATCH_SIZE = 500;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {
        log.info("SyncUserVideoLikeJob start");
        List<UserVideoLikePO> userVideoLikePOS = userVideoLikeService.convertDTOsToPOs(userVideoLikeService.getActionDTOs());
        List<VideoPO> userLikeCountDTOs = userVideoLikeService.getCountDTOs().stream().map(item -> {
            VideoPO videoPO = new VideoPO();
            videoPO.setId(item.getToId());
            videoPO.setLikeCount(item.getCount());
            return videoPO;
        }).collect(Collectors.toList());
        log.info("SyncUserVideoLikeJob batchUpdatem, size1={}, size2={}", userVideoLikePOS.size(), userLikeCountDTOs.size());
        log.info("data: {}", JSONUtil.toJsonPrettyStr(userVideoLikePOS));
        log.info("dataCount: {}", JSONUtil.toJsonPrettyStr(userLikeCountDTOs));
        DBBatchUtil.batchUpdate(sqlSessionTemplate, userVideoLikePOS, userVideoLikeService.getBaseMapper(), userLikeSyncConsumer, BATCH_SIZE);
        DBBatchUtil.batchUpdate(sqlSessionTemplate, userLikeCountDTOs, sqlSessionTemplate.getMapper(VideoMapper.class), userLikeCountSyncConsumer, BATCH_SIZE);
        log.info("SyncUserVideoLikeJob end");
    }
}
