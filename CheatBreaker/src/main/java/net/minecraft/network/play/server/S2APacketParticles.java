package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2APacketParticles extends Packet
{
    private String field_149236_a;
    private float field_149234_b;
    private float field_149235_c;
    private float field_149232_d;
    private float field_149233_e;
    private float field_149230_f;
    private float field_149231_g;
    private float field_149237_h;
    private int field_149238_i;
    

    public S2APacketParticles() {}

    public S2APacketParticles(String p_i45199_1_, float p_i45199_2_, float p_i45199_3_, float p_i45199_4_, float p_i45199_5_, float p_i45199_6_, float p_i45199_7_, float p_i45199_8_, int p_i45199_9_)
    {
        this.field_149236_a = p_i45199_1_;
        this.field_149234_b = p_i45199_2_;
        this.field_149235_c = p_i45199_3_;
        this.field_149232_d = p_i45199_4_;
        this.field_149233_e = p_i45199_5_;
        this.field_149230_f = p_i45199_6_;
        this.field_149231_g = p_i45199_7_;
        this.field_149237_h = p_i45199_8_;
        this.field_149238_i = p_i45199_9_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        this.field_149236_a = p_148837_1_.readStringFromBuffer(64);
        this.field_149234_b = p_148837_1_.readFloat();
        this.field_149235_c = p_148837_1_.readFloat();
        this.field_149232_d = p_148837_1_.readFloat();
        this.field_149233_e = p_148837_1_.readFloat();
        this.field_149230_f = p_148837_1_.readFloat();
        this.field_149231_g = p_148837_1_.readFloat();
        this.field_149237_h = p_148837_1_.readFloat();
        this.field_149238_i = p_148837_1_.readInt();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeStringToBuffer(this.field_149236_a);
        p_148840_1_.writeFloat(this.field_149234_b);
        p_148840_1_.writeFloat(this.field_149235_c);
        p_148840_1_.writeFloat(this.field_149232_d);
        p_148840_1_.writeFloat(this.field_149233_e);
        p_148840_1_.writeFloat(this.field_149230_f);
        p_148840_1_.writeFloat(this.field_149231_g);
        p_148840_1_.writeFloat(this.field_149237_h);
        p_148840_1_.writeInt(this.field_149238_i);
    }

    public String func_149228_c()
    {
        return this.field_149236_a;
    }

    public double func_149220_d()
    {
        return (double)this.field_149234_b;
    }

    public double func_149226_e()
    {
        return (double)this.field_149235_c;
    }

    public double func_149225_f()
    {
        return (double)this.field_149232_d;
    }

    public float func_149221_g()
    {
        return this.field_149233_e;
    }

    public float func_149224_h()
    {
        return this.field_149230_f;
    }

    public float func_149223_i()
    {
        return this.field_149231_g;
    }

    public float func_149227_j()
    {
        return this.field_149237_h;
    }

    public int func_149222_k()
    {
        return this.field_149238_i;
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleParticles(this);
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }
}
