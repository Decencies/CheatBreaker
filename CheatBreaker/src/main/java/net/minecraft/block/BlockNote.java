package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.world.World;

public class BlockNote extends BlockContainer
{


    public BlockNote()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        boolean var6 = p_149695_1_.isBlockIndirectlyGettingPowered(p_149695_2_, p_149695_3_, p_149695_4_);
        TileEntityNote var7 = (TileEntityNote)p_149695_1_.getTileEntity(p_149695_2_, p_149695_3_, p_149695_4_);

        if (var7 != null && var7.field_145880_i != var6)
        {
            if (var6)
            {
                var7.func_145878_a(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
            }

            var7.field_145880_i = var6;
        }
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (p_149727_1_.isClient)
        {
            return true;
        }
        else
        {
            TileEntityNote var10 = (TileEntityNote)p_149727_1_.getTileEntity(p_149727_2_, p_149727_3_, p_149727_4_);

            if (var10 != null)
            {
                var10.func_145877_a();
                var10.func_145878_a(p_149727_1_, p_149727_2_, p_149727_3_, p_149727_4_);
            }

            return true;
        }
    }

    /**
     * Called when a player hits the block. Args: world, x, y, z, player
     */
    public void onBlockClicked(World p_149699_1_, int p_149699_2_, int p_149699_3_, int p_149699_4_, EntityPlayer p_149699_5_)
    {
        if (!p_149699_1_.isClient)
        {
            TileEntityNote var6 = (TileEntityNote)p_149699_1_.getTileEntity(p_149699_2_, p_149699_3_, p_149699_4_);

            if (var6 != null)
            {
                var6.func_145878_a(p_149699_1_, p_149699_2_, p_149699_3_, p_149699_4_);
            }
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityNote();
    }

    public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_)
    {
        float var7 = (float)Math.pow(2.0D, (double)(p_149696_6_ - 12) / 12.0D);
        String var8 = "harp";

        if (p_149696_5_ == 1)
        {
            var8 = "bd";
        }

        if (p_149696_5_ == 2)
        {
            var8 = "snare";
        }

        if (p_149696_5_ == 3)
        {
            var8 = "hat";
        }

        if (p_149696_5_ == 4)
        {
            var8 = "bassattack";
        }

        p_149696_1_.playSoundEffect((double)p_149696_2_ + 0.5D, (double)p_149696_3_ + 0.5D, (double)p_149696_4_ + 0.5D, "note." + var8, 3.0F, var7);
        p_149696_1_.spawnParticle("note", (double)p_149696_2_ + 0.5D, (double)p_149696_3_ + 1.2D, (double)p_149696_4_ + 0.5D, (double)p_149696_6_ / 24.0D, 0.0D, 0.0D);
        return true;
    }
}
