package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import org.apache.commons.lang3.ArrayUtils;

public class S3APacketTabComplete extends Packet
{
    private String[] field_149632_a;


    public S3APacketTabComplete() {}

    public S3APacketTabComplete(String[] p_i45178_1_)
    {
        this.field_149632_a = p_i45178_1_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        this.field_149632_a = new String[p_148837_1_.readVarIntFromBuffer()];

        for (int var2 = 0; var2 < this.field_149632_a.length; ++var2)
        {
            this.field_149632_a[var2] = p_148837_1_.readStringFromBuffer(32767);
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeVarIntToBuffer(this.field_149632_a.length);
        String[] var2 = this.field_149632_a;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            String var5 = var2[var4];
            p_148840_1_.writeStringToBuffer(var5);
        }
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleTabComplete(this);
    }

    public String[] func_149630_c()
    {
        return this.field_149632_a;
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("candidates=\'%s\'", new Object[] {ArrayUtils.toString(this.field_149632_a)});
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }
}
