package net.minecraft.client.renderer;

import java.util.Comparator;
import net.minecraft.entity.Entity;

public class EntitySorter implements Comparator
{
    /** Entity position X */
    private double entityPosX;

    /** Entity position Y */
    private double entityPosY;

    /** Entity position Z */
    private double entityPosZ;


    public EntitySorter(Entity p_i1242_1_)
    {
        this.entityPosX = -p_i1242_1_.posX;
        this.entityPosY = -p_i1242_1_.posY;
        this.entityPosZ = -p_i1242_1_.posZ;
    }

    public int compare(WorldRenderer p_compare_1_, WorldRenderer p_compare_2_)
    {
        double var3 = (double)p_compare_1_.posXPlus + this.entityPosX;
        double var5 = (double)p_compare_1_.posYPlus + this.entityPosY;
        double var7 = (double)p_compare_1_.posZPlus + this.entityPosZ;
        double var9 = (double)p_compare_2_.posXPlus + this.entityPosX;
        double var11 = (double)p_compare_2_.posYPlus + this.entityPosY;
        double var13 = (double)p_compare_2_.posZPlus + this.entityPosZ;
        return (int)((var3 * var3 + var5 * var5 + var7 * var7 - (var9 * var9 + var11 * var11 + var13 * var13)) * 1024.0D);
    }

    public int compare(Object p_compare_1_, Object p_compare_2_)
    {
        return this.compare((WorldRenderer)p_compare_1_, (WorldRenderer)p_compare_2_);
    }
}
