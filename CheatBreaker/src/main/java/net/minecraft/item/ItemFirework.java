package net.minecraft.item;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemFirework extends Item
{


    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (!p_77648_3_.isClient)
        {
            EntityFireworkRocket var11 = new EntityFireworkRocket(p_77648_3_, (double)((float)p_77648_4_ + p_77648_8_), (double)((float)p_77648_5_ + p_77648_9_), (double)((float)p_77648_6_ + p_77648_10_), p_77648_1_);
            p_77648_3_.spawnEntityInWorld(var11);

            if (!p_77648_2_.capabilities.isCreativeMode)
            {
                --p_77648_1_.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_)
    {
        if (p_77624_1_.hasTagCompound())
        {
            NBTTagCompound var5 = p_77624_1_.getTagCompound().getCompoundTag("Fireworks");

            if (var5 != null)
            {
                if (var5.func_150297_b("Flight", 99))
                {
                    p_77624_3_.add(StatCollector.translateToLocal("item.fireworks.flight") + " " + var5.getByte("Flight"));
                }

                NBTTagList var6 = var5.getTagList("Explosions", 10);

                if (var6 != null && var6.tagCount() > 0)
                {
                    for (int var7 = 0; var7 < var6.tagCount(); ++var7)
                    {
                        NBTTagCompound var8 = var6.getCompoundTagAt(var7);
                        ArrayList var9 = new ArrayList();
                        ItemFireworkCharge.func_150902_a(var8, var9);

                        if (var9.size() > 0)
                        {
                            for (int var10 = 1; var10 < var9.size(); ++var10)
                            {
                                var9.set(var10, "  " + (String)var9.get(var10));
                            }

                            p_77624_3_.addAll(var9);
                        }
                    }
                }
            }
        }
    }
}
