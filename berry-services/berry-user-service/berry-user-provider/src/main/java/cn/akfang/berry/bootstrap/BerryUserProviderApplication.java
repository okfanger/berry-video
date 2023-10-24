package cn.akfang.berry.bootstrap;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(
        scanBasePackages = {"cn.akfang.berry.bootstrap", "cn.akfang.berry.provider"}
)
@EnableDiscoveryClient
@DubboComponentScan(basePackages = {"cn.akfang.berry.provider"})
@MapperScan(basePackages = {"cn.akfang.berry.mapper"})
public class BerryUserProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BerryUserProviderApplication.class, args);
    }
}
