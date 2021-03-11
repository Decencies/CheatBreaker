package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.type.ClickEvent;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.event.type.TickEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class CPSModule extends AbstractModule {

    private final List<Long> clicks;
    private final Setting showBackground;
    private final Setting textColor;
    private final Setting backgroundColor;

    public CPSModule() {
        super("CPS");
        this.setDefaultAnchor(CBGuiAnchor.RIGHT_TOP);
        this.setDefaultTranslations(0.0f, 0.0f);
        this.setState(false);
        this.clicks = new ArrayList<>();
        this.showBackground = new Setting(this, "Show Background").setValue(true);
        this.textColor = new Setting(this, "Text Color").setValue(-1).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.backgroundColor = new Setting(this, "Background Color").setValue(0x6F000000).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.setPreviewLabel("[9 CPS]", 1.1030303f * 1.2692307f);
        this.addEvent(GuiDrawEvent.class, this::onDraw);
        this.addEvent(TickEvent.class, this::onTick);
        this.addEvent(ClickEvent.class, this::onClick);
    }

    private void onDraw(GuiDrawEvent drawEvent) {
        if (!this.isRenderHud()) {
            return;
        }
        GL11.glPushMatrix();
        this.scaleAndTranslate(drawEvent.getResolution());
        if ((Boolean) this.showBackground.getValue()) {
            this.setDimensions(56, 18);
            Gui.drawRect(0.0f, 0.0f, 56, 13, this.backgroundColor.getColorValue());
            String string = this.clicks.size() + " CPS";
            this.minecraft.fontRenderer.drawString(string, (int)(this.width / 2.0f - (float)(this.minecraft.fontRenderer.getStringWidth(string) / 2)), 3, this.textColor.getColorValue());
        } else {
            String string = "[" + this.clicks.size() + " CPS]";
            this.minecraft.fontRenderer.drawString(string, (int)(this.width / 2.0f - (float)(this.minecraft.fontRenderer.getStringWidth(string) / 2)), 3, this.textColor.getColorValue(), true);
            this.setDimensions(this.minecraft.fontRenderer.getStringWidth(string), 18);
        }
        GL11.glPopMatrix();
    }

    private void onTick(TickEvent cBTickEvent) {
        this.clicks.removeIf(l -> l < System.currentTimeMillis() - 1000L);
    }

    private void onClick(ClickEvent cBClickEvent) {
        if (cBClickEvent.getMouseButton() == 0) {
            this.clicks.add(System.currentTimeMillis());
        }
    }

}
