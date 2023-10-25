package cn.akfang.berry.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RedisIdGenerator {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 6;
    private static final String ID_COUNTER_KEY = "id_counter";
    private static final String RECYCLE_SET_KEY = "recycle_set";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String generateId() {
        Long counter = redisTemplate.opsForValue().increment(ID_COUNTER_KEY);
        String id = generateRandomId(counter);
        return id;
    }

    public void recycleId(String id) {
        redisTemplate.opsForSet().add(RECYCLE_SET_KEY, id);
    }

    public boolean existId(String id) {
        return redisTemplate.opsForSet().isMember(RECYCLE_SET_KEY, id);
    }

    private String generateRandomId(long counter) {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        Random random = new Random(counter);
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }
        String id = sb.toString();
        if (existId(id)) {
            return generateRandomId(counter + 1); // ID已存在于回收集合中，重新生成
        }
        return id;
    }
}