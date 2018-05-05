package org.nettyperftest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.concurrent.atomic.LongAccumulator;

/**
 * Created by m on 5/4/18.
 */
public class Encoder  extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          Message corfuMsg,
                          ByteBuf byteBuf) throws Exception {

            corfuMsg.serialize(byteBuf);
    }
}
