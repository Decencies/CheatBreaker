package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockHugeMushroom extends Block
{
    private static final String[] field_149793_a = new String[] {"skin_brown", "skin_red"};
    private final int field_149792_b;
    private IIcon[] field_149794_M;
    private IIcon field_149795_N;
    private IIcon field_149796_O;


    public BlockHugeMushroom(Material p_i45412_1_, int p_i45412_2_)
    {
        super(p_i45412_1_);
        this.field_149792_b = p_i45412_2_;
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_2_ == 10 && p_149691_1_ > 1 ? this.field_149795_N : (p_149691_2_ >= 1 && p_149691_2_ <= 9 && p_149691_1_ == 1 ? this.field_149794_M[this.field_149792_b] : (p_149691_2_ >= 1 && p_149691_2_ <= 3 && p_149691_1_ == 2 ? this.field_149794_M[this.field_149792_b] : (p_149691_2_ >= 7 && p_149691_2_ <= 9 && p_149691_1_ == 3 ? this.field_149794_M[this.field_149792_b] : ((p_149691_2_ == 1 || p_149691_2_ == 4 || p_149691_2_ == 7) && p_149691_1_ == 4 ? this.field_149794_M[this.field_149792_b] : ((p_149691_2_ == 3 || p_149691_2_ == 6 || p_149691_2_ == 9) && p_149691_1_ == 5 ? this.field_149794_M[this.field_149792_b] : (p_149691_2_ == 14 ? this.field_149794_M[this.field_149792_b] : (p_149691_2_ == 15 ? this.field_149795_N : this.field_149796_O)))))));
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        int var2 = p_149745_1_.nextInt(10) - 7;

        if (var2 < 0)
        {
            var2 = 0;
        }

        return var2;
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return Item.getItemById(Block.getIdFromBlock(Blocks.brown_mushroom) + this.field_149792_b);
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return Item.getItemById(Block.getIdFromBlock(Blocks.brown_mushroom) + this.field_149792_b);
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_149794_M = new IIcon[field_149793_a.length];

        for (int var2 = 0; var2 < this.field_149794_M.length; ++var2)
        {
            this.field_149794_M[var2] = p_149651_1_.registerIcon(this.getTextureName() + "_" + field_149793_a[var2]);
        }

        this.field_149796_O = p_149651_1_.registerIcon(this.getTextureName() + "_" + "inside");
        this.field_149795_N = p_149651_1_.registerIcon(this.getTextureName() + "_" + "skin_stem");
    }
}
