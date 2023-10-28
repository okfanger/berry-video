package cn.akfang.berry.common.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QiniuTransformCallBackDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String pipeline;
    private int code;
    private String desc;
    private String reqid;
    private String inputBucket;
    private String inputKey;
    private List<Items> items;

    @Data
    public static class Items implements Serializable {
        private String cmd;
        private int code;
        private String desc;
        private String hash;
        private String key;
        private int returnOld;
    }
}
