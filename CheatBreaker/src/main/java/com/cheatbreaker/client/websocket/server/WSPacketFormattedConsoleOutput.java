package com.cheatbreaker.client.websocket.server;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;

import java.io.IOException;

public class WSPacketFormattedConsoleOutput
        extends WSPacket {
    private String lIIIIlIIllIIlIIlIIIlIIllI;
    private String IlllIIIlIlllIllIlIIlllIlI;

    public WSPacketFormattedConsoleOutput() {
    }

    public WSPacketFormattedConsoleOutput(String string, String string2) {
        this.lIIIIlIIllIIlIIlIIIlIIllI = string;
        this.IlllIIIlIlllIllIlIIlllIlI = string2;
    }

    @Override
    public void write(ByteBufWrapper buf) {
        buf.writeString(this.lIIIIlIIllIIlIIlIIIlIIllI);
        buf.writeString(this.IlllIIIlIlllIllIlIIlllIlI);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.lIIIIlIIllIIlIIlIIIlIIllI = buf.readStringFromBuffer(128);
        this.IlllIIIlIlllIllIlIIlllIlI = buf.readStringFromBuffer(512);
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleFormattedConsoleOutput(this);
    }

    public String getPrefix() {
        return this.lIIIIlIIllIIlIIlIIIlIIllI;
    }

    public String getContent() {
        return this.IlllIIIlIlllIllIlIIlllIlI;
    }
}
