package net.minecraft.src;

import java.util.HashSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.TesselatorVertexState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;

public class WorldRendererSmooth extends WorldRenderer
{
    private WrUpdateState updateState = new WrUpdateState();
    public int activeSet = 0;
    public int[] activeListIndex = new int[] {0, 0};
    public int[][][] glWorkLists = new int[2][2][16];
    public boolean[] tempSkipRenderPass = new boolean[2];
    public TesselatorVertexState tempVertexState;

    public WorldRendererSmooth(World par1World, List par2List, int par3, int par4, int par5, int par6)
    {
        super(par1World, par2List, par3, par4, par5, par6);
    }

    private void checkGlWorkLists()
    {
        if (this.glWorkLists[0][0][0] <= 0)
        {
            int glWorkBase = this.renderGlobal.displayListAllocator.allocateDisplayLists(64);

            for (int set = 0; set < 2; ++set)
            {
                int setBase = glWorkBase + set * 2 * 16;

                for (int pass = 0; pass < 2; ++pass)
                {
                    int passBase = setBase + pass * 16;

                    for (int t = 0; t < 16; ++t)
                    {
                        this.glWorkLists[set][pass][t] = passBase + t;
                    }
                }
            }
        }
    }

    /**
     * Sets a new position for the renderer and setting it up so it can be reloaded with the new data for that position
     */
    public void setPosition(int px, int py, int pz)
    {
        if (this.isUpdating)
        {
            WrUpdates.finishCurrentUpdate();
        }

        super.setPosition(px, py, pz);
    }

    public void updateRenderer()
    {
        if (this.worldObj != null)
        {
            this.updateRenderer(0L);
            this.finishUpdate();
        }
    }

