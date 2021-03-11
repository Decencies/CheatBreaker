package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MapGenRavine extends MapGenBase
{
    private float[] field_75046_d = new float[1024];


    protected void func_151540_a(long p_151540_1_, int p_151540_3_, int p_151540_4_, Block[] p_151540_5_, double p_151540_6_, double p_151540_8_, double p_151540_10_, float p_151540_12_, float p_151540_13_, float p_151540_14_, int p_151540_15_, int p_151540_16_, double p_151540_17_)
    {
        Random var19 = new Random(p_151540_1_);
        double var20 = (double)(p_151540_3_ * 16 + 8);
        double var22 = (double)(p_151540_4_ * 16 + 8);
        float var24 = 0.0F;
        float var25 = 0.0F;

        if (p_151540_16_ <= 0)
        {
            int var26 = this.range * 16 - 16;
            p_151540_16_ = var26 - var19.nextInt(var26 / 4);
        }

        boolean var53 = false;

        if (p_151540_15_ == -1)
        {
            p_151540_15_ = p_151540_16_ / 2;
            var53 = true;
        }

        float var27 = 1.0F;

        for (int var28 = 0; var28 < 256; ++var28)
        {
            if (var28 == 0 || var19.nextInt(3) == 0)
            {
                var27 = 1.0F + var19.nextFloat() * var19.nextFloat() * 1.0F;
            }

            this.field_75046_d[var28] = var27 * var27;
        }

        for (; p_151540_15_ < p_151540_16_; ++p_151540_15_)
        {
            double var54 = 1.5D + (double)(MathHelper.sin((float)p_151540_15_ * (float)Math.PI / (float)p_151540_16_) * p_151540_12_ * 1.0F);
            double var30 = var54 * p_151540_17_;
            var54 *= (double)var19.nextFloat() * 0.25D + 0.75D;
            var30 *= (double)var19.nextFloat() * 0.25D + 0.75D;
            float var32 = MathHelper.cos(p_151540_14_);
            float var33 = MathHelper.sin(p_151540_14_);
            p_151540_6_ += (double)(MathHelper.cos(p_151540_13_) * var32);
            p_151540_8_ += (double)var33;
            p_151540_10_ += (double)(MathHelper.sin(p_151540_13_) * var32);
            p_151540_14_ *= 0.7F;
            p_151540_14_ += var25 * 0.05F;
            p_151540_13_ += var24 * 0.05F;
            var25 *= 0.8F;
            var24 *= 0.5F;
            var25 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 2.0F;
            var24 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 4.0F;

            if (var53 || var19.nextInt(4) != 0)
            {
                double var34 = p_151540_6_ - var20;
                double var36 = p_151540_10_ - var22;
                double var38 = (double)(p_151540_16_ - p_151540_15_);
                double var40 = (double)(p_151540_12_ + 2.0F + 16.0F);

                if (var34 * var34 + var36 * var36 - var38 * var38 > var40 * var40)
                {
                    return;
                }

                if (p_151540_6_ >= var20 - 16.0D - var54 * 2.0D && p_151540_10_ >= var22 - 16.0D - var54 * 2.0D && p_151540_6_ <= var20 + 16.0D + var54 * 2.0D && p_151540_10_ <= var22 + 16.0D + var54 * 2.0D)
                {
                    int var55 = MathHelper.floor_double(p_151540_6_ - var54) - p_151540_3_ * 16 - 1;
                    int var35 = MathHelper.floor_double(p_151540_6_ + var54) - p_151540_3_ * 16 + 1;
                    int var56 = MathHelper.floor_double(p_151540_8_ - var30) - 1;
                    int var37 = MathHelper.floor_double(p_151540_8_ + var30) + 1;
                    int var57 = MathHelper.floor_double(p_151540_10_ - var54) - p_151540_4_ * 16 - 1;
                    int var39 = MathHelper.floor_double(p_151540_10_ + var54) - p_151540_4_ * 16 + 1;

                    if (var55 < 0)
                    {
                        var55 = 0;
                    }

                    if (var35 > 16)
                    {
                        var35 = 16;
                    }

                    if (var56 < 1)
                    {
                        var56 = 1;
                    }

                    if (var37 > 248)
                    {
                        var37 = 248;
                    }

                    if (var57 < 0)
                    {
                        var57 = 0;
                    }

                    if (var39 > 16)
                    {
                        var39 = 16;
                    }

                    boolean var58 = false;
                    int var41;
                    int var44;

                    for (var41 = var55; !var58 && var41 < var35; ++var41)
                    {
                        for (int var42 = var57; !var58 && var42 < var39; ++var42)
                        {
                            for (int var43 = var37 + 1; !var58 && var43 >= var56 - 1; --var43)
                            {
                                var44 = (var41 * 16 + var42) * 256 + var43;

                                if (var43 >= 0 && var43 < 256)
                                {
                                    Block var45 = p_151540_5_[var44];

                                    if (var45 == Blocks.flowing_water || var45 == Blocks.water)
                                    {
                                        var58 = true;
                                    }

                                    if (var43 != var56 - 1 && var41 != var55 && var41 != var35 - 1 && var42 != var57 && var42 != var39 - 1)
                                    {
                                        var43 = var56;
                                    }
                                }
                            }
                        }
                    }

                    if (!var58)
                    {
                        for (var41 = var55; var41 < var35; ++var41)
                        {
                            double var59 = ((double)(var41 + p_151540_3_ * 16) + 0.5D - p_151540_6_) / var54;

                            for (var44 = var57; var44 < var39; ++var44)
                            {
                                double var60 = ((double)(var44 + p_151540_4_ * 16) + 0.5D - p_151540_10_) / var54;
                                int var47 = (var41 * 16 + var44) * 256 + var37;
                                boolean var48 = false;

                                if (var59 * var59 + var60 * var60 < 1.0D)
                                {
                                    for (int var49 = var37 - 1; var49 >= var56; --var49)
                                    {
                                        double var50 = ((double)var49 + 0.5D - p_151540_8_) / var30;

                                        if ((var59 * var59 + var60 * var60) * (double)this.field_75046_d[var49] + var50 * var50 / 6.0D < 1.0D)
                                        {
                                            Block var52 = p_151540_5_[var47];

                                            if (var52 == Blocks.grass)
                                            {
                                                var48 = true;
                                            }

                                            if (var52 == Blocks.stone || var52 == Blocks.dirt || var52 == Blocks.grass)
                                            {
                                                if (var49 < 10)
                                                {
                                                    p_151540_5_[var47] = Blocks.flowing_lava;
                                                }
                                                else
                                                {
                                                    p_151540_5_[var47] = null;

                                                    if (var48 && p_151540_5_[var47 - 1] == Blocks.dirt)
                                                    {
                                                        p_151540_5_[var47 - 1] = this.worldObj.getBiomeGenForCoords(var41 + p_151540_3_ * 16, var44 + p_151540_4_ * 16).topBlock;
                                                    }
                                                }
                                            }
                                        }

                                        --var47;
                                    }
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
        if (this.rand.nextInt(50) == 0)
        {
            double var7 = (double)(p_151538_2_ * 16 + this.rand.nextInt(16));
            double var9 = (double)(this.rand.nextInt(this.rand.nextInt(40) + 8) + 20);
            double var11 = (double)(p_151538_3_ * 16 + this.rand.nextInt(16));
            byte var13 = 1;

            for (int var14 = 0; var14 < var13; ++var14)
            {
                float var15 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var16 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var17 = (this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
                this.func_151540_a(this.rand.nextLong(), p_151538_4_, p_151538_5_, p_151538_6_, var7, var9, var11, var17, var15, var16, 0, 0, 3.0D);
            }
        }
    }
}
