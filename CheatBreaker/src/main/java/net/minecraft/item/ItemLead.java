package net.minecraft.item;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemLead extends Item
{


    public ItemLead()
    {
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        Block var11 = p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_);

        if (var11.getRenderType() == 11)
        {
            if (p_77648_3_.isClient)
            {
                return true;
            }
            else
            {
                func_150909_a(p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public static boolean func_150909_a(EntityPlayer p_150909_0_, World p_150909_1_, int p_150909_2_, int p_150909_3_, int p_150909_4_)
    {
        EntityLeashKnot var5 = EntityLeashKnot.getKnotForBlock(p_150909_1_, p_150909_2_, p_150909_3_, p_150909_4_);
        boolean var6 = false;
        double var7 = 7.0D;
        List var9 = p_150909_1_.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox((double)p_150909_2_ - var7, (double)p_150909_3_ - var7, (double)p_150909_4_ - var7, (double)p_150909_2_ + var7, (double)p_150909_3_ + var7, (double)p_150909_4_ + var7));

        if (var9 != null)
        {
            Iterator var10 = var9.iterator();

            while (var10.hasNext())
            {
                EntityLiving var11 = (EntityLiving)var10.next();

                if (var11.getLeashed() && var11.getLeashedToEntity() == p_150909_0_)
                {
                    if (var5 == null)
                    {
                        var5 = EntityLeashKnot.func_110129_a(p_150909_1_, p_150909_2_, p_150909_3_, p_150909_4_);
                    }

                    var11.setLeashedToEntity(var5, true);
                    var6 = true;
                }
            }
        }

        return var6;
    }
}
