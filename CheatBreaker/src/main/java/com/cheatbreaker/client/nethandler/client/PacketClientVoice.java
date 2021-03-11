package com.cheatbreaker.client.nethandler.client;

import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.nethandler.Packet;
import com.cheatbreaker.client.nethandler.ICBNetHandler;
import com.cheatbreaker.client.nethandler.server.ICBNetHandlerServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@AllArgsConstructor @NoArgsConstructor @Getter
public class PacketClientVoice extends Packet {

    private byte[] data;

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        this.writeBlob(buf, this.data);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.data = this.readBlob(buf);
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerServer)handler).handleVoice(this);
    }

}
