package org.nettyperftest;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

/**
 * Created by m on 5/4/18.
 */
public class Message {

    long token;

    UUID id;

    public Message(long token, UUID id) {
        this.token = token;
        this.id = id;
    }

    public static Message deserialize(ByteBuf buf) {
        long token = buf.readLong();
        long lsb = buf.readLong();
        long msb = buf.readLong();
        return new Message(token, new UUID(msb, lsb));
    }

    public void serialize(ByteBuf outBuf) {
        outBuf.writeLong(this.token);
        outBuf.writeLong(this.id.getLeastSignificantBits());
        outBuf.writeLong(this.id.getMostSignificantBits());
    }
}
