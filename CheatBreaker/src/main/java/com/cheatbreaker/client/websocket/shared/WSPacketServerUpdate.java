package com.cheatbreaker.client.websocket.shared;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;

import java.io.IOException;

public class WSPacketServerUpdate
        extends WSPacket {
    private String server;
    private String playerId;

    public WSPacketServerUpdate() {
    }

    public WSPacketServerUpdate(String playerId, String string2) {
        this.playerId = playerId;
        this.server = string2;
    }

    @Override
    public void write(ByteBufWrapper buf) {
        buf.writeString(this.playerId);
        buf.writeString(this.server);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.playerId = buf.readStringFromBuffer(52);
        this.server = buf.readStringFromBuffer(100);
    }

    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleServerUpdate(this);
    }

    public String getServer() {
        return this.server;
    }

    public String getPlayerId() {
        return this.playerId;
    }
}