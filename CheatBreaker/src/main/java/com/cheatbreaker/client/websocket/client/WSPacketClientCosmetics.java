package com.cheatbreaker.client.websocket.client;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.util.cosmetic.Cosmetic;
import com.cheatbreaker.client.websocket.WSPacket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WSPacketClientCosmetics
        extends WSPacket {
    private List<Cosmetic> cosmetics;

    public WSPacketClientCosmetics() {
    }

    public WSPacketClientCosmetics(List<Cosmetic> list) {
        this.cosmetics = list;
    }

    @Override
    public void write(ByteBufWrapper buf) {
        buf.buf().writeInt(this.cosmetics.size());
        for (Cosmetic cosmetic : this.cosmetics) {
            buf.writeString(cosmetic.getName());
            buf.buf().writeFloat(cosmetic.getScale());
            buf.writeString(cosmetic.getLocation().toString().replaceFirst("minecraft:", ""));
            buf.buf().writeBoolean(cosmetic.isEquipped());
        }
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        int n = buf.buf().readInt();
        this.cosmetics = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            String string = buf.readStringFromBuffer(128);
            float f = buf.buf().readFloat();
            String string2 = buf.readStringFromBuffer(512);
            boolean bl = buf.buf().readBoolean();
            Cosmetic cosmetic = new Cosmetic("", string, f, bl, string2);
            this.cosmetics.add(cosmetic);
        }
    }

    @Override
    public void handle(AssetsWebSocket handler) {
    }

    public List<Cosmetic> getCosmetics() {
        return this.cosmetics;
    }
}
