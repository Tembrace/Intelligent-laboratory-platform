package com.shundei.netty.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteOrder;

/**
 * @author huoyouri
 * 
 * 解决粘包半包的问题
 */
@Slf4j
public class ProtocolDecoder extends LengthFieldBasedFrameDecoder {

    public ProtocolDecoder() {
        this(1024, 3,
                2, 1, 0);
    }

    public ProtocolDecoder(int maxFrameLength, int lengthFieldOffset,
                           int lengthFieldLength, int lengthAdjustment,
                           int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset,
                lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    /**
     * 自定义LengthFieldBasedFrameDecoder读取的长度
     */
    @Override
    protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
        ByteBuf slice = buf.slice(offset, length);
        byte b = slice.readByte();
        byte b1 = slice.readByte();
        int low = Byte.toUnsignedInt(b);
        int high = Byte.toUnsignedInt(b1);
        log.info("ProtocolDecoder: 数据体长度{}", (high * 256 + low) / 4 + 2);
        return (high * 256L + low) / 4 + 2;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("{}", cause.getMessage());
    }

}
