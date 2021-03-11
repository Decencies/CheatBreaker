package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class WorldGenSavannaTree extends WorldGenAbstractTree
{


    public WorldGenSavannaTree(boolean p_i45463_1_)
    {
        super(p_i45463_1_);
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        int var6 = p_76484_2_.nextInt(3) + p_76484_2_.nextInt(3) + 5;
        boolean var7 = true;

        if (p_76484_4_ >= 1 && p_76484_4_ + var6 + 1 <= 256)
        {
            int var10;
            int var11;

            for (int var8 = p_76484_4_; var8 <= p_76484_4_ + 1 + var6; ++var8)
            {
                byte var9 = 1;

                if (var8 == p_76484_4_)
                {
                    var9 = 0;
                }

                if (var8 >= p_76484_4_ + 1 + var6 - 2)
                {
                    var9 = 2;
                }

                for (var10 = p_76484_3_ - var9; var10 <= p_76484_3_ + var9 && var7; ++var10)
                {
                    for (var11 = p_76484_5_ - var9; var11 <= p_76484_5_ + var9 && var7; ++var11)
                    {
                        if (var8 >= 0 && var8 < 256)
                        {
                            Block var12 = p_76484_1_.getBlock(var10, var8, var11);

                            if (!this.func_150523_a(var12))
                            {
                                var7 = false;
                            }
                        }
                        else
                        {
                            var7 = false;
                        }
                    }
                }
            }

            if (!var7)
            {
                return false;
            }
            else
            {
                Block var21 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_);

                if ((var21 == Blocks.grass || var21 == Blocks.dirt) && p_76484_4_ < 256 - var6 - 1)
                {
                    this.func_150515_a(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, Blocks.dirt);
                    int var22 = p_76484_2_.nextInt(4);
                    var10 = var6 - p_76484_2_.nextInt(4) - 1;
                    var11 = 3 - p_76484_2_.nextInt(3);
                    int var23 = p_76484_3_;
                    int var13 = p_76484_5_;
                    int var14 = 0;
                    int var15;
                    int var16;

                    for (var15 = 0; var15 < var6; ++var15)
                    {
                        var16 = p_76484_4_ + var15;

                        if (var15 >= var10 && var11 > 0)
                        {
                            var23 += Direction.offsetX[var22];
                            var13 += Direction.offsetZ[var22];
                            --var11;
                        }

                        Block var17 = p_76484_1_.getBlock(var23, var16, var13);

                        if (var17.getMaterial() == Material.air || var17.getMaterial() == Material.leaves)
                        {
                            this.func_150516_a(p_76484_1_, var23, var16, var13, Blocks.log2, 0);
                            var14 = var16;
                        }
                    }

                    for (var15 = -1; var15 <= 1; ++var15)
                    {
                        for (var16 = -1; var16 <= 1; ++var16)
                        {
                            this.func_150525_a(p_76484_1_, var23 + var15, var14 + 1, var13 + var16);
                        }
                    }

                    this.func_150525_a(p_76484_1_, var23 + 2, var14 + 1, var13);
                    this.func_150525_a(p_76484_1_, var23 - 2, var14 + 1, var13);
                    this.func_150525_a(p_76484_1_, var23, var14 + 1, var13 + 2);
                    this.func_150525_a(p_76484_1_, var23, var14 + 1, var13 - 2);

                    for (var15 = -3; var15 <= 3; ++var15)
                    {
                        for (var16 = -3; var16 <= 3; ++var16)
                        {
                            if (Math.abs(var15) != 3 || Math.abs(var16) != 3)
                            {
                                this.func_150525_a(p_76484_1_, var23 + var15, var14, var13 + var16);
                            }
                        }
                    }

                    var23 = p_76484_3_;
                    var13 = p_76484_5_;
                    var15 = p_76484_2_.nextInt(4);

                    if (var15 != var22)
                    {
                        var16 = var10 - p_76484_2_.nextInt(2) - 1;
                        int var24 = 1 + p_76484_2_.nextInt(3);
                        var14 = 0;
                        int var18;
                        int var19;

                        for (var18 = var16; var18 < var6 && var24 > 0; --var24)
                        {
                            if (var18 >= 1)
                            {
                                var19 = p_76484_4_ + var18;
                                var23 += Direction.offsetX[var15];
                                var13 += Direction.offsetZ[var15];
                                Block var20 = p_76484_1_.getBlock(var23, var19, var13);

                                if (var20.getMaterial() == Material.air || var20.getMaterial() == Material.leaves)
                                {
                                    this.func_150516_a(p_76484_1_, var23, var19, var13, Blocks.log2, 0);
                                    var14 = var19;
                                }
                            }

                            ++var18;
                        }

                        if (var14 > 0)
                        {
                            for (var18 = -1; var18 <= 1; ++var18)
                            {
                                for (var19 = -1; var19 <= 1; ++var19)
                                {
                                    this.func_150525_a(p_76484_1_, var23 + var18, var14 + 1, var13 + var19);
                                }
                            }

                            for (var18 = -2; var18 <= 2; ++var18)
                            {
                                for (var19 = -2; var19 <= 2; ++var19)
                                {
                                    if (Math.abs(var18) != 2 || Math.abs(var19) != 2)
                                    {
                                        this.func_150525_a(p_76484_1_, var23 + var18, var14, var13 + var19);
                                    }
                                }
                            }
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

    private void func_150525_a(World p_150525_1_, int p_150525_2_, int p_150525_3_, int p_150525_4_)
    {
        Block var5 = p_150525_1_.getBlock(p_150525_2_, p_150525_3_, p_150525_4_);

        if (var5.getMaterial() == Material.air || var5.getMaterial() == Material.leaves)
        {
            this.func_150516_a(p_150525_1_, p_150525_2_, p_150525_3_, p_150525_4_, Blocks.leaves2, 0);
        }
    }
}
