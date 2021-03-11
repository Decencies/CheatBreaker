package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class WorldGenDesertWells extends WorldGenerator
{


    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        while (p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_, p_76484_5_) && p_76484_4_ > 2)
        {
            --p_76484_4_;
        }

        if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_) != Blocks.sand)
        {
            return false;
        }
        else
        {
            int var6;
            int var7;

            for (var6 = -2; var6 <= 2; ++var6)
            {
                for (var7 = -2; var7 <= 2; ++var7)
                {
                    if (p_76484_1_.isAirBlock(p_76484_3_ + var6, p_76484_4_ - 1, p_76484_5_ + var7) && p_76484_1_.isAirBlock(p_76484_3_ + var6, p_76484_4_ - 2, p_76484_5_ + var7))
                    {
                        return false;
                    }
                }
            }

            for (var6 = -1; var6 <= 0; ++var6)
            {
                for (var7 = -2; var7 <= 2; ++var7)
                {
                    for (int var8 = -2; var8 <= 2; ++var8)
                    {
                        p_76484_1_.setBlock(p_76484_3_ + var7, p_76484_4_ + var6, p_76484_5_ + var8, Blocks.sandstone, 0, 2);
                    }
                }
            }

            p_76484_1_.setBlock(p_76484_3_, p_76484_4_, p_76484_5_, Blocks.flowing_water, 0, 2);
            p_76484_1_.setBlock(p_76484_3_ - 1, p_76484_4_, p_76484_5_, Blocks.flowing_water, 0, 2);
            p_76484_1_.setBlock(p_76484_3_ + 1, p_76484_4_, p_76484_5_, Blocks.flowing_water, 0, 2);
            p_76484_1_.setBlock(p_76484_3_, p_76484_4_, p_76484_5_ - 1, Blocks.flowing_water, 0, 2);
            p_76484_1_.setBlock(p_76484_3_, p_76484_4_, p_76484_5_ + 1, Blocks.flowing_water, 0, 2);

            for (var6 = -2; var6 <= 2; ++var6)
            {
                for (var7 = -2; var7 <= 2; ++var7)
                {
                    if (var6 == -2 || var6 == 2 || var7 == -2 || var7 == 2)
                    {
                        p_76484_1_.setBlock(p_76484_3_ + var6, p_76484_4_ + 1, p_76484_5_ + var7, Blocks.sandstone, 0, 2);
                    }
                }
            }

            p_76484_1_.setBlock(p_76484_3_ + 2, p_76484_4_ + 1, p_76484_5_, Blocks.stone_slab, 1, 2);
            p_76484_1_.setBlock(p_76484_3_ - 2, p_76484_4_ + 1, p_76484_5_, Blocks.stone_slab, 1, 2);
            p_76484_1_.setBlock(p_76484_3_, p_76484_4_ + 1, p_76484_5_ + 2, Blocks.stone_slab, 1, 2);
            p_76484_1_.setBlock(p_76484_3_, p_76484_4_ + 1, p_76484_5_ - 2, Blocks.stone_slab, 1, 2);

            for (var6 = -1; var6 <= 1; ++var6)
            {
                for (var7 = -1; var7 <= 1; ++var7)
                {
                    if (var6 == 0 && var7 == 0)
                    {
                        p_76484_1_.setBlock(p_76484_3_ + var6, p_76484_4_ + 4, p_76484_5_ + var7, Blocks.sandstone, 0, 2);
                    }
                    else
                    {
                        p_76484_1_.setBlock(p_76484_3_ + var6, p_76484_4_ + 4, p_76484_5_ + var7, Blocks.stone_slab, 1, 2);
                    }
                }
            }

            for (var6 = 1; var6 <= 3; ++var6)
            {
                p_76484_1_.setBlock(p_76484_3_ - 1, p_76484_4_ + var6, p_76484_5_ - 1, Blocks.sandstone, 0, 2);
                p_76484_1_.setBlock(p_76484_3_ - 1, p_76484_4_ + var6, p_76484_5_ + 1, Blocks.sandstone, 0, 2);
                p_76484_1_.setBlock(p_76484_3_ + 1, p_76484_4_ + var6, p_76484_5_ - 1, Blocks.sandstone, 0, 2);
                p_76484_1_.setBlock(p_76484_3_ + 1, p_76484_4_ + var6, p_76484_5_ + 1, Blocks.sandstone, 0, 2);
            }

            return true;
        }
    }
}
