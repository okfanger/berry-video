package cn.akfang.berry.service.base;

import cn.akfang.berry.common.enums.LikeStatusEnum;
import cn.akfang.berry.common.enums.LikeTypeEnum;
import cn.akfang.berry.common.model.dto.ActionCountDTO;
import cn.akfang.berry.common.model.dto.ActionDTO;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ActionService<A, B, M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
    protected String buildKey(A fromId, B toId) {
        return fromId + "::" + toId;
    }

    protected abstract RedisTemplate<String, Object> getRedisTemplate();

    protected abstract LikeTypeEnum getLikeTypeEnum();

    public abstract M getBaseMapper();

    protected boolean isAction(A fromId, B toId) {
        Optional<Integer> valueOpt = Optional.ofNullable(
                (Integer) getRedisTemplate().opsForHash().get(
                        getLikeTypeEnum().getRedisKey(),
                        buildKey(fromId, toId)
                )
        );
        return valueOpt.map(integer -> ObjectUtil.equal(integer, LikeStatusEnum.LIKE.getCode()))
                .orElse(false);
    }

    public void saveActionToRedis(A fromId, B toId) {
        getRedisTemplate().opsForHash().put(
                getLikeTypeEnum().getRedisKey(),
                buildKey(fromId, toId),
                LikeStatusEnum.LIKE.getCode()
        );
        getRedisTemplate().opsForHash().increment(
                getLikeTypeEnum().getRedisHashCountKey(),
                String.valueOf(toId),
                1
        );
    }

    public void saveUnActionToRedis(A fromId, B toId) {
        getRedisTemplate().opsForHash().put(
                getLikeTypeEnum().getRedisKey(),
                buildKey(fromId, toId),
                LikeStatusEnum.UNLIKE.getCode()
        );
        getRedisTemplate().opsForHash().increment(
                getLikeTypeEnum().getRedisHashCountKey(),
                String.valueOf(toId),
                -1
        );
    }

    public Integer getActionCountFromRedis(B toId) {
        return Optional.ofNullable(
                (Integer) getRedisTemplate().opsForHash().get(
                        getLikeTypeEnum().getRedisHashCountKey(),
                        String.valueOf(toId)
                )
        ).orElse(0);
    }

    public List<ActionDTO<Long, Long>> getActionDTOs() {
        ArrayList<ActionDTO<Long, Long>> actionDTOs = new ArrayList<>();
        Cursor<Map.Entry<Object, Object>> scan = getRedisTemplate().opsForHash().scan(getLikeTypeEnum().getRedisKey(), null);
        scan.forEachRemaining(
                entry -> {
                    String key = (String) entry.getKey();
                    String[] split = key.split("::");
                    String fromId = split[0];
                    String toId = split[1];
                    Integer value = (Integer) entry.getValue();
                    if (ObjectUtil.isNotNull(value)) {
                        actionDTOs.add(new ActionDTO<>(Long.parseLong(fromId), Long.parseLong(toId), value));
                    }
                }
        );
        scan.close();
        return actionDTOs;
    }

    public abstract List<T> convertDTOsToPOs(List<ActionDTO<Long, Long>> actionDTOs);

    public List<ActionCountDTO<Long>> getCountDTOs() {
        ArrayList<ActionCountDTO<Long>> countDTOs = new ArrayList<>();
        Cursor<Map.Entry<Object, Object>> scan = getRedisTemplate().opsForHash().scan(getLikeTypeEnum().getRedisHashCountKey(), null);
        scan.forEachRemaining(
                entry -> {
                    String key = (String) entry.getKey();
                    Integer value = (Integer) entry.getValue();
                    if (ObjectUtil.isNotNull(value)) {
                        countDTOs.add(new ActionCountDTO<>(Long.parseLong(key), value));
                    }
                }
        );
        scan.close();
        return countDTOs;
    }

    public List<String> getActionedToIds(A fromId) {
        Cursor<Map.Entry<Object, Object>> scan = getRedisTemplate().opsForHash().scan(
                getLikeTypeEnum().getRedisKey(),
                ScanOptions.scanOptions()
                        .match(fromId + "::*").build()
        );
        List<String> collect = scan.stream()
                .filter(entry -> entry.getValue() == LikeStatusEnum.LIKE.getCode()).map(
                        entry -> {
                            String key = (String) entry.getKey();
                            String[] split = key.split("::");
                            return split[1];
                        }
                ).collect(Collectors.toList());
        scan.close();
        return collect;
    }

    public List<String> getActionedFromIds(B toId) {
        Cursor<Map.Entry<Object, Object>> scan = getRedisTemplate().opsForHash().scan(
                getLikeTypeEnum().getRedisKey(),
                ScanOptions.scanOptions()
                        .match("*::" + toId).build()
        );
        List<String> collect = scan.stream()
                .filter(entry -> entry.getValue() == LikeStatusEnum.LIKE.getCode()).map(
                        entry -> {
                            String key = (String) entry.getKey();
                            String[] split = key.split("::");
                            return split[0];
                        }
                ).collect(Collectors.toList());
        scan.close();
        return collect;
    }
}


