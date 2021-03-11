package net.minecraft.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;

public class TileEntityEnderChest extends TileEntity
{
    public float field_145972_a;
    public float field_145975_i;
    public int field_145973_j;
    private int field_145974_k;


    public void updateEntity()
    {
        super.updateEntity();

        if (++this.field_145974_k % 20 * 4 == 0)
        {
            this.worldObj.func_147452_c(this.field_145851_c, this.field_145848_d, this.field_145849_e, Blocks.ender_chest, 1, this.field_145973_j);
        }

        this.field_145975_i = this.field_145972_a;
        float var1 = 0.1F;
        double var4;

        if (this.field_145973_j > 0 && this.field_145972_a == 0.0F)
        {
            double var2 = (double)this.field_145851_c + 0.5D;
            var4 = (double)this.field_145849_e + 0.5D;
            this.worldObj.playSoundEffect(var2, (double)this.field_145848_d + 0.5D, var4, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.field_145973_j == 0 && this.field_145972_a > 0.0F || this.field_145973_j > 0 && this.field_145972_a < 1.0F)
        {
            float var8 = this.field_145972_a;

            if (this.field_145973_j > 0)
            {
                this.field_145972_a += var1;
            }
            else
            {
                this.field_145972_a -= var1;
            }

            if (this.field_145972_a > 1.0F)
            {
                this.field_145972_a = 1.0F;
            }

            float var3 = 0.5F;

            if (this.field_145972_a < var3 && var8 >= var3)
            {
                var4 = (double)this.field_145851_c + 0.5D;
                double var6 = (double)this.field_145849_e + 0.5D;
                this.worldObj.playSoundEffect(var4, (double)this.field_145848_d + 0.5D, var6, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.field_145972_a < 0.0F)
            {
                this.field_145972_a = 0.0F;
            }
        }
    }

    public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_)
    {
        if (p_145842_1_ == 1)
        {
            this.field_145973_j = p_145842_2_;
            return true;
        }
        else
        {
            return super.receiveClientEvent(p_145842_1_, p_145842_2_);
        }
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate()
    {
        this.updateContainingBlockInfo();
        super.invalidate();
    }

    public void func_145969_a()
    {
        ++this.field_145973_j;
        this.worldObj.func_147452_c(this.field_145851_c, this.field_145848_d, this.field_145849_e, Blocks.ender_chest, 1, this.field_145973_j);
    }

    public void func_145970_b()
    {
        --this.field_145973_j;
        this.worldObj.func_147452_c(this.field_145851_c, this.field_145848_d, this.field_145849_e, Blocks.ender_chest, 1, this.field_145973_j);
    }

    public boolean func_145971_a(EntityPlayer p_145971_1_)
    {
        return this.worldObj.getTileEntity(this.field_145851_c, this.field_145848_d, this.field_145849_e) != this ? false : p_145971_1_.getDistanceSq((double)this.field_145851_c + 0.5D, (double)this.field_145848_d + 0.5D, (double)this.field_145849_e + 0.5D) <= 64.0D;
    }
}
