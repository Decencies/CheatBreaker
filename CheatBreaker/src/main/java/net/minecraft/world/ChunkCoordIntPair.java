package net.minecraft.world;

public class ChunkCoordIntPair
{
    /** The X position of this Chunk Coordinate Pair */
    public final int chunkXPos;

    /** The Z position of this Chunk Coordinate Pair */
    public final int chunkZPos;

    private int cachedHashCode = 0;


    public ChunkCoordIntPair(int par1, int par2)
    {
        this.chunkXPos = par1;
        this.chunkZPos = par2;
    }

    /**
     * converts a chunk coordinate pair to an integer (suitable for hashing)
     */
    public static long chunkXZ2Int(int par0, int par1)
    {
        return (long)par0 & 4294967295L | ((long)par1 & 4294967295L) << 32;
    }

    public int hashCode()
    {
        if (this.cachedHashCode == 0)
        {
            int var1 = 1664525 * this.chunkXPos + 1013904223;
            int var2 = 1664525 * (this.chunkZPos ^ -559038737) + 1013904223;
            this.cachedHashCode = var1 ^ var2;
        }

        return this.cachedHashCode;
    }

    public boolean equals(Object par1Obj)
    {
        if (this == par1Obj)
        {
            return true;
        }
        else if (!(par1Obj instanceof ChunkCoordIntPair))
        {
            return false;
        }
        else
        {
            ChunkCoordIntPair var2 = (ChunkCoordIntPair)par1Obj;
            return this.chunkXPos == var2.chunkXPos && this.chunkZPos == var2.chunkZPos;
        }
    }

    public int getCenterXPos()
    {
        return (this.chunkXPos << 4) + 8;
    }

    public int getCenterZPosition()
    {
        return (this.chunkZPos << 4) + 8;
    }

    public ChunkPosition func_151349_a(int p_151349_1_)
    {
        return new ChunkPosition(this.getCenterXPos(), p_151349_1_, this.getCenterZPosition());
    }

    public String toString()
    {
        return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
    }
}
