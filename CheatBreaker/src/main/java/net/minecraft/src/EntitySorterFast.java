package net.minecraft.src;

import java.util.Comparator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;

public class EntitySorterFast implements Comparator
{
    private Entity entity;

    public EntitySorterFast(Entity par1Entity)
    {
        this.entity = par1Entity;
    }

    public void prepareToSort(WorldRenderer[] renderers, int countWorldRenderers)
    {
        for (int i = 0; i < countWorldRenderers; ++i)
        {
            WorldRenderer wr = renderers[i];
            wr.updateDistanceToEntitySquared(this.entity);
        }
    }

    public int compare(WorldRenderer par1WorldRenderer, WorldRenderer par2WorldRenderer)
    {
        float dist1 = par1WorldRenderer.sortDistanceToEntitySquared;
        float dist2 = par2WorldRenderer.sortDistanceToEntitySquared;
        return dist1 == dist2 ? 0 : (dist1 > dist2 ? 1 : -1);
    }

    public int compare(Object par1Obj, Object par2Obj)
    {
        return this.compare((WorldRenderer)par1Obj, (WorldRenderer)par2Obj);
    }
}
