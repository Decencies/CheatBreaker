package net.minecraft.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class WeightedRandom
{


    /**
     * Returns the total weight of all items in a collection.
     */
    public static int getTotalWeight(Collection p_76272_0_)
    {
        int var1 = 0;
        WeightedRandom.Item var3;

        for (Iterator var2 = p_76272_0_.iterator(); var2.hasNext(); var1 += var3.itemWeight)
        {
            var3 = (WeightedRandom.Item)var2.next();
        }

        return var1;
    }

    /**
     * Returns a random choice from the input items, with a total weight value.
     */
    public static WeightedRandom.Item getRandomItem(Random p_76273_0_, Collection p_76273_1_, int p_76273_2_)
    {
        if (p_76273_2_ <= 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            int var3 = p_76273_0_.nextInt(p_76273_2_);
            Iterator var4 = p_76273_1_.iterator();
            WeightedRandom.Item var5;

            do
            {
                if (!var4.hasNext())
                {
                    return null;
                }

                var5 = (WeightedRandom.Item)var4.next();
                var3 -= var5.itemWeight;
            }
            while (var3 >= 0);

            return var5;
        }
    }

    /**
     * Returns a random choice from the input items.
     */
    public static WeightedRandom.Item getRandomItem(Random p_76271_0_, Collection p_76271_1_)
    {
        return getRandomItem(p_76271_0_, p_76271_1_, getTotalWeight(p_76271_1_));
    }

    /**
     * Returns the total weight of all items in a array.
     */
    public static int getTotalWeight(WeightedRandom.Item[] p_76270_0_)
    {
        int var1 = 0;
        WeightedRandom.Item[] var2 = p_76270_0_;
        int var3 = p_76270_0_.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            WeightedRandom.Item var5 = var2[var4];
            var1 += var5.itemWeight;
        }

        return var1;
    }

    /**
     * Returns a random choice from the input array of items, with a total weight value.
     */
    public static WeightedRandom.Item getRandomItem(Random p_76269_0_, WeightedRandom.Item[] p_76269_1_, int p_76269_2_)
    {
        if (p_76269_2_ <= 0)
        {
            throw new IllegalArgumentException();
        }
        else
        {
            int var3 = p_76269_0_.nextInt(p_76269_2_);
            WeightedRandom.Item[] var4 = p_76269_1_;
            int var5 = p_76269_1_.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                WeightedRandom.Item var7 = var4[var6];
                var3 -= var7.itemWeight;

                if (var3 < 0)
                {
                    return var7;
                }
            }

            return null;
        }
    }

    /**
     * Returns a random choice from the input items.
     */
    public static WeightedRandom.Item getRandomItem(Random p_76274_0_, WeightedRandom.Item[] p_76274_1_)
    {
        return getRandomItem(p_76274_0_, p_76274_1_, getTotalWeight(p_76274_1_));
    }

    public static class Item
    {
        protected int itemWeight;


        public Item(int p_i1556_1_)
        {
            this.itemWeight = p_i1556_1_;
        }
    }
}
