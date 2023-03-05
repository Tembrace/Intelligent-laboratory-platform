package com.tang.backendcommon;


/**
 * 状态返回码
 *
 * @author huoyouri
 */
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS(20000, "成功", ""),

    /**
     * 请求参数错误
     */
    PARAMS_ERROR(40000, "请求参数错误", ""),

    /**
     * 请求数据为空
     */
    NULL_ERROR(40001, "请求数据为空", ""),

    /**
     * 未登录
     */
    NOT_LOGIN(40100, "未登录", ""),

    /**
     * 无权限
     */
    NO_AUTH(40101, "无权限", ""),

    /**
     * 系统内部异常
     */
    SYSTEM_ERROR(50000, "系统内部异常", "");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String msg;

    /**
     * 详细描述
     */
    private final String desc;

    ResponseCode(int code, String msg, String desc) {
        this.code = code;
        this.msg = msg;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDesc() {
        return desc;
    }
}
