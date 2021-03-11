package com.cheatbreaker.client.module.type.keystrokes;

import com.cheatbreaker.client.ui.module.CBModulesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class Key {
    private final String displayString;
    private int keyCode;
    private final float width;
    private final float height;
    private float fadeTime;
    private boolean pressed;
    private long lastPressed;
    private Color pressedColor;
    private Color unpressedColor;

    public Key(String string, int keyCode, float width, float height, float fadeTime) {
        this.displayString = string;
        this.keyCode = keyCode;
        this.width = width;
        this.height = height;
        this.fadeTime = fadeTime;
    }

    public void render(final float n, final float n2, final int n3, final int n4, final int n5, final int n6) {
        final Minecraft minecraft = Minecraft.getMinecraft();
        final int n7 = (this.keyCode == -99 || this.keyCode == -100) ? ((this.keyCode == -99) ? 1 : 0) : -1;
        final boolean pressed = (minecraft.currentScreen == null || minecraft.currentScreen instanceof GuiContainer || minecraft.currentScreen instanceof CBModulesGui) && ((n7 != -1) ? Mouse.isButtonDown(n7) : Keyboard.isKeyDown(this.keyCode));
        if (pressed && !this.pressed) {
            this.pressed = true;
            this.lastPressed = System.currentTimeMillis();
            this.pressedColor = new Color(n5, true);
            this.unpressedColor = new Color(n6, true);
        }
        else if (this.pressed && !pressed) {
            this.pressed = false;
            this.lastPressed = System.currentTimeMillis();
            this.pressedColor = new Color(n6, true);
            this.unpressedColor = new Color(n5, true);
        }
//        if (pressed) {
//            System.out.println(displayString);
//        }
        int rgb;
        if (System.currentTimeMillis() - this.lastPressed < fadeTime) {
            final float n9 = (System.currentTimeMillis() - this.lastPressed) / fadeTime;
            rgb = new Color(
                    (int)Math.abs(n9 * this.unpressedColor.getRed() + (1.0f - n9) * this.pressedColor.getRed()),
                    (int)Math.abs(n9 * this.unpressedColor.getGreen() + (1.0f - n9) * this.pressedColor.getGreen()),
                    (int)Math.abs(n9 * this.unpressedColor.getBlue() + (1.0f - n9) * this.pressedColor.getBlue()),
                    (int)Math.abs(n9 * this.unpressedColor.getAlpha() + (1.0f - n9) * this.pressedColor.getAlpha())
            ).getRGB();
        }
        else {
            rgb = (pressed ? n6 : n5);
        }
        Gui.drawRect(n, n2, n + this.width, n2 + this.height, rgb);
        if (this.keyCode == minecraft.gameSettings.keyBindJump.getKeyCode()) {
            Gui.drawRect(n + this.width / 2 - this.width / 8, n2 + this.height / 2, n + this.width / 2.0f + this.width / 8, n2 + this.height / 2 + 1, 0xFF000000 | (pressed ? n4 : n3));
        }
        else {
            // added pixel perfect aligning
            int width = minecraft.fontRenderer.getStringWidth(displayString);
            int offset = displayString.length() == 1 ? 1 : (displayString.contains("R") ? 2 : 1);
            minecraft.fontRenderer.drawString(this.displayString, (int)(n + this.width / 2.0f) - width / 2 + offset, (int)(n2 + this.height / 2.0f - minecraft.fontRenderer.FONT_HEIGHT / 2 + 1.0f), pressed ? n4 : n3);
        }
    }

    public void setKeyCode(int n) {
        this.keyCode = n;
    }

    public String getDisplayString() {
        return this.displayString;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public int getKeyCode() {
        return this.keyCode;
    }
}
