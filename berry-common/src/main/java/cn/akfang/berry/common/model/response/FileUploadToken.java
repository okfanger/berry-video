package cn.akfang.berry.common.model.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FileUploadToken implements Serializable {
    private static final long serialVersionUID = 1L;

    String upToken;
}
