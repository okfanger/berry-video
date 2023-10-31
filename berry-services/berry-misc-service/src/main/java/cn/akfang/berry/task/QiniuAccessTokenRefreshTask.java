package cn.akfang.berry.task;

import cn.akfang.berry.manager.QiniuOSSManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
@EnableAsync
@Slf4j
public class QiniuAccessTokenRefreshTask {
    @Autowired
    QiniuOSSManager qiniuOSSManager;

    /**
     * 每天凌晨刷新七牛云token
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void execute() {
        log.info("===refresh qiniu access token start===");
        qiniuOSSManager.afterPropertiesSet();
        log.info("===refresh qiniu access token end===");
    }
}
