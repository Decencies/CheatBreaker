package net.minecraft.command;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IEntitySelector
{
    IEntitySelector selectAnything = new IEntitySelector()
    {

        public boolean isEntityApplicable(Entity p_82704_1_)
        {
            return p_82704_1_.isEntityAlive();
        }
    };
    IEntitySelector field_152785_b = new IEntitySelector()
    {

        public boolean isEntityApplicable(Entity p_82704_1_)
        {
            return p_82704_1_.isEntityAlive() && p_82704_1_.riddenByEntity == null && p_82704_1_.ridingEntity == null;
        }
    };
    IEntitySelector selectInventories = new IEntitySelector()
    {

        public boolean isEntityApplicable(Entity p_82704_1_)
        {
            return p_82704_1_ instanceof IInventory && p_82704_1_.isEntityAlive();
        }
    };

    /**
     * Return whether the specified entity is applicable to this filter.
     */
    boolean isEntityApplicable(Entity p_82704_1_);

    public static class ArmoredMob implements IEntitySelector
    {
        private final ItemStack field_96567_c;


        public ArmoredMob(ItemStack p_i1584_1_)
        {
            this.field_96567_c = p_i1584_1_;
        }

        public boolean isEntityApplicable(Entity p_82704_1_)
        {
            if (!p_82704_1_.isEntityAlive())
            {
                return false;
            }
            else if (!(p_82704_1_ instanceof EntityLivingBase))
            {
                return false;
            }
            else
            {
                EntityLivingBase var2 = (EntityLivingBase)p_82704_1_;
                return var2.getEquipmentInSlot(EntityLiving.getArmorPosition(this.field_96567_c)) != null ? false : (var2 instanceof EntityLiving ? ((EntityLiving)var2).canPickUpLoot() : var2 instanceof EntityPlayer);
            }
        }
    }
}
