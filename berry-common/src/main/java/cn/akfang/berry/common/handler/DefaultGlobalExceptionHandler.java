package cn.akfang.berry.common.handler;

import cn.akfang.berry.common.enums.ErrorCode;
import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.exception.BusinessException;
import cn.akfang.berry.common.model.response.BaseResponse;
import cn.akfang.berry.common.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultGlobalExceptionHandler {
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }

    public BaseResponse<?> berryRpcException(BerryRpcException e) {
        log.error("berryRpcException", e);
        return ResultUtils.error(e.getBizCode(), e.getBizMessage());
    }
}
