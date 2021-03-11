package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRedstoneDiode extends BlockDirectional
{
    protected final boolean field_149914_a;


    protected BlockRedstoneDiode(boolean p_i45400_1_)
    {
        super(Material.circuits);
        this.field_149914_a = p_i45400_1_;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return !World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_) ? false : super.canPlaceBlockAt(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_);
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World p_149718_1_, int p_149718_2_, int p_149718_3_, int p_149718_4_)
    {
        return !World.doesBlockHaveSolidTopSurface(p_149718_1_, p_149718_2_, p_149718_3_ - 1, p_149718_4_) ? false : super.canBlockStay(p_149718_1_, p_149718_2_, p_149718_3_, p_149718_4_);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        int var6 = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);

        if (!this.func_149910_g(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, var6))
        {
            boolean var7 = this.func_149900_a(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, var6);

            if (this.field_149914_a && !var7)
            {
                p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_, this.func_149898_i(), var6, 2);
            }
            else if (!this.field_149914_a)
            {
                p_149674_1_.setBlock(p_149674_2_, p_149674_3_, p_149674_4_, this.func_149906_e(), var6, 2);

                if (!var7)
                {
                    p_149674_1_.func_147454_a(p_149674_2_, p_149674_3_, p_149674_4_, this.func_149906_e(), this.func_149899_k(var6), -1);
                }
            }
        }
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 0 ? (this.field_149914_a ? Blocks.redstone_torch.getBlockTextureFromSide(p_149691_1_) : Blocks.unlit_redstone_torch.getBlockTextureFromSide(p_149691_1_)) : (p_149691_1_ == 1 ? this.blockIcon : Blocks.double_stone_slab.getBlockTextureFromSide(1));
    }

    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
    {
        return p_149646_5_ != 0 && p_149646_5_ != 1;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 36;
    }

    protected boolean func_149905_c(int p_149905_1_)
    {
        return this.field_149914_a;
    }

    public int isProvidingStrongPower(IBlockAccess p_149748_1_, int p_149748_2_, int p_149748_3_, int p_149748_4_, int p_149748_5_)
    {
        return this.isProvidingWeakPower(p_149748_1_, p_149748_2_, p_149748_3_, p_149748_4_, p_149748_5_);
    }

    public int isProvidingWeakPower(IBlockAccess p_149709_1_, int p_149709_2_, int p_149709_3_, int p_149709_4_, int p_149709_5_)
    {
        int var6 = p_149709_1_.getBlockMetadata(p_149709_2_, p_149709_3_, p_149709_4_);

        if (!this.func_149905_c(var6))
        {
            return 0;
        }
        else
        {
            int var7 = func_149895_l(var6);
            return var7 == 0 && p_149709_5_ == 3 ? this.func_149904_f(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, var6) : (var7 == 1 && p_149709_5_ == 4 ? this.func_149904_f(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, var6) : (var7 == 2 && p_149709_5_ == 2 ? this.func_149904_f(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, var6) : (var7 == 3 && p_149709_5_ == 5 ? this.func_149904_f(p_149709_1_, p_149709_2_, p_149709_3_, p_149709_4_, var6) : 0)));
        }
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (!this.canBlockStay(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_))
        {
            this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
            p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
            p_149695_1_.notifyBlocksOfNeighborChange(p_149695_2_ + 1, p_149695_3_, p_149695_4_, this);
            p_149695_1_.notifyBlocksOfNeighborChange(p_149695_2_ - 1, p_149695_3_, p_149695_4_, this);
            p_149695_1_.notifyBlocksOfNeighborChange(p_149695_2_, p_149695_3_, p_149695_4_ + 1, this);
            p_149695_1_.notifyBlocksOfNeighborChange(p_149695_2_, p_149695_3_, p_149695_4_ - 1, this);
            p_149695_1_.notifyBlocksOfNeighborChange(p_149695_2_, p_149695_3_ - 1, p_149695_4_, this);
            p_149695_1_.notifyBlocksOfNeighborChange(p_149695_2_, p_149695_3_ + 1, p_149695_4_, this);
        }
        else
        {
            this.func_149897_b(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
        }
    }

    protected void func_149897_b(World p_149897_1_, int p_149897_2_, int p_149897_3_, int p_149897_4_, Block p_149897_5_)
    {
        int var6 = p_149897_1_.getBlockMetadata(p_149897_2_, p_149897_3_, p_149897_4_);

        if (!this.func_149910_g(p_149897_1_, p_149897_2_, p_149897_3_, p_149897_4_, var6))
        {
            boolean var7 = this.func_149900_a(p_149897_1_, p_149897_2_, p_149897_3_, p_149897_4_, var6);

            if ((this.field_149914_a && !var7 || !this.field_149914_a && var7) && !p_149897_1_.func_147477_a(p_149897_2_, p_149897_3_, p_149897_4_, this))
            {
                byte var8 = -1;

                if (this.func_149912_i(p_149897_1_, p_149897_2_, p_149897_3_, p_149897_4_, var6))
                {
                    var8 = -3;
                }
                else if (this.field_149914_a)
                {
                    var8 = -2;
                }

                p_149897_1_.func_147454_a(p_149897_2_, p_149897_3_, p_149897_4_, this, this.func_149901_b(var6), var8);
            }
        }
    }

    public boolean func_149910_g(IBlockAccess p_149910_1_, int p_149910_2_, int p_149910_3_, int p_149910_4_, int p_149910_5_)
    {
        return false;
    }

    protected boolean func_149900_a(World p_149900_1_, int p_149900_2_, int p_149900_3_, int p_149900_4_, int p_149900_5_)
    {
        return this.func_149903_h(p_149900_1_, p_149900_2_, p_149900_3_, p_149900_4_, p_149900_5_) > 0;
    }

    protected int func_149903_h(World p_149903_1_, int p_149903_2_, int p_149903_3_, int p_149903_4_, int p_149903_5_)
    {
        int var6 = func_149895_l(p_149903_5_);
        int var7 = p_149903_2_ + Direction.offsetX[var6];
        int var8 = p_149903_4_ + Direction.offsetZ[var6];
        int var9 = p_149903_1_.getIndirectPowerLevelTo(var7, p_149903_3_, var8, Direction.directionToFacing[var6]);
        return var9 >= 15 ? var9 : Math.max(var9, p_149903_1_.getBlock(var7, p_149903_3_, var8) == Blocks.redstone_wire ? p_149903_1_.getBlockMetadata(var7, p_149903_3_, var8) : 0);
    }

    protected int func_149902_h(IBlockAccess p_149902_1_, int p_149902_2_, int p_149902_3_, int p_149902_4_, int p_149902_5_)
    {
        int var6 = func_149895_l(p_149902_5_);

        switch (var6)
        {
            case 0:
            case 2:
                return Math.max(this.func_149913_i(p_149902_1_, p_149902_2_ - 1, p_149902_3_, p_149902_4_, 4), this.func_149913_i(p_149902_1_, p_149902_2_ + 1, p_149902_3_, p_149902_4_, 5));

            case 1:
            case 3:
                return Math.max(this.func_149913_i(p_149902_1_, p_149902_2_, p_149902_3_, p_149902_4_ + 1, 3), this.func_149913_i(p_149902_1_, p_149902_2_, p_149902_3_, p_149902_4_ - 1, 2));

            default:
                return 0;
        }
    }

    protected int func_149913_i(IBlockAccess p_149913_1_, int p_149913_2_, int p_149913_3_, int p_149913_4_, int p_149913_5_)
    {
        Block var6 = p_149913_1_.getBlock(p_149913_2_, p_149913_3_, p_149913_4_);
        return this.func_149908_a(var6) ? (var6 == Blocks.redstone_wire ? p_149913_1_.getBlockMetadata(p_149913_2_, p_149913_3_, p_149913_4_) : p_149913_1_.isBlockProvidingPowerTo(p_149913_2_, p_149913_3_, p_149913_4_, p_149913_5_)) : 0;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int var7 = ((MathHelper.floor_double((double)(p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, var7, 3);
        boolean var8 = this.func_149900_a(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, var7);

        if (var8)
        {
            p_149689_1_.scheduleBlockUpdate(p_149689_2_, p_149689_3_, p_149689_4_, this, 1);
        }
    }

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        this.func_149911_e(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }

    protected void func_149911_e(World p_149911_1_, int p_149911_2_, int p_149911_3_, int p_149911_4_)
    {
        int var5 = func_149895_l(p_149911_1_.getBlockMetadata(p_149911_2_, p_149911_3_, p_149911_4_));

        if (var5 == 1)
        {
            p_149911_1_.func_147460_e(p_149911_2_ + 1, p_149911_3_, p_149911_4_, this);
            p_149911_1_.func_147441_b(p_149911_2_ + 1, p_149911_3_, p_149911_4_, this, 4);
        }

        if (var5 == 3)
        {
            p_149911_1_.func_147460_e(p_149911_2_ - 1, p_149911_3_, p_149911_4_, this);
            p_149911_1_.func_147441_b(p_149911_2_ - 1, p_149911_3_, p_149911_4_, this, 5);
        }

        if (var5 == 2)
        {
            p_149911_1_.func_147460_e(p_149911_2_, p_149911_3_, p_149911_4_ + 1, this);
            p_149911_1_.func_147441_b(p_149911_2_, p_149911_3_, p_149911_4_ + 1, this, 2);
        }

        if (var5 == 0)
        {
            p_149911_1_.func_147460_e(p_149911_2_, p_149911_3_, p_149911_4_ - 1, this);
            p_149911_1_.func_147441_b(p_149911_2_, p_149911_3_, p_149911_4_ - 1, this, 3);
        }
    }

    public void onBlockDestroyedByPlayer(World p_149664_1_, int p_149664_2_, int p_149664_3_, int p_149664_4_, int p_149664_5_)
    {
        if (this.field_149914_a)
        {
            p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_ + 1, p_149664_3_, p_149664_4_, this);
            p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_ - 1, p_149664_3_, p_149664_4_, this);
            p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_, p_149664_3_, p_149664_4_ + 1, this);
            p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_, p_149664_3_, p_149664_4_ - 1, this);
            p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_, p_149664_3_ - 1, p_149664_4_, this);
            p_149664_1_.notifyBlocksOfNeighborChange(p_149664_2_, p_149664_3_ + 1, p_149664_4_, this);
        }

        super.onBlockDestroyedByPlayer(p_149664_1_, p_149664_2_, p_149664_3_, p_149664_4_, p_149664_5_);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    protected boolean func_149908_a(Block p_149908_1_)
    {
        return p_149908_1_.canProvidePower();
    }

    protected int func_149904_f(IBlockAccess p_149904_1_, int p_149904_2_, int p_149904_3_, int p_149904_4_, int p_149904_5_)
    {
        return 15;
    }

    public static boolean func_149909_d(Block p_149909_0_)
    {
        return Blocks.unpowered_repeater.func_149907_e(p_149909_0_) || Blocks.unpowered_comparator.func_149907_e(p_149909_0_);
    }

    public boolean func_149907_e(Block p_149907_1_)
    {
        return p_149907_1_ == this.func_149906_e() || p_149907_1_ == this.func_149898_i();
    }

    public boolean func_149912_i(World p_149912_1_, int p_149912_2_, int p_149912_3_, int p_149912_4_, int p_149912_5_)
    {
        int var6 = func_149895_l(p_149912_5_);

        if (func_149909_d(p_149912_1_.getBlock(p_149912_2_ - Direction.offsetX[var6], p_149912_3_, p_149912_4_ - Direction.offsetZ[var6])))
        {
            int var7 = p_149912_1_.getBlockMetadata(p_149912_2_ - Direction.offsetX[var6], p_149912_3_, p_149912_4_ - Direction.offsetZ[var6]);
            int var8 = func_149895_l(var7);
            return var8 != var6;
        }
        else
        {
            return false;
        }
    }

    protected int func_149899_k(int p_149899_1_)
    {
        return this.func_149901_b(p_149899_1_);
    }

    protected abstract int func_149901_b(int p_149901_1_);

    protected abstract BlockRedstoneDiode func_149906_e();

    protected abstract BlockRedstoneDiode func_149898_i();

    public boolean func_149667_c(Block p_149667_1_)
    {
        return this.func_149907_e(p_149667_1_);
    }
}
