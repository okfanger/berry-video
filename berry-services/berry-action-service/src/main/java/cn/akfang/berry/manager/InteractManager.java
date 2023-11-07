package cn.akfang.berry.manager;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class InteractManager {

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    public static final String redisKeyPrefix = "INTERACT_WS::";
    public static final String redisKeyRoomPrefix = redisKeyPrefix + "ROOM";
    public static final String redisKeyRoomMemberPrefix = redisKeyPrefix + "ROOM_MEMBER";
    public static final Long defaultExpireTime = 60 * 60L;


    private String buildRoomZSetKey(String roomId) {
        return redisKeyPrefix + "::" + roomId;
    }

    private String buildRoomInfoKey(String roomId) {
        return redisKeyRoomPrefix + "::" + roomId;
    }

    public String createRoom() {
        String roomId = IdUtil.getSnowflakeNextIdStr();
        long createTimestamp = System.currentTimeMillis();
        redisTemplate.opsForHash().put(buildRoomInfoKey(roomId), "member_count", 0);
        redisTemplate.opsForHash().put(buildRoomInfoKey(roomId), "expire_timestamp", createTimestamp + defaultExpireTime);
        redisTemplate.opsForHash().getOperations().expire(roomId, Duration.of(defaultExpireTime, ChronoUnit.SECONDS));
        redisTemplate.opsForZSet().add(buildRoomZSetKey(roomId), roomId, createTimestamp);
        redisTemplate.opsForZSet().getOperations().expire(roomId, Duration.of(defaultExpireTime, ChronoUnit.SECONDS));

        return roomId;
    }

    public boolean isLogicExists(String roomId) {
        Object o = redisTemplate.opsForHash().get(buildRoomInfoKey(roomId), "expire_timestamp");
        if (ObjectUtil.isNull(o)) {
            return false;
        } else return System.currentTimeMillis() <= Long.parseLong(o.toString());
    }

}
