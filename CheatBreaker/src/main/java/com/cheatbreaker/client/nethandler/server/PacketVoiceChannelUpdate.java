package com.cheatbreaker.client.nethandler.server;

import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.nethandler.Packet;
import com.cheatbreaker.client.nethandler.ICBNetHandler;
import com.cheatbreaker.client.nethandler.client.ICBNetHandlerClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Getter @ToString
public class PacketVoiceChannelUpdate extends Packet {

    public int status;
    private UUID channelUuid;
    private UUID uuid;
    private String name;

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeVarInt(this.status);
        buf.writeUUID(this.channelUuid);
        buf.writeUUID(this.uuid);
        buf.writeString(this.name);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.status = buf.readVarInt();
        this.channelUuid = buf.readUUID();
        this.uuid = buf.readUUID();
        this.name = buf.readString();
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerClient)handler).handleVoiceChannelUpdate(this);
    }

}
