package cn.akfang.berry.mq;

import cn.akfang.berry.common.model.entity.FilePO;
import cn.akfang.berry.common.model.mq.QiniuTransformCallBackBodyDTO;
import cn.akfang.berry.constant.QiniuMessageConstants;
import cn.akfang.berry.service.FileService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    @RabbitHandler
    public void process(QiniuTransformCallBackBodyDTO messageBody) {
        log.info("VideoTransformCallbackReceiver: {}", JSONUtil.toJsonStr(messageBody));
        String sourceKey = messageBody.getInputKey();
        Map<String, String> metadata = new HashMap<>();
        messageBody.getItems().forEach((item) -> {
            if (StrUtil.endWith(item.getKey().trim(), ".mp4")) {
                metadata.put("mp4", item.getKey());
            } else if (StrUtil.endWith(item.getKey().trim(), ".m3u8")) {
                metadata.put("m3u8", item.getKey());
            } else if (StrUtil.endWith(item.getKey().trim(), ".jpg")) {
                metadata.put("jpg", item.getKey());
            }
        });

        boolean update = new LambdaUpdateChainWrapper<>(fileService.getBaseMapper())
                .eq(FilePO::getKey, sourceKey)
                .set(FilePO::getMetadata, JSONUtil.toJsonStr(metadata))
                .update();
        if (!update) {
            log.error("filePO updated failed: {}", JSONUtil.toJsonStr(messageBody));
            throw new RuntimeException("filePO updated failed");
        } else {
            log.info("filePO updated success: {}", JSONUtil.toJsonStr(messageBody));
        }
    }
}
