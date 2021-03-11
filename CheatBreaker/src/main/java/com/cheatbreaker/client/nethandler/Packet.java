package com.cheatbreaker.client.nethandler;

import com.cheatbreaker.client.nethandler.client.PacketClientVoice;
import com.cheatbreaker.client.nethandler.client.PacketVoiceChannelSwitch;
import com.cheatbreaker.client.nethandler.client.PacketVoiceMute;
import com.cheatbreaker.client.nethandler.server.*;
import com.cheatbreaker.client.nethandler.shared.PacketAddWaypoint;
import com.cheatbreaker.client.nethandler.shared.PacketRemoveWaypoint;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.buffer.Unpooled;
import lombok.Getter;

import java.io.IOException;

public abstract class Packet {

    private static final BiMap<Class<? extends Packet>, Integer> REGISTRY = HashBiMap.create();

    @Getter private Object attachment;

    public abstract void write(ByteBufWrapper buf) throws IOException;

    public abstract void read(ByteBufWrapper buf) throws IOException;

    public abstract void process(ICBNetHandler handler);

    public static Packet handle(ICBNetHandler netHandler, byte[] data) {
        return Packet.handle(netHandler, data, null);
    }

    public static Packet handle(ICBNetHandler netHandler, byte[] data, Object attachment) {
        ByteBufWrapper wrappedBuffer = new ByteBufWrapper(Unpooled.wrappedBuffer(data));
        int packetId = wrappedBuffer.readVarInt();
        Class<? extends Packet> packetClass = REGISTRY.inverse().get(packetId);
        if (packetClass != null) {
            try {
                Packet packet = packetClass.newInstance();
                if (attachment != null) {
                    packet.attach(attachment);
                }
                packet.read(wrappedBuffer);
                packet.process(netHandler);
                return packet;
            }
            catch (IOException | IllegalAccessException | InstantiationException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] getPacketData(Packet packet) {
        ByteBufWrapper wrappedBuffer = new ByteBufWrapper(Unpooled.buffer());
        wrappedBuffer.writeVarInt(REGISTRY.get(packet.getClass()));
        try {
            packet.write(wrappedBuffer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return wrappedBuffer.buf().array();
    }

    private static void addPacket(int id, Class<? extends Packet> clazz) {
        if (REGISTRY.containsKey(clazz)) {
            throw new IllegalArgumentException("Duplicate packet class (" + clazz.getSimpleName() + "), already used by " + REGISTRY.get(clazz));
        }
        if (REGISTRY.containsValue(id)) {
            throw new IllegalArgumentException("Duplicate packet ID (" + id + "), already used by " + REGISTRY.inverse().get(id));
        }
        REGISTRY.put(clazz, id);
    }

    protected void writeBlob(ByteBufWrapper b, byte[] bytes) {
        b.buf().writeShort(bytes.length);
        b.buf().writeBytes(bytes);
    }

    protected byte[] readBlob(ByteBufWrapper b) {
        short key = b.buf().readShort();
        if (key < 0) {
            System.out.println("Key was smaller than nothing!  Weird key!");
            return null;
        }
        byte[] blob = new byte[key];
        b.buf().readBytes(blob);
        return blob;
    }

    public void attach(Object obj) {
        this.attachment = obj;
    }

    static {
        Packet.addPacket(0, PacketAddWaypoint.class);
        Packet.addPacket(2, PacketRemoveWaypoint.class);
        Packet.addPacket(3, PacketCooldown.class);
        Packet.addPacket(4, PacketNotification.class);
        Packet.addPacket(5, PacketStaffModState.class);
        Packet.addPacket(6, PacketUpdateNametags.class);
        Packet.addPacket(7, PacketTeammates.class);
        Packet.addPacket(8, PacketOverrideNametags.class);
        Packet.addPacket(9, PacketAddHologram.class);
        Packet.addPacket(10, PacketUpdateHologram.class);
        Packet.addPacket(11, PacketRemoveHologram.class);
        Packet.addPacket(12, PacketTitle.class);
        Packet.addPacket(14, PacketServerRule.class);
        Packet.addPacket(15, PacketClientVoice.class);
        Packet.addPacket(16, PacketVoice.class);
        Packet.addPacket(17, PacketVoiceChannel.class);
        Packet.addPacket(18, PacketVoiceChannelUpdate.class);
        Packet.addPacket(19, PacketVoiceChannelSwitch.class);
        Packet.addPacket(20, PacketVoiceMute.class);
        Packet.addPacket(21, PacketDeleteVoiceChannel.class);
        Packet.addPacket(23, PacketUpdateWorld.class);
        Packet.addPacket(24, PacketServerUpdate.class);
        Packet.addPacket(25, PacketWorldBorder.class);
        Packet.addPacket(26, PacketWorldBorderUpdate.class);
        Packet.addPacket(27, PacketWorldBorderRemove.class);
    }

}
