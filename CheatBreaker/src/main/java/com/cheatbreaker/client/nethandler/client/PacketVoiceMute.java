package com.cheatbreaker.client.nethandler.client;

import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.nethandler.Packet;
import com.cheatbreaker.client.nethandler.ICBNetHandler;
import com.cheatbreaker.client.nethandler.server.ICBNetHandlerServer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Getter
public class PacketVoiceMute extends Packet {

    private UUID muting;

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeUUID(this.muting);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.muting = buf.readUUID();
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerServer)handler).handleVoiceMute(this);
    }

}
