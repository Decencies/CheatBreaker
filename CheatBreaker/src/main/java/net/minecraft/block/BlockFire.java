package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;

public class BlockFire extends Block
{
    private int[] field_149849_a = new int[256];
    private int[] field_149848_b = new int[256];
    private IIcon[] field_149850_M;
    

    protected BlockFire()
    {
        super(Material.fire);
        this.setTickRandomly(true);
    }

    public static void func_149843_e()
    {
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.planks), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.double_wooden_slab), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.wooden_slab), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.fence), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.oak_stairs), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.birch_stairs), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.spruce_stairs), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.jungle_stairs), 5, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.log), 5, 5);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.log2), 5, 5);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.leaves), 30, 60);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.leaves2), 30, 60);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.bookshelf), 30, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.tnt), 15, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.tallgrass), 60, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.double_plant), 60, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.yellow_flower), 60, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.red_flower), 60, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.wool), 30, 60);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.vine), 15, 100);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.coal_block), 5, 5);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.hay_block), 60, 20);
        Blocks.fire.func_149842_a(getIdFromBlock(Blocks.carpet), 60, 20);
    }

    public void func_149842_a(int p_149842_1_, int p_149842_2_, int p_149842_3_)
    {
        this.field_149849_a[p_149842_1_] = p_149842_2_;
        this.field_149848_b[p_149842_1_] = p_149842_3_;
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

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 3;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        return 0;
    }

    public int func_149738_a(World p_149738_1_)
    {
        return 30;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_)
    {
        if (p_149674_1_.getGameRules().getGameRuleBooleanValue("doFireTick"))
        {
            boolean var6 = p_149674_1_.getBlock(p_149674_2_, p_149674_3_ - 1, p_149674_4_) == Blocks.netherrack;

            if (p_149674_1_.provider instanceof WorldProviderEnd && p_149674_1_.getBlock(p_149674_2_, p_149674_3_ - 1, p_149674_4_) == Blocks.bedrock)
            {
                var6 = true;
            }

            if (!this.canPlaceBlockAt(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_))
            {
                p_149674_1_.setBlockToAir(p_149674_2_, p_149674_3_, p_149674_4_);
            }

            if (!var6 && p_149674_1_.isRaining() && (p_149674_1_.canLightningStrikeAt(p_149674_2_, p_149674_3_, p_149674_4_) || p_149674_1_.canLightningStrikeAt(p_149674_2_ - 1, p_149674_3_, p_149674_4_) || p_149674_1_.canLightningStrikeAt(p_149674_2_ + 1, p_149674_3_, p_149674_4_) || p_149674_1_.canLightningStrikeAt(p_149674_2_, p_149674_3_, p_149674_4_ - 1) || p_149674_1_.canLightningStrikeAt(p_149674_2_, p_149674_3_, p_149674_4_ + 1)))
            {
                p_149674_1_.setBlockToAir(p_149674_2_, p_149674_3_, p_149674_4_);
            }
            else
            {
                int var7 = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_, p_149674_4_);

                if (var7 < 15)
                {
                    p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_, p_149674_4_, var7 + p_149674_5_.nextInt(3) / 2, 4);
                }

                p_149674_1_.scheduleBlockUpdate(p_149674_2_, p_149674_3_, p_149674_4_, this, this.func_149738_a(p_149674_1_) + p_149674_5_.nextInt(10));

                if (!var6 && !this.func_149847_e(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_))
                {
                    if (!World.doesBlockHaveSolidTopSurface(p_149674_1_, p_149674_2_, p_149674_3_ - 1, p_149674_4_) || var7 > 3)
                    {
                        p_149674_1_.setBlockToAir(p_149674_2_, p_149674_3_, p_149674_4_);
                    }
                }
                else if (!var6 && !this.func_149844_e(p_149674_1_, p_149674_2_, p_149674_3_ - 1, p_149674_4_) && var7 == 15 && p_149674_5_.nextInt(4) == 0)
                {
                    p_149674_1_.setBlockToAir(p_149674_2_, p_149674_3_, p_149674_4_);
                }
                else
                {
                    boolean var8 = p_149674_1_.isBlockHighHumidity(p_149674_2_, p_149674_3_, p_149674_4_);
                    byte var9 = 0;

                    if (var8)
                    {
                        var9 = -50;
                    }

                    this.func_149841_a(p_149674_1_, p_149674_2_ + 1, p_149674_3_, p_149674_4_, 300 + var9, p_149674_5_, var7);
                    this.func_149841_a(p_149674_1_, p_149674_2_ - 1, p_149674_3_, p_149674_4_, 300 + var9, p_149674_5_, var7);
                    this.func_149841_a(p_149674_1_, p_149674_2_, p_149674_3_ - 1, p_149674_4_, 250 + var9, p_149674_5_, var7);
                    this.func_149841_a(p_149674_1_, p_149674_2_, p_149674_3_ + 1, p_149674_4_, 250 + var9, p_149674_5_, var7);
                    this.func_149841_a(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_ - 1, 300 + var9, p_149674_5_, var7);
                    this.func_149841_a(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_ + 1, 300 + var9, p_149674_5_, var7);

                    for (int var10 = p_149674_2_ - 1; var10 <= p_149674_2_ + 1; ++var10)
                    {
                        for (int var11 = p_149674_4_ - 1; var11 <= p_149674_4_ + 1; ++var11)
                        {
                            for (int var12 = p_149674_3_ - 1; var12 <= p_149674_3_ + 4; ++var12)
                            {
                                if (var10 != p_149674_2_ || var12 != p_149674_3_ || var11 != p_149674_4_)
                                {
                                    int var13 = 100;

                                    if (var12 > p_149674_3_ + 1)
                                    {
                                        var13 += (var12 - (p_149674_3_ + 1)) * 100;
                                    }

                                    int var14 = this.func_149845_m(p_149674_1_, var10, var12, var11);

                                    if (var14 > 0)
                                    {
                                        int var15 = (var14 + 40 + p_149674_1_.difficultySetting.getDifficultyId() * 7) / (var7 + 30);

                                        if (var8)
                                        {
                                            var15 /= 2;
                                        }

                                        if (var15 > 0 && p_149674_5_.nextInt(var13) <= var15 && (!p_149674_1_.isRaining() || !p_149674_1_.canLightningStrikeAt(var10, var12, var11)) && !p_149674_1_.canLightningStrikeAt(var10 - 1, var12, p_149674_4_) && !p_149674_1_.canLightningStrikeAt(var10 + 1, var12, var11) && !p_149674_1_.canLightningStrikeAt(var10, var12, var11 - 1) && !p_149674_1_.canLightningStrikeAt(var10, var12, var11 + 1))
                                        {
                                            int var16 = var7 + p_149674_5_.nextInt(5) / 4;

                                            if (var16 > 15)
                                            {
                                                var16 = 15;
                                            }

                                            p_149674_1_.setBlock(var10, var12, var11, this, var16, 3);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean func_149698_L()
    {
        return false;
    }

    private void func_149841_a(World p_149841_1_, int p_149841_2_, int p_149841_3_, int p_149841_4_, int p_149841_5_, Random p_149841_6_, int p_149841_7_)
    {
        int var8 = this.field_149848_b[Block.getIdFromBlock(p_149841_1_.getBlock(p_149841_2_, p_149841_3_, p_149841_4_))];

        if (p_149841_6_.nextInt(p_149841_5_) < var8)
        {
            boolean var9 = p_149841_1_.getBlock(p_149841_2_, p_149841_3_, p_149841_4_) == Blocks.tnt;

            if (p_149841_6_.nextInt(p_149841_7_ + 10) < 5 && !p_149841_1_.canLightningStrikeAt(p_149841_2_, p_149841_3_, p_149841_4_))
            {
                int var10 = p_149841_7_ + p_149841_6_.nextInt(5) / 4;

                if (var10 > 15)
                {
                    var10 = 15;
                }

                p_149841_1_.setBlock(p_149841_2_, p_149841_3_, p_149841_4_, this, var10, 3);
            }
            else
            {
                p_149841_1_.setBlockToAir(p_149841_2_, p_149841_3_, p_149841_4_);
            }

            if (var9)
            {
                Blocks.tnt.onBlockDestroyedByPlayer(p_149841_1_, p_149841_2_, p_149841_3_, p_149841_4_, 1);
            }
        }
    }

    private boolean func_149847_e(World p_149847_1_, int p_149847_2_, int p_149847_3_, int p_149847_4_)
    {
        return this.func_149844_e(p_149847_1_, p_149847_2_ + 1, p_149847_3_, p_149847_4_) ? true : (this.func_149844_e(p_149847_1_, p_149847_2_ - 1, p_149847_3_, p_149847_4_) ? true : (this.func_149844_e(p_149847_1_, p_149847_2_, p_149847_3_ - 1, p_149847_4_) ? true : (this.func_149844_e(p_149847_1_, p_149847_2_, p_149847_3_ + 1, p_149847_4_) ? true : (this.func_149844_e(p_149847_1_, p_149847_2_, p_149847_3_, p_149847_4_ - 1) ? true : this.func_149844_e(p_149847_1_, p_149847_2_, p_149847_3_, p_149847_4_ + 1)))));
    }

    private int func_149845_m(World p_149845_1_, int p_149845_2_, int p_149845_3_, int p_149845_4_)
    {
        byte var5 = 0;

        if (!p_149845_1_.isAirBlock(p_149845_2_, p_149845_3_, p_149845_4_))
        {
            return 0;
        }
        else
        {
            int var6 = this.func_149846_a(p_149845_1_, p_149845_2_ + 1, p_149845_3_, p_149845_4_, var5);
            var6 = this.func_149846_a(p_149845_1_, p_149845_2_ - 1, p_149845_3_, p_149845_4_, var6);
            var6 = this.func_149846_a(p_149845_1_, p_149845_2_, p_149845_3_ - 1, p_149845_4_, var6);
            var6 = this.func_149846_a(p_149845_1_, p_149845_2_, p_149845_3_ + 1, p_149845_4_, var6);
            var6 = this.func_149846_a(p_149845_1_, p_149845_2_, p_149845_3_, p_149845_4_ - 1, var6);
            var6 = this.func_149846_a(p_149845_1_, p_149845_2_, p_149845_3_, p_149845_4_ + 1, var6);
            return var6;
        }
    }

    public boolean isCollidable()
    {
        return false;
    }

    public boolean func_149844_e(IBlockAccess p_149844_1_, int p_149844_2_, int p_149844_3_, int p_149844_4_)
    {
        return this.field_149849_a[Block.getIdFromBlock(p_149844_1_.getBlock(p_149844_2_, p_149844_3_, p_149844_4_))] > 0;
    }

    public int func_149846_a(World p_149846_1_, int p_149846_2_, int p_149846_3_, int p_149846_4_, int p_149846_5_)
    {
        int var6 = this.field_149849_a[Block.getIdFromBlock(p_149846_1_.getBlock(p_149846_2_, p_149846_3_, p_149846_4_))];
        return var6 > p_149846_5_ ? var6 : p_149846_5_;
    }

    public boolean canPlaceBlockAt(World p_149742_1_, int p_149742_2_, int p_149742_3_, int p_149742_4_)
    {
        return World.doesBlockHaveSolidTopSurface(p_149742_1_, p_149742_2_, p_149742_3_ - 1, p_149742_4_) || this.func_149847_e(p_149742_1_, p_149742_2_, p_149742_3_, p_149742_4_);
    }

    public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_)
    {
        if (!World.doesBlockHaveSolidTopSurface(p_149695_1_, p_149695_2_, p_149695_3_ - 1, p_149695_4_) && !this.func_149847_e(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_))
        {
            p_149695_1_.setBlockToAir(p_149695_2_, p_149695_3_, p_149695_4_);
        }
    }

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_)
    {
        if (p_149726_1_.provider.dimensionId > 0 || !Blocks.portal.func_150000_e(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_))
        {
            if (!World.doesBlockHaveSolidTopSurface(p_149726_1_, p_149726_2_, p_149726_3_ - 1, p_149726_4_) && !this.func_149847_e(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_))
            {
                p_149726_1_.setBlockToAir(p_149726_2_, p_149726_3_, p_149726_4_);
            }
            else
            {
                p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_, p_149726_4_, this, this.func_149738_a(p_149726_1_) + p_149726_1_.rand.nextInt(10));
            }
        }
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World p_149734_1_, int p_149734_2_, int p_149734_3_, int p_149734_4_, Random p_149734_5_)
    {
        if (p_149734_5_.nextInt(24) == 0)
        {
            p_149734_1_.playSound((double)((float)p_149734_2_ + 0.5F), (double)((float)p_149734_3_ + 0.5F), (double)((float)p_149734_4_ + 0.5F), "fire.fire", 1.0F + p_149734_5_.nextFloat(), p_149734_5_.nextFloat() * 0.7F + 0.3F, false);
        }

        int var6;
        float var7;
        float var8;
        float var9;

        if (!World.doesBlockHaveSolidTopSurface(p_149734_1_, p_149734_2_, p_149734_3_ - 1, p_149734_4_) && !Blocks.fire.func_149844_e(p_149734_1_, p_149734_2_, p_149734_3_ - 1, p_149734_4_))
        {
            if (Blocks.fire.func_149844_e(p_149734_1_, p_149734_2_ - 1, p_149734_3_, p_149734_4_))
            {
                for (var6 = 0; var6 < 2; ++var6)
                {
                    var7 = (float)p_149734_2_ + p_149734_5_.nextFloat() * 0.1F;
                    var8 = (float)p_149734_3_ + p_149734_5_.nextFloat();
                    var9 = (float)p_149734_4_ + p_149734_5_.nextFloat();
                    p_149734_1_.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.func_149844_e(p_149734_1_, p_149734_2_ + 1, p_149734_3_, p_149734_4_))
            {
                for (var6 = 0; var6 < 2; ++var6)
                {
                    var7 = (float)(p_149734_2_ + 1) - p_149734_5_.nextFloat() * 0.1F;
                    var8 = (float)p_149734_3_ + p_149734_5_.nextFloat();
                    var9 = (float)p_149734_4_ + p_149734_5_.nextFloat();
                    p_149734_1_.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.func_149844_e(p_149734_1_, p_149734_2_, p_149734_3_, p_149734_4_ - 1))
            {
                for (var6 = 0; var6 < 2; ++var6)
                {
                    var7 = (float)p_149734_2_ + p_149734_5_.nextFloat();
                    var8 = (float)p_149734_3_ + p_149734_5_.nextFloat();
                    var9 = (float)p_149734_4_ + p_149734_5_.nextFloat() * 0.1F;
                    p_149734_1_.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.func_149844_e(p_149734_1_, p_149734_2_, p_149734_3_, p_149734_4_ + 1))
            {
                for (var6 = 0; var6 < 2; ++var6)
                {
                    var7 = (float)p_149734_2_ + p_149734_5_.nextFloat();
                    var8 = (float)p_149734_3_ + p_149734_5_.nextFloat();
                    var9 = (float)(p_149734_4_ + 1) - p_149734_5_.nextFloat() * 0.1F;
                    p_149734_1_.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Blocks.fire.func_149844_e(p_149734_1_, p_149734_2_, p_149734_3_ + 1, p_149734_4_))
            {
                for (var6 = 0; var6 < 2; ++var6)
                {
                    var7 = (float)p_149734_2_ + p_149734_5_.nextFloat();
                    var8 = (float)(p_149734_3_ + 1) - p_149734_5_.nextFloat() * 0.1F;
                    var9 = (float)p_149734_4_ + p_149734_5_.nextFloat();
                    p_149734_1_.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        else
        {
            for (var6 = 0; var6 < 3; ++var6)
            {
                var7 = (float)p_149734_2_ + p_149734_5_.nextFloat();
                var8 = (float)p_149734_3_ + p_149734_5_.nextFloat() * 0.5F + 0.5F;
                var9 = (float)p_149734_4_ + p_149734_5_.nextFloat();
                p_149734_1_.spawnParticle("largesmoke", (double)var7, (double)var8, (double)var9, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.field_149850_M = new IIcon[] {p_149651_1_.registerIcon(this.getTextureName() + "_layer_0"), p_149651_1_.registerIcon(this.getTextureName() + "_layer_1")};
    }

    public IIcon func_149840_c(int p_149840_1_)
    {
        return this.field_149850_M[p_149840_1_];
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return this.field_149850_M[0];
    }

    public MapColor getMapColor(int p_149728_1_)
    {
        return MapColor.field_151656_f;
    }
}
