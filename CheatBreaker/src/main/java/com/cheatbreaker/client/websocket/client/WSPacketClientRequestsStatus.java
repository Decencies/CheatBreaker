package com.cheatbreaker.client.websocket.client;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;

public class WSPacketClientRequestsStatus
        extends WSPacket {
    private boolean accepting;

    public WSPacketClientRequestsStatus(boolean bl) {
        this.accepting = bl;
    }

    public WSPacketClientRequestsStatus() {
    }

    @Override
    public void write(ByteBufWrapper lIlIllllllllIlIIIllIIllII2) {
        lIlIllllllllIlIIIllIIllII2.buf().writeBoolean(this.accepting);
    }

    @Override
    public void read(ByteBufWrapper lIlIllllllllIlIIIllIIllII2) {
        this.accepting = lIlIllllllllIlIIIllIIllII2.buf().readBoolean();
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
    }

    public boolean isAccepting() {
        return this.accepting;
    }
}

