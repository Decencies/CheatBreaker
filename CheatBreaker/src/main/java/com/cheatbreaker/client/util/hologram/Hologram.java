package com.cheatbreaker.client.util.hologram;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hologram {
    private final UUID lIIIIlIIllIIlIIlIIIlIIllI;
    private String[] lIIIIIIIIIlIllIIllIlIIlIl;
    private final double IlllIIIlIlllIllIlIIlllIlI;
    private final double IIIIllIlIIIllIlllIlllllIl;
    private final double IIIIllIIllIIIIllIllIIIlIl;
    private static final List<Hologram> holograms = new ArrayList<>();

    public Hologram(UUID uUID, double d, double d2, double d3) {
        this.lIIIIlIIllIIlIIlIIIlIIllI = uUID;
        this.IlllIIIlIlllIllIlIIlllIlI = d;
        this.IIIIllIlIIIllIlllIlllllIl = d2;
        this.IIIIllIIllIIIIllIllIIIlIl = d3;
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI() {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        RenderManager renderManager = RenderManager.instance;
        for (Hologram hologram : holograms) {
            if (hologram.IlllIIIlIlllIllIlIIlllIlI() == null || hologram.IlllIIIlIlllIllIlIIlllIlI().length <= 0) continue;
            for (int i = hologram.IlllIIIlIlllIllIlIIlllIlI().length - 1; i >= 0; --i) {
                String string = hologram.IlllIIIlIlllIllIlIIlllIlI()[hologram.IlllIIIlIlllIllIlIIlllIlI().length - i - 1];
                float f = (float)(hologram.IIIIllIlIIIllIlllIlllllIl() - (double)((float)RenderManager.renderPosX));
                float f2 = (float)(hologram.IIIIllIIllIIIIllIllIIIlIl() + 1.0 + (double)((float)i * (0.16049382f * 1.5576924f)) - (double)((float)RenderManager.renderPosY));
                float f3 = (float)(hologram.IlIlIIIlllIIIlIlllIlIllIl() - (double)((float)RenderManager.renderPosZ));
                float f4 = 1.7391304f * 0.92f;
                float f5 = 1.4081633f * 0.011835749f * f4;
                GL11.glPushMatrix();
                GL11.glTranslatef(f, f2, f3);
                GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                GL11.glRotatef(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                GL11.glRotatef(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                GL11.glScalef(-f5, -f5, f5);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                Tessellator tessellator = Tessellator.instance;
                int n = 0;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tessellator.startDrawingQuads();
                int n2 = fontRenderer.getStringWidth(string) / 2;
                tessellator.setColorRGBA_F(0.0f, 0.0f, 0.0f, 0.6875f * 0.36363637f);
                tessellator.addVertex(-n2 - 1, -1 + n, 0.0);
                tessellator.addVertex(-n2 - 1, 8 + n, 0.0);
                tessellator.addVertex(n2 + 1, 8 + n, 0.0);
                tessellator.addVertex(n2 + 1, -1 + n, 0.0);
                tessellator.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                // lIIIIIIIIIlIllIIllIlIIlIl = drawString
                fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, n, 0x20FFFFFF);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, n, -1);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
    }

    public UUID lIIIIIIIIIlIllIIllIlIIlIl() {
        return this.lIIIIlIIllIIlIIlIIIlIIllI;
    }

    public String[] IlllIIIlIlllIllIlIIlllIlI() {
        return this.lIIIIIIIIIlIllIIllIlIIlIl;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(String[] arrstring) {
        this.lIIIIIIIIIlIllIIllIlIIlIl = arrstring;
    }

    public double IIIIllIlIIIllIlllIlllllIl() {
        return this.IlllIIIlIlllIllIlIIlllIlI;
    }

    public double IIIIllIIllIIIIllIllIIIlIl() {
        return this.IIIIllIlIIIllIlllIlllllIl;
    }

    public double IlIlIIIlllIIIlIlllIlIllIl() {
        return this.IIIIllIIllIIIIllIllIIIlIl;
    }

    public static List<Hologram> IIIllIllIlIlllllllIlIlIII() {
        return holograms;
    }
}