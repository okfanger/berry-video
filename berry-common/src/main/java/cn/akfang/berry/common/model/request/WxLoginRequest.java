package cn.akfang.berry.common.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxLoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String clientId;
    private String code;
}
