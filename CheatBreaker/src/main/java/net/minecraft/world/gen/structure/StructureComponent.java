package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public abstract class StructureComponent
{
    protected StructureBoundingBox boundingBox;

    /** switches the Coordinate System base off the Bounding Box */
    protected int coordBaseMode;

    /** The type ID of this component. */
    protected int componentType;


    public StructureComponent() {}

    protected StructureComponent(int p_i2091_1_)
    {
        this.componentType = p_i2091_1_;
        this.coordBaseMode = -1;
    }

    public NBTTagCompound func_143010_b()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        var1.setString("id", MapGenStructureIO.func_143036_a(this));
        var1.setTag("BB", this.boundingBox.func_151535_h());
        var1.setInteger("O", this.coordBaseMode);
        var1.setInteger("GD", this.componentType);
        this.func_143012_a(var1);
        return var1;
    }

    protected abstract void func_143012_a(NBTTagCompound p_143012_1_);

    public void func_143009_a(World p_143009_1_, NBTTagCompound p_143009_2_)
    {
        if (p_143009_2_.hasKey("BB"))
        {
            this.boundingBox = new StructureBoundingBox(p_143009_2_.getIntArray("BB"));
        }

        this.coordBaseMode = p_143009_2_.getInteger("O");
        this.componentType = p_143009_2_.getInteger("GD");
        this.func_143011_b(p_143009_2_);
    }

    protected abstract void func_143011_b(NBTTagCompound p_143011_1_);

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {}

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public abstract boolean addComponentParts(World p_74875_1_, Random p_74875_2_, StructureBoundingBox p_74875_3_);

    public StructureBoundingBox getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Returns the component type ID of this component.
     */
    public int getComponentType()
    {
        return this.componentType;
    }

    /**
     * Discover if bounding box can fit within the current bounding box object.
     */
    public static StructureComponent findIntersecting(List p_74883_0_, StructureBoundingBox p_74883_1_)
    {
        Iterator var2 = p_74883_0_.iterator();
        StructureComponent var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (StructureComponent)var2.next();
        }
        while (var3.getBoundingBox() == null || !var3.getBoundingBox().intersectsWith(p_74883_1_));

        return var3;
    }

    public ChunkPosition func_151553_a()
    {
        return new ChunkPosition(this.boundingBox.getCenterX(), this.boundingBox.getCenterY(), this.boundingBox.getCenterZ());
    }

    /**
     * checks the entire StructureBoundingBox for Liquids
     */
    protected boolean isLiquidInStructureBoundingBox(World p_74860_1_, StructureBoundingBox p_74860_2_)
    {
        int var3 = Math.max(this.boundingBox.minX - 1, p_74860_2_.minX);
        int var4 = Math.max(this.boundingBox.minY - 1, p_74860_2_.minY);
        int var5 = Math.max(this.boundingBox.minZ - 1, p_74860_2_.minZ);
        int var6 = Math.min(this.boundingBox.maxX + 1, p_74860_2_.maxX);
        int var7 = Math.min(this.boundingBox.maxY + 1, p_74860_2_.maxY);
        int var8 = Math.min(this.boundingBox.maxZ + 1, p_74860_2_.maxZ);
        int var9;
        int var10;

        for (var9 = var3; var9 <= var6; ++var9)
        {
            for (var10 = var5; var10 <= var8; ++var10)
            {
                if (p_74860_1_.getBlock(var9, var4, var10).getMaterial().isLiquid())
                {
                    return true;
                }

                if (p_74860_1_.getBlock(var9, var7, var10).getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        for (var9 = var3; var9 <= var6; ++var9)
        {
            for (var10 = var4; var10 <= var7; ++var10)
            {
                if (p_74860_1_.getBlock(var9, var10, var5).getMaterial().isLiquid())
                {
                    return true;
                }

                if (p_74860_1_.getBlock(var9, var10, var8).getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        for (var9 = var5; var9 <= var8; ++var9)
        {
            for (var10 = var4; var10 <= var7; ++var10)
            {
                if (p_74860_1_.getBlock(var3, var10, var9).getMaterial().isLiquid())
                {
                    return true;
                }

                if (p_74860_1_.getBlock(var6, var10, var9).getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected int getXWithOffset(int p_74865_1_, int p_74865_2_)
    {
        switch (this.coordBaseMode)
        {
            case 0:
            case 2:
                return this.boundingBox.minX + p_74865_1_;

            case 1:
                return this.boundingBox.maxX - p_74865_2_;

            case 3:
                return this.boundingBox.minX + p_74865_2_;

            default:
                return p_74865_1_;
        }
    }

    protected int getYWithOffset(int p_74862_1_)
    {
        return this.coordBaseMode == -1 ? p_74862_1_ : p_74862_1_ + this.boundingBox.minY;
    }

    protected int getZWithOffset(int p_74873_1_, int p_74873_2_)
    {
        switch (this.coordBaseMode)
        {
            case 0:
                return this.boundingBox.minZ + p_74873_2_;

            case 1:
            case 3:
                return this.boundingBox.minZ + p_74873_1_;

            case 2:
                return this.boundingBox.maxZ - p_74873_2_;

            default:
                return p_74873_2_;
        }
    }

    protected int func_151555_a(Block p_151555_1_, int p_151555_2_)
    {
        if (p_151555_1_ == Blocks.rail)
        {
            if (this.coordBaseMode == 1 || this.coordBaseMode == 3)
            {
                if (p_151555_2_ == 1)
                {
                    return 0;
                }

                return 1;
            }
        }
        else if (p_151555_1_ != Blocks.wooden_door && p_151555_1_ != Blocks.iron_door)
        {
            if (p_151555_1_ != Blocks.stone_stairs && p_151555_1_ != Blocks.oak_stairs && p_151555_1_ != Blocks.nether_brick_stairs && p_151555_1_ != Blocks.stone_brick_stairs && p_151555_1_ != Blocks.sandstone_stairs)
            {
                if (p_151555_1_ == Blocks.ladder)
                {
                    if (this.coordBaseMode == 0)
                    {
                        if (p_151555_2_ == 2)
                        {
                            return 3;
                        }

                        if (p_151555_2_ == 3)
                        {
                            return 2;
                        }
                    }
                    else if (this.coordBaseMode == 1)
                    {
                        if (p_151555_2_ == 2)
                        {
                            return 4;
                        }

                        if (p_151555_2_ == 3)
                        {
                            return 5;
                        }

                        if (p_151555_2_ == 4)
                        {
                            return 2;
                        }

                        if (p_151555_2_ == 5)
                        {
                            return 3;
                        }
                    }
                    else if (this.coordBaseMode == 3)
                    {
                        if (p_151555_2_ == 2)
                        {
                            return 5;
                        }

                        if (p_151555_2_ == 3)
                        {
                            return 4;
                        }

                        if (p_151555_2_ == 4)
                        {
                            return 2;
                        }

                        if (p_151555_2_ == 5)
                        {
                            return 3;
                        }
                    }
                }
                else if (p_151555_1_ == Blocks.stone_button)
                {
                    if (this.coordBaseMode == 0)
                    {
                        if (p_151555_2_ == 3)
                        {
                            return 4;
                        }

                        if (p_151555_2_ == 4)
                        {
                            return 3;
                        }
                    }
                    else if (this.coordBaseMode == 1)
                    {
                        if (p_151555_2_ == 3)
                        {
                            return 1;
                        }

                        if (p_151555_2_ == 4)
                        {
                            return 2;
                        }

                        if (p_151555_2_ == 2)
                        {
                            return 3;
                        }

                        if (p_151555_2_ == 1)
                        {
                            return 4;
                        }
                    }
                    else if (this.coordBaseMode == 3)
                    {
                        if (p_151555_2_ == 3)
                        {
                            return 2;
                        }

                        if (p_151555_2_ == 4)
                        {
                            return 1;
                        }

                        if (p_151555_2_ == 2)
                        {
                            return 3;
                        }

                        if (p_151555_2_ == 1)
                        {
                            return 4;
                        }
                    }
                }
                else if (p_151555_1_ != Blocks.tripwire_hook && !(p_151555_1_ instanceof BlockDirectional))
                {
                    if (p_151555_1_ == Blocks.piston || p_151555_1_ == Blocks.sticky_piston || p_151555_1_ == Blocks.lever || p_151555_1_ == Blocks.dispenser)
                    {
                        if (this.coordBaseMode == 0)
                        {
                            if (p_151555_2_ == 2 || p_151555_2_ == 3)
                            {
                                return Facing.oppositeSide[p_151555_2_];
                            }
                        }
                        else if (this.coordBaseMode == 1)
                        {
                            if (p_151555_2_ == 2)
                            {
                                return 4;
                            }

                            if (p_151555_2_ == 3)
                            {
                                return 5;
                            }

                            if (p_151555_2_ == 4)
                            {
                                return 2;
                            }

                            if (p_151555_2_ == 5)
                            {
                                return 3;
                            }
                        }
                        else if (this.coordBaseMode == 3)
                        {
                            if (p_151555_2_ == 2)
                            {
                                return 5;
                            }

                            if (p_151555_2_ == 3)
                            {
                                return 4;
                            }

                            if (p_151555_2_ == 4)
                            {
                                return 2;
                            }

                            if (p_151555_2_ == 5)
                            {
                                return 3;
                            }
                        }
                    }
                }
                else if (this.coordBaseMode == 0)
                {
                    if (p_151555_2_ == 0 || p_151555_2_ == 2)
                    {
                        return Direction.rotateOpposite[p_151555_2_];
                    }
                }
                else if (this.coordBaseMode == 1)
                {
                    if (p_151555_2_ == 2)
                    {
                        return 1;
                    }

                    if (p_151555_2_ == 0)
                    {
                        return 3;
                    }

                    if (p_151555_2_ == 1)
                    {
                        return 2;
                    }

                    if (p_151555_2_ == 3)
                    {
                        return 0;
                    }
                }
                else if (this.coordBaseMode == 3)
                {
                    if (p_151555_2_ == 2)
                    {
                        return 3;
                    }

                    if (p_151555_2_ == 0)
                    {
                        return 1;
                    }

                    if (p_151555_2_ == 1)
                    {
                        return 2;
                    }

                    if (p_151555_2_ == 3)
                    {
                        return 0;
                    }
                }
            }
            else if (this.coordBaseMode == 0)
            {
                if (p_151555_2_ == 2)
                {
                    return 3;
                }

                if (p_151555_2_ == 3)
                {
                    return 2;
                }
            }
            else if (this.coordBaseMode == 1)
            {
                if (p_151555_2_ == 0)
                {
                    return 2;
                }

                if (p_151555_2_ == 1)
                {
                    return 3;
                }

                if (p_151555_2_ == 2)
                {
                    return 0;
                }

                if (p_151555_2_ == 3)
                {
                    return 1;
                }
            }
            else if (this.coordBaseMode == 3)
            {
                if (p_151555_2_ == 0)
                {
                    return 2;
                }

                if (p_151555_2_ == 1)
                {
                    return 3;
                }

                if (p_151555_2_ == 2)
                {
                    return 1;
                }

                if (p_151555_2_ == 3)
                {
                    return 0;
                }
            }
        }
        else if (this.coordBaseMode == 0)
        {
            if (p_151555_2_ == 0)
            {
                return 2;
            }

            if (p_151555_2_ == 2)
            {
                return 0;
            }
        }
        else
        {
            if (this.coordBaseMode == 1)
            {
                return p_151555_2_ + 1 & 3;
            }

            if (this.coordBaseMode == 3)
            {
                return p_151555_2_ + 3 & 3;
            }
        }

        return p_151555_2_;
    }

    protected void func_151550_a(World p_151550_1_, Block p_151550_2_, int p_151550_3_, int p_151550_4_, int p_151550_5_, int p_151550_6_, StructureBoundingBox p_151550_7_)
    {
        int var8 = this.getXWithOffset(p_151550_4_, p_151550_6_);
        int var9 = this.getYWithOffset(p_151550_5_);
        int var10 = this.getZWithOffset(p_151550_4_, p_151550_6_);

        if (p_151550_7_.isVecInside(var8, var9, var10))
        {
            p_151550_1_.setBlock(var8, var9, var10, p_151550_2_, p_151550_3_, 2);
        }
    }

    protected Block func_151548_a(World p_151548_1_, int p_151548_2_, int p_151548_3_, int p_151548_4_, StructureBoundingBox p_151548_5_)
    {
        int var6 = this.getXWithOffset(p_151548_2_, p_151548_4_);
        int var7 = this.getYWithOffset(p_151548_3_);
        int var8 = this.getZWithOffset(p_151548_2_, p_151548_4_);
        return !p_151548_5_.isVecInside(var6, var7, var8) ? Blocks.air : p_151548_1_.getBlock(var6, var7, var8);
    }

    /**
     * arguments: (World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ)
     */
    protected void fillWithAir(World p_74878_1_, StructureBoundingBox p_74878_2_, int p_74878_3_, int p_74878_4_, int p_74878_5_, int p_74878_6_, int p_74878_7_, int p_74878_8_)
    {
        for (int var9 = p_74878_4_; var9 <= p_74878_7_; ++var9)
        {
            for (int var10 = p_74878_3_; var10 <= p_74878_6_; ++var10)
            {
                for (int var11 = p_74878_5_; var11 <= p_74878_8_; ++var11)
                {
                    this.func_151550_a(p_74878_1_, Blocks.air, 0, var10, var9, var11, p_74878_2_);
                }
            }
        }
    }

    protected void func_151549_a(World p_151549_1_, StructureBoundingBox p_151549_2_, int p_151549_3_, int p_151549_4_, int p_151549_5_, int p_151549_6_, int p_151549_7_, int p_151549_8_, Block p_151549_9_, Block p_151549_10_, boolean p_151549_11_)
    {
        for (int var12 = p_151549_4_; var12 <= p_151549_7_; ++var12)
        {
            for (int var13 = p_151549_3_; var13 <= p_151549_6_; ++var13)
            {
                for (int var14 = p_151549_5_; var14 <= p_151549_8_; ++var14)
                {
                    if (!p_151549_11_ || this.func_151548_a(p_151549_1_, var13, var12, var14, p_151549_2_).getMaterial() != Material.air)
                    {
                        if (var12 != p_151549_4_ && var12 != p_151549_7_ && var13 != p_151549_3_ && var13 != p_151549_6_ && var14 != p_151549_5_ && var14 != p_151549_8_)
                        {
                            this.func_151550_a(p_151549_1_, p_151549_10_, 0, var13, var12, var14, p_151549_2_);
                        }
                        else
                        {
                            this.func_151550_a(p_151549_1_, p_151549_9_, 0, var13, var12, var14, p_151549_2_);
                        }
                    }
                }
            }
        }
    }

    protected void func_151556_a(World p_151556_1_, StructureBoundingBox p_151556_2_, int p_151556_3_, int p_151556_4_, int p_151556_5_, int p_151556_6_, int p_151556_7_, int p_151556_8_, Block p_151556_9_, int p_151556_10_, Block p_151556_11_, int p_151556_12_, boolean p_151556_13_)
    {
        for (int var14 = p_151556_4_; var14 <= p_151556_7_; ++var14)
        {
            for (int var15 = p_151556_3_; var15 <= p_151556_6_; ++var15)
            {
                for (int var16 = p_151556_5_; var16 <= p_151556_8_; ++var16)
                {
                    if (!p_151556_13_ || this.func_151548_a(p_151556_1_, var15, var14, var16, p_151556_2_).getMaterial() != Material.air)
                    {
                        if (var14 != p_151556_4_ && var14 != p_151556_7_ && var15 != p_151556_3_ && var15 != p_151556_6_ && var16 != p_151556_5_ && var16 != p_151556_8_)
                        {
                            this.func_151550_a(p_151556_1_, p_151556_11_, p_151556_12_, var15, var14, var16, p_151556_2_);
                        }
                        else
                        {
                            this.func_151550_a(p_151556_1_, p_151556_9_, p_151556_10_, var15, var14, var16, p_151556_2_);
                        }
                    }
                }
            }
        }
    }

    /**
     * arguments: World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ, boolean alwaysreplace, Random rand, StructurePieceBlockSelector blockselector
     */
    protected void fillWithRandomizedBlocks(World p_74882_1_, StructureBoundingBox p_74882_2_, int p_74882_3_, int p_74882_4_, int p_74882_5_, int p_74882_6_, int p_74882_7_, int p_74882_8_, boolean p_74882_9_, Random p_74882_10_, StructureComponent.BlockSelector p_74882_11_)
    {
        for (int var12 = p_74882_4_; var12 <= p_74882_7_; ++var12)
        {
            for (int var13 = p_74882_3_; var13 <= p_74882_6_; ++var13)
            {
                for (int var14 = p_74882_5_; var14 <= p_74882_8_; ++var14)
                {
                    if (!p_74882_9_ || this.func_151548_a(p_74882_1_, var13, var12, var14, p_74882_2_).getMaterial() != Material.air)
                    {
                        p_74882_11_.selectBlocks(p_74882_10_, var13, var12, var14, var12 == p_74882_4_ || var12 == p_74882_7_ || var13 == p_74882_3_ || var13 == p_74882_6_ || var14 == p_74882_5_ || var14 == p_74882_8_);
                        this.func_151550_a(p_74882_1_, p_74882_11_.func_151561_a(), p_74882_11_.getSelectedBlockMetaData(), var13, var12, var14, p_74882_2_);
                    }
                }
            }
        }
    }

    protected void func_151551_a(World p_151551_1_, StructureBoundingBox p_151551_2_, Random p_151551_3_, float p_151551_4_, int p_151551_5_, int p_151551_6_, int p_151551_7_, int p_151551_8_, int p_151551_9_, int p_151551_10_, Block p_151551_11_, Block p_151551_12_, boolean p_151551_13_)
    {
        for (int var14 = p_151551_6_; var14 <= p_151551_9_; ++var14)
        {
            for (int var15 = p_151551_5_; var15 <= p_151551_8_; ++var15)
            {
                for (int var16 = p_151551_7_; var16 <= p_151551_10_; ++var16)
                {
                    if (p_151551_3_.nextFloat() <= p_151551_4_ && (!p_151551_13_ || this.func_151548_a(p_151551_1_, var15, var14, var16, p_151551_2_).getMaterial() != Material.air))
                    {
                        if (var14 != p_151551_6_ && var14 != p_151551_9_ && var15 != p_151551_5_ && var15 != p_151551_8_ && var16 != p_151551_7_ && var16 != p_151551_10_)
                        {
                            this.func_151550_a(p_151551_1_, p_151551_12_, 0, var15, var14, var16, p_151551_2_);
                        }
                        else
                        {
                            this.func_151550_a(p_151551_1_, p_151551_11_, 0, var15, var14, var16, p_151551_2_);
                        }
                    }
                }
            }
        }
    }

    protected void func_151552_a(World p_151552_1_, StructureBoundingBox p_151552_2_, Random p_151552_3_, float p_151552_4_, int p_151552_5_, int p_151552_6_, int p_151552_7_, Block p_151552_8_, int p_151552_9_)
    {
        if (p_151552_3_.nextFloat() < p_151552_4_)
        {
            this.func_151550_a(p_151552_1_, p_151552_8_, p_151552_9_, p_151552_5_, p_151552_6_, p_151552_7_, p_151552_2_);
        }
    }

    protected void func_151547_a(World p_151547_1_, StructureBoundingBox p_151547_2_, int p_151547_3_, int p_151547_4_, int p_151547_5_, int p_151547_6_, int p_151547_7_, int p_151547_8_, Block p_151547_9_, boolean p_151547_10_)
    {
        float var11 = (float)(p_151547_6_ - p_151547_3_ + 1);
        float var12 = (float)(p_151547_7_ - p_151547_4_ + 1);
        float var13 = (float)(p_151547_8_ - p_151547_5_ + 1);
        float var14 = (float)p_151547_3_ + var11 / 2.0F;
        float var15 = (float)p_151547_5_ + var13 / 2.0F;

        for (int var16 = p_151547_4_; var16 <= p_151547_7_; ++var16)
        {
            float var17 = (float)(var16 - p_151547_4_) / var12;

            for (int var18 = p_151547_3_; var18 <= p_151547_6_; ++var18)
            {
                float var19 = ((float)var18 - var14) / (var11 * 0.5F);

                for (int var20 = p_151547_5_; var20 <= p_151547_8_; ++var20)
                {
                    float var21 = ((float)var20 - var15) / (var13 * 0.5F);

                    if (!p_151547_10_ || this.func_151548_a(p_151547_1_, var18, var16, var20, p_151547_2_).getMaterial() != Material.air)
                    {
                        float var22 = var19 * var19 + var17 * var17 + var21 * var21;

                        if (var22 <= 1.05F)
                        {
                            this.func_151550_a(p_151547_1_, p_151547_9_, 0, var18, var16, var20, p_151547_2_);
                        }
                    }
                }
            }
        }
    }

    /**
     * Deletes all continuous blocks from selected position upwards. Stops at hitting air.
     */
    protected void clearCurrentPositionBlocksUpwards(World p_74871_1_, int p_74871_2_, int p_74871_3_, int p_74871_4_, StructureBoundingBox p_74871_5_)
    {
        int var6 = this.getXWithOffset(p_74871_2_, p_74871_4_);
        int var7 = this.getYWithOffset(p_74871_3_);
        int var8 = this.getZWithOffset(p_74871_2_, p_74871_4_);

        if (p_74871_5_.isVecInside(var6, var7, var8))
        {
            while (!p_74871_1_.isAirBlock(var6, var7, var8) && var7 < 255)
            {
                p_74871_1_.setBlock(var6, var7, var8, Blocks.air, 0, 2);
                ++var7;
            }
        }
    }

    protected void func_151554_b(World p_151554_1_, Block p_151554_2_, int p_151554_3_, int p_151554_4_, int p_151554_5_, int p_151554_6_, StructureBoundingBox p_151554_7_)
    {
        int var8 = this.getXWithOffset(p_151554_4_, p_151554_6_);
        int var9 = this.getYWithOffset(p_151554_5_);
        int var10 = this.getZWithOffset(p_151554_4_, p_151554_6_);

        if (p_151554_7_.isVecInside(var8, var9, var10))
        {
            while ((p_151554_1_.isAirBlock(var8, var9, var10) || p_151554_1_.getBlock(var8, var9, var10).getMaterial().isLiquid()) && var9 > 1)
            {
                p_151554_1_.setBlock(var8, var9, var10, p_151554_2_, p_151554_3_, 2);
                --var9;
            }
        }
    }

    /**
     * Used to generate chests with items in it. ex: Temple Chests, Village Blacksmith Chests, Mineshaft Chests.
     */
    protected boolean generateStructureChestContents(World p_74879_1_, StructureBoundingBox p_74879_2_, Random p_74879_3_, int p_74879_4_, int p_74879_5_, int p_74879_6_, WeightedRandomChestContent[] p_74879_7_, int p_74879_8_)
    {
        int var9 = this.getXWithOffset(p_74879_4_, p_74879_6_);
        int var10 = this.getYWithOffset(p_74879_5_);
        int var11 = this.getZWithOffset(p_74879_4_, p_74879_6_);

        if (p_74879_2_.isVecInside(var9, var10, var11) && p_74879_1_.getBlock(var9, var10, var11) != Blocks.chest)
        {
            p_74879_1_.setBlock(var9, var10, var11, Blocks.chest, 0, 2);
            TileEntityChest var12 = (TileEntityChest)p_74879_1_.getTileEntity(var9, var10, var11);

            if (var12 != null)
            {
                WeightedRandomChestContent.generateChestContents(p_74879_3_, p_74879_7_, var12, p_74879_8_);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Used to generate dispenser contents for structures. ex: Jungle Temples.
     */
    protected boolean generateStructureDispenserContents(World p_74869_1_, StructureBoundingBox p_74869_2_, Random p_74869_3_, int p_74869_4_, int p_74869_5_, int p_74869_6_, int p_74869_7_, WeightedRandomChestContent[] p_74869_8_, int p_74869_9_)
    {
        int var10 = this.getXWithOffset(p_74869_4_, p_74869_6_);
        int var11 = this.getYWithOffset(p_74869_5_);
        int var12 = this.getZWithOffset(p_74869_4_, p_74869_6_);

        if (p_74869_2_.isVecInside(var10, var11, var12) && p_74869_1_.getBlock(var10, var11, var12) != Blocks.dispenser)
        {
            p_74869_1_.setBlock(var10, var11, var12, Blocks.dispenser, this.func_151555_a(Blocks.dispenser, p_74869_7_), 2);
            TileEntityDispenser var13 = (TileEntityDispenser)p_74869_1_.getTileEntity(var10, var11, var12);

            if (var13 != null)
            {
                WeightedRandomChestContent.func_150706_a(p_74869_3_, p_74869_8_, var13, p_74869_9_);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void placeDoorAtCurrentPosition(World p_74881_1_, StructureBoundingBox p_74881_2_, Random p_74881_3_, int p_74881_4_, int p_74881_5_, int p_74881_6_, int p_74881_7_)
    {
        int var8 = this.getXWithOffset(p_74881_4_, p_74881_6_);
        int var9 = this.getYWithOffset(p_74881_5_);
        int var10 = this.getZWithOffset(p_74881_4_, p_74881_6_);

        if (p_74881_2_.isVecInside(var8, var9, var10))
        {
            ItemDoor.func_150924_a(p_74881_1_, var8, var9, var10, p_74881_7_, Blocks.wooden_door);
        }
    }

    public abstract static class BlockSelector
    {
        protected Block field_151562_a;
        protected int selectedBlockMetaData;


        protected BlockSelector()
        {
            this.field_151562_a = Blocks.air;
        }

        public abstract void selectBlocks(Random p_75062_1_, int p_75062_2_, int p_75062_3_, int p_75062_4_, boolean p_75062_5_);

        public Block func_151561_a()
        {
            return this.field_151562_a;
        }

        public int getSelectedBlockMetaData()
        {
            return this.selectedBlockMetaData;
        }
    }
}
