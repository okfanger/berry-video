package cn.akfang.berry.service;

public interface LikeRedisService<T, K> {
    void saveLiked2Redis(T fromId, K toId);

    void unlikeFromRedis(T fromId, K toId);

    void deleteLikedFromRedis(T fromId, K toId);

    void incrementLikedCount(K toId);

    void decrementLikedCount(K toId);

    Integer getLikedCount(K toId);

    Boolean isLiked(T fromId, K toId);

//    LikeRedisHotPage<K, T, E> getLikedCountTop(Integer page, Integer size);

//    List<E> rankPage(LikeRedisHotPage<K, T, E> page);
}
