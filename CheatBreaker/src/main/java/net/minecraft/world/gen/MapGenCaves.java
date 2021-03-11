package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MapGenCaves extends MapGenBase
{


    protected void func_151542_a(long p_151542_1_, int p_151542_3_, int p_151542_4_, Block[] p_151542_5_, double p_151542_6_, double p_151542_8_, double p_151542_10_)
    {
        this.func_151541_a(p_151542_1_, p_151542_3_, p_151542_4_, p_151542_5_, p_151542_6_, p_151542_8_, p_151542_10_, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void func_151541_a(long p_151541_1_, int p_151541_3_, int p_151541_4_, Block[] p_151541_5_, double p_151541_6_, double p_151541_8_, double p_151541_10_, float p_151541_12_, float p_151541_13_, float p_151541_14_, int p_151541_15_, int p_151541_16_, double p_151541_17_)
    {
        double var19 = (double)(p_151541_3_ * 16 + 8);
        double var21 = (double)(p_151541_4_ * 16 + 8);
        float var23 = 0.0F;
        float var24 = 0.0F;
        Random var25 = new Random(p_151541_1_);

        if (p_151541_16_ <= 0)
        {
            int var26 = this.range * 16 - 16;
            p_151541_16_ = var26 - var25.nextInt(var26 / 4);
        }

        boolean var54 = false;

        if (p_151541_15_ == -1)
        {
            p_151541_15_ = p_151541_16_ / 2;
            var54 = true;
        }

        int var27 = var25.nextInt(p_151541_16_ / 2) + p_151541_16_ / 4;

        for (boolean var28 = var25.nextInt(6) == 0; p_151541_15_ < p_151541_16_; ++p_151541_15_)
        {
            double var29 = 1.5D + (double)(MathHelper.sin((float)p_151541_15_ * (float)Math.PI / (float)p_151541_16_) * p_151541_12_ * 1.0F);
            double var31 = var29 * p_151541_17_;
            float var33 = MathHelper.cos(p_151541_14_);
            float var34 = MathHelper.sin(p_151541_14_);
            p_151541_6_ += (double)(MathHelper.cos(p_151541_13_) * var33);
            p_151541_8_ += (double)var34;
            p_151541_10_ += (double)(MathHelper.sin(p_151541_13_) * var33);

            if (var28)
            {
                p_151541_14_ *= 0.92F;
            }
            else
            {
                p_151541_14_ *= 0.7F;
            }

            p_151541_14_ += var24 * 0.1F;
            p_151541_13_ += var23 * 0.1F;
            var24 *= 0.9F;
            var23 *= 0.75F;
            var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
            var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;

            if (!var54 && p_151541_15_ == var27 && p_151541_12_ > 1.0F && p_151541_16_ > 0)
            {
                this.func_151541_a(var25.nextLong(), p_151541_3_, p_151541_4_, p_151541_5_, p_151541_6_, p_151541_8_, p_151541_10_, var25.nextFloat() * 0.5F + 0.5F, p_151541_13_ - ((float)Math.PI / 2F), p_151541_14_ / 3.0F, p_151541_15_, p_151541_16_, 1.0D);
                this.func_151541_a(var25.nextLong(), p_151541_3_, p_151541_4_, p_151541_5_, p_151541_6_, p_151541_8_, p_151541_10_, var25.nextFloat() * 0.5F + 0.5F, p_151541_13_ + ((float)Math.PI / 2F), p_151541_14_ / 3.0F, p_151541_15_, p_151541_16_, 1.0D);
                return;
            }

            if (var54 || var25.nextInt(4) != 0)
            {
                double var35 = p_151541_6_ - var19;
                double var37 = p_151541_10_ - var21;
                double var39 = (double)(p_151541_16_ - p_151541_15_);
                double var41 = (double)(p_151541_12_ + 2.0F + 16.0F);

                if (var35 * var35 + var37 * var37 - var39 * var39 > var41 * var41)
                {
                    return;
                }

                if (p_151541_6_ >= var19 - 16.0D - var29 * 2.0D && p_151541_10_ >= var21 - 16.0D - var29 * 2.0D && p_151541_6_ <= var19 + 16.0D + var29 * 2.0D && p_151541_10_ <= var21 + 16.0D + var29 * 2.0D)
                {
                    int var55 = MathHelper.floor_double(p_151541_6_ - var29) - p_151541_3_ * 16 - 1;
                    int var36 = MathHelper.floor_double(p_151541_6_ + var29) - p_151541_3_ * 16 + 1;
                    int var56 = MathHelper.floor_double(p_151541_8_ - var31) - 1;
                    int var38 = MathHelper.floor_double(p_151541_8_ + var31) + 1;
                    int var57 = MathHelper.floor_double(p_151541_10_ - var29) - p_151541_4_ * 16 - 1;
                    int var40 = MathHelper.floor_double(p_151541_10_ + var29) - p_151541_4_ * 16 + 1;

                    if (var55 < 0)
                    {
                        var55 = 0;
                    }

                    if (var36 > 16)
                    {
                        var36 = 16;
                    }

                    if (var56 < 1)
                    {
                        var56 = 1;
                    }

                    if (var38 > 248)
                    {
                        var38 = 248;
                    }

                    if (var57 < 0)
                    {
                        var57 = 0;
                    }

                    if (var40 > 16)
                    {
                        var40 = 16;
                    }

                    boolean var58 = false;
                    int var42;
                    int var45;

                    for (var42 = var55; !var58 && var42 < var36; ++var42)
                    {
                        for (int var43 = var57; !var58 && var43 < var40; ++var43)
                        {
                            for (int var44 = var38 + 1; !var58 && var44 >= var56 - 1; --var44)
                            {
                                var45 = (var42 * 16 + var43) * 256 + var44;

                                if (var44 >= 0 && var44 < 256)
                                {
                                    Block var46 = p_151541_5_[var45];

                                    if (var46 == Blocks.flowing_water || var46 == Blocks.water)
                                    {
                                        var58 = true;
                                    }

                                    if (var44 != var56 - 1 && var42 != var55 && var42 != var36 - 1 && var43 != var57 && var43 != var40 - 1)
                                    {
                                        var44 = var56;
                                    }
                                }
                            }
                        }
                    }

                    if (!var58)
                    {
                        for (var42 = var55; var42 < var36; ++var42)
                        {
                            double var59 = ((double)(var42 + p_151541_3_ * 16) + 0.5D - p_151541_6_) / var29;

                            for (var45 = var57; var45 < var40; ++var45)
                            {
                                double var60 = ((double)(var45 + p_151541_4_ * 16) + 0.5D - p_151541_10_) / var29;
                                int var48 = (var42 * 16 + var45) * 256 + var38;
                                boolean var49 = false;

                                if (var59 * var59 + var60 * var60 < 1.0D)
                                {
                                    for (int var50 = var38 - 1; var50 >= var56; --var50)
                                    {
                                        double var51 = ((double)var50 + 0.5D - p_151541_8_) / var31;

                                        if (var51 > -0.7D && var59 * var59 + var51 * var51 + var60 * var60 < 1.0D)
                                        {
                                            Block var53 = p_151541_5_[var48];

                                            if (var53 == Blocks.grass)
                                            {
                                                var49 = true;
                                            }

                                            if (var53 == Blocks.stone || var53 == Blocks.dirt || var53 == Blocks.grass)
                                            {
                                                if (var50 < 10)
                                                {
                                                    p_151541_5_[var48] = Blocks.lava;
                                                }
                                                else
                                                {
                                                    p_151541_5_[var48] = null;

                                                    if (var49 && p_151541_5_[var48 - 1] == Blocks.dirt)
                                                    {
                                                        p_151541_5_[var48 - 1] = this.worldObj.getBiomeGenForCoords(var42 + p_151541_3_ * 16, var45 + p_151541_4_ * 16).topBlock;
                                                    }
                                                }
                                            }
                                        }

                                        --var48;
                                    }
                                }
                            }
                        }

                        if (var54)
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
        int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);

        if (this.rand.nextInt(7) != 0)
        {
            var7 = 0;
        }

        for (int var8 = 0; var8 < var7; ++var8)
        {
            double var9 = (double)(p_151538_2_ * 16 + this.rand.nextInt(16));
            double var11 = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
            double var13 = (double)(p_151538_3_ * 16 + this.rand.nextInt(16));
            int var15 = 1;

            if (this.rand.nextInt(4) == 0)
            {
                this.func_151542_a(this.rand.nextLong(), p_151538_4_, p_151538_5_, p_151538_6_, var9, var11, var13);
                var15 += this.rand.nextInt(4);
            }

            for (int var16 = 0; var16 < var15; ++var16)
            {
                float var17 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                if (this.rand.nextInt(10) == 0)
                {
                    var19 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
                }

                this.func_151541_a(this.rand.nextLong(), p_151538_4_, p_151538_5_, p_151538_6_, var9, var11, var13, var19, var17, var18, 0, 0, 1.0D);
            }
        }
    }
}
