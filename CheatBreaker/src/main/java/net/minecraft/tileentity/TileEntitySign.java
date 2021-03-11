package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S33PacketUpdateSign;

public class TileEntitySign extends TileEntity
{
    public String[] field_145915_a = new String[] {"", "", "", ""};
    public int field_145918_i = -1;
    private boolean field_145916_j = true;
    private EntityPlayer field_145917_k;


    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setString("Text1", this.field_145915_a[0]);
        p_145841_1_.setString("Text2", this.field_145915_a[1]);
        p_145841_1_.setString("Text3", this.field_145915_a[2]);
        p_145841_1_.setString("Text4", this.field_145915_a[3]);
    }

    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        this.field_145916_j = false;
        super.readFromNBT(p_145839_1_);

        for (int var2 = 0; var2 < 4; ++var2)
        {
            this.field_145915_a[var2] = p_145839_1_.getString("Text" + (var2 + 1));

            if (this.field_145915_a[var2].length() > 15)
            {
                this.field_145915_a[var2] = this.field_145915_a[var2].substring(0, 15);
            }
        }
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        String[] var1 = new String[4];
        System.arraycopy(this.field_145915_a, 0, var1, 0, 4);
        return new S33PacketUpdateSign(this.field_145851_c, this.field_145848_d, this.field_145849_e, var1);
    }

    public boolean func_145914_a()
    {
        return this.field_145916_j;
    }

    public void func_145913_a(boolean p_145913_1_)
    {
        this.field_145916_j = p_145913_1_;

        if (!p_145913_1_)
        {
            this.field_145917_k = null;
        }
    }

    public void func_145912_a(EntityPlayer p_145912_1_)
    {
        this.field_145917_k = p_145912_1_;
    }

    public EntityPlayer func_145911_b()
    {
        return this.field_145917_k;
    }
}
