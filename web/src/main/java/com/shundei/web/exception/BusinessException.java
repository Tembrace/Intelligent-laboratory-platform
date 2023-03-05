package com.shundei.web.exception;


import com.tang.backendcommon.ResponseCode;

/**
 * 自定义异常类
 *
 * @author huoyouri
 */
public class BusinessException extends RuntimeException {
    /**
     * 异常状态码
     */
    private final int code;

    /**
     * 详细描述
     */
    private final String desc;

    public BusinessException(String msg, int code, String desc) {
        super(msg);
        this.code = code;
        this.desc = desc;
    }

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
        this.desc = responseCode.getDesc();
    }

    public BusinessException(ResponseCode responseCode, String desc) {
        super(responseCode.getMsg());
        this.code = responseCode.getCode();
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
