package cn.akfang.berry.handler;

import cn.akfang.berry.common.exception.BerryRpcException;
import cn.akfang.berry.common.exception.BusinessException;
import cn.akfang.berry.common.handler.DefaultGlobalExceptionHandler;
import cn.akfang.berry.common.model.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserGlobalExceptionHandler extends DefaultGlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    @Override
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        return super.businessExceptionHandler(e);
    }

    @ExceptionHandler(RuntimeException.class)
    @Override
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        return super.runtimeExceptionHandler(e);
    }

    @ExceptionHandler(BerryRpcException.class)
    @Override
    public BaseResponse<?> berryRpcException(BerryRpcException e) {
        return super.berryRpcException(e);
    }
}
