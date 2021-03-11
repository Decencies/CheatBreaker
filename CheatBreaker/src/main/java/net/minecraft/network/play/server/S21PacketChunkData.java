package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class S21PacketChunkData extends Packet
{
    private int field_149284_a;
    private int field_149282_b;
    private int field_149283_c;
    private int field_149280_d;
    private byte[] field_149281_e;
    private byte[] field_149278_f;
    private boolean field_149279_g;
    private int field_149285_h;
    private static byte[] field_149286_i = new byte[196864];


    public S21PacketChunkData() {}

    public S21PacketChunkData(Chunk p_i45196_1_, boolean p_i45196_2_, int p_i45196_3_)
    {
        this.field_149284_a = p_i45196_1_.xPosition;
        this.field_149282_b = p_i45196_1_.zPosition;
        this.field_149279_g = p_i45196_2_;
        S21PacketChunkData.Extracted var4 = func_149269_a(p_i45196_1_, p_i45196_2_, p_i45196_3_);
        Deflater var5 = new Deflater(-1);
        this.field_149280_d = var4.field_150281_c;
        this.field_149283_c = var4.field_150280_b;

        try
        {
            this.field_149278_f = var4.field_150282_a;
            var5.setInput(var4.field_150282_a, 0, var4.field_150282_a.length);
            var5.finish();
            this.field_149281_e = new byte[var4.field_150282_a.length];
            this.field_149285_h = var5.deflate(this.field_149281_e);
        }
        finally
        {
            var5.end();
        }
    }

    public static int func_149275_c()
    {
        return 196864;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        this.field_149284_a = p_148837_1_.readInt();
        this.field_149282_b = p_148837_1_.readInt();
        this.field_149279_g = p_148837_1_.readBoolean();
        this.field_149283_c = p_148837_1_.readShort();
        this.field_149280_d = p_148837_1_.readShort();
        this.field_149285_h = p_148837_1_.readInt();

        if (field_149286_i.length < this.field_149285_h)
        {
            field_149286_i = new byte[this.field_149285_h];
        }

        p_148837_1_.readBytes(field_149286_i, 0, this.field_149285_h);
        int var2 = 0;
        int var3;

        for (var3 = 0; var3 < 16; ++var3)
        {
            var2 += this.field_149283_c >> var3 & 1;
        }

        var3 = 12288 * var2;

        if (this.field_149279_g)
        {
            var3 += 256;
        }

        this.field_149278_f = new byte[var3];
        Inflater var4 = new Inflater();
        var4.setInput(field_149286_i, 0, this.field_149285_h);

        try
        {
            var4.inflate(this.field_149278_f);
        }
        catch (DataFormatException var9)
        {
            throw new IOException("Bad compressed data format");
        }
        finally
        {
            var4.end();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeInt(this.field_149284_a);
        p_148840_1_.writeInt(this.field_149282_b);
        p_148840_1_.writeBoolean(this.field_149279_g);
        p_148840_1_.writeShort((short)(this.field_149283_c & 65535));
        p_148840_1_.writeShort((short)(this.field_149280_d & 65535));
        p_148840_1_.writeInt(this.field_149285_h);
        p_148840_1_.writeBytes(this.field_149281_e, 0, this.field_149285_h);
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleChunkData(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("x=%d, z=%d, full=%b, sects=%d, add=%d, size=%d", new Object[] {Integer.valueOf(this.field_149284_a), Integer.valueOf(this.field_149282_b), Boolean.valueOf(this.field_149279_g), Integer.valueOf(this.field_149283_c), Integer.valueOf(this.field_149280_d), Integer.valueOf(this.field_149285_h)});
    }

    public byte[] func_149272_d()
    {
        return this.field_149278_f;
    }

    public static S21PacketChunkData.Extracted func_149269_a(Chunk p_149269_0_, boolean p_149269_1_, int p_149269_2_)
    {
        int var3 = 0;
        ExtendedBlockStorage[] var4 = p_149269_0_.getBlockStorageArray();
        int var5 = 0;
        S21PacketChunkData.Extracted var6 = new S21PacketChunkData.Extracted();
        byte[] var7 = field_149286_i;

        if (p_149269_1_)
        {
            p_149269_0_.sendUpdates = true;
        }

        int var8;

        for (var8 = 0; var8 < var4.length; ++var8)
        {
            if (var4[var8] != null && (!p_149269_1_ || !var4[var8].isEmpty()) && (p_149269_2_ & 1 << var8) != 0)
            {
                var6.field_150280_b |= 1 << var8;

                if (var4[var8].getBlockMSBArray() != null)
                {
                    var6.field_150281_c |= 1 << var8;
                    ++var5;
                }
            }
        }

        for (var8 = 0; var8 < var4.length; ++var8)
        {
            if (var4[var8] != null && (!p_149269_1_ || !var4[var8].isEmpty()) && (p_149269_2_ & 1 << var8) != 0)
            {
                byte[] var9 = var4[var8].getBlockLSBArray();
                System.arraycopy(var9, 0, var7, var3, var9.length);
                var3 += var9.length;
            }
        }

        NibbleArray var11;

        for (var8 = 0; var8 < var4.length; ++var8)
        {
            if (var4[var8] != null && (!p_149269_1_ || !var4[var8].isEmpty()) && (p_149269_2_ & 1 << var8) != 0)
            {
                var11 = var4[var8].getMetadataArray();
                System.arraycopy(var11.data, 0, var7, var3, var11.data.length);
                var3 += var11.data.length;
            }
        }

        for (var8 = 0; var8 < var4.length; ++var8)
        {
            if (var4[var8] != null && (!p_149269_1_ || !var4[var8].isEmpty()) && (p_149269_2_ & 1 << var8) != 0)
            {
                var11 = var4[var8].getBlocklightArray();
                System.arraycopy(var11.data, 0, var7, var3, var11.data.length);
                var3 += var11.data.length;
            }
        }

        if (!p_149269_0_.worldObj.provider.hasNoSky)
        {
            for (var8 = 0; var8 < var4.length; ++var8)
            {
                if (var4[var8] != null && (!p_149269_1_ || !var4[var8].isEmpty()) && (p_149269_2_ & 1 << var8) != 0)
                {
                    var11 = var4[var8].getSkylightArray();
                    System.arraycopy(var11.data, 0, var7, var3, var11.data.length);
                    var3 += var11.data.length;
                }
            }
        }

        if (var5 > 0)
        {
            for (var8 = 0; var8 < var4.length; ++var8)
            {
                if (var4[var8] != null && (!p_149269_1_ || !var4[var8].isEmpty()) && var4[var8].getBlockMSBArray() != null && (p_149269_2_ & 1 << var8) != 0)
                {
                    var11 = var4[var8].getBlockMSBArray();
                    System.arraycopy(var11.data, 0, var7, var3, var11.data.length);
                    var3 += var11.data.length;
                }
            }
        }

        if (p_149269_1_)
        {
            byte[] var10 = p_149269_0_.getBiomeArray();
            System.arraycopy(var10, 0, var7, var3, var10.length);
            var3 += var10.length;
        }

        var6.field_150282_a = new byte[var3];
        System.arraycopy(var7, 0, var6.field_150282_a, 0, var3);
        return var6;
    }

    public int func_149273_e()
    {
        return this.field_149284_a;
    }

    public int func_149271_f()
    {
        return this.field_149282_b;
    }

    public int func_149276_g()
    {
        return this.field_149283_c;
    }

    public int func_149270_h()
    {
        return this.field_149280_d;
    }

    public boolean func_149274_i()
    {
        return this.field_149279_g;
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }

    public static class Extracted
    {
        public byte[] field_150282_a;
        public int field_150280_b;
        public int field_150281_c;

    }
}
