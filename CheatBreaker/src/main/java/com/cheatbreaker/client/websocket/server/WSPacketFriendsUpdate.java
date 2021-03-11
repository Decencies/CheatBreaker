package com.cheatbreaker.client.websocket.server;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WSPacketFriendsUpdate extends WSPacket {
    private boolean consoleAllowed;
    private boolean IlllIIIlIlllIllIlIIlllIlI;
    private Map<String, List<String>> IIIIllIlIIIllIlllIlllllIl;
    private Map<String, List<String>> IIIIllIIllIIIIllIllIIIlIl;

    @Override
    public void write(ByteBufWrapper lIlIllllllllIlIIIllIIllII2) {
    }

    @Override
    public void read(ByteBufWrapper lIlIllllllllIlIIIllIIllII2) throws IOException {
        int n;
        this.consoleAllowed = lIlIllllllllIlIIIllIIllII2.buf().readBoolean();
        this.IlllIIIlIlllIllIlIIlllIlI = lIlIllllllllIlIIIllIIllII2.buf().readBoolean();
        int n2 = lIlIllllllllIlIIIllIIllII2.buf().readInt();
        int n3 = lIlIllllllllIlIIIllIIllII2.buf().readInt();
        this.IIIIllIlIIIllIlllIlllllIl = new HashMap<>();
        for (n = 0; n < n2; ++n) {
            this.IIIIllIlIIIllIlllIlllllIl.put(
                    lIlIllllllllIlIIIllIIllII2.readStringFromBuffer(52),
                    ImmutableList.of(
                            lIlIllllllllIlIIIllIIllII2.readStringFromBuffer(32),
                            String.valueOf(lIlIllllllllIlIIIllIIllII2.buf().readInt()),
                            lIlIllllllllIlIIIllIIllII2.readStringFromBuffer(256)
                    )
            );
        }
        this.IIIIllIIllIIIIllIllIIIlIl = new HashMap<>();
        for (n = 0; n < n3; ++n) {
            this.IIIIllIIllIIIIllIllIIIlIl.put(
                    lIlIllllllllIlIIIllIIllII2.readStringFromBuffer(52),
                    ImmutableList.of(
                            lIlIllllllllIlIIIllIIllII2.readStringFromBuffer(32),
                            String.valueOf(lIlIllllllllIlIIIllIIllII2.buf().readLong())
                    )
            );
        }
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleFriendsUpdate(this);
    }

    public boolean isConsoleAllowed() {
        return this.consoleAllowed;
    }

    public boolean isAcceptingFriendRequests() {
        return this.IlllIIIlIlllIllIlIIlllIlI;
    }

    public Map<String, List<String>> getOnlineMap() {
        return this.IIIIllIlIIIllIlllIlllllIl;
    }

    public Map<String, List<String>> getOfflineMap() {
        return this.IIIIllIIllIIIIllIllIIIlIl;
    }
}
