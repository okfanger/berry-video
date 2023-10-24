package cn.akfang.berry.consumer;

import cn.akfang.berry.service.VideoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideoConsumer {
    @DubboReference
    VideoService videoService;
}
