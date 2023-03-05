package com.shundei.netty.message.login;


import com.shundei.netty.common.TimeStringToBytes;
import com.shundei.netty.message.MyMessage;

import static com.shundei.netty.handle.type.AFNType.AFN_05;
import static com.shundei.netty.handle.type.DTType.F31;
import static com.shundei.netty.handle.type.MyMessageType.RSP_GATEWAY_TIME;

/**
 * 登录回复信息 2
 *
 * @author huoyouri
 */
public final class MyLoginAckMessageTime extends MyMessage {
    /**
     * 默认的登录回复信息
     */
    public MyLoginAckMessageTime() {
        super(
                RSP_GATEWAY_TIME,
                (byte) (0x00),
                new byte[5],
                AFN_05.getAfn(),
                (byte) (0x60),
                F31.getDt(),
                TimeStringToBytes.curTransformToBytes()
        );
    }
}
