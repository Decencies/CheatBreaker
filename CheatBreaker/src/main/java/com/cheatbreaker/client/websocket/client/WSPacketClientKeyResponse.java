package com.cheatbreaker.client.websocket.client;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;

import java.beans.ConstructorProperties;

public class WSPacketClientKeyResponse
        extends WSPacket {
    private byte[] data;

    @Override
    public void write(ByteBufWrapper buf) {
        this.writeKey(buf.buf(), this.data);
    }

    @Override
    public void read(ByteBufWrapper buf) {
        this.data = this.readKey(buf.buf());
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
    }

    public byte[] getData() {
        return this.data;
    }

    @ConstructorProperties(value={"data"})
    public WSPacketClientKeyResponse(byte[] data) {
        this.data = data;
    }

    public WSPacketClientKeyResponse() {
    }
}

