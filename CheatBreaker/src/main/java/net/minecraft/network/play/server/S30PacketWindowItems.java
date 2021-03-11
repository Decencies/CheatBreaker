package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S30PacketWindowItems extends Packet
{
    private int field_148914_a;
    private ItemStack[] field_148913_b;


    public S30PacketWindowItems() {}

    public S30PacketWindowItems(int p_i45186_1_, List p_i45186_2_)
    {
        this.field_148914_a = p_i45186_1_;
        this.field_148913_b = new ItemStack[p_i45186_2_.size()];

        for (int var3 = 0; var3 < this.field_148913_b.length; ++var3)
        {
            ItemStack var4 = (ItemStack)p_i45186_2_.get(var3);
            this.field_148913_b[var3] = var4 == null ? null : var4.copy();
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer p_148837_1_) throws IOException
    {
        this.field_148914_a = p_148837_1_.readUnsignedByte();
        short var2 = p_148837_1_.readShort();
        this.field_148913_b = new ItemStack[var2];

        for (int var3 = 0; var3 < var2; ++var3)
        {
            this.field_148913_b[var3] = p_148837_1_.readItemStackFromBuffer();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer p_148840_1_) throws IOException
    {
        p_148840_1_.writeByte(this.field_148914_a);
        p_148840_1_.writeShort(this.field_148913_b.length);
        ItemStack[] var2 = this.field_148913_b;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            ItemStack var5 = var2[var4];
            p_148840_1_.writeItemStackToBuffer(var5);
        }
    }

    public void processPacket(INetHandlerPlayClient p_148833_1_)
    {
        p_148833_1_.handleWindowItems(this);
    }

    public int func_148911_c()
    {
        return this.field_148914_a;
    }

    public ItemStack[] func_148910_d()
    {
        return this.field_148913_b;
    }

    public void processPacket(INetHandler p_148833_1_)
    {
        this.processPacket((INetHandlerPlayClient)p_148833_1_);
    }
}
