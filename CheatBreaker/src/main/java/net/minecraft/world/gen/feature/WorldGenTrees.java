package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class WorldGenTrees extends WorldGenAbstractTree
{
    /** The minimum height of a generated tree. */
    private final int minTreeHeight;

    /** True if this tree should grow Vines. */
    private final boolean vinesGrow;

    /** The metadata value of the wood to use in tree generation. */
    private final int metaWood;

    /** The metadata value of the leaves to use in tree generation. */
    private final int metaLeaves;


    public WorldGenTrees(boolean p_i2027_1_)
    {
        this(p_i2027_1_, 4, 0, 0, false);
    }

    public WorldGenTrees(boolean p_i2028_1_, int p_i2028_2_, int p_i2028_3_, int p_i2028_4_, boolean p_i2028_5_)
    {
        super(p_i2028_1_);
        this.minTreeHeight = p_i2028_2_;
        this.metaWood = p_i2028_3_;
        this.metaLeaves = p_i2028_4_;
        this.vinesGrow = p_i2028_5_;
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        int var6 = p_76484_2_.nextInt(3) + this.minTreeHeight;
        boolean var7 = true;

        if (p_76484_4_ >= 1 && p_76484_4_ + var6 + 1 <= 256)
        {
            byte var9;
            int var11;
            Block var12;

            for (int var8 = p_76484_4_; var8 <= p_76484_4_ + 1 + var6; ++var8)
            {
                var9 = 1;

                if (var8 == p_76484_4_)
                {
                    var9 = 0;
                }

                if (var8 >= p_76484_4_ + 1 + var6 - 2)
                {
                    var9 = 2;
                }

                for (int var10 = p_76484_3_ - var9; var10 <= p_76484_3_ + var9 && var7; ++var10)
                {
                    for (var11 = p_76484_5_ - var9; var11 <= p_76484_5_ + var9 && var7; ++var11)
                    {
                        if (var8 >= 0 && var8 < 256)
                        {
                            var12 = p_76484_1_.getBlock(var10, var8, var11);

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
                Block var19 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_);

                if ((var19 == Blocks.grass || var19 == Blocks.dirt || var19 == Blocks.farmland) && p_76484_4_ < 256 - var6 - 1)
                {
                    this.func_150515_a(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, Blocks.dirt);
                    var9 = 3;
                    byte var20 = 0;
                    int var13;
                    int var14;
                    int var15;
                    int var21;

                    for (var11 = p_76484_4_ - var9 + var6; var11 <= p_76484_4_ + var6; ++var11)
                    {
                        var21 = var11 - (p_76484_4_ + var6);
                        var13 = var20 + 1 - var21 / 2;

                        for (var14 = p_76484_3_ - var13; var14 <= p_76484_3_ + var13; ++var14)
                        {
                            var15 = var14 - p_76484_3_;

                            for (int var16 = p_76484_5_ - var13; var16 <= p_76484_5_ + var13; ++var16)
                            {
                                int var17 = var16 - p_76484_5_;

                                if (Math.abs(var15) != var13 || Math.abs(var17) != var13 || p_76484_2_.nextInt(2) != 0 && var21 != 0)
                                {
                                    Block var18 = p_76484_1_.getBlock(var14, var11, var16);

                                    if (var18.getMaterial() == Material.air || var18.getMaterial() == Material.leaves)
                                    {
                                        this.func_150516_a(p_76484_1_, var14, var11, var16, Blocks.leaves, this.metaLeaves);
                                    }
                                }
                            }
                        }
                    }

                    for (var11 = 0; var11 < var6; ++var11)
                    {
                        var12 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + var11, p_76484_5_);

                        if (var12.getMaterial() == Material.air || var12.getMaterial() == Material.leaves)
                        {
                            this.func_150516_a(p_76484_1_, p_76484_3_, p_76484_4_ + var11, p_76484_5_, Blocks.log, this.metaWood);

                            if (this.vinesGrow && var11 > 0)
                            {
                                if (p_76484_2_.nextInt(3) > 0 && p_76484_1_.isAirBlock(p_76484_3_ - 1, p_76484_4_ + var11, p_76484_5_))
                                {
                                    this.func_150516_a(p_76484_1_, p_76484_3_ - 1, p_76484_4_ + var11, p_76484_5_, Blocks.vine, 8);
                                }

                                if (p_76484_2_.nextInt(3) > 0 && p_76484_1_.isAirBlock(p_76484_3_ + 1, p_76484_4_ + var11, p_76484_5_))
                                {
                                    this.func_150516_a(p_76484_1_, p_76484_3_ + 1, p_76484_4_ + var11, p_76484_5_, Blocks.vine, 2);
                                }

                                if (p_76484_2_.nextInt(3) > 0 && p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_ + var11, p_76484_5_ - 1))
                                {
                                    this.func_150516_a(p_76484_1_, p_76484_3_, p_76484_4_ + var11, p_76484_5_ - 1, Blocks.vine, 1);
                                }

                                if (p_76484_2_.nextInt(3) > 0 && p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_ + var11, p_76484_5_ + 1))
                                {
                                    this.func_150516_a(p_76484_1_, p_76484_3_, p_76484_4_ + var11, p_76484_5_ + 1, Blocks.vine, 4);
                                }
                            }
                        }
                    }

                    if (this.vinesGrow)
                    {
                        for (var11 = p_76484_4_ - 3 + var6; var11 <= p_76484_4_ + var6; ++var11)
                        {
                            var21 = var11 - (p_76484_4_ + var6);
                            var13 = 2 - var21 / 2;

                            for (var14 = p_76484_3_ - var13; var14 <= p_76484_3_ + var13; ++var14)
                            {
                                for (var15 = p_76484_5_ - var13; var15 <= p_76484_5_ + var13; ++var15)
                                {
                                    if (p_76484_1_.getBlock(var14, var11, var15).getMaterial() == Material.leaves)
                                    {
                                        if (p_76484_2_.nextInt(4) == 0 && p_76484_1_.getBlock(var14 - 1, var11, var15).getMaterial() == Material.air)
                                        {
                                            this.growVines(p_76484_1_, var14 - 1, var11, var15, 8);
                                        }

                                        if (p_76484_2_.nextInt(4) == 0 && p_76484_1_.getBlock(var14 + 1, var11, var15).getMaterial() == Material.air)
                                        {
                                            this.growVines(p_76484_1_, var14 + 1, var11, var15, 2);
                                        }

                                        if (p_76484_2_.nextInt(4) == 0 && p_76484_1_.getBlock(var14, var11, var15 - 1).getMaterial() == Material.air)
                                        {
                                            this.growVines(p_76484_1_, var14, var11, var15 - 1, 1);
                                        }

                                        if (p_76484_2_.nextInt(4) == 0 && p_76484_1_.getBlock(var14, var11, var15 + 1).getMaterial() == Material.air)
                                        {
                                            this.growVines(p_76484_1_, var14, var11, var15 + 1, 4);
                                        }
                                    }
                                }
                            }
                        }

                        if (p_76484_2_.nextInt(5) == 0 && var6 > 5)
                        {
                            for (var11 = 0; var11 < 2; ++var11)
                            {
                                for (var21 = 0; var21 < 4; ++var21)
                                {
                                    if (p_76484_2_.nextInt(4 - var11) == 0)
                                    {
                                        var13 = p_76484_2_.nextInt(3);
                                        this.func_150516_a(p_76484_1_, p_76484_3_ + Direction.offsetX[Direction.rotateOpposite[var21]], p_76484_4_ + var6 - 5 + var11, p_76484_5_ + Direction.offsetZ[Direction.rotateOpposite[var21]], Blocks.cocoa, var13 << 2 | var21);
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
     * Grows vines downward from the given block for a given length. Args: World, x, starty, z, vine-length
     */
    private void growVines(World p_76529_1_, int p_76529_2_, int p_76529_3_, int p_76529_4_, int p_76529_5_)
    {
        this.func_150516_a(p_76529_1_, p_76529_2_, p_76529_3_, p_76529_4_, Blocks.vine, p_76529_5_);
        int var6 = 4;

        while (true)
        {
            --p_76529_3_;

            if (p_76529_1_.getBlock(p_76529_2_, p_76529_3_, p_76529_4_).getMaterial() != Material.air || var6 <= 0)
            {
                return;
            }

            this.func_150516_a(p_76529_1_, p_76529_2_, p_76529_3_, p_76529_4_, Blocks.vine, p_76529_5_);
            --var6;
        }
    }
}
