package com.shundei.netty.handle;


import com.shundei.netty.handle.type.AFNType;
import com.shundei.netty.handle.type.DTType;
import com.shundei.netty.handle.type.MyMessageType;
import com.shundei.netty.message.MyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;


/**
 * 将用户数据区变成自己定义的MyMessage
 *
 * @author huoyouri
 */
@Slf4j
@ChannelHandler.Sharable
public class ContentCodec extends MessageToMessageCodec<ByteBuf, MyMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MyMessage msg, List<Object> out) throws Exception {
        if (ObjectUtils.anyNull(ctx, msg, out)) {
            log.info("ContentCodec error, ctx, msg, out cannot be null");
            throw new RuntimeException("第二个编解码器的编码异常，参数为空");
        }
        ByteBuf buffer = ctx.alloc().buffer();
        byte[] address = msg.getAddress();
        byte afn = msg.getAfn();
        int dt = msg.getDT();
        byte control = msg.getControl();
        byte seq = msg.getSeq();
        byte[] userData = msg.getUserData();
        buffer.writeByte(control);
        buffer.writeBytes(address);
        buffer.writeByte(afn);
        buffer.writeByte(seq);
        buffer.writeInt(dt);
        // 可能为空，其余的都不为空，也不管了
        if (userData != null) {
            buffer.writeBytes(userData);
        }
        ctx.writeAndFlush(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (ObjectUtils.anyNull(ctx, msg, out)) {
            log.info("ContentCodec error, ctx, msg, out cannot be null");
            throw new RuntimeException("第二个编解码器的解码异常，参数为空");
        }
        if (msg.readableBytes() == 0) {
            log.info("ContentCodec error, msg len cannot be zero");
            throw new RuntimeException("消息长度没有");
        }
        // 解码
        int len = msg.readableBytes();
        // 控制码
        byte control = msg.readByte();
        // 地址
        ByteBuf address = msg.readBytes(5);
        byte[] addr = new byte[5];
        for (int i = 0; i < addr.length; i++) {
            addr[i] = address.readByte();
        }
        address.resetReaderIndex();
        // AFN分辨消息类型
        byte afn = msg.readByte();
        // SEQ
        byte sequence = msg.readByte();
        // DA+DT,目前DA没用到，功能没到那么多
        int dataT = msg.readInt();
        MyMessageType msgType = judgeAFN(AFNType.valueOf(afn), DTType.valueOf(dataT));
        // 数据
        ByteBuf data = msg.readBytes(len - 12);
        if (msgType != MyMessageType.NO) {
            // 这里只要new了就肯定不会为null
            byte[] bytes = new byte[len - 12];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = data.readByte();
            }
            MyMessage message = new MyMessage(
                    msgType,
                    control,
                    addr,
                    afn,
                    sequence,
                    dataT,
                    bytes
            );
            out.add(message);
        } else {
            log.info("消息类型不对");
        }
    }

    /**
     * 解码的情况，control码不管
     *
     * @param afn   afn
     * @param dataT dt
     * @return 类型
     */
    public MyMessageType judgeAFN(AFNType afn, DTType dataT) {
        switch (afn) {
            case AFN_02:
                // 登录
                if (dataT == DTType.F1) {
                    return MyMessageType.REQ_LOGIN;
                }
                // 心跳
                if (dataT == DTType.F3) {
                    return MyMessageType.REQ_PING;
                }
                break;
            case AFN_0D:
                // 电表
                if (dataT == DTType.F21) {
                    return MyMessageType.REQ_ELECT;
                }
                // 环境表
                if (dataT == DTType.F15) {
                    return MyMessageType.REQ_ENVIRON;
                }
                break;
            default:
                break;
        }
        return MyMessageType.NO;
    }
}