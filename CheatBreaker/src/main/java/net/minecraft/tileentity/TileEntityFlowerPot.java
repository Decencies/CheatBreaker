package net.minecraft.tileentity;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityFlowerPot extends TileEntity
{
    private Item field_145967_a;
    private int field_145968_i;


    public TileEntityFlowerPot() {}

    public TileEntityFlowerPot(Item p_i45442_1_, int p_i45442_2_)
    {
        this.field_145967_a = p_i45442_1_;
        this.field_145968_i = p_i45442_2_;
    }

    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("Item", Item.getIdFromItem(this.field_145967_a));
        p_145841_1_.setInteger("Data", this.field_145968_i);
    }

    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.field_145967_a = Item.getItemById(p_145839_1_.getInteger("Item"));
        this.field_145968_i = p_145839_1_.getInteger("Data");
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.field_145851_c, this.field_145848_d, this.field_145849_e, 5, var1);
    }

    public void func_145964_a(Item p_145964_1_, int p_145964_2_)
    {
        this.field_145967_a = p_145964_1_;
        this.field_145968_i = p_145964_2_;
    }

    public Item func_145965_a()
    {
        return this.field_145967_a;
    }

    public int func_145966_b()
    {
        return this.field_145968_i;
    }
}
