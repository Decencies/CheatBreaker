package com.cheatbreaker.client.websocket.shared;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;

import java.io.IOException;

public class WSPacketClientFriendRequestUpdate
        extends WSPacket {
    private boolean add;
    private String playerId;

    public WSPacketClientFriendRequestUpdate() {
    }

    public WSPacketClientFriendRequestUpdate(boolean bl, String string) {
        this.add = bl;
        this.playerId = string;
    }

    @Override
    public void write(ByteBufWrapper buf) {
        buf.buf().writeBoolean(this.add);
        buf.writeString(this.playerId);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.add = buf.buf().readBoolean();
        this.playerId = buf.readStringFromBuffer(52);
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleFriendRequestUpdate(this);
    }

    public boolean isAdd() {
        return this.add;
    }

    public String lIIIIIIIIIlIllIIllIlIIlIl() {
        return this.playerId;
    }
}