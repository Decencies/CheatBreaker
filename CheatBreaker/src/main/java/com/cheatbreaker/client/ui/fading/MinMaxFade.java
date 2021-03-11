package com.cheatbreaker.client.ui.fading;

public class MinMaxFade extends FloatFade {
    public MinMaxFade(long l) {
        super(l);
    }

    @Override
    protected float getValue() {
        float f = super.getValue();
        return (double)f < 0.5 ? 2.0f * f * f : (float)-1 + ((float)4 - 2.0f * f) * f;
    }
}

