package net.minecraft.item;

import net.minecraft.block.BlockLeaves;
import net.minecraft.util.IIcon;

public class ItemLeaves extends ItemBlock
{
    private final BlockLeaves field_150940_b;


    public ItemLeaves(BlockLeaves p_i45344_1_)
    {
        super(p_i45344_1_);
        this.field_150940_b = p_i45344_1_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_ | 4;
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.field_150940_b.getIcon(0, p_77617_1_);
    }

    public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_)
    {
        return this.field_150940_b.getRenderColor(p_82790_1_.getItemDamage());
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        int var2 = p_77667_1_.getItemDamage();

        if (var2 < 0 || var2 >= this.field_150940_b.func_150125_e().length)
        {
            var2 = 0;
        }

        return super.getUnlocalizedName() + "." + this.field_150940_b.func_150125_e()[var2];
    }
}
