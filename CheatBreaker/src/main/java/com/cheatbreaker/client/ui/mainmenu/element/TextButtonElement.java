package com.cheatbreaker.client.ui.mainmenu.element;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.fading.ColorFade;
import com.cheatbreaker.client.ui.mainmenu.AbstractElement;

public class TextButtonElement extends AbstractElement {
    private final String IIIllIllIlIlllllllIlIlIII;
    private final ColorFade IllIIIIIIIlIlIllllIIllIII;

    public TextButtonElement(String string) {
        this.IIIllIllIlIlllllllIlIlIII = string;
        this.IllIIIIIIIlIlIllllIIllIII = new ColorFade(-1879048193, -1);
    }

    @Override
    protected void handleElementDraw(float f, float f2, boolean bl) {
        CheatBreaker.getInstance().robotoBold14px.drawString(this.IIIllIllIlIlllllllIlIlIII, this.x + (float)6, this.y + (float)6, this.IllIIIIIIIlIlIllllIIllIII.lIIIIIIIIIlIllIIllIlIIlIl(this.isMouseInside(f, f2) && bl).getRGB());
    }
}