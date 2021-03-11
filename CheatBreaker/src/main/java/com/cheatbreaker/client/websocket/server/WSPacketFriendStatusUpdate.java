package com.cheatbreaker.client.websocket.server;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;

import java.io.IOException;

public class WSPacketFriendStatusUpdate
        extends WSPacket {
    private String playerId;
    private String name;
    private boolean friend;

    public WSPacketFriendStatusUpdate() {
    }

    public WSPacketFriendStatusUpdate(String playerId, String string2, boolean bl) {
        this.playerId = playerId;
        this.name = string2;
        this.friend = bl;
    }

    @Override
    public void write(ByteBufWrapper buf) {
        buf.writeString(this.playerId);
        buf.writeString(this.name);
        buf.buf().writeBoolean(this.friend);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.playerId = buf.readStringFromBuffer(52);
        this.name = buf.readStringFromBuffer(32);
        this.friend = buf.buf().readBoolean();
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleFriendRequest(this, true);
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public String getName() {
        return this.name;
    }

    public boolean isFriend() {
        return this.friend;
    }
}
