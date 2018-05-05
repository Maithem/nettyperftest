package org.nettyperftest;

import io.netty.buffer.ByteBuf;

/**
 * Created by m on 5/4/18.
 */
public class Message {

    final ByteBuf buf;

    public Message(ByteBuf buf) {
        this.buf = buf;
    }

    public static Message deserialize(ByteBuf buf) {
        return new Message(buf);
    }

    public void serialize(ByteBuf buf) {
        buf.writeBytes(buf);
    }
}
