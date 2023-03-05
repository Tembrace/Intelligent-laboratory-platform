package com.shundei.netty.handle;

import com.shundei.netty.message.MyMessage;
import com.shundei.netty.store.SessionStoreService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主要的处理器处理各种类型的信息
 *
 * @author huoyouri
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class CoreHandler extends SimpleChannelInboundHandler<MyMessage> {

    /**
     * 通道构建好了才获得值的，所以是null
     */
    // @Value("${netty.read_time_out}")
    // private Integer readTimeOut;

    /**
     * 通道构建好了才获得值的，所以是null
     */
    // @Value("${netty.connect_count}")
    // private Integer connectCount;

    private static final int connectCount = 3;

    private static final int readTimeOut = 70;

    private AtomicInteger count;

    // TODO 这里能否用自动注入
    @Autowired
    private SessionStoreService sessionStoreService;

    private ProtocolProcess protocolProcess;

    public CoreHandler(ProtocolProcess protocolProcess) {
        this.protocolProcess = protocolProcess;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessage msg) {
        if (ObjectUtils.anyNull(ctx, msg)) {
            log.info("CoreHandler error, ctx, msg cannot be null");
            throw new RuntimeException("核心处理器处理异常，参数为空");
        }
        switch (msg.getMessageType()) {
            case REQ_LOGIN:
                protocolProcess.login().processLogin(ctx.channel());
                break;
            case REQ_PING:
                protocolProcess.ping().processPing(ctx.channel());
                break;
            case REQ_ENVIRON:
                protocolProcess.environ().processEnviron(ctx.channel(), msg);
                break;
            case REQ_ELECT:
                protocolProcess.elect().processElect(ctx.channel(), msg);
                break;
            default:
                break;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        count = new AtomicInteger(0);
        super.channelActive(ctx);
    }

    /**
     * TODO 这里的触发函数如果不在初始化的时候加上Idle触发器就是没用的，具体原因位置
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        super.userEventTriggered(ctx, evt);
        // 产生了读空闲事件
        if (event.state() == IdleState.READER_IDLE) {
            if (count.get() < connectCount) {
                log.info("{}s dont read the data", readTimeOut);
                // count++
                count.getAndIncrement();
                throw new RuntimeException("失去连接");
            } else {
                count = new AtomicInteger(0);
                String channelId = ctx.channel().id().asShortText();
                // ctx.channel().close().addListener(future -> {
                //     // 这里会抛异常，那么就不会继续往下执行
                //     try {
                //         if (sessionStoreService.containsKey(channelId)) {
                //             sessionStoreService.remove(channelId);
                //             log.info("DISCONNECT - clientId: {}", channelId);
                //         }
                //     }catch (NullPointerException e){
                //         log.info("the cache dont have the channel, you dont have login");
                //     }
                // });
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("exception:{}", cause.getMessage());
        // super.exceptionCaught(ctx, cause);
    }
}
