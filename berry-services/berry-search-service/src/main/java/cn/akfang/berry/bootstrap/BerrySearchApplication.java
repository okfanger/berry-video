package cn.akfang.berry.bootstrap;

import org.dromara.easyes.starter.register.EsMapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"cn.akfang.berry"})
@EnableFeignClients(basePackages = {"cn.akfang.berry.common.feign.client"})
@EnableDiscoveryClient
@EsMapperScan("cn.akfang.berry.mapper")
public class BerrySearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BerrySearchApplication.class, args);
    }
}