    public boolean updateRenderer(long finishTime)
    {
        if (this.worldObj == null)
        {
            return true;
        }
        else
        {
            this.needsUpdate = false;

            if (!this.isUpdating)
            {
                if (this.needsBoxUpdate)
                {
                    GL11.glNewList(this.glRenderList + 2, GL11.GL_COMPILE);
                    RenderItem.renderAABB(AxisAlignedBB.getBoundingBox((double)this.posXClip, (double)this.posYClip, (double)this.posZClip, (double)(this.posXClip + 16), (double)(this.posYClip + 16), (double)(this.posZClip + 16)));
                    GL11.glEndList();
                    this.needsBoxUpdate = false;
                }

                if (Reflector.LightCache.exists())
                {
                    Object xMin = Reflector.getFieldValue(Reflector.LightCache_cache);
                    Reflector.callVoid(xMin, Reflector.LightCache_clear, new Object[0]);
                    Reflector.callVoid(Reflector.BlockCoord_resetPool, new Object[0]);
                }

                Chunk.isLit = false;
            }

            int var27 = this.posX;
            int yMin = this.posY;
            int zMin = this.posZ;
            int xMax = this.posX + 16;
            int yMax = this.posY + 16;
            int zMax = this.posZ + 16;
            ChunkCacheOF chunkcache = null;
            RenderBlocks renderblocks = null;
            HashSet setOldEntityRenders = null;
            int viewEntityPosX = 0;
            int viewEntityPosY = 0;
            int viewEntityPosZ = 0;

            if (!this.isUpdating)
            {
                for (int setNewEntityRenderers = 0; setNewEntityRenderers < 2; ++setNewEntityRenderers)
                {
                    this.tempSkipRenderPass[setNewEntityRenderers] = true;
                }

                this.tempVertexState = null;
                Minecraft var28 = Minecraft.getMinecraft();
                EntityLivingBase renderPass = var28.renderViewEntity;
                viewEntityPosX = MathHelper.floor_double(renderPass.posX);
                viewEntityPosY = MathHelper.floor_double(renderPass.posY);
                viewEntityPosZ = MathHelper.floor_double(renderPass.posZ);
                byte renderNextPass = 1;
                chunkcache = new ChunkCacheOF(this.worldObj, var27 - renderNextPass, yMin - renderNextPass, zMin - renderNextPass, xMax + renderNextPass, yMax + renderNextPass, zMax + renderNextPass, renderNextPass);
                renderblocks = new RenderBlocks(chunkcache);
                Reflector.callVoid(Reflector.ForgeHooksClient_setWorldRendererRB, new Object[] {renderblocks});
                setOldEntityRenders = new HashSet();
                setOldEntityRenders.addAll(this.tileEntityRenderers);
                this.tileEntityRenderers.clear();
            }

            if (this.isUpdating || !chunkcache.extendedLevelsInChunkCache())
            {
                this.bytesDrawn = 0;

                if (!this.isUpdating)
                {
                    chunkcache.renderStart();
                }

                this.tessellator = Tessellator.instance;
                boolean var29 = Reflector.ForgeHooksClient.exists();
                this.checkGlWorkLists();

                for (int var31 = 0; var31 < 2; ++var31)
                {
                    boolean var32 = false;
                    boolean hasRenderedBlocks = false;
                    boolean hasGlList = false;

                    for (int y = yMin; y < yMax; ++y)
                    {
                        if (this.isUpdating)
                        {
                            this.isUpdating = false;
                            chunkcache = this.updateState.chunkcache;
                            renderblocks = this.updateState.renderblocks;
                            Reflector.callVoid(Reflector.ForgeHooksClient_setWorldRendererRB, new Object[] {renderblocks});
                            setOldEntityRenders = this.updateState.setOldEntityRenders;
                            viewEntityPosX = this.updateState.viewEntityPosX;
                            viewEntityPosY = this.updateState.viewEntityPosY;
                            viewEntityPosZ = this.updateState.viewEntityPosZ;
                            var31 = this.updateState.renderPass;
                            y = this.updateState.y;
                            var32 = this.updateState.flag;
                            hasRenderedBlocks = this.updateState.hasRenderedBlocks;
                            hasGlList = this.updateState.hasGlList;

                            if (hasGlList)
                            {
                                this.preRenderBlocksSmooth(var31);
                            }
                        }
                        else if (hasGlList && finishTime != 0L && System.nanoTime() - finishTime > 0L && this.activeListIndex[var31] < 15)
                        {
                            if (hasRenderedBlocks)
                            {
                                this.tempSkipRenderPass[var31] = false;
                            }

                            this.postRenderBlocksSmooth(var31, this.renderGlobal.renderViewEntity, false);
                            ++this.activeListIndex[var31];
                            this.updateState.chunkcache = chunkcache;
                            this.updateState.renderblocks = renderblocks;
                            this.updateState.setOldEntityRenders = setOldEntityRenders;
                            this.updateState.viewEntityPosX = viewEntityPosX;
                            this.updateState.viewEntityPosY = viewEntityPosY;
                            this.updateState.viewEntityPosZ = viewEntityPosZ;
                            this.updateState.renderPass = var31;
                            this.updateState.y = y;
                            this.updateState.flag = var32;
                            this.updateState.hasRenderedBlocks = hasRenderedBlocks;
                            this.updateState.hasGlList = hasGlList;
                            this.isUpdating = true;
                            return false;
                        }

                        for (int z = zMin; z < zMax; ++z)
                        {
                            for (int x = var27; x < xMax; ++x)
                            {
                                Block block = chunkcache.getBlock(x, y, z);

                                if (block.getMaterial() != Material.air)
                                {
                                    if (!hasGlList)
                                    {
                                        hasGlList = true;
                                        this.preRenderBlocksSmooth(var31);
                                    }

                                    boolean hasTileEntity = false;

                                    if (var29)
                                    {
                                        hasTileEntity = Reflector.callBoolean(block, Reflector.ForgeBlock_hasTileEntity, new Object[] {Integer.valueOf(chunkcache.getBlockMetadata(x, y, z))});
                                    }
                                    else
                                    {
                                        hasTileEntity = block.hasTileEntity();
                                    }

                                    if (var31 == 0 && hasTileEntity)
                                    {
                                        TileEntity blockPass = chunkcache.getTileEntity(x, y, z);

                                        if (TileEntityRendererDispatcher.instance.hasSpecialRenderer(blockPass))
                                        {
                                            this.tileEntityRenderers.add(blockPass);
                                        }
                                    }

                                    int var33 = block.getRenderBlockPass();

                                    if (var33 > var31)
                                    {
                                        var32 = true;
                                    }

                                    boolean canRender = var33 == var31;

                                    if (Reflector.ForgeBlock_canRenderInPass.exists())
                                    {
                                        canRender = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInPass, new Object[] {Integer.valueOf(var31)});
                                    }

                                    if (canRender)
                                    {
                                        hasRenderedBlocks |= renderblocks.renderBlockByRenderType(block, x, y, z);

                                        if (block.getRenderType() == 0 && x == viewEntityPosX && y == viewEntityPosY && z == viewEntityPosZ)
                                        {
                                            renderblocks.setRenderFromInside(true);
                                            renderblocks.setRenderAllFaces(true);
                                            renderblocks.renderBlockByRenderType(block, x, y, z);
                                            renderblocks.setRenderFromInside(false);
                                            renderblocks.setRenderAllFaces(false);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (hasRenderedBlocks)
                    {
                        this.tempSkipRenderPass[var31] = false;
                    }

                    if (hasGlList)
                    {
                        this.postRenderBlocksSmooth(var31, this.renderGlobal.renderViewEntity, true);
                    }
                    else
                    {
                        hasRenderedBlocks = false;
                    }

                    if (!var32)
                    {
                        break;
                    }
                }

                Reflector.callVoid(Reflector.ForgeHooksClient_setWorldRendererRB, new Object[] {(RenderBlocks)null});
                chunkcache.renderFinish();
            }

            HashSet var30 = new HashSet();
            var30.addAll(this.tileEntityRenderers);
            var30.removeAll(setOldEntityRenders);
            this.tileEntities.addAll(var30);
            setOldEntityRenders.removeAll(this.tileEntityRenderers);
            this.tileEntities.removeAll(setOldEntityRenders);
            this.isChunkLit = Chunk.isLit;
            this.isInitialized = true;
            ++chunksUpdated;
            this.isVisible = true;
            this.isVisibleFromPosition = false;
            this.skipRenderPass[0] = this.tempSkipRenderPass[0];
            this.skipRenderPass[1] = this.tempSkipRenderPass[1];
            this.skipAllRenderPasses = this.skipRenderPass[0] && this.skipRenderPass[1];
            this.vertexState = this.tempVertexState;
            this.isUpdating = false;
            this.updateFinished();
            return true;
        }
    }

    protected void preRenderBlocksSmooth(int renderpass)
    {
        GL11.glNewList(this.glWorkLists[this.activeSet][renderpass][this.activeListIndex[renderpass]], GL11.GL_COMPILE);
        this.tessellator.setRenderingChunk(true);

        if (Config.isFastRender())
        {
            Reflector.callVoid(Reflector.ForgeHooksClient_onPreRenderWorld, new Object[] {this, Integer.valueOf(renderpass)});
            this.tessellator.startDrawingQuads();
            this.tessellator.setTranslation((double)(-globalChunkOffsetX), 0.0D, (double)(-globalChunkOffsetZ));
        }
        else
        {
            GL11.glPushMatrix();
            this.setupGLTranslation();
            float var2 = 1.000001F;
            GL11.glTranslatef(-8.0F, -8.0F, -8.0F);
            GL11.glScalef(var2, var2, var2);
            GL11.glTranslatef(8.0F, 8.0F, 8.0F);
            Reflector.callVoid(Reflector.ForgeHooksClient_onPreRenderWorld, new Object[] {this, Integer.valueOf(renderpass)});
            this.tessellator.startDrawingQuads();
            this.tessellator.setTranslation((double)(-this.posX), (double)(-this.posY), (double)(-this.posZ));
        }
    }

    protected void postRenderBlocksSmooth(int renderpass, EntityLivingBase entityLiving, boolean updateFinished)
    {
        if (Config.isTranslucentBlocksFancy() && renderpass == 1 && !this.tempSkipRenderPass[renderpass])
        {
            TesselatorVertexState tsv = this.tessellator.getVertexState((float)entityLiving.posX, (float)entityLiving.posY, (float)entityLiving.posZ);

            if (this.tempVertexState == null)
            {
                this.tempVertexState = tsv;
            }
            else
            {
                this.tempVertexState.addTessellatorVertexState(tsv);
            }
        }

        this.bytesDrawn += this.tessellator.draw();
        Reflector.callVoid(Reflector.ForgeHooksClient_onPostRenderWorld, new Object[] {this, Integer.valueOf(renderpass)});
        this.tessellator.setRenderingChunk(false);

        if (!Config.isFastRender())
        {
            GL11.glPopMatrix();
        }

        GL11.glEndList();
        this.tessellator.setTranslation(0.0D, 0.0D, 0.0D);
    }

    public void finishUpdate()
    {
        int pass;
        int i;
        int list;

        for (pass = 0; pass < 2; ++pass)
        {
            if (!this.skipRenderPass[pass])
            {
                GL11.glNewList(this.glRenderList + pass, GL11.GL_COMPILE);

                for (i = 0; i <= this.activeListIndex[pass]; ++i)
                {
                    list = this.glWorkLists[this.activeSet][pass][i];
                    GL11.glCallList(list);
                }

                GL11.glEndList();
            }
        }

        if (this.activeSet == 0)
        {
            this.activeSet = 1;
        }
        else
        {
            this.activeSet = 0;
        }

        for (pass = 0; pass < 2; ++pass)
        {
            if (!this.skipRenderPass[pass])
            {
                for (i = 0; i <= this.activeListIndex[pass]; ++i)
                {
                    list = this.glWorkLists[this.activeSet][pass][i];
                    GL11.glNewList(list, GL11.GL_COMPILE);
                    GL11.glEndList();
                }
            }
        }

        for (pass = 0; pass < 2; ++pass)
        {
            this.activeListIndex[pass] = 0;
        }
    }
}
