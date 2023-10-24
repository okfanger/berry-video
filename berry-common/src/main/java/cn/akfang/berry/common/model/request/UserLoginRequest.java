package cn.akfang.berry.common.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
