package com.shundei.netty.message.elect;


import com.shundei.netty.message.MyMessage;

import static com.shundei.netty.handle.type.AFNType.AFN_00;
import static com.shundei.netty.handle.type.AFNType.AFN_0D;
import static com.shundei.netty.handle.type.DTType.F21;
import static com.shundei.netty.handle.type.DTType.F3;
import static com.shundei.netty.handle.type.MyMessageType.RSP_ELECT;

/**
 * 电表回复信息
 *
 * @author huoyouri
 */
public final class MyElectAckMessage extends MyMessage {
    /**
     * 默认的登录回复信息
     */
    public MyElectAckMessage() {
        super(
                RSP_ELECT,
                (byte) (0x00),
                new byte[5],
                AFN_00.getAfn(),
                (byte) (0x60),
                F3.getDt(),
                getElectAck()
        );
    }

    /**
     * 获得电表的返回
     *
     * @return 返回的消息体
     */
    private static byte[] getElectAck() {
        int dt = F21.getDt();
        byte low = (byte) (dt & 0xff);
        dt >>= 8;
        byte high = (byte) (dt & 0xff);
        return new byte[]{
                AFN_0D.getAfn(), (byte) 0x00, (byte) 0x00, high, low
        };
    }
}
