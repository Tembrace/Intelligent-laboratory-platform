package com.shundei.netty.message.environ;


import com.shundei.netty.message.MyMessage;

import static com.shundei.netty.handle.type.AFNType.AFN_00;
import static com.shundei.netty.handle.type.AFNType.AFN_0D;
import static com.shundei.netty.handle.type.DTType.F15;
import static com.shundei.netty.handle.type.DTType.F3;
import static com.shundei.netty.handle.type.MyMessageType.RSP_ENVIRON;

/**
 * 环境表回复信息
 *
 * @author huoyouri
 */
public final class MyEnvironAckMessage extends MyMessage {
    /**
     * 默认的登录回复信息
     */
    public MyEnvironAckMessage() {
        super(
                RSP_ENVIRON,
                (byte) (0x00),
                new byte[5],
                AFN_00.getAfn(),
                (byte) (0x60),
                F3.getDt(),
                getEnvironAck()
        );
    }

    /**
     * 获得环境表的返回
     *
     * @return 返回的消息体
     */
    private static byte[] getEnvironAck() {
        int dt = F15.getDt();
        byte low = (byte) (dt & 0xff);
        dt >>= 8;
        byte high = (byte) (dt & 0xff);
        return new byte[]{
                AFN_0D.getAfn(), (byte) 0x00, (byte) 0x00, high, low
        };
    }
}
