package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDaylightDetector;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDaylightDetector extends BlockContainer
{
    private IIcon[] field_149958_a = new IIcon[2];


    public BlockDaylightDetector()
    {
        super(Material.wood);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
    }

    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
        return p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_) {}

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {}

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {}

    public void func_149957_e(World p_149957_1_, int p_149957_2_, int p_149957_3_, int p_149957_4_)
    {
        if (!p_149957_1_.provider.hasNoSky)
        {
            int var5 = p_149957_1_.getBlockMetadata(p_149957_2_, p_149957_3_, p_149957_4_);
            int var6 = p_149957_1_.getSavedLightValue(EnumSkyBlock.Sky, p_149957_2_, p_149957_3_, p_149957_4_) - p_149957_1_.skylightSubtracted;
            float var7 = p_149957_1_.getCelestialAngleRadians(1.0F);

            if (var7 < (float)Math.PI)
            {
                var7 += (0.0F - var7) * 0.2F;
            }
            else
            {
                var7 += (((float)Math.PI * 2F) - var7) * 0.2F;
            }

            var6 = Math.round((float)var6 * MathHelper.cos(var7));

            if (var6 < 0)
            {
                var6 = 0;
            }

            if (var6 > 15)
            {
                var6 = 15;
            }

            if (var5 != var6)
            {
                p_149957_1_.setBlockMetadataWithNotify(p_149957_2_, p_149957_3_, p_149957_4_, var6, 3);
            }
        }
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityDaylightDetector();
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? this.field_149958_a[0] : this.field_149958_a[1];
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_149958_a[0] = p_149651_1_.registerIcon(this.getTextureName() + "_top");
        this.field_149958_a[1] = p_149651_1_.registerIcon(this.getTextureName() + "_side");
    }
}
