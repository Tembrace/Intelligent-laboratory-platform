package com.shundei.netty.handle.type;

/**
 * MyMessage的AFN信息
 *
 * @author huoyouri
 */
public enum AFNType {
    /**
     * 确认/否认
     */
    AFN_00((byte) 0x00),

    /**
     * 复位
     */
    AFN_01((byte) 0x01),

    /**
     * 链路接口检测
     */
    AFN_02((byte) 0x02),

    /**
     * 设置参数
     */
    AFN_04((byte) 0x04),

    /**
     * 控制命令
     */
    AFN_05((byte) 0x05),

    /**
     * 身份认证及密钥协商
     */
    AFN_06((byte) 0x06),

    /**
     * 请求终端配置及信息
     */
    AFN_09((byte) 0x09),

    /**
     * 查询参数
     */
    AFN_0A((byte) 0x0a),

    /**
     * 请求一类数据
     */
    AFN_0C((byte) 0x0c),

    /**
     * 请求二类数据
     */
    AFN_0D((byte) 0x0d),

    /**
     * 文件传输
     */
    AFN_0F((byte) 0x0f);

    private final byte afn;

    AFNType(byte afn) {
        this.afn = afn;
    }

    public byte getAfn() {
        return afn;
    }

    /**
     * 转换为枚举类
     *
     * @param afn 类型
     * @return 对应类型的枚举类
     */
    public static AFNType valueOf(int afn) {
        for (AFNType t : values()) {
            if (t.afn == afn) {
                return t;
            }
        }
        throw new IllegalArgumentException("unknown AFNType: " + afn);
    }
}
