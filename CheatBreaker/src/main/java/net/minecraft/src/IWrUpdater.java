package net.minecraft.src;

import java.util.List;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public interface IWrUpdater
{
    void initialize();

    WorldRenderer makeWorldRenderer(World var1, List var2, int var3, int var4, int var5, int var6);

    void preRender(RenderGlobal var1, EntityLivingBase var2);

    void postRender();

    boolean updateRenderers(RenderGlobal var1, EntityLivingBase var2, boolean var3);

    void resumeBackgroundUpdates();

    void pauseBackgroundUpdates();

    void finishCurrentUpdate();

    void clearAllUpdates();

    void terminate();
}
