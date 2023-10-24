package cn.akfang.berry.common.exception;

import cn.akfang.berry.common.model.ErrorCode;
import lombok.Getter;
import org.apache.dubbo.rpc.RpcException;

import java.util.Date;

@Getter
public class BerryRpcException extends RpcException {
    private int bizCode;
    private String bizMessage;
    private final long bizTimestamp = new Date().getTime();
    private final Boolean isBiz = true;

    public BerryRpcException(int bizCode, String bizMessage) {
        super();
        this.bizCode = bizCode;
        this.bizMessage = bizMessage;
    }

    public BerryRpcException(ErrorCode errorCode, String bizMessage) {
        super();
        this.bizCode = errorCode.getCode();
        this.bizMessage = bizMessage;
    }

    public BerryRpcException(ErrorCode errorCode) {
        super();
        this.bizCode = errorCode.getCode();
        this.bizMessage = errorCode.getMessage();
    }
}
