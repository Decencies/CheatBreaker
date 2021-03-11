package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

public abstract class RenderLiving extends RendererLivingEntity
{


    public RenderLiving(ModelBase p_i1262_1_, float p_i1262_2_)
    {
        super(p_i1262_1_, p_i1262_2_);
    }

    protected boolean func_110813_b(EntityLiving p_110813_1_)
    {
        return super.func_110813_b(p_110813_1_) && (p_110813_1_.getAlwaysRenderNameTagForRender() || p_110813_1_.hasCustomNameTag() && p_110813_1_ == this.renderManager.field_147941_i);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        super.doRender((EntityLivingBase)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        this.func_110827_b(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    private double func_110828_a(double p_110828_1_, double p_110828_3_, double p_110828_5_)
    {
        return p_110828_1_ + (p_110828_3_ - p_110828_1_) * p_110828_5_;
    }

    protected void func_110827_b(EntityLiving p_110827_1_, double p_110827_2_, double p_110827_4_, double p_110827_6_, float p_110827_8_, float p_110827_9_)
    {
        Entity var10 = p_110827_1_.getLeashedToEntity();

        if (var10 != null)
        {
            p_110827_4_ -= (1.6D - (double)p_110827_1_.height) * 0.5D;
            Tessellator var11 = Tessellator.instance;
            double var12 = this.func_110828_a((double)var10.prevRotationYaw, (double)var10.rotationYaw, (double)(p_110827_9_ * 0.5F)) * 0.01745329238474369D;
            double var14 = this.func_110828_a((double)var10.prevRotationPitch, (double)var10.rotationPitch, (double)(p_110827_9_ * 0.5F)) * 0.01745329238474369D;
            double var16 = Math.cos(var12);
            double var18 = Math.sin(var12);
            double var20 = Math.sin(var14);

            if (var10 instanceof EntityHanging)
            {
                var16 = 0.0D;
                var18 = 0.0D;
                var20 = -1.0D;
            }

            double var22 = Math.cos(var14);
            double var24 = this.func_110828_a(var10.prevPosX, var10.posX, (double)p_110827_9_) - var16 * 0.7D - var18 * 0.5D * var22;
            double var26 = this.func_110828_a(var10.prevPosY + (double)var10.getEyeHeight() * 0.7D, var10.posY + (double)var10.getEyeHeight() * 0.7D, (double)p_110827_9_) - var20 * 0.5D - 0.25D;
            double var28 = this.func_110828_a(var10.prevPosZ, var10.posZ, (double)p_110827_9_) - var18 * 0.7D + var16 * 0.5D * var22;
            double var30 = this.func_110828_a((double)p_110827_1_.prevRenderYawOffset, (double)p_110827_1_.renderYawOffset, (double)p_110827_9_) * 0.01745329238474369D + (Math.PI / 2D);
            var16 = Math.cos(var30) * (double)p_110827_1_.width * 0.4D;
            var18 = Math.sin(var30) * (double)p_110827_1_.width * 0.4D;
            double var32 = this.func_110828_a(p_110827_1_.prevPosX, p_110827_1_.posX, (double)p_110827_9_) + var16;
            double var34 = this.func_110828_a(p_110827_1_.prevPosY, p_110827_1_.posY, (double)p_110827_9_);
            double var36 = this.func_110828_a(p_110827_1_.prevPosZ, p_110827_1_.posZ, (double)p_110827_9_) + var18;
            p_110827_2_ += var16;
            p_110827_6_ += var18;
            double var38 = (double)((float)(var24 - var32));
            double var40 = (double)((float)(var26 - var34));
            double var42 = (double)((float)(var28 - var36));
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            boolean var44 = true;
            double var45 = 0.025D;
            var11.startDrawing(5);
            int var47;
            float var48;

            for (var47 = 0; var47 <= 24; ++var47)
            {
                if (var47 % 2 == 0)
                {
                    var11.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
                }
                else
                {
                    var11.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
                }

                var48 = (float)var47 / 24.0F;
                var11.addVertex(p_110827_2_ + var38 * (double)var48 + 0.0D, p_110827_4_ + var40 * (double)(var48 * var48 + var48) * 0.5D + (double)((24.0F - (float)var47) / 18.0F + 0.125F), p_110827_6_ + var42 * (double)var48);
                var11.addVertex(p_110827_2_ + var38 * (double)var48 + 0.025D, p_110827_4_ + var40 * (double)(var48 * var48 + var48) * 0.5D + (double)((24.0F - (float)var47) / 18.0F + 0.125F) + 0.025D, p_110827_6_ + var42 * (double)var48);
            }

            var11.draw();
            var11.startDrawing(5);

            for (var47 = 0; var47 <= 24; ++var47)
            {
                if (var47 % 2 == 0)
                {
                    var11.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
                }
                else
                {
                    var11.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
                }

                var48 = (float)var47 / 24.0F;
                var11.addVertex(p_110827_2_ + var38 * (double)var48 + 0.0D, p_110827_4_ + var40 * (double)(var48 * var48 + var48) * 0.5D + (double)((24.0F - (float)var47) / 18.0F + 0.125F) + 0.025D, p_110827_6_ + var42 * (double)var48);
                var11.addVertex(p_110827_2_ + var38 * (double)var48 + 0.025D, p_110827_4_ + var40 * (double)(var48 * var48 + var48) * 0.5D + (double)((24.0F - (float)var47) / 18.0F + 0.125F), p_110827_6_ + var42 * (double)var48 + 0.025D);
            }

            var11.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
    }

    protected boolean func_110813_b(EntityLivingBase p_110813_1_)
    {
        return this.func_110813_b((EntityLiving)p_110813_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
