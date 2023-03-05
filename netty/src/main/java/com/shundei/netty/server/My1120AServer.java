package com.shundei.netty.server;

import com.shundei.netty.handle.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * Server
 *
 * @author huoyouri
 */
@Component
@Slf4j
public class My1120AServer {
    @Value("${netty.bind_address}")
    private String host;
    @Value("${netty.bind_port}")
    private Integer port;
    @Value("${netty.leak_detector_level}")
    private String leakDetectorLevel;
    @Value("${netty.boss_group_thread_count}")
    private Integer bossGroupThreadCount;
    @Value("${netty.worker_group_thread_count}")
    private Integer workerGroupThreadCount;
    @Value("${netty.read_time_out}")
    private Integer readTimeOut;
    @Autowired
    private ProtocolProcess protocolProcess;

    private Channel serverChannel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private MessageCodecSharable messageCodecSharable;
    private ContentCodec contentCodec;
    private LoggingHandler loggingHandler;

    @PostConstruct
    public void init() throws Exception {
        log.info("Setting resource leak detector level to {}", leakDetectorLevel);
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.valueOf(leakDetectorLevel.toUpperCase()));
        log.info("Starting 376.1 netty server...");
        bossGroup = new NioEventLoopGroup(bossGroupThreadCount);
        workerGroup = new NioEventLoopGroup(workerGroupThreadCount);
        messageCodecSharable = new MessageCodecSharable();
        contentCodec = new ContentCodec();
        loggingHandler = new LoggingHandler(LogLevel.INFO);
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("ping", new IdleStateHandler(readTimeOut, 0, 0));
                        pipeline.addLast("loggingHandler", loggingHandler);
                        pipeline.addLast("protocolDecoder", new ProtocolDecoder());
                        pipeline.addLast("messageCodec", messageCodecSharable);
                        pipeline.addLast("contentCodec", contentCodec);
                        pipeline.addLast("coreHandler", new CoreHandler(protocolProcess));
                    }
                });
        serverChannel = b.bind(host, port).sync().channel();
        log.info("Starting successful 376.1 netty server");
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {
        log.info("Stopping netty server!");
        try {
            serverChannel.close().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        log.info("netty server stopped!");
    }
}
