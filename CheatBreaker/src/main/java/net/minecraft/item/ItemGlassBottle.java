package net.minecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemGlassBottle extends Item
{


    public ItemGlassBottle()
    {
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return Items.potionitem.getIconFromDamage(0);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
        MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(p_77659_2_, p_77659_3_, true);

        if (var4 == null)
        {
            return p_77659_1_;
        }
        else
        {
            if (var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int var5 = var4.blockX;
                int var6 = var4.blockY;
                int var7 = var4.blockZ;

                if (!p_77659_2_.canMineBlock(p_77659_3_, var5, var6, var7))
                {
                    return p_77659_1_;
                }

                if (!p_77659_3_.canPlayerEdit(var5, var6, var7, var4.sideHit, p_77659_1_))
                {
                    return p_77659_1_;
                }

                if (p_77659_2_.getBlock(var5, var6, var7).getMaterial() == Material.water)
                {
                    --p_77659_1_.stackSize;

                    if (p_77659_1_.stackSize <= 0)
                    {
                        return new ItemStack(Items.potionitem);
                    }

                    if (!p_77659_3_.inventory.addItemStackToInventory(new ItemStack(Items.potionitem)))
                    {
                        p_77659_3_.dropPlayerItemWithRandomChoice(new ItemStack(Items.potionitem, 1, 0), false);
                    }
                }
            }

            return p_77659_1_;
        }
    }

    public void registerIcons(IIconRegister p_94581_1_) {}
}
