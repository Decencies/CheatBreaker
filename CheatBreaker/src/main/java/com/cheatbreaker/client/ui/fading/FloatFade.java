package com.cheatbreaker.client.ui.fading;

public class FloatFade
        extends AbstractFade {
    public FloatFade(long l) {
        super(l, 1.0f);
    }

    public FloatFade(long l, float f) {
        super(l, f);
    }

    @Override
    protected float getValue() {
        return (float)(this.IlllIIIlIlllIllIlIIlllIlI - this.llIIlllIIIIlllIllIlIlllIl()) / (float)this.IlllIIIlIlllIllIlIIlllIlI;
    }
}

