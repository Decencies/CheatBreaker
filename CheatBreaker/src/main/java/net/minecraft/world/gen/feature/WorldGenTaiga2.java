package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class WorldGenTaiga2 extends WorldGenAbstractTree
{


    public WorldGenTaiga2(boolean p_i2025_1_)
    {
        super(p_i2025_1_);
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        int var6 = p_76484_2_.nextInt(4) + 6;
        int var7 = 1 + p_76484_2_.nextInt(2);
        int var8 = var6 - var7;
        int var9 = 2 + p_76484_2_.nextInt(2);
        boolean var10 = true;

        if (p_76484_4_ >= 1 && p_76484_4_ + var6 + 1 <= 256)
        {
            int var13;
            int var22;

            for (int var11 = p_76484_4_; var11 <= p_76484_4_ + 1 + var6 && var10; ++var11)
            {
                boolean var12 = true;

                if (var11 - p_76484_4_ < var7)
                {
                    var22 = 0;
                }
                else
                {
                    var22 = var9;
                }

                for (var13 = p_76484_3_ - var22; var13 <= p_76484_3_ + var22 && var10; ++var13)
                {
                    for (int var14 = p_76484_5_ - var22; var14 <= p_76484_5_ + var22 && var10; ++var14)
                    {
                        if (var11 >= 0 && var11 < 256)
                        {
                            Block var15 = p_76484_1_.getBlock(var13, var11, var14);

                            if (var15.getMaterial() != Material.air && var15.getMaterial() != Material.leaves)
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
                Block var21 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_);

                if ((var21 == Blocks.grass || var21 == Blocks.dirt || var21 == Blocks.farmland) && p_76484_4_ < 256 - var6 - 1)
                {
                    this.func_150515_a(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, Blocks.dirt);
                    var22 = p_76484_2_.nextInt(2);
                    var13 = 1;
                    byte var23 = 0;
                    int var16;
                    int var24;

                    for (var24 = 0; var24 <= var8; ++var24)
                    {
                        var16 = p_76484_4_ + var6 - var24;

                        for (int var17 = p_76484_3_ - var22; var17 <= p_76484_3_ + var22; ++var17)
                        {
                            int var18 = var17 - p_76484_3_;

                            for (int var19 = p_76484_5_ - var22; var19 <= p_76484_5_ + var22; ++var19)
                            {
                                int var20 = var19 - p_76484_5_;

                                if ((Math.abs(var18) != var22 || Math.abs(var20) != var22 || var22 <= 0) && !p_76484_1_.getBlock(var17, var16, var19).func_149730_j())
                                {
                                    this.func_150516_a(p_76484_1_, var17, var16, var19, Blocks.leaves, 1);
                                }
                            }
                        }

                        if (var22 >= var13)
                        {
                            var22 = var23;
                            var23 = 1;
                            ++var13;

                            if (var13 > var9)
                            {
                                var13 = var9;
                            }
                        }
                        else
                        {
                            ++var22;
                        }
                    }

                    var24 = p_76484_2_.nextInt(3);

                    for (var16 = 0; var16 < var6 - var24; ++var16)
                    {
                        Block var25 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + var16, p_76484_5_);

                        if (var25.getMaterial() == Material.air || var25.getMaterial() == Material.leaves)
                        {
                            this.func_150516_a(p_76484_1_, p_76484_3_, p_76484_4_ + var16, p_76484_5_, Blocks.log, 1);
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
