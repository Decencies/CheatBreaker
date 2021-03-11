package com.cheatbreaker.client.ui.overlay;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.fading.FloatFade;
import com.cheatbreaker.client.ui.fading.AbstractFade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Alert {
    private final AbstractFade fade = new FloatFade(275L);
    private static final int lIIIIIIIIIlIllIIllIlIIlIl = 140;
    private static final int IlllIIIlIlllIllIlIIlllIlI = 55;
    private boolean IIIIllIlIIIllIlllIlllllIl;
    private float x;
    private float IlIlIIIlllIIIlIlllIlIllIl;
    private float IIIllIllIlIlllllllIlIlIII;
    private final String IllIIIIIIIlIlIllllIIllIII;
    private final String[] lines;
    private final long createdTime;

    public Alert(String string, String[] arrstring, float f, float f2) {
        this.IllIIIIIIIlIlIllllIIllIII = string;
        this.lines = arrstring;
        this.x = f;
        this.IIIllIllIlIlllllllIlIlIII = f2;
        this.IlIlIIIlllIIIlIlllIlIllIl = f2;
        this.createdTime = System.currentTimeMillis();
    }

    public void drawAlert() {
        float f = this.IlIlIIIlllIIIlIlllIlIllIl - (this.IlIlIIIlllIIIlIlllIlIllIl - this.IIIllIllIlIlllllllIlIlIII) * this.fade.IllIIIIIIIlIlIllllIIllIII();
        if (this.IIIIllIlIIIllIlllIlllllIl) {
            Gui.drawGradientRect(this.x, f, this.x + (float)140, f + (float)55, -819057106, -822083584);
            for (int i = 0; i < this.lines.length && i <= 3; ++i) {
                CheatBreaker.getInstance().playRegular16px.drawString(this.lines[i], this.x + (float)4, f + (float)4 + (float)(i * 10), -1);
            }
        } else {
            Gui.drawGradientRect(this.x, f, this.x + (float)140, f + (float)55, -819057106, -822083584);
            CheatBreaker.getInstance().playRegular16px.drawString(this.IllIIIIIIIlIlIllllIIllIII, this.x + (float)4, f + (float)4, -1);
            Gui.drawRect(this.x + (float)4, f + 14.5f, this.x + (float)140 - (float)5, f + (float)15, 0x2E5E5E5E);
            for (int i = 0; i < this.lines.length && i <= 2; ++i) {
                CheatBreaker.getInstance().playRegular16px.drawString(this.lines[i], this.x + (float)4, f + (float)17 + (float)(i * 10), -1);
            }
        }
        if (!(Minecraft.getMinecraft().currentScreen instanceof OverlayGui)) {
            CheatBreaker.getInstance().playRegular16px.drawString("Press Shift + Tab", this.x + (float)4, f + (float) Alert.IIIIllIIllIIIIllIllIIIlIl() - (float)12, 0x6FFFFFFF);
        }
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(float f) {
        this.IlIlIIIlllIIIlIlllIlIllIl = this.IIIllIllIlIlllllllIlIlIII;
        this.IIIllIllIlIlllllllIlIlIII = f;
        this.fade.lIIIIIIIIIlIllIIllIlIIlIl();
    }

    public boolean lIIIIIIIIIlIllIIllIlIIlIl() {
        return !this.fade.IIIIllIlIIIllIlllIlllllIl() || this.fade.IIIIllIIllIIIIllIllIIIlIl();
    }

    public boolean IlllIIIlIlllIllIlIIlllIlI() {
        return System.currentTimeMillis() - this.createdTime > 3500L;
    }

    public static void displayMessage(String string, String string2) {
        OverlayGui.getInstance().queueAlert(string, string2);
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(String string) {
        OverlayGui.getInstance().setSection(string);
    }

    public static int IIIIllIlIIIllIlllIlllllIl() {
        return 140;
    }

    public static int IIIIllIIllIIIIllIllIIIlIl() {
        return 55;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(boolean bl) {
        this.IIIIllIlIIIllIlllIlllllIl = bl;
    }

    public float getX() {
        return this.x;
    }

    public float IIIllIllIlIlllllllIlIlIII() {
        return this.IlIlIIIlllIIIlIlllIlIllIl;
    }

    public float IllIIIIIIIlIlIllllIIllIII() {
        return this.IIIllIllIlIlllllllIlIlIII;
    }

    public void setX(float f) {
        this.x = f;
    }

    public void IlllIIIlIlllIllIlIIlllIlI(float f) {
        this.IlIlIIIlllIIIlIlllIlIllIl = f;
    }

    public void IIIIllIlIIIllIlllIlllllIl(float f) {
        this.IIIllIllIlIlllllllIlIlIII = f;
    }
}
