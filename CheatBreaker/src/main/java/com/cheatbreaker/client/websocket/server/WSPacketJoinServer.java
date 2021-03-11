package com.cheatbreaker.client.websocket.server;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;
import net.minecraft.util.CryptManager;

import java.security.PublicKey;

public class WSPacketJoinServer
        extends WSPacket {
    private PublicKey publicKey;
    private byte[] IlllIIIlIlllIllIlIIlllIlI;

    @Override
    public void write(ByteBufWrapper lIlIllllllllIlIIIllIIllII2) {
    }

    @Override
    public void read(ByteBufWrapper lIlIllllllllIlIIIllIIllII2) {
        this.publicKey = CryptManager.decodePublicKey(this.readKey(lIlIllllllllIlIIIllIIllII2.buf()));
        this.IlllIIIlIlllIllIlIIlllIlI = this.readKey(lIlIllllllllIlIIIllIIllII2.buf());
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleJoinServer(this);
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public byte[] lIIIIIIIIIlIllIIllIlIIlIl() {
        return this.IlllIIIlIlllIllIlIIlllIlI;
    }
}

