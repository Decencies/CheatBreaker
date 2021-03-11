package net.minecraft.src;

public class BlockPos
{
    private final int x;
    private final int y;
    private final int z;

    public BlockPos(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getZ()
    {
        return this.z;
    }

    public BlockPos add(BlockPos pos)
    {
        return new BlockPos(this.x + pos.x, this.y + pos.y, this.z + pos.z);
    }

    public BlockPos sub(BlockPos pos)
    {
        return new BlockPos(this.x - pos.x, this.y - pos.y, this.z - pos.z);
    }

    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        else if (!(obj instanceof BlockPos))
        {
            return false;
        }
        else
        {
            BlockPos blockPos = (BlockPos)obj;
            return this.getX() != blockPos.getX() ? false : (this.getY() != blockPos.getY() ? false : this.getZ() == blockPos.getZ());
        }
    }

    public int hashCode()
    {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }

    public String toString()
    {
        return "" + this.x + ", " + this.y + ", " + this.z;
    }
}
