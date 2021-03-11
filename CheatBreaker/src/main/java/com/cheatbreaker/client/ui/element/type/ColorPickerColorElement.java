package com.cheatbreaker.client.ui.element.type;

import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import net.minecraft.client.gui.Gui;

public class ColorPickerColorElement
        extends AbstractModulesGuiElement {
    public int color;

    public ColorPickerColorElement(float f, int n) {
        super(f);
        this.color = n;
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -1358954496);
        Gui.drawRect(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, this.color | 0xFF000000);
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
    }
}
