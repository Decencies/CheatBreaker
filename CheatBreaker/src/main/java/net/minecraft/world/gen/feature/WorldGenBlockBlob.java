package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class WorldGenBlockBlob extends WorldGenerator
{
    private Block field_150545_a;
    private int field_150544_b;


    public WorldGenBlockBlob(Block p_i45450_1_, int p_i45450_2_)
    {
        super(false);
        this.field_150545_a = p_i45450_1_;
        this.field_150544_b = p_i45450_2_;
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        while (true)
        {
            if (p_76484_4_ > 3)
            {
                label63:
                {
                    if (!p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_))
                    {
                        Block var6 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_);

                        if (var6 == Blocks.grass || var6 == Blocks.dirt || var6 == Blocks.stone)
                        {
                            break label63;
                        }
                    }

                    --p_76484_4_;
                    continue;
                }
            }

            if (p_76484_4_ <= 3)
            {
                return false;
            }

            int var18 = this.field_150544_b;

            for (int var7 = 0; var18 >= 0 && var7 < 3; ++var7)
            {
                int var8 = var18 + p_76484_2_.nextInt(2);
                int var9 = var18 + p_76484_2_.nextInt(2);
                int var10 = var18 + p_76484_2_.nextInt(2);
                float var11 = (float)(var8 + var9 + var10) * 0.333F + 0.5F;

                for (int var12 = p_76484_3_ - var8; var12 <= p_76484_3_ + var8; ++var12)
                {
                    for (int var13 = p_76484_5_ - var10; var13 <= p_76484_5_ + var10; ++var13)
                    {
                        for (int var14 = p_76484_4_ - var9; var14 <= p_76484_4_ + var9; ++var14)
                        {
                            float var15 = (float)(var12 - p_76484_3_);
                            float var16 = (float)(var13 - p_76484_5_);
                            float var17 = (float)(var14 - p_76484_4_);

                            if (var15 * var15 + var16 * var16 + var17 * var17 <= var11 * var11)
                            {
                                p_76484_1_.setBlock(var12, var14, var13, this.field_150545_a, 0, 4);
                            }
                        }
                    }
                }

                p_76484_3_ += -(var18 + 1) + p_76484_2_.nextInt(2 + var18 * 2);
                p_76484_5_ += -(var18 + 1) + p_76484_2_.nextInt(2 + var18 * 2);
                p_76484_4_ += 0 - p_76484_2_.nextInt(2);
            }

            return true;
        }
    }
}
