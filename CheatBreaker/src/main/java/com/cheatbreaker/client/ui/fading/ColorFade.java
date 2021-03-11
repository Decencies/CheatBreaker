package com.cheatbreaker.client.ui.fading;

import java.awt.Color;

public class ColorFade
        extends ExponentialFade {
    private int IllIIIIIIIlIlIllllIIllIII;
    private int lIIIIllIIlIlIllIIIlIllIlI;
    private boolean IlllIllIlIIIIlIIlIIllIIIl;
    private Color IlIlllIIIIllIllllIllIIlIl;
    private Color llIIlllIIIIlllIllIlIlllIl;

    public ColorFade(long l, int n, int n2) {
        super(l);
        this.IllIIIIIIIlIlIllllIIllIII = n;
        this.lIIIIllIIlIlIllIIIlIllIlI = n2;
    }

    public ColorFade(int n, int n2) {
        this(175L, n, n2);
    }

    public Color lIIIIIIIIIlIllIIllIlIIlIl(boolean bl) {
        Color color = new Color(bl ? this.lIIIIllIIlIlIllIIIlIllIlI : this.IllIIIIIIIlIlIllllIIllIII, true);
        if (bl && !this.IlllIllIlIIIIlIIlIIllIIIl) {
            this.IlllIllIlIIIIlIIlIIllIIIl = true;
            this.IlIlllIIIIllIllllIllIIlIl = new Color(this.IllIIIIIIIlIlIllllIIllIII, true);
            this.llIIlllIIIIlllIllIlIlllIl = new Color(this.lIIIIllIIlIlIllIIIlIllIlI, true);
            this.lIIIIIIIIIlIllIIllIlIIlIl();
        } else if (this.IlllIllIlIIIIlIIlIIllIIIl && !bl) {
            this.IlllIllIlIIIIlIIlIIllIIIl = false;
            this.IlIlllIIIIllIllllIllIIlIl = new Color(this.lIIIIllIIlIlIllIIIlIllIlI, true);
            this.llIIlllIIIIlllIllIlIlllIl = new Color(this.IllIIIIIIIlIlIllllIIllIII, true);
            this.lIIIIIIIIIlIllIIllIlIIlIl();
        }
        if (this.IIIllIllIlIlllllllIlIlIII()) {
            float f = super.IllIIIIIIIlIlIllllIIllIII();
            int n = (int)Math.abs(f * (float)this.llIIlllIIIIlllIllIlIlllIl.getRed() + (1.0f - f) * (float)this.IlIlllIIIIllIllllIllIIlIl.getRed());
            int n2 = (int)Math.abs(f * (float)this.llIIlllIIIIlllIllIlIlllIl.getGreen() + (1.0f - f) * (float)this.IlIlllIIIIllIllllIllIIlIl.getGreen());
            int n3 = (int)Math.abs(f * (float)this.llIIlllIIIIlllIllIlIlllIl.getBlue() + (1.0f - f) * (float)this.IlIlllIIIIllIllllIllIIlIl.getBlue());
            int n4 = (int)Math.abs(f * (float)this.llIIlllIIIIlllIllIlIlllIl.getAlpha() + (1.0f - f) * (float)this.IlIlllIIIIllIllllIllIIlIl.getAlpha());
            color = new Color(n, n2, n3, n4);
        }
        return color;
    }

    public void lIIIIIIIIIlIllIIllIlIIlIl(int n) {
        this.IllIIIIIIIlIlIllllIIllIII = n;
    }

    public void IlllIIIlIlllIllIlIIlllIlI(int n) {
        this.lIIIIllIIlIlIllIIIlIllIlI = n;
    }
}