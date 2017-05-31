package com.qiaoyy.netty;

import com.qiaoyy.core.AppInit;
import com.qiaoyy.log.AppLog;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * WebSocket服务
 */
public class WebSocketServer {
    public static WebSocketServer inst;
    public static int port;
    private NioEventLoopGroup bossGroup = null;
    private NioEventLoopGroup ioGroup = null;

    private WebSocketServer() {
    }

    public static WebSocketServer getInstance() {
        if (inst == null) {
            inst = new WebSocketServer();
            inst.initData();
        }
        return inst;
    }

    public void initData() {
        try {
            port = Integer.parseInt(AppInit.run.getEnvironment().getProperty("app.port"));
        } catch (Exception e) {
            AppLog.stdout("app.config.err");
            e.printStackTrace();
        }
    }

    /**
     * 启动WebSocket
     */
    public void start() {
        bossGroup = new NioEventLoopGroup(8, new DefaultThreadFactory("BOSS_LOOP"));
        ioGroup = new NioEventLoopGroup(8, new DefaultThreadFactory("IO_LOOP"));
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, ioGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("http-codec", new HttpServerCodec());
                pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                pipeline.addLast("handler", new WebSocketServerHandler());
            }
        });
        // 启动端口
        ChannelFuture future;
        try {
            future = bootstrap.bind(port).sync();
            if (future.isSuccess()) {
                AppLog.LOG_COMMON.info("webSocket.port.bind.ok - {}", port);
            }
        } catch (InterruptedException e) {
            AppLog.stdout("webSocket.port.bind.err - {}", port);
        }
    }

    public void shut() {
        if (ioGroup != null) {
            ioGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        AppLog.LOG_COMMON.info("webSocket.port.unbind - {}", port);
    }
}
