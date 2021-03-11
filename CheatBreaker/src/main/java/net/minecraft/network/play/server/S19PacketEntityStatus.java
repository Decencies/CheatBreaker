package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S19PacketEntityStatus extends Packet
{
    private int field_149164_a;
    private byte field_149163_b;


    public S19PacketEntityStatus() {}

    public S19PacketEntityStatus(Entity p_i46335_1_, byte p_i46335_2_)
    {
        this.field_149164_a = p_i46335_1_.getEntityId();
        this.field_149163_b = p_i46335_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        this.field_149164_a = p_148837_1_.readInt();
        this.field_149163_b = p_148837_1_.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeInt(this.field_149164_a);
        p_148840_1_.writeByte(this.field_149163_b);
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleEntityStatus(this);
    }

    public Entity func_149161_a(World p_149161_1_)
    {
        return p_149161_1_.getEntityByID(this.field_149164_a);
    }

    public byte func_149160_c()
    {
        return this.field_149163_b;
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }
}
