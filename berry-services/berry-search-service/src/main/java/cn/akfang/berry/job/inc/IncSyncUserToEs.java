package cn.akfang.berry.job.inc;

import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.mapper.UserEsMapper;
import cn.akfang.berry.model.UserEsPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IncSyncUserToEs {


    @Autowired
    private UserEsMapper userEsMapper;

    @Autowired
    private UserClient userClient;

    private static final Integer PER_NUM = 100;

    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        // 获取两分钟前有更新记录的视频
        List<Long> longs;
        try {
            longs = userClient.listIdsMinutesAgo(1L);
            if (longs == null || longs.isEmpty()) {
                return;
            }
        } catch (Exception ignore) {
            return;
        }
        log.info("user need sync num: {}", longs.size());
        for (int i = 0; i < longs.size(); i += PER_NUM) {
            List<Long> ids = longs.subList(i, Math.min(i + PER_NUM, longs.size()));
            List<UserPO> userPOS = userClient.listByIds(ids);
            List<UserEsPO> collect = userPOS.stream().map((item) -> {
                UserEsPO userEsPO = new UserEsPO();
                userEsPO.setId(String.valueOf(item.getId()));
                userEsPO.setUserId(item.getId());
                userEsPO.setNickName(item.getNickName());
                return userEsPO;
            }).collect(Collectors.toList());
            userEsMapper.insertBatch(collect);
        }
        log.info("user sync job Es2Db end");
    }
}
