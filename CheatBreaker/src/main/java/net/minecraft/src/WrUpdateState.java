package net.minecraft.src;

import java.util.HashSet;
import net.minecraft.client.renderer.RenderBlocks;

public class WrUpdateState
{
    public ChunkCacheOF chunkcache = null;
    public RenderBlocks renderblocks = null;
    public HashSet setOldEntityRenders = null;
    int viewEntityPosX = 0;
    int viewEntityPosY = 0;
    int viewEntityPosZ = 0;
    public int renderPass = 0;
    public int y = 0;
    public boolean flag = false;
    public boolean hasRenderedBlocks = false;
    public boolean hasGlList = false;
}
