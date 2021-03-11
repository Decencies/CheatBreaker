package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class WorldGenSwamp extends WorldGenAbstractTree
{


    public WorldGenSwamp()
    {
        super(false);
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        int var6;

        for (var6 = p_76484_2_.nextInt(4) + 5; p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_).getMaterial() == Material.water; --p_76484_4_)
        {
            ;
        }

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
                    var9 = 3;
                }

                for (var10 = p_76484_3_ - var9; var10 <= p_76484_3_ + var9 && var7; ++var10)
                {
                    for (var11 = p_76484_5_ - var9; var11 <= p_76484_5_ + var9 && var7; ++var11)
                    {
                        if (var8 >= 0 && var8 < 256)
                        {
                            Block var12 = p_76484_1_.getBlock(var10, var8, var11);

                            if (var12.getMaterial() != Material.air && var12.getMaterial() != Material.leaves)
                            {
                                if (var12 != Blocks.water && var12 != Blocks.flowing_water)
                                {
                                    var7 = false;
                                }
                                else if (var8 > p_76484_4_)
                                {
                                    var7 = false;
                                }
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
                Block var16 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_);

                if ((var16 == Blocks.grass || var16 == Blocks.dirt) && p_76484_4_ < 256 - var6 - 1)
                {
                    this.func_150515_a(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, Blocks.dirt);
                    int var13;
                    int var17;
                    int var19;

                    for (var17 = p_76484_4_ - 3 + var6; var17 <= p_76484_4_ + var6; ++var17)
                    {
                        var10 = var17 - (p_76484_4_ + var6);
                        var11 = 2 - var10 / 2;

                        for (var19 = p_76484_3_ - var11; var19 <= p_76484_3_ + var11; ++var19)
                        {
                            var13 = var19 - p_76484_3_;

                            for (int var14 = p_76484_5_ - var11; var14 <= p_76484_5_ + var11; ++var14)
                            {
                                int var15 = var14 - p_76484_5_;

                                if ((Math.abs(var13) != var11 || Math.abs(var15) != var11 || p_76484_2_.nextInt(2) != 0 && var10 != 0) && !p_76484_1_.getBlock(var19, var17, var14).func_149730_j())
                                {
                                    this.func_150515_a(p_76484_1_, var19, var17, var14, Blocks.leaves);
                                }
                            }
                        }
                    }

                    for (var17 = 0; var17 < var6; ++var17)
                    {
                        Block var18 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + var17, p_76484_5_);

                        if (var18.getMaterial() == Material.air || var18.getMaterial() == Material.leaves || var18 == Blocks.flowing_water || var18 == Blocks.water)
                        {
                            this.func_150515_a(p_76484_1_, p_76484_3_, p_76484_4_ + var17, p_76484_5_, Blocks.log);
                        }
                    }

                    for (var17 = p_76484_4_ - 3 + var6; var17 <= p_76484_4_ + var6; ++var17)
                    {
                        var10 = var17 - (p_76484_4_ + var6);
                        var11 = 2 - var10 / 2;

                        for (var19 = p_76484_3_ - var11; var19 <= p_76484_3_ + var11; ++var19)
                        {
                            for (var13 = p_76484_5_ - var11; var13 <= p_76484_5_ + var11; ++var13)
                            {
                                if (p_76484_1_.getBlock(var19, var17, var13).getMaterial() == Material.leaves)
                                {
                                    if (p_76484_2_.nextInt(4) == 0 && p_76484_1_.getBlock(var19 - 1, var17, var13).getMaterial() == Material.air)
                                    {
                                        this.generateVines(p_76484_1_, var19 - 1, var17, var13, 8);
                                    }

                                    if (p_76484_2_.nextInt(4) == 0 && p_76484_1_.getBlock(var19 + 1, var17, var13).getMaterial() == Material.air)
                                    {
                                        this.generateVines(p_76484_1_, var19 + 1, var17, var13, 2);
                                    }

                                    if (p_76484_2_.nextInt(4) == 0 && p_76484_1_.getBlock(var19, var17, var13 - 1).getMaterial() == Material.air)
                                    {
                                        this.generateVines(p_76484_1_, var19, var17, var13 - 1, 1);
                                    }

                                    if (p_76484_2_.nextInt(4) == 0 && p_76484_1_.getBlock(var19, var17, var13 + 1).getMaterial() == Material.air)
                                    {
                                        this.generateVines(p_76484_1_, var19, var17, var13 + 1, 4);
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

    /**
     * Generates vines at the given position until it hits a block.
     */
    private void generateVines(World p_76536_1_, int p_76536_2_, int p_76536_3_, int p_76536_4_, int p_76536_5_)
    {
        this.func_150516_a(p_76536_1_, p_76536_2_, p_76536_3_, p_76536_4_, Blocks.vine, p_76536_5_);
        int var6 = 4;

        while (true)
        {
            --p_76536_3_;

            if (p_76536_1_.getBlock(p_76536_2_, p_76536_3_, p_76536_4_).getMaterial() != Material.air || var6 <= 0)
            {
                return;
            }

            this.func_150516_a(p_76536_1_, p_76536_2_, p_76536_3_, p_76536_4_, Blocks.vine, p_76536_5_);
            --var6;
        }
    }
}
