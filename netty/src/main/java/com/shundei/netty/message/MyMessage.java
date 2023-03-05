package com.shundei.netty.message;


import com.shundei.netty.handle.type.MyMessageType;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 基于所有的MyMessage,本次协议中关于控制和地址都没有实现，即随意写入符合长度的即可
 *
 * @author huoyouri
 */
public class MyMessage implements Serializable {
    private final MyMessageType messageType;
    private final byte control;
    /**
     * 5个字节
     */
    private final byte[] address;
    private final byte afn;
    private final byte seq;
    private final int DT;
    private final byte[] userData;

    public MyMessage(MyMessageType messageType,
                     byte control,
                     byte[] address,
                     byte afn,
                     byte seq,
                     int DT,
                     byte[] userData) {
        this.messageType = messageType;
        this.control = control;
        this.address = address;
        this.afn = afn;
        this.seq = seq;
        this.DT = DT;
        this.userData = userData;
    }

    public MyMessageType getMessageType() {
        return messageType;
    }

    public byte getControl() {
        return control;
    }

    public byte[] getAddress() {
        return address;
    }

    public byte getAfn() {
        return afn;
    }

    public byte getSeq() {
        return seq;
    }

    public int getDT() {
        return DT;
    }

    public byte[] getUserData() {
        return userData;
    }


    @Override
    public String toString() {
        return "MyMessage{" +
                "messageType=" + messageType +
                ", control=" + control +
                ", address=" + Arrays.toString(address) +
                ", afn=" + afn +
                ", seq=" + seq +
                ", DT=" + DT +
                ", userData=" + Arrays.toString(userData) +
                '}';
    }
}
