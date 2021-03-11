package net.minecraft.client.util;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.client.renderer.RenderList;

public class RenderDistanceSorter implements Comparator
{
    int field_152632_a;
    int field_152633_b;
    

    public RenderDistanceSorter(int p_i1051_1_, int p_i1051_2_)
    {
        this.field_152632_a = p_i1051_1_;
        this.field_152633_b = p_i1051_2_;
    }

    public int compare(RenderList p_compare_1_, RenderList p_compare_2_)
    {
        int var3 = p_compare_1_.renderChunkX - this.field_152632_a;
        int var4 = p_compare_1_.renderChunkZ - this.field_152633_b;
        int var5 = p_compare_2_.renderChunkX - this.field_152632_a;
        int var6 = p_compare_2_.renderChunkZ - this.field_152633_b;
        int var7 = var3 * var3 + var4 * var4;
        int var8 = var5 * var5 + var6 * var6;
        return ComparisonChain.start().compare(var8, var7).result();
    }

    public int compare(Object p_compare_1_, Object p_compare_2_)
    {
        return this.compare((RenderList)p_compare_1_, (RenderList)p_compare_2_);
    }
}
