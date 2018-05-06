package org.nettyperftest;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by m on 5/4/18.
 */
public class Server {

    public static void main(String[] args) throws Exception {

        int port = 9090;

        EventLoopGroup boss = new NioEventLoopGroup(1, new ThreadFactoryBuilder()
                .setNameFormat("accept-%d")
                .build());

        EventLoopGroup worker = new NioEventLoopGroup(0, new ThreadFactoryBuilder()
                .setNameFormat("worker-%d")
                .build());

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class);

        bootstrap.option(ChannelOption.SO_BACKLOG, 100)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        final Router router = new Router();

        bootstrap.childHandler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new LengthFieldPrepender(4));
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                ch.pipeline().addLast(new Decoder());
                ch.pipeline().addLast(new Encoder());
                ch.pipeline().addLast(router);
            }
        });

        bootstrap.bind("localhost", 9090).sync().channel().closeFuture().syncUninterruptibly();

        System.out.println("Starting server...");
    }

}
