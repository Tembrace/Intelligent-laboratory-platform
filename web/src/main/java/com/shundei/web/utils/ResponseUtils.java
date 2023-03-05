package com.shundei.web.utils;

import com.tang.backendcommon.BaseResponse;
import com.tang.backendcommon.ResponseCode;

/**
 * 返回工具类
 *
 * @author huoyouri
 */
public class ResponseUtils {

    /**
     * 成功
     *
     * @param responseCode 状态码
     * @param data         数据体
     * @param <T>          泛型
     * @return 基本响应体
     */
    public static <T> BaseResponse<T> success(ResponseCode responseCode, T data) {
        return new BaseResponse(responseCode.getCode(), data);
    }

    public static <T> BaseResponse<T> success(ResponseCode responseCode, T data, String desc) {
        return new BaseResponse(responseCode.getCode(), data, desc);
    }

    public static <T> BaseResponse<T> success(int code, T data, String msg) {
        return new BaseResponse(code, data, msg);
    }

    public static <T> BaseResponse<T> success(int code, T data, String msg, String desc) {
        return new BaseResponse(code, data, msg, desc);
    }

    /**
     * 失败
     *
     * @param responseCode 状态码
     * @return 基本响应体
     */
    public static BaseResponse error(ResponseCode responseCode) {
        return new BaseResponse(responseCode.getCode(), responseCode.getMsg());
    }

    public static BaseResponse error(ResponseCode responseCode, String desc) {
        return new BaseResponse(responseCode.getCode(), desc);
    }

    public static BaseResponse error(int code, String msg) {
        return new BaseResponse(code, msg);
    }

    public static BaseResponse error(int code, String msg, String desc) {
        return new BaseResponse(code, msg, desc);
    }
}
