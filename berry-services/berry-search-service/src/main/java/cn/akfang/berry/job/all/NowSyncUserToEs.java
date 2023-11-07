package cn.akfang.berry.job.all;

import cn.akfang.berry.common.constants.EsConstants;
import cn.akfang.berry.common.feign.client.UserClient;
import cn.akfang.berry.common.model.entity.UserPO;
import cn.akfang.berry.mapper.UserEsMapper;
import cn.akfang.berry.model.UserEsPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class NowSyncUserToEs implements CommandLineRunner {
    private static final Integer PER_NUM = 500;

    @Autowired
    private UserEsMapper userEsMapper;

    @Autowired
    private UserClient userClient;

    @Override
    public void run(String... args) throws Exception {
        if (userEsMapper.existsIndex(EsConstants.USER_INDEX)) {
            return;
        } else {
            userEsMapper.createIndex(EsConstants.USER_INDEX);
            sync();
        }
    }

    private void sync() {
        log.info("[user]全量同步开始");
        List<Long> ids = userClient.listAllIds();
        for (int i = 0; i < ids.size(); i += PER_NUM) {
            List<Long> idList = ids.subList(i, Math.min(i + PER_NUM, ids.size()));
            List<UserPO> videoEsPOS = userClient.listByIds(idList);
            userEsMapper.insertBatch(videoEsPOS.stream().map((item) -> {
                UserEsPO userEsPO = new UserEsPO();
                userEsPO.setId(String.valueOf(item.getId()));
                userEsPO.setUserId(item.getId());
                userEsPO.setNickName(item.getNickName());
                return userEsPO;
            }).collect(java.util.stream.Collectors.toList()));
        }
        log.info("全量同步结束");
    }
}
