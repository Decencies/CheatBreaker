package com.cheatbreaker.client.ui.element;

import com.cheatbreaker.client.ui.module.CBModulesGui;
import net.minecraft.client.Minecraft;

public abstract class AbstractModulesGuiElement {
    public float scale;
    public int yOffset = 0;
    public int x;
    protected int y;
    protected int width;
    protected int height;

    public AbstractModulesGuiElement(float scaleFactor) {
        this.scale = scaleFactor;
    }

    public void setDimensions(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void handleDrawElement(int mouseX, int mouseY, float partialTicks);

    public abstract void handleMouseClick(int mouseX, int mouseY, int button);

    public boolean isMouseInside(int mouseX, int mouseY) {
        boolean minX = (float)mouseX > (float)this.x * this.scale;
        boolean maxX = (float)mouseX < (float)(this.x + this.width) * this.scale;
        boolean minY = (float)mouseY > (float)(this.y + this.yOffset) * this.scale;
        boolean maxY = (float)mouseY < (float)(this.y + this.height + this.yOffset) * this.scale;
        return minX && maxX && minY && maxY && Minecraft.getMinecraft().currentScreen instanceof CBModulesGui;
    }

    public void onScroll(int n) {
    }

    public int getHeight() {
        return this.height;
    }
}

