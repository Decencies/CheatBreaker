package net.minecraft.src;

import net.minecraft.client.renderer.GLAllocation;

public class WrDisplayListBlock
{
    private int start = -1;
    private int used = -1;
    private int end = -1;
    public static final int BLOCK_LENGTH = 16384;

    public WrDisplayListBlock()
    {
        this.start = GLAllocation.generateDisplayLists(16384);
        this.used = this.start;
        this.end = this.start + 16384;
    }

    public boolean canAllocate(int len)
    {
        return this.used + len < this.end;
    }

    public int allocate(int len)
    {
        if (!this.canAllocate(len))
        {
            return -1;
        }
        else
        {
            int allocated = this.used;
            this.used += len;
            return allocated;
        }
    }

    public void reset()
    {
        this.used = this.start;
    }

    public void deleteDisplayLists()
    {
        GLAllocation.deleteDisplayLists(this.start);
    }

    public int getStart()
    {
        return this.start;
    }

    public int getUsed()
    {
        return this.used;
    }

    public int getEnd()
    {
        return this.end;
    }
}
