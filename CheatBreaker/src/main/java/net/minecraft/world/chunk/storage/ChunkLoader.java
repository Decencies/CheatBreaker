package net.minecraft.world.chunk.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.NibbleArray;

public class ChunkLoader
{


    public static ChunkLoader.AnvilConverterData load(NBTTagCompound p_76691_0_)
    {
        int var1 = p_76691_0_.getInteger("xPos");
        int var2 = p_76691_0_.getInteger("zPos");
        ChunkLoader.AnvilConverterData var3 = new ChunkLoader.AnvilConverterData(var1, var2);
        var3.blocks = p_76691_0_.getByteArray("Blocks");
        var3.data = new NibbleArrayReader(p_76691_0_.getByteArray("Data"), 7);
        var3.skyLight = new NibbleArrayReader(p_76691_0_.getByteArray("SkyLight"), 7);
        var3.blockLight = new NibbleArrayReader(p_76691_0_.getByteArray("BlockLight"), 7);
        var3.heightmap = p_76691_0_.getByteArray("HeightMap");
        var3.terrainPopulated = p_76691_0_.getBoolean("TerrainPopulated");
        var3.entities = p_76691_0_.getTagList("Entities", 10);
        var3.field_151564_i = p_76691_0_.getTagList("TileEntities", 10);
        var3.field_151563_j = p_76691_0_.getTagList("TileTicks", 10);

        try
        {
            var3.lastUpdated = p_76691_0_.getLong("LastUpdate");
        }
        catch (ClassCastException var5)
        {
            var3.lastUpdated = (long)p_76691_0_.getInteger("LastUpdate");
        }

        return var3;
    }

    public static void convertToAnvilFormat(ChunkLoader.AnvilConverterData p_76690_0_, NBTTagCompound p_76690_1_, WorldChunkManager p_76690_2_)
    {
        p_76690_1_.setInteger("xPos", p_76690_0_.x);
        p_76690_1_.setInteger("zPos", p_76690_0_.z);
        p_76690_1_.setLong("LastUpdate", p_76690_0_.lastUpdated);
        int[] var3 = new int[p_76690_0_.heightmap.length];

        for (int var4 = 0; var4 < p_76690_0_.heightmap.length; ++var4)
        {
            var3[var4] = p_76690_0_.heightmap[var4];
        }

        p_76690_1_.setIntArray("HeightMap", var3);
        p_76690_1_.setBoolean("TerrainPopulated", p_76690_0_.terrainPopulated);
        NBTTagList var16 = new NBTTagList();
        int var7;

        for (int var5 = 0; var5 < 8; ++var5)
        {
            boolean var6 = true;

            for (var7 = 0; var7 < 16 && var6; ++var7)
            {
                int var8 = 0;

                while (var8 < 16 && var6)
                {
                    int var9 = 0;

                    while (true)
                    {
                        if (var9 < 16)
                        {
                            int var10 = var7 << 11 | var9 << 7 | var8 + (var5 << 4);
                            byte var11 = p_76690_0_.blocks[var10];

                            if (var11 == 0)
                            {
                                ++var9;
                                continue;
                            }

                            var6 = false;
                        }

                        ++var8;
                        break;
                    }
                }
            }

            if (!var6)
            {
                byte[] var19 = new byte[4096];
                NibbleArray var20 = new NibbleArray(var19.length, 4);
                NibbleArray var21 = new NibbleArray(var19.length, 4);
                NibbleArray var22 = new NibbleArray(var19.length, 4);

                for (int var23 = 0; var23 < 16; ++var23)
                {
                    for (int var12 = 0; var12 < 16; ++var12)
                    {
                        for (int var13 = 0; var13 < 16; ++var13)
                        {
                            int var14 = var23 << 11 | var13 << 7 | var12 + (var5 << 4);
                            byte var15 = p_76690_0_.blocks[var14];
                            var19[var12 << 8 | var13 << 4 | var23] = (byte)(var15 & 255);
                            var20.set(var23, var12, var13, p_76690_0_.data.get(var23, var12 + (var5 << 4), var13));
                            var21.set(var23, var12, var13, p_76690_0_.skyLight.get(var23, var12 + (var5 << 4), var13));
                            var22.set(var23, var12, var13, p_76690_0_.blockLight.get(var23, var12 + (var5 << 4), var13));
                        }
                    }
                }

                NBTTagCompound var24 = new NBTTagCompound();
                var24.setByte("Y", (byte)(var5 & 255));
                var24.setByteArray("Blocks", var19);
                var24.setByteArray("Data", var20.data);
                var24.setByteArray("SkyLight", var21.data);
                var24.setByteArray("BlockLight", var22.data);
                var16.appendTag(var24);
            }
        }

        p_76690_1_.setTag("Sections", var16);
        byte[] var17 = new byte[256];

        for (int var18 = 0; var18 < 16; ++var18)
        {
            for (var7 = 0; var7 < 16; ++var7)
            {
                var17[var7 << 4 | var18] = (byte)(p_76690_2_.getBiomeGenAt(p_76690_0_.x << 4 | var18, p_76690_0_.z << 4 | var7).biomeID & 255);
            }
        }

        p_76690_1_.setByteArray("Biomes", var17);
        p_76690_1_.setTag("Entities", p_76690_0_.entities);
        p_76690_1_.setTag("TileEntities", p_76690_0_.field_151564_i);

        if (p_76690_0_.field_151563_j != null)
        {
            p_76690_1_.setTag("TileTicks", p_76690_0_.field_151563_j);
        }
    }

    public static class AnvilConverterData
    {
        public long lastUpdated;
        public boolean terrainPopulated;
        public byte[] heightmap;
        public NibbleArrayReader blockLight;
        public NibbleArrayReader skyLight;
        public NibbleArrayReader data;
        public byte[] blocks;
        public NBTTagList entities;
        public NBTTagList field_151564_i;
        public NBTTagList field_151563_j;
        public final int x;
        public final int z;


        public AnvilConverterData(int p_i1999_1_, int p_i1999_2_)
        {
            this.x = p_i1999_1_;
            this.z = p_i1999_2_;
        }
    }
}
