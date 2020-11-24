package com.xw.webchatroom.NettyServer;

import com.xw.webchatroom.controller.UserController;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author xiongwei
 * @WriteTime 2020-11-20 15:42
 */


public class MyServer {

    private int port;

    //绑定的users
    public static Map<String, Channel> users = new ConcurrentHashMap<>();

    public MyServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        //创建二个线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try{

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline pipeline = ch.pipeline();

                            //基于http协议  使用 http 编码 解码器
                            pipeline.addLast(new HttpServerCodec());
                            //以块方式写   添加ChunkedWriteHandler 处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            /*
                            http 数据在传输过程中是分段的  HttpObjectAggregator  将多个段聚合
                            当浏览器发送大量数据时  就会发出多次 http 请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));


                            //自定义的handler  处理业务逻辑
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                            /*
                            对应 WebSocket   数据以帧 形式传递
                            WebSocketServerProtocolHandler  核心功能 将http 升级为 ws 协议 保持长连接
                            是通过状态码 101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536 * 10));

                        }
                    });


            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println(MyServer.class + " 启动正在监听： " + channelFuture.channel().localAddress());

            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


}
