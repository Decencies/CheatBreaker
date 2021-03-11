package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortal extends BlockBreakable
{
    public static final int[][] field_150001_a = new int[][] {new int[0], {3, 1}, {2, 0}};


    public BlockPortal()
    {
        super("portal", Material.Portal, false);
        this.setTickRandomly(true);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        super.updateTick(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_, p_149674_5_);

        if (p_149674_1_.provider.isSurfaceWorld() && p_149674_1_.getGameRules().getGameRuleBooleanValue("doMobSpawning") && p_149674_5_.nextInt(2000) < p_149674_1_.difficultySetting.getDifficultyId())
        {
            int var6;

            for (var6 = p_149674_3_; !World.doesBlockHaveSolidTopSurface(p_149674_1_, p_149674_2_, var6, p_149674_4_) && var6 > 0; --var6)
            {
                ;
            }

            if (var6 > 0 && !p_149674_1_.getBlock(p_149674_2_, var6 + 1, p_149674_4_).isNormalCube())
            {
                Entity var7 = ItemMonsterPlacer.spawnCreature(p_149674_1_, 57, (double)p_149674_2_ + 0.5D, (double)var6 + 1.1D, (double)p_149674_4_ + 0.5D);

                if (var7 != null)
                {
                    var7.timeUntilPortal = var7.getPortalCooldown();
                }
            }
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_)
    {
        int var5 = func_149999_b(p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_));

        if (var5 == 0)
        {
            if (p_149719_1_.getBlock(p_149719_2_ - 1, p_149719_3_, p_149719_4_) != this && p_149719_1_.getBlock(p_149719_2_ + 1, p_149719_3_, p_149719_4_) != this)
            {
                var5 = 2;
            }
            else
            {
                var5 = 1;
            }

            if (p_149719_1_ instanceof World && !((World)p_149719_1_).isClient)
            {
                ((World)p_149719_1_).setBlockMetadataWithNotify(p_149719_2_, p_149719_3_, p_149719_4_, var5, 2);
            }
        }

        float var6 = 0.125F;
        float var7 = 0.125F;

        if (var5 == 1)
        {
            var6 = 0.5F;
        }

        if (var5 == 2)
        {
            var7 = 0.5F;
        }

        this.setBlockBounds(0.5F - var6, 0.0F, 0.5F - var7, 0.5F + var6, 1.0F, 0.5F + var7);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean func_150000_e(World p_150000_1_, int p_150000_2_, int p_150000_3_, int p_150000_4_)
    {
        BlockPortal.Size var5 = new BlockPortal.Size(p_150000_1_, p_150000_2_, p_150000_3_, p_150000_4_, 1);
        BlockPortal.Size var6 = new BlockPortal.Size(p_150000_1_, p_150000_2_, p_150000_3_, p_150000_4_, 2);

        if (var5.func_150860_b() && var5.field_150864_e == 0)
        {
            var5.func_150859_c();
            return true;
        }
        else if (var6.func_150860_b() && var6.field_150864_e == 0)
        {
            var6.func_150859_c();
            return true;
        }
        else
        {
            return false;
        }
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        int var6 = func_149999_b(p_149695_1_.getBlockMetadata(p_149695_2_, p_149695_3_, p_149695_4_));
        BlockPortal.Size var7 = new BlockPortal.Size(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, 1);
        BlockPortal.Size var8 = new BlockPortal.Size(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, 2);

        if (var6 == 1 && (!var7.func_150860_b() || var7.field_150864_e < var7.field_150868_h * var7.field_150862_g))
        {
            p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_, Blocks.air);
        }
        else if (var6 == 2 && (!var8.func_150860_b() || var8.field_150864_e < var8.field_150868_h * var8.field_150862_g))
        {
            p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_, Blocks.air);
        }
        else if (var6 == 0 && !var7.func_150860_b() && !var8.func_150860_b())
        {
            p_149695_1_.setBlock(p_149695_2_, p_149695_3_, p_149695_4_, Blocks.air);
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_)
    {
        int var6 = 0;

        if (p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_) == this)
        {
            var6 = func_149999_b(p_149646_1_.getBlockMetadata(p_149646_2_, p_149646_3_, p_149646_4_));

            if (var6 == 0)
            {
                return false;
            }

            if (var6 == 2 && p_149646_5_ != 5 && p_149646_5_ != 4)
            {
                return false;
            }

            if (var6 == 1 && p_149646_5_ != 3 && p_149646_5_ != 2)
            {
                return false;
            }
        }

        boolean var7 = p_149646_1_.getBlock(p_149646_2_ - 1, p_149646_3_, p_149646_4_) == this && p_149646_1_.getBlock(p_149646_2_ - 2, p_149646_3_, p_149646_4_) != this;
        boolean var8 = p_149646_1_.getBlock(p_149646_2_ + 1, p_149646_3_, p_149646_4_) == this && p_149646_1_.getBlock(p_149646_2_ + 2, p_149646_3_, p_149646_4_) != this;
        boolean var9 = p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_ - 1) == this && p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_ - 2) != this;
        boolean var10 = p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_ + 1) == this && p_149646_1_.getBlock(p_149646_2_, p_149646_3_, p_149646_4_ + 2) != this;
        boolean var11 = var7 || var8 || var6 == 1;
        boolean var12 = var9 || var10 || var6 == 2;
        return var11 && p_149646_5_ == 4 ? true : (var11 && p_149646_5_ == 5 ? true : (var12 && p_149646_5_ == 2 ? true : var12 && p_149646_5_ == 3));
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

    public void onEntityCollidedWithBlock(World p_149670_1_, int p_149670_2_, int p_149670_3_, int p_149670_4_, Entity p_149670_5_)
    {
        if (p_149670_5_.ridingEntity == null && p_149670_5_.riddenByEntity == null)
        {
            p_149670_5_.setInPortal();
        }
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_)
    {
        if (p_149734_5_.nextInt(100) == 0)
        {
            p_149734_1_.playSound((double)p_149734_2_ + 0.5D, (double)p_149734_3_ + 0.5D, (double)p_149734_4_ + 0.5D, "portal.portal", 0.5F, p_149734_5_.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int var6 = 0; var6 < 4; ++var6)
        {
            double var7 = (double)((float)p_149734_2_ + p_149734_5_.nextFloat());
            double var9 = (double)((float)p_149734_3_ + p_149734_5_.nextFloat());
            double var11 = (double)((float)p_149734_4_ + p_149734_5_.nextFloat());
            double var13 = 0.0D;
            double var15 = 0.0D;
            double var17 = 0.0D;
            int var19 = p_149734_5_.nextInt(2) * 2 - 1;
            var13 = ((double)p_149734_5_.nextFloat() - 0.5D) * 0.5D;
            var15 = ((double)p_149734_5_.nextFloat() - 0.5D) * 0.5D;
            var17 = ((double)p_149734_5_.nextFloat() - 0.5D) * 0.5D;

            if (p_149734_1_.getBlock(p_149734_2_ - 1, p_149734_3_, p_149734_4_) != this && p_149734_1_.getBlock(p_149734_2_ + 1, p_149734_3_, p_149734_4_) != this)
            {
                var7 = (double)p_149734_2_ + 0.5D + 0.25D * (double)var19;
                var13 = (double)(p_149734_5_.nextFloat() * 2.0F * (float)var19);
            }
            else
            {
                var11 = (double)p_149734_4_ + 0.5D + 0.25D * (double)var19;
                var17 = (double)(p_149734_5_.nextFloat() * 2.0F * (float)var19);
            }

            p_149734_1_.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
        }
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_)
    {
        return Item.getItemById(0);
    }

    public static int func_149999_b(int p_149999_0_)
    {
        return p_149999_0_ & 3;
    }

    public static class Size
    {
        private final World field_150867_a;
        private final int field_150865_b;
        private final int field_150866_c;
        private final int field_150863_d;
        private int field_150864_e = 0;
        private ChunkCoordinates field_150861_f;
        private int field_150862_g;
        private int field_150868_h;


        public Size(World p_i45415_1_, int p_i45415_2_, int p_i45415_3_, int p_i45415_4_, int p_i45415_5_)
        {
            this.field_150867_a = p_i45415_1_;
            this.field_150865_b = p_i45415_5_;
            this.field_150863_d = BlockPortal.field_150001_a[p_i45415_5_][0];
            this.field_150866_c = BlockPortal.field_150001_a[p_i45415_5_][1];

            for (int var6 = p_i45415_3_; p_i45415_3_ > var6 - 21 && p_i45415_3_ > 0 && this.func_150857_a(p_i45415_1_.getBlock(p_i45415_2_, p_i45415_3_ - 1, p_i45415_4_)); --p_i45415_3_)
            {
                ;
            }

            int var7 = this.func_150853_a(p_i45415_2_, p_i45415_3_, p_i45415_4_, this.field_150863_d) - 1;

            if (var7 >= 0)
            {
                this.field_150861_f = new ChunkCoordinates(p_i45415_2_ + var7 * Direction.offsetX[this.field_150863_d], p_i45415_3_, p_i45415_4_ + var7 * Direction.offsetZ[this.field_150863_d]);
                this.field_150868_h = this.func_150853_a(this.field_150861_f.posX, this.field_150861_f.posY, this.field_150861_f.posZ, this.field_150866_c);

                if (this.field_150868_h < 2 || this.field_150868_h > 21)
                {
                    this.field_150861_f = null;
                    this.field_150868_h = 0;
                }
            }

            if (this.field_150861_f != null)
            {
                this.field_150862_g = this.func_150858_a();
            }
        }

        protected int func_150853_a(int p_150853_1_, int p_150853_2_, int p_150853_3_, int p_150853_4_)
        {
            int var6 = Direction.offsetX[p_150853_4_];
            int var7 = Direction.offsetZ[p_150853_4_];
            int var5;
            Block var8;

            for (var5 = 0; var5 < 22; ++var5)
            {
                var8 = this.field_150867_a.getBlock(p_150853_1_ + var6 * var5, p_150853_2_, p_150853_3_ + var7 * var5);

                if (!this.func_150857_a(var8))
                {
                    break;
                }

                Block var9 = this.field_150867_a.getBlock(p_150853_1_ + var6 * var5, p_150853_2_ - 1, p_150853_3_ + var7 * var5);

                if (var9 != Blocks.obsidian)
                {
                    break;
                }
            }

            var8 = this.field_150867_a.getBlock(p_150853_1_ + var6 * var5, p_150853_2_, p_150853_3_ + var7 * var5);
            return var8 == Blocks.obsidian ? var5 : 0;
        }

        protected int func_150858_a()
        {
            int var1;
            int var2;
            int var3;
            int var4;
            label56:

            for (this.field_150862_g = 0; this.field_150862_g < 21; ++this.field_150862_g)
            {
                var1 = this.field_150861_f.posY + this.field_150862_g;

                for (var2 = 0; var2 < this.field_150868_h; ++var2)
                {
                    var3 = this.field_150861_f.posX + var2 * Direction.offsetX[BlockPortal.field_150001_a[this.field_150865_b][1]];
                    var4 = this.field_150861_f.posZ + var2 * Direction.offsetZ[BlockPortal.field_150001_a[this.field_150865_b][1]];
                    Block var5 = this.field_150867_a.getBlock(var3, var1, var4);

                    if (!this.func_150857_a(var5))
                    {
                        break label56;
                    }

                    if (var5 == Blocks.portal)
                    {
                        ++this.field_150864_e;
                    }

                    if (var2 == 0)
                    {
                        var5 = this.field_150867_a.getBlock(var3 + Direction.offsetX[BlockPortal.field_150001_a[this.field_150865_b][0]], var1, var4 + Direction.offsetZ[BlockPortal.field_150001_a[this.field_150865_b][0]]);

                        if (var5 != Blocks.obsidian)
                        {
                            break label56;
                        }
                    }
                    else if (var2 == this.field_150868_h - 1)
                    {
                        var5 = this.field_150867_a.getBlock(var3 + Direction.offsetX[BlockPortal.field_150001_a[this.field_150865_b][1]], var1, var4 + Direction.offsetZ[BlockPortal.field_150001_a[this.field_150865_b][1]]);

                        if (var5 != Blocks.obsidian)
                        {
                            break label56;
                        }
                    }
                }
            }

            for (var1 = 0; var1 < this.field_150868_h; ++var1)
            {
                var2 = this.field_150861_f.posX + var1 * Direction.offsetX[BlockPortal.field_150001_a[this.field_150865_b][1]];
                var3 = this.field_150861_f.posY + this.field_150862_g;
                var4 = this.field_150861_f.posZ + var1 * Direction.offsetZ[BlockPortal.field_150001_a[this.field_150865_b][1]];

                if (this.field_150867_a.getBlock(var2, var3, var4) != Blocks.obsidian)
                {
                    this.field_150862_g = 0;
                    break;
                }
            }

            if (this.field_150862_g <= 21 && this.field_150862_g >= 3)
            {
                return this.field_150862_g;
            }
            else
            {
                this.field_150861_f = null;
                this.field_150868_h = 0;
                this.field_150862_g = 0;
                return 0;
            }
        }

        protected boolean func_150857_a(Block p_150857_1_)
        {
            return p_150857_1_.blockMaterial == Material.air || p_150857_1_ == Blocks.fire || p_150857_1_ == Blocks.portal;
        }

        public boolean func_150860_b()
        {
            return this.field_150861_f != null && this.field_150868_h >= 2 && this.field_150868_h <= 21 && this.field_150862_g >= 3 && this.field_150862_g <= 21;
        }

        public void func_150859_c()
        {
            for (int var1 = 0; var1 < this.field_150868_h; ++var1)
            {
                int var2 = this.field_150861_f.posX + Direction.offsetX[this.field_150866_c] * var1;
                int var3 = this.field_150861_f.posZ + Direction.offsetZ[this.field_150866_c] * var1;

                for (int var4 = 0; var4 < this.field_150862_g; ++var4)
                {
                    int var5 = this.field_150861_f.posY + var4;
                    this.field_150867_a.setBlock(var2, var5, var3, Blocks.portal, this.field_150865_b, 2);
                }
            }
        }
    }
}
