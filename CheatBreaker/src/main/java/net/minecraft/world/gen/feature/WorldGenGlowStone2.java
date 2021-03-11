package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class WorldGenGlowStone2 extends WorldGenerator
{


    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        if (!p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_, p_76484_5_))
        {
            return false;
        }
        else if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + 1, p_76484_5_) != Blocks.netherrack)
        {
            return false;
        }
        else
        {
            p_76484_1_.setBlock(p_76484_3_, p_76484_4_, p_76484_5_, Blocks.glowstone, 0, 2);

            for (int var6 = 0; var6 < 1500; ++var6)
            {
                int var7 = p_76484_3_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);
                int var8 = p_76484_4_ - p_76484_2_.nextInt(12);
                int var9 = p_76484_5_ + p_76484_2_.nextInt(8) - p_76484_2_.nextInt(8);

                if (p_76484_1_.getBlock(var7, var8, var9).getMaterial() == Material.air)
                {
                    int var10 = 0;

                    for (int var11 = 0; var11 < 6; ++var11)
                    {
                        Block var12 = null;

                        if (var11 == 0)
                        {
                            var12 = p_76484_1_.getBlock(var7 - 1, var8, var9);
                        }

                        if (var11 == 1)
                        {
                            var12 = p_76484_1_.getBlock(var7 + 1, var8, var9);
                        }

                        if (var11 == 2)
                        {
                            var12 = p_76484_1_.getBlock(var7, var8 - 1, var9);
                        }

                        if (var11 == 3)
                        {
                            var12 = p_76484_1_.getBlock(var7, var8 + 1, var9);
                        }

                        if (var11 == 4)
                        {
                            var12 = p_76484_1_.getBlock(var7, var8, var9 - 1);
                        }

                        if (var11 == 5)
                        {
                            var12 = p_76484_1_.getBlock(var7, var8, var9 + 1);
                        }

                        if (var12 == Blocks.glowstone)
                        {
                            ++var10;
                        }
                    }

                    if (var10 == 1)
                    {
                        p_76484_1_.setBlock(var7, var8, var9, Blocks.glowstone, 0, 2);
                    }
                }
            }

            return true;
        }
    }
}
