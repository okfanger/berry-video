package cn.akfang.berry.common.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FeedPage<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<T> records;
}
