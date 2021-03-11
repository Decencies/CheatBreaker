package net.minecraft.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block
{
    protected final boolean field_150053_a;


    public static final boolean func_150049_b_(World p_150049_0_, int p_150049_1_, int p_150049_2_, int p_150049_3_)
    {
        return func_150051_a(p_150049_0_.getBlock(p_150049_1_, p_150049_2_, p_150049_3_));
    }

    public static final boolean func_150051_a(Block p_150051_0_)
    {
        return p_150051_0_ == Blocks.rail || p_150051_0_ == Blocks.golden_rail || p_150051_0_ == Blocks.detector_rail || p_150051_0_ == Blocks.activator_rail;
    }

    protected BlockRailBase(boolean p_i45389_1_)
    {
        super(Material.circuits);
        this.field_150053_a = p_i45389_1_;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    public boolean func_150050_e()
    {
        return this.field_150053_a;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public MovingObjectPosition collisionRayTrace(World p_149731_1_, int p_149731_2_, int p_149731_3_, int p_149731_4_, Vec3 p_149731_5_, Vec3 p_149731_6_)
    {
        this.setBlockBoundsBasedOnState(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_);
        return super.collisionRayTrace(p_149731_1_, p_149731_2_, p_149731_3_, p_149731_4_, p_149731_5_, p_149731_6_);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        int var5 = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_);

        if (var5 >= 2 && var5 <= 5)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 9;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        return 1;
    }

    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_);
    }

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        if (!p_149726_1_.isClient)
        {
            this.func_150052_a(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_, true);

            if (this.field_150053_a)
            {
                this.onNeighborBlockChange(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_, this);
            }
        }
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (!p_149695_1_.isClient)
        {
            int var6 = p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_);
            int var7 = var6;

            if (this.field_150053_a)
            {
                var7 = var6 & 7;
            }

            boolean var8 = false;

            if (!World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_ - 1, p_149695_4_))
            {
                var8 = true;
            }

            if (var7 == 2 && !World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_ + 1, p_149695_3_, p_149695_4_))
            {
                var8 = true;
            }

            if (var7 == 3 && !World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_ - 1, p_149695_3_, p_149695_4_))
            {
                var8 = true;
            }

            if (var7 == 4 && !World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_ - 1))
            {
                var8 = true;
            }

            if (var7 == 5 && !World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_ + 1))
            {
                var8 = true;
            }

            if (var8)
            {
                this.dropBlockAsItem(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_), 0);
                p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
            }
            else
            {
                this.func_150048_a(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, var6, var7, p_149695_5_);
            }
        }
    }

    protected void func_150048_a(World p_150048_1_, int p_150048_2_, int p_150048_3_, int p_150048_4_, int p_150048_5_, int p_150048_6_, Block p_150048_7_) {}

    protected void func_150052_a(World p_150052_1_, int p_150052_2_, int p_150052_3_, int p_150052_4_, boolean p_150052_5_)
    {
        if (!p_150052_1_.isClient)
        {
            (new BlockRailBase.Rail(p_150052_1_, p_150052_2_, p_150052_3_, p_150052_4_)).func_150655_a(p_150052_1_.isBlockIndirectlyGettingPowered(p_150052_2_, p_150052_3_, p_150052_4_), p_150052_5_);
        }
    }

    public int getMobilityFlag()
    {
        return 0;
    }

    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        int var7 = p_149749_6_;

        if (this.field_150053_a)
        {
            var7 = p_149749_6_ & 7;
        }

        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);

        if (var7 == 2 || var7 == 3 || var7 == 4 || var7 == 5)
        {
            p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_ + 1, p_149749_4_, p_149749_5_);
        }

        if (this.field_150053_a)
        {
            p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
            p_149749_1_.notifyBlocksOfNeighborChange(p_149749_2_, p_149749_3_ - 1, p_149749_4_, p_149749_5_);
        }
    }

    public class Rail
    {
        private World field_150660_b;
        private int field_150661_c;
        private int field_150658_d;
        private int field_150659_e;
        private final boolean field_150656_f;
        private List field_150657_g = new ArrayList();


        public Rail(World p_i45388_2_, int p_i45388_3_, int p_i45388_4_, int p_i45388_5_)
        {
            this.field_150660_b = p_i45388_2_;
            this.field_150661_c = p_i45388_3_;
            this.field_150658_d = p_i45388_4_;
            this.field_150659_e = p_i45388_5_;
            Block var6 = p_i45388_2_.getBlock(p_i45388_3_, p_i45388_4_, p_i45388_5_);
            int var7 = p_i45388_2_.getBlockMetadata(p_i45388_3_, p_i45388_4_, p_i45388_5_);

            if (((BlockRailBase)var6).field_150053_a)
            {
                this.field_150656_f = true;
                var7 &= -9;
            }
            else
            {
                this.field_150656_f = false;
            }

            this.func_150648_a(var7);
        }

        private void func_150648_a(int p_150648_1_)
        {
            this.field_150657_g.clear();

            if (p_150648_1_ == 0)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1));
            }
            else if (p_150648_1_ == 1)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e));
            }
            else if (p_150648_1_ == 2)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c + 1, this.field_150658_d + 1, this.field_150659_e));
            }
            else if (p_150648_1_ == 3)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c - 1, this.field_150658_d + 1, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e));
            }
            else if (p_150648_1_ == 4)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d + 1, this.field_150659_e - 1));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1));
            }
            else if (p_150648_1_ == 5)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d + 1, this.field_150659_e + 1));
            }
            else if (p_150648_1_ == 6)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1));
            }
            else if (p_150648_1_ == 7)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1));
            }
            else if (p_150648_1_ == 8)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1));
            }
            else if (p_150648_1_ == 9)
            {
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e));
                this.field_150657_g.add(new ChunkPosition(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1));
            }
        }

        private void func_150651_b()
        {
            for (int var1 = 0; var1 < this.field_150657_g.size(); ++var1)
            {
                BlockRailBase.Rail var2 = this.func_150654_a((ChunkPosition)this.field_150657_g.get(var1));

                if (var2 != null && var2.func_150653_a(this))
                {
                    this.field_150657_g.set(var1, new ChunkPosition(var2.field_150661_c, var2.field_150658_d, var2.field_150659_e));
                }
                else
                {
                    this.field_150657_g.remove(var1--);
                }
            }
        }

        private boolean func_150646_a(int p_150646_1_, int p_150646_2_, int p_150646_3_)
        {
            return BlockRailBase.func_150049_b_(this.field_150660_b, p_150646_1_, p_150646_2_, p_150646_3_) ? true : (BlockRailBase.func_150049_b_(this.field_150660_b, p_150646_1_, p_150646_2_ + 1, p_150646_3_) ? true : BlockRailBase.func_150049_b_(this.field_150660_b, p_150646_1_, p_150646_2_ - 1, p_150646_3_));
        }

        private BlockRailBase.Rail func_150654_a(ChunkPosition p_150654_1_)
        {
            return BlockRailBase.func_150049_b_(this.field_150660_b, p_150654_1_.field_151329_a, p_150654_1_.field_151327_b, p_150654_1_.field_151328_c) ? BlockRailBase.this.new Rail(this.field_150660_b, p_150654_1_.field_151329_a, p_150654_1_.field_151327_b, p_150654_1_.field_151328_c) : (BlockRailBase.func_150049_b_(this.field_150660_b, p_150654_1_.field_151329_a, p_150654_1_.field_151327_b + 1, p_150654_1_.field_151328_c) ? BlockRailBase.this.new Rail(this.field_150660_b, p_150654_1_.field_151329_a, p_150654_1_.field_151327_b + 1, p_150654_1_.field_151328_c) : (BlockRailBase.func_150049_b_(this.field_150660_b, p_150654_1_.field_151329_a, p_150654_1_.field_151327_b - 1, p_150654_1_.field_151328_c) ? BlockRailBase.this.new Rail(this.field_150660_b, p_150654_1_.field_151329_a, p_150654_1_.field_151327_b - 1, p_150654_1_.field_151328_c) : null));
        }

        private boolean func_150653_a(BlockRailBase.Rail p_150653_1_)
        {
            for (int var2 = 0; var2 < this.field_150657_g.size(); ++var2)
            {
                ChunkPosition var3 = (ChunkPosition)this.field_150657_g.get(var2);

                if (var3.field_151329_a == p_150653_1_.field_150661_c && var3.field_151328_c == p_150653_1_.field_150659_e)
                {
                    return true;
                }
            }

            return false;
        }

        private boolean func_150652_b(int p_150652_1_, int p_150652_2_, int p_150652_3_)
        {
            for (int var4 = 0; var4 < this.field_150657_g.size(); ++var4)
            {
                ChunkPosition var5 = (ChunkPosition)this.field_150657_g.get(var4);

                if (var5.field_151329_a == p_150652_1_ && var5.field_151328_c == p_150652_3_)
                {
                    return true;
                }
            }

            return false;
        }

        protected int func_150650_a()
        {
            int var1 = 0;

            if (this.func_150646_a(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1))
            {
                ++var1;
            }

            if (this.func_150646_a(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1))
            {
                ++var1;
            }

            if (this.func_150646_a(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e))
            {
                ++var1;
            }

            if (this.func_150646_a(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e))
            {
                ++var1;
            }

            return var1;
        }

        private boolean func_150649_b(BlockRailBase.Rail p_150649_1_)
        {
            return this.func_150653_a(p_150649_1_) ? true : (this.field_150657_g.size() == 2 ? false : (this.field_150657_g.isEmpty() ? true : true));
        }

        private void func_150645_c(BlockRailBase.Rail p_150645_1_)
        {
            this.field_150657_g.add(new ChunkPosition(p_150645_1_.field_150661_c, p_150645_1_.field_150658_d, p_150645_1_.field_150659_e));
            boolean var2 = this.func_150652_b(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1);
            boolean var3 = this.func_150652_b(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1);
            boolean var4 = this.func_150652_b(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e);
            boolean var5 = this.func_150652_b(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e);
            byte var6 = -1;

            if (var2 || var3)
            {
                var6 = 0;
            }

            if (var4 || var5)
            {
                var6 = 1;
            }

            if (!this.field_150656_f)
            {
                if (var3 && var5 && !var2 && !var4)
                {
                    var6 = 6;
                }

                if (var3 && var4 && !var2 && !var5)
                {
                    var6 = 7;
                }

                if (var2 && var4 && !var3 && !var5)
                {
                    var6 = 8;
                }

                if (var2 && var5 && !var3 && !var4)
                {
                    var6 = 9;
                }
            }

            if (var6 == 0)
            {
                if (BlockRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c, this.field_150658_d + 1, this.field_150659_e - 1))
                {
                    var6 = 4;
                }

                if (BlockRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c, this.field_150658_d + 1, this.field_150659_e + 1))
                {
                    var6 = 5;
                }
            }

            if (var6 == 1)
            {
                if (BlockRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c + 1, this.field_150658_d + 1, this.field_150659_e))
                {
                    var6 = 2;
                }

                if (BlockRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c - 1, this.field_150658_d + 1, this.field_150659_e))
                {
                    var6 = 3;
                }
            }

            if (var6 < 0)
            {
                var6 = 0;
            }

            int var7 = var6;

            if (this.field_150656_f)
            {
                var7 = this.field_150660_b.getBlockMetadata(this.field_150661_c, this.field_150658_d, this.field_150659_e) & 8 | var6;
            }

            this.field_150660_b.setBlockMetadataWithNotify(this.field_150661_c, this.field_150658_d, this.field_150659_e, var7, 3);
        }

        private boolean func_150647_c(int p_150647_1_, int p_150647_2_, int p_150647_3_)
        {
            BlockRailBase.Rail var4 = this.func_150654_a(new ChunkPosition(p_150647_1_, p_150647_2_, p_150647_3_));

            if (var4 == null)
            {
                return false;
            }
            else
            {
                var4.func_150651_b();
                return var4.func_150649_b(this);
            }
        }

        public void func_150655_a(boolean p_150655_1_, boolean p_150655_2_)
        {
            boolean var3 = this.func_150647_c(this.field_150661_c, this.field_150658_d, this.field_150659_e - 1);
            boolean var4 = this.func_150647_c(this.field_150661_c, this.field_150658_d, this.field_150659_e + 1);
            boolean var5 = this.func_150647_c(this.field_150661_c - 1, this.field_150658_d, this.field_150659_e);
            boolean var6 = this.func_150647_c(this.field_150661_c + 1, this.field_150658_d, this.field_150659_e);
            byte var7 = -1;

            if ((var3 || var4) && !var5 && !var6)
            {
                var7 = 0;
            }

            if ((var5 || var6) && !var3 && !var4)
            {
                var7 = 1;
            }

            if (!this.field_150656_f)
            {
                if (var4 && var6 && !var3 && !var5)
                {
                    var7 = 6;
                }

                if (var4 && var5 && !var3 && !var6)
                {
                    var7 = 7;
                }

                if (var3 && var5 && !var4 && !var6)
                {
                    var7 = 8;
                }

                if (var3 && var6 && !var4 && !var5)
                {
                    var7 = 9;
                }
            }

            if (var7 == -1)
            {
                if (var3 || var4)
                {
                    var7 = 0;
                }

                if (var5 || var6)
                {
                    var7 = 1;
                }

                if (!this.field_150656_f)
                {
                    if (p_150655_1_)
                    {
                        if (var4 && var6)
                        {
                            var7 = 6;
                        }

                        if (var5 && var4)
                        {
                            var7 = 7;
                        }

                        if (var6 && var3)
                        {
                            var7 = 9;
                        }

                        if (var3 && var5)
                        {
                            var7 = 8;
                        }
                    }
                    else
                    {
                        if (var3 && var5)
                        {
                            var7 = 8;
                        }

                        if (var6 && var3)
                        {
                            var7 = 9;
                        }

                        if (var5 && var4)
                        {
                            var7 = 7;
                        }

                        if (var4 && var6)
                        {
                            var7 = 6;
                        }
                    }
                }
            }

            if (var7 == 0)
            {
                if (BlockRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c, this.field_150658_d + 1, this.field_150659_e - 1))
                {
                    var7 = 4;
                }

                if (BlockRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c, this.field_150658_d + 1, this.field_150659_e + 1))
                {
                    var7 = 5;
                }
            }

            if (var7 == 1)
            {
                if (BlockRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c + 1, this.field_150658_d + 1, this.field_150659_e))
                {
                    var7 = 2;
                }

                if (BlockRailBase.func_150049_b_(this.field_150660_b, this.field_150661_c - 1, this.field_150658_d + 1, this.field_150659_e))
                {
                    var7 = 3;
                }
            }

            if (var7 < 0)
            {
                var7 = 0;
            }

            this.func_150648_a(var7);
            int var8 = var7;

            if (this.field_150656_f)
            {
                var8 = this.field_150660_b.getBlockMetadata(this.field_150661_c, this.field_150658_d, this.field_150659_e) & 8 | var7;
            }

            if (p_150655_2_ || this.field_150660_b.getBlockMetadata(this.field_150661_c, this.field_150658_d, this.field_150659_e) != var8)
            {
                this.field_150660_b.setBlockMetadataWithNotify(this.field_150661_c, this.field_150658_d, this.field_150659_e, var8, 3);

                for (int var9 = 0; var9 < this.field_150657_g.size(); ++var9)
                {
                    BlockRailBase.Rail var10 = this.func_150654_a((ChunkPosition)this.field_150657_g.get(var9));

                    if (var10 != null)
                    {
                        var10.func_150651_b();

                        if (var10.func_150649_b(this))
                        {
                            var10.func_150645_c(this);
                        }
                    }
                }
            }
        }
    }
}
