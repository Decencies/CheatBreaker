package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirt extends Block
{
    public static final String[] field_150009_a = new String[] {"default", "default", "podzol"};
    private IIcon field_150008_b;
    private IIcon field_150010_M;


    protected BlockDirt()
    {
        super(Material.ground);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        if (p_149691_2_ == 2)
        {
            if (p_149691_1_ == 1)
            {
                return this.field_150008_b;
            }

            if (p_149691_1_ != 0)
            {
                return this.field_150010_M;
            }
        }

        return this.blockIcon;
    }

    public IIcon getIcon(IBlockAccess p_149673_1_, int p_149673_2_, int p_149673_3_, int p_149673_4_, int p_149673_5_)
    {
        int var6 = p_149673_1_.getBlockMetadata(p_149673_2_, p_149673_3_, p_149673_4_);

        if (var6 == 2)
        {
            if (p_149673_5_ == 1)
            {
                return this.field_150008_b;
            }

            if (p_149673_5_ != 0)
            {
                Material var7 = p_149673_1_.getBlock(p_149673_2_, p_149673_3_ + 1, p_149673_4_).getMaterial();

                if (var7 == Material.field_151597_y || var7 == Material.craftedSnow)
                {
                    return Blocks.grass.getIcon(p_149673_1_, p_149673_2_, p_149673_3_, p_149673_4_, p_149673_5_);
                }

                Block var8 = p_149673_1_.getBlock(p_149673_2_, p_149673_3_ + 1, p_149673_4_);

                if (var8 != Blocks.dirt && var8 != Blocks.grass)
                {
                    return this.field_150010_M;
                }
            }
        }

        return this.blockIcon;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int p_149692_1_)
    {
        return 0;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int p_149644_1_)
    {
        if (p_149644_1_ == 1)
        {
            p_149644_1_ = 0;
        }

        return super.createStackedBlock(p_149644_1_);
    }

    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        p_149666_3_.add(new ItemStack(this, 1, 0));
        p_149666_3_.add(new ItemStack(this, 1, 2));
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        super.registerBlockIcons(p_149651_1_);
        this.field_150008_b = p_149651_1_.registerIcon(this.getTextureName() + "_" + "podzol_top");
        this.field_150010_M = p_149651_1_.registerIcon(this.getTextureName() + "_" + "podzol_side");
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_)
    {
        int var5 = p_149643_1_.getBlockMetadata(p_149643_2_, p_149643_3_, p_149643_4_);

        if (var5 == 1)
        {
            var5 = 0;
        }

        return var5;
    }
}
