package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S36PacketSignEditorOpen extends Packet
{
    private int field_149133_a;
    private int field_149131_b;
    private int field_149132_c;


    public S36PacketSignEditorOpen() {}

    public S36PacketSignEditorOpen(int p_i45207_1_, int p_i45207_2_, int p_i45207_3_)
    {
        this.field_149133_a = p_i45207_1_;
        this.field_149131_b = p_i45207_2_;
        this.field_149132_c = p_i45207_3_;
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleSignEditorOpen(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        this.field_149133_a = p_148837_1_.readInt();
        this.field_149131_b = p_148837_1_.readInt();
        this.field_149132_c = p_148837_1_.readInt();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeInt(this.field_149133_a);
        p_148840_1_.writeInt(this.field_149131_b);
        p_148840_1_.writeInt(this.field_149132_c);
    }

    public int func_149129_c()
    {
        return this.field_149133_a;
    }

    public int func_149128_d()
    {
        return this.field_149131_b;
    }

    public int func_149127_e()
    {
        return this.field_149132_c;
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }
}
