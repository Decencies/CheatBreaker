package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MapGenCavesHell extends MapGenBase
{


    protected void func_151544_a(long p_151544_1_, int p_151544_3_, int p_151544_4_, Block[] p_151544_5_, double p_151544_6_, double p_151544_8_, double p_151544_10_)
    {
        this.func_151543_a(p_151544_1_, p_151544_3_, p_151544_4_, p_151544_5_, p_151544_6_, p_151544_8_, p_151544_10_, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void func_151543_a(long p_151543_1_, int p_151543_3_, int p_151543_4_, Block[] p_151543_5_, double p_151543_6_, double p_151543_8_, double p_151543_10_, float p_151543_12_, float p_151543_13_, float p_151543_14_, int p_151543_15_, int p_151543_16_, double p_151543_17_)
    {
        double var19 = (double)(p_151543_3_ * 16 + 8);
        double var21 = (double)(p_151543_4_ * 16 + 8);
        float var23 = 0.0F;
        float var24 = 0.0F;
        Random var25 = new Random(p_151543_1_);

        if (p_151543_16_ <= 0)
        {
            int var26 = this.range * 16 - 16;
            p_151543_16_ = var26 - var25.nextInt(var26 / 4);
        }

        boolean var53 = false;

        if (p_151543_15_ == -1)
        {
            p_151543_15_ = p_151543_16_ / 2;
            var53 = true;
        }

        int var27 = var25.nextInt(p_151543_16_ / 2) + p_151543_16_ / 4;

        for (boolean var28 = var25.nextInt(6) == 0; p_151543_15_ < p_151543_16_; ++p_151543_15_)
        {
            double var29 = 1.5D + (double)(MathHelper.sin((float)p_151543_15_ * (float)Math.PI / (float)p_151543_16_) * p_151543_12_ * 1.0F);
            double var31 = var29 * p_151543_17_;
            float var33 = MathHelper.cos(p_151543_14_);
            float var34 = MathHelper.sin(p_151543_14_);
            p_151543_6_ += (double)(MathHelper.cos(p_151543_13_) * var33);
            p_151543_8_ += (double)var34;
            p_151543_10_ += (double)(MathHelper.sin(p_151543_13_) * var33);

            if (var28)
            {
                p_151543_14_ *= 0.92F;
            }
            else
            {
                p_151543_14_ *= 0.7F;
            }

            p_151543_14_ += var24 * 0.1F;
            p_151543_13_ += var23 * 0.1F;
            var24 *= 0.9F;
            var23 *= 0.75F;
            var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
            var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;

            if (!var53 && p_151543_15_ == var27 && p_151543_12_ > 1.0F)
            {
                this.func_151543_a(var25.nextLong(), p_151543_3_, p_151543_4_, p_151543_5_, p_151543_6_, p_151543_8_, p_151543_10_, var25.nextFloat() * 0.5F + 0.5F, p_151543_13_ - ((float)Math.PI / 2F), p_151543_14_ / 3.0F, p_151543_15_, p_151543_16_, 1.0D);
                this.func_151543_a(var25.nextLong(), p_151543_3_, p_151543_4_, p_151543_5_, p_151543_6_, p_151543_8_, p_151543_10_, var25.nextFloat() * 0.5F + 0.5F, p_151543_13_ + ((float)Math.PI / 2F), p_151543_14_ / 3.0F, p_151543_15_, p_151543_16_, 1.0D);
                return;
            }

            if (var53 || var25.nextInt(4) != 0)
            {
                double var35 = p_151543_6_ - var19;
                double var37 = p_151543_10_ - var21;
                double var39 = (double)(p_151543_16_ - p_151543_15_);
                double var41 = (double)(p_151543_12_ + 2.0F + 16.0F);

                if (var35 * var35 + var37 * var37 - var39 * var39 > var41 * var41)
                {
                    return;
                }

                if (p_151543_6_ >= var19 - 16.0D - var29 * 2.0D && p_151543_10_ >= var21 - 16.0D - var29 * 2.0D && p_151543_6_ <= var19 + 16.0D + var29 * 2.0D && p_151543_10_ <= var21 + 16.0D + var29 * 2.0D)
                {
                    int var54 = MathHelper.floor_double(p_151543_6_ - var29) - p_151543_3_ * 16 - 1;
                    int var36 = MathHelper.floor_double(p_151543_6_ + var29) - p_151543_3_ * 16 + 1;
                    int var55 = MathHelper.floor_double(p_151543_8_ - var31) - 1;
                    int var38 = MathHelper.floor_double(p_151543_8_ + var31) + 1;
                    int var56 = MathHelper.floor_double(p_151543_10_ - var29) - p_151543_4_ * 16 - 1;
                    int var40 = MathHelper.floor_double(p_151543_10_ + var29) - p_151543_4_ * 16 + 1;

                    if (var54 < 0)
                    {
                        var54 = 0;
                    }

                    if (var36 > 16)
                    {
                        var36 = 16;
                    }

                    if (var55 < 1)
                    {
                        var55 = 1;
                    }

                    if (var38 > 120)
                    {
                        var38 = 120;
                    }

                    if (var56 < 0)
                    {
                        var56 = 0;
                    }

                    if (var40 > 16)
                    {
                        var40 = 16;
                    }

                    boolean var57 = false;
                    int var42;
                    int var45;

                    for (var42 = var54; !var57 && var42 < var36; ++var42)
                    {
                        for (int var43 = var56; !var57 && var43 < var40; ++var43)
                        {
                            for (int var44 = var38 + 1; !var57 && var44 >= var55 - 1; --var44)
                            {
                                var45 = (var42 * 16 + var43) * 128 + var44;

                                if (var44 >= 0 && var44 < 128)
                                {
                                    Block var46 = p_151543_5_[var45];

                                    if (var46 == Blocks.flowing_lava || var46 == Blocks.lava)
                                    {
                                        var57 = true;
                                    }

                                    if (var44 != var55 - 1 && var42 != var54 && var42 != var36 - 1 && var43 != var56 && var43 != var40 - 1)
                                    {
                                        var44 = var55;
                                    }
                                }
                            }
                        }
                    }

                    if (!var57)
                    {
                        for (var42 = var54; var42 < var36; ++var42)
                        {
                            double var58 = ((double)(var42 + p_151543_3_ * 16) + 0.5D - p_151543_6_) / var29;

                            for (var45 = var56; var45 < var40; ++var45)
                            {
                                double var59 = ((double)(var45 + p_151543_4_ * 16) + 0.5D - p_151543_10_) / var29;
                                int var48 = (var42 * 16 + var45) * 128 + var38;

                                for (int var49 = var38 - 1; var49 >= var55; --var49)
                                {
                                    double var50 = ((double)var49 + 0.5D - p_151543_8_) / var31;

                                    if (var50 > -0.7D && var58 * var58 + var50 * var50 + var59 * var59 < 1.0D)
                                    {
                                        Block var52 = p_151543_5_[var48];

                                        if (var52 == Blocks.netherrack || var52 == Blocks.dirt || var52 == Blocks.grass)
                                        {
                                            p_151543_5_[var48] = null;
                                        }
                                    }

                                    --var48;
                                }
                            }
                        }

                        if (var53)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void func_151538_a(World p_151538_1_, int p_151538_2_, int p_151538_3_, int p_151538_4_, int p_151538_5_, Block[] p_151538_6_)
    {
        int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);

        if (this.rand.nextInt(5) != 0)
        {
            var7 = 0;
        }

        for (int var8 = 0; var8 < var7; ++var8)
        {
            double var9 = (double)(p_151538_2_ * 16 + this.rand.nextInt(16));
            double var11 = (double)this.rand.nextInt(128);
            double var13 = (double)(p_151538_3_ * 16 + this.rand.nextInt(16));
            int var15 = 1;

            if (this.rand.nextInt(4) == 0)
            {
                this.func_151544_a(this.rand.nextLong(), p_151538_4_, p_151538_5_, p_151538_6_, var9, var11, var13);
                var15 += this.rand.nextInt(4);
            }

            for (int var16 = 0; var16 < var15; ++var16)
            {
                float var17 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
                this.func_151543_a(this.rand.nextLong(), p_151538_4_, p_151538_5_, p_151538_6_, var9, var11, var13, var19 * 2.0F, var17, var18, 0, 0, 0.5D);
            }
        }
    }
}
