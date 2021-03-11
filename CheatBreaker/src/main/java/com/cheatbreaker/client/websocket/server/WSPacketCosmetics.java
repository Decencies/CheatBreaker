package com.cheatbreaker.client.websocket.server;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.util.cosmetic.Cosmetic;
import com.cheatbreaker.client.websocket.WSPacket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WSPacketCosmetics
        extends WSPacket {
    private List<Cosmetic> cosmetics;
    private String playerId;

    public WSPacketCosmetics() {
    }

    public WSPacketCosmetics(String string, List<Cosmetic> list) {
        this.playerId = string;
        this.cosmetics = list;
    }

    @Override
    public void write(ByteBufWrapper buf) {
        buf.buf().writeInt(this.cosmetics.size());
        for (Cosmetic cosmetic : this.cosmetics) {
            buf.writeString(cosmetic.getName());
            buf.buf().writeFloat(cosmetic.getScale());
            buf.writeString(cosmetic.getLocation().toString());
            buf.buf().writeBoolean(cosmetic.isEquipped());
        }
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.playerId = buf.readStringFromBuffer(52);
        int n = buf.buf().readInt();
        this.cosmetics = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            float scale = buf.buf().readFloat();
            boolean bl = buf.buf().readBoolean();
            String string = buf.readStringFromBuffer(512);
            String string2 = buf.readStringFromBuffer(128);
            this.cosmetics.add(new Cosmetic(this.playerId, string2, scale, bl, string));
        }
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleCosmetics(this);
    }

    public List<Cosmetic> getCosmetics() {
        return this.cosmetics;
    }

    public String getPlayerId() {
        return this.playerId;
    }
}