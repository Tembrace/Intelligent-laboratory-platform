package com.shundei.netty.handle;

import com.shundei.netty.clients.NettyClientsService;
import com.shundei.netty.handle.process.Elect;
import com.shundei.netty.handle.process.Environ;
import com.shundei.netty.handle.process.Login;
import com.shundei.netty.handle.process.Ping;
import com.shundei.netty.store.SessionStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 协议处理
 *
 * @author huoyouri
 */
@Component
@Slf4j
public class ProtocolProcess {

    @Autowired
    private SessionStoreService sessionStoreService;

    @Autowired
    private NettyClientsService nettyClientsService;

    private Login login;

    private Ping ping;

    private Environ environ;

    private Elect elect;

    public Login login() {
        if (login == null) {
            login = new Login(sessionStoreService);
        }
        return login;
    }

    public Ping ping() {
        if (ping == null) {
            ping = new Ping();
        }
        return ping;
    }

    public Environ environ() {
        if (environ == null) {
            environ = new Environ(nettyClientsService, sessionStoreService);
        }
        return environ;
    }

    public Elect elect() {
        if (elect == null) {
            elect = new Elect(nettyClientsService, sessionStoreService);
        }
        return elect;
    }
}
