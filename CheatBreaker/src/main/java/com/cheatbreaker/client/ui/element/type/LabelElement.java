package com.cheatbreaker.client.ui.element.type;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import net.minecraft.client.gui.Gui;

public class LabelElement
        extends AbstractModulesGuiElement {
    private Setting lIIIIlIIllIIlIIlIIIlIIllI;

    public LabelElement(Setting cBSetting, float f) {
        super(f);
        this.lIIIIlIIllIIlIIlIIIlIIllI = cBSetting;
        this.height = 12;
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        CheatBreaker.getInstance().ubuntuMedium16px.drawString(((String)this.lIIIIlIIllIIlIIlIIIlIIllI.getValue()).toUpperCase(), this.x + 2, (float)(this.y + 2), 0x6F000000);
        Gui.drawRect(this.x + 2, this.y + this.height - 1, this.x + this.width / 2 - 20, this.y + this.height, 0x1F2F2F2F);
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
    }
}