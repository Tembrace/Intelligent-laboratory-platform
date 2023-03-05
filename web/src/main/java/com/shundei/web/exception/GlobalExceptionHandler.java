package com.shundei.web.exception;

import com.shundei.web.utils.ResponseUtils;
import com.tang.backendcommon.BaseResponse;
import com.tang.backendcommon.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 使用springAOP解决
 *
 * @author huoyouri
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 该方法只去捕获BusinessException异常
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("BusinessException: " + e.getMessage() + ":" + e.getDesc());
        return ResponseUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException: " + e.getMessage());
        return ResponseUtils.error(ResponseCode.SYSTEM_ERROR);
    }
}
