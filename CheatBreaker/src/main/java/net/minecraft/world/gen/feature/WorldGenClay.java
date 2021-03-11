package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class WorldGenClay extends WorldGenerator
{
    private Block field_150546_a;

    /** The number of blocks to generate. */
    private int numberOfBlocks;


    public WorldGenClay(int p_i2011_1_)
    {
        this.field_150546_a = Blocks.clay;
        this.numberOfBlocks = p_i2011_1_;
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        if (p_76484_1_.getBlock(p_76484_3_, p_76484_4_, p_76484_5_).getMaterial() != Material.water)
        {
            return false;
        }
        else
        {
            int var6 = p_76484_2_.nextInt(this.numberOfBlocks - 2) + 2;
            byte var7 = 1;

            for (int var8 = p_76484_3_ - var6; var8 <= p_76484_3_ + var6; ++var8)
            {
                for (int var9 = p_76484_5_ - var6; var9 <= p_76484_5_ + var6; ++var9)
                {
                    int var10 = var8 - p_76484_3_;
                    int var11 = var9 - p_76484_5_;

                    if (var10 * var10 + var11 * var11 <= var6 * var6)
                    {
                        for (int var12 = p_76484_4_ - var7; var12 <= p_76484_4_ + var7; ++var12)
                        {
                            Block var13 = p_76484_1_.getBlock(var8, var12, var9);

                            if (var13 == Blocks.dirt || var13 == Blocks.clay)
                            {
                                p_76484_1_.setBlock(var8, var12, var9, this.field_150546_a, 0, 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}
