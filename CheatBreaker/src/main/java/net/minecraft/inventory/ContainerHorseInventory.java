package net.minecraft.inventory;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory extends Container
{
    private IInventory field_111243_a;
    private EntityHorse theHorse;


    public ContainerHorseInventory(IInventory p_i1817_1_, final IInventory p_i1817_2_, final EntityHorse p_i1817_3_)
    {
        this.field_111243_a = p_i1817_2_;
        this.theHorse = p_i1817_3_;
        byte var4 = 3;
        p_i1817_2_.openInventory();
        int var5 = (var4 - 4) * 18;
        this.addSlotToContainer(new Slot(p_i1817_2_, 0, 8, 18)
        {

            public boolean isItemValid(ItemStack p_75214_1_)
            {
                return super.isItemValid(p_75214_1_) && p_75214_1_.getItem() == Items.saddle && !this.getHasStack();
            }
        });
        this.addSlotToContainer(new Slot(p_i1817_2_, 1, 8, 36)
        {

            public boolean isItemValid(ItemStack p_75214_1_)
            {
                return super.isItemValid(p_75214_1_) && p_i1817_3_.func_110259_cr() && EntityHorse.func_146085_a(p_75214_1_.getItem());
            }
            public boolean func_111238_b()
            {
                return p_i1817_3_.func_110259_cr();
            }
        });
        int var6;
        int var7;

        if (p_i1817_3_.isChested())
        {
            for (var6 = 0; var6 < var4; ++var6)
            {
                for (var7 = 0; var7 < 5; ++var7)
                {
                    this.addSlotToContainer(new Slot(p_i1817_2_, 2 + var7 + var6 * 5, 80 + var7 * 18, 18 + var6 * 18));
                }
            }
        }

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(p_i1817_1_, var7 + var6 * 9 + 9, 8 + var7 * 18, 102 + var6 * 18 + var5));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(p_i1817_1_, var6, 8 + var6 * 18, 160 + var5));
        }
    }

    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return this.field_111243_a.isUseableByPlayer(p_75145_1_) && this.theHorse.isEntityAlive() && this.theHorse.getDistanceToEntity(p_75145_1_) < 8.0F;
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

            if (p_82846_2_ < this.field_111243_a.getSizeInventory())
            {
                if (!this.mergeItemStack(var5, this.field_111243_a.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (this.getSlot(1).isItemValid(var5) && !this.getSlot(1).getHasStack())
            {
                if (!this.mergeItemStack(var5, 1, 2, false))
                {
                    return null;
                }
            }
            else if (this.getSlot(0).isItemValid(var5))
            {
                if (!this.mergeItemStack(var5, 0, 1, false))
                {
                    return null;
                }
            }
            else if (this.field_111243_a.getSizeInventory() <= 2 || !this.mergeItemStack(var5, 2, this.field_111243_a.getSizeInventory(), false))
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
        this.field_111243_a.closeInventory();
    }
}
