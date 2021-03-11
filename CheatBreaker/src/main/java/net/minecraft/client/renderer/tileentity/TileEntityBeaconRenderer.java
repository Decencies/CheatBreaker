package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation field_147523_b = new ResourceLocation("textures/entity/beacon_beam.png");


    public void renderTileEntityAt(TileEntityBeacon p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        float var9 = p_147500_1_.func_146002_i();
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        if (var9 > 0.0F)
        {
            Tessellator var10 = Tessellator.instance;
            this.bindTexture(field_147523_b);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            OpenGlHelper.glBlendFunc(770, 1, 1, 0);
            float var11 = (float)p_147500_1_.getWorldObj().getTotalWorldTime() + p_147500_8_;
            float var12 = -var11 * 0.2F - (float)MathHelper.floor_float(-var11 * 0.1F);
            byte var13 = 1;
            double var14 = (double)var11 * 0.025D * (1.0D - (double)(var13 & 1) * 2.5D);
            var10.startDrawingQuads();
            var10.setColorRGBA(255, 255, 255, 32);
            double var16 = (double)var13 * 0.2D;
            double var18 = 0.5D + Math.cos(var14 + 2.356194490192345D) * var16;
            double var20 = 0.5D + Math.sin(var14 + 2.356194490192345D) * var16;
            double var22 = 0.5D + Math.cos(var14 + (Math.PI / 4D)) * var16;
            double var24 = 0.5D + Math.sin(var14 + (Math.PI / 4D)) * var16;
            double var26 = 0.5D + Math.cos(var14 + 3.9269908169872414D) * var16;
            double var28 = 0.5D + Math.sin(var14 + 3.9269908169872414D) * var16;
            double var30 = 0.5D + Math.cos(var14 + 5.497787143782138D) * var16;
            double var32 = 0.5D + Math.sin(var14 + 5.497787143782138D) * var16;
            double var34 = (double)(256.0F * var9);
            double var36 = 0.0D;
            double var38 = 1.0D;
            double var40 = (double)(-1.0F + var12);
            double var42 = (double)(256.0F * var9) * (0.5D / var16) + var40;
            var10.addVertexWithUV(p_147500_2_ + var18, p_147500_4_ + var34, p_147500_6_ + var20, var38, var42);
            var10.addVertexWithUV(p_147500_2_ + var18, p_147500_4_, p_147500_6_ + var20, var38, var40);
            var10.addVertexWithUV(p_147500_2_ + var22, p_147500_4_, p_147500_6_ + var24, var36, var40);
            var10.addVertexWithUV(p_147500_2_ + var22, p_147500_4_ + var34, p_147500_6_ + var24, var36, var42);
            var10.addVertexWithUV(p_147500_2_ + var30, p_147500_4_ + var34, p_147500_6_ + var32, var38, var42);
            var10.addVertexWithUV(p_147500_2_ + var30, p_147500_4_, p_147500_6_ + var32, var38, var40);
            var10.addVertexWithUV(p_147500_2_ + var26, p_147500_4_, p_147500_6_ + var28, var36, var40);
            var10.addVertexWithUV(p_147500_2_ + var26, p_147500_4_ + var34, p_147500_6_ + var28, var36, var42);
            var10.addVertexWithUV(p_147500_2_ + var22, p_147500_4_ + var34, p_147500_6_ + var24, var38, var42);
            var10.addVertexWithUV(p_147500_2_ + var22, p_147500_4_, p_147500_6_ + var24, var38, var40);
            var10.addVertexWithUV(p_147500_2_ + var30, p_147500_4_, p_147500_6_ + var32, var36, var40);
            var10.addVertexWithUV(p_147500_2_ + var30, p_147500_4_ + var34, p_147500_6_ + var32, var36, var42);
            var10.addVertexWithUV(p_147500_2_ + var26, p_147500_4_ + var34, p_147500_6_ + var28, var38, var42);
            var10.addVertexWithUV(p_147500_2_ + var26, p_147500_4_, p_147500_6_ + var28, var38, var40);
            var10.addVertexWithUV(p_147500_2_ + var18, p_147500_4_, p_147500_6_ + var20, var36, var40);
            var10.addVertexWithUV(p_147500_2_ + var18, p_147500_4_ + var34, p_147500_6_ + var20, var36, var42);
            var10.draw();
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glDepthMask(false);
            var10.startDrawingQuads();
            var10.setColorRGBA(255, 255, 255, 32);
            double var44 = 0.2D;
            double var15 = 0.2D;
            double var17 = 0.8D;
            double var19 = 0.2D;
            double var21 = 0.2D;
            double var23 = 0.8D;
            double var25 = 0.8D;
            double var27 = 0.8D;
            double var29 = (double)(256.0F * var9);
            double var31 = 0.0D;
            double var33 = 1.0D;
            double var35 = (double)(-1.0F + var12);
            double var37 = (double)(256.0F * var9) + var35;
            var10.addVertexWithUV(p_147500_2_ + var44, p_147500_4_ + var29, p_147500_6_ + var15, var33, var37);
            var10.addVertexWithUV(p_147500_2_ + var44, p_147500_4_, p_147500_6_ + var15, var33, var35);
            var10.addVertexWithUV(p_147500_2_ + var17, p_147500_4_, p_147500_6_ + var19, var31, var35);
            var10.addVertexWithUV(p_147500_2_ + var17, p_147500_4_ + var29, p_147500_6_ + var19, var31, var37);
            var10.addVertexWithUV(p_147500_2_ + var25, p_147500_4_ + var29, p_147500_6_ + var27, var33, var37);
            var10.addVertexWithUV(p_147500_2_ + var25, p_147500_4_, p_147500_6_ + var27, var33, var35);
            var10.addVertexWithUV(p_147500_2_ + var21, p_147500_4_, p_147500_6_ + var23, var31, var35);
            var10.addVertexWithUV(p_147500_2_ + var21, p_147500_4_ + var29, p_147500_6_ + var23, var31, var37);
            var10.addVertexWithUV(p_147500_2_ + var17, p_147500_4_ + var29, p_147500_6_ + var19, var33, var37);
            var10.addVertexWithUV(p_147500_2_ + var17, p_147500_4_, p_147500_6_ + var19, var33, var35);
            var10.addVertexWithUV(p_147500_2_ + var25, p_147500_4_, p_147500_6_ + var27, var31, var35);
            var10.addVertexWithUV(p_147500_2_ + var25, p_147500_4_ + var29, p_147500_6_ + var27, var31, var37);
            var10.addVertexWithUV(p_147500_2_ + var21, p_147500_4_ + var29, p_147500_6_ + var23, var33, var37);
            var10.addVertexWithUV(p_147500_2_ + var21, p_147500_4_, p_147500_6_ + var23, var33, var35);
            var10.addVertexWithUV(p_147500_2_ + var44, p_147500_4_, p_147500_6_ + var15, var31, var35);
            var10.addVertexWithUV(p_147500_2_ + var44, p_147500_4_ + var29, p_147500_6_ + var15, var31, var37);
            var10.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(true);
        }
    }

    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        this.renderTileEntityAt((TileEntityBeacon)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
    }
}
