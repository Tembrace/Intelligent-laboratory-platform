package com.shundei.netty.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * 解析帧的帧头，校验和，帧尾，判断并去掉
 *
 * @author huoyouri
 */
@ChannelHandler.Sharable
@Slf4j
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    @Value("${netty.test_judge_frame_sum}")
    private boolean testValue;

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        if (ObjectUtils.anyNull(ctx, msg, out)) {
            log.info("MessageCodecSharable error, ctx, msg, out cannot be null");
            throw new RuntimeException("最开始编解码器的编码异常，参数为空");
        }
        if (msg.readableBytes() == 0) {
            log.info("MessageCodecSharable error, msg len cannot be zero");
            throw new RuntimeException("消息长度没有");
        }
        // 编码
        log.info("MessageCodecSharable-encode");
        // 起始字符68H
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeByte(0x68);
        // 长度
        int len = msg.readableBytes();
        // 左移两位
        int temp = len * 4;
        byte lowLen = (byte) (temp + 2);
        byte highLen = (byte) (temp / 256);
        buf.writeByte(lowLen);
        buf.writeByte(highLen);
        buf.writeByte(lowLen);
        buf.writeByte(highLen);
        buf.writeByte(0x68);
        // 数据区
        // 这里写入的时候msg的读指针也会指向最后，毕竟是先读出来在写入
        buf.writeBytes(msg);
        msg.resetReaderIndex();
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = msg.readByte();
        }
        // 帧校验和
        buf.writeByte(countFrameSum(bytes));
        // 结束字符
        buf.writeByte(0x16);
        buf.resetReaderIndex();
        buf.resetReaderIndex();
        out.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        if (ObjectUtils.anyNull(ctx, msg, out)) {
            log.info("MessageCodecSharable error, ctx, msg, out cannot be null");
            throw new RuntimeException("最开始编解码器的解码异常，参数为空");
        }
        if (msg.readableBytes() == 0) {
            log.info("MessageCodecSharable error, msg len cannot be zero");
            throw new RuntimeException("消息长度没有");
        }
        // 解码
        log.info("MessageCodecSharable-decode");
        msg.resetReaderIndex();
        if (verifyFrame(msg)) {
            msg.resetReaderIndex();
            msg.readByte();
            byte lowLen = msg.readByte();
            byte highLen = msg.readByte();
            int len = (Byte.toUnsignedInt(highLen) * 256 + Byte.toUnsignedInt(lowLen)) / 4;
            msg.readChar();
            msg.readByte();
            ByteBuf byteBuf = msg.readBytes(len);
            out.add(byteBuf);
        }
    }

    /**
     * 校验帧
     *
     * @param msg 帧
     * @return 返回结果
     */
    private boolean verifyFrame(ByteBuf msg) {
        byte b = msg.readByte();
        if (b == (byte) 0x68) {
            byte lowLen = msg.readByte();
            byte highLen = msg.readByte();
            int len = (Byte.toUnsignedInt(highLen) * 256 + Byte.toUnsignedInt(lowLen)) / 4;
            msg.readChar();
            byte b1 = msg.readByte();
            if (b1 == (byte) 0x68) {
                ByteBuf byteBuf = msg.readBytes(len);
                byte[] bytes = new byte[byteBuf.readableBytes()];
                // 下面对bytes赋值的时候不能用a:bytes的形式赋值
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = byteBuf.readByte();
                }
                byte b2 = msg.readByte();
                if (!testValue || b2 == countFrameSum(bytes)) {
                    byte b3 = msg.readByte();
                    if (b3 == 0x16) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 计算帧校验和
     *
     * @param userDate 用户数据区数据
     * @return 帧校验和
     */
    private byte countFrameSum(byte[] userDate) {
        byte sum = 0;
        for (byte b : userDate) {
            sum = (byte) (sum + b);
        }
        return sum;
    }
}
