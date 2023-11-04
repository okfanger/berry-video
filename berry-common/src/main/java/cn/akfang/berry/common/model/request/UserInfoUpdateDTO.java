package cn.akfang.berry.common.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nickName;
    private Integer gender;
}
