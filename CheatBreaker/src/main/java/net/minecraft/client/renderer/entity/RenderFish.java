package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderFish extends Render
{
    private static final ResourceLocation field_110792_a = new ResourceLocation("textures/particle/particles.png");
    

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityFishHook p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(p_76986_1_);
        Tessellator var10 = Tessellator.instance;
        byte var11 = 1;
        byte var12 = 2;
        float var13 = (float)(var11 * 8 + 0) / 128.0F;
        float var14 = (float)(var11 * 8 + 8) / 128.0F;
        float var15 = (float)(var12 * 8 + 0) / 128.0F;
        float var16 = (float)(var12 * 8 + 8) / 128.0F;
        float var17 = 1.0F;
        float var18 = 0.5F;
        float var19 = 0.5F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        var10.startDrawingQuads();
        var10.setNormal(0.0F, 1.0F, 0.0F);
        var10.addVertexWithUV((double)(0.0F - var18), (double)(0.0F - var19), 0.0D, (double)var13, (double)var16);
        var10.addVertexWithUV((double)(var17 - var18), (double)(0.0F - var19), 0.0D, (double)var14, (double)var16);
        var10.addVertexWithUV((double)(var17 - var18), (double)(1.0F - var19), 0.0D, (double)var14, (double)var15);
        var10.addVertexWithUV((double)(0.0F - var18), (double)(1.0F - var19), 0.0D, (double)var13, (double)var15);
        var10.draw();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();

        if (p_76986_1_.field_146042_b != null)
        {
            float var20 = p_76986_1_.field_146042_b.getSwingProgress(p_76986_9_);
            float var21 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI);
            Vec3 var22 = Vec3.createVectorHelper(-0.5D, 0.03D, 0.8D);
            var22.rotateAroundX(-(p_76986_1_.field_146042_b.prevRotationPitch + (p_76986_1_.field_146042_b.rotationPitch - p_76986_1_.field_146042_b.prevRotationPitch) * p_76986_9_) * (float)Math.PI / 180.0F);
            var22.rotateAroundY(-(p_76986_1_.field_146042_b.prevRotationYaw + (p_76986_1_.field_146042_b.rotationYaw - p_76986_1_.field_146042_b.prevRotationYaw) * p_76986_9_) * (float)Math.PI / 180.0F);
            var22.rotateAroundY(var21 * 0.5F);
            var22.rotateAroundX(-var21 * 0.7F);
            double var23 = p_76986_1_.field_146042_b.prevPosX + (p_76986_1_.field_146042_b.posX - p_76986_1_.field_146042_b.prevPosX) * (double)p_76986_9_ + var22.xCoord;
            double var25 = p_76986_1_.field_146042_b.prevPosY + (p_76986_1_.field_146042_b.posY - p_76986_1_.field_146042_b.prevPosY) * (double)p_76986_9_ + var22.yCoord;
            double var27 = p_76986_1_.field_146042_b.prevPosZ + (p_76986_1_.field_146042_b.posZ - p_76986_1_.field_146042_b.prevPosZ) * (double)p_76986_9_ + var22.zCoord;
            double var29 = p_76986_1_.field_146042_b == Minecraft.getMinecraft().thePlayer ? 0.0D : (double)p_76986_1_.field_146042_b.getEyeHeight();

            if (this.renderManager.options.thirdPersonView > 0 || p_76986_1_.field_146042_b != Minecraft.getMinecraft().thePlayer)
            {
                float var31 = (p_76986_1_.field_146042_b.prevRenderYawOffset + (p_76986_1_.field_146042_b.renderYawOffset - p_76986_1_.field_146042_b.prevRenderYawOffset) * p_76986_9_) * (float)Math.PI / 180.0F;
                double var32 = (double)MathHelper.sin(var31);
                double var34 = (double)MathHelper.cos(var31);
                var23 = p_76986_1_.field_146042_b.prevPosX + (p_76986_1_.field_146042_b.posX - p_76986_1_.field_146042_b.prevPosX) * (double)p_76986_9_ - var34 * 0.35D - var32 * 0.85D;
                var25 = p_76986_1_.field_146042_b.prevPosY + var29 + (p_76986_1_.field_146042_b.posY - p_76986_1_.field_146042_b.prevPosY) * (double)p_76986_9_ - 0.45D;
                var27 = p_76986_1_.field_146042_b.prevPosZ + (p_76986_1_.field_146042_b.posZ - p_76986_1_.field_146042_b.prevPosZ) * (double)p_76986_9_ - var32 * 0.35D + var34 * 0.85D;
            }

            double var46 = p_76986_1_.prevPosX + (p_76986_1_.posX - p_76986_1_.prevPosX) * (double)p_76986_9_;
            double var33 = p_76986_1_.prevPosY + (p_76986_1_.posY - p_76986_1_.prevPosY) * (double)p_76986_9_ + 0.25D;
            double var35 = p_76986_1_.prevPosZ + (p_76986_1_.posZ - p_76986_1_.prevPosZ) * (double)p_76986_9_;
            double var37 = (double)((float)(var23 - var46));
            double var39 = (double)((float)(var25 - var33));
            double var41 = (double)((float)(var27 - var35));
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            var10.startDrawing(3);
            var10.setColorOpaque_I(0);
            byte var43 = 16;

            for (int var44 = 0; var44 <= var43; ++var44)
            {
                float var45 = (float)var44 / (float)var43;
                var10.addVertex(p_76986_2_ + var37 * (double)var45, p_76986_4_ + var39 * (double)(var45 * var45 + var45) * 0.5D + 0.25D, p_76986_6_ + var41 * (double)var45);
            }

            var10.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityFishHook p_110775_1_)
    {
        return field_110792_a;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    public ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityFishHook)p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityFishHook)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
