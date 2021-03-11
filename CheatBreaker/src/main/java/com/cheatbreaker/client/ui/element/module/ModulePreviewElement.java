package com.cheatbreaker.client.ui.element.module;

import java.util.Objects;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.element.AbstractScrollableElement;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.module.CBModulePlaceGui;
import com.cheatbreaker.client.module.type.cooldowns.CooldownRenderer;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.ui.util.font.CBFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ModulePreviewElement extends AbstractModulesGuiElement {
    private final AbstractModule module;
    private final ModulesGuiButtonElement optionsButton;
    private final ModulesGuiButtonElement toggleOrHideFromHud;
    private final ModulesGuiButtonElement toggle;
    private final AbstractScrollableElement IlIlllIIIIllIllllIllIIlIl;

    public ModulePreviewElement(AbstractScrollableElement parent, AbstractModule module, float f) {
        super(f);
        this.module = module;
        this.IlIlllIIIIllIllllIllIIlIl = parent;
        CBFontRenderer optionsFontRenderer = CheatBreaker.getInstance().playBold18px;
        CBFontRenderer hideOrToggleFontRenderer = CheatBreaker.getInstance().playRegular14px;
        this.optionsButton = new ModulesGuiButtonElement(optionsFontRenderer, null, "Options", this.x + 4, this.y + this.height - 20, this.x + this.width - 4, this.y + this.height - 6, -12418828, f);
        this.toggleOrHideFromHud = new ModulesGuiButtonElement(hideOrToggleFontRenderer, null, module.getGuiAnchor() == null ? (module.isRenderHud() ? "Disable" : "Enable") : (module.isRenderHud() ? "Hide from HUD" : "Add to HUD"), this.x + 4, this.y + this.height - 38, this.x + this.width / 2 - 2, this.y + this.height - 24, module.isRenderHud() ? -5756117 : -13916106, f);
        this.toggleOrHideFromHud.lIIIIlIIllIIlIIlIIIlIIllI(module != CheatBreaker.getInstance().moduleManager.minmap && module != CheatBreaker.getInstance().moduleManager.notifications);
        this.toggle = new ModulesGuiButtonElement(hideOrToggleFontRenderer, null, module.isEnabled() ? "Disable" : "Enable", this.x + this.width / 2 + 2, this.y + this.height - 38, this.x + this.width - 4, this.y + this.height - 24, module.isEnabled() ? -5756117 : -13916106, f);
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        float f2;
        Object object;
        if (this.module.isEnabled()) {
            Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -13916106);
        } else {
            Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -1347374928);
        }
        CBFontRenderer playBold18px = CheatBreaker.getInstance().playBold18px;
        GL11.glPushMatrix();
        int n3 = 0;
        int n4 = 0;
        if (this.module == CheatBreaker.getInstance().moduleManager.armourStatus) {
            n3 = -10;
            object = "329/329";
            f2 = Minecraft.getMinecraft().fontRenderer.getStringWidth((String)object);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((String)object, (int)((float)(this.x + 1 + this.width / 2) - f2 / 2.0f), this.y + this.height / 2 - 18, -1);
        } else if (this.module == CheatBreaker.getInstance().moduleManager.potionStatus) {
            n4 = -30;
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Speed II", this.x + 8 + this.width / 2 - 20, this.y + this.height / 2 - 36, -1);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("0:42", this.x + 8 + this.width / 2 - 20, this.y + this.height / 2 - 26, -1);
        } else if (this.module == CheatBreaker.getInstance().moduleManager.scoreboard) {
            Gui.drawRect(this.x + 20, this.y + this.height / 2f - 44, this.x + this.width - 20, this.y + this.height / 2 - 6, 0x6F000000);
            Minecraft.getMinecraft().fontRenderer.drawString("Score", this.x + this.width / 2, this.y + this.height / 2 - 40, -1);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Steve", this.x + 24, this.y + this.height / 2 - 28, -1);
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Alex", this.x + 24, this.y + this.height / 2 - 18, -1);
            Minecraft.getMinecraft().fontRenderer.drawString(EnumChatFormatting.RED + "0", this.x + this.width - 26, this.y + this.height / 2 - 18, -1);
            Minecraft.getMinecraft().fontRenderer.drawString(EnumChatFormatting.RED + "1", this.x + this.width - 26, this.y + this.height / 2 - 28, -1);
        }
        if (this.module == CheatBreaker.getInstance().moduleManager.cooldowns) {
            object = new CooldownRenderer("EnderPearl", 368, 9000L);
            ((CooldownRenderer)object).lIIIIlIIllIIlIIlIIIlIIllI(CheatBreaker.getInstance().moduleManager.cooldowns.colorTheme, this.x + this.width / 2 - 18, this.y + this.height / 2 - 26 - 18, -1);
        } else if ((this.module.getPreviewType() == null || this.module.getPreviewType() == AbstractModule.PreviewType.LABEL) && this.module != CheatBreaker.getInstance().moduleManager.scoreboard) {
            object = "";
            if (this.module.getPreviewType() == null) {
                f2 = 2.0f;
                for (String string : this.module.getName().split(" ")) {
                    String string2 = string.substring(0, 1);
                    object = (String)object + (Objects.equals(object, "") ? string2 : string2.toLowerCase());
                }
            } else {
                f2 = this.module.getPreviewLabelSize();
                object = this.module.getPreviewLabel();
            }
            GL11.glScalef(f2, f2, f2);
            float f3 = (float)Minecraft.getMinecraft().fontRenderer.getStringWidth((String)object) * f2;
            if (this.module.getPreviewType() == null) {
                Minecraft.getMinecraft().fontRenderer.drawString((String)object, (int)(((float)(this.x + 1 + this.width / 2) - f3 / 2.0f) / f2), (int)((float)(this.y + this.height / 2 - 32) / f2), -13750738);
            } else {
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow((String)object, (int)(((float)(this.x + 1 + this.width / 2) - f3 / 2.0f) / f2), (int)((float)(this.y + this.height / 2 - 32) / f2), -1);
            }
        } else if (this.module.getPreviewType() == AbstractModule.PreviewType.ICON) {
            float f4 = this.module.getPreviewIconWidth();
            f2 = this.module.getPreviewIconHeight();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.module.getPreviewIcon(), (float)(this.x + this.width / 2) - f4 / 2.0f + (float)n4, (float)(this.y + n3 + this.height / 2 - 26) - f2 / 2.0f, f4, f2);
        }
        GL11.glPopMatrix();
        float moduleNameOffset = this.y + this.height / 2f;
        playBold18px.drawCenteredString(this.module.getName(), (float)(this.x + this.width / 2) - 1.0681819f * 0.46808508f, moduleNameOffset + 1, 0x5F000000);
        playBold18px.drawCenteredString(this.module.getName(), (float)(this.x + this.width / 2) - 1.125f * 1.3333334f, moduleNameOffset, -1);
        this.toggle.displayString = this.module.isEnabled() ? "Disable" : "Enable";
        this.toggle.yOffset = this.yOffset;
        int n5 = this.toggle.lIIIIlIIllIIlIIlIIIlIIllI = this.module.isEnabled() ? -5756117 : -13916106;
        this.toggleOrHideFromHud.displayString = this.module.getGuiAnchor() == null ? (this.module.isRenderHud() && this.module.isEnabled() ? "Disable" : "Enable") : (this.module.isRenderHud() && this.module.isEnabled() ? "Hide from HUD" : "Add to HUD");
        this.toggleOrHideFromHud.lIIIIlIIllIIlIIlIIIlIIllI = this.module.isRenderHud() && this.module.isEnabled() ? -5756117 : -13916106;
        this.optionsButton.setDimensions(this.x + 4, this.y + this.height - 20, this.width - 8, 16);
        this.optionsButton.yOffset = this.yOffset;
        this.optionsButton.handleDrawElement(mouseX, mouseY, partialTicks);
        this.toggleOrHideFromHud.setDimensions(this.x + 4, this.y + this.height - 38, this.module.isEditable ? this.width - 8 : this.width / 2 + 2, this.y + this.height - 24 - (this.y + this.height - 38));
        this.toggleOrHideFromHud.yOffset = this.yOffset;
        this.toggleOrHideFromHud.handleDrawElement(mouseX, mouseY, partialTicks);
        if (!this.module.isEditable) {
            this.toggle.setDimensions(this.x + this.width / 2 + 8, this.y + this.height - 38, this.width / 2 - 12, this.y + this.height - 24 - (this.y + this.height - 38));
            this.toggle.handleDrawElement(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
        if (this.optionsButton.isMouseInside(mouseX, mouseY)) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            ((ModuleListElement) CBModulesGui.instance.IIIIllIIllIIIIllIllIIIlIl).llIlIIIlIIIIlIlllIlIIIIll = false;
            ((ModuleListElement)CBModulesGui.instance.IIIIllIIllIIIIllIllIIIlIl).scrollable = this.IlIlllIIIIllIllllIllIIlIl;
            ((ModuleListElement)CBModulesGui.instance.IIIIllIIllIIIIllIllIIIlIl).module = this.module;
            CBModulesGui.instance.currentScrollableElement = CBModulesGui.instance.IIIIllIIllIIIIllIllIIIlIl;
        } else if (!this.module.isEditable && this.toggle.isMouseInside(mouseX, mouseY)) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.module.setState(!this.module.isEnabled());
            this.toggle.displayString = this.module.isEnabled() ? "Disable" : "Enable";
            int n4 = this.toggle.lIIIIlIIllIIlIIlIIIlIIllI = this.module.isEnabled() ? -5756117 : -13916106;
            if (this.module.isEnabled()) {
                this.lIIIIIIIIIlIllIIllIlIIlIl();
                this.module.setState(true);
            }
        } else if (this.toggleOrHideFromHud.IlllIllIlIIIIlIIlIIllIIIl && this.toggleOrHideFromHud.isMouseInside(mouseX, mouseY)) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            if (!this.module.isEnabled()) {
                this.module.setRenderHud(true);
                this.lIIIIIIIIIlIllIIllIlIIlIl();
                if (this.module.getGuiAnchor() == null) {
                    this.module.setState(true);
                } else {
                    Minecraft.getMinecraft().displayGuiScreen(new CBModulePlaceGui(CBModulesGui.instance, this.module));
                }
            } else {
                this.module.setRenderHud(!this.module.isRenderHud());
                if (this.module.isRenderHud()) {
                    this.lIIIIIIIIIlIllIIllIlIIlIl();
                    if (this.module.getGuiAnchor() == null) {
                        this.module.setState(true);
                    } else {
                        Minecraft.getMinecraft().displayGuiScreen(new CBModulePlaceGui(CBModulesGui.instance, this.module));
                    }
                } else if (this.module.isEditable && this.module.isEnabled()) {
                    this.module.setState(false);
                }
            }
            this.toggleOrHideFromHud.displayString = this.module.getGuiAnchor() == null ? (this.module.isRenderHud() && this.module.isEnabled() ? "Disable" : "Enable") : (this.module.isRenderHud() && this.module.isEnabled() ? "Hide from HUD" : "Add to HUD");
            this.toggleOrHideFromHud.lIIIIlIIllIIlIIlIIIlIIllI = this.module.isRenderHud() && this.module.isEnabled() ? -5756117 : -13916106;
        }
    }

    private void lIIIIIIIIIlIllIIllIlIIlIl() {
        if (this.module == CheatBreaker.getInstance().moduleManager.llIIlllIIIIlllIllIlIlllIl) {
            return;
        }
        for (Setting cBSetting : this.module.getSettingsList()) {
            if (cBSetting.getType() != Setting.Type.INTEGER || !cBSetting.getLabel().toLowerCase().contains("color") || cBSetting.getLabel().toLowerCase().contains("background") || cBSetting.getLabel().toLowerCase().contains("pressed")) continue;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            cBSetting.setValue(CheatBreaker.getInstance().globalSettings.defaultColor.getValue());
        }
    }
}
