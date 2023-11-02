package cn.akfang.berry.mq;

import cn.akfang.berry.common.feign.client.MiscClient;
import cn.akfang.berry.common.model.dto.VideoTransformMessageDTO;
import cn.akfang.berry.common.model.entity.VideoPO;
import cn.akfang.berry.constant.VideoMessageConstants;
import cn.akfang.berry.service.ChannelVideoService;
import cn.akfang.berry.service.VideoService;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = {VideoMessageConstants.VIDEO_TRANSFORM_QUEUE})
@Slf4j
public class VideoTransformReceiver {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    VideoService videoService;

    @Autowired
    ChannelVideoService channelVideoService;

    @Autowired
    MiscClient miscClient;

    @RabbitHandler
    public void process(VideoTransformMessageDTO dto) {
        log.info("receive video transform message: {}", JSONUtil.toJsonStr(dto));
        videoService.lambdaUpdate()
                .eq(VideoPO::getFileId, dto.getFileId())
                .set(VideoPO::getDefaultUrl, dto.getDefaultKey())
                .set(VideoPO::getCover, dto.getCover())
                .update();
    }
}
