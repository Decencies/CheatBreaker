package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class FPSModule extends AbstractModule {

    private final Setting showBackground;
    private final Setting textColor;
    private final Setting backgroundColor;

    public FPSModule() {
        super("FPS");
        this.setDefaultAnchor(CBGuiAnchor.MIDDLE_TOP);
        this.setDefaultTranslations(0.0f, 0.0f);
        this.setDefaultState(false);
        this.showBackground = new Setting(this, "Show Background").setValue(true);
        this.textColor = new Setting(this, "Text Color").setValue(-1).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.backgroundColor = new Setting(this, "Background Color").setValue(0x6F000000).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.setPreviewLabel("[144 FPS]", 1.6978723f * 0.8245614f);
        this.addEvent(GuiDrawEvent.class, this::onRender);
    }
    private void onRender(GuiDrawEvent drawEvent) {
        if (!this.isRenderHud()) {
            return;
        }
        GL11.glPushMatrix();
        this.scaleAndTranslate(drawEvent.getResolution());
        if ((Boolean) this.showBackground.getValue()) {
            this.setDimensions(56, 18);
            Gui.drawRect(0.0f, 0.0f, 56, 13, this.backgroundColor.getColorValue());
            String string = Minecraft.debugFPS + " FPS";
            this.minecraft.fontRenderer.drawString(string, (int)(this.width / 2.0f - (float)(this.minecraft.fontRenderer.getStringWidth(string) / 2)), 3, this.textColor.getColorValue());
        } else {
            String string = "[" + Minecraft.debugFPS + " FPS]";
            this.minecraft.fontRenderer.drawString(string, (int)(this.width / 2.0f - (float)(this.minecraft.fontRenderer.getStringWidth(string) / 2)), 3, this.textColor.getColorValue(), true);
            this.setDimensions(this.minecraft.fontRenderer.getStringWidth(string), 18);
        }
        GL11.glPopMatrix();
    }
}
