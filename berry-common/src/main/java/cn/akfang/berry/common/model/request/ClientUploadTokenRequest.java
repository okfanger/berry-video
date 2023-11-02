package cn.akfang.berry.common.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class ClientUploadTokenRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank
    private String uuid;
}
