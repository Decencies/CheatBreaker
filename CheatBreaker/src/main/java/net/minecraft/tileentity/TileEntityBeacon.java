package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityBeacon extends TileEntity implements IInventory
{
    public static final Potion[][] field_146009_a = new Potion[][] {{Potion.moveSpeed, Potion.digSpeed}, {Potion.resistance, Potion.jump}, {Potion.damageBoost}, {Potion.regeneration}};
    private long field_146016_i;
    private float field_146014_j;
    private boolean field_146015_k;
    private int field_146012_l = -1;
    private int field_146013_m;
    private int field_146010_n;
    private ItemStack field_146011_o;
    private String field_146008_p;


    public void updateEntity()
    {
        if (this.worldObj.getTotalWorldTime() % 80L == 0L)
        {
            this.func_146003_y();
            this.func_146000_x();
        }
    }

    private void func_146000_x()
    {
        if (this.field_146015_k && this.field_146012_l > 0 && !this.worldObj.isClient && this.field_146013_m > 0)
        {
            double var1 = (double)(this.field_146012_l * 10 + 10);
            byte var3 = 0;

            if (this.field_146012_l >= 4 && this.field_146013_m == this.field_146010_n)
            {
                var3 = 1;
            }

            AxisAlignedBB var4 = AxisAlignedBB.getBoundingBox((double)this.field_145851_c, (double)this.field_145848_d, (double)this.field_145849_e, (double)(this.field_145851_c + 1), (double)(this.field_145848_d + 1), (double)(this.field_145849_e + 1)).expand(var1, var1, var1);
            var4.maxY = (double)this.worldObj.getHeight();
            List var5 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, var4);
            Iterator var6 = var5.iterator();
            EntityPlayer var7;

            while (var6.hasNext())
            {
                var7 = (EntityPlayer)var6.next();
                var7.addPotionEffect(new PotionEffect(this.field_146013_m, 180, var3, true));
            }

            if (this.field_146012_l >= 4 && this.field_146013_m != this.field_146010_n && this.field_146010_n > 0)
            {
                var6 = var5.iterator();

                while (var6.hasNext())
                {
                    var7 = (EntityPlayer)var6.next();
                    var7.addPotionEffect(new PotionEffect(this.field_146010_n, 180, 0, true));
                }
            }
        }
    }

    private void func_146003_y()
    {
        int var1 = this.field_146012_l;

        if (!this.worldObj.canBlockSeeTheSky(this.field_145851_c, this.field_145848_d + 1, this.field_145849_e))
        {
            this.field_146015_k = false;
            this.field_146012_l = 0;
        }
        else
        {
            this.field_146015_k = true;
            this.field_146012_l = 0;

            for (int var2 = 1; var2 <= 4; this.field_146012_l = var2++)
            {
                int var3 = this.field_145848_d - var2;

                if (var3 < 0)
                {
                    break;
                }

                boolean var4 = true;

                for (int var5 = this.field_145851_c - var2; var5 <= this.field_145851_c + var2 && var4; ++var5)
                {
                    for (int var6 = this.field_145849_e - var2; var6 <= this.field_145849_e + var2; ++var6)
                    {
                        Block var7 = this.worldObj.getBlock(var5, var3, var6);

                        if (var7 != Blocks.emerald_block && var7 != Blocks.gold_block && var7 != Blocks.diamond_block && var7 != Blocks.iron_block)
                        {
                            var4 = false;
                            break;
                        }
                    }
                }

                if (!var4)
                {
                    break;
                }
            }

            if (this.field_146012_l == 0)
            {
                this.field_146015_k = false;
            }
        }

        if (!this.worldObj.isClient && this.field_146012_l == 4 && var1 < this.field_146012_l)
        {
            Iterator var8 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox((double)this.field_145851_c, (double)this.field_145848_d, (double)this.field_145849_e, (double)this.field_145851_c, (double)(this.field_145848_d - 4), (double)this.field_145849_e).expand(10.0D, 5.0D, 10.0D)).iterator();

            while (var8.hasNext())
            {
                EntityPlayer var9 = (EntityPlayer)var8.next();
                var9.triggerAchievement(AchievementList.field_150965_K);
            }
        }
    }

    public float func_146002_i()
    {
        if (!this.field_146015_k)
        {
            return 0.0F;
        }
        else
        {
            int var1 = (int)(this.worldObj.getTotalWorldTime() - this.field_146016_i);
            this.field_146016_i = this.worldObj.getTotalWorldTime();

            if (var1 > 1)
            {
                this.field_146014_j -= (float)var1 / 40.0F;

                if (this.field_146014_j < 0.0F)
                {
                    this.field_146014_j = 0.0F;
                }
            }

            this.field_146014_j += 0.025F;

            if (this.field_146014_j > 1.0F)
            {
                this.field_146014_j = 1.0F;
            }

            return this.field_146014_j;
        }
    }

    public int func_146007_j()
    {
        return this.field_146013_m;
    }

    public int func_146006_k()
    {
        return this.field_146010_n;
    }

    public int func_145998_l()
    {
        return this.field_146012_l;
    }

    public void func_146005_c(int p_146005_1_)
    {
        this.field_146012_l = p_146005_1_;
    }

    public void func_146001_d(int p_146001_1_)
    {
        this.field_146013_m = 0;

        for (int var2 = 0; var2 < this.field_146012_l && var2 < 3; ++var2)
        {
            Potion[] var3 = field_146009_a[var2];
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                Potion var6 = var3[var5];

                if (var6.id == p_146001_1_)
                {
                    this.field_146013_m = p_146001_1_;
                    return;
                }
            }
        }
    }

    public void func_146004_e(int p_146004_1_)
    {
        this.field_146010_n = 0;

        if (this.field_146012_l >= 4)
        {
            for (int var2 = 0; var2 < 4; ++var2)
            {
                Potion[] var3 = field_146009_a[var2];
                int var4 = var3.length;

                for (int var5 = 0; var5 < var4; ++var5)
                {
                    Potion var6 = var3[var5];

                    if (var6.id == p_146004_1_)
                    {
                        this.field_146010_n = p_146004_1_;
                        return;
                    }
                }
            }
        }
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        this.writeToNBT(var1);
        return new S35PacketUpdateTileEntity(this.field_145851_c, this.field_145848_d, this.field_145849_e, 3, var1);
    }

    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }

    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        this.field_146013_m = p_145839_1_.getInteger("Primary");
        this.field_146010_n = p_145839_1_.getInteger("Secondary");
        this.field_146012_l = p_145839_1_.getInteger("Levels");
    }

    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setInteger("Primary", this.field_146013_m);
        p_145841_1_.setInteger("Secondary", this.field_146010_n);
        p_145841_1_.setInteger("Levels", this.field_146012_l);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 1;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return p_70301_1_ == 0 ? this.field_146011_o : null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        if (p_70298_1_ == 0 && this.field_146011_o != null)
        {
            if (p_70298_2_ >= this.field_146011_o.stackSize)
            {
                ItemStack var3 = this.field_146011_o;
                this.field_146011_o = null;
                return var3;
            }
            else
            {
                this.field_146011_o.stackSize -= p_70298_2_;
                return new ItemStack(this.field_146011_o.getItem(), p_70298_2_, this.field_146011_o.getItemDamage());
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int p_70304_1_)
    {
        if (p_70304_1_ == 0 && this.field_146011_o != null)
        {
            ItemStack var2 = this.field_146011_o;
            this.field_146011_o = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {
        if (p_70299_1_ == 0)
        {
            this.field_146011_o = p_70299_2_;
        }
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return this.isInventoryNameLocalized() ? this.field_146008_p : "container.beacon";
    }

    /**
     * Returns if the inventory name is localized
     */
    public boolean isInventoryNameLocalized()
    {
        return this.field_146008_p != null && this.field_146008_p.length() > 0;
    }

    public void func_145999_a(String p_145999_1_)
    {
        this.field_146008_p = p_145999_1_;
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 1;
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
    {
        return this.worldObj.getTileEntity(this.field_145851_c, this.field_145848_d, this.field_145849_e) != this ? false : p_70300_1_.getDistanceSq((double)this.field_145851_c + 0.5D, (double)this.field_145848_d + 0.5D, (double)this.field_145849_e + 0.5D) <= 64.0D;
    }

    public void openInventory() {}

    public void closeInventory() {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_)
    {
        return p_94041_2_.getItem() == Items.emerald || p_94041_2_.getItem() == Items.diamond || p_94041_2_.getItem() == Items.gold_ingot || p_94041_2_.getItem() == Items.iron_ingot;
    }
}
