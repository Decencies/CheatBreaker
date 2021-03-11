package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class WorldGenShrub extends WorldGenTrees
{
    private int field_150528_a;
    private int field_150527_b;


    public WorldGenShrub(int p_i2015_1_, int p_i2015_2_)
    {
        super(false);
        this.field_150527_b = p_i2015_1_;
        this.field_150528_a = p_i2015_2_;
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        Block var6;

        while (((var6 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_)).getMaterial() == Material.air || var6.getMaterial() == Material.leaves) && p_76484_4_ > 0)
        {
            --p_76484_4_;
        }

        Block var7 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_);

        if (var7 == Blocks.dirt || var7 == Blocks.grass)
        {
            ++p_76484_4_;
            this.func_150516_a(p_76484_1_, p_76484_3_, p_76484_4_, p_76484_5_, Blocks.log, this.field_150527_b);

            for (int var8 = p_76484_4_; var8 <= p_76484_4_ + 2; ++var8)
            {
                int var9 = var8 - p_76484_4_;
                int var10 = 2 - var9;

                for (int var11 = p_76484_3_ - var10; var11 <= p_76484_3_ + var10; ++var11)
                {
                    int var12 = var11 - p_76484_3_;

                    for (int var13 = p_76484_5_ - var10; var13 <= p_76484_5_ + var10; ++var13)
                    {
                        int var14 = var13 - p_76484_5_;

                        if ((Math.abs(var12) != var10 || Math.abs(var14) != var10 || p_76484_2_.nextInt(2) != 0) && !p_76484_1_.getBlock(var11, var8, var13).func_149730_j())
                        {
                            this.func_150516_a(p_76484_1_, var11, var8, var13, Blocks.leaves, this.field_150528_a);
                        }
                    }
                }
            }
        }

        return true;
    }
}
