package com.shundei.netty.handle.type;

/**
 * MyMessage的DT信息，DA一直都是0
 *
 * @author huoyouri
 */
public enum DTType {
    /**
     * F1 登录
     */
    F1(0x0100),

    /**
     * F3 回复
     */
    F3(0x0400),

    /**
     * F15 环境表
     */
    F15(0x4001),

    /**
     * F21 电表
     */
    F21(0x1002),

    /**
     * F31 校时
     */
    F31(0x4003);

    private final int dt;

    DTType(int dt) {
        this.dt = dt;
    }

    public int getDt() {
        return dt;
    }

    /**
     * 转换为枚举类
     *
     * @param dt 类型
     * @return 对应类型的枚举类
     */
    public static DTType valueOf(int dt) {
        for (DTType t : values()) {
            if (t.dt == dt) {
                return t;
            }
        }
        throw new IllegalArgumentException("unknown DTType: " + dt);
    }
}
