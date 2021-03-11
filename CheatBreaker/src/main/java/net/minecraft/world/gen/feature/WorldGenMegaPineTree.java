package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaPineTree extends WorldGenHugeTrees
{
    private boolean field_150542_e;
    

    public WorldGenMegaPineTree(boolean p_i45457_1_, boolean p_i45457_2_)
    {
        super(p_i45457_1_, 13, 15, 1, 1);
        this.field_150542_e = p_i45457_2_;
    }

    public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
    {
        int var6 = this.func_150533_a(p_76484_2_);

        if (!this.func_150537_a(p_76484_1_, p_76484_2_, p_76484_3_, p_76484_4_, p_76484_5_, var6))
        {
            return false;
        }
        else
        {
            this.func_150541_c(p_76484_1_, p_76484_3_, p_76484_5_, p_76484_4_ + var6, 0, p_76484_2_);

            for (int var7 = 0; var7 < var6; ++var7)
            {
                Block var8 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + var7, p_76484_5_);

                if (var8.getMaterial() == Material.air || var8.getMaterial() == Material.leaves)
                {
                    this.func_150516_a(p_76484_1_, p_76484_3_, p_76484_4_ + var7, p_76484_5_, Blocks.log, this.woodMetadata);
                }

                if (var7 < var6 - 1)
                {
                    var8 = p_76484_1_.getBlock(p_76484_3_ + 1, p_76484_4_ + var7, p_76484_5_);

                    if (var8.getMaterial() == Material.air || var8.getMaterial() == Material.leaves)
                    {
                        this.func_150516_a(p_76484_1_, p_76484_3_ + 1, p_76484_4_ + var7, p_76484_5_, Blocks.log, this.woodMetadata);
                    }

                    var8 = p_76484_1_.getBlock(p_76484_3_ + 1, p_76484_4_ + var7, p_76484_5_ + 1);

                    if (var8.getMaterial() == Material.air || var8.getMaterial() == Material.leaves)
                    {
                        this.func_150516_a(p_76484_1_, p_76484_3_ + 1, p_76484_4_ + var7, p_76484_5_ + 1, Blocks.log, this.woodMetadata);
                    }

                    var8 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + var7, p_76484_5_ + 1);

                    if (var8.getMaterial() == Material.air || var8.getMaterial() == Material.leaves)
                    {
                        this.func_150516_a(p_76484_1_, p_76484_3_, p_76484_4_ + var7, p_76484_5_ + 1, Blocks.log, this.woodMetadata);
                    }
                }
            }

            return true;
        }
    }

    private void func_150541_c(World p_150541_1_, int p_150541_2_, int p_150541_3_, int p_150541_4_, int p_150541_5_, Random p_150541_6_)
    {
        int var7 = p_150541_6_.nextInt(5);

        if (this.field_150542_e)
        {
            var7 += this.baseHeight;
        }
        else
        {
            var7 += 3;
        }

        int var8 = 0;

        for (int var9 = p_150541_4_ - var7; var9 <= p_150541_4_; ++var9)
        {
            int var10 = p_150541_4_ - var9;
            int var11 = p_150541_5_ + MathHelper.floor_float((float)var10 / (float)var7 * 3.5F);
            this.func_150535_a(p_150541_1_, p_150541_2_, var9, p_150541_3_, var11 + (var10 > 0 && var11 == var8 && (var9 & 1) == 0 ? 1 : 0), p_150541_6_);
            var8 = var11;
        }
    }

    public void func_150524_b(World p_150524_1_, Random p_150524_2_, int p_150524_3_, int p_150524_4_, int p_150524_5_)
    {
        this.func_150539_c(p_150524_1_, p_150524_2_, p_150524_3_ - 1, p_150524_4_, p_150524_5_ - 1);
        this.func_150539_c(p_150524_1_, p_150524_2_, p_150524_3_ + 2, p_150524_4_, p_150524_5_ - 1);
        this.func_150539_c(p_150524_1_, p_150524_2_, p_150524_3_ - 1, p_150524_4_, p_150524_5_ + 2);
        this.func_150539_c(p_150524_1_, p_150524_2_, p_150524_3_ + 2, p_150524_4_, p_150524_5_ + 2);

        for (int var6 = 0; var6 < 5; ++var6)
        {
            int var7 = p_150524_2_.nextInt(64);
            int var8 = var7 % 8;
            int var9 = var7 / 8;

            if (var8 == 0 || var8 == 7 || var9 == 0 || var9 == 7)
            {
                this.func_150539_c(p_150524_1_, p_150524_2_, p_150524_3_ - 3 + var8, p_150524_4_, p_150524_5_ - 3 + var9);
            }
        }
    }

    private void func_150539_c(World p_150539_1_, Random p_150539_2_, int p_150539_3_, int p_150539_4_, int p_150539_5_)
    {
        for (int var6 = -2; var6 <= 2; ++var6)
        {
            for (int var7 = -2; var7 <= 2; ++var7)
            {
                if (Math.abs(var6) != 2 || Math.abs(var7) != 2)
                {
                    this.func_150540_a(p_150539_1_, p_150539_3_ + var6, p_150539_4_, p_150539_5_ + var7);
                }
            }
        }
    }

    private void func_150540_a(World p_150540_1_, int p_150540_2_, int p_150540_3_, int p_150540_4_)
    {
        for (int var5 = p_150540_3_ + 2; var5 >= p_150540_3_ - 3; --var5)
        {
            Block var6 = p_150540_1_.getBlock(p_150540_2_, var5, p_150540_4_);

            if (var6 == Blocks.grass || var6 == Blocks.dirt)
            {
                this.func_150516_a(p_150540_1_, p_150540_2_, var5, p_150540_4_, Blocks.dirt, 2);
                break;
            }

            if (var6.getMaterial() != Material.air && var5 < p_150540_3_)
            {
                break;
            }
        }
    }
}
