package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireworkOverlayFX extends EntityFX
{


    protected EntityFireworkOverlayFX(World p_i46357_1_, double p_i46357_2_, double p_i46357_4_, double p_i46357_6_)
    {
        super(p_i46357_1_, p_i46357_2_, p_i46357_4_, p_i46357_6_);
        this.particleMaxAge = 4;
    }

    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_)
    {
        float var8 = 0.25F;
        float var9 = var8 + 0.25F;
        float var10 = 0.125F;
        float var11 = var10 + 0.25F;
        float var12 = 7.1F * MathHelper.sin(((float)this.particleAge + p_70539_2_ - 1.0F) * 0.25F * (float)Math.PI);
        this.particleAlpha = 0.6F - ((float)this.particleAge + p_70539_2_ - 1.0F) * 0.25F * 0.5F;
        float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)p_70539_2_ - interpPosX);
        float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)p_70539_2_ - interpPosY);
        float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)p_70539_2_ - interpPosZ);
        p_70539_1_.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        p_70539_1_.addVertexWithUV((double)(var13 - p_70539_3_ * var12 - p_70539_6_ * var12), (double)(var14 - p_70539_4_ * var12), (double)(var15 - p_70539_5_ * var12 - p_70539_7_ * var12), (double)var9, (double)var11);
        p_70539_1_.addVertexWithUV((double)(var13 - p_70539_3_ * var12 + p_70539_6_ * var12), (double)(var14 + p_70539_4_ * var12), (double)(var15 - p_70539_5_ * var12 + p_70539_7_ * var12), (double)var9, (double)var10);
        p_70539_1_.addVertexWithUV((double)(var13 + p_70539_3_ * var12 + p_70539_6_ * var12), (double)(var14 + p_70539_4_ * var12), (double)(var15 + p_70539_5_ * var12 + p_70539_7_ * var12), (double)var8, (double)var10);
        p_70539_1_.addVertexWithUV((double)(var13 + p_70539_3_ * var12 - p_70539_6_ * var12), (double)(var14 - p_70539_4_ * var12), (double)(var15 + p_70539_5_ * var12 - p_70539_7_ * var12), (double)var8, (double)var11);
    }
}
