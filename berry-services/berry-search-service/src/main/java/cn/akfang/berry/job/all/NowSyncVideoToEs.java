package cn.akfang.berry.job.all;

import cn.akfang.berry.common.constants.EsConstants;
import cn.akfang.berry.common.feign.client.VideoClient;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.mapper.VideoEsMapper;
import cn.akfang.berry.model.VideoEsPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class NowSyncVideoToEs implements CommandLineRunner {
    private static final Integer PER_NUM = 500;

    @Autowired
    private VideoEsMapper videoEsMapper;

    @Autowired
    private VideoClient videoClient;

    @Override
    public void run(String... args) throws Exception {
        if (videoEsMapper.existsIndex(EsConstants.VIDEO_INDEX)) {
            return;
        } else {
            videoEsMapper.createIndex(EsConstants.VIDEO_INDEX);
            sync();
        }
    }

    private void sync() {
        log.info("[video]全量同步开始");
        List<Long> ids = videoClient.listAllIds();
        for (int i = 0; i < ids.size(); i += PER_NUM) {
            List<Long> idList = ids.subList(i, Math.min(i + PER_NUM, ids.size()));
            List<VideoPO> videoEsPOS = videoClient.listByIds(idList);
            videoEsMapper.insertBatch(videoEsPOS.stream().map((item) -> {
                VideoEsPO videoEsPO = new VideoEsPO();
                videoEsPO.setId(String.valueOf(item.getId()));
                videoEsPO.setVideoId(item.getId());
                videoEsPO.setContent(item.getContent());
                return videoEsPO;
            }).collect(java.util.stream.Collectors.toList()));
        }
        log.info("全量同步结束");
    }
}
