package cn.akfang.berry.task;

import cn.akfang.berry.manager.QiniuOSSManager;
import cn.akfang.berry.service.FileService;
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
public class QiniuFileRecycleTask {
    @Autowired
    QiniuOSSManager qiniuOSSManager;

    @Autowired
    FileService fileService;

    /**
     * 每天清除删除后的视频关联资源
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Async
    public void execute() {
        log.info("===remove qiniu recycle task start===");
        // TODO: 查询数据库中软删除的视频，挨个回收oss资源
        log.info("===remove qiniu recycle task end===");
    }
}
