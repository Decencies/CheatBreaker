package com.cheatbreaker.client.module.type.togglesprint;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.opengl.GL11;

public class ToggleSprintModule extends AbstractModule {

    public static Setting toggleSprint;
    public static Setting toggleSneak;
    public static Setting showHudText;
    private Setting textColor;
    public static Setting doubleTap;
    public static Setting flyBoost;
    public static Setting showWhileTyping;
    private Setting flyBoostLabel;
    public static Setting flyBoostAmount;
    public Setting renderHud;
    public Setting settingsList;
    public Setting defaultSettingsValues;
    public Setting eventMap;
    public Setting previewType;
    public Setting previewIconLocation;
    public Setting previewIconWidth;
    public Setting previewIconHeight;
    public Setting previewLabelSize;
    public Setting previewLabel;
    public static boolean lIIIIIllllIIIIlIlIIIIlIlI;

    public ToggleSprintModule() {
        super("ToggleSprint");
        this.setDefaultAnchor(CBGuiAnchor.LEFT_TOP);
        this.setDefaultTranslations(0.0f, 10);
        this.setDefaultState(false);
        this.isEditable = false;
        toggleSprint = new Setting(this, "Toggle Sprint").setValue(true);
        toggleSneak = new Setting(this, "Toggle Sneak").setValue(false);
        showHudText = new Setting(this, "Show HUD Text").setValue(true);
        this.textColor = new Setting(this, "Text Color").setValue(-1).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        doubleTap = new Setting(this, "Double Tap").setValue(false);
        showWhileTyping = new Setting(this, "Show While Typing").setValue(true);
        this.flyBoostLabel = new Setting(this, "label").setValue("Fly Boost");
        flyBoost = new Setting(this, "Fly Boost").setValue(true);
        flyBoostAmount = new Setting(this, "Fly Boost Amount").setValue(4).setMinMax(2, 8).setParent(flyBoost);
        this.renderHud = new Setting(this, "Fly Boost String").setValue("[Flying (%BOOST%x boost)]");
        this.settingsList = new Setting(this, "Fly String").setValue("[Flying]");
        this.defaultSettingsValues = new Setting(this, "Riding String").setValue("[Riding]");
        this.eventMap = new Setting(this, "Descend String").setValue("[Descending]");
        this.previewType = new Setting(this, "Dismount String").setValue("[Dismounting]");
        this.previewIconLocation = new Setting(this, "Sneaking String").setValue("[Sneaking (Key Held)]");
        this.previewIconWidth = new Setting(this, "Sprinting Held String").setValue("[Sprinting (Key Held)]");
        this.previewIconHeight = new Setting(this, "Sprinting Vanilla String").setValue("[Sprinting (Vanilla)]");
        this.previewLabelSize = new Setting(this, "Sprinting Toggle String").setValue("[Sprinting (Toggled)]");
        this.previewLabel = new Setting(this, "Sneaking Toggle String").setValue("[Sneaking (Toggled)]");
        this.setPreviewLabel("[Sprinting (Toggled)]", 1.0f);
        this.addEvent(GuiDrawEvent.class, this::renderReal);
    }

    private void renderReal(GuiDrawEvent guiDrawEvent) {
        if (!this.isRenderHud()) {
            return;
        }
        if ((Boolean) showHudText.getValue() && ((Boolean) showWhileTyping.getValue() || !(this.minecraft.currentScreen instanceof GuiChat))) {
            GL11.glPushMatrix();
            int n = this.minecraft.fontRenderer.getStringWidth(ToggleSprintHandler.IlIlIIIlllIIIlIlllIlIllIl);
            this.setDimensions(n, 18);
            this.scaleAndTranslate(guiDrawEvent.getResolution());
            this.minecraft.fontRenderer.drawStringWithShadow(ToggleSprintHandler.IlIlIIIlllIIIlIlllIlIllIl, 0.0f, 0.0f, this.textColor.getColorValue());
            GL11.glPopMatrix();
        } else {
            this.setDimensions(50, 18);
        }
    }

    static {
        lIIIIIllllIIIIlIlIIIIlIlI = false;
    }
    
}
