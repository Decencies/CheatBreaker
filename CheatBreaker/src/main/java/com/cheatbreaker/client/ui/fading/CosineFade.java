package com.cheatbreaker.client.ui.fading;

public class CosineFade
        extends FloatFade {
    public CosineFade(long l) {
        super(l, 0.0f);
    }

    @Override
    protected float getValue() {
        float f = super.getValue();
        float f2 = f * 2.0f - 1.0f;
        return (float)(Math.cos((double)f2 * (0.6666666865348816 * 4.712388839944558)) + 1.0) / 2.0f;
    }
}
