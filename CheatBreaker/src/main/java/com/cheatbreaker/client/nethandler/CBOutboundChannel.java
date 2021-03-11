package com.cheatbreaker.client.nethandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import javax.crypto.SecretKey;

public class CBOutboundChannel extends ChannelOutboundHandlerAdapter {
    private long long1 = 1L;
    private long long2 = 0L;
    private long long3;
    private final byte[] IIIIllIlIIIllIlllIlllllIl = "ZB9hEJy5l+u8QARAlX9T0w".getBytes();

    public CBOutboundChannel(SecretKey secretKey) {
        for (byte by : secretKey.getEncoded()) {
            this.long1 = (this.long1 + (long)(by & 0xFF)) % 65521L;
            this.long2 = (this.long2 + this.long1) % 65521L;
        }
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        ByteBuf byteBuf = (ByteBuf)object;
        while (byteBuf.readableBytes() > 0) {
            int n = byteBuf.readByte() & 0xFF;
            this.long1 = (this.long1 + (long)n) % 65521L;
            this.long2 = (this.long2 + this.long1) % 65521L;
        }
        byteBuf.readerIndex(0);
        for (byte by : this.IIIIllIlIIIllIlllIlllllIl) {
            this.long1 = (this.long1 + (long)(by & 0xFF)) % 65521L;
            this.long2 = (this.long2 + this.long1) % 65521L;
        }
        this.long3 = this.long2 << 16 | this.long1;
        super.write(channelHandlerContext, object, channelPromise);
    }

    public long lIIIIlIIllIIlIIlIIIlIIllI() {
        return this.long3;
    }
}
 