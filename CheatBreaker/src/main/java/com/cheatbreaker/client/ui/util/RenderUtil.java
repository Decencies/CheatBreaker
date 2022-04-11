package com.cheatbreaker.client.ui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderUtil {

    protected static float lIIIIlIIllIIlIIlIIIlIIllI = 0.0f;
    private static final double lIIIIIIIIIlIllIIllIlIIlIl = 30.0;

    public static void lIIIIlIIllIIlIIlIIIlIIllI(float f, float f2, float f3, float f4, int n, int n2) {
        float f5 = 3.875f * 0.0010080645f;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(f, f2 + (float)n2, lIIIIlIIllIIlIIlIIIlIIllI, f3 * f5, (f4 + (float)n2) * f5);
        tessellator.addVertexWithUV(f + (float)n, f2 + (float)n2, lIIIIlIIllIIlIIlIIIlIIllI, (f3 + (float)n) * f5, (f4 + (float)n2) * f5);
        tessellator.addVertexWithUV(f + (float)n, f2, lIIIIlIIllIIlIIlIIIlIIllI, (f3 + (float)n) * f5, f4 * f5);
        tessellator.addVertexWithUV(f, f2, lIIIIlIIllIIlIIlIIIlIIllI, f3 * f5, f4 * f5);
        tessellator.draw();
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(int n, int n2, int n3, int n4, ScaledResolution scaledResolution) {
        int n5 = scaledResolution.getScaleFactor();
        int n6 = n4 - n2;
        int n7 = n3 - n;
        int n8 = scaledResolution.getScaledHeight() - n4;
        GL11.glScissor(n * n5, n8 * n5, n7 * n5, n6 * n5);
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(ResourceLocation resourceLocation, float f, float f2, float f3) {
        float f4 = f * 2.0f;
        float f5 = f * 2.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        GL11.glEnable(3042);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GL11.glBegin(7);
        GL11.glTexCoord2d(f6 / f, f7 / f);
        GL11.glVertex2d(f2, f3);
        GL11.glTexCoord2d(f6 / f, (f7 + f) / f);
        GL11.glVertex2d(f2, f3 + f5);
        GL11.glTexCoord2d((f6 + f) / f, (f7 + f) / f);
        GL11.glVertex2d(f2 + f4, f3 + f5);
        GL11.glTexCoord2d((f6 + f) / f, f7 / f);
        GL11.glVertex2d(f2 + f4, f3);
        GL11.glEnd();
        GL11.glDisable(3042);
    }

    public static void lIIIIIIIIIlIllIIllIlIIlIl(ResourceLocation resourceLocation, float f, float f2, float f3) {
        float f4 = f * 2.0f;
        float f5 = f * 2.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        GL11.glEnable(3042);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GL11.glBegin(7);
        GL11.glTexCoord2d(f6 / f, f7 / f);
        GL11.glVertex2d(f2, f3);
        GL11.glTexCoord2d(f6 / f, (f7 + f) / f);
        GL11.glVertex2d(f2, f3 + f5);
        GL11.glTexCoord2d((f6 + f) / f, (f7 + f) / f);
        GL11.glVertex2d(f2 + f4, f3 + f5);
        GL11.glTexCoord2d((f6 + f) / f, f7 / f);
        GL11.glVertex2d(f2 + f4, f3);
        GL11.glEnd();
        GL11.glDisable(3042);
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(ResourceLocation resourceLocation, float f, float f2, float f3, float f4) {
        float f5 = f3 / 2.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        GL11.glEnable(3042);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GL11.glBegin(7);
        GL11.glTexCoord2d(f6 / f5, f7 / f5);
        GL11.glVertex2d(f, f2);
        GL11.glTexCoord2d(f6 / f5, (f7 + f5) / f5);
        GL11.glVertex2d(f, f2 + f4);
        GL11.glTexCoord2d((f6 + f5) / f5, (f7 + f5) / f5);
        GL11.glVertex2d(f + f3, f2 + f4);
        GL11.glTexCoord2d((f6 + f5) / f5, f7 / f5);
        GL11.glVertex2d(f + f3, f2);
        GL11.glEnd();
        GL11.glDisable(3042);
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(int x, int y, int width, int height, float f, int n5) {
        int n6 = height - y;
        int n7 = width - x;
        int n8 = n5 - height;
        GL11.glScissor((int)((float)x * f), (int)((float)n8 * f), (int)((float)n7 * f), (int)((float)n6 * f));
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(double d, double d2, double d3, double d4, double d5, int n) {
        int n2;
        float f = (float)(n >> 24 & 0xFF) / (float)255;
        float f2 = (float)(n >> 16 & 0xFF) / (float)255;
        float f3 = (float)(n >> 8 & 0xFF) / (float)255;
        float f4 = (float)(n & 0xFF) / (float)255;
        GL11.glPushAttrib(0);
        GL11.glScaled(1.476190447807312 * 0.33870968393182915, 0.46794872796120124 * 1.068493127822876, 1.0533332824707031 * 0.47468356722498867);
        d *= (double)2;
        d2 *= (double)2;
        d3 *= (double)2;
        d4 *= (double)2;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        for (n2 = 0; n2 <= 90; n2 += 3) {
            GL11.glVertex2d(d + d5 + Math.sin((double)n2 * (6.5973445528769465 * 0.4761904776096344) / (double)180) * (d5 * (double)-1), d2 + d5 + Math.cos((double)n2 * (42.5 * 0.07391982714328925) / (double)180) * (d5 * (double)-1));
        }
        for (n2 = 90; n2 <= 180; n2 += 3) {
            GL11.glVertex2d(d + d5 + Math.sin((double)n2 * (0.5711986642890533 * 5.5) / (double)180) * (d5 * (double)-1), d4 - d5 + Math.cos((double)n2 * (0.21052631735801697 * 14.922564993369743) / (double)180) * (d5 * (double)-1));
        }
        for (n2 = 0; n2 <= 90; n2 += 3) {
            GL11.glVertex2d(d3 - d5 + Math.sin((double)n2 * (4.466951941998311 * 0.7032967209815979) / (double)180) * d5, d4 - d5 + Math.cos((double)n2 * (28.33333396911621 * 0.11087973822685955) / (double)180) * d5);
        }
        for (n2 = 90; n2 <= 180; n2 += 3) {
            GL11.glVertex2d(d3 - d5 + Math.sin((double)n2 * ((double)0.6f * 5.2359875479235365) / (double)180) * d5, d2 + d5 + Math.cos((double)n2 * (2.8529412746429443 * 1.1011767685204017) / (double)180) * d5);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glScaled(2, 2, 2);
        GL11.glPopAttrib();
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(double d, double d2, double d3) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(6);
        tessellator.addVertex(d, d2, lIIIIlIIllIIlIIlIIIlIIllI);
        double d4 = 3.0 * 2.0943951023931953;
        double d5 = d4 / (double)30;
        for (double d6 = -d5; d6 < d4; d6 += d5) {
            tessellator.addVertex(d + d3 * Math.cos(-d6), d2 + d3 * Math.sin(-d6), lIIIIlIIllIIlIIlIIIlIIllI);
        }
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(double d, double d2, double d3, double d4, double d5, int n, double d6) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        d5 = (d5 + (double)n) % (double)n;
        Tessellator tessellator = Tessellator.instance;
        for (double d7 = (double)360 / (double)n * d5; d7 < (double)360 / (double)n * (d5 + d6); d7 += 1.0) {
            double d8 = d7 * (0.6976743936538696 * 4.502949631183398) / (double)180;
            double d9 = (d7 - 1.0) * (1.9384295391612096 * 1.6206896305084229) / (double)180;
            double[] arrd = new double[]{Math.cos(d8) * d3, -Math.sin(d8) * d3, Math.cos(d9) * d3, -Math.sin(d9) * d3};
            double[] arrd2 = new double[]{Math.cos(d8) * d4, -Math.sin(d8) * d4, Math.cos(d9) * d4, -Math.sin(d9) * d4};
            tessellator.startDrawing(7);
            tessellator.addVertex(d + arrd2[0], d2 + arrd2[1], 0.0);
            tessellator.addVertex(d + arrd2[2], d2 + arrd2[3], 0.0);
            tessellator.addVertex(d + arrd[2], d2 + arrd[3], 0.0);
            tessellator.addVertex(d + arrd[0], d2 + arrd[1], 0.0);
            tessellator.draw();
        }
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(float f, float f2, float f3, int n) {
        if (f2 < f) {
            float f4 = f;
            f = f2;
            f2 = f4;
        }
        Gui.drawRect(f, f3, f2 + 1.0f, f3 + 1.0f, n);
    }

    public static void lIIIIIIIIIlIllIIllIlIIlIl(float f, float f2, float f3, int n) {
        if (f3 < f2) {
            float f4 = f2;
            f2 = f3;
            f3 = f4;
        }
        Gui.drawRect(f, f2 + 1.0f, f + 1.0f, f3, n);
    }

    public static void lIIIIIIIIIlIllIIllIlIIlIl(float f, float f2, float f3, float f4, int n, int n2) {
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        Gui.drawRect((f *= 2.0f) + 1.0f, (f2 *= 2.0f) + 1.0f, (f3 *= 2.0f) - 1.0f, (f4 *= 2.0f) - 1.0f, n2);
        RenderUtil.lIIIIIIIIIlIllIIllIlIIlIl(f, f2 + 1.0f, f4 - 2.0f, n);
        RenderUtil.lIIIIIIIIIlIllIIllIlIIlIl(f3 - 1.0f, f2 + 1.0f, f4 - 2.0f, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f + 2.0f, f3 - (float)3, f2, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f + 2.0f, f3 - (float)3, f4 - 1.0f, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f + 1.0f, f + 1.0f, f2 + 1.0f, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f3 - 2.0f, f3 - 2.0f, f2 + 1.0f, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f3 - 2.0f, f3 - 2.0f, f4 - 2.0f, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f + 1.0f, f + 1.0f, f4 - 2.0f, n);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(float f, float f2, float f3, float f4, int n, int n2, int n3) {
        GL11.glScalef(0.234375f * 2.1333334f, 0.65f * 0.7692308f, 0.5692308f * 0.87837833f);
        Gui.drawGradientRect((f *= 2.0f) + 1.0f, (f2 *= 2.0f) + 1.0f, (f3 *= 2.0f) - 1.0f, (f4 *= 2.0f) - 1.0f, n2, n3);
        RenderUtil.lIIIIIIIIIlIllIIllIlIIlIl(f, f2 + 1.0f, f4 - 2.0f, n);
        RenderUtil.lIIIIIIIIIlIllIIllIlIIlIl(f3 - 1.0f, f2 + 1.0f, f4 - 2.0f, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f + 2.0f, f3 - (float)3, f2, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f + 2.0f, f3 - (float)3, f4 - 1.0f, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f + 1.0f, f + 1.0f, f2 + 1.0f, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f3 - 2.0f, f3 - 2.0f, f2 + 1.0f, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f3 - 2.0f, f3 - 2.0f, f4 - 2.0f, n);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(f + 1.0f, f + 1.0f, f4 - 2.0f, n);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

}
