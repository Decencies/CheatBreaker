package net.minecraft.src;

import java.util.HashSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
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

public class WorldRendererThreaded extends WorldRenderer
{
    private int glRenderListWork;
    private int glRenderListBoundingBox;
    public boolean[] tempSkipRenderPass = new boolean[2];
    public TesselatorVertexState tempVertexState;
    private Tessellator tessellatorWork = null;

    public WorldRendererThreaded(World par1World, List par2List, int par3, int par4, int par5, int par6)
    {
        super(par1World, par2List, par3, par4, par5, par6);
        RenderGlobal renderGlobal = Minecraft.getMinecraft().renderGlobal;
        this.glRenderListWork = renderGlobal.displayListAllocator.allocateDisplayLists(2);
        this.glRenderListBoundingBox = this.glRenderList + 2;
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
            this.updateRenderer((IWrUpdateListener)null);
            this.finishUpdate();
        }
    }

    public void updateRenderer(IWrUpdateListener updateListener)
    {
        if (this.worldObj != null)
        {
            this.needsUpdate = false;
            int xMin = this.posX;
            int yMin = this.posY;
            int zMin = this.posZ;
            int xMax = this.posX + 16;
            int yMax = this.posY + 16;
            int zMax = this.posZ + 16;

            for (int hashset = 0; hashset < this.tempSkipRenderPass.length; ++hashset)
            {
                this.tempSkipRenderPass[hashset] = true;
            }

            Chunk.isLit = false;
            HashSet var30 = new HashSet();
            var30.addAll(this.tileEntityRenderers);
            this.tileEntityRenderers.clear();
            Minecraft var9 = Minecraft.getMinecraft();
            EntityLivingBase var10 = var9.renderViewEntity;
            int viewEntityPosX = MathHelper.floor_double(var10.posX);
            int viewEntityPosY = MathHelper.floor_double(var10.posY);
            int viewEntityPosZ = MathHelper.floor_double(var10.posZ);
            byte one = 1;
            ChunkCacheOF chunkcache = new ChunkCacheOF(this.worldObj, xMin - one, yMin - one, zMin - one, xMax + one, yMax + one, zMax + one, one);

            if (!chunkcache.extendedLevelsInChunkCache())
            {
                ++chunksUpdated;
                chunkcache.renderStart();
                RenderBlocks hashset1 = new RenderBlocks(chunkcache);
                Reflector.callVoid(Reflector.ForgeHooksClient_setWorldRendererRB, new Object[] {hashset1});
                this.bytesDrawn = 0;
                this.tempVertexState = null;
                this.tessellator = Tessellator.instance;
                boolean hasForge = Reflector.ForgeHooksClient.exists();
                WrUpdateControl uc = new WrUpdateControl();

                for (int renderPass = 0; renderPass < 2; ++renderPass)
                {
                    uc.setRenderPass(renderPass);
                    boolean renderNextPass = false;
                    boolean hasRenderedBlocks = false;
                    boolean hasGlList = false;

                    for (int y = yMin; y < yMax; ++y)
                    {
                        if (hasRenderedBlocks && updateListener != null)
                        {
                            updateListener.updating(uc);
                            this.tessellator = Tessellator.instance;
                        }

                        for (int z = zMin; z < zMax; ++z)
                        {
                            for (int x = xMin; x < xMax; ++x)
                            {
                                Block block = chunkcache.getBlock(x, y, z);

                                if (block.getMaterial() != Material.air)
                                {
                                    if (!hasGlList)
                                    {
                                        hasGlList = true;
                                        this.preRenderBlocksThreaded(renderPass);
                                    }

                                    boolean hasTileEntity = false;

                                    if (hasForge)
                                    {
                                        hasTileEntity = Reflector.callBoolean(block, Reflector.ForgeBlock_hasTileEntity, new Object[] {Integer.valueOf(chunkcache.getBlockMetadata(x, y, z))});
                                    }
                                    else
                                    {
                                        hasTileEntity = block.hasTileEntity();
                                    }

                                    if (renderPass == 0 && hasTileEntity)
                                    {
                                        TileEntity blockPass = chunkcache.getTileEntity(x, y, z);

                                        if (TileEntityRendererDispatcher.instance.hasSpecialRenderer(blockPass))
                                        {
                                            this.tileEntityRenderers.add(blockPass);
                                        }
                                    }

                                    int var32 = block.getRenderBlockPass();

                                    if (var32 > renderPass)
                                    {
                                        renderNextPass = true;
                                    }

                                    boolean canRender = var32 == renderPass;

                                    if (Reflector.ForgeBlock_canRenderInPass.exists())
                                    {
                                        canRender = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInPass, new Object[] {Integer.valueOf(renderPass)});
                                    }

                                    if (canRender)
                                    {
                                        hasRenderedBlocks |= hashset1.renderBlockByRenderType(block, x, y, z);

                                        if (block.getRenderType() == 0 && x == viewEntityPosX && y == viewEntityPosY && z == viewEntityPosZ)
                                        {
                                            hashset1.setRenderFromInside(true);
                                            hashset1.setRenderAllFaces(true);
                                            hashset1.renderBlockByRenderType(block, x, y, z);
                                            hashset1.setRenderFromInside(false);
                                            hashset1.setRenderAllFaces(false);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (hasRenderedBlocks)
                    {
                        this.tempSkipRenderPass[renderPass] = false;
                    }

                    if (hasGlList)
                    {
                        if (updateListener != null)
                        {
                            updateListener.updating(uc);
                        }

                        this.tessellator = Tessellator.instance;
                        this.postRenderBlocksThreaded(renderPass, this.renderGlobal.renderViewEntity);
                    }
                    else
                    {
                        hasRenderedBlocks = false;
                    }

                    if (!renderNextPass)
                    {
                        break;
                    }
                }

                Reflector.callVoid(Reflector.ForgeHooksClient_setWorldRendererRB, new Object[] {(RenderBlocks)null});
                chunkcache.renderFinish();
            }

            HashSet var31 = new HashSet();
            var31.addAll(this.tileEntityRenderers);
            var31.removeAll(var30);
            this.tileEntities.addAll(var31);
            var30.removeAll(this.tileEntityRenderers);
            this.tileEntities.removeAll(var30);
            this.isChunkLit = Chunk.isLit;
            this.isInitialized = true;
        }
    }

    protected void preRenderBlocksThreaded(int renderpass)
    {
        GL11.glNewList(this.glRenderListWork + renderpass, GL11.GL_COMPILE);
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

    protected void postRenderBlocksThreaded(int renderpass, EntityLivingBase entityLiving)
    {
        if (Config.isTranslucentBlocksFancy() && renderpass == 1 && !this.tempSkipRenderPass[renderpass])
        {
            this.tempVertexState = this.tessellator.getVertexState((float)entityLiving.posX, (float)entityLiving.posY, (float)entityLiving.posZ);
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
        int temp = this.glRenderList;
        this.glRenderList = this.glRenderListWork;
        this.glRenderListWork = temp;
        int lightCache;

        for (lightCache = 0; lightCache < 2; ++lightCache)
        {
            if (!this.skipRenderPass[lightCache])
            {
                GL11.glNewList(this.glRenderListWork + lightCache, GL11.GL_COMPILE);
                GL11.glEndList();
            }
        }

        for (lightCache = 0; lightCache < 2; ++lightCache)
        {
            this.skipRenderPass[lightCache] = this.tempSkipRenderPass[lightCache];
        }

        this.skipAllRenderPasses = this.skipRenderPass[0] && this.skipRenderPass[1];

        if (this.needsBoxUpdate && !this.skipAllRenderPasses())
        {
            GL11.glNewList(this.glRenderListBoundingBox, GL11.GL_COMPILE);
            RenderItem.renderAABB(AxisAlignedBB.getBoundingBox((double)this.posXClip, (double)this.posYClip, (double)this.posZClip, (double)(this.posXClip + 16), (double)(this.posYClip + 16), (double)(this.posZClip + 16)));
            GL11.glEndList();
            this.needsBoxUpdate = false;
        }

        this.vertexState = this.tempVertexState;
        this.isVisible = true;
        this.isVisibleFromPosition = false;

        if (Reflector.LightCache.exists())
        {
            Object var3 = Reflector.getFieldValue(Reflector.LightCache_cache);
            Reflector.callVoid(var3, Reflector.LightCache_clear, new Object[0]);
            Reflector.callVoid(Reflector.BlockCoord_resetPool, new Object[0]);
        }

        this.updateFinished();
    }

    /**
     * Renders the occlusion query GL List
     */
    public void callOcclusionQueryList()
    {
        GL11.glCallList(this.glRenderListBoundingBox);
    }
}
