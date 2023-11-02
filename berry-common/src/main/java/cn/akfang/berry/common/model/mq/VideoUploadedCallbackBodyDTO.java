package cn.akfang.berry.common.model.mq;

import lombok.Data;

@Data
public class VideoUploadedCallbackBodyDTO implements java.io.Serializable {
    private String hash;
    private String key;
    private String bucket;
    private String fsize;
    private Long fileId;
}
