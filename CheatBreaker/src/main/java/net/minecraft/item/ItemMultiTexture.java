package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class ItemMultiTexture extends ItemBlock
{
    protected final Block field_150941_b;
    protected final String[] field_150942_c;


    public ItemMultiTexture(Block p_i45346_1_, Block p_i45346_2_, String[] p_i45346_3_)
    {
        super(p_i45346_1_);
        this.field_150941_b = p_i45346_2_;
        this.field_150942_c = p_i45346_3_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return this.field_150941_b.getIcon(2, p_77617_1_);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    public int getMetadata(int p_77647_1_)
    {
        return p_77647_1_;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack p_77667_1_)
    {
        int var2 = p_77667_1_.getItemDamage();

        if (var2 < 0 || var2 >= this.field_150942_c.length)
        {
            var2 = 0;
        }

        return super.getUnlocalizedName() + "." + this.field_150942_c[var2];
    }
}
