package com.cheatbreaker.client.websocket.shared;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;

import java.io.IOException;

public class WSPacketMessage
        extends WSPacket {
    private String playerId;
    private String message;

    public WSPacketMessage() {
    }

    public WSPacketMessage(String playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }

    @Override
    public void write(ByteBufWrapper buf) {
        buf.writeString(this.playerId);
        buf.writeString(this.message);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.playerId = buf.readStringFromBuffer(52);
        this.message = buf.readStringFromBuffer(1024);
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleMessage(this);
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public String getMessage() {
        return this.message;
    }
}

