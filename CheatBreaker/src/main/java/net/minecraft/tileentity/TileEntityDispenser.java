package net.minecraft.tileentity;

import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityDispenser extends TileEntity implements IInventory
{
    private ItemStack[] field_146022_i = new ItemStack[9];
    private Random field_146021_j = new Random();
    protected String field_146020_a;


    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return 9;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int p_70301_1_)
    {
        return this.field_146022_i[p_70301_1_];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_)
    {
        if (this.field_146022_i[p_70298_1_] != null)
        {
            ItemStack var3;

            if (this.field_146022_i[p_70298_1_].stackSize <= p_70298_2_)
            {
                var3 = this.field_146022_i[p_70298_1_];
                this.field_146022_i[p_70298_1_] = null;
                this.onInventoryChanged();
                return var3;
            }
            else
            {
                var3 = this.field_146022_i[p_70298_1_].splitStack(p_70298_2_);

                if (this.field_146022_i[p_70298_1_].stackSize == 0)
                {
                    this.field_146022_i[p_70298_1_] = null;
                }

                this.onInventoryChanged();
                return var3;
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
        if (this.field_146022_i[p_70304_1_] != null)
        {
            ItemStack var2 = this.field_146022_i[p_70304_1_];
            this.field_146022_i[p_70304_1_] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    public int func_146017_i()
    {
        int var1 = -1;
        int var2 = 1;

        for (int var3 = 0; var3 < this.field_146022_i.length; ++var3)
        {
            if (this.field_146022_i[var3] != null && this.field_146021_j.nextInt(var2++) == 0)
            {
                var1 = var3;
            }
        }

        return var1;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
    {
        this.field_146022_i[p_70299_1_] = p_70299_2_;

        if (p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit())
        {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }

        this.onInventoryChanged();
    }

    public int func_146019_a(ItemStack p_146019_1_)
    {
        for (int var2 = 0; var2 < this.field_146022_i.length; ++var2)
        {
            if (this.field_146022_i[var2] == null || this.field_146022_i[var2].getItem() == null)
            {
                this.setInventorySlotContents(var2, p_146019_1_);
                return var2;
            }
        }

        return -1;
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName()
    {
        return this.isInventoryNameLocalized() ? this.field_146020_a : "container.dispenser";
    }

    public void func_146018_a(String p_146018_1_)
    {
        this.field_146020_a = p_146018_1_;
    }

    /**
     * Returns if the inventory name is localized
     */
    public boolean isInventoryNameLocalized()
    {
        return this.field_146020_a != null;
    }

    public void readFromNBT(NBTTagCompound p_145839_1_)
    {
        super.readFromNBT(p_145839_1_);
        NBTTagList var2 = p_145839_1_.getTagList("Items", 10);
        this.field_146022_i = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            int var5 = var4.getByte("Slot") & 255;

            if (var5 >= 0 && var5 < this.field_146022_i.length)
            {
                this.field_146022_i[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }

        if (p_145839_1_.func_150297_b("CustomName", 8))
        {
            this.field_146020_a = p_145839_1_.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound p_145841_1_)
    {
        super.writeToNBT(p_145841_1_);
        NBTTagList var2 = new NBTTagList();

        for (int var3 = 0; var3 < this.field_146022_i.length; ++var3)
        {
            if (this.field_146022_i[var3] != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte)var3);
                this.field_146022_i[var3].writeToNBT(var4);
                var2.appendTag(var4);
            }
        }

        p_145841_1_.setTag("Items", var2);

        if (this.isInventoryNameLocalized())
        {
            p_145841_1_.setString("CustomName", this.field_146020_a);
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
        return true;
    }
}
