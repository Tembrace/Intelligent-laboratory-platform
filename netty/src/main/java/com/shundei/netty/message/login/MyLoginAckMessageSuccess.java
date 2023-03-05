package com.shundei.netty.message.login;


import com.shundei.netty.message.MyMessage;

import static com.shundei.netty.handle.type.AFNType.AFN_02;
import static com.shundei.netty.handle.type.DTType.F1;
import static com.shundei.netty.handle.type.MyMessageType.RSP_GATEWAY_SUCCESS;

/**
 * 登录回复信息 1
 *
 * @author huoyouri
 */
public final class MyLoginAckMessageSuccess extends MyMessage {
    /**
     * 默认的登录回复信息
     */
    public MyLoginAckMessageSuccess() {
        super(
                RSP_GATEWAY_SUCCESS,
                (byte) (0x00),
                new byte[5],
                AFN_02.getAfn(),
                (byte) (0x60),
                F1.getDt(),
                null
        );
    }
}
