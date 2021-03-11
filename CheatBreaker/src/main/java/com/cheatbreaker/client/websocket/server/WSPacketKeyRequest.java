package com.cheatbreaker.client.websocket.server;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;

import java.beans.ConstructorProperties;

public class WSPacketKeyRequest extends WSPacket {
    private byte[] publicKey;

    @Override
    public void write(ByteBufWrapper buf) {
        this.writeKey(buf.buf(), this.publicKey);
    }

    @Override
    public void read(ByteBufWrapper buf) {
        this.publicKey = this.readKey(buf.buf());
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleKeyRequest(this);
    }

    public WSPacketKeyRequest() {
    }

    @ConstructorProperties(value={"publicKey"})
    public WSPacketKeyRequest(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public byte[] getPublicKey() {
        return this.publicKey;
    }
}