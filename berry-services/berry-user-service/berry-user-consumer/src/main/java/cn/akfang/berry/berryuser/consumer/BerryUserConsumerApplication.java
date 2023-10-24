package cn.akfang.berry.berryuser.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BerryUserConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BerryUserConsumerApplication.class, args);
    }

}
