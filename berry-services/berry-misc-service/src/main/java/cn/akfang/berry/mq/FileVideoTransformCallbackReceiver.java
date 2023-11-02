package cn.akfang.berry.mq;

import cn.akfang.berry.common.model.dto.VideoTransformMessageDTO;
import cn.akfang.berry.common.model.entity.FilePO;
import cn.akfang.berry.common.model.mq.QiniuTransformCallBackBodyDTO;
import cn.akfang.berry.constant.QiniuMessageConstants;
import cn.akfang.berry.constant.VideoMessageConstants;
import cn.akfang.berry.service.FileService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RabbitListener(queues = {QiniuMessageConstants.QINIU_CALLBACK_TRANSFORM_QUEUE})
@Slf4j
public class FileVideoTransformCallbackReceiver {

    @Autowired
    FileService fileService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void process(QiniuTransformCallBackBodyDTO messageBody) {
        log.info("VideoTransformCallbackReceiver: {}", JSONUtil.toJsonStr(messageBody));
        String sourceKey = messageBody.getInputKey();
        FilePO filePO = fileService.getByKey(sourceKey);
        Map<String, String> metadata = new HashMap<>();
        VideoTransformMessageDTO videoTransformMessageDTO = new VideoTransformMessageDTO();
        videoTransformMessageDTO.setSourceKey(sourceKey);
        videoTransformMessageDTO.setFileId(filePO.getId());
        messageBody.getItems().forEach((item) -> {
            if (StrUtil.endWith(item.getKey().trim(), ".mp4")) {
                metadata.put("mp4", item.getKey());
            } else if (StrUtil.endWith(item.getKey().trim(), ".m3u8")) {
                videoTransformMessageDTO.setDefaultKey(item.getKey());
                metadata.put("m3u8", item.getKey());
            } else if (StrUtil.endWith(item.getKey().trim(), ".jpg")) {
                videoTransformMessageDTO.setCover(item.getKey());
                metadata.put("jpg", item.getKey());
            }
        });

        boolean update = new LambdaUpdateChainWrapper<>(fileService.getBaseMapper())
                .eq(FilePO::getKey, sourceKey)
                .set(FilePO::getMetadata, JSONUtil.toJsonStr(metadata))
                .update();

        rabbitTemplate.convertAndSend(VideoMessageConstants.VIDEO_EXCHANGE,
                VideoMessageConstants.VIDEO_TRANSFORM_ROUTING_KEY, videoTransformMessageDTO);

        if (!update) {
            log.error("filePO updated failed: {}", JSONUtil.toJsonStr(messageBody));
            throw new RuntimeException("filePO updated failed");
        } else {
            log.info("filePO updated success: {}", JSONUtil.toJsonStr(messageBody));
        }
    }
}
