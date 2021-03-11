package net.minecraft.client.gui;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Gui
{
    public static final ResourceLocation optionsBackground = new ResourceLocation("textures/gui/options_background.png");
    public static final ResourceLocation statIcons = new ResourceLocation("textures/gui/container/stats_icons.png");
    public static final ResourceLocation icons = new ResourceLocation("textures/gui/icons.png");
    protected static float zLevel;


    protected void drawHorizontalLine(int p_73730_1_, int p_73730_2_, int p_73730_3_, int p_73730_4_)
    {
        if (p_73730_2_ < p_73730_1_)
        {
            int var5 = p_73730_1_;
            p_73730_1_ = p_73730_2_;
            p_73730_2_ = var5;
        }

        drawRect(p_73730_1_, p_73730_3_, p_73730_2_ + 1, p_73730_3_ + 1, p_73730_4_);
    }

    protected void drawVerticalLine(int p_73728_1_, int p_73728_2_, int p_73728_3_, int p_73728_4_)
    {
        if (p_73728_3_ < p_73728_2_)
        {
            int var5 = p_73728_2_;
            p_73728_2_ = p_73728_3_;
            p_73728_3_ = var5;
        }

        drawRect(p_73728_1_, p_73728_2_ + 1, p_73728_1_ + 1, p_73728_3_, p_73728_4_);
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
     */
    public static void drawRect(float p_73734_0_, float p_73734_1_, float p_73734_2_, float p_73734_3_, int p_73734_4_)
    {
        float var5;

        if (p_73734_0_ < p_73734_2_)
        {
            var5 = p_73734_0_;
            p_73734_0_ = p_73734_2_;
            p_73734_2_ = var5;
        }

        if (p_73734_1_ < p_73734_3_)
        {
            var5 = p_73734_1_;
            p_73734_1_ = p_73734_3_;
            p_73734_3_ = var5;
        }

        float var10 = (float)(p_73734_4_ >> 24 & 255) / 255.0F;
        float var6 = (float)(p_73734_4_ >> 16 & 255) / 255.0F;
        float var7 = (float)(p_73734_4_ >> 8 & 255) / 255.0F;
        float var8 = (float)(p_73734_4_ & 255) / 255.0F;
        Tessellator var9 = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(var6, var7, var8, var10);
        var9.startDrawingQuads();
        var9.addVertex((double)p_73734_0_, (double)p_73734_3_, 0.0D);
        var9.addVertex((double)p_73734_2_, (double)p_73734_3_, 0.0D);
        var9.addVertex((double)p_73734_2_, (double)p_73734_1_, 0.0D);
        var9.addVertex((double)p_73734_0_, (double)p_73734_1_, 0.0D);
        var9.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(float f, float f2, float f3, float f4, float f5, int n, int n2) {
        Gui.drawRect(f + f5, f2 + f5, f3 - f5, f4 - f5, n2);
        Gui.drawRect(f, f2 + f5, f + f5, f4 - f5, n);
        Gui.drawRect(f3 - f5, f2 + f5, f3, f4 - f5, n);
        Gui.drawRect(f, f2, f3, f2 + f5, n);
        Gui.drawRect(f, f4 - f5, f3, f4, n);
    }

    public static void lIIIIIIIIIlIllIIllIlIIlIl(float f, float f2, float f3, float f4, float f5, int n, int n2) {
        Gui.drawRect(f, f2, f3, f4, n2);
        Gui.drawRect(f - f5, f2 - f5, f, f4 + f5, n);
        Gui.drawRect(f3, f2 - f5, f3 + f5, f4 + f5, n);
        Gui.drawRect(f, f2 - f5, f3, f2, n);
        Gui.drawRect(f, f4, f3, f4 + f5, n);
    }

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    public static void drawGradientRect(float p_73733_1_, float p_73733_2_, float p_73733_3_, float p_73733_4_, int p_73733_5_, int p_73733_6_)
    {
        float var7 = (float)(p_73733_5_ >> 24 & 255) / 255.0F;
        float var8 = (float)(p_73733_5_ >> 16 & 255) / 255.0F;
        float var9 = (float)(p_73733_5_ >> 8 & 255) / 255.0F;
        float var10 = (float)(p_73733_5_ & 255) / 255.0F;
        float var11 = (float)(p_73733_6_ >> 24 & 255) / 255.0F;
        float var12 = (float)(p_73733_6_ >> 16 & 255) / 255.0F;
        float var13 = (float)(p_73733_6_ >> 8 & 255) / 255.0F;
        float var14 = (float)(p_73733_6_ & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex((double)p_73733_3_, (double)p_73733_2_, (double)zLevel);
        var15.addVertex((double)p_73733_1_, (double)p_73733_2_, (double)zLevel);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex((double)p_73733_1_, (double)p_73733_4_, (double)zLevel);
        var15.addVertex((double)p_73733_3_, (double)p_73733_4_, (double)zLevel);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Renders the specified text to the screen, center-aligned.
     */
    public void drawCenteredString(FontRenderer p_73732_1_, String p_73732_2_, int p_73732_3_, int p_73732_4_, int p_73732_5_)
    {
        p_73732_1_.drawStringWithShadow(p_73732_2_, p_73732_3_ - p_73732_1_.getStringWidth(p_73732_2_) / 2, p_73732_4_, p_73732_5_);
    }

    /**
     * Renders the specified text to the screen.
     */
    public void drawString(FontRenderer p_73731_1_, String p_73731_2_, int p_73731_3_, int p_73731_4_, int p_73731_5_)
    {
        p_73731_1_.drawStringWithShadow(p_73731_2_, p_73731_3_, p_73731_4_, p_73731_5_);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawTexturedModalRect(float p_73729_1_, float p_73729_2_, float p_73729_3_, float p_73729_4_, int p_73729_5_, int p_73729_6_)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + p_73729_6_), (double)this.zLevel, (double)((float)(p_73729_3_ + 0) * var7), (double)((float)(p_73729_4_ + p_73729_6_) * var8));
        var9.addVertexWithUV((double)(p_73729_1_ + p_73729_5_), (double)(p_73729_2_ + p_73729_6_), (double)this.zLevel, (double)((float)(p_73729_3_ + p_73729_5_) * var7), (double)((float)(p_73729_4_ + p_73729_6_) * var8));
        var9.addVertexWithUV((double)(p_73729_1_ + p_73729_5_), (double)(p_73729_2_ + 0), (double)this.zLevel, (double)((float)(p_73729_3_ + p_73729_5_) * var7), (double)((float)(p_73729_4_ + 0) * var8));
        var9.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + 0), (double)this.zLevel, (double)((float)(p_73729_3_ + 0) * var7), (double)((float)(p_73729_4_ + 0) * var8));
        var9.draw();
    }

    public void drawTexturedModelRectFromIcon(int p_94065_1_, int p_94065_2_, IIcon p_94065_3_, int p_94065_4_, int p_94065_5_)
    {
        Tessellator var6 = Tessellator.instance;
        var6.startDrawingQuads();
        var6.addVertexWithUV((double)(p_94065_1_ + 0), (double)(p_94065_2_ + p_94065_5_), (double)this.zLevel, (double)p_94065_3_.getMinU(), (double)p_94065_3_.getMaxV());
        var6.addVertexWithUV((double)(p_94065_1_ + p_94065_4_), (double)(p_94065_2_ + p_94065_5_), (double)this.zLevel, (double)p_94065_3_.getMaxU(), (double)p_94065_3_.getMaxV());
        var6.addVertexWithUV((double)(p_94065_1_ + p_94065_4_), (double)(p_94065_2_ + 0), (double)this.zLevel, (double)p_94065_3_.getMaxU(), (double)p_94065_3_.getMinV());
        var6.addVertexWithUV((double)(p_94065_1_ + 0), (double)(p_94065_2_ + 0), (double)this.zLevel, (double)p_94065_3_.getMinU(), (double)p_94065_3_.getMinV());
        var6.draw();
    }

    public static void func_146110_a(int p_146110_0_, int p_146110_1_, float p_146110_2_, float p_146110_3_, int p_146110_4_, int p_146110_5_, float p_146110_6_, float p_146110_7_)
    {
        float var8 = 1.0F / p_146110_6_;
        float var9 = 1.0F / p_146110_7_;
        Tessellator var10 = Tessellator.instance;
        var10.startDrawingQuads();
        var10.addVertexWithUV((double)p_146110_0_, (double)(p_146110_1_ + p_146110_5_), 0.0D, (double)(p_146110_2_ * var8), (double)((p_146110_3_ + (float)p_146110_5_) * var9));
        var10.addVertexWithUV((double)(p_146110_0_ + p_146110_4_), (double)(p_146110_1_ + p_146110_5_), 0.0D, (double)((p_146110_2_ + (float)p_146110_4_) * var8), (double)((p_146110_3_ + (float)p_146110_5_) * var9));
        var10.addVertexWithUV((double)(p_146110_0_ + p_146110_4_), (double)p_146110_1_, 0.0D, (double)((p_146110_2_ + (float)p_146110_4_) * var8), (double)(p_146110_3_ * var9));
        var10.addVertexWithUV((double)p_146110_0_, (double)p_146110_1_, 0.0D, (double)(p_146110_2_ * var8), (double)(p_146110_3_ * var9));
        var10.draw();
    }

    public static void func_152125_a(int p_152125_0_, int p_152125_1_, float p_152125_2_, float p_152125_3_, int p_152125_4_, int p_152125_5_, int p_152125_6_, int p_152125_7_, float p_152125_8_, float p_152125_9_)
    {
        float var10 = 1.0F / p_152125_8_;
        float var11 = 1.0F / p_152125_9_;
        Tessellator var12 = Tessellator.instance;
        var12.startDrawingQuads();
        var12.addVertexWithUV((double)p_152125_0_, (double)(p_152125_1_ + p_152125_7_), 0.0D, (double)(p_152125_2_ * var10), (double)((p_152125_3_ + (float)p_152125_5_) * var11));
        var12.addVertexWithUV((double)(p_152125_0_ + p_152125_6_), (double)(p_152125_1_ + p_152125_7_), 0.0D, (double)((p_152125_2_ + (float)p_152125_4_) * var10), (double)((p_152125_3_ + (float)p_152125_5_) * var11));
        var12.addVertexWithUV((double)(p_152125_0_ + p_152125_6_), (double)p_152125_1_, 0.0D, (double)((p_152125_2_ + (float)p_152125_4_) * var10), (double)(p_152125_3_ * var11));
        var12.addVertexWithUV((double)p_152125_0_, (double)p_152125_1_, 0.0D, (double)(p_152125_2_ * var10), (double)(p_152125_3_ * var11));
        var12.draw();
    }
}
