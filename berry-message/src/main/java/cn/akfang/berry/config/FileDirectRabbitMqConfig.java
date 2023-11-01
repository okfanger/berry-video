package cn.akfang.berry.config;

import cn.akfang.berry.constant.QiniuMessageConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileDirectRabbitMqConfig {
    @Bean
    DirectExchange qiniuCallbackExchange() {
        return new DirectExchange(QiniuMessageConstants.QINIU_CALLBACK_EXCHANGE, true, false);
    }

    @Bean
    Queue qiniuCallbackUploadedQueue() {
        return new Queue(QiniuMessageConstants.QINIU_CALLBACK_UPLOADED_QUEUE, true, false, false);
    }

    @Bean
    Queue qiniuCallbackTransformQueue() {
        return new Queue(QiniuMessageConstants.QINIU_CALLBACK_TRANSFORM_QUEUE, true, false, false);
    }


    @Bean
    Binding fileVideoUploadBinding() {
        return BindingBuilder.bind(qiniuCallbackUploadedQueue())
                .to(qiniuCallbackExchange())
                .with(QiniuMessageConstants.QINIU_CALLBACK_UPLOADED_ROUTING_KEY);
    }

    @Bean
    Binding fileVideoTransformBinding() {
        return BindingBuilder.bind(qiniuCallbackTransformQueue())
                .to(qiniuCallbackExchange())
                .with(QiniuMessageConstants.QINIU_CALLBACK_TRANSFORM_ROUTING_KEY);
    }
}
