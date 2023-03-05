package com.shundei.netty.message.ping;


import com.shundei.netty.message.MyMessage;

import static com.shundei.netty.handle.type.AFNType.AFN_02;
import static com.shundei.netty.handle.type.DTType.F3;
import static com.shundei.netty.handle.type.MyMessageType.RSP_PING;

/**
 * 心跳回复
 *
 * @author huoyouri
 */
public final class PingAckMessage extends MyMessage {
    /**
     * 默认的ping返回
     */
    public PingAckMessage() {
        super(
                RSP_PING,
                (byte) (0x00),
                new byte[5],
                AFN_02.getAfn(),
                (byte) (0x60),
                F3.getDt(),
                null
        );
    }

}
