package com.tang.backendcommon;

import lombok.Data;

import java.io.Serializable;

/**
 * 基础返回类
 *
 * @author huoyouri
 */
@Data
public class BaseResponse<T> implements Serializable {
    /**
     * 状态码
     */
    private int code;

    /**
     * 返回体
     */
    private T data;

    /**
     * 消息
     */
    private String msg;

    /**
     * 详细描述
     */
    private String desc;

    public BaseResponse(int code, T data, String msg, String desc) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.desc = desc;
    }

    public BaseResponse() {
    }


    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(int code, T data, String msg) {
        this(code, data, msg, "");
    }

    public BaseResponse(int code, String msg) {
        this(code, null, msg, "");
    }

    public BaseResponse(int code, String msg, String desc) {
        this(code, null, msg, desc);
    }

}
