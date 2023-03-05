package com.shundei.netty.handle.process;

import com.shundei.netty.message.login.MyLoginAckMessageSuccess;
import com.shundei.netty.message.login.MyLoginAckMessageTime;
import com.shundei.netty.store.SessionStore;
import com.shundei.netty.store.SessionStoreService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;


/**
 * 登录消息处理类
 *
 * @author huoyouri
 */
@Slf4j
public class Login {

    private SessionStoreService sessionStoreService;

    public Login(SessionStoreService sessionStoreService) {
        this.sessionStoreService = sessionStoreService;
    }

    public void processLogin(Channel channel) {
        if (channel == null) {
            log.info("Login channel cannot be null");
            throw new RuntimeException("登录通道为空");
        }
        log.info("目前处理该通道的事件循环是：{}", channel.eventLoop().toString());
        log.info("目前处理该通道的事件循环组是：{}", channel.eventLoop().parent().toString());
        log.info("processLogin");
        // 存储信息，id后续增添网关编号再改
        sessionStoreService.put(
                channel.id().asShortText(),
                new SessionStore(channel.id().asLongText(), channel));
        channel.writeAndFlush(new MyLoginAckMessageSuccess()).addListener(future -> {
            channel.writeAndFlush(new MyLoginAckMessageTime());
        });
        log.info("LOGIN - clientId: {}", channel.id().asShortText());
    }
}
