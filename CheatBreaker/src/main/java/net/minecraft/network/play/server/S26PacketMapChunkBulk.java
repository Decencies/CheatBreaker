package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;

public class S26PacketMapChunkBulk extends Packet
{
    private int[] field_149266_a;
    private int[] field_149264_b;
    private int[] field_149265_c;
    private int[] field_149262_d;
    private byte[] field_149263_e;
    private byte[][] field_149260_f;
    private int field_149261_g;
    private boolean field_149267_h;
    private static byte[] field_149268_i = new byte[0];


    public S26PacketMapChunkBulk() {}

    public S26PacketMapChunkBulk(List p_i45197_1_)
    {
        int var2 = p_i45197_1_.size();
        this.field_149266_a = new int[var2];
        this.field_149264_b = new int[var2];
        this.field_149265_c = new int[var2];
        this.field_149262_d = new int[var2];
        this.field_149260_f = new byte[var2][];
        this.field_149267_h = !p_i45197_1_.isEmpty() && !((Chunk)p_i45197_1_.get(0)).worldObj.provider.hasNoSky;
        int var3 = 0;

        for (int var4 = 0; var4 < var2; ++var4)
        {
            Chunk var5 = (Chunk)p_i45197_1_.get(var4);
            S21PacketChunkData.Extracted var6 = S21PacketChunkData.func_149269_a(var5, true, 65535);

            if (field_149268_i.length < var3 + var6.field_150282_a.length)
            {
                byte[] var7 = new byte[var3 + var6.field_150282_a.length];
                System.arraycopy(field_149268_i, 0, var7, 0, field_149268_i.length);
                field_149268_i = var7;
            }

            System.arraycopy(var6.field_150282_a, 0, field_149268_i, var3, var6.field_150282_a.length);
            var3 += var6.field_150282_a.length;
            this.field_149266_a[var4] = var5.xPosition;
            this.field_149264_b[var4] = var5.zPosition;
            this.field_149265_c[var4] = var6.field_150280_b;
            this.field_149262_d[var4] = var6.field_150281_c;
            this.field_149260_f[var4] = var6.field_150282_a;
        }

        Deflater var11 = new Deflater(-1);

        try
        {
            var11.setInput(field_149268_i, 0, var3);
            var11.finish();
            this.field_149263_e = new byte[var3];
            this.field_149261_g = var11.deflate(this.field_149263_e);
        }
        finally
        {
            var11.end();
        }
    }

    public static int func_149258_c()
    {
        return 5;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        short var2 = p_148837_1_.readShort();
        this.field_149261_g = p_148837_1_.readInt();
        this.field_149267_h = p_148837_1_.readBoolean();
        this.field_149266_a = new int[var2];
        this.field_149264_b = new int[var2];
        this.field_149265_c = new int[var2];
        this.field_149262_d = new int[var2];
        this.field_149260_f = new byte[var2][];

        if (field_149268_i.length < this.field_149261_g)
        {
            field_149268_i = new byte[this.field_149261_g];
        }

        p_148837_1_.readBytes(field_149268_i, 0, this.field_149261_g);
        byte[] var3 = new byte[S21PacketChunkData.func_149275_c() * var2];
        Inflater var4 = new Inflater();
        var4.setInput(field_149268_i, 0, this.field_149261_g);

        try
        {
            var4.inflate(var3);
        }
        catch (DataFormatException var12)
        {
            throw new IOException("Bad compressed data format");
        }
        finally
        {
            var4.end();
        }

        int var5 = 0;

        for (int var6 = 0; var6 < var2; ++var6)
        {
            this.field_149266_a[var6] = p_148837_1_.readInt();
            this.field_149264_b[var6] = p_148837_1_.readInt();
            this.field_149265_c[var6] = p_148837_1_.readShort();
            this.field_149262_d[var6] = p_148837_1_.readShort();
            int var7 = 0;
            int var8 = 0;
            int var9;

            for (var9 = 0; var9 < 16; ++var9)
            {
                var7 += this.field_149265_c[var6] >> var9 & 1;
                var8 += this.field_149262_d[var6] >> var9 & 1;
            }

            var9 = 2048 * 4 * var7 + 256;
            var9 += 2048 * var8;

            if (this.field_149267_h)
            {
                var9 += 2048 * var7;
            }

            this.field_149260_f[var6] = new byte[var9];
            System.arraycopy(var3, var5, this.field_149260_f[var6], 0, var9);
            var5 += var9;
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeShort(this.field_149266_a.length);
        p_148840_1_.writeInt(this.field_149261_g);
        p_148840_1_.writeBoolean(this.field_149267_h);
        p_148840_1_.writeBytes(this.field_149263_e, 0, this.field_149261_g);

        for (int var2 = 0; var2 < this.field_149266_a.length; ++var2)
        {
            p_148840_1_.writeInt(this.field_149266_a[var2]);
            p_148840_1_.writeInt(this.field_149264_b[var2]);
            p_148840_1_.writeShort((short)(this.field_149265_c[var2] & 65535));
            p_148840_1_.writeShort((short)(this.field_149262_d[var2] & 65535));
        }
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleMapChunkBulk(this);
    }

    public int func_149255_a(int p_149255_1_)
    {
        return this.field_149266_a[p_149255_1_];
    }

    public int func_149253_b(int p_149253_1_)
    {
        return this.field_149264_b[p_149253_1_];
    }

    public int func_149254_d()
    {
        return this.field_149266_a.length;
    }

    public byte[] func_149256_c(int p_149256_1_)
    {
        return this.field_149260_f[p_149256_1_];
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        StringBuilder var1 = new StringBuilder();

        for (int var2 = 0; var2 < this.field_149266_a.length; ++var2)
        {
            if (var2 > 0)
            {
                var1.append(", ");
            }

            var1.append(String.format("{x=%d, z=%d, sections=%d, adds=%d, data=%d}", new Object[] {Integer.valueOf(this.field_149266_a[var2]), Integer.valueOf(this.field_149264_b[var2]), Integer.valueOf(this.field_149265_c[var2]), Integer.valueOf(this.field_149262_d[var2]), Integer.valueOf(this.field_149260_f[var2].length)}));
        }

        return String.format("size=%d, chunks=%d[%s]", new Object[] {Integer.valueOf(this.field_149261_g), Integer.valueOf(this.field_149266_a.length), var1});
    }

    public int[] func_149252_e()
    {
        return this.field_149265_c;
    }

    public int[] func_149257_f()
    {
        return this.field_149262_d;
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }
}
