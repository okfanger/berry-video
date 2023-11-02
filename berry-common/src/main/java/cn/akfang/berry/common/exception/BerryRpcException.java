package cn.akfang.berry.common.exception;

import cn.akfang.berry.common.enums.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Getter
@NoArgsConstructor
public class BerryRpcException extends RuntimeException implements Serializable {
    private int bizCode;
    private String bizMessage;
    private final long bizTimestamp = new Date().getTime();

    public BerryRpcException(int bizCode, String bizMessage) {
        this.bizCode = bizCode;
        this.bizMessage = bizMessage;
    }

    public BerryRpcException(ErrorCode errorCode, String bizMessage) {
        this.bizCode = errorCode.getCode();
        this.bizMessage = bizMessage;
    }

    public BerryRpcException(ErrorCode errorCode) {
        this.bizCode = errorCode.getCode();
        this.bizMessage = errorCode.getMessage();
    }
}
