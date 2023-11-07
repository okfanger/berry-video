package cn.akfang.berry.common.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VideoActionInfoRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Long> videoIds;
    private Long userId;
}
