package cn.akfang.berry.common.model.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserTokenResponse implements Serializable {
    private static final long serialVersionUID = 1L;


    private String token;
}
