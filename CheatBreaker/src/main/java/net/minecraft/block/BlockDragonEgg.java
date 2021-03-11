package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDragonEgg extends Block
{


    public BlockDragonEgg()
    {
        super(Material.dragonEgg);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
    }

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_, p_149726_4_, this, this.func_149738_a(p_149726_1_));
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        p_149695_1_.scheduleBlockUpdate(p_149695_2_, p_149695_3_, p_149695_4_, this, this.func_149738_a(p_149695_1_));
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        this.func_150018_e(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
    }

    private void func_150018_e(World p_150018_1_, int p_150018_2_, int p_150018_3_, int p_150018_4_)
    {
        if (BlockFalling.func_149831_e(p_150018_1_, p_150018_2_, p_150018_3_ - 1, p_150018_4_) && p_150018_3_ >= 0)
        {
            byte var5 = 32;

            if (!BlockFalling.field_149832_M && p_150018_1_.checkChunksExist(p_150018_2_ - var5, p_150018_3_ - var5, p_150018_4_ - var5, p_150018_2_ + var5, p_150018_3_ + var5, p_150018_4_ + var5))
            {
                EntityFallingBlock var6 = new EntityFallingBlock(p_150018_1_, (double)((float)p_150018_2_ + 0.5F), (double)((float)p_150018_3_ + 0.5F), (double)((float)p_150018_4_ + 0.5F), this);
                p_150018_1_.spawnEntityInWorld(var6);
            }
            else
            {
                p_150018_1_.setBlockToAir(p_150018_2_, p_150018_3_, p_150018_4_);

                while (BlockFalling.func_149831_e(p_150018_1_, p_150018_2_, p_150018_3_ - 1, p_150018_4_) && p_150018_3_ > 0)
                {
                    --p_150018_3_;
                }

                if (p_150018_3_ > 0)
                {
                    p_150018_1_.setBlock(p_150018_2_, p_150018_3_, p_150018_4_, this, 0, 2);
                }
            }
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        this.func_150019_m(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_);
        return true;
    }

    /**
     * Called when a player hits the block. Args: world, x, y, z, player
     */
    public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_, EntityPlayer p_149699_5_)
    {
        this.func_150019_m(p_149699_1_, p_149699_2_, p_149699_3_, p_149699_4_);
    }

    private void func_150019_m(World p_150019_1_, int p_150019_2_, int p_150019_3_, int p_150019_4_)
    {
        if (p_150019_1_.getBlock(p_150019_2_, p_150019_3_, p_150019_4_) == this)
        {
            for (int var5 = 0; var5 < 1000; ++var5)
            {
                int var6 = p_150019_2_ + p_150019_1_.rand.nextInt(16) - p_150019_1_.rand.nextInt(16);
                int var7 = p_150019_3_ + p_150019_1_.rand.nextInt(8) - p_150019_1_.rand.nextInt(8);
                int var8 = p_150019_4_ + p_150019_1_.rand.nextInt(16) - p_150019_1_.rand.nextInt(16);

                if (p_150019_1_.getBlock(var6, var7, var8).blockMaterial == Material.air)
                {
                    if (!p_150019_1_.isClient)
                    {
                        p_150019_1_.setBlock(var6, var7, var8, this, p_150019_1_.getBlockMetadata(p_150019_2_, p_150019_3_, p_150019_4_), 2);
                        p_150019_1_.setBlockToAir(p_150019_2_, p_150019_3_, p_150019_4_);
                    }
                    else
                    {
                        short var9 = 128;

                        for (int var10 = 0; var10 < var9; ++var10)
                        {
                            double var11 = p_150019_1_.rand.nextDouble();
                            float var13 = (p_150019_1_.rand.nextFloat() - 0.5F) * 0.2F;
                            float var14 = (p_150019_1_.rand.nextFloat() - 0.5F) * 0.2F;
                            float var15 = (p_150019_1_.rand.nextFloat() - 0.5F) * 0.2F;
                            double var16 = (double)var6 + (double)(p_150019_2_ - var6) * var11 + (p_150019_1_.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
                            double var18 = (double)var7 + (double)(p_150019_3_ - var7) * var11 + p_150019_1_.rand.nextDouble() * 1.0D - 0.5D;
                            double var20 = (double)var8 + (double)(p_150019_4_ - var8) * var11 + (p_150019_1_.rand.nextDouble() - 0.5D) * 1.0D + 0.5D;
                            p_150019_1_.spawnParticle("portal", var16, var18, var20, (double)var13, (double)var14, (double)var15);
                        }
                    }

                    return;
                }
            }
        }
    }

    public int func_149738_a(World p_149738_1_)
    {
        return 5;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
    {
        return true;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 27;
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return Item.getItemById(0);
    }
}
