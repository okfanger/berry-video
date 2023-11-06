package cn.akfang.berry.mq;

import cn.akfang.berry.common.enums.VideoVisibleEnum;
import cn.akfang.berry.common.feign.client.MiscClient;
import cn.akfang.berry.common.model.dto.VideoSaveDTO;
import cn.akfang.berry.common.model.entity.ChannelVideoPO;
import cn.akfang.berry.common.model.entity.FilePO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.constant.VideoMessageConstants;
import cn.akfang.berry.service.ChannelVideoService;
import cn.akfang.berry.service.VideoService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = {VideoMessageConstants.VIDEO_SAVE_QUEUE})
@Slf4j
public class VideoPublishReceiver {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    VideoService videoService;

    @Autowired
    ChannelVideoService channelVideoService;

    @Autowired
    MiscClient miscClient;

    @RabbitHandler
    public Boolean process(VideoSaveDTO dto) {
        log.info("receive video save message: {}", JSONUtil.toJsonStr(dto));

        // cache
        FilePO filePO = miscClient.getFileById(dto.getFileId());
        if (ObjectUtil.isNull(filePO)) {
            log.error("file not found: {}", JSONUtil.toJsonStr(dto));
            throw new RuntimeException("file not found");
        }

        VideoPO videoPO = new VideoPO();
        videoPO.setContent(dto.getContent());
        videoPO.setFileId(dto.getFileId());
        videoPO.setAuthorId(dto.getAuthorId());
        videoPO.setVisible(VideoVisibleEnum.getPendingEnum(dto.getVisible()).getCode());
        videoPO.setCommentCount(0);
        videoPO.setLikeCount(0);

        Map metadata = JSONUtil.toBean(filePO.getMetadata(), Map.class);
        videoPO.setDefaultUrl(String.valueOf(metadata.get("m3u8")));
        videoPO.setCover(String.valueOf(metadata.get("jpg")));

        boolean saveVideo = videoService.save(videoPO);
        if (!saveVideo) {
            log.error("save video failed: {}", JSONUtil.toJsonStr(dto));
            throw new RuntimeException("save video failed");
        }

        // channel_video
        ChannelVideoPO channelVideoPO = new ChannelVideoPO();
        channelVideoPO.setVideoId(videoPO.getId());
        channelVideoPO.setChannelId(dto.getChannelId());


        return channelVideoService.save(channelVideoPO);
    }
}
