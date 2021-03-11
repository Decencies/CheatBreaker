package net.minecraft.dispenser;

import net.minecraft.item.ItemStack;

public interface IBehaviorDispenseItem
{
    IBehaviorDispenseItem itemDispenseBehaviorProvider = new IBehaviorDispenseItem()
    {

        public ItemStack dispense(IBlockSource p_82482_1_, ItemStack p_82482_2_)
        {
            return p_82482_2_;
        }
    };

    /**
     * Dispenses the specified ItemStack from a dispenser.
     */
    ItemStack dispense(IBlockSource p_82482_1_, ItemStack p_82482_2_);
}
