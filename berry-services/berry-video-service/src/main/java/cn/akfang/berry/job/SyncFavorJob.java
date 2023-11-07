package cn.akfang.berry.job;

import cn.akfang.berry.common.model.dto.ActionCountDTO;
import cn.akfang.berry.common.model.dto.ActionDTO;
import cn.akfang.berry.common.model.entity.UserVideoFavorPO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.common.utils.DBBatchUtil;
import cn.akfang.berry.job.consumer.UserFavorCountSyncConsumer;
import cn.akfang.berry.job.consumer.UserFavorSyncConsumer;
import cn.akfang.berry.service.impl.UserVideoFavorServiceImpl;
import cn.akfang.berry.service.impl.VideoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SyncFavorJob {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    UserVideoFavorServiceImpl userVideoFavorService;
    @Autowired
    VideoServiceImpl videoService;
    private static final Integer BATCH_SIZE = 500;

    @Autowired
    UserFavorCountSyncConsumer userFavorCountSyncConsumer;
    @Autowired
    UserFavorSyncConsumer userFavorSyncConsumer;


    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() {
        log.info("SyncFavorJob start");
        // 2. 同步收藏视频
        List<ActionDTO<Long, Long>> favorDTOs = userVideoFavorService.getActionDTOs();
        List<ActionCountDTO<Long>> favorCountDTOs = userVideoFavorService.getCountDTOs();

        List<UserVideoFavorPO> userVideoFavorPOS = userVideoFavorService.convertDTOsToPOs(favorDTOs);
        List<VideoPO> collect = favorCountDTOs.stream().map(item -> {
            VideoPO videoPO = new VideoPO();
            videoPO.setId(item.getToId());
            videoPO.setLikeCount(item.getCount());
            return videoPO;
        }).collect(Collectors.toList());
        log.info("SyncFavorJob batchUpdatem, size1={}, size2={}", userVideoFavorPOS.size(), collect.size());
        DBBatchUtil.batchUpdate(sqlSessionTemplate, userVideoFavorPOS, userVideoFavorService.getBaseMapper(), userFavorSyncConsumer, BATCH_SIZE);
        DBBatchUtil.batchUpdate(sqlSessionTemplate, collect, videoService.getBaseMapper(), userFavorCountSyncConsumer, BATCH_SIZE);
        log.info("SyncFavorJob end");
    }
}
