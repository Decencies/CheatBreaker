package com.cheatbreaker.client.nethandler.shared;

import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.nethandler.Packet;
import com.cheatbreaker.client.nethandler.ICBNetHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.IOException;

@AllArgsConstructor @NoArgsConstructor @Getter
public class PacketAddWaypoint extends Packet {

    private String name;
    private String world;
    private int color;
    private int x;
    private int y;
    private int z;
    private boolean forced;
    private boolean visible;

    @Override
    public void write(ByteBufWrapper buf) throws IOException {
        buf.writeString(this.name);
        buf.writeString(this.world);
        buf.buf().writeInt(this.color);
        buf.buf().writeInt(this.x);
        buf.buf().writeInt(this.y);
        buf.buf().writeInt(this.z);
        buf.buf().writeBoolean(this.forced);
        buf.buf().writeBoolean(this.visible);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.name = buf.readString();
        this.world = buf.readString();
        this.color = buf.buf().readInt();
        this.x = buf.buf().readInt();
        this.y = buf.buf().readInt();
        this.z = buf.buf().readInt();
        this.forced = buf.buf().readBoolean();
        this.visible = buf.buf().readBoolean();
    }

    @Override
    public void process(ICBNetHandler handler) {
        handler.handleAddWaypoint(this);
    }

}
