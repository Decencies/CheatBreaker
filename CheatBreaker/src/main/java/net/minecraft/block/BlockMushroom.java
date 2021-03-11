package net.minecraft.block;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;

public class BlockMushroom extends BlockBush implements IGrowable
{


    protected BlockMushroom()
    {
        float var1 = 0.2F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var1 * 2.0F, 0.5F + var1);
        this.setTickRandomly(true);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        if (p_149674_5_.nextInt(25) == 0)
        {
            byte var6 = 4;
            int var7 = 5;
            int var8;
            int var9;
            int var10;

            for (var8 = p_149674_2_ - var6; var8 <= p_149674_2_ + var6; ++var8)
            {
                for (var9 = p_149674_4_ - var6; var9 <= p_149674_4_ + var6; ++var9)
                {
                    for (var10 = p_149674_3_ - 1; var10 <= p_149674_3_ + 1; ++var10)
                    {
                        if (p_149674_1_.getBlock(var8, var10, var9) == this)
                        {
                            --var7;

                            if (var7 <= 0)
                            {
                                return;
                            }
                        }
                    }
                }
            }

            var8 = p_149674_2_ + p_149674_5_.nextInt(3) - 1;
            var9 = p_149674_3_ + p_149674_5_.nextInt(2) - p_149674_5_.nextInt(2);
            var10 = p_149674_4_ + p_149674_5_.nextInt(3) - 1;

            for (int var11 = 0; var11 < 4; ++var11)
            {
                if (p_149674_1_.isAirBlock(var8, var9, var10) && this.canBlockStay(p_149674_1_, var8, var9, var10))
                {
                    p_149674_2_ = var8;
                    p_149674_3_ = var9;
                    p_149674_4_ = var10;
                }

                var8 = p_149674_2_ + p_149674_5_.nextInt(3) - 1;
                var9 = p_149674_3_ + p_149674_5_.nextInt(2) - p_149674_5_.nextInt(2);
                var10 = p_149674_4_ + p_149674_5_.nextInt(3) - 1;
            }

            if (p_149674_1_.isAirBlock(var8, var9, var10) && this.canBlockStay(p_149674_1_, var8, var9, var10))
            {
                p_149674_1_.setBlock(var8, var9, var10, this, 0, 2);
            }
        }
    }

    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return super.canPlaceBlockAt(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_) && this.canBlockStay(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_);
    }

    protected boolean func_149854_a(Block p_149854_1_)
    {
        return p_149854_1_.func_149730_j();
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_)
    {
        if (p_149718_3_ >= 0 && p_149718_3_ < 256)
        {
            Block var5 = p_149718_1_.getBlock(p_149718_2_, p_149718_3_ - 1, p_149718_4_);
            return var5 == Blocks.mycelium || var5 == Blocks.dirt && p_149718_1_.getBlockMetadata(p_149718_2_, p_149718_3_ - 1, p_149718_4_) == 2 || p_149718_1_.getFullBlockLightValue(p_149718_2_, p_149718_3_, p_149718_4_) < 13 && this.func_149854_a(var5);
        }
        else
        {
            return false;
        }
    }

    public boolean func_149884_c(World p_149884_1_, int p_149884_2_, int p_149884_3_, int p_149884_4_, Random p_149884_5_)
    {
        int var6 = p_149884_1_.getBlockMetadata(p_149884_2_, p_149884_3_, p_149884_4_);
        p_149884_1_.setBlockToAir(p_149884_2_, p_149884_3_, p_149884_4_);
        WorldGenBigMushroom var7 = null;

        if (this == Blocks.brown_mushroom)
        {
            var7 = new WorldGenBigMushroom(0);
        }
        else if (this == Blocks.red_mushroom)
        {
            var7 = new WorldGenBigMushroom(1);
        }

        if (var7 != null && var7.generate(p_149884_1_, p_149884_5_, p_149884_2_, p_149884_3_, p_149884_4_))
        {
            return true;
        }
        else
        {
            p_149884_1_.setBlock(p_149884_2_, p_149884_3_, p_149884_4_, this, var6, 3);
            return false;
        }
    }

    public boolean func_149851_a(World p_149851_1_, int p_149851_2_, int p_149851_3_, int p_149851_4_, boolean p_149851_5_)
    {
        return true;
    }

    public boolean func_149852_a(World p_149852_1_, Random p_149852_2_, int p_149852_3_, int p_149852_4_, int p_149852_5_)
    {
        return (double)p_149852_2_.nextFloat() < 0.4D;
    }

    public void func_149853_b(World p_149853_1_, Random p_149853_2_, int p_149853_3_, int p_149853_4_, int p_149853_5_)
    {
        this.func_149884_c(p_149853_1_, p_149853_3_, p_149853_4_, p_149853_5_, p_149853_2_);
    }
}
