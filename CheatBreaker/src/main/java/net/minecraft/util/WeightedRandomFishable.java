package net.minecraft.util;

import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class WeightedRandomFishable extends WeightedRandom.Item
{
    private final ItemStack field_150711_b;
    private float field_150712_c;
    private boolean field_150710_d;


    public WeightedRandomFishable(ItemStack p_i45317_1_, int p_i45317_2_)
    {
        super(p_i45317_2_);
        this.field_150711_b = p_i45317_1_;
    }

    public ItemStack func_150708_a(Random p_150708_1_)
    {
        ItemStack var2 = this.field_150711_b.copy();

        if (this.field_150712_c > 0.0F)
        {
            int var3 = (int)(this.field_150712_c * (float)this.field_150711_b.getMaxDamage());
            int var4 = var2.getMaxDamage() - p_150708_1_.nextInt(p_150708_1_.nextInt(var3) + 1);

            if (var4 > var3)
            {
                var4 = var3;
            }

            if (var4 < 1)
            {
                var4 = 1;
            }

            var2.setItemDamage(var4);
        }

        if (this.field_150710_d)
        {
            EnchantmentHelper.addRandomEnchantment(p_150708_1_, var2, 30);
        }

        return var2;
    }

    public WeightedRandomFishable func_150709_a(float p_150709_1_)
    {
        this.field_150712_c = p_150709_1_;
        return this;
    }

    public WeightedRandomFishable func_150707_a()
    {
        this.field_150710_d = true;
        return this;
    }
}
