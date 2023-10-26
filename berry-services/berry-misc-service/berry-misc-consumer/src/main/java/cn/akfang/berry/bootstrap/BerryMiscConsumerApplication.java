package cn.akfang.berry.bootstrap;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
        scanBasePackages = {"cn.akfang.berry"},
        exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@DubboComponentScan(basePackages = {"cn.akfang.berry.consumer"})
public class BerryMiscConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BerryMiscConsumerApplication.class, args);
    }
}
