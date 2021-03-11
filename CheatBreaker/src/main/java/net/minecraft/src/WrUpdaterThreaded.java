package net.minecraft.src;

import java.util.List;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;

public class WrUpdaterThreaded implements IWrUpdater
{
    private WrUpdateThread updateThread = null;
    private float timePerUpdateMs = 10.0F;
    private long updateStartTimeNs = 0L;
    private boolean firstUpdate = true;
    private int updateTargetNum = 0;

    public void terminate()
    {
        if (this.updateThread != null)
        {
            this.updateThread.terminate();
            this.updateThread.unpauseToEndOfUpdate();
        }
    }

    public void initialize() {}

    private void delayedInit()
    {
        if (this.updateThread == null)
        {
            this.createUpdateThread(Display.getDrawable());
        }
    }

    public WorldRenderer makeWorldRenderer(World worldObj, List tileEntities, int x, int y, int z, int glRenderListBase)
    {
        return new WorldRendererThreaded(worldObj, tileEntities, x, y, z, glRenderListBase);
    }

    public WrUpdateThread createUpdateThread(Drawable displayDrawable)
    {
        if (this.updateThread != null)
        {
            throw new IllegalStateException("UpdateThread is already existing");
        }
        else
        {
            try
            {
                Pbuffer e = new Pbuffer(1, 1, new PixelFormat(), displayDrawable);
                this.updateThread = new WrUpdateThread(e);
                this.updateThread.setPriority(1);
                this.updateThread.start();
                this.updateThread.pause();
                return this.updateThread;
            }
            catch (Exception var3)
            {
                throw new RuntimeException(var3);
            }
        }
    }

    public boolean isUpdateThread()
    {
        return Thread.currentThread() == this.updateThread;
    }

    public static boolean isBackgroundChunkLoading()
    {
        return true;
    }

    public void preRender(RenderGlobal rg, EntityLivingBase player)
    {
        this.updateTargetNum = 0;

        if (this.updateThread != null)
        {
            if (this.updateStartTimeNs == 0L)
            {
                this.updateStartTimeNs = System.nanoTime();
            }

            if (this.updateThread.hasWorkToDo())
            {
                this.updateTargetNum = Config.getUpdatesPerFrame();

                if (Config.isDynamicUpdates() && !rg.isMoving(player))
                {
                    this.updateTargetNum *= 3;
                }

                this.updateTargetNum = Math.min(this.updateTargetNum, this.updateThread.getPendingUpdatesCount());

                if (this.updateTargetNum > 0)
                {
                    this.updateThread.unpause();
                }
            }
        }
    }

    public void postRender()
    {
        if (this.updateThread != null)
        {
            float sleepTimeMs = 0.0F;

            if (this.updateTargetNum > 0)
            {
                long deltaTime = System.nanoTime() - this.updateStartTimeNs;
                float targetRunTime = this.timePerUpdateMs * (1.0F + (float)(this.updateTargetNum - 1) / 2.0F);

                if (targetRunTime > 0.0F)
                {
                    int sleepTimeMsInt = (int)targetRunTime;
                    Config.sleep((long)sleepTimeMsInt);
                }

                this.updateThread.pause();
            }

            float deltaTime1 = 0.2F;

            if (this.updateTargetNum > 0)
            {
                int updateCount = this.updateThread.resetUpdateCount();

                if (updateCount < this.updateTargetNum)
                {
                    this.timePerUpdateMs += deltaTime1;
                }

                if (updateCount > this.updateTargetNum)
                {
                    this.timePerUpdateMs -= deltaTime1;
                }

                if (updateCount == this.updateTargetNum)
                {
                    this.timePerUpdateMs -= deltaTime1;
                }
            }
            else
            {
                this.timePerUpdateMs -= deltaTime1 / 5.0F;
            }

            if (this.timePerUpdateMs < 0.0F)
            {
                this.timePerUpdateMs = 0.0F;
            }

            this.updateStartTimeNs = System.nanoTime();
        }
    }

