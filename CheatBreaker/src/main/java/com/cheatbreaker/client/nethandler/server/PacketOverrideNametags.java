package com.cheatbreaker.client.nethandler.server;

import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.nethandler.Packet;
import com.cheatbreaker.client.nethandler.ICBNetHandler;
import com.cheatbreaker.client.nethandler.client.ICBNetHandlerClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Getter
public class PacketOverrideNametags extends Packet {

    private UUID player;
    private List<String> tags;

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeUUID(this.player);
        buf.writeOptional(this.tags, t -> {
            buf.writeVarInt(t.size());
            t.forEach(buf::writeString);
        });
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.player = buf.readUUID();
        this.tags = buf.readOptional(() -> {
            int tagsSize = buf.readVarInt();
            ArrayList<String> tags = new ArrayList<>();
            for (int i = 0; i < tagsSize; ++i) {
                tags.add(buf.readString());
            }
            return tags;
        });
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerClient)handler).handleOverrideNametags(this);
    }

}
