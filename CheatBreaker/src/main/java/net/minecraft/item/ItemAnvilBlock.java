package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;

public class ItemAnvilBlock extends ItemMultiTexture
{


    public ItemAnvilBlock(Block p_i1826_1_)
    {
        super(p_i1826_1_, p_i1826_1_, BlockAnvil.field_149834_a);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_ << 2;
    }
}
