package com.cheatbreaker.client.websocket.client;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;
import net.minecraft.util.CryptManager;

import java.security.PublicKey;
import javax.crypto.SecretKey;

public class WSPacketClientJoinServerResponse
        extends WSPacket {
    private byte[] lIIIIlIIllIIlIIlIIIlIIllI;
    private byte[] IlllIIIlIlllIllIlIIlllIlI;

    public WSPacketClientJoinServerResponse(SecretKey secretKey, PublicKey publicKey, byte[] arrby) {
        this.lIIIIlIIllIIlIIlIIIlIIllI = CryptManager.encryptData(publicKey, secretKey.getEncoded());
        this.IlllIIIlIlllIllIlIIlllIlI = CryptManager.decryptData(publicKey, arrby);
    }

    @Override
    public void write(ByteBufWrapper bufWrapper) {
        this.writeKey(bufWrapper.buf(), this.lIIIIlIIllIIlIIlIIIlIIllI);
        this.writeKey(bufWrapper.buf(), this.IlllIIIlIlllIllIlIIlllIlI);
    }

    @Override
    public void read(ByteBufWrapper bufWrapper) {
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
    }
}

