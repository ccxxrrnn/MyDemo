package com.xw.webchatroom.NettyServer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author xiongwei
 * @WriteTime 2020-11-20 15:50
 */

public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //这是一个channel组
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd hh:mm:ss"));


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        System.out.println("服务器获得消息" + msg.text());

        String[] s = msg.text().split(" ");
        //from
        String from = s[0];
        String to = s[1].toUpperCase().equals("ALL") ? null:s[1];
        String text = s[2];

        for (Channel ch:channels) {
            ch.writeAndFlush(new TextWebSocketFrame( date+"\n"
                    + from + ":" + "\t" + (to == null?"":"@" + to)+" "
                    +text));
        }

    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        //asLongText 唯一值
        System.out.println("HandlerAdded 被调用" + ctx.channel().id().asLongText());
        channels.add(ctx.channel());
        //被添加，channel也需要响应
        for (Channel ch : channels){
            ch.writeAndFlush(new TextWebSocketFrame(date+"\n"+ctx.name() + "加入"));
        }
        //有人加入，我们需要刷新页面
        System.out.println("HandlerAdded 被调用" + ctx.channel().id().asShortText()); //不唯一
        System.out.println(channels);
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        channels.remove(ctx.channel());
        for (Channel ch : channels){
            ch.writeAndFlush(date);
            ch.writeAndFlush(new TextWebSocketFrame(date+"\n"+"username" + "离开"));
        }
        System.out.println("handlerRemoved 被调用" + ctx.channel().id().asLongText());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println("异常发生" + cause.getMessage());
        ctx.close();
    }
}
