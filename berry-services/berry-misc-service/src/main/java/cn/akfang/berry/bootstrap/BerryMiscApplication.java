package cn.akfang.berry.bootstrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
        scanBasePackages = {"cn.akfang.berry"})
@EnableDiscoveryClient
@MapperScan(basePackages = {"cn.akfang.berry.mapper"})
@EnableFeignClients(basePackages = {"cn.akfang.berry.common.feign.client"})
public class BerryMiscApplication {
    public static void main(String[] args) {
        System.setProperty("rocketmq.client.logUseSlf4j", "true");
        SpringApplication.run(BerryMiscApplication.class, args);
    }
}
