package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonBase extends Block
{
    private final boolean field_150082_a;
    private IIcon field_150081_b;
    private IIcon field_150083_M;
    private IIcon field_150084_N;


    public BlockPistonBase(boolean p_i45443_1_)
    {
        super(Material.piston);
        this.field_150082_a = p_i45443_1_;
        this.setStepSound(soundTypePiston);
        this.setHardness(0.5F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public IIcon func_150073_e()
    {
        return this.field_150084_N;
    }

    public void func_150070_b(float p_150070_1_, float p_150070_2_, float p_150070_3_, float p_150070_4_, float p_150070_5_, float p_150070_6_)
    {
        this.setBlockBounds(p_150070_1_, p_150070_2_, p_150070_3_, p_150070_4_, p_150070_5_, p_150070_6_);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        int var3 = func_150076_b(p_149691_2_);
        return var3 > 5 ? this.field_150084_N : (p_149691_1_ == var3 ? (!func_150075_c(p_149691_2_) && this.field_149759_B <= 0.0D && this.field_149760_C <= 0.0D && this.field_149754_D <= 0.0D && this.field_149755_E >= 1.0D && this.field_149756_F >= 1.0D && this.field_149757_G >= 1.0D ? this.field_150084_N : this.field_150081_b) : (p_149691_1_ == Facing.oppositeSide[var3] ? this.field_150083_M : this.blockIcon));
    }

    public static IIcon func_150074_e(String p_150074_0_)
    {
        return p_150074_0_ == "piston_side" ? Blocks.piston.blockIcon : (p_150074_0_ == "piston_top_normal" ? Blocks.piston.field_150084_N : (p_150074_0_ == "piston_top_sticky" ? Blocks.sticky_piston.field_150084_N : (p_150074_0_ == "piston_inner" ? Blocks.piston.field_150081_b : null)));
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon("piston_side");
        this.field_150084_N = p_149651_1_.registerIcon(this.field_150082_a ? "piston_top_sticky" : "piston_top_normal");
        this.field_150081_b = p_149651_1_.registerIcon("piston_inner");
        this.field_150083_M = p_149651_1_.registerIcon("piston_bottom");
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 16;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        return false;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
        int var7 = func_150071_a(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_, p_149689_5_);
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, var7, 2);

        if (!p_149689_1_.isClient)
        {
            this.func_150078_e(p_149689_1_, p_149689_2_, p_149689_3_, p_149689_4_);
        }
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (!p_149695_1_.isClient)
        {
            this.func_150078_e(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_);
        }
    }

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        if (!p_149726_1_.isClient && p_149726_1_.getTileEntity(p_149726_2_, p_149726_3_, p_149726_4_) == null)
        {
            this.func_150078_e(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
        }
    }

    private void func_150078_e(World p_150078_1_, int p_150078_2_, int p_150078_3_, int p_150078_4_)
    {
        int var5 = p_150078_1_.getBlockMetadata(p_150078_2_, p_150078_3_, p_150078_4_);
        int var6 = func_150076_b(var5);

        if (var6 != 7)
        {
            boolean var7 = this.func_150072_a(p_150078_1_, p_150078_2_, p_150078_3_, p_150078_4_, var6);

            if (var7 && !func_150075_c(var5))
            {
                if (func_150077_h(p_150078_1_, p_150078_2_, p_150078_3_, p_150078_4_, var6))
                {
                    p_150078_1_.func_147452_c(p_150078_2_, p_150078_3_, p_150078_4_, this, 0, var6);
                }
            }
            else if (!var7 && func_150075_c(var5))
            {
                p_150078_1_.setBlockMetadataWithNotify(p_150078_2_, p_150078_3_, p_150078_4_, var6, 2);
                p_150078_1_.func_147452_c(p_150078_2_, p_150078_3_, p_150078_4_, this, 1, var6);
            }
        }
    }

    private boolean func_150072_a(World p_150072_1_, int p_150072_2_, int p_150072_3_, int p_150072_4_, int p_150072_5_)
    {
        return p_150072_5_ != 0 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ - 1, p_150072_4_, 0) ? true : (p_150072_5_ != 1 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_, 1) ? true : (p_150072_5_ != 2 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_ - 1, 2) ? true : (p_150072_5_ != 3 && p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_ + 1, 3) ? true : (p_150072_5_ != 5 && p_150072_1_.getIndirectPowerOutput(p_150072_2_ + 1, p_150072_3_, p_150072_4_, 5) ? true : (p_150072_5_ != 4 && p_150072_1_.getIndirectPowerOutput(p_150072_2_ - 1, p_150072_3_, p_150072_4_, 4) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_, p_150072_4_, 0) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 2, p_150072_4_, 1) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_ - 1, 2) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_, p_150072_3_ + 1, p_150072_4_ + 1, 3) ? true : (p_150072_1_.getIndirectPowerOutput(p_150072_2_ - 1, p_150072_3_ + 1, p_150072_4_, 4) ? true : p_150072_1_.getIndirectPowerOutput(p_150072_2_ + 1, p_150072_3_ + 1, p_150072_4_, 5)))))))))));
    }

    public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_, int p_149696_5_, int p_149696_6_)
    {
        if (!p_149696_1_.isClient)
        {
            boolean var7 = this.func_150072_a(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_);

            if (var7 && p_149696_5_ == 1)
            {
                p_149696_1_.setBlockMetadataWithNotify(p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_ | 8, 2);
                return false;
            }

            if (!var7 && p_149696_5_ == 0)
            {
                return false;
            }
        }

        if (p_149696_5_ == 0)
        {
            if (!this.func_150079_i(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_))
            {
                return false;
            }

            p_149696_1_.setBlockMetadataWithNotify(p_149696_2_, p_149696_3_, p_149696_4_, p_149696_6_ | 8, 2);
            p_149696_1_.playSoundEffect((double)p_149696_2_ + 0.5D, (double)p_149696_3_ + 0.5D, (double)p_149696_4_ + 0.5D, "tile.piston.out", 0.5F, p_149696_1_.rand.nextFloat() * 0.25F + 0.6F);
        }
        else if (p_149696_5_ == 1)
        {
            TileEntity var16 = p_149696_1_.getTileEntity(p_149696_2_ + Facing.offsetsXForSide[p_149696_6_], p_149696_3_ + Facing.offsetsYForSide[p_149696_6_], p_149696_4_ + Facing.offsetsZForSide[p_149696_6_]);

            if (var16 instanceof TileEntityPiston)
            {
                ((TileEntityPiston)var16).func_145866_f();
            }

            p_149696_1_.setBlock(p_149696_2_, p_149696_3_, p_149696_4_, Blocks.piston_extension, p_149696_6_, 3);
            p_149696_1_.setTileEntity(p_149696_2_, p_149696_3_, p_149696_4_, BlockPistonMoving.func_149962_a(this, p_149696_6_, p_149696_6_, false, true));

            if (this.field_150082_a)
            {
                int var8 = p_149696_2_ + Facing.offsetsXForSide[p_149696_6_] * 2;
                int var9 = p_149696_3_ + Facing.offsetsYForSide[p_149696_6_] * 2;
                int var10 = p_149696_4_ + Facing.offsetsZForSide[p_149696_6_] * 2;
                Block var11 = p_149696_1_.getBlock(var8, var9, var10);
                int var12 = p_149696_1_.getBlockMetadata(var8, var9, var10);
                boolean var13 = false;

                if (var11 == Blocks.piston_extension)
                {
                    TileEntity var14 = p_149696_1_.getTileEntity(var8, var9, var10);

                    if (var14 instanceof TileEntityPiston)
                    {
                        TileEntityPiston var15 = (TileEntityPiston)var14;

                        if (var15.func_145864_c() == p_149696_6_ && var15.func_145868_b())
                        {
                            var15.func_145866_f();
                            var11 = var15.func_145861_a();
                            var12 = var15.getBlockMetadata();
                            var13 = true;
                        }
                    }
                }

                if (!var13 && var11.getMaterial() != Material.air && func_150080_a(var11, p_149696_1_, var8, var9, var10, false) && (var11.getMobilityFlag() == 0 || var11 == Blocks.piston || var11 == Blocks.sticky_piston))
                {
                    p_149696_2_ += Facing.offsetsXForSide[p_149696_6_];
                    p_149696_3_ += Facing.offsetsYForSide[p_149696_6_];
                    p_149696_4_ += Facing.offsetsZForSide[p_149696_6_];
                    p_149696_1_.setBlock(p_149696_2_, p_149696_3_, p_149696_4_, Blocks.piston_extension, var12, 3);
                    p_149696_1_.setTileEntity(p_149696_2_, p_149696_3_, p_149696_4_, BlockPistonMoving.func_149962_a(var11, var12, p_149696_6_, false, false));
                    p_149696_1_.setBlockToAir(var8, var9, var10);
                }
                else if (!var13)
                {
                    p_149696_1_.setBlockToAir(p_149696_2_ + Facing.offsetsXForSide[p_149696_6_], p_149696_3_ + Facing.offsetsYForSide[p_149696_6_], p_149696_4_ + Facing.offsetsZForSide[p_149696_6_]);
                }
            }
            else
            {
                p_149696_1_.setBlockToAir(p_149696_2_ + Facing.offsetsXForSide[p_149696_6_], p_149696_3_ + Facing.offsetsYForSide[p_149696_6_], p_149696_4_ + Facing.offsetsZForSide[p_149696_6_]);
            }

            p_149696_1_.playSoundEffect((double)p_149696_2_ + 0.5D, (double)p_149696_3_ + 0.5D, (double)p_149696_4_ + 0.5D, "tile.piston.in", 0.5F, p_149696_1_.rand.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        int var5 = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);

        if (func_150075_c(var5))
        {
            float var6 = 0.25F;

            switch (func_150076_b(var5))
            {
                case 0:
                    this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 1:
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
                    break;

                case 2:
                    this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
                    break;

                case 3:
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
                    break;

                case 4:
                    this.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 5:
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            }
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_)
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        this.setBlockBoundsBasedOnState(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
        return super.getCollisionBoundingBoxFromPool(p_149668_1_, p_149668_2_, p_149668_3_, p_149668_4_);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public static int func_150076_b(int p_150076_0_)
    {
        return p_150076_0_ & 7;
    }

    public static boolean func_150075_c(int p_150075_0_)
    {
        return (p_150075_0_ & 8) != 0;
    }

    public static int func_150071_a(World p_150071_0_, int p_150071_1_, int p_150071_2_, int p_150071_3_, EntityLivingBase p_150071_4_)
    {
        if (MathHelper.abs((float)p_150071_4_.posX - (float)p_150071_1_) < 2.0F && MathHelper.abs((float)p_150071_4_.posZ - (float)p_150071_3_) < 2.0F)
        {
            double var5 = p_150071_4_.posY + 1.82D - (double)p_150071_4_.yOffset;

            if (var5 - (double)p_150071_2_ > 2.0D)
            {
                return 1;
            }

            if ((double)p_150071_2_ - var5 > 0.0D)
            {
                return 0;
            }
        }

        int var7 = MathHelper.floor_double((double)(p_150071_4_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return var7 == 0 ? 2 : (var7 == 1 ? 5 : (var7 == 2 ? 3 : (var7 == 3 ? 4 : 0)));
    }

    private static boolean func_150080_a(Block p_150080_0_, World p_150080_1_, int p_150080_2_, int p_150080_3_, int p_150080_4_, boolean p_150080_5_)
    {
        if (p_150080_0_ == Blocks.obsidian)
        {
            return false;
        }
        else
        {
            if (p_150080_0_ != Blocks.piston && p_150080_0_ != Blocks.sticky_piston)
            {
                if (p_150080_0_.getBlockHardness(p_150080_1_, p_150080_2_, p_150080_3_, p_150080_4_) == -1.0F)
                {
                    return false;
                }

                if (p_150080_0_.getMobilityFlag() == 2)
                {
                    return false;
                }

                if (p_150080_0_.getMobilityFlag() == 1)
                {
                    if (!p_150080_5_)
                    {
                        return false;
                    }

                    return true;
                }
            }
            else if (func_150075_c(p_150080_1_.getBlockMetadata(p_150080_2_, p_150080_3_, p_150080_4_)))
            {
                return false;
            }

            return !(p_150080_0_ instanceof ITileEntityProvider);
        }
    }

    private static boolean func_150077_h(World p_150077_0_, int p_150077_1_, int p_150077_2_, int p_150077_3_, int p_150077_4_)
    {
        int var5 = p_150077_1_ + Facing.offsetsXForSide[p_150077_4_];
        int var6 = p_150077_2_ + Facing.offsetsYForSide[p_150077_4_];
        int var7 = p_150077_3_ + Facing.offsetsZForSide[p_150077_4_];
        int var8 = 0;

        while (true)
        {
            if (var8 < 13)
            {
                if (var6 <= 0 || var6 >= 255)
                {
                    return false;
                }

                Block var9 = p_150077_0_.getBlock(var5, var6, var7);

                if (var9.getMaterial() != Material.air)
                {
                    if (!func_150080_a(var9, p_150077_0_, var5, var6, var7, true))
                    {
                        return false;
                    }

                    if (var9.getMobilityFlag() != 1)
                    {
                        if (var8 == 12)
                        {
                            return false;
                        }

                        var5 += Facing.offsetsXForSide[p_150077_4_];
                        var6 += Facing.offsetsYForSide[p_150077_4_];
                        var7 += Facing.offsetsZForSide[p_150077_4_];
                        ++var8;
                        continue;
                    }
                }
            }

            return true;
        }
    }

    private boolean func_150079_i(World p_150079_1_, int p_150079_2_, int p_150079_3_, int p_150079_4_, int p_150079_5_)
    {
        int var6 = p_150079_2_ + Facing.offsetsXForSide[p_150079_5_];
        int var7 = p_150079_3_ + Facing.offsetsYForSide[p_150079_5_];
        int var8 = p_150079_4_ + Facing.offsetsZForSide[p_150079_5_];
        int var9 = 0;

        while (true)
        {
            if (var9 < 13)
            {
                if (var7 <= 0 || var7 >= 255)
                {
                    return false;
                }

                Block var10 = p_150079_1_.getBlock(var6, var7, var8);

                if (var10.getMaterial() != Material.air)
                {
                    if (!func_150080_a(var10, p_150079_1_, var6, var7, var8, true))
                    {
                        return false;
                    }

                    if (var10.getMobilityFlag() != 1)
                    {
                        if (var9 == 12)
                        {
                            return false;
                        }

                        var6 += Facing.offsetsXForSide[p_150079_5_];
                        var7 += Facing.offsetsYForSide[p_150079_5_];
                        var8 += Facing.offsetsZForSide[p_150079_5_];
                        ++var9;
                        continue;
                    }

                    var10.dropBlockAsItem(p_150079_1_, var6, var7, var8, p_150079_1_.getBlockMetadata(var6, var7, var8), 0);
                    p_150079_1_.setBlockToAir(var6, var7, var8);
                }
            }

            var9 = var6;
            int var19 = var7;
            int var11 = var8;
            int var12 = 0;
            Block[] var13;
            int var14;
            int var15;
            int var16;

            for (var13 = new Block[13]; var6 != p_150079_2_ || var7 != p_150079_3_ || var8 != p_150079_4_; var8 = var16)
            {
                var14 = var6 - Facing.offsetsXForSide[p_150079_5_];
                var15 = var7 - Facing.offsetsYForSide[p_150079_5_];
                var16 = var8 - Facing.offsetsZForSide[p_150079_5_];
                Block var17 = p_150079_1_.getBlock(var14, var15, var16);
                int var18 = p_150079_1_.getBlockMetadata(var14, var15, var16);

                if (var17 == this && var14 == p_150079_2_ && var15 == p_150079_3_ && var16 == p_150079_4_)
                {
                    p_150079_1_.setBlock(var6, var7, var8, Blocks.piston_extension, p_150079_5_ | (this.field_150082_a ? 8 : 0), 4);
                    p_150079_1_.setTileEntity(var6, var7, var8, BlockPistonMoving.func_149962_a(Blocks.piston_head, p_150079_5_ | (this.field_150082_a ? 8 : 0), p_150079_5_, true, false));
                }
                else
                {
                    p_150079_1_.setBlock(var6, var7, var8, Blocks.piston_extension, var18, 4);
                    p_150079_1_.setTileEntity(var6, var7, var8, BlockPistonMoving.func_149962_a(var17, var18, p_150079_5_, true, false));
                }

                var13[var12++] = var17;
                var6 = var14;
                var7 = var15;
            }

            var6 = var9;
            var7 = var19;
            var8 = var11;

            for (var12 = 0; var6 != p_150079_2_ || var7 != p_150079_3_ || var8 != p_150079_4_; var8 = var16)
            {
                var14 = var6 - Facing.offsetsXForSide[p_150079_5_];
                var15 = var7 - Facing.offsetsYForSide[p_150079_5_];
                var16 = var8 - Facing.offsetsZForSide[p_150079_5_];
                p_150079_1_.notifyBlocksOfNeighborChange(var14, var15, var16, var13[var12++]);
                var6 = var14;
                var7 = var15;
            }

            return true;
        }
    }
}
