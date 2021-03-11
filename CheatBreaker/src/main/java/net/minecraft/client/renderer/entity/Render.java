package net.minecraft.client.renderer.entity;

import com.cheatbreaker.client.CheatBreaker;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.List;

public abstract class Render
{
    private static final ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
    protected RenderManager renderManager;
    protected RenderBlocks field_147909_c = new RenderBlocks();
    protected float shadowSize;

    /**
     * Determines the darkness of the object's shadow. Higher value makes a darker shadow.
     */
    protected float shadowOpaque = 1.0F;
    private boolean field_147908_f = false;


    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public abstract void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_);

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    public abstract ResourceLocation getEntityTexture(Entity p_110775_1_);

    public boolean func_147905_a()
    {
        return this.field_147908_f;
    }

    protected void bindEntityTexture(Entity p_110777_1_)
    {
        this.bindTexture(this.getEntityTexture(p_110777_1_));
    }

    protected void bindTexture(ResourceLocation p_110776_1_)
    {
        this.renderManager.renderEngine.bindTexture(p_110776_1_);
    }

    /**
     * Renders fire on top of the entity. Args: entity, x, y, z, partialTickTime
     */
    private void renderEntityOnFire(Entity p_76977_1_, double p_76977_2_, double p_76977_4_, double p_76977_6_, float p_76977_8_)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        IIcon var9 = Blocks.fire.func_149840_c(0);
        IIcon var10 = Blocks.fire.func_149840_c(1);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)p_76977_2_, (float)p_76977_4_, (float)p_76977_6_);
        float var11 = p_76977_1_.width * 1.4F;
        GL11.glScalef(var11, var11, var11);
        Tessellator var12 = Tessellator.instance;
        float var13 = 0.5F;
        float var14 = 0.0F;
        float var15 = p_76977_1_.height / var11;
        float var16 = (float)(p_76977_1_.posY - p_76977_1_.boundingBox.minY);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, 0.0F, -0.3F + (float)((int)var15) * 0.02F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var17 = 0.0F;
        int var18 = 0;
        var12.startDrawingQuads();

        while (var15 > 0.0F)
        {
            IIcon var19 = var18 % 2 == 0 ? var9 : var10;
            this.bindTexture(TextureMap.locationBlocksTexture);
            float var20 = var19.getMinU();
            float var21 = var19.getMinV();
            float var22 = var19.getMaxU();
            float var23 = var19.getMaxV();

            if (var18 / 2 % 2 == 0)
            {
                float var24 = var22;
                var22 = var20;
                var20 = var24;
            }

            var12.addVertexWithUV((double)(var13 - var14), (double)(0.0F - var16), (double)var17, (double)var22, (double)var23);
            var12.addVertexWithUV((double)(-var13 - var14), (double)(0.0F - var16), (double)var17, (double)var20, (double)var23);
            var12.addVertexWithUV((double)(-var13 - var14), (double)(1.4F - var16), (double)var17, (double)var20, (double)var21);
            var12.addVertexWithUV((double)(var13 - var14), (double)(1.4F - var16), (double)var17, (double)var22, (double)var21);
            var15 -= 0.45F;
            var16 -= 0.45F;
            var13 *= 0.9F;
            var17 += 0.03F;
            ++var18;
        }

        var12.draw();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    /**
     * Renders the entity shadows at the position, shadow alpha and partialTickTime. Args: entity, x, y, z, shadowAlpha,
     * partialTickTime
     */
    private void renderShadow(Entity p_76975_1_, double p_76975_2_, double p_76975_4_, double p_76975_6_, float p_76975_8_, float p_76975_9_)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.renderManager.renderEngine.bindTexture(shadowTextures);
        World var10 = this.getWorldFromRenderManager();
        GL11.glDepthMask(false);
        float var11 = this.shadowSize;

        if (p_76975_1_ instanceof EntityLiving)
        {
            EntityLiving var12 = (EntityLiving)p_76975_1_;
            var11 *= var12.getRenderSizeModifier();

            if (var12.isChild())
            {
                var11 *= 0.5F;
            }
        }

        double var35 = p_76975_1_.lastTickPosX + (p_76975_1_.posX - p_76975_1_.lastTickPosX) * (double)p_76975_9_;
        double var14 = p_76975_1_.lastTickPosY + (p_76975_1_.posY - p_76975_1_.lastTickPosY) * (double)p_76975_9_ + (double)p_76975_1_.getShadowSize();
        double var16 = p_76975_1_.lastTickPosZ + (p_76975_1_.posZ - p_76975_1_.lastTickPosZ) * (double)p_76975_9_;
        int var18 = MathHelper.floor_double(var35 - (double)var11);
        int var19 = MathHelper.floor_double(var35 + (double)var11);
        int var20 = MathHelper.floor_double(var14 - (double)var11);
        int var21 = MathHelper.floor_double(var14);
        int var22 = MathHelper.floor_double(var16 - (double)var11);
        int var23 = MathHelper.floor_double(var16 + (double)var11);
        double var24 = p_76975_2_ - var35;
        double var26 = p_76975_4_ - var14;
        double var28 = p_76975_6_ - var16;
        Tessellator var30 = Tessellator.instance;
        var30.startDrawingQuads();

        for (int var31 = var18; var31 <= var19; ++var31)
        {
            for (int var32 = var20; var32 <= var21; ++var32)
            {
                for (int var33 = var22; var33 <= var23; ++var33)
                {
                    Block var34 = var10.getBlock(var31, var32 - 1, var33);

                    if (var34.getMaterial() != Material.air && var10.getBlockLightValue(var31, var32, var33) > 3)
                    {
                        this.func_147907_a(var34, p_76975_2_, p_76975_4_ + (double)p_76975_1_.getShadowSize(), p_76975_6_, var31, var32, var33, p_76975_8_, var11, var24, var26 + (double)p_76975_1_.getShadowSize(), var28);
                    }
                }
            }
        }

        var30.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }

    /**
     * Returns the render manager's world object
     */
    private World getWorldFromRenderManager()
    {
        return this.renderManager.worldObj;
    }

    private void func_147907_a(Block p_147907_1_, double p_147907_2_, double p_147907_4_, double p_147907_6_, int p_147907_8_, int p_147907_9_, int p_147907_10_, float p_147907_11_, float p_147907_12_, double p_147907_13_, double p_147907_15_, double p_147907_17_)
    {
        Tessellator var19 = Tessellator.instance;

        if (p_147907_1_.renderAsNormalBlock())
        {
            double var20 = ((double)p_147907_11_ - (p_147907_4_ - ((double)p_147907_9_ + p_147907_15_)) / 2.0D) * 0.5D * (double)this.getWorldFromRenderManager().getLightBrightness(p_147907_8_, p_147907_9_, p_147907_10_);

            if (var20 >= 0.0D)
            {
                if (var20 > 1.0D)
                {
                    var20 = 1.0D;
                }

                var19.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float)var20);
                double var22 = (double)p_147907_8_ + p_147907_1_.getBlockBoundsMinX() + p_147907_13_;
                double var24 = (double)p_147907_8_ + p_147907_1_.getBlockBoundsMaxX() + p_147907_13_;
                double var26 = (double)p_147907_9_ + p_147907_1_.getBlockBoundsMinY() + p_147907_15_ + 0.015625D;
                double var28 = (double)p_147907_10_ + p_147907_1_.getBlockBoundsMinZ() + p_147907_17_;
                double var30 = (double)p_147907_10_ + p_147907_1_.getBlockBoundsMaxZ() + p_147907_17_;
                float var32 = (float)((p_147907_2_ - var22) / 2.0D / (double)p_147907_12_ + 0.5D);
                float var33 = (float)((p_147907_2_ - var24) / 2.0D / (double)p_147907_12_ + 0.5D);
                float var34 = (float)((p_147907_6_ - var28) / 2.0D / (double)p_147907_12_ + 0.5D);
                float var35 = (float)((p_147907_6_ - var30) / 2.0D / (double)p_147907_12_ + 0.5D);
                var19.addVertexWithUV(var22, var26, var28, (double)var32, (double)var34);
                var19.addVertexWithUV(var22, var26, var30, (double)var32, (double)var35);
                var19.addVertexWithUV(var24, var26, var30, (double)var33, (double)var35);
                var19.addVertexWithUV(var24, var26, var28, (double)var33, (double)var34);
            }
        }
    }

    /**
     * Renders a white box with the bounds of the AABB translated by the offset. Args: aabb, x, y, z
     */
    public static void renderOffsetAABB(AxisAlignedBB p_76978_0_, double p_76978_1_, double p_76978_3_, double p_76978_5_)
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator var7 = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var7.startDrawingQuads();
        var7.setTranslation(p_76978_1_, p_76978_3_, p_76978_5_);
        var7.setNormal(0.0F, 0.0F, -1.0F);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        var7.setNormal(0.0F, 0.0F, 1.0F);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var7.setNormal(0.0F, -1.0F, 0.0F);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        var7.setNormal(0.0F, 1.0F, 0.0F);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        var7.setNormal(-1.0F, 0.0F, 0.0F);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        var7.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        var7.setNormal(1.0F, 0.0F, 0.0F);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var7.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        var7.setTranslation(0.0D, 0.0D, 0.0D);
        var7.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Adds to the tesselator a box using the aabb for the bounds. Args: aabb
     */
    public static void renderAABB(AxisAlignedBB p_76980_0_)
    {
        Tessellator var1 = Tessellator.instance;
        var1.startDrawingQuads();
        var1.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.maxY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.minX, p_76980_0_.minY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.minZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.maxY, p_76980_0_.maxZ);
        var1.addVertex(p_76980_0_.maxX, p_76980_0_.minY, p_76980_0_.maxZ);
        var1.draw();
    }

    /**
     * Sets the RenderManager.
     */
    public void setRenderManager(RenderManager p_76976_1_)
    {
        this.renderManager = p_76976_1_;
    }

    /**
     * Renders the entity's shadow and fire (if its on fire). Args: entity, x, y, z, yaw, partialTickTime
     */
    public void doRenderShadowAndFire(Entity p_76979_1_, double p_76979_2_, double p_76979_4_, double p_76979_6_, float p_76979_8_, float p_76979_9_)
    {
        if (this.renderManager.options.fancyGraphics && this.shadowSize > 0.0F && !p_76979_1_.isInvisible())
        {
            double var10 = this.renderManager.getDistanceToCamera(p_76979_1_.posX, p_76979_1_.posY, p_76979_1_.posZ);
            float var12 = (float)((1.0D - var10 / 256.0D) * (double)this.shadowOpaque);

            if (var12 > 0.0F)
            {
                this.renderShadow(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, var12, p_76979_9_);
            }
        }

        if (p_76979_1_.canRenderOnFire())
        {
            this.renderEntityOnFire(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, p_76979_9_);
        }
    }

    /**
     * Returns the font renderer from the set render manager
     */
    public FontRenderer getFontRendererFromRenderManager()
    {
        return this.renderManager.getFontRenderer();
    }

    public void updateIcons(IIconRegister p_94143_1_) {}

    protected void func_147906_a(Entity entity, String string, double d, double d2, double d3, int n) {
        double var10 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);

        if (var10 <= (n * n)) {
            if (CheatBreaker.getInstance().getNetHandler().getNametagsMap().containsKey(entity.getUniqueID())) {
                List<String> list = CheatBreaker.getInstance().getNetHandler().getNametagsMap().get(entity.getUniqueID());
                int n2 = 0;
                for (String string2 : list) {
                    this.boogaloo(-n2, entity, string2, d, d2, d3, n);
                    ++n2;
                }
            } else boogaloo(0.0, entity, string, d, d2, d3, n);
        }
    }

    public void boogaloo(double d, Entity p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_, double p_147906_7_, int p_147906_9_) {
        double var10 = p_147906_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);

        if (var10 <= (double) (p_147906_9_ * p_147906_9_)) {
            FontRenderer var12 = this.getFontRendererFromRenderManager();
            float var13 = 1.6F;
            float var14 = 0.016666668F * var13;
            GL11.glPushMatrix();
//            GL11.glTranslatef((float) p_147906_3_ + 0.0F, (float) p_147906_5_ + p_147906_1_.height + 0.5F, (float) p_147906_7_);
            GL11.glTranslatef((float)p_147906_3_ + 0.0f, (float)(p_147906_5_ + p_147906_1_.height + 0.5F - d / 3), (float)p_147906_7_);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-var14, -var14, var14);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            Tessellator var15 = Tessellator.instance;
            byte var16 = 0;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            var15.startDrawingQuads();
            int var17 = var12.getStringWidth(p_147906_2_) / 2;
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var15.addVertex((-var17 - 1), (-1 + var16), 0.0D);
            var15.addVertex((-var17 - 1), (8 + var16), 0.0D);
            var15.addVertex((var17 + 1), (8 + var16), 0.0D);
            var15.addVertex((var17 + 1), (-1 + var16), 0.0D);
            var15.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            var12.drawString(p_147906_2_, -var12.getStringWidth(p_147906_2_) / 2, var16, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            var12.drawString(p_147906_2_, -var12.getStringWidth(p_147906_2_) / 2, var16, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

//    protected void func_147906_a(Entity p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_, double p_147906_7_, int p_147906_9_)
//    {
//        double var10 = p_147906_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);
//
//        if (var10 <= (double)(p_147906_9_ * p_147906_9_))
//        {
//            FontRenderer var12 = this.getFontRendererFromRenderManager();
//            float var13 = 1.6F;
//            float var14 = 0.016666668F * var13;
//            GL11.glPushMatrix();
//            GL11.glTranslatef((float)p_147906_3_ + 0.0F, (float)p_147906_5_ + p_147906_1_.height + 0.5F, (float)p_147906_7_);
//            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
//            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
//            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
//            GL11.glScalef(-var14, -var14, var14);
//            GL11.glDisable(GL11.GL_LIGHTING);
//            GL11.glDepthMask(false);
//            GL11.glDisable(GL11.GL_DEPTH_TEST);
//            GL11.glEnable(GL11.GL_BLEND);
//            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
//            Tessellator var15 = Tessellator.instance;
//            byte var16 = 0;
//
//            if (p_147906_2_.equals("deadmau5"))
//            {
//                var16 = -10;
//            }
//
//            GL11.glDisable(GL11.GL_TEXTURE_2D);
//            var15.startDrawingQuads();
//            int var17 = var12.getStringWidth(p_147906_2_) / 2;
//            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
//            var15.addVertex((double)(-var17 - 1), (double)(-1 + var16), 0.0D);
//            var15.addVertex((double)(-var17 - 1), (double)(8 + var16), 0.0D);
//            var15.addVertex((double)(var17 + 1), (double)(8 + var16), 0.0D);
//            var15.addVertex((double)(var17 + 1), (double)(-1 + var16), 0.0D);
//            var15.draw();
//            GL11.glEnable(GL11.GL_TEXTURE_2D);
//            var12.drawString(p_147906_2_, -var12.getStringWidth(p_147906_2_) / 2, var16, 553648127);
//            GL11.glEnable(GL11.GL_DEPTH_TEST);
//            GL11.glDepthMask(true);
//            var12.drawString(p_147906_2_, -var12.getStringWidth(p_147906_2_) / 2, var16, -1);
//            GL11.glEnable(GL11.GL_LIGHTING);
//            GL11.glDisable(GL11.GL_BLEND);
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            GL11.glPopMatrix();
//        }
//    }
}
