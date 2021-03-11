package net.minecraft.item;

import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

public class ItemFireworkCharge extends Item
{
    private IIcon field_150904_a;


    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public IIcon getIconFromDamageForRenderPass(int p_77618_1_, int p_77618_2_)
    {
        return p_77618_2_ > 0 ? this.field_150904_a : super.getIconFromDamageForRenderPass(p_77618_1_, p_77618_2_);
    }

    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        if (p_82790_2_ != 1)
        {
            return super.getColorFromItemStack(p_82790_1_, p_82790_2_);
        }
        else
        {
            NBTBase var3 = func_150903_a(p_82790_1_, "Colors");

            if (var3 != null && var3 instanceof NBTTagIntArray)
            {
                NBTTagIntArray var4 = (NBTTagIntArray)var3;
                int[] var5 = var4.func_150302_c();

                if (var5.length == 1)
                {
                    return var5[0];
                }
                else
                {
                    int var6 = 0;
                    int var7 = 0;
                    int var8 = 0;
                    int[] var9 = var5;
                    int var10 = var5.length;

                    for (int var11 = 0; var11 < var10; ++var11)
                    {
                        int var12 = var9[var11];
                        var6 += (var12 & 16711680) >> 16;
                        var7 += (var12 & 65280) >> 8;
                        var8 += (var12 & 255) >> 0;
                    }

                    var6 /= var5.length;
                    var7 /= var5.length;
                    var8 /= var5.length;
                    return var6 << 16 | var7 << 8 | var8;
                }
            }
            else
            {
                return 9079434;
            }
        }
    }

    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    public static NBTBase func_150903_a(ItemStack p_150903_0_, String p_150903_1_)
    {
        if (p_150903_0_.hasTagCompound())
        {
            NBTTagCompound var2 = p_150903_0_.getTagCompound().getCompoundTag("Explosion");

            if (var2 != null)
            {
                return var2.getTag(p_150903_1_);
            }
        }

        return null;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_)
    {
        if (p_77624_1_.hasTagCompound())
        {
            NBTTagCompound var5 = p_77624_1_.getTagCompound().getCompoundTag("Explosion");

            if (var5 != null)
            {
                func_150902_a(var5, p_77624_3_);
            }
        }
    }

    public static void func_150902_a(NBTTagCompound p_150902_0_, List p_150902_1_)
    {
        byte var2 = p_150902_0_.getByte("Type");

        if (var2 >= 0 && var2 <= 4)
        {
            p_150902_1_.add(StatCollector.translateToLocal("item.fireworksCharge.type." + var2).trim());
        }
        else
        {
            p_150902_1_.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
        }

        int[] var3 = p_150902_0_.getIntArray("Colors");
        int var8;
        int var9;

        if (var3.length > 0)
        {
            boolean var4 = true;
            String var5 = "";
            int[] var6 = var3;
            int var7 = var3.length;

            for (var8 = 0; var8 < var7; ++var8)
            {
                var9 = var6[var8];

                if (!var4)
                {
                    var5 = var5 + ", ";
                }

                var4 = false;
                boolean var10 = false;

                for (int var11 = 0; var11 < 16; ++var11)
                {
                    if (var9 == ItemDye.field_150922_c[var11])
                    {
                        var10 = true;
                        var5 = var5 + StatCollector.translateToLocal("item.fireworksCharge." + ItemDye.field_150923_a[var11]);
                        break;
                    }
                }

                if (!var10)
                {
                    var5 = var5 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
            }

            p_150902_1_.add(var5);
        }

        int[] var13 = p_150902_0_.getIntArray("FadeColors");
        boolean var14;

        if (var13.length > 0)
        {
            var14 = true;
            String var15 = StatCollector.translateToLocal("item.fireworksCharge.fadeTo") + " ";
            int[] var17 = var13;
            var8 = var13.length;

            for (var9 = 0; var9 < var8; ++var9)
            {
                int var18 = var17[var9];

                if (!var14)
                {
                    var15 = var15 + ", ";
                }

                var14 = false;
                boolean var19 = false;

                for (int var12 = 0; var12 < 16; ++var12)
                {
                    if (var18 == ItemDye.field_150922_c[var12])
                    {
                        var19 = true;
                        var15 = var15 + StatCollector.translateToLocal("item.fireworksCharge." + ItemDye.field_150923_a[var12]);
                        break;
                    }
                }

                if (!var19)
                {
                    var15 = var15 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
            }

            p_150902_1_.add(var15);
        }

        var14 = p_150902_0_.getBoolean("Trail");

        if (var14)
        {
            p_150902_1_.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
        }

        boolean var16 = p_150902_0_.getBoolean("Flicker");

        if (var16)
        {
            p_150902_1_.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
        }
    }

    public void registerIcons(IIconRegister p_94581_1_)
    {
        super.registerIcons(p_94581_1_);
        this.field_150904_a = p_94581_1_.registerIcon(this.getIconString() + "_overlay");
    }
}
