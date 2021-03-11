package net.minecraft.client.renderer.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderEndPortal extends TileEntitySpecialRenderer
{
    private static final ResourceLocation field_147529_c = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation field_147526_d = new ResourceLocation("textures/entity/end_portal.png");
    private static final Random field_147527_e = new Random(31100L);
    FloatBuffer field_147528_b = GLAllocation.createDirectFloatBuffer(16);


    public void renderTileEntityAt(TileEntityEndPortal p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        float var9 = (float)this.field_147501_a.field_147560_j;
        float var10 = (float)this.field_147501_a.field_147561_k;
        float var11 = (float)this.field_147501_a.field_147558_l;
        GL11.glDisable(GL11.GL_LIGHTING);
        field_147527_e.setSeed(31100L);
        float var12 = 0.75F;

        for (int var13 = 0; var13 < 16; ++var13)
        {
            GL11.glPushMatrix();
            float var14 = (float)(16 - var13);
            float var15 = 0.0625F;
            float var16 = 1.0F / (var14 + 1.0F);

            if (var13 == 0)
            {
                this.bindTexture(field_147529_c);
                var16 = 0.1F;
                var14 = 65.0F;
                var15 = 0.125F;
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }

            if (var13 == 1)
            {
                this.bindTexture(field_147526_d);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                var15 = 0.5F;
            }

            float var17 = (float)(-(p_147500_4_ + (double)var12));
            float var18 = var17 + ActiveRenderInfo.objectY;
            float var19 = var17 + var14 + ActiveRenderInfo.objectY;
            float var20 = var18 / var19;
            var20 += (float)(p_147500_4_ + (double)var12);
            GL11.glTranslatef(var9, var20, var11);
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_OBJECT_LINEAR);
            GL11.glTexGeni(GL11.GL_Q, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_EYE_LINEAR);
            GL11.glTexGen(GL11.GL_S, GL11.GL_OBJECT_PLANE, this.func_147525_a(1.0F, 0.0F, 0.0F, 0.0F));
            GL11.glTexGen(GL11.GL_T, GL11.GL_OBJECT_PLANE, this.func_147525_a(0.0F, 0.0F, 1.0F, 0.0F));
            GL11.glTexGen(GL11.GL_R, GL11.GL_OBJECT_PLANE, this.func_147525_a(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glTexGen(GL11.GL_Q, GL11.GL_EYE_PLANE, this.func_147525_a(0.0F, 1.0F, 0.0F, 0.0F));
            GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_Q);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
            GL11.glScalef(var15, var15, var15);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            GL11.glRotatef((float)(var13 * var13 * 4321 + var13 * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
            GL11.glTranslatef(-var9, -var11, -var10);
            var18 = var17 + ActiveRenderInfo.objectY;
            GL11.glTranslatef(ActiveRenderInfo.objectX * var14 / var18, ActiveRenderInfo.objectZ * var14 / var18, -var10);
            Tessellator var23 = Tessellator.instance;
            var23.startDrawingQuads();
            var20 = field_147527_e.nextFloat() * 0.5F + 0.1F;
            float var21 = field_147527_e.nextFloat() * 0.5F + 0.4F;
            float var22 = field_147527_e.nextFloat() * 0.5F + 0.5F;

            if (var13 == 0)
            {
                var22 = 1.0F;
                var21 = 1.0F;
                var20 = 1.0F;
            }

            var23.setColorRGBA_F(var20 * var16, var21 * var16, var22 * var16, 1.0F);
            var23.addVertex(p_147500_2_, p_147500_4_ + (double)var12, p_147500_6_);
            var23.addVertex(p_147500_2_, p_147500_4_ + (double)var12, p_147500_6_ + 1.0D);
            var23.addVertex(p_147500_2_ + 1.0D, p_147500_4_ + (double)var12, p_147500_6_ + 1.0D);
            var23.addVertex(p_147500_2_ + 1.0D, p_147500_4_ + (double)var12, p_147500_6_);
            var23.draw();
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_Q);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private FloatBuffer func_147525_a(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_)
    {
        this.field_147528_b.clear();
        this.field_147528_b.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.field_147528_b.flip();
        return this.field_147528_b;
    }

    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_, float p_147500_8_)
    {
        this.renderTileEntityAt((TileEntityEndPortal)p_147500_1_, p_147500_2_, p_147500_4_, p_147500_6_, p_147500_8_);
    }
}
