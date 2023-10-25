package cn.akfang.berry.common.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class QiniuUploadDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    String hash;
    String key;
}
