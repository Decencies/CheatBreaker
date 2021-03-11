package net.minecraft.world.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class WorldChunkManager
{
    private GenLayer genBiomes;

    /** A GenLayer containing the indices into BiomeGenBase.biomeList[] */
    private GenLayer biomeIndexLayer;

    /** The BiomeCache object for this world. */
    private BiomeCache biomeCache;

    /** A list of biomes that the player can spawn in. */
    private List biomesToSpawnIn;


    protected WorldChunkManager()
    {
        this.biomeCache = new BiomeCache(this);
        this.biomesToSpawnIn = new ArrayList();
        this.biomesToSpawnIn.add(BiomeGenBase.forest);
        this.biomesToSpawnIn.add(BiomeGenBase.plains);
        this.biomesToSpawnIn.add(BiomeGenBase.taiga);
        this.biomesToSpawnIn.add(BiomeGenBase.taigaHills);
        this.biomesToSpawnIn.add(BiomeGenBase.forestHills);
        this.biomesToSpawnIn.add(BiomeGenBase.jungle);
        this.biomesToSpawnIn.add(BiomeGenBase.jungleHills);
    }

    public WorldChunkManager(long p_i1975_1_, WorldType p_i1975_3_)
    {
        this();
        GenLayer[] var4 = GenLayer.initializeAllBiomeGenerators(p_i1975_1_, p_i1975_3_);
        this.genBiomes = var4[0];
        this.biomeIndexLayer = var4[1];
    }

    public WorldChunkManager(World p_i1976_1_)
    {
        this(p_i1976_1_.getSeed(), p_i1976_1_.getWorldInfo().getTerrainType());
    }

    /**
     * Gets the list of valid biomes for the player to spawn in.
     */
    public List getBiomesToSpawnIn()
    {
        return this.biomesToSpawnIn;
    }

    /**
     * Returns the BiomeGenBase related to the x, z position on the world.
     */
    public BiomeGenBase getBiomeGenAt(int p_76935_1_, int p_76935_2_)
    {
        return this.biomeCache.getBiomeGenAt(p_76935_1_, p_76935_2_);
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    public float[] getRainfall(float[] p_76936_1_, int p_76936_2_, int p_76936_3_, int p_76936_4_, int p_76936_5_)
    {
        IntCache.resetIntCache();

        if (p_76936_1_ == null || p_76936_1_.length < p_76936_4_ * p_76936_5_)
        {
            p_76936_1_ = new float[p_76936_4_ * p_76936_5_];
        }

        int[] var6 = this.biomeIndexLayer.getInts(p_76936_2_, p_76936_3_, p_76936_4_, p_76936_5_);

        for (int var7 = 0; var7 < p_76936_4_ * p_76936_5_; ++var7)
        {
            try
            {
                float var8 = (float)BiomeGenBase.func_150568_d(var6[var7]).getIntRainfall() / 65536.0F;

                if (var8 > 1.0F)
                {
                    var8 = 1.0F;
                }

                p_76936_1_[var7] = var8;
            }
            catch (Throwable var11)
            {
                CrashReport var9 = CrashReport.makeCrashReport(var11, "Invalid Biome id");
                CrashReportCategory var10 = var9.makeCategory("DownfallBlock");
                var10.addCrashSection("biome id", Integer.valueOf(var7));
                var10.addCrashSection("downfalls[] size", Integer.valueOf(p_76936_1_.length));
                var10.addCrashSection("x", Integer.valueOf(p_76936_2_));
                var10.addCrashSection("z", Integer.valueOf(p_76936_3_));
                var10.addCrashSection("w", Integer.valueOf(p_76936_4_));
                var10.addCrashSection("h", Integer.valueOf(p_76936_5_));
                throw new ReportedException(var9);
            }
        }

        return p_76936_1_;
    }

    /**
     * Return an adjusted version of a given temperature based on the y height
     */
    public float getTemperatureAtHeight(float p_76939_1_, int p_76939_2_)
    {
        return p_76939_1_;
    }

    /**
     * Returns an array of biomes for the location input.
     */
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] p_76937_1_, int p_76937_2_, int p_76937_3_, int p_76937_4_, int p_76937_5_)
    {
        IntCache.resetIntCache();

        if (p_76937_1_ == null || p_76937_1_.length < p_76937_4_ * p_76937_5_)
        {
            p_76937_1_ = new BiomeGenBase[p_76937_4_ * p_76937_5_];
        }

        int[] var6 = this.genBiomes.getInts(p_76937_2_, p_76937_3_, p_76937_4_, p_76937_5_);

        try
        {
            for (int var7 = 0; var7 < p_76937_4_ * p_76937_5_; ++var7)
            {
                p_76937_1_[var7] = BiomeGenBase.func_150568_d(var6[var7]);
            }

            return p_76937_1_;
        }
        catch (Throwable var10)
        {
            CrashReport var8 = CrashReport.makeCrashReport(var10, "Invalid Biome id");
            CrashReportCategory var9 = var8.makeCategory("RawBiomeBlock");
            var9.addCrashSection("biomes[] size", Integer.valueOf(p_76937_1_.length));
            var9.addCrashSection("x", Integer.valueOf(p_76937_2_));
            var9.addCrashSection("z", Integer.valueOf(p_76937_3_));
            var9.addCrashSection("w", Integer.valueOf(p_76937_4_));
            var9.addCrashSection("h", Integer.valueOf(p_76937_5_));
            throw new ReportedException(var8);
        }
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] p_76933_1_, int p_76933_2_, int p_76933_3_, int p_76933_4_, int p_76933_5_)
    {
        return this.getBiomeGenAt(p_76933_1_, p_76933_2_, p_76933_3_, p_76933_4_, p_76933_5_, true);
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     */
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] p_76931_1_, int p_76931_2_, int p_76931_3_, int p_76931_4_, int p_76931_5_, boolean p_76931_6_)
    {
        IntCache.resetIntCache();

        if (p_76931_1_ == null || p_76931_1_.length < p_76931_4_ * p_76931_5_)
        {
            p_76931_1_ = new BiomeGenBase[p_76931_4_ * p_76931_5_];
        }

        if (p_76931_6_ && p_76931_4_ == 16 && p_76931_5_ == 16 && (p_76931_2_ & 15) == 0 && (p_76931_3_ & 15) == 0)
        {
            BiomeGenBase[] var9 = this.biomeCache.getCachedBiomes(p_76931_2_, p_76931_3_);
            System.arraycopy(var9, 0, p_76931_1_, 0, p_76931_4_ * p_76931_5_);
            return p_76931_1_;
        }
        else
        {
            int[] var7 = this.biomeIndexLayer.getInts(p_76931_2_, p_76931_3_, p_76931_4_, p_76931_5_);

            for (int var8 = 0; var8 < p_76931_4_ * p_76931_5_; ++var8)
            {
                p_76931_1_[var8] = BiomeGenBase.func_150568_d(var7[var8]);
            }

            return p_76931_1_;
        }
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    public boolean areBiomesViable(int p_76940_1_, int p_76940_2_, int p_76940_3_, List p_76940_4_)
    {
        IntCache.resetIntCache();
        int var5 = p_76940_1_ - p_76940_3_ >> 2;
        int var6 = p_76940_2_ - p_76940_3_ >> 2;
        int var7 = p_76940_1_ + p_76940_3_ >> 2;
        int var8 = p_76940_2_ + p_76940_3_ >> 2;
        int var9 = var7 - var5 + 1;
        int var10 = var8 - var6 + 1;
        int[] var11 = this.genBiomes.getInts(var5, var6, var9, var10);

        try
        {
            for (int var12 = 0; var12 < var9 * var10; ++var12)
            {
                BiomeGenBase var16 = BiomeGenBase.func_150568_d(var11[var12]);

                if (!p_76940_4_.contains(var16))
                {
                    return false;
                }
            }

            return true;
        }
        catch (Throwable var15)
        {
            CrashReport var13 = CrashReport.makeCrashReport(var15, "Invalid Biome id");
            CrashReportCategory var14 = var13.makeCategory("Layer");
            var14.addCrashSection("Layer", this.genBiomes.toString());
            var14.addCrashSection("x", Integer.valueOf(p_76940_1_));
            var14.addCrashSection("z", Integer.valueOf(p_76940_2_));
            var14.addCrashSection("radius", Integer.valueOf(p_76940_3_));
            var14.addCrashSection("allowed", p_76940_4_);
            throw new ReportedException(var13);
        }
    }

    public ChunkPosition func_150795_a(int p_150795_1_, int p_150795_2_, int p_150795_3_, List p_150795_4_, Random p_150795_5_)
    {
        IntCache.resetIntCache();
        int var6 = p_150795_1_ - p_150795_3_ >> 2;
        int var7 = p_150795_2_ - p_150795_3_ >> 2;
        int var8 = p_150795_1_ + p_150795_3_ >> 2;
        int var9 = p_150795_2_ + p_150795_3_ >> 2;
        int var10 = var8 - var6 + 1;
        int var11 = var9 - var7 + 1;
        int[] var12 = this.genBiomes.getInts(var6, var7, var10, var11);
        ChunkPosition var13 = null;
        int var14 = 0;

        for (int var15 = 0; var15 < var10 * var11; ++var15)
        {
            int var16 = var6 + var15 % var10 << 2;
            int var17 = var7 + var15 / var10 << 2;
            BiomeGenBase var18 = BiomeGenBase.func_150568_d(var12[var15]);

            if (p_150795_4_.contains(var18) && (var13 == null || p_150795_5_.nextInt(var14 + 1) == 0))
            {
                var13 = new ChunkPosition(var16, 0, var17);
                ++var14;
            }
        }

        return var13;
    }

    /**
     * Calls the WorldChunkManager's biomeCache.cleanupCache()
     */
    public void cleanupCache()
    {
        this.biomeCache.cleanupCache();
    }
}
