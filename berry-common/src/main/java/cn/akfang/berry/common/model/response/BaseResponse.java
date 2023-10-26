package cn.akfang.berry.common.model.response;

import cn.akfang.berry.common.enums.ErrorCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int status;

    private T data;

    private String message;

    private boolean success = false;

    public BaseResponse() {

    }
    public BaseResponse(int status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int status, T data) {
        this(status, data, "");
    }


    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
