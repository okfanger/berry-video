package cn.akfang.berry.config;

import cn.akfang.berry.constant.VideoMessageConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoDirectRabbitMqConfig {
    @Bean
    DirectExchange videoExchange() {
        return new DirectExchange(VideoMessageConstants.VIDEO_EXCHANGE, true, false);
    }

    @Bean
    Queue videoSaveQueue() {
        return new Queue(VideoMessageConstants.VIDEO_SAVE_QUEUE, true, false, false);
    }

    @Bean
    Queue videoTransformQueue() {
        return new Queue(VideoMessageConstants.VIDEO_TRANSFORM_QUEUE, true, false, false);
    }

    @Bean
    Binding videoSaveBinding() {
        return BindingBuilder.bind(videoSaveQueue())
                .to(videoExchange())
                .with(VideoMessageConstants.VIDEO_SAVE_ROUTING_KEY);
    }

    @Bean
    Binding videoTransformBinding() {
        return BindingBuilder.bind(videoTransformQueue())
                .to(videoExchange())
                .with(VideoMessageConstants.VIDEO_TRANSFORM_ROUTING_KEY);
    }

}
