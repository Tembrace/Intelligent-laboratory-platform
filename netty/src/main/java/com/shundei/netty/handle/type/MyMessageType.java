package com.shundei.netty.handle.type;

/**
 * MyMessage的类型信息,只需要记录网关过来的消息
 *
 * @author huoyouri
 */
public enum MyMessageType {
    /**
     * 网关登录
     */
    REQ_LOGIN(0),

    /**
     * 网关回复1 确认
     */
    RSP_GATEWAY_SUCCESS(1),
    
    /**
     * 网关回复2 校时
     */
    RSP_GATEWAY_TIME(2),

    /**
     * 心跳
     */
    REQ_PING(3),

    /**
     * 心跳回复
     */
    RSP_PING(4),

    /**
     * 环境表信息
     */
    REQ_ENVIRON(5),

    /**
     * 回复
     */
    RSP_ENVIRON(6),

    /**
     * 电表信息
     */
    REQ_ELECT(7),

    /**
     * 回复
     */
    RSP_ELECT(8),

    /**
     * 未识别的消息类型
     */
    NO(-1);

    private final int type;

    MyMessageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    /**
     * 转换为枚举类
     *
     * @param type 类型
     * @return 对应类型的枚举类
     */
    public static MyMessageType valueOf(int type) {
        for (MyMessageType t : values()) {
            if (t.type == type) {
                return t;
            }
        }
        throw new IllegalArgumentException("unknown MyMessageType: " + type);
    }
}
