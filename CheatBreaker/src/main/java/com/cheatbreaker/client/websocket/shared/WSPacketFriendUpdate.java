package com.cheatbreaker.client.websocket.shared;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;

import java.io.IOException;

public class WSPacketFriendUpdate
        extends WSPacket {
    private String playerId;
    private String name;
    private long offlineSince;
    private boolean online;

    public WSPacketFriendUpdate() {
    }

    public WSPacketFriendUpdate(String playerId, String name, long offlineSince, boolean online) {
        this.playerId = playerId;
        this.name = name;
        this.offlineSince = offlineSince;
        this.online = online;
    }

    @Override
    public void write(ByteBufWrapper buf) {
        buf.writeString(this.playerId);
        buf.writeString(this.name);
        buf.buf().writeLong(this.offlineSince);
        buf.buf().writeBoolean(this.online);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.playerId = buf.readStringFromBuffer(52);
        this.name = buf.readStringFromBuffer(32);
        this.offlineSince = buf.buf().readLong();
        this.online = buf.buf().readBoolean();
    }

    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleFriendUpdate(this);
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public String getName() {
        return this.name;
    }

    public long getOfflineSince() {
        return this.offlineSince;
    }

    public boolean isOnline() {
        return this.online;
    }
}
