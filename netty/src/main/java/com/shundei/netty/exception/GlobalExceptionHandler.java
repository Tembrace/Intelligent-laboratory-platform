package com.shundei.netty.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器
 * 使用springAOP解决
 *
 * @author huoyouri
 */
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public void runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException: " + e.getMessage());
    }
}
