package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerHopper extends Container
{
    private final IInventory field_94538_a;


    public ContainerHopper(InventoryPlayer p_i1814_1_, IInventory p_i1814_2_)
    {
        this.field_94538_a = p_i1814_2_;
        p_i1814_2_.openInventory();
        byte var3 = 51;
        int var4;

        for (var4 = 0; var4 < p_i1814_2_.getSizeInventory(); ++var4)
        {
            this.addSlotToContainer(new Slot(p_i1814_2_, var4, 44 + var4 * 18, 20));
        }

        for (var4 = 0; var4 < 3; ++var4)
        {
            for (int var5 = 0; var5 < 9; ++var5)
            {
                this.addSlotToContainer(new Slot(p_i1814_1_, var5 + var4 * 9 + 9, 8 + var5 * 18, var4 * 18 + var3));
            }
        }

        for (var4 = 0; var4 < 9; ++var4)
        {
            this.addSlotToContainer(new Slot(p_i1814_1_, var4, 8 + var4 * 18, 58 + var3));
        }
    }

    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return this.field_94538_a.isUseableByPlayer(p_75145_1_);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_)
    {
        ItemStack var3 = null;
        Slot var4 = (Slot)this.inventorySlots.get(p_82846_2_);

        if (var4 != null && var4.getHasStack())
        {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (p_82846_2_ < this.field_94538_a.getSizeInventory())
            {
                if (!this.mergeItemStack(var5, this.field_94538_a.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, this.field_94538_a.getSizeInventory(), false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }
        }

        return var3;
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer p_75134_1_)
    {
        super.onContainerClosed(p_75134_1_);
        this.field_94538_a.closeInventory();
    }
}
