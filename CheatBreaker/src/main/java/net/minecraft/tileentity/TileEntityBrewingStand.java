package net.minecraft.tileentity;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionHelper;

public class TileEntityBrewingStand extends TileEntity implements ISidedInventory
{
    private static final int[] field_145941_a = new int[] {3};
    private static final int[] field_145947_i = new int[] {0, 1, 2};
    private ItemStack[] field_145945_j = new ItemStack[4];
    private int field_145946_k;
    private int field_145943_l;
    private Item field_145944_m;
    private String field_145942_n;


    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return this.isInventoryNameLocalized() ? this.field_145942_n : "container.brewing";
    }

    /**
     * Returns if the inventory name is localized
     */
    public boolean isInventoryNameLocalized()
    {
        return this.field_145942_n != null && this.field_145942_n.length() > 0;
    }

    public void func_145937_a(String p_145937_1_)
    {
        this.field_145942_n = p_145937_1_;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.field_145945_j.length;
    }

    public void updateEntity()
    {
        if (this.field_145946_k > 0)
        {
            --this.field_145946_k;

            if (this.field_145946_k == 0)
            {
                this.func_145940_l();
                this.onInventoryChanged();
            }
            else if (!this.func_145934_k())
            {
                this.field_145946_k = 0;
                this.onInventoryChanged();
            }
            else if (this.field_145944_m != this.field_145945_j[3].getItem())
            {
                this.field_145946_k = 0;
                this.onInventoryChanged();
            }
        }
        else if (this.func_145934_k())
        {
            this.field_145946_k = 400;
            this.field_145944_m = this.field_145945_j[3].getItem();
        }

        int var1 = this.func_145939_j();

        if (var1 != this.field_145943_l)
        {
            this.field_145943_l = var1;
            this.worldObj.setBlockMetadataWithNotify(this.field_145851_c, this.field_145848_d, this.field_145849_e, var1, 2);
        }

        super.updateEntity();
    }

    public int func_145935_i()
    {
        return this.field_145946_k;
    }

    private boolean func_145934_k()
    {
        if (this.field_145945_j[3] != null && this.field_145945_j[3].stackSize > 0)
        {
            ItemStack var1 = this.field_145945_j[3];

            if (!var1.getItem().isPotionIngredient(var1))
            {
                return false;
            }
            else
            {
                boolean var2 = false;

                for (int var3 = 0; var3 < 3; ++var3)
                {
                    if (this.field_145945_j[var3] != null && this.field_145945_j[var3].getItem() == Items.potionitem)
                    {
                        int var4 = this.field_145945_j[var3].getItemDamage();
                        int var5 = this.func_145936_c(var4, var1);

                        if (!ItemPotion.isSplash(var4) && ItemPotion.isSplash(var5))
                        {
                            var2 = true;
                            break;
                        }

                        List var6 = Items.potionitem.getEffects(var4);
                        List var7 = Items.potionitem.getEffects(var5);

                        if ((var4 <= 0 || var6 != var7) && (var6 == null || !var6.equals(var7) && var7 != null) && var4 != var5)
                        {
                            var2 = true;
                            break;
                        }
                    }
                }

                return var2;
            }
        }
        else
        {
            return false;
        }
    }

    private void func_145940_l()
    {
        if (this.func_145934_k())
        {
            ItemStack var1 = this.field_145945_j[3];

            for (int var2 = 0; var2 < 3; ++var2)
            {
                if (this.field_145945_j[var2] != null && this.field_145945_j[var2].getItem() == Items.potionitem)
                {
                    int var3 = this.field_145945_j[var2].getItemDamage();
                    int var4 = this.func_145936_c(var3, var1);
                    List var5 = Items.potionitem.getEffects(var3);
                    List var6 = Items.potionitem.getEffects(var4);

                    if ((var3 <= 0 || var5 != var6) && (var5 == null || !var5.equals(var6) && var6 != null))
                    {
                        if (var3 != var4)
                        {
                            this.field_145945_j[var2].setItemDamage(var4);
                        }
                    }
                    else if (!ItemPotion.isSplash(var3) && ItemPotion.isSplash(var4))
                    {
                        this.field_145945_j[var2].setItemDamage(var4);
                    }
                }
            }

            if (var1.getItem().hasContainerItem())
            {
                this.field_145945_j[3] = new ItemStack(var1.getItem().getContainerItem());
            }
            else
            {
                --this.field_145945_j[3].stackSize;

                if (this.field_145945_j[3].stackSize <= 0)
                {
                    this.field_145945_j[3] = null;
                }
            }
        }
    }

    private int func_145936_c(int p_145936_1_, ItemStack p_145936_2_)
    {
        return p_145936_2_ == null ? p_145936_1_ : (p_145936_2_.getItem().isPotionIngredient(p_145936_2_) ? PotionHelper.applyIngredient(p_145936_1_, p_145936_2_.getItem().getPotionEffect(p_145936_2_)) : p_145936_1_);
    }

    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        NBTTagList var2 = p_145839_1_.getTagList("Items", 10);
        this.field_145945_j = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            byte var5 = var4.getByte("Slot");

            if (var5 >= 0 && var5 < this.field_145945_j.length)
            {
                this.field_145945_j[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        this.field_145946_k = p_145839_1_.getShort("BrewTime");

        if (p_145839_1_.func_150297_b("CustomName", 8))
        {
            this.field_145942_n = p_145839_1_.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        p_145841_1_.setShort("BrewTime", (short)this.field_145946_k);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.field_145945_j.length; ++var3)
        {
            if (this.field_145945_j[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.field_145945_j[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        p_145841_1_.setTag("Items", var2);

        if (this.isInventoryNameLocalized())
        {
            p_145841_1_.setString("CustomName", this.field_145942_n);
        }
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return p_70301_1_ >= 0 && p_70301_1_ < this.field_145945_j.length ? this.field_145945_j[p_70301_1_] : null;
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        if (p_70298_1_ >= 0 && p_70298_1_ < this.field_145945_j.length)
        {
            ItemStack var3 = this.field_145945_j[p_70298_1_];
            this.field_145945_j[p_70298_1_] = null;
            return var3;
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
        if (p_70304_1_ >= 0 && p_70304_1_ < this.field_145945_j.length)
        {
            ItemStack var2 = this.field_145945_j[p_70304_1_];
            this.field_145945_j[p_70304_1_] = null;
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
        if (p_70299_1_ >= 0 && p_70299_1_ < this.field_145945_j.length)
        {
            this.field_145945_j[p_70299_1_] = p_70299_2_;
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit()
    {
        return 64;
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
        return p_94041_1_ == 3 ? p_94041_2_.getItem().isPotionIngredient(p_94041_2_) : p_94041_2_.getItem() == Items.potionitem || p_94041_2_.getItem() == Items.glass_bottle;
    }

    public void func_145938_d(int p_145938_1_)
    {
        this.field_145946_k = p_145938_1_;
    }

    public int func_145939_j()
    {
        int var1 = 0;

        for (int var2 = 0; var2 < 3; ++var2)
        {
            if (this.field_145945_j[var2] != null)
            {
                var1 |= 1 << var2;
            }
        }

        return var1;
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int p_94128_1_)
    {
        return p_94128_1_ == 1 ? field_145941_a : field_145947_i;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_)
    {
        return this.isItemValidForSlot(p_102007_1_, p_102007_2_);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_)
    {
        return true;
    }
}
