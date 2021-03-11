package net.minecraft.world.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;

public class ChunkProviderFlat implements IChunkProvider
{
    private World worldObj;
    private Random random;
    private final Block[] cachedBlockIDs = new Block[256];
    private final byte[] cachedBlockMetadata = new byte[256];
    private final FlatGeneratorInfo flatWorldGenInfo;
    private final List structureGenerators = new ArrayList();
    private final boolean hasDecoration;
    private final boolean hasDungeons;
    private WorldGenLakes waterLakeGenerator;
    private WorldGenLakes lavaLakeGenerator;


    public ChunkProviderFlat(World p_i2004_1_, long p_i2004_2_, boolean p_i2004_4_, String p_i2004_5_)
    {
        this.worldObj = p_i2004_1_;
        this.random = new Random(p_i2004_2_);
        this.flatWorldGenInfo = FlatGeneratorInfo.createFlatGeneratorFromString(p_i2004_5_);

        if (p_i2004_4_)
        {
            Map var6 = this.flatWorldGenInfo.getWorldFeatures();

            if (var6.containsKey("village"))
            {
                Map var7 = (Map)var6.get("village");

                if (!var7.containsKey("size"))
                {
                    var7.put("size", "1");
                }

                this.structureGenerators.add(new MapGenVillage(var7));
            }

            if (var6.containsKey("biome_1"))
            {
                this.structureGenerators.add(new MapGenScatteredFeature((Map)var6.get("biome_1")));
            }

            if (var6.containsKey("mineshaft"))
            {
                this.structureGenerators.add(new MapGenMineshaft((Map)var6.get("mineshaft")));
            }

            if (var6.containsKey("stronghold"))
            {
                this.structureGenerators.add(new MapGenStronghold((Map)var6.get("stronghold")));
            }
        }

        this.hasDecoration = this.flatWorldGenInfo.getWorldFeatures().containsKey("decoration");

        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lake"))
        {
            this.waterLakeGenerator = new WorldGenLakes(Blocks.water);
        }

        if (this.flatWorldGenInfo.getWorldFeatures().containsKey("lava_lake"))
        {
            this.lavaLakeGenerator = new WorldGenLakes(Blocks.lava);
        }

        this.hasDungeons = this.flatWorldGenInfo.getWorldFeatures().containsKey("dungeon");
        Iterator var9 = this.flatWorldGenInfo.getFlatLayers().iterator();

        while (var9.hasNext())
        {
            FlatLayerInfo var10 = (FlatLayerInfo)var9.next();

            for (int var8 = var10.getMinY(); var8 < var10.getMinY() + var10.getLayerCount(); ++var8)
            {
                this.cachedBlockIDs[var8] = var10.func_151536_b();
                this.cachedBlockMetadata[var8] = (byte)var10.getFillBlockMeta();
            }
        }
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    public Chunk loadChunk(int p_73158_1_, int p_73158_2_)
    {
        return this.provideChunk(p_73158_1_, p_73158_2_);
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP com.cheatbreaker.client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int p_73154_1_, int p_73154_2_)
    {
        Chunk var3 = new Chunk(this.worldObj, p_73154_1_, p_73154_2_);
        int var6;

        for (int var4 = 0; var4 < this.cachedBlockIDs.length; ++var4)
        {
            Block var5 = this.cachedBlockIDs[var4];

            if (var5 != null)
            {
                var6 = var4 >> 4;
                ExtendedBlockStorage var7 = var3.getBlockStorageArray()[var6];

                if (var7 == null)
                {
                    var7 = new ExtendedBlockStorage(var4, !this.worldObj.provider.hasNoSky);
                    var3.getBlockStorageArray()[var6] = var7;
                }

                for (int var8 = 0; var8 < 16; ++var8)
                {
                    for (int var9 = 0; var9 < 16; ++var9)
                    {
                        var7.func_150818_a(var8, var4 & 15, var9, var5);
                        var7.setExtBlockMetadata(var8, var4 & 15, var9, this.cachedBlockMetadata[var4]);
                    }
                }
            }
        }

        var3.generateSkylightMap();
        BiomeGenBase[] var10 = this.worldObj.getWorldChunkManager().loadBlockGeneratorData((BiomeGenBase[])null, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        byte[] var11 = var3.getBiomeArray();

        for (var6 = 0; var6 < var11.length; ++var6)
        {
            var11[var6] = (byte)var10[var6].biomeID;
        }

        Iterator var12 = this.structureGenerators.iterator();

        while (var12.hasNext())
        {
            MapGenBase var13 = (MapGenBase)var12.next();
            var13.func_151539_a(this, this.worldObj, p_73154_1_, p_73154_2_, (Block[])null);
        }

        var3.generateSkylightMap();
        return var3;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    public boolean chunkExists(int p_73149_1_, int p_73149_2_)
    {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
    {
        int var4 = p_73153_2_ * 16;
        int var5 = p_73153_3_ * 16;
        BiomeGenBase var6 = this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
        boolean var7 = false;
        this.random.setSeed(this.worldObj.getSeed());
        long var8 = this.random.nextLong() / 2L * 2L + 1L;
        long var10 = this.random.nextLong() / 2L * 2L + 1L;
        this.random.setSeed((long)p_73153_2_ * var8 + (long)p_73153_3_ * var10 ^ this.worldObj.getSeed());
        Iterator var12 = this.structureGenerators.iterator();

        while (var12.hasNext())
        {
            MapGenStructure var13 = (MapGenStructure)var12.next();
            boolean var14 = var13.generateStructuresInChunk(this.worldObj, this.random, p_73153_2_, p_73153_3_);

            if (var13 instanceof MapGenVillage)
            {
                var7 |= var14;
            }
        }

        int var16;
        int var17;
        int var18;

        if (this.waterLakeGenerator != null && !var7 && this.random.nextInt(4) == 0)
        {
            var16 = var4 + this.random.nextInt(16) + 8;
            var17 = this.random.nextInt(256);
            var18 = var5 + this.random.nextInt(16) + 8;
            this.waterLakeGenerator.generate(this.worldObj, this.random, var16, var17, var18);
        }

        if (this.lavaLakeGenerator != null && !var7 && this.random.nextInt(8) == 0)
        {
            var16 = var4 + this.random.nextInt(16) + 8;
            var17 = this.random.nextInt(this.random.nextInt(248) + 8);
            var18 = var5 + this.random.nextInt(16) + 8;

            if (var17 < 63 || this.random.nextInt(10) == 0)
            {
                this.lavaLakeGenerator.generate(this.worldObj, this.random, var16, var17, var18);
            }
        }

        if (this.hasDungeons)
        {
            for (var16 = 0; var16 < 8; ++var16)
            {
                var17 = var4 + this.random.nextInt(16) + 8;
                var18 = this.random.nextInt(256);
                int var15 = var5 + this.random.nextInt(16) + 8;
                (new WorldGenDungeons()).generate(this.worldObj, this.random, var17, var18, var15);
            }
        }

        if (this.hasDecoration)
        {
            var6.decorate(this.worldObj, this.random, var4, var5);
        }
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_)
    {
        return true;
    }

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    public void saveExtraData() {}

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave()
    {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return "FlatLevelSource";
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    public List getPossibleCreatures(EnumCreatureType p_73155_1_, int p_73155_2_, int p_73155_3_, int p_73155_4_)
    {
        BiomeGenBase var5 = this.worldObj.getBiomeGenForCoords(p_73155_2_, p_73155_4_);
        return var5.getSpawnableList(p_73155_1_);
    }

    public ChunkPosition func_147416_a(World p_147416_1_, String p_147416_2_, int p_147416_3_, int p_147416_4_, int p_147416_5_)
    {
        if ("Stronghold".equals(p_147416_2_))
        {
            Iterator var6 = this.structureGenerators.iterator();

            while (var6.hasNext())
            {
                MapGenStructure var7 = (MapGenStructure)var6.next();

                if (var7 instanceof MapGenStronghold)
                {
                    return var7.func_151545_a(p_147416_1_, p_147416_3_, p_147416_4_, p_147416_5_);
                }
            }
        }

        return null;
    }

    public int getLoadedChunkCount()
    {
        return 0;
    }

    public void recreateStructures(int p_82695_1_, int p_82695_2_)
    {
        Iterator var3 = this.structureGenerators.iterator();

        while (var3.hasNext())
        {
            MapGenStructure var4 = (MapGenStructure)var3.next();
            var4.func_151539_a(this, this.worldObj, p_82695_1_, p_82695_2_, (Block[])null);
        }
    }
}
