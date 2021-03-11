package com.cheatbreaker.client.nethandler.server;

import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.nethandler.Packet;
import com.cheatbreaker.client.nethandler.ICBNetHandler;
import com.cheatbreaker.client.nethandler.client.ICBNetHandlerClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor @Getter
public class PacketVoiceChannel extends Packet {

    private UUID uuid;
    private String name;
    private Map<UUID, String> players;
    private Map<UUID, String> listening;

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeUUID(this.uuid);
        buf.writeString(this.name);
        this.writeMap(buf, this.players);
        this.writeMap(buf, this.listening);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.uuid = buf.readUUID();
        this.name = buf.readString();
        this.players = this.readMap(buf);
        this.listening = this.readMap(buf);
    }

    private void writeMap(ByteBufWrapper out, Map<UUID, String> players) {
        out.writeVarInt(players.size());
        players.forEach((key, value) -> {
            out.writeUUID(key);
            out.writeString(value);
        });
    }

    private Map<UUID, String> readMap(ByteBufWrapper in) {
        int size = in.readVarInt();
        HashMap<UUID, String> players = new HashMap<>();
        for (int i = 0; i < size; ++i) {
            UUID uuid = in.readUUID();
            String name = in.readString();
            players.put(uuid, name);
        }
        return players;
    }

    @Override
    public void process(ICBNetHandler handler) {
        ((ICBNetHandlerClient)handler).handleVoiceChannels(this);
    }

}
