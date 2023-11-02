package cn.akfang.berry.common.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadToken implements Serializable {
    private static final long serialVersionUID = 1L;

    private String upToken;
}
