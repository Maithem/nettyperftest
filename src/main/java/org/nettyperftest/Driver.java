package org.nettyperftest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by m on 5/5/18.
 */
public class Driver {

    public static void main(String[] args) throws Exception {

        Client client = new Client();

        client.start("localhost", 9090);

        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(1);
        client.channel.writeAndFlush(new Message(buf)).get();
    }
}
