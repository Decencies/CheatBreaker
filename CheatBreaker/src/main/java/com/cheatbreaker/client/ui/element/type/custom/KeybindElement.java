package com.cheatbreaker.client.ui.element.type.custom;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.element.module.ModulesGuiButtonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class KeybindElement
        extends AbstractModulesGuiElement {
    private Setting lIIIIlIIllIIlIIlIIIlIIllI;
    private ModulesGuiButtonElement IllIIIIIIIlIlIllllIIllIII;
    private boolean lIIIIllIIlIlIllIIIlIllIlI = false;

    public KeybindElement(Setting cBSetting, float f) {
        super(f);
        this.lIIIIlIIllIIlIIlIIIlIIllI = cBSetting;
        this.height = 12;
        this.IllIIIIIIIlIlIllllIIllIII = new ModulesGuiButtonElement(CheatBreaker.getInstance().playBold18px, null, Keyboard.getKeyName((Integer)cBSetting.getValue()), this.x + this.width - 100, this.y, 96, 18, -9442858, f);
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        boolean bl = (float) mouseX > (float)(this.x + this.width - 48) * this.scale && (float) mouseX < (float)(this.x + this.width - 10) * this.scale && (float) mouseY > (float)(this.y + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + 10 + this.yOffset) * this.scale;
        boolean bl2 = (float) mouseX > (float)(this.x + this.width - 92) * this.scale && (float) mouseX < (float)(this.x + this.width - 48) * this.scale && (float) mouseY > (float)(this.y + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + 10 + this.yOffset) * this.scale;
        CheatBreaker.getInstance().ubuntuMedium16px.drawString(this.lIIIIlIIllIIlIIlIIIlIIllI.getLabel().toUpperCase(), this.x + 10, (float)(this.y + 4), bl2 || bl ? -1090519040 : -1895825408);
        if (this.lIIIIllIIlIlIllIIIlIllIlI && Keyboard.getEventKeyState()) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.lIIIIlIIllIIlIIlIIIlIIllI.setValue(Keyboard.getEventKey());
            this.IllIIIIIIIlIlIllllIIllIII.displayString = Keyboard.getKeyName((Integer)this.lIIIIlIIllIIlIIlIIIlIIllI.getValue());
            this.lIIIIllIIlIlIllIIIlIllIlI = false;
        }
        this.IllIIIIIIIlIlIllllIIllIII.yOffset = this.yOffset;
        this.IllIIIIIIIlIlIllllIIllIII.setDimensions(this.x + this.width - 100, this.y, 96, 18);
        this.IllIIIIIIIlIlIllllIIllIII.handleDrawElement(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
        if (this.IllIIIIIIIlIlIllllIIllIII.isMouseInside(mouseX, mouseY)) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.lIIIIllIIlIlIllIIIlIllIlI = true;
            this.IllIIIIIIIlIlIllllIIllIII.displayString = "<PRESS ANY KEY>";
        }
    }
}
