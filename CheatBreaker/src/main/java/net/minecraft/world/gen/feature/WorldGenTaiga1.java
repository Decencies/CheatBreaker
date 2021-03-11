package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class WorldGenTaiga1 extends WorldGenAbstractTree
{


    public WorldGenTaiga1()
    {
        super(false);
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        int var6 = p_76484_2_.nextInt(5) + 7;
        int var7 = var6 - p_76484_2_.nextInt(2) - 3;
        int var8 = var6 - var7;
        int var9 = 1 + p_76484_2_.nextInt(var8 + 1);
        boolean var10 = true;

        if (p_76484_4_ >= 1 && p_76484_4_ + var6 + 1 <= 256)
        {
            int var13;
            int var14;
            int var19;

            for (int var11 = p_76484_4_; var11 <= p_76484_4_ + 1 + var6 && var10; ++var11)
            {
                boolean var12 = true;

                if (var11 - p_76484_4_ < var7)
                {
                    var19 = 0;
                }
                else
                {
                    var19 = var9;
                }

                for (var13 = p_76484_3_ - var19; var13 <= p_76484_3_ + var19 && var10; ++var13)
                {
                    for (var14 = p_76484_5_ - var19; var14 <= p_76484_5_ + var19 && var10; ++var14)
                    {
                        if (var11 >= 0 && var11 < 256)
                        {
                            Block var15 = p_76484_1_.getBlock(var13, var11, var14);

                            if (!this.func_150523_a(var15))
                            {
                                var10 = false;
                            }
                        }
                        else
                        {
                            var10 = false;
                        }
                    }
                }
            }

            if (!var10)
            {
                return false;
            }
            else
            {
                Block var18 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_);

                if ((var18 == Blocks.grass || var18 == Blocks.dirt) && p_76484_4_ < 256 - var6 - 1)
                {
                    this.func_150515_a(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, Blocks.dirt);
                    var19 = 0;

                    for (var13 = p_76484_4_ + var6; var13 >= p_76484_4_ + var7; --var13)
                    {
                        for (var14 = p_76484_3_ - var19; var14 <= p_76484_3_ + var19; ++var14)
                        {
                            int var21 = var14 - p_76484_3_;

                            for (int var16 = p_76484_5_ - var19; var16 <= p_76484_5_ + var19; ++var16)
                            {
                                int var17 = var16 - p_76484_5_;

                                if ((Math.abs(var21) != var19 || Math.abs(var17) != var19 || var19 <= 0) && !p_76484_1_.getBlock(var14, var13, var16).func_149730_j())
                                {
                                    this.func_150516_a(p_76484_1_, var14, var13, var16, Blocks.leaves, 1);
                                }
                            }
                        }

                        if (var19 >= 1 && var13 == p_76484_4_ + var7 + 1)
                        {
                            --var19;
                        }
                        else if (var19 < var9)
                        {
                            ++var19;
                        }
                    }

                    for (var13 = 0; var13 < var6 - 1; ++var13)
                    {
                        Block var20 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + var13, p_76484_5_);

                        if (var20.getMaterial() == Material.air || var20.getMaterial() == Material.leaves)
                        {
                            this.func_150516_a(p_76484_1_, p_76484_3_, p_76484_4_ + var13, p_76484_5_, Blocks.log, 1);
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
}
