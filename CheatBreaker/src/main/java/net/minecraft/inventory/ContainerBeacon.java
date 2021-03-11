package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;

public class ContainerBeacon extends Container
{
    private TileEntityBeacon theBeacon;

    /**
     * This beacon's slot where you put in Emerald, Diamond, Gold or Iron Ingot.
     */
    private final ContainerBeacon.BeaconSlot beaconSlot;
    private int field_82865_g;
    private int field_82867_h;
    private int field_82868_i;


    public ContainerBeacon(InventoryPlayer p_i1802_1_, TileEntityBeacon p_i1802_2_)
    {
        this.theBeacon = p_i1802_2_;
        this.addSlotToContainer(this.beaconSlot = new ContainerBeacon.BeaconSlot(p_i1802_2_, 0, 136, 110));
        byte var3 = 36;
        short var4 = 137;
        int var5;

        for (var5 = 0; var5 < 3; ++var5)
        {
            for (int var6 = 0; var6 < 9; ++var6)
            {
                this.addSlotToContainer(new Slot(p_i1802_1_, var6 + var5 * 9 + 9, var3 + var6 * 18, var4 + var5 * 18));
            }
        }

        for (var5 = 0; var5 < 9; ++var5)
        {
            this.addSlotToContainer(new Slot(p_i1802_1_, var5, var3 + var5 * 18, 58 + var4));
        }

        this.field_82865_g = p_i1802_2_.func_145998_l();
        this.field_82867_h = p_i1802_2_.func_146007_j();
        this.field_82868_i = p_i1802_2_.func_146006_k();
    }

    public void addCraftingToCrafters(ICrafting p_75132_1_)
    {
        super.addCraftingToCrafters(p_75132_1_);
        p_75132_1_.sendProgressBarUpdate(this, 0, this.field_82865_g);
        p_75132_1_.sendProgressBarUpdate(this, 1, this.field_82867_h);
        p_75132_1_.sendProgressBarUpdate(this, 2, this.field_82868_i);
    }

    public void updateProgressBar(int p_75137_1_, int p_75137_2_)
    {
        if (p_75137_1_ == 0)
        {
            this.theBeacon.func_146005_c(p_75137_2_);
        }

        if (p_75137_1_ == 1)
        {
            this.theBeacon.func_146001_d(p_75137_2_);
        }

        if (p_75137_1_ == 2)
        {
            this.theBeacon.func_146004_e(p_75137_2_);
        }
    }

    public TileEntityBeacon func_148327_e()
    {
        return this.theBeacon;
    }

    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return this.theBeacon.isUseableByPlayer(p_75145_1_);
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

            if (p_82846_2_ == 0)
            {
                if (!this.mergeItemStack(var5, 1, 37, true))
                {
                    return null;
                }

                var4.onSlotChange(var5, var3);
            }
            else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(var5) && var5.stackSize == 1)
            {
                if (!this.mergeItemStack(var5, 0, 1, false))
                {
                    return null;
                }
            }
            else if (p_82846_2_ >= 1 && p_82846_2_ < 28)
            {
                if (!this.mergeItemStack(var5, 28, 37, false))
                {
                    return null;
                }
            }
            else if (p_82846_2_ >= 28 && p_82846_2_ < 37)
            {
                if (!this.mergeItemStack(var5, 1, 28, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 1, 37, false))
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

            if (var5.stackSize == var3.stackSize)
            {
                return null;
            }

            var4.onPickupFromSlot(p_82846_1_, var5);
        }

        return var3;
    }

    class BeaconSlot extends Slot
    {


        public BeaconSlot(IInventory p_i1801_2_, int p_i1801_3_, int p_i1801_4_, int p_i1801_5_)
        {
            super(p_i1801_2_, p_i1801_3_, p_i1801_4_, p_i1801_5_);
        }

        public boolean isItemValid(ItemStack p_75214_1_)
        {
            return p_75214_1_ == null ? false : p_75214_1_.getItem() == Items.emerald || p_75214_1_.getItem() == Items.diamond || p_75214_1_.getItem() == Items.gold_ingot || p_75214_1_.getItem() == Items.iron_ingot;
        }

        public int getSlotStackLimit()
        {
            return 1;
        }
    }
}
