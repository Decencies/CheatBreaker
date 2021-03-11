package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;

public class ItemDoublePlant extends ItemMultiTexture
{


    public ItemDoublePlant(Block p_i45335_1_, BlockDoublePlant p_i45335_2_, String[] p_i45335_3_)
    {
        super(p_i45335_1_, p_i45335_2_, p_i45335_3_);
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return BlockDoublePlant.func_149890_d(p_77617_1_) == 0 ? ((BlockDoublePlant)this.field_150941_b).field_149891_b[0] : ((BlockDoublePlant)this.field_150941_b).func_149888_a(true, p_77617_1_);
    }

    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        int var3 = BlockDoublePlant.func_149890_d(p_82790_1_.getItemDamage());
        return var3 != 2 && var3 != 3 ? super.getColorFromItemStack(p_82790_1_, p_82790_2_) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }
}
