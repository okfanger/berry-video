package cn.akfang.berry.mq;

import cn.akfang.berry.common.model.entity.FilePO;
import cn.akfang.berry.common.model.mq.VideoUploadedCallbackBodyDTO;
import cn.akfang.berry.constant.QiniuMessageConstants;
import cn.akfang.berry.service.FileService;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = {QiniuMessageConstants.QINIU_CALLBACK_UPLOADED_QUEUE})
@Slf4j
public class FileVideoUploadedCallbackReceiver {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    FileService fileService;

    @RabbitHandler
    public Long process(VideoUploadedCallbackBodyDTO messageBody) {
        log.info("video uploaded callback receiver: {}", messageBody);
        FilePO filePO = new FilePO();
        BeanUtil.copyProperties(messageBody, filePO);
        boolean save = fileService.save(filePO);
        if (!save) {
            log.error("filePO created failed: {}", messageBody);
            throw new RuntimeException("file created failed");
        }
        log.info("filePO created success: videoId={}", filePO.getId());
        return filePO.getId();
    }
}
