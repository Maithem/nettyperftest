package org.nettyperftest;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by m on 5/4/18.
 */
@ChannelHandler.Sharable
public class Router extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        Message m = (Message) msg;
        //System.out.println("Server received message");
        // do something with m
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(cause);
        ctx.close();
    }
}
