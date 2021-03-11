package net.minecraft.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockCarrot extends BlockCrops
{
    private IIcon[] field_149868_a;


    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        if (p_149691_2_ < 7)
        {
            if (p_149691_2_ == 6)
            {
                p_149691_2_ = 5;
            }

            return this.field_149868_a[p_149691_2_ >> 1];
        }
        else
        {
            return this.field_149868_a[3];
        }
    }

    protected Item func_149866_i()
    {
        return Items.carrot;
    }

    protected Item func_149865_P()
    {
        return Items.carrot;
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_149868_a = new IIcon[4];

        for (int var2 = 0; var2 < this.field_149868_a.length; ++var2)
        {
            this.field_149868_a[var2] = p_149651_1_.registerIcon(this.getTextureName() + "_stage_" + var2);
        }
    }
}
