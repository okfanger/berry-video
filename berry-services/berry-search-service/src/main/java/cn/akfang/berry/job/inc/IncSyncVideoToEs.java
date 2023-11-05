package cn.akfang.berry.job.inc;

import cn.akfang.berry.common.feign.client.VideoClient;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.mapper.VideoEsMapper;
import cn.akfang.berry.model.VideoEsPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IncSyncVideoToEs {


    @Autowired
    private VideoEsMapper videoEsMapper;

    @Autowired
    private VideoClient videoClient;

    private static final Integer PER_NUM = 100;

    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        // 获取两分钟前有更新记录的视频
        List<Long> longs;
        try {
            longs = videoClient.listIdsMinutesAgo(1L);
            if (longs == null || longs.isEmpty()) {
                return;
            }
        } catch (Exception ignore) {
            return;
        }
        log.info("video need sync num: {}", longs.size());
        for (int i = 0; i < longs.size(); i += PER_NUM) {
            List<Long> ids = longs.subList(i, Math.min(i + PER_NUM, longs.size()));
            List<VideoPO> videoPOS = videoClient.listByIds(ids);
            List<VideoEsPO> collect = videoPOS.stream().map((item) -> {
                VideoEsPO videoEsPO = new VideoEsPO();
                videoEsPO.setId(String.valueOf(item.getId()));
                videoEsPO.setVideoId(item.getId());
                videoEsPO.setContent(item.getContent());
                return videoEsPO;
            }).collect(Collectors.toList());
            videoEsMapper.insertBatch(collect);
        }
        log.info("video sync job Es2Db end");
    }
}
