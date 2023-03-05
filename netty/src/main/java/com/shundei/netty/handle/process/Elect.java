package com.shundei.netty.handle.process;

import com.shundei.netty.clients.NettyClientsService;
import com.shundei.netty.common.DealMeterUtils;
import com.shundei.netty.message.MyMessage;
import com.shundei.netty.message.elect.MyElectAckMessage;
import com.shundei.netty.store.SessionStoreService;
import com.tang.backendcommon.dto.ElectParamsRequest;
import com.tang.backendcommon.inner.InnerElectricityParamsService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;

import java.util.List;

/**
 * 电表信息处理类
 *
 * @author huoyouri
 */
@Slf4j
@EnableDubbo
public class Elect {

    // private NettyClientsService nettyClientsService;
    private SessionStoreService sessionStoreService;

    @DubboReference
    private InnerElectricityParamsService innerElectricityParamsService;

    public Elect(NettyClientsService nettyClientsService, SessionStoreService sessionStoreService) {
        // this.nettyClientsService = nettyClientsService;
        this.sessionStoreService = sessionStoreService;
    }

    public void processElect(Channel channel, MyMessage message) {
        if (channel == null || message == null) {
            log.info("Elect channel, message cannot be null");
            throw new RuntimeException("电表处理通道,消息为空");
        }
        if (sessionStoreService.containsKey(channel.id().asShortText())) {
            log.info("目前处理该通道的事件循环是：{}", channel.eventLoop().toString());
            log.info("目前处理该通道的事件循环组是：{}", channel.eventLoop().parent().toString());
            log.info("processElect");
            byte[] data = message.getUserData();
            if (data == null || data.length == 0) {
                log.info("Elect channel, message cannot be null");
                return;
            }
            channel.writeAndFlush(new MyElectAckMessage());
            List<ElectParamsRequest> requests = DealMeterUtils.dealElects(data);
            // boolean res = nettyClientsService.insertElect(requests);
            requests.forEach(request -> innerElectricityParamsService.insertElectParams(request));
        } else {
            log.info("you should login");
        }
    }
}
