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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Getter
public class PacketUpdateNametags extends Packet {

    private Map<UUID, List<String>> playersMap;

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeVarInt(this.playersMap == null ? -1 : this.playersMap.size());
        if (this.playersMap != null) {
            for (Map.Entry<UUID, List<String>> entry : this.playersMap.entrySet()) {
                UUID uuid = entry.getKey();
                List<String> tags = entry.getValue();
                buf.writeUUID(uuid);
                buf.writeVarInt(tags.size());
                tags.forEach(buf::writeString);
            }
        }
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        int playersMapSize = buf.readVarInt();
        if (playersMapSize == -1) {
            this.playersMap = null;
        } else {
            this.playersMap = new HashMap<>();
            for (int i = 0; i < playersMapSize; ++i) {
                UUID uuid = buf.readUUID();
                int tagsSize = buf.readVarInt();
                ArrayList<String> tags = new ArrayList<String>();
                for (int j = 0; j < tagsSize; ++j) {
                    tags.add(buf.readString());
                }
                this.playersMap.put(uuid, tags);
            }
        }
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerClient)handler).handleNametagsUpdate(this);
    }

}
