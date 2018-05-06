package org.nettyperftest;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by m on 5/4/18.
 */
public class Client  extends SimpleChannelInboundHandler<Message> {

    public volatile Channel channel;

    public void start(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup(0, new ThreadFactoryBuilder()
                .setNameFormat("client-worker-%d")
                .build());

        final EventExecutorGroup ee = new DefaultEventExecutorGroup(4, new ThreadFactoryBuilder()
                .setNameFormat("event-%d")
                .build());

        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.SO_REUSEADDR, true);
        b.option(ChannelOption.TCP_NODELAY, true);

        final Client router = this;

        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LengthFieldPrepender(4));
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                ch.pipeline().addLast(ee, new Decoder());
                ch.pipeline().addLast(ee, new Encoder());
                ch.pipeline().addLast(ee, router);
            }
        });

        ChannelFuture cf = b.connect(host, port);
        cf.syncUninterruptibly();
        channel = cf.channel();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message m) throws Exception {
        System.out.println("Client received message");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(cause);
        ctx.close();
    }
}
