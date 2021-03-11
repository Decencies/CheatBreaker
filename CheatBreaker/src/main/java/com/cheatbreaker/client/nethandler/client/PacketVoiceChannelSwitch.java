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
public class PacketVoiceChannelSwitch extends Packet {

    private UUID switchingTo;

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeUUID(this.switchingTo);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.switchingTo = buf.readUUID();
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerServer)handler).handleVoiceChannelSwitch(this);
    }

}
