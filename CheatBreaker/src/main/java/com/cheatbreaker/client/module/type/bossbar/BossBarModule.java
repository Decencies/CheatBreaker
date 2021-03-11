package com.cheatbreaker.client.module.type.bossbar;

import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.event.type.RenderPreviewEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.boss.BossStatus;
import org.lwjgl.opengl.GL11;

public class BossBarModule extends AbstractModule {

    public BossBarModule() {
        super("Native Anus");
        this.setDefaultAnchor(CBGuiAnchor.MIDDLE_TOP);
        this.addEvent(GuiDrawEvent.class, this::renderReal);
        this.addEvent(RenderPreviewEvent.class, this::renderPreview);
        this.setDefaultState(true);
    }

    public void renderPreview(RenderPreviewEvent renderPreviewEvent) {
        GL11.glPushMatrix();
        this.scaleAndTranslate(renderPreviewEvent.getResolution());
        if (BossStatus.bossName == null || BossStatus.statusBarTime <= 0) {
            this.minecraft.getTextureManager().bindTexture(Gui.icons);
            FontRenderer fontRenderer = this.minecraft.fontRenderer;
            int n2 = 182;
            int n3 = 0;
            float f = 1.0f;
            int n4 = (int)(f * (float)(n2 + 1));
            int n5 = 13;
            this.minecraft.scaledTessellator(n3, n5, 0, 74, n2, 5);
            this.minecraft.scaledTessellator(n3, n5, 0, 74, n2, 5);
            if (n4 > 0) {
                this.minecraft.scaledTessellator(n3, n5, 0, 79, n4, 5);
            }
            String bossName = "Wither";
            fontRenderer.drawStringWithShadow(bossName, this.width / 2.0f - (float)(fontRenderer.getStringWidth(bossName) / 2), n5 - 10, 0xFFFFFF);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            //this.minecraft.getTextureManager().bindTexture(Gui.icons);
            this.setDimensions(182, 20);
        }
        GL11.glPopMatrix();
    }

    public void renderReal(GuiDrawEvent guiDrawEvent) {
        GL11.glPushMatrix();
        this.scaleAndTranslate(guiDrawEvent.getResolution());
        if (BossStatus.bossName != null || BossStatus.statusBarTime > 0) {
            this.minecraft.getTextureManager().bindTexture(Gui.icons);
            FontRenderer fontRenderer = this.minecraft.fontRenderer;
            int n2 = 182;
            int n3 = 0;
            float f = 1.0f;
            int n4 = (int)(f * (float)(n2 + 1));
            int n5 = 13;
            this.minecraft.scaledTessellator(n3, n5, 0, 74, n2, 5);
            this.minecraft.scaledTessellator(n3, n5, 0, 74, n2, 5);
            if (n4 > 0) {
                this.minecraft.scaledTessellator(n3, n5, 0, 79, n4, 5);
            }
            String bossName = BossStatus.bossName;
            fontRenderer.drawStringWithShadow(bossName, this.width / 2.0f - (float)(fontRenderer.getStringWidth(bossName) / 2), n5 - 10, 0xFFFFFF);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            //this.minecraft.getTextureManager().bindTexture(Gui.icons);
            this.setDimensions(182, 20);
        }
        GL11.glPopMatrix();
    }


}
