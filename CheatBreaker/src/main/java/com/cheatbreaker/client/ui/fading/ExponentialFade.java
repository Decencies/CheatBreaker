package com.cheatbreaker.client.ui.fading;

public class ExponentialFade
        extends FloatFade {
    public ExponentialFade(long l) {
        super(l);
    }

    @Override
    public float getValue() {
        float f = super.getValue();
        return (float)Math.pow(f * (2.0f - f), 1.0);
    }
}
