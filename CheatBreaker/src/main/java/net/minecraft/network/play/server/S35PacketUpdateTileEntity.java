package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S35PacketUpdateTileEntity extends Packet
{
    private int field_148863_a;
    private int field_148861_b;
    private int field_148862_c;
    private int field_148859_d;
    private NBTTagCompound field_148860_e;


    public S35PacketUpdateTileEntity() {}

    public S35PacketUpdateTileEntity(int p_i45175_1_, int p_i45175_2_, int p_i45175_3_, int p_i45175_4_, NBTTagCompound p_i45175_5_)
    {
        this.field_148863_a = p_i45175_1_;
        this.field_148861_b = p_i45175_2_;
        this.field_148862_c = p_i45175_3_;
        this.field_148859_d = p_i45175_4_;
        this.field_148860_e = p_i45175_5_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        this.field_148863_a = p_148837_1_.readInt();
        this.field_148861_b = p_148837_1_.readShort();
        this.field_148862_c = p_148837_1_.readInt();
        this.field_148859_d = p_148837_1_.readUnsignedByte();
        this.field_148860_e = p_148837_1_.readNBTTagCompoundFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeInt(this.field_148863_a);
        p_148840_1_.writeShort(this.field_148861_b);
        p_148840_1_.writeInt(this.field_148862_c);
        p_148840_1_.writeByte((byte)this.field_148859_d);
        p_148840_1_.writeNBTTagCompoundToBuffer(this.field_148860_e);
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleUpdateTileEntity(this);
    }

    public int func_148856_c()
    {
        return this.field_148863_a;
    }

    public int func_148855_d()
    {
        return this.field_148861_b;
    }

    public int func_148854_e()
    {
        return this.field_148862_c;
    }

    public int func_148853_f()
    {
        return this.field_148859_d;
    }

    public NBTTagCompound func_148857_g()
    {
        return this.field_148860_e;
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }
}
