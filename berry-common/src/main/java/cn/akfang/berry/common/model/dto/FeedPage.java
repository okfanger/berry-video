package cn.akfang.berry.common.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class FeedPage<T> {
    private Long lastId;
    private List<T> records;
}