    public boolean updateRenderers(RenderGlobal rg, EntityLivingBase entityliving, boolean flag)
    {
        this.delayedInit();

        if (rg.worldRenderersToUpdate.size() <= 0)
        {
            return true;
        }
        else
        {
            int num = 0;
            byte NOT_IN_FRUSTRUM_MUL = 4;
            int numValid = 0;
            WorldRenderer wrBest = null;
            float distSqBest = Float.MAX_VALUE;
            int indexBest = -1;
            int maxUpdateNum;
            float dstIndex;

            for (maxUpdateNum = 0; maxUpdateNum < rg.worldRenderersToUpdate.size(); ++maxUpdateNum)
            {
                WorldRenderer turboMode = (WorldRenderer)rg.worldRenderersToUpdate.get(maxUpdateNum);

                if (turboMode != null)
                {
                    ++numValid;

                    if (!turboMode.isUpdating)
                    {
                        if (!turboMode.needsUpdate)
                        {
                            rg.worldRenderersToUpdate.set(maxUpdateNum, (Object)null);
                        }
                        else
                        {
                            dstIndex = turboMode.distanceToEntitySquared(entityliving);

                            if (dstIndex < 512.0F)
                            {
                                if (dstIndex < 256.0F && rg.isActingNow() && turboMode.isInFrustum || this.firstUpdate)
                                {
                                    if (this.updateThread != null)
                                    {
                                        this.updateThread.unpauseToEndOfUpdate();
                                    }

                                    turboMode.updateRenderer(entityliving);
                                    turboMode.needsUpdate = false;
                                    rg.worldRenderersToUpdate.set(maxUpdateNum, (Object)null);
                                    ++num;
                                    continue;
                                }

                                if (this.updateThread != null)
                                {
                                    this.updateThread.addRendererToUpdate(turboMode, true);
                                    turboMode.needsUpdate = false;
                                    rg.worldRenderersToUpdate.set(maxUpdateNum, (Object)null);
                                    ++num;
                                    continue;
                                }
                            }

                            if (!turboMode.isInFrustum)
                            {
                                dstIndex *= (float)NOT_IN_FRUSTRUM_MUL;
                            }

                            if (wrBest == null)
                            {
                                wrBest = turboMode;
                                distSqBest = dstIndex;
                                indexBest = maxUpdateNum;
                            }
                            else if (dstIndex < distSqBest)
                            {
                                wrBest = turboMode;
                                distSqBest = dstIndex;
                                indexBest = maxUpdateNum;
                            }
                        }
                    }
                }
            }

            maxUpdateNum = Config.getUpdatesPerFrame();
            boolean var17 = false;

            if (Config.isDynamicUpdates() && !rg.isMoving(entityliving))
            {
                maxUpdateNum *= 3;
                var17 = true;
            }

            if (this.updateThread != null)
            {
                maxUpdateNum = this.updateThread.getUpdateCapacity();

                if (maxUpdateNum <= 0)
                {
                    return true;
                }
            }

            int i;

            if (wrBest != null)
            {
                this.updateRenderer(wrBest, entityliving);
                rg.worldRenderersToUpdate.set(indexBest, (Object)null);
                ++num;
                dstIndex = distSqBest / 5.0F;

                for (i = 0; i < rg.worldRenderersToUpdate.size() && num < maxUpdateNum; ++i)
                {
                    WorldRenderer wr = (WorldRenderer)rg.worldRenderersToUpdate.get(i);

                    if (wr != null && !wr.isUpdating)
                    {
                        float distSq = wr.distanceToEntitySquared(entityliving);

                        if (!wr.isInFrustum)
                        {
                            distSq *= (float)NOT_IN_FRUSTRUM_MUL;
                        }

                        float diffDistSq = Math.abs(distSq - distSqBest);

                        if (diffDistSq < dstIndex)
                        {
                            this.updateRenderer(wr, entityliving);
                            rg.worldRenderersToUpdate.set(i, (Object)null);
                            ++num;
                        }
                    }
                }
            }

            if (numValid == 0)
            {
                rg.worldRenderersToUpdate.clear();
            }

            if (rg.worldRenderersToUpdate.size() > 100 && numValid < rg.worldRenderersToUpdate.size() * 4 / 5)
            {
                int var18 = 0;

                for (i = 0; i < rg.worldRenderersToUpdate.size(); ++i)
                {
                    Object var19 = rg.worldRenderersToUpdate.get(i);

                    if (var19 != null)
                    {
                        if (i != var18)
                        {
                            rg.worldRenderersToUpdate.set(var18, var19);
                        }

                        ++var18;
                    }
                }

                for (i = rg.worldRenderersToUpdate.size() - 1; i >= var18; --i)
                {
                    rg.worldRenderersToUpdate.remove(i);
                }
            }

            this.firstUpdate = false;
            return true;
        }
    }

    private void updateRenderer(WorldRenderer wr, EntityLivingBase entityLiving)
    {
        WrUpdateThread ut = this.updateThread;

        if (ut != null)
        {
            ut.addRendererToUpdate(wr, false);
            wr.needsUpdate = false;
        }
        else
        {
            wr.updateRenderer(entityLiving);
            wr.needsUpdate = false;
            wr.isUpdating = false;
        }
    }

    public void finishCurrentUpdate()
    {
        if (this.updateThread != null)
        {
            this.updateThread.unpauseToEndOfUpdate();
        }
    }

    public void resumeBackgroundUpdates()
    {
        if (this.updateThread != null)
        {
            this.updateThread.unpause();
        }
    }

    public void pauseBackgroundUpdates()
    {
        if (this.updateThread != null)
        {
            this.updateThread.pause();
        }
    }

    public void clearAllUpdates()
    {
        if (this.updateThread != null)
        {
            this.updateThread.clearAllUpdates();
        }

        this.firstUpdate = true;
    }
}
