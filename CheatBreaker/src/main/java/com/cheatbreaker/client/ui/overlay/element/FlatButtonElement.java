package com.cheatbreaker.client.ui.overlay.element;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import net.minecraft.client.gui.Gui;

public class FlatButtonElement extends AbstractElement {
    private String IIIllIllIlIlllllllIlIlIII;

    public FlatButtonElement(String string) {
        this.IIIllIllIlIlllllllIlIlIII = string;
    }

    @Override
    public void handleElementDraw(float f, float f2, boolean bl) {
        this.lIIIIlIIllIIlIIlIIIlIIllI(this.IIIllIllIlIlllllllIlIlIII, f, f2, bl);
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(String string, float f, float f2, boolean bl) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, bl && this.isMouseInside(f, f2) ? -16747106 : -13158601);
        CheatBreaker.getInstance().playRegular14px.drawCenteredString(string, this.x + this.width / 2.0f, this.y + this.height / 2.0f - (float)5, -1);
    }

    public String IllIIIIIIIlIlIllllIIllIII() {
        return this.IIIllIllIlIlllllllIlIlIII;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(String string) {
        this.IIIllIllIlIlllllllIlIlIII = string;
    }
}
