package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonMoving extends BlockContainer
{


    public BlockPistonMoving()
    {
        super(Material.piston);
        this.setHardness(-1.0F);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return null;
    }

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {}

    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        TileEntity var7 = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

        if (var7 instanceof TileEntityPiston)
        {
            ((TileEntityPiston)var7).func_145866_f();
        }
        else
        {
            super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
        }
    }

    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return false;
    }

    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World p_149707_1_, int p_149707_2_, int p_149707_3_, int p_149707_4_, int p_149707_5_)
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return -1;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (!p_149727_1_.isClient && p_149727_1_.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_) == null)
        {
            p_149727_1_.setBlockToAir(p_149727_2_, p_149727_3_, p_149727_4_);
            return true;
        }
        else
        {
            return false;
        }
    }

    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return null;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_)
    {
        if (!p_149690_1_.isClient)
        {
            TileEntityPiston var8 = this.func_149963_e(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_);

            if (var8 != null)
            {
                var8.func_145861_a().dropBlockAsItem(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, var8.getBlockMetadata(), 0);
            }
        }
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (!p_149695_1_.isClient)
        {
            p_149695_1_.getTileEntity(p_149695_2_, p_149695_3_, p_149695_4_);
        }
    }

    public static TileEntity func_149962_a(Block p_149962_0_, int p_149962_1_, int p_149962_2_, boolean p_149962_3_, boolean p_149962_4_)
    {
        return new TileEntityPiston(p_149962_0_, p_149962_1_, p_149962_2_, p_149962_3_, p_149962_4_);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        TileEntityPiston var5 = this.func_149963_e(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);

        if (var5 == null)
        {
            return null;
        }
        else
        {
            float var6 = var5.func_145860_a(0.0F);

            if (var5.func_145868_b())
            {
                var6 = 1.0F - var6;
            }

            return this.func_149964_a(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_, var5.func_145861_a(), var6, var5.func_145864_c());
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        TileEntityPiston var5 = this.func_149963_e(p_149719_1_, p_149719_2_, p_149719_3_, p_149719_4_);

        if (var5 != null)
        {
            Block var6 = var5.func_145861_a();

            if (var6 == this || var6.getMaterial() == Material.air)
            {
                return;
            }

            var6.setBlockBoundsBasedOnState(p_149719_1_, p_149719_2_, p_149719_3_, p_149719_4_);
            float var7 = var5.func_145860_a(0.0F);

            if (var5.func_145868_b())
            {
                var7 = 1.0F - var7;
            }

            int var8 = var5.func_145864_c();
            this.field_149759_B = var6.getBlockBoundsMinX() - (double)((float)Facing.offsetsXForSide[var8] * var7);
            this.field_149760_C = var6.getBlockBoundsMinY() - (double)((float)Facing.offsetsYForSide[var8] * var7);
            this.field_149754_D = var6.getBlockBoundsMinZ() - (double)((float)Facing.offsetsZForSide[var8] * var7);
            this.field_149755_E = var6.getBlockBoundsMaxX() - (double)((float)Facing.offsetsXForSide[var8] * var7);
            this.field_149756_F = var6.getBlockBoundsMaxY() - (double)((float)Facing.offsetsYForSide[var8] * var7);
            this.field_149757_G = var6.getBlockBoundsMaxZ() - (double)((float)Facing.offsetsZForSide[var8] * var7);
        }
    }

    public AxisAlignedBB func_149964_a(World p_149964_1_, int p_149964_2_, int p_149964_3_, int p_149964_4_, Block p_149964_5_, float p_149964_6_, int p_149964_7_)
    {
        if (p_149964_5_ != this && p_149964_5_.getMaterial() != Material.air)
        {
            AxisAlignedBB var8 = p_149964_5_.getCollisionBoundingBoxFromPool(p_149964_1_, p_149964_2_, p_149964_3_, p_149964_4_);

            if (var8 == null)
            {
                return null;
            }
            else
            {
                if (Facing.offsetsXForSide[p_149964_7_] < 0)
                {
                    var8.minX -= (double)((float)Facing.offsetsXForSide[p_149964_7_] * p_149964_6_);
                }
                else
                {
                    var8.maxX -= (double)((float)Facing.offsetsXForSide[p_149964_7_] * p_149964_6_);
                }

                if (Facing.offsetsYForSide[p_149964_7_] < 0)
                {
                    var8.minY -= (double)((float)Facing.offsetsYForSide[p_149964_7_] * p_149964_6_);
                }
                else
                {
                    var8.maxY -= (double)((float)Facing.offsetsYForSide[p_149964_7_] * p_149964_6_);
                }

                if (Facing.offsetsZForSide[p_149964_7_] < 0)
                {
                    var8.minZ -= (double)((float)Facing.offsetsZForSide[p_149964_7_] * p_149964_6_);
                }
                else
                {
                    var8.maxZ -= (double)((float)Facing.offsetsZForSide[p_149964_7_] * p_149964_6_);
                }

                return var8;
            }
        }
        else
        {
            return null;
        }
    }

    private TileEntityPiston func_149963_e(IBlockAccess p_149963_1_, int p_149963_2_, int p_149963_3_, int p_149963_4_)
    {
        TileEntity var5 = p_149963_1_.getTileEntity(p_149963_2_, p_149963_3_, p_149963_4_);
        return var5 instanceof TileEntityPiston ? (TileEntityPiston)var5 : null;
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return Item.getItemById(0);
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("piston_top_normal");
    }
}
