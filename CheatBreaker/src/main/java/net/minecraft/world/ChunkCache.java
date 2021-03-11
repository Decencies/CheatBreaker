package net.minecraft.world;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess
{
    private int chunkX;
    private int chunkZ;
    private Chunk[][] chunkArray;

    /** True if the chunk cache is empty. */
    private boolean isEmpty;

    /** Reference to the World object. */
    private World worldObj;


    public ChunkCache(World p_i1964_1_, int p_i1964_2_, int p_i1964_3_, int p_i1964_4_, int p_i1964_5_, int p_i1964_6_, int p_i1964_7_, int p_i1964_8_)
    {
        this.worldObj = p_i1964_1_;
        this.chunkX = p_i1964_2_ - p_i1964_8_ >> 4;
        this.chunkZ = p_i1964_4_ - p_i1964_8_ >> 4;
        int var9 = p_i1964_5_ + p_i1964_8_ >> 4;
        int var10 = p_i1964_7_ + p_i1964_8_ >> 4;
        this.chunkArray = new Chunk[var9 - this.chunkX + 1][var10 - this.chunkZ + 1];
        this.isEmpty = true;
        int var11;
        int var12;
        Chunk var13;

        for (var11 = this.chunkX; var11 <= var9; ++var11)
        {
            for (var12 = this.chunkZ; var12 <= var10; ++var12)
            {
                var13 = p_i1964_1_.getChunkFromChunkCoords(var11, var12);

                if (var13 != null)
                {
                    this.chunkArray[var11 - this.chunkX][var12 - this.chunkZ] = var13;
                }
            }
        }

        for (var11 = p_i1964_2_ >> 4; var11 <= p_i1964_5_ >> 4; ++var11)
        {
            for (var12 = p_i1964_4_ >> 4; var12 <= p_i1964_7_ >> 4; ++var12)
            {
                var13 = this.chunkArray[var11 - this.chunkX][var12 - this.chunkZ];

                if (var13 != null && !var13.getAreLevelsEmpty(p_i1964_3_, p_i1964_6_))
                {
                    this.isEmpty = false;
                }
            }
        }
    }

    /**
     * set by !chunk.getAreLevelsEmpty
     */
    public boolean extendedLevelsInChunkCache()
    {
        return this.isEmpty;
    }

    public Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_)
    {
        Block var4 = Blocks.air;

        if (p_147439_2_ >= 0 && p_147439_2_ < 256)
        {
            int var5 = (p_147439_1_ >> 4) - this.chunkX;
            int var6 = (p_147439_3_ >> 4) - this.chunkZ;

            if (var5 >= 0 && var5 < this.chunkArray.length && var6 >= 0 && var6 < this.chunkArray[var5].length)
            {
                Chunk var7 = this.chunkArray[var5][var6];

                if (var7 != null)
                {
                    var4 = var7.func_150810_a(p_147439_1_ & 15, p_147439_2_, p_147439_3_ & 15);
                }
            }
        }

        return var4;
    }

    public TileEntity getTileEntity(int p_147438_1_, int p_147438_2_, int p_147438_3_)
    {
        int var4 = (p_147438_1_ >> 4) - this.chunkX;
        int var5 = (p_147438_3_ >> 4) - this.chunkZ;
        return this.chunkArray[var4][var5].func_150806_e(p_147438_1_ & 15, p_147438_2_, p_147438_3_ & 15);
    }

    /**
     * Any Light rendered on a 1.8 Block goes through here
     */
    public int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_)
    {
        int var5 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Sky, p_72802_1_, p_72802_2_, p_72802_3_);
        int var6 = this.getSkyBlockTypeBrightness(EnumSkyBlock.Block, p_72802_1_, p_72802_2_, p_72802_3_);

        if (var6 < p_72802_4_)
        {
            var6 = p_72802_4_;
        }

        return var5 << 20 | var6 << 4;
    }

    /**
     * Returns the block metadata at coords x,y,z
     */
    public int getBlockMetadata(int p_72805_1_, int p_72805_2_, int p_72805_3_)
    {
        if (p_72805_2_ < 0)
        {
            return 0;
        }
        else if (p_72805_2_ >= 256)
        {
            return 0;
        }
        else
        {
            int var4 = (p_72805_1_ >> 4) - this.chunkX;
            int var5 = (p_72805_3_ >> 4) - this.chunkZ;
            return this.chunkArray[var4][var5].getBlockMetadata(p_72805_1_ & 15, p_72805_2_, p_72805_3_ & 15);
        }
    }

    /**
     * Gets the biome for a given set of x/z coordinates
     */
    public BiomeGenBase getBiomeGenForCoords(int p_72807_1_, int p_72807_2_)
    {
        return this.worldObj.getBiomeGenForCoords(p_72807_1_, p_72807_2_);
    }

    /**
     * Returns true if the block at the specified coordinates is empty
     */
    public boolean isAirBlock(int p_147437_1_, int p_147437_2_, int p_147437_3_)
    {
        return this.getBlock(p_147437_1_, p_147437_2_, p_147437_3_).getMaterial() == Material.air;
    }

    /**
     * Brightness for SkyBlock.Sky is clear white and (through color computing it is assumed) DEPENDENT ON DAYTIME.
     * Brightness for SkyBlock.Block is yellowish and independent.
     */
    public int getSkyBlockTypeBrightness(EnumSkyBlock p_72810_1_, int p_72810_2_, int p_72810_3_, int p_72810_4_)
    {
        if (p_72810_3_ < 0)
        {
            p_72810_3_ = 0;
        }

        if (p_72810_3_ >= 256)
        {
            p_72810_3_ = 255;
        }

        if (p_72810_3_ >= 0 && p_72810_3_ < 256 && p_72810_2_ >= -30000000 && p_72810_4_ >= -30000000 && p_72810_2_ < 30000000 && p_72810_4_ <= 30000000)
        {
            if (p_72810_1_ == EnumSkyBlock.Sky && this.worldObj.provider.hasNoSky)
            {
                return 0;
            }
            else
            {
                int var5;
                int var6;

                if (this.getBlock(p_72810_2_, p_72810_3_, p_72810_4_).func_149710_n())
                {
                    var5 = this.getSpecialBlockBrightness(p_72810_1_, p_72810_2_, p_72810_3_ + 1, p_72810_4_);
                    var6 = this.getSpecialBlockBrightness(p_72810_1_, p_72810_2_ + 1, p_72810_3_, p_72810_4_);
                    int var7 = this.getSpecialBlockBrightness(p_72810_1_, p_72810_2_ - 1, p_72810_3_, p_72810_4_);
                    int var8 = this.getSpecialBlockBrightness(p_72810_1_, p_72810_2_, p_72810_3_, p_72810_4_ + 1);
                    int var9 = this.getSpecialBlockBrightness(p_72810_1_, p_72810_2_, p_72810_3_, p_72810_4_ - 1);

                    if (var6 > var5)
                    {
                        var5 = var6;
                    }

                    if (var7 > var5)
                    {
                        var5 = var7;
                    }

                    if (var8 > var5)
                    {
                        var5 = var8;
                    }

                    if (var9 > var5)
                    {
                        var5 = var9;
                    }

                    return var5;
                }
                else
                {
                    var5 = (p_72810_2_ >> 4) - this.chunkX;
                    var6 = (p_72810_4_ >> 4) - this.chunkZ;
                    return this.chunkArray[var5][var6].getSavedLightValue(p_72810_1_, p_72810_2_ & 15, p_72810_3_, p_72810_4_ & 15);
                }
            }
        }
        else
        {
            return p_72810_1_.defaultLightValue;
        }
    }

    /**
     * is only used on stairs and tilled fields
     */
    public int getSpecialBlockBrightness(EnumSkyBlock p_72812_1_, int p_72812_2_, int p_72812_3_, int p_72812_4_)
    {
        if (p_72812_3_ < 0)
        {
            p_72812_3_ = 0;
        }

        if (p_72812_3_ >= 256)
        {
            p_72812_3_ = 255;
        }

        if (p_72812_3_ >= 0 && p_72812_3_ < 256 && p_72812_2_ >= -30000000 && p_72812_4_ >= -30000000 && p_72812_2_ < 30000000 && p_72812_4_ <= 30000000)
        {
            int var5 = (p_72812_2_ >> 4) - this.chunkX;
            int var6 = (p_72812_4_ >> 4) - this.chunkZ;
            return this.chunkArray[var5][var6].getSavedLightValue(p_72812_1_, p_72812_2_ & 15, p_72812_3_, p_72812_4_ & 15);
        }
        else
        {
            return p_72812_1_.defaultLightValue;
        }
    }

    /**
     * Returns current world height.
     */
    public int getHeight()
    {
        return 256;
    }

    /**
     * Is this block powering in the specified direction Args: x, y, z, direction
     */
    public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_)
    {
        return this.getBlock(p_72879_1_, p_72879_2_, p_72879_3_).isProvidingStrongPower(this, p_72879_1_, p_72879_2_, p_72879_3_, p_72879_4_);
    }
}
