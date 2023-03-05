package com.shundei.netty.handle.process;

import com.shundei.netty.clients.NettyClientsService;
import com.shundei.netty.common.DealMeterUtils;
import com.shundei.netty.message.MyMessage;
import com.shundei.netty.message.environ.MyEnvironAckMessage;
import com.shundei.netty.store.SessionStoreService;
import com.tang.backendcommon.dto.EnvironmentParamsRequest;
import com.tang.backendcommon.inner.InnerEnvironmentParamsService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;

import java.util.List;

/**
 * 环境表信息处理类
 *
 * @author huoyouri
 */
@Slf4j
@EnableDubbo
public class Environ {

    // private NettyClientsService nettyClientsService;
    private SessionStoreService sessionStoreService;

    @DubboReference
    private InnerEnvironmentParamsService innerEnvironmentParamsService;

    public Environ(NettyClientsService nettyClientsService, SessionStoreService sessionStoreService) {
        // this.nettyClientsService = nettyClientsService;
        this.sessionStoreService = sessionStoreService;
    }

    public void processEnviron(Channel channel, MyMessage message) {
        long begin = System.currentTimeMillis();
        if (channel == null || message == null) {
            log.info("Environ channel, message cannot be null");
            throw new RuntimeException("环境表处理通道,消息为空");
        }
        if (sessionStoreService.containsKey(channel.id().asShortText())) {
            // 已登录
            log.info("目前处理该通道的事件循环是：{}", channel.eventLoop().toString());
            log.info("目前处理该通道的事件循环组是：{}", channel.eventLoop().parent().toString());
            log.info("processEnviron");
            byte[] data = message.getUserData();
            if (data == null || data.length == 0) {
                log.info("Environ channel, message cannot be null");
                return;
            }
            channel.writeAndFlush(new MyEnvironAckMessage());
            List<EnvironmentParamsRequest> requests = DealMeterUtils.dealEnvirons(data);
            // boolean res = nettyClientsService.insertEnviron(requests);
            requests.forEach(request -> innerEnvironmentParamsService.insertEnvironParams(request));
            log.info("time:{}", System.currentTimeMillis() - begin);
        } else {
            log.info("you should login");
        }
    }
}
