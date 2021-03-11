package net.minecraft.world.chunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk
{
    private static final Logger logger = LogManager.getLogger();

    /**
     * Determines if the chunk is lit or not at a light value greater than 0.
     */
    public static boolean isLit;

    /**
     * Used to store block IDs, block MSBs, Sky-light maps, Block-light maps, and metadata. Each entry corresponds to a
     * logical segment of 16x16x16 blocks, stacked vertically.
     */
    private ExtendedBlockStorage[] storageArrays;

    /**
     * Contains a 16x16 mapping on the X/Z plane of the biome ID to which each colum belongs.
     */
    private byte[] blockBiomeArray;

    /**
     * A map, similar to heightMap, that tracks how far down precipitation can fall.
     */
    public int[] precipitationHeightMap;

    /** Which columns need their skylightMaps updated. */
    public boolean[] updateSkylightColumns;

    /** Whether or not this Chunk is currently loaded into the World */
    public boolean isChunkLoaded;

    /** Reference to the World object. */
    public World worldObj;
    public int[] heightMap;

    /** The x coordinate of the chunk. */
    public final int xPosition;

    /** The z coordinate of the chunk. */
    public final int zPosition;
    private boolean isGapLightingUpdated;

    /** A Map of ChunkPositions to TileEntities in this chunk */
    public Map chunkTileEntityMap;

    /**
     * Array of Lists containing the entities in this Chunk. Each List represents a 16 block subchunk.
     */
    public List[] entityLists;

    /** Boolean value indicating if the terrain is populated. */
    public boolean isTerrainPopulated;
    public boolean isLightPopulated;
    public boolean field_150815_m;

    /**
     * Set to true if the chunk has been modified and needs to be updated internally.
     */
    public boolean isModified;

    /**
     * Whether this Chunk has any Entities and thus requires saving on every tick
     */
    public boolean hasEntities;

    /** The time according to World.worldTime when this chunk was last saved */
    public long lastSaveTime;

    /**
     * Updates to this chunk will not be sent to clients if this is false. This field is set to true the first time the
     * chunk is sent to a com.cheatbreaker.client, and never set to false.
     */
    public boolean sendUpdates;

    /** Lowest value in the heightmap. */
    public int heightMapMinimum;

    /** the cumulative number of ticks players have been in this chunk */
    public long inhabitedTime;

    /**
     * Contains the current round-robin relight check index, and is implied as the relight check location as well.
     */
    private int queuedLightChecks;


    public Chunk(World p_i1995_1_, int p_i1995_2_, int p_i1995_3_)
    {
        this.storageArrays = new ExtendedBlockStorage[16];
        this.blockBiomeArray = new byte[256];
        this.precipitationHeightMap = new int[256];
        this.updateSkylightColumns = new boolean[256];
        this.chunkTileEntityMap = new HashMap();
        this.queuedLightChecks = 4096;
        this.entityLists = new List[16];
        this.worldObj = p_i1995_1_;
        this.xPosition = p_i1995_2_;
        this.zPosition = p_i1995_3_;
        this.heightMap = new int[256];

        for (int var4 = 0; var4 < this.entityLists.length; ++var4)
        {
            this.entityLists[var4] = new ArrayList();
        }

        Arrays.fill(this.precipitationHeightMap, -999);
        Arrays.fill(this.blockBiomeArray, (byte) - 1);
    }

    public Chunk(World p_i45446_1_, Block[] p_i45446_2_, int p_i45446_3_, int p_i45446_4_)
    {
        this(p_i45446_1_, p_i45446_3_, p_i45446_4_);
        int var5 = p_i45446_2_.length / 256;
        boolean var6 = !p_i45446_1_.provider.hasNoSky;

        for (int var7 = 0; var7 < 16; ++var7)
        {
            for (int var8 = 0; var8 < 16; ++var8)
            {
                for (int var9 = 0; var9 < var5; ++var9)
                {
                    Block var10 = p_i45446_2_[var7 << 11 | var8 << 7 | var9];

                    if (var10 != null && var10.getMaterial() != Material.air)
                    {
                        int var11 = var9 >> 4;

                        if (this.storageArrays[var11] == null)
                        {
                            this.storageArrays[var11] = new ExtendedBlockStorage(var11 << 4, var6);
                        }

                        this.storageArrays[var11].func_150818_a(var7, var9 & 15, var8, var10);
                    }
                }
            }
        }
    }

    public Chunk(World p_i45447_1_, Block[] p_i45447_2_, byte[] p_i45447_3_, int p_i45447_4_, int p_i45447_5_)
    {
        this(p_i45447_1_, p_i45447_4_, p_i45447_5_);
        int var6 = p_i45447_2_.length / 256;
        boolean var7 = !p_i45447_1_.provider.hasNoSky;

        for (int var8 = 0; var8 < 16; ++var8)
        {
            for (int var9 = 0; var9 < 16; ++var9)
            {
                for (int var10 = 0; var10 < var6; ++var10)
                {
                    int var11 = var8 * var6 * 16 | var9 * var6 | var10;
                    Block var12 = p_i45447_2_[var11];

                    if (var12 != null && var12 != Blocks.air)
                    {
                        int var13 = var10 >> 4;

                        if (this.storageArrays[var13] == null)
                        {
                            this.storageArrays[var13] = new ExtendedBlockStorage(var13 << 4, var7);
                        }

                        this.storageArrays[var13].func_150818_a(var8, var10 & 15, var9, var12);
                        this.storageArrays[var13].setExtBlockMetadata(var8, var10 & 15, var9, p_i45447_3_[var11]);
                    }
                }
            }
        }
    }

    /**
     * Checks whether the chunk is at the X/Z location specified
     */
    public boolean isAtLocation(int p_76600_1_, int p_76600_2_)
    {
        return p_76600_1_ == this.xPosition && p_76600_2_ == this.zPosition;
    }

    /**
     * Returns the value in the height map at this x, z coordinate in the chunk
     */
    public int getHeightValue(int p_76611_1_, int p_76611_2_)
    {
        return this.heightMap[p_76611_2_ << 4 | p_76611_1_];
    }

    /**
     * Returns the topmost ExtendedBlockStorage instance for this Chunk that actually contains a block.
     */
    public int getTopFilledSegment()
    {
        for (int var1 = this.storageArrays.length - 1; var1 >= 0; --var1)
        {
            if (this.storageArrays[var1] != null)
            {
                return this.storageArrays[var1].getYLocation();
            }
        }

        return 0;
    }

    /**
     * Returns the ExtendedBlockStorage array for this Chunk.
     */
    public ExtendedBlockStorage[] getBlockStorageArray()
    {
        return this.storageArrays;
    }

    /**
     * Generates the height map for a chunk from scratch
     */
    public void generateHeightMap()
    {
        int var1 = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;

        for (int var2 = 0; var2 < 16; ++var2)
        {
            int var3 = 0;

            while (var3 < 16)
            {
                this.precipitationHeightMap[var2 + (var3 << 4)] = -999;
                int var4 = var1 + 16 - 1;

                while (true)
                {
                    if (var4 > 0)
                    {
                        Block var5 = this.func_150810_a(var2, var4 - 1, var3);

                        if (var5.getLightOpacity() == 0)
                        {
                            --var4;
                            continue;
                        }

                        this.heightMap[var3 << 4 | var2] = var4;

                        if (var4 < this.heightMapMinimum)
                        {
                            this.heightMapMinimum = var4;
                        }
                    }

                    ++var3;
                    break;
                }
            }
        }

        this.isModified = true;
    }

    /**
     * Generates the initial skylight map for the chunk upon generation or load.
     */
    public void generateSkylightMap()
    {
        int var1 = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;

        for (int var2 = 0; var2 < 16; ++var2)
        {
            int var3 = 0;

            while (var3 < 16)
            {
                this.precipitationHeightMap[var2 + (var3 << 4)] = -999;
                int var4 = var1 + 16 - 1;

                while (true)
                {
                    if (var4 > 0)
                    {
                        if (this.func_150808_b(var2, var4 - 1, var3) == 0)
                        {
                            --var4;
                            continue;
                        }

                        this.heightMap[var3 << 4 | var2] = var4;

                        if (var4 < this.heightMapMinimum)
                        {
                            this.heightMapMinimum = var4;
                        }
                    }

                    if (!this.worldObj.provider.hasNoSky)
                    {
                        var4 = 15;
                        int var5 = var1 + 16 - 1;

                        do
                        {
                            int var6 = this.func_150808_b(var2, var5, var3);

                            if (var6 == 0 && var4 != 15)
                            {
                                var6 = 1;
                            }

                            var4 -= var6;

                            if (var4 > 0)
                            {
                                ExtendedBlockStorage var7 = this.storageArrays[var5 >> 4];

                                if (var7 != null)
                                {
                                    var7.setExtSkylightValue(var2, var5 & 15, var3, var4);
                                    this.worldObj.func_147479_m((this.xPosition << 4) + var2, var5, (this.zPosition << 4) + var3);
                                }
                            }

                            --var5;
                        }
                        while (var5 > 0 && var4 > 0);
                    }

                    ++var3;
                    break;
                }
            }
        }

        this.isModified = true;
    }

    /**
     * Propagates a given sky-visible block's light value downward and upward to neighboring blocks as necessary.
     */
    private void propagateSkylightOcclusion(int p_76595_1_, int p_76595_2_)
    {
        this.updateSkylightColumns[p_76595_1_ + p_76595_2_ * 16] = true;
        this.isGapLightingUpdated = true;
    }

    private void recheckGaps(boolean p_150803_1_)
    {
        this.worldObj.theProfiler.startSection("recheckGaps");

        if (this.worldObj.doChunksNearChunkExist(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8, 16))
        {
            for (int var2 = 0; var2 < 16; ++var2)
            {
                for (int var3 = 0; var3 < 16; ++var3)
                {
                    if (this.updateSkylightColumns[var2 + var3 * 16])
                    {
                        this.updateSkylightColumns[var2 + var3 * 16] = false;
                        int var4 = this.getHeightValue(var2, var3);
                        int var5 = this.xPosition * 16 + var2;
                        int var6 = this.zPosition * 16 + var3;
                        int var7 = this.worldObj.getChunkHeightMapMinimum(var5 - 1, var6);
                        int var8 = this.worldObj.getChunkHeightMapMinimum(var5 + 1, var6);
                        int var9 = this.worldObj.getChunkHeightMapMinimum(var5, var6 - 1);
                        int var10 = this.worldObj.getChunkHeightMapMinimum(var5, var6 + 1);

                        if (var8 < var7)
                        {
                            var7 = var8;
                        }

                        if (var9 < var7)
                        {
                            var7 = var9;
                        }

                        if (var10 < var7)
                        {
                            var7 = var10;
                        }

                        this.checkSkylightNeighborHeight(var5, var6, var7);
                        this.checkSkylightNeighborHeight(var5 - 1, var6, var4);
                        this.checkSkylightNeighborHeight(var5 + 1, var6, var4);
                        this.checkSkylightNeighborHeight(var5, var6 - 1, var4);
                        this.checkSkylightNeighborHeight(var5, var6 + 1, var4);

                        if (p_150803_1_)
                        {
                            this.worldObj.theProfiler.endSection();
                            return;
                        }
                    }
                }
            }

            this.isGapLightingUpdated = false;
        }

        this.worldObj.theProfiler.endSection();
    }

    /**
     * Checks the height of a block next to a sky-visible block and schedules a lighting update as necessary.
     */
    private void checkSkylightNeighborHeight(int p_76599_1_, int p_76599_2_, int p_76599_3_)
    {
        int var4 = this.worldObj.getHeightValue(p_76599_1_, p_76599_2_);

        if (var4 > p_76599_3_)
        {
            this.updateSkylightNeighborHeight(p_76599_1_, p_76599_2_, p_76599_3_, var4 + 1);
        }
        else if (var4 < p_76599_3_)
        {
            this.updateSkylightNeighborHeight(p_76599_1_, p_76599_2_, var4, p_76599_3_ + 1);
        }
    }

    private void updateSkylightNeighborHeight(int p_76609_1_, int p_76609_2_, int p_76609_3_, int p_76609_4_)
    {
        if (p_76609_4_ > p_76609_3_ && this.worldObj.doChunksNearChunkExist(p_76609_1_, 0, p_76609_2_, 16))
        {
            for (int var5 = p_76609_3_; var5 < p_76609_4_; ++var5)
            {
                this.worldObj.updateLightByType(EnumSkyBlock.Sky, p_76609_1_, var5, p_76609_2_);
            }

            this.isModified = true;
        }
    }

    /**
     * Initiates the recalculation of both the block-light and sky-light for a given block inside a chunk.
     */
    private void relightBlock(int p_76615_1_, int p_76615_2_, int p_76615_3_)
    {
        int var4 = this.heightMap[p_76615_3_ << 4 | p_76615_1_] & 255;
        int var5 = var4;

        if (p_76615_2_ > var4)
        {
            var5 = p_76615_2_;
        }

        while (var5 > 0 && this.func_150808_b(p_76615_1_, var5 - 1, p_76615_3_) == 0)
        {
            --var5;
        }

        if (var5 != var4)
        {
            this.worldObj.markBlocksDirtyVertical(p_76615_1_ + this.xPosition * 16, p_76615_3_ + this.zPosition * 16, var5, var4);
            this.heightMap[p_76615_3_ << 4 | p_76615_1_] = var5;
            int var6 = this.xPosition * 16 + p_76615_1_;
            int var7 = this.zPosition * 16 + p_76615_3_;
            int var8;
            int var12;

            if (!this.worldObj.provider.hasNoSky)
            {
                ExtendedBlockStorage var9;

                if (var5 < var4)
                {
                    for (var8 = var5; var8 < var4; ++var8)
                    {
                        var9 = this.storageArrays[var8 >> 4];

                        if (var9 != null)
                        {
                            var9.setExtSkylightValue(p_76615_1_, var8 & 15, p_76615_3_, 15);
                            this.worldObj.func_147479_m((this.xPosition << 4) + p_76615_1_, var8, (this.zPosition << 4) + p_76615_3_);
                        }
                    }
                }
                else
                {
                    for (var8 = var4; var8 < var5; ++var8)
                    {
                        var9 = this.storageArrays[var8 >> 4];

                        if (var9 != null)
                        {
                            var9.setExtSkylightValue(p_76615_1_, var8 & 15, p_76615_3_, 0);
                            this.worldObj.func_147479_m((this.xPosition << 4) + p_76615_1_, var8, (this.zPosition << 4) + p_76615_3_);
                        }
                    }
                }

                var8 = 15;

                while (var5 > 0 && var8 > 0)
                {
                    --var5;
                    var12 = this.func_150808_b(p_76615_1_, var5, p_76615_3_);

                    if (var12 == 0)
                    {
                        var12 = 1;
                    }

                    var8 -= var12;

                    if (var8 < 0)
                    {
                        var8 = 0;
                    }

                    ExtendedBlockStorage var10 = this.storageArrays[var5 >> 4];

                    if (var10 != null)
                    {
                        var10.setExtSkylightValue(p_76615_1_, var5 & 15, p_76615_3_, var8);
                    }
                }
            }

            var8 = this.heightMap[p_76615_3_ << 4 | p_76615_1_];
            var12 = var4;
            int var13 = var8;

            if (var8 < var4)
            {
                var12 = var8;
                var13 = var4;
            }

            if (var8 < this.heightMapMinimum)
            {
                this.heightMapMinimum = var8;
            }

            if (!this.worldObj.provider.hasNoSky)
            {
                this.updateSkylightNeighborHeight(var6 - 1, var7, var12, var13);
                this.updateSkylightNeighborHeight(var6 + 1, var7, var12, var13);
                this.updateSkylightNeighborHeight(var6, var7 - 1, var12, var13);
                this.updateSkylightNeighborHeight(var6, var7 + 1, var12, var13);
                this.updateSkylightNeighborHeight(var6, var7, var12, var13);
            }

            this.isModified = true;
        }
    }

    public int func_150808_b(int p_150808_1_, int p_150808_2_, int p_150808_3_)
    {
        return this.func_150810_a(p_150808_1_, p_150808_2_, p_150808_3_).getLightOpacity();
    }

    public Block func_150810_a(final int p_150810_1_, final int p_150810_2_, final int p_150810_3_)
    {
        Block var4 = Blocks.air;

        if (p_150810_2_ >> 4 < this.storageArrays.length)
        {
            ExtendedBlockStorage var5 = this.storageArrays[p_150810_2_ >> 4];

            if (var5 != null)
            {
                try
                {
                    var4 = var5.func_150819_a(p_150810_1_, p_150810_2_ & 15, p_150810_3_);
                }
                catch (Throwable var9)
                {
                    CrashReport var7 = CrashReport.makeCrashReport(var9, "Getting block");
                    CrashReportCategory var8 = var7.makeCategory("Block being got");
                    var8.addCrashSectionCallable("Location", new Callable()
                    {

                        public String call()
                        {
                            return CrashReportCategory.getLocationInfo(p_150810_1_, p_150810_2_, p_150810_3_);
                        }
                    });
                    throw new ReportedException(var7);
                }
            }
        }

        return var4;
    }

    /**
     * Return the metadata corresponding to the given coordinates inside a chunk.
     */
    public int getBlockMetadata(int p_76628_1_, int p_76628_2_, int p_76628_3_)
    {
        if (p_76628_2_ >> 4 >= this.storageArrays.length)
        {
            return 0;
        }
        else
        {
            ExtendedBlockStorage var4 = this.storageArrays[p_76628_2_ >> 4];
            return var4 != null ? var4.getExtBlockMetadata(p_76628_1_, p_76628_2_ & 15, p_76628_3_) : 0;
        }
    }

    public boolean func_150807_a(int p_150807_1_, int p_150807_2_, int p_150807_3_, Block p_150807_4_, int p_150807_5_)
    {
        int var6 = p_150807_3_ << 4 | p_150807_1_;

        if (p_150807_2_ >= this.precipitationHeightMap[var6] - 1)
        {
            this.precipitationHeightMap[var6] = -999;
        }

        int var7 = this.heightMap[var6];
        Block var8 = this.func_150810_a(p_150807_1_, p_150807_2_, p_150807_3_);
        int var9 = this.getBlockMetadata(p_150807_1_, p_150807_2_, p_150807_3_);

        if (var8 == p_150807_4_ && var9 == p_150807_5_)
        {
            return false;
        }
        else
        {
            ExtendedBlockStorage var10 = this.storageArrays[p_150807_2_ >> 4];
            boolean var11 = false;

            if (var10 == null)
            {
                if (p_150807_4_ == Blocks.air)
                {
                    return false;
                }

                var10 = this.storageArrays[p_150807_2_ >> 4] = new ExtendedBlockStorage(p_150807_2_ >> 4 << 4, !this.worldObj.provider.hasNoSky);
                var11 = p_150807_2_ >= var7;
            }

            int var12 = this.xPosition * 16 + p_150807_1_;
            int var13 = this.zPosition * 16 + p_150807_3_;

            if (!this.worldObj.isClient)
            {
                var8.onBlockPreDestroy(this.worldObj, var12, p_150807_2_, var13, var9);
            }

            var10.func_150818_a(p_150807_1_, p_150807_2_ & 15, p_150807_3_, p_150807_4_);

            if (!this.worldObj.isClient)
            {
                var8.breakBlock(this.worldObj, var12, p_150807_2_, var13, var8, var9);
            }
            else if (var8 instanceof ITileEntityProvider && var8 != p_150807_4_)
            {
                this.worldObj.removeTileEntity(var12, p_150807_2_, var13);
            }

            if (var10.func_150819_a(p_150807_1_, p_150807_2_ & 15, p_150807_3_) != p_150807_4_)
            {
                return false;
            }
            else
            {
                var10.setExtBlockMetadata(p_150807_1_, p_150807_2_ & 15, p_150807_3_, p_150807_5_);

                if (var11)
                {
                    this.generateSkylightMap();
                }
                else
                {
                    int var14 = p_150807_4_.getLightOpacity();
                    int var15 = var8.getLightOpacity();

                    if (var14 > 0)
                    {
                        if (p_150807_2_ >= var7)
                        {
                            this.relightBlock(p_150807_1_, p_150807_2_ + 1, p_150807_3_);
                        }
                    }
                    else if (p_150807_2_ == var7 - 1)
                    {
                        this.relightBlock(p_150807_1_, p_150807_2_, p_150807_3_);
                    }

                    if (var14 != var15 && (var14 < var15 || this.getSavedLightValue(EnumSkyBlock.Sky, p_150807_1_, p_150807_2_, p_150807_3_) > 0 || this.getSavedLightValue(EnumSkyBlock.Block, p_150807_1_, p_150807_2_, p_150807_3_) > 0))
                    {
                        this.propagateSkylightOcclusion(p_150807_1_, p_150807_3_);
                    }
                }

                TileEntity var16;

                if (var8 instanceof ITileEntityProvider)
                {
                    var16 = this.func_150806_e(p_150807_1_, p_150807_2_, p_150807_3_);

                    if (var16 != null)
                    {
                        var16.updateContainingBlockInfo();
                    }
                }

                if (!this.worldObj.isClient)
                {
                    p_150807_4_.onBlockAdded(this.worldObj, var12, p_150807_2_, var13);
                }

                if (p_150807_4_ instanceof ITileEntityProvider)
                {
                    var16 = this.func_150806_e(p_150807_1_, p_150807_2_, p_150807_3_);

                    if (var16 == null)
                    {
                        var16 = ((ITileEntityProvider)p_150807_4_).createNewTileEntity(this.worldObj, p_150807_5_);
                        this.worldObj.setTileEntity(var12, p_150807_2_, var13, var16);
                    }

                    if (var16 != null)
                    {
                        var16.updateContainingBlockInfo();
                    }
                }

                this.isModified = true;
                return true;
            }
        }
    }

    /**
     * Set the metadata of a block in the chunk
     */
    public boolean setBlockMetadata(int p_76589_1_, int p_76589_2_, int p_76589_3_, int p_76589_4_)
    {
        ExtendedBlockStorage var5 = this.storageArrays[p_76589_2_ >> 4];

        if (var5 == null)
        {
            return false;
        }
        else
        {
            int var6 = var5.getExtBlockMetadata(p_76589_1_, p_76589_2_ & 15, p_76589_3_);

            if (var6 == p_76589_4_)
            {
                return false;
            }
            else
            {
                this.isModified = true;
                var5.setExtBlockMetadata(p_76589_1_, p_76589_2_ & 15, p_76589_3_, p_76589_4_);

                if (var5.func_150819_a(p_76589_1_, p_76589_2_ & 15, p_76589_3_) instanceof ITileEntityProvider)
                {
                    TileEntity var7 = this.func_150806_e(p_76589_1_, p_76589_2_, p_76589_3_);

                    if (var7 != null)
                    {
                        var7.updateContainingBlockInfo();
                        var7.blockMetadata = p_76589_4_;
                    }
                }

                return true;
            }
        }
    }

    /**
     * Gets the amount of light saved in this block (doesn't adjust for daylight)
     */
    public int getSavedLightValue(EnumSkyBlock p_76614_1_, int p_76614_2_, int p_76614_3_, int p_76614_4_)
    {
        ExtendedBlockStorage var5 = this.storageArrays[p_76614_3_ >> 4];
        return var5 == null ? (this.canBlockSeeTheSky(p_76614_2_, p_76614_3_, p_76614_4_) ? p_76614_1_.defaultLightValue : 0) : (p_76614_1_ == EnumSkyBlock.Sky ? (this.worldObj.provider.hasNoSky ? 0 : var5.getExtSkylightValue(p_76614_2_, p_76614_3_ & 15, p_76614_4_)) : (p_76614_1_ == EnumSkyBlock.Block ? var5.getExtBlocklightValue(p_76614_2_, p_76614_3_ & 15, p_76614_4_) : p_76614_1_.defaultLightValue));
    }

    /**
     * Sets the light value at the coordinate. If enumskyblock is set to sky it sets it in the skylightmap and if its a
     * block then into the blocklightmap. Args enumSkyBlock, x, y, z, lightValue
     */
    public void setLightValue(EnumSkyBlock p_76633_1_, int p_76633_2_, int p_76633_3_, int p_76633_4_, int p_76633_5_)
    {
        ExtendedBlockStorage var6 = this.storageArrays[p_76633_3_ >> 4];

        if (var6 == null)
        {
            var6 = this.storageArrays[p_76633_3_ >> 4] = new ExtendedBlockStorage(p_76633_3_ >> 4 << 4, !this.worldObj.provider.hasNoSky);
            this.generateSkylightMap();
        }

        this.isModified = true;

        if (p_76633_1_ == EnumSkyBlock.Sky)
        {
            if (!this.worldObj.provider.hasNoSky)
            {
                var6.setExtSkylightValue(p_76633_2_, p_76633_3_ & 15, p_76633_4_, p_76633_5_);
            }
        }
        else if (p_76633_1_ == EnumSkyBlock.Block)
        {
            var6.setExtBlocklightValue(p_76633_2_, p_76633_3_ & 15, p_76633_4_, p_76633_5_);
        }
    }

    /**
     * Gets the amount of light on a block taking into account sunlight
     */
    public int getBlockLightValue(int p_76629_1_, int p_76629_2_, int p_76629_3_, int p_76629_4_)
    {
        ExtendedBlockStorage var5 = this.storageArrays[p_76629_2_ >> 4];

        if (var5 == null)
        {
            return !this.worldObj.provider.hasNoSky && p_76629_4_ < EnumSkyBlock.Sky.defaultLightValue ? EnumSkyBlock.Sky.defaultLightValue - p_76629_4_ : 0;
        }
        else
        {
            int var6 = this.worldObj.provider.hasNoSky ? 0 : var5.getExtSkylightValue(p_76629_1_, p_76629_2_ & 15, p_76629_3_);

            if (var6 > 0)
            {
                isLit = true;
            }

            var6 -= p_76629_4_;
            int var7 = var5.getExtBlocklightValue(p_76629_1_, p_76629_2_ & 15, p_76629_3_);

            if (var7 > var6)
            {
                var6 = var7;
            }

            return var6;
        }
    }

    /**
     * Adds an entity to the chunk. Args: entity
     */
    public void addEntity(Entity p_76612_1_)
    {
        this.hasEntities = true;
        int var2 = MathHelper.floor_double(p_76612_1_.posX / 16.0D);
        int var3 = MathHelper.floor_double(p_76612_1_.posZ / 16.0D);

        if (var2 != this.xPosition || var3 != this.zPosition)
        {
            logger.warn("Wrong location! " + p_76612_1_ + " (at " + var2 + ", " + var3 + " instead of " + this.xPosition + ", " + this.zPosition + ")");
            Thread.dumpStack();
        }

        int var4 = MathHelper.floor_double(p_76612_1_.posY / 16.0D);

        if (var4 < 0)
        {
            var4 = 0;
        }

        if (var4 >= this.entityLists.length)
        {
            var4 = this.entityLists.length - 1;
        }

        p_76612_1_.addedToChunk = true;
        p_76612_1_.chunkCoordX = this.xPosition;
        p_76612_1_.chunkCoordY = var4;
        p_76612_1_.chunkCoordZ = this.zPosition;
        this.entityLists[var4].add(p_76612_1_);
    }

    /**
     * removes entity using its y chunk coordinate as its index
     */
    public void removeEntity(Entity p_76622_1_)
    {
        this.removeEntityAtIndex(p_76622_1_, p_76622_1_.chunkCoordY);
    }

    /**
     * Removes entity at the specified index from the entity array.
     */
    public void removeEntityAtIndex(Entity p_76608_1_, int p_76608_2_)
    {
        if (p_76608_2_ < 0)
        {
            p_76608_2_ = 0;
        }

        if (p_76608_2_ >= this.entityLists.length)
        {
            p_76608_2_ = this.entityLists.length - 1;
        }

        this.entityLists[p_76608_2_].remove(p_76608_1_);
    }

    /**
     * Returns whether is not a block above this one blocking sight to the sky (done via checking against the heightmap)
     */
    public boolean canBlockSeeTheSky(int p_76619_1_, int p_76619_2_, int p_76619_3_)
    {
        return p_76619_2_ >= this.heightMap[p_76619_3_ << 4 | p_76619_1_];
    }

    public TileEntity func_150806_e(int p_150806_1_, int p_150806_2_, int p_150806_3_)
    {
        ChunkPosition var4 = new ChunkPosition(p_150806_1_, p_150806_2_, p_150806_3_);
        TileEntity var5 = (TileEntity)this.chunkTileEntityMap.get(var4);

        if (var5 == null)
        {
            Block var6 = this.func_150810_a(p_150806_1_, p_150806_2_, p_150806_3_);

            if (!var6.hasTileEntity())
            {
                return null;
            }

            var5 = ((ITileEntityProvider)var6).createNewTileEntity(this.worldObj, this.getBlockMetadata(p_150806_1_, p_150806_2_, p_150806_3_));
            this.worldObj.setTileEntity(this.xPosition * 16 + p_150806_1_, p_150806_2_, this.zPosition * 16 + p_150806_3_, var5);
        }

        if (var5 != null && var5.isInvalid())
        {
            this.chunkTileEntityMap.remove(var4);
            return null;
        }
        else
        {
            return var5;
        }
    }

    public void addTileEntity(TileEntity p_150813_1_)
    {
        int var2 = p_150813_1_.field_145851_c - this.xPosition * 16;
        int var3 = p_150813_1_.field_145848_d;
        int var4 = p_150813_1_.field_145849_e - this.zPosition * 16;
        this.func_150812_a(var2, var3, var4, p_150813_1_);

        if (this.isChunkLoaded)
        {
            this.worldObj.field_147482_g.add(p_150813_1_);
        }
    }

    public void func_150812_a(int p_150812_1_, int p_150812_2_, int p_150812_3_, TileEntity p_150812_4_)
    {
        ChunkPosition var5 = new ChunkPosition(p_150812_1_, p_150812_2_, p_150812_3_);
        p_150812_4_.setWorldObj(this.worldObj);
        p_150812_4_.field_145851_c = this.xPosition * 16 + p_150812_1_;
        p_150812_4_.field_145848_d = p_150812_2_;
        p_150812_4_.field_145849_e = this.zPosition * 16 + p_150812_3_;

        if (this.func_150810_a(p_150812_1_, p_150812_2_, p_150812_3_) instanceof ITileEntityProvider)
        {
            if (this.chunkTileEntityMap.containsKey(var5))
            {
                ((TileEntity)this.chunkTileEntityMap.get(var5)).invalidate();
            }

            p_150812_4_.validate();
            this.chunkTileEntityMap.put(var5, p_150812_4_);
        }
    }

    public void removeTileEntity(int p_150805_1_, int p_150805_2_, int p_150805_3_)
    {
        ChunkPosition var4 = new ChunkPosition(p_150805_1_, p_150805_2_, p_150805_3_);

        if (this.isChunkLoaded)
        {
            TileEntity var5 = (TileEntity)this.chunkTileEntityMap.remove(var4);

            if (var5 != null)
            {
                var5.invalidate();
            }
        }
    }

    /**
     * Called when this Chunk is loaded by the ChunkProvider
     */
    public void onChunkLoad()
    {
        this.isChunkLoaded = true;
        this.worldObj.func_147448_a(this.chunkTileEntityMap.values());

        for (int var1 = 0; var1 < this.entityLists.length; ++var1)
        {
            Iterator var2 = this.entityLists[var1].iterator();

            while (var2.hasNext())
            {
                Entity var3 = (Entity)var2.next();
                var3.onChunkLoad();
            }

            this.worldObj.addLoadedEntities(this.entityLists[var1]);
        }
    }

    /**
     * Called when this Chunk is unloaded by the ChunkProvider
     */
    public void onChunkUnload()
    {
        this.isChunkLoaded = false;
        Iterator var1 = this.chunkTileEntityMap.values().iterator();

        while (var1.hasNext())
        {
            TileEntity var2 = (TileEntity)var1.next();
            this.worldObj.func_147457_a(var2);
        }

        for (int var3 = 0; var3 < this.entityLists.length; ++var3)
        {
            this.worldObj.unloadEntities(this.entityLists[var3]);
        }
    }

    /**
     * Sets the isModified flag for this Chunk
     */
    public void setChunkModified()
    {
        this.isModified = true;
    }

    /**
     * Fills the given list of all entities that intersect within the given bounding box that aren't the passed entity
     * Args: entity, aabb, listToFill
     */
    public void getEntitiesWithinAABBForEntity(Entity p_76588_1_, AxisAlignedBB p_76588_2_, List p_76588_3_, IEntitySelector p_76588_4_)
    {
        int var5 = MathHelper.floor_double((p_76588_2_.minY - 2.0D) / 16.0D);
        int var6 = MathHelper.floor_double((p_76588_2_.maxY + 2.0D) / 16.0D);
        var5 = MathHelper.clamp_int(var5, 0, this.entityLists.length - 1);
        var6 = MathHelper.clamp_int(var6, 0, this.entityLists.length - 1);

        for (int var7 = var5; var7 <= var6; ++var7)
        {
            List var8 = this.entityLists[var7];

            for (int var9 = 0; var9 < var8.size(); ++var9)
            {
                Entity var10 = (Entity)var8.get(var9);

                if (var10 != p_76588_1_ && var10.boundingBox.intersectsWith(p_76588_2_) && (p_76588_4_ == null || p_76588_4_.isEntityApplicable(var10)))
                {
                    p_76588_3_.add(var10);
                    Entity[] var11 = var10.getParts();

                    if (var11 != null)
                    {
                        for (int var12 = 0; var12 < var11.length; ++var12)
                        {
                            var10 = var11[var12];

                            if (var10 != p_76588_1_ && var10.boundingBox.intersectsWith(p_76588_2_) && (p_76588_4_ == null || p_76588_4_.isEntityApplicable(var10)))
                            {
                                p_76588_3_.add(var10);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets all entities that can be assigned to the specified class. Args: entityClass, aabb, listToFill
     */
    public void getEntitiesOfTypeWithinAAAB(Class p_76618_1_, AxisAlignedBB p_76618_2_, List p_76618_3_, IEntitySelector p_76618_4_)
    {
        int var5 = MathHelper.floor_double((p_76618_2_.minY - 2.0D) / 16.0D);
        int var6 = MathHelper.floor_double((p_76618_2_.maxY + 2.0D) / 16.0D);
        var5 = MathHelper.clamp_int(var5, 0, this.entityLists.length - 1);
        var6 = MathHelper.clamp_int(var6, 0, this.entityLists.length - 1);

        for (int var7 = var5; var7 <= var6; ++var7)
        {
            List var8 = this.entityLists[var7];

            for (int var9 = 0; var9 < var8.size(); ++var9)
            {
                Entity var10 = (Entity)var8.get(var9);

                if (p_76618_1_.isAssignableFrom(var10.getClass()) && var10.boundingBox.intersectsWith(p_76618_2_) && (p_76618_4_ == null || p_76618_4_.isEntityApplicable(var10)))
                {
                    p_76618_3_.add(var10);
                }
            }
        }
    }

    /**
     * Returns true if this Chunk needs to be saved
     */
    public boolean needsSaving(boolean p_76601_1_)
    {
        if (p_76601_1_)
        {
            if (this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime || this.isModified)
            {
                return true;
            }
        }
        else if (this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L)
        {
            return true;
        }

        return this.isModified;
    }

    public Random getRandomWithSeed(long p_76617_1_)
    {
        return new Random(this.worldObj.getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ p_76617_1_);
    }

    public boolean isEmpty()
    {
        return false;
    }

    public void populateChunk(IChunkProvider p_76624_1_, IChunkProvider p_76624_2_, int p_76624_3_, int p_76624_4_)
    {
        if (!this.isTerrainPopulated && p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ + 1) && p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ + 1) && p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_))
        {
            p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_);
        }

        if (p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_) && !p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_).isTerrainPopulated && p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ + 1) && p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ + 1) && p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ + 1))
        {
            p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_);
        }

        if (p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ - 1) && !p_76624_1_.provideChunk(p_76624_3_, p_76624_4_ - 1).isTerrainPopulated && p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ - 1) && p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ - 1) && p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_))
        {
            p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_ - 1);
        }

        if (p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ - 1) && !p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_ - 1).isTerrainPopulated && p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ - 1) && p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_))
        {
            p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_ - 1);
        }
    }

    /**
     * Gets the height to which rain/snow will fall. Calculates it if not already stored.
     */
    public int getPrecipitationHeight(int p_76626_1_, int p_76626_2_)
    {
        int var3 = p_76626_1_ | p_76626_2_ << 4;
        int var4 = this.precipitationHeightMap[var3];

        if (var4 == -999)
        {
            int var5 = this.getTopFilledSegment() + 15;
            var4 = -1;

            while (var5 > 0 && var4 == -1)
            {
                Block var6 = this.func_150810_a(p_76626_1_, var5, p_76626_2_);
                Material var7 = var6.getMaterial();

                if (!var7.blocksMovement() && !var7.isLiquid())
                {
                    --var5;
                }
                else
                {
                    var4 = var5 + 1;
                }
            }

            this.precipitationHeightMap[var3] = var4;
        }

        return var4;
    }

    public void func_150804_b(boolean p_150804_1_)
    {
        if (this.isGapLightingUpdated && !this.worldObj.provider.hasNoSky && !p_150804_1_)
        {
            this.recheckGaps(this.worldObj.isClient);
        }

        this.field_150815_m = true;

        if (!this.isLightPopulated && this.isTerrainPopulated)
        {
            this.func_150809_p();
        }
    }

    public boolean func_150802_k()
    {
        return this.field_150815_m && this.isTerrainPopulated && this.isLightPopulated;
    }

    /**
     * Gets a ChunkCoordIntPair representing the Chunk's position.
     */
    public ChunkCoordIntPair getChunkCoordIntPair()
    {
        return new ChunkCoordIntPair(this.xPosition, this.zPosition);
    }

    /**
     * Returns whether the ExtendedBlockStorages containing levels (in blocks) from arg 1 to arg 2 are fully empty
     * (true) or not (false).
     */
    public boolean getAreLevelsEmpty(int p_76606_1_, int p_76606_2_)
    {
        if (p_76606_1_ < 0)
        {
            p_76606_1_ = 0;
        }

        if (p_76606_2_ >= 256)
        {
            p_76606_2_ = 255;
        }

        for (int var3 = p_76606_1_; var3 <= p_76606_2_; var3 += 16)
        {
            ExtendedBlockStorage var4 = this.storageArrays[var3 >> 4];

            if (var4 != null && !var4.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public void setStorageArrays(ExtendedBlockStorage[] p_76602_1_)
    {
        this.storageArrays = p_76602_1_;
    }

    /**
     * Initialise this chunk with new binary data
     */
    public void fillChunk(byte[] p_76607_1_, int p_76607_2_, int p_76607_3_, boolean p_76607_4_)
    {
        int var5 = 0;
        boolean var6 = !this.worldObj.provider.hasNoSky;
        int var7;

        for (var7 = 0; var7 < this.storageArrays.length; ++var7)
        {
            if ((p_76607_2_ & 1 << var7) != 0)
            {
                if (this.storageArrays[var7] == null)
                {
                    this.storageArrays[var7] = new ExtendedBlockStorage(var7 << 4, var6);
                }

                byte[] var8 = this.storageArrays[var7].getBlockLSBArray();
                System.arraycopy(p_76607_1_, var5, var8, 0, var8.length);
                var5 += var8.length;
            }
            else if (p_76607_4_ && this.storageArrays[var7] != null)
            {
                this.storageArrays[var7] = null;
            }
        }

        NibbleArray var10;

        for (var7 = 0; var7 < this.storageArrays.length; ++var7)
        {
            if ((p_76607_2_ & 1 << var7) != 0 && this.storageArrays[var7] != null)
            {
                var10 = this.storageArrays[var7].getMetadataArray();
                System.arraycopy(p_76607_1_, var5, var10.data, 0, var10.data.length);
                var5 += var10.data.length;
            }
        }

        for (var7 = 0; var7 < this.storageArrays.length; ++var7)
        {
            if ((p_76607_2_ & 1 << var7) != 0 && this.storageArrays[var7] != null)
            {
                var10 = this.storageArrays[var7].getBlocklightArray();
                System.arraycopy(p_76607_1_, var5, var10.data, 0, var10.data.length);
                var5 += var10.data.length;
            }
        }

        if (var6)
        {
            for (var7 = 0; var7 < this.storageArrays.length; ++var7)
            {
                if ((p_76607_2_ & 1 << var7) != 0 && this.storageArrays[var7] != null)
                {
                    var10 = this.storageArrays[var7].getSkylightArray();
                    System.arraycopy(p_76607_1_, var5, var10.data, 0, var10.data.length);
                    var5 += var10.data.length;
                }
            }
        }

        for (var7 = 0; var7 < this.storageArrays.length; ++var7)
        {
            if ((p_76607_3_ & 1 << var7) != 0)
            {
                if (this.storageArrays[var7] == null)
                {
                    var5 += 2048;
                }
                else
                {
                    var10 = this.storageArrays[var7].getBlockMSBArray();

                    if (var10 == null)
                    {
                        var10 = this.storageArrays[var7].createBlockMSBArray();
                    }

                    System.arraycopy(p_76607_1_, var5, var10.data, 0, var10.data.length);
                    var5 += var10.data.length;
                }
            }
            else if (p_76607_4_ && this.storageArrays[var7] != null && this.storageArrays[var7].getBlockMSBArray() != null)
            {
                this.storageArrays[var7].clearMSBArray();
            }
        }

        if (p_76607_4_)
        {
            System.arraycopy(p_76607_1_, var5, this.blockBiomeArray, 0, this.blockBiomeArray.length);
            int var10000 = var5 + this.blockBiomeArray.length;
        }

        for (var7 = 0; var7 < this.storageArrays.length; ++var7)
        {
            if (this.storageArrays[var7] != null && (p_76607_2_ & 1 << var7) != 0)
            {
                this.storageArrays[var7].removeInvalidBlocks();
            }
        }

        this.isLightPopulated = true;
        this.isTerrainPopulated = true;
        this.generateHeightMap();
        Iterator var9 = this.chunkTileEntityMap.values().iterator();

        while (var9.hasNext())
        {
            TileEntity var11 = (TileEntity)var9.next();
            var11.updateContainingBlockInfo();
        }
    }

    /**
     * This method retrieves the biome at a set of coordinates
     */
    public BiomeGenBase getBiomeGenForWorldCoords(int p_76591_1_, int p_76591_2_, WorldChunkManager p_76591_3_)
    {
        int var4 = this.blockBiomeArray[p_76591_2_ << 4 | p_76591_1_] & 255;

        if (var4 == 255)
        {
            BiomeGenBase var5 = p_76591_3_.getBiomeGenAt((this.xPosition << 4) + p_76591_1_, (this.zPosition << 4) + p_76591_2_);
            var4 = var5.biomeID;
            this.blockBiomeArray[p_76591_2_ << 4 | p_76591_1_] = (byte)(var4 & 255);
        }

        return BiomeGenBase.func_150568_d(var4) == null ? BiomeGenBase.plains : BiomeGenBase.func_150568_d(var4);
    }

    /**
     * Returns an array containing a 16x16 mapping on the X/Z of block positions in this Chunk to biome IDs.
     */
    public byte[] getBiomeArray()
    {
        return this.blockBiomeArray;
    }

    /**
     * Accepts a 256-entry array that contains a 16x16 mapping on the X/Z plane of block positions in this Chunk to
     * biome IDs.
     */
    public void setBiomeArray(byte[] p_76616_1_)
    {
        this.blockBiomeArray = p_76616_1_;
    }

    /**
     * Resets the relight check index to 0 for this Chunk.
     */
    public void resetRelightChecks()
    {
        this.queuedLightChecks = 0;
    }

    /**
     * Called once-per-chunk-per-tick, and advances the round-robin relight check index per-storage-block by up to 8
     * blocks at a time. In a worst-case scenario, can potentially take up to 1.6 seconds, calculated via
     * (4096/(8*16))/20, to re-check all blocks in a chunk, which could explain both lagging light updates in certain
     * cases as well as Nether relight
     */
    public void enqueueRelightChecks()
    {
        for (int var1 = 0; var1 < 8; ++var1)
        {
            if (this.queuedLightChecks >= 4096)
            {
                return;
            }

            int var2 = this.queuedLightChecks % 16;
            int var3 = this.queuedLightChecks / 16 % 16;
            int var4 = this.queuedLightChecks / 256;
            ++this.queuedLightChecks;
            int var5 = (this.xPosition << 4) + var3;
            int var6 = (this.zPosition << 4) + var4;

            for (int var7 = 0; var7 < 16; ++var7)
            {
                int var8 = (var2 << 4) + var7;

                if (this.storageArrays[var2] == null && (var7 == 0 || var7 == 15 || var3 == 0 || var3 == 15 || var4 == 0 || var4 == 15) || this.storageArrays[var2] != null && this.storageArrays[var2].func_150819_a(var3, var7, var4).getMaterial() == Material.air)
                {
                    if (this.worldObj.getBlock(var5, var8 - 1, var6).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(var5, var8 - 1, var6);
                    }

                    if (this.worldObj.getBlock(var5, var8 + 1, var6).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(var5, var8 + 1, var6);
                    }

                    if (this.worldObj.getBlock(var5 - 1, var8, var6).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(var5 - 1, var8, var6);
                    }

                    if (this.worldObj.getBlock(var5 + 1, var8, var6).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(var5 + 1, var8, var6);
                    }

                    if (this.worldObj.getBlock(var5, var8, var6 - 1).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(var5, var8, var6 - 1);
                    }

                    if (this.worldObj.getBlock(var5, var8, var6 + 1).getLightValue() > 0)
                    {
                        this.worldObj.func_147451_t(var5, var8, var6 + 1);
                    }

                    this.worldObj.func_147451_t(var5, var8, var6);
                }
            }
        }
    }

    public void func_150809_p()
    {
        this.isTerrainPopulated = true;
        this.isLightPopulated = true;

        if (!this.worldObj.provider.hasNoSky)
        {
            if (this.worldObj.checkChunksExist(this.xPosition * 16 - 1, 0, this.zPosition * 16 - 1, this.xPosition * 16 + 1, 63, this.zPosition * 16 + 1))
            {
                for (int var1 = 0; var1 < 16; ++var1)
                {
                    for (int var2 = 0; var2 < 16; ++var2)
                    {
                        if (!this.func_150811_f(var1, var2))
                        {
                            this.isLightPopulated = false;
                            break;
                        }
                    }
                }

                if (this.isLightPopulated)
                {
                    Chunk var3 = this.worldObj.getChunkFromBlockCoords(this.xPosition * 16 - 1, this.zPosition * 16);
                    var3.func_150801_a(3);
                    var3 = this.worldObj.getChunkFromBlockCoords(this.xPosition * 16 + 16, this.zPosition * 16);
                    var3.func_150801_a(1);
                    var3 = this.worldObj.getChunkFromBlockCoords(this.xPosition * 16, this.zPosition * 16 - 1);
                    var3.func_150801_a(0);
                    var3 = this.worldObj.getChunkFromBlockCoords(this.xPosition * 16, this.zPosition * 16 + 16);
                    var3.func_150801_a(2);
                }
            }
            else
            {
                this.isLightPopulated = false;
            }
        }
    }

    private void func_150801_a(int p_150801_1_)
    {
        if (this.isTerrainPopulated)
        {
            int var2;

            if (p_150801_1_ == 3)
            {
                for (var2 = 0; var2 < 16; ++var2)
                {
                    this.func_150811_f(15, var2);
                }
            }
            else if (p_150801_1_ == 1)
            {
                for (var2 = 0; var2 < 16; ++var2)
                {
                    this.func_150811_f(0, var2);
                }
            }
            else if (p_150801_1_ == 0)
            {
                for (var2 = 0; var2 < 16; ++var2)
                {
                    this.func_150811_f(var2, 15);
                }
            }
            else if (p_150801_1_ == 2)
            {
                for (var2 = 0; var2 < 16; ++var2)
                {
                    this.func_150811_f(var2, 0);
                }
            }
        }
    }

    private boolean func_150811_f(int p_150811_1_, int p_150811_2_)
    {
        int var3 = this.getTopFilledSegment();
        boolean var4 = false;
        boolean var5 = false;
        int var6;

        for (var6 = var3 + 16 - 1; var6 > 63 || var6 > 0 && !var5; --var6)
        {
            int var7 = this.func_150808_b(p_150811_1_, var6, p_150811_2_);

            if (var7 == 255 && var6 < 63)
            {
                var5 = true;
            }

            if (!var4 && var7 > 0)
            {
                var4 = true;
            }
            else if (var4 && var7 == 0 && !this.worldObj.func_147451_t(this.xPosition * 16 + p_150811_1_, var6, this.zPosition * 16 + p_150811_2_))
            {
                return false;
            }
        }

        for (; var6 > 0; --var6)
        {
            if (this.func_150810_a(p_150811_1_, var6, p_150811_2_).getLightValue() > 0)
            {
                this.worldObj.func_147451_t(this.xPosition * 16 + p_150811_1_, var6, this.zPosition * 16 + p_150811_2_);
            }
        }

        return true;
    }
}
