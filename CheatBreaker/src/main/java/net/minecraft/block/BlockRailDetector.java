package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailDetector extends BlockRailBase
{
    private IIcon[] field_150055_b;


    public BlockRailDetector()
    {
        super(true);
        this.setTickRandomly(true);
    }

    public int func_149738_a(World p_149738_1_)
    {
        return 20;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
    {
        if (!p_149670_1_.isClient)
        {
            int var6 = p_149670_1_.getBlockMetadata(p_149670_2_, p_149670_3_, p_149670_4_);

            if ((var6 & 8) == 0)
            {
                this.func_150054_a(p_149670_1_, p_149670_2_, p_149670_3_, p_149670_4_, var6);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        if (!p_149674_1_.isClient)
        {
            int var6 = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);

            if ((var6 & 8) != 0)
            {
                this.func_150054_a(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, var6);
            }
        }
    }

    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
        return (p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_) & 8) != 0 ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_)
    {
        return (p_149748_1_.getBlockMetadata(p_149748_2_, p_149748_3_, p_149748_4_) & 8) == 0 ? 0 : (p_149748_5_ == 1 ? 15 : 0);
    }

    private void func_150054_a(World p_150054_1_, int p_150054_2_, int p_150054_3_, int p_150054_4_, int p_150054_5_)
    {
        boolean var6 = (p_150054_5_ & 8) != 0;
        boolean var7 = false;
        float var8 = 0.125F;
        List var9 = p_150054_1_.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getBoundingBox((double)((float)p_150054_2_ + var8), (double)p_150054_3_, (double)((float)p_150054_4_ + var8), (double)((float)(p_150054_2_ + 1) - var8), (double)((float)(p_150054_3_ + 1) - var8), (double)((float)(p_150054_4_ + 1) - var8)));

        if (!var9.isEmpty())
        {
            var7 = true;
        }

        if (var7 && !var6)
        {
            p_150054_1_.setBlockMetadataWithNotify(p_150054_2_, p_150054_3_, p_150054_4_, p_150054_5_ | 8, 3);
            p_150054_1_.notifyBlocksOfNeighborChange(p_150054_2_, p_150054_3_, p_150054_4_, this);
            p_150054_1_.notifyBlocksOfNeighborChange(p_150054_2_, p_150054_3_ - 1, p_150054_4_, this);
            p_150054_1_.markBlockRangeForRenderUpdate(p_150054_2_, p_150054_3_, p_150054_4_, p_150054_2_, p_150054_3_, p_150054_4_);
        }

        if (!var7 && var6)
        {
            p_150054_1_.setBlockMetadataWithNotify(p_150054_2_, p_150054_3_, p_150054_4_, p_150054_5_ & 7, 3);
            p_150054_1_.notifyBlocksOfNeighborChange(p_150054_2_, p_150054_3_, p_150054_4_, this);
            p_150054_1_.notifyBlocksOfNeighborChange(p_150054_2_, p_150054_3_ - 1, p_150054_4_, this);
            p_150054_1_.markBlockRangeForRenderUpdate(p_150054_2_, p_150054_3_, p_150054_4_, p_150054_2_, p_150054_3_, p_150054_4_);
        }

        if (var7)
        {
            p_150054_1_.scheduleBlockUpdate(p_150054_2_, p_150054_3_, p_150054_4_, this, this.func_149738_a(p_150054_1_));
        }

        p_150054_1_.func_147453_f(p_150054_2_, p_150054_3_, p_150054_4_, this);
    }

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
        this.func_150054_a(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_, p_149726_1_.getBlockMetadata(p_149726_2_, p_149726_3_, p_149726_4_));
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World p_149736_1_, int p_149736_2_, int p_149736_3_, int p_149736_4_, int p_149736_5_)
    {
        if ((p_149736_1_.getBlockMetadata(p_149736_2_, p_149736_3_, p_149736_4_) & 8) > 0)
        {
            float var6 = 0.125F;
            List var7 = p_149736_1_.getEntitiesWithinAABB(EntityMinecartCommandBlock.class, AxisAlignedBB.getBoundingBox((double)((float)p_149736_2_ + var6), (double)p_149736_3_, (double)((float)p_149736_4_ + var6), (double)((float)(p_149736_2_ + 1) - var6), (double)((float)(p_149736_3_ + 1) - var6), (double)((float)(p_149736_4_ + 1) - var6)));

            if (var7.size() > 0)
            {
                return ((EntityMinecartCommandBlock)var7.get(0)).func_145822_e().func_145760_g();
            }

            List var8 = p_149736_1_.selectEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getBoundingBox((double)((float)p_149736_2_ + var6), (double)p_149736_3_, (double)((float)p_149736_4_ + var6), (double)((float)(p_149736_2_ + 1) - var6), (double)((float)(p_149736_3_ + 1) - var6), (double)((float)(p_149736_4_ + 1) - var6)), IEntitySelector.selectInventories);

            if (var8.size() > 0)
            {
                return Container.calcRedstoneFromInventory((IInventory)var8.get(0));
            }
        }

        return 0;
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_150055_b = new IIcon[2];
        this.field_150055_b[0] = p_149651_1_.registerIcon(this.getTextureName());
        this.field_150055_b[1] = p_149651_1_.registerIcon(this.getTextureName() + "_powered");
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return (p_149691_2_ & 8) != 0 ? this.field_150055_b[1] : this.field_150055_b[0];
    }
}
