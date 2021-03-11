package net.minecraft.block;

import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockNewLog extends BlockLog
{
    public static final String[] field_150169_M = new String[] {"acacia", "big_oak"};


    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_150167_a = new IIcon[field_150169_M.length];
        this.field_150166_b = new IIcon[field_150169_M.length];

        for (int var2 = 0; var2 < this.field_150167_a.length; ++var2)
        {
            this.field_150167_a[var2] = p_149651_1_.registerIcon(this.getTextureName() + "_" + field_150169_M[var2]);
            this.field_150166_b[var2] = p_149651_1_.registerIcon(this.getTextureName() + "_" + field_150169_M[var2] + "_top");
        }
    }
}
