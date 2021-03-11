package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Direction;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVine extends Block
{


    public BlockVine()
    {
        super(Material.vine);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 20;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        float var5 = 0.0625F;
        int var6 = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);
        float var7 = 1.0F;
        float var8 = 1.0F;
        float var9 = 1.0F;
        float var10 = 0.0F;
        float var11 = 0.0F;
        float var12 = 0.0F;
        boolean var13 = var6 > 0;

        if ((var6 & 2) != 0)
        {
            var10 = Math.max(var10, 0.0625F);
            var7 = 0.0F;
            var8 = 0.0F;
            var11 = 1.0F;
            var9 = 0.0F;
            var12 = 1.0F;
            var13 = true;
        }

        if ((var6 & 8) != 0)
        {
            var7 = Math.min(var7, 0.9375F);
            var10 = 1.0F;
            var8 = 0.0F;
            var11 = 1.0F;
            var9 = 0.0F;
            var12 = 1.0F;
            var13 = true;
        }

        if ((var6 & 4) != 0)
        {
            var12 = Math.max(var12, 0.0625F);
            var9 = 0.0F;
            var7 = 0.0F;
            var10 = 1.0F;
            var8 = 0.0F;
            var11 = 1.0F;
            var13 = true;
        }

        if ((var6 & 1) != 0)
        {
            var9 = Math.min(var9, 0.9375F);
            var12 = 1.0F;
            var7 = 0.0F;
            var10 = 1.0F;
            var8 = 0.0F;
            var11 = 1.0F;
            var13 = true;
        }

        if (!var13 && this.func_150093_a(p_149719_1_.getBlock(p_149719_2_, p_149719_3_ + 1, p_149719_4_)))
        {
            var8 = Math.min(var8, 0.9375F);
            var11 = 1.0F;
            var7 = 0.0F;
            var10 = 1.0F;
            var9 = 0.0F;
            var12 = 1.0F;
        }

        this.setBlockBounds(var7, var8, var9, var10, var11, var12);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World p_149707_1_, int p_149707_2_, int p_149707_3_, int p_149707_4_, int p_149707_5_)
    {
        switch (p_149707_5_)
        {
            case 1:
                return this.func_150093_a(p_149707_1_.getBlock(p_149707_2_, p_149707_3_ + 1, p_149707_4_));

            case 2:
                return this.func_150093_a(p_149707_1_.getBlock(p_149707_2_, p_149707_3_, p_149707_4_ + 1));

            case 3:
                return this.func_150093_a(p_149707_1_.getBlock(p_149707_2_, p_149707_3_, p_149707_4_ - 1));

            case 4:
                return this.func_150093_a(p_149707_1_.getBlock(p_149707_2_ + 1, p_149707_3_, p_149707_4_));

            case 5:
                return this.func_150093_a(p_149707_1_.getBlock(p_149707_2_ - 1, p_149707_3_, p_149707_4_));

            default:
                return false;
        }
    }

    private boolean func_150093_a(Block p_150093_1_)
    {
        return p_150093_1_.renderAsNormalBlock() && p_150093_1_.blockMaterial.blocksMovement();
    }

    private boolean func_150094_e(World p_150094_1_, int p_150094_2_, int p_150094_3_, int p_150094_4_)
    {
        int var5 = p_150094_1_.getBlockMetadata(p_150094_2_, p_150094_3_, p_150094_4_);
        int var6 = var5;

        if (var5 > 0)
        {
            for (int var7 = 0; var7 <= 3; ++var7)
            {
                int var8 = 1 << var7;

                if ((var5 & var8) != 0 && !this.func_150093_a(p_150094_1_.getBlock(p_150094_2_ + Direction.offsetX[var7], p_150094_3_, p_150094_4_ + Direction.offsetZ[var7])) && (p_150094_1_.getBlock(p_150094_2_, p_150094_3_ + 1, p_150094_4_) != this || (p_150094_1_.getBlockMetadata(p_150094_2_, p_150094_3_ + 1, p_150094_4_) & var8) == 0))
                {
                    var6 &= ~var8;
                }
            }
        }

        if (var6 == 0 && !this.func_150093_a(p_150094_1_.getBlock(p_150094_2_, p_150094_3_ + 1, p_150094_4_)))
        {
            return false;
        }
        else
        {
            if (var6 != var5)
            {
                p_150094_1_.setBlockMetadataWithNotify(p_150094_2_, p_150094_3_, p_150094_4_, var6, 2);
            }

            return true;
        }
    }

    public int getBlockColor()
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int p_149741_1_)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return p_149720_1_.getBiomeGenForCoords(p_149720_2_, p_149720_4_).getBiomeFoliageColor(p_149720_2_, p_149720_3_, p_149720_4_);
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (!p_149695_1_.isClient && !this.func_150094_e(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_))
        {
            this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
            p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        if (!p_149674_1_.isClient && p_149674_1_.rand.nextInt(4) == 0)
        {
            byte var6 = 4;
            int var7 = 5;
            boolean var8 = false;
            int var9;
            int var10;
            int var11;
            label134:

            for (var9 = p_149674_2_ - var6; var9 <= p_149674_2_ + var6; ++var9)
            {
                for (var10 = p_149674_4_ - var6; var10 <= p_149674_4_ + var6; ++var10)
                {
                    for (var11 = p_149674_3_ - 1; var11 <= p_149674_3_ + 1; ++var11)
                    {
                        if (p_149674_1_.getBlock(var9, var11, var10) == this)
                        {
                            --var7;

                            if (var7 <= 0)
                            {
                                var8 = true;
                                break label134;
                            }
                        }
                    }
                }
            }

            var9 = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);
            var10 = p_149674_1_.rand.nextInt(6);
            var11 = Direction.facingToDirection[var10];
            int var13;

            if (var10 == 1 && p_149674_3_ < 255 && p_149674_1_.isAirBlock(p_149674_2_, p_149674_3_ + 1, p_149674_4_))
            {
                if (var8)
                {
                    return;
                }

                int var15 = p_149674_1_.rand.nextInt(16) & var9;

                if (var15 > 0)
                {
                    for (var13 = 0; var13 <= 3; ++var13)
                    {
                        if (!this.func_150093_a(p_149674_1_.getBlock(p_149674_2_ + Direction.offsetX[var13], p_149674_3_ + 1, p_149674_4_ + Direction.offsetZ[var13])))
                        {
                            var15 &= ~(1 << var13);
                        }
                    }

                    if (var15 > 0)
                    {
                        p_149674_1_.setBlock(p_149674_2_, p_149674_3_ + 1, p_149674_4_, this, var15, 2);
                    }
                }
            }
            else
            {
                Block var12;
                int var14;

                if (var10 >= 2 && var10 <= 5 && (var9 & 1 << var11) == 0)
                {
                    if (var8)
                    {
                        return;
                    }

                    var12 = p_149674_1_.getBlock(p_149674_2_ + Direction.offsetX[var11], p_149674_3_, p_149674_4_ + Direction.offsetZ[var11]);

                    if (var12.blockMaterial == Material.air)
                    {
                        var13 = var11 + 1 & 3;
                        var14 = var11 + 3 & 3;

                        if ((var9 & 1 << var13) != 0 && this.func_150093_a(p_149674_1_.getBlock(p_149674_2_ + Direction.offsetX[var11] + Direction.offsetX[var13], p_149674_3_, p_149674_4_ + Direction.offsetZ[var11] + Direction.offsetZ[var13])))
                        {
                            p_149674_1_.setBlock(p_149674_2_ + Direction.offsetX[var11], p_149674_3_, p_149674_4_ + Direction.offsetZ[var11], this, 1 << var13, 2);
                        }
                        else if ((var9 & 1 << var14) != 0 && this.func_150093_a(p_149674_1_.getBlock(p_149674_2_ + Direction.offsetX[var11] + Direction.offsetX[var14], p_149674_3_, p_149674_4_ + Direction.offsetZ[var11] + Direction.offsetZ[var14])))
                        {
                            p_149674_1_.setBlock(p_149674_2_ + Direction.offsetX[var11], p_149674_3_, p_149674_4_ + Direction.offsetZ[var11], this, 1 << var14, 2);
                        }
                        else if ((var9 & 1 << var13) != 0 && p_149674_1_.isAirBlock(p_149674_2_ + Direction.offsetX[var11] + Direction.offsetX[var13], p_149674_3_, p_149674_4_ + Direction.offsetZ[var11] + Direction.offsetZ[var13]) && this.func_150093_a(p_149674_1_.getBlock(p_149674_2_ + Direction.offsetX[var13], p_149674_3_, p_149674_4_ + Direction.offsetZ[var13])))
                        {
                            p_149674_1_.setBlock(p_149674_2_ + Direction.offsetX[var11] + Direction.offsetX[var13], p_149674_3_, p_149674_4_ + Direction.offsetZ[var11] + Direction.offsetZ[var13], this, 1 << (var11 + 2 & 3), 2);
                        }
                        else if ((var9 & 1 << var14) != 0 && p_149674_1_.isAirBlock(p_149674_2_ + Direction.offsetX[var11] + Direction.offsetX[var14], p_149674_3_, p_149674_4_ + Direction.offsetZ[var11] + Direction.offsetZ[var14]) && this.func_150093_a(p_149674_1_.getBlock(p_149674_2_ + Direction.offsetX[var14], p_149674_3_, p_149674_4_ + Direction.offsetZ[var14])))
                        {
                            p_149674_1_.setBlock(p_149674_2_ + Direction.offsetX[var11] + Direction.offsetX[var14], p_149674_3_, p_149674_4_ + Direction.offsetZ[var11] + Direction.offsetZ[var14], this, 1 << (var11 + 2 & 3), 2);
                        }
                        else if (this.func_150093_a(p_149674_1_.getBlock(p_149674_2_ + Direction.offsetX[var11], p_149674_3_ + 1, p_149674_4_ + Direction.offsetZ[var11])))
                        {
                            p_149674_1_.setBlock(p_149674_2_ + Direction.offsetX[var11], p_149674_3_, p_149674_4_ + Direction.offsetZ[var11], this, 0, 2);
                        }
                    }
                    else if (var12.blockMaterial.isOpaque() && var12.renderAsNormalBlock())
                    {
                        p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, var9 | 1 << var11, 2);
                    }
                }
                else if (p_149674_3_ > 1)
                {
                    var12 = p_149674_1_.getBlock(p_149674_2_, p_149674_3_ - 1, p_149674_4_);

                    if (var12.blockMaterial == Material.air)
                    {
                        var13 = p_149674_1_.rand.nextInt(16) & var9;

                        if (var13 > 0)
                        {
                            p_149674_1_.setBlock(p_149674_2_, p_149674_3_ - 1, p_149674_4_, this, var13, 2);
                        }
                    }
                    else if (var12 == this)
                    {
                        var13 = p_149674_1_.rand.nextInt(16) & var9;
                        var14 = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_ - 1, p_149674_4_);

                        if (var14 != (var14 | var13))
                        {
                            p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_ - 1, p_149674_4_, var14 | var13, 2);
                        }
                    }
                }
            }
        }
    }

    public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_)
    {
        byte var10 = 0;

        switch (p_149660_5_)
        {
            case 2:
                var10 = 1;
                break;

            case 3:
                var10 = 4;
                break;

            case 4:
                var10 = 8;
                break;

            case 5:
                var10 = 2;
        }

        return var10 != 0 ? var10 : p_149660_9_;
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return null;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }

    public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_, int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_)
    {
        if (!p_149636_1_.isClient && p_149636_2_.getCurrentEquippedItem() != null && p_149636_2_.getCurrentEquippedItem().getItem() == Items.shears)
        {
            p_149636_2_.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
            this.dropBlockAsItem_do(p_149636_1_, p_149636_3_, p_149636_4_, p_149636_5_, new ItemStack(Blocks.vine, 1, 0));
        }
        else
        {
            super.harvestBlock(p_149636_1_, p_149636_2_, p_149636_3_, p_149636_4_, p_149636_5_, p_149636_6_);
        }
    }
}
