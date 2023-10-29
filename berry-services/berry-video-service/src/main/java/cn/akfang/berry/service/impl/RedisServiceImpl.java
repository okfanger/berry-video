//package cn.akfang.berry.service.impl;
//
//
//import cn.akfang.berry.common.model.dto.LikedCountDTO;
//import cn.akfang.berry.common.model.dto.UserLikeDTO;
//import cn.akfang.berry.common.utils.RedisKeyUtils;
//import cn.akfang.berry.service.RedisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.Cursor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ScanOptions;
//import org.springframework.stereotype.Service;
//import cn.akfang.berry.common.enums.LikeStatusEnum;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class RedisServiceImpl implements RedisService {
//
//    @Autowired
//    RedisTemplate<String, Object> redisTemplate;
//
//    @Override
//    public void saveLiked2Redis(Long fromId, Long toId) {
//        String key = RedisKeyUtils.getLikedKey(fromId, toId);
//        redisTemplate.opsForHash().put(
//                RedisKeyUtils
//                        .MAP_KEY_USER_LIKED, key,
//                LikeStatusEnum
//                        .LIKE.getCode());
//    }
//
//    @Override
//    public void unlikeFromRedis(Long fromId, Long toId) {
//        String key = RedisKeyUtils.getLikedKey(fromId, toId);
//        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, LikeStatusEnum.UNLIKE.getCode());
//    }
//
//    @Override
//    public void deleteLikedFromRedis(Long likedUserId, Long likedPostId) {
//        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId);
//        redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
//    }
//
//    @Override
//    public void incrementLikedCount(Long toId) {
//        redisTemplate.opsForHash().increment(
//                RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, toId, 1
//        );
//    }
//
//    @Override
//    public void decrementLikedCount(Long toId) {
//        redisTemplate.opsForHash().increment(
//                RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, toId, -1
//        );
//    }
//
//    @Override
//    public List<UserLikeDTO> getLikedDataFromRedis() {
//        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(
//                RedisKeyUtils.MAP_KEY_USER_LIKED, ScanOptions.NONE);
//        List<UserLikeDTO> list = new ArrayList<>();
//
//        while (cursor.hasNext()){
//            Map.Entry<Object, Object> entry = cursor.next();
//            String key = (String) entry.getKey();
//            //分离出 likedUserId，likedPostId
//            String[] split = key.split("::");
//            String likedUserId = split[0];
//            String likedPostId = split[1];
//            Integer value = (Integer) entry.getValue();
//            UserLikeDTO userLikeDTO = new UserLikeDTO(likedUserId, likedPostId, value);
//            list.add(userLikeDTO);
//            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
//        }
//        return list;
//    }
//
//
//
//    @Override
//    public List<LikedCountDTO> getLikedCountFromRedis() {
//        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(
//                RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
//        List<LikedCountDTO> list = new ArrayList<>();
//        while (cursor.hasNext()){
//            Map.Entry<Object, Object> map = cursor.next();
//            String key = (String)map.getKey();
//            LikedCountDTO dto = new LikedCountDTO(key, (Integer) map.getValue());
//            list.add(dto);
//            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key);
//        }
//        return list;
//    }
//
//    @Override
//    public Integer getLikedCount(Long id, ) {
//        return (Integer) redisTemplate.opsForHash().get(
//                RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, likedUserId);
//    }
//
//}