package com.shundei.netty.handle.process;

import com.shundei.netty.message.ping.PingAckMessage;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 心跳处理类
 *
 * @author huoyouri
 */
@Slf4j
public class Ping {
    public void processPing(Channel channel) {
        if (channel == null) {
            log.info("Ping channel cannot be null");
            throw new RuntimeException("心跳通道为空");
        }
        log.info("目前处理该通道的事件循环是：{}", channel.eventLoop().toString());
        log.info("目前处理该通道的事件循环组是：{}", channel.eventLoop().parent().toString());
        log.info("processPing");
        channel.writeAndFlush(new PingAckMessage());
    }
}
