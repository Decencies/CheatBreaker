package com.cheatbreaker.client.ui.module;

import com.cheatbreaker.client.module.AbstractModule;

public class CBModulePosition {

    protected AbstractModule module;
    protected float x;
    protected float y;

    CBModulePosition(AbstractModule cBModule, float x, float y) {
        this.module = cBModule;
        this.x = x;
        this.y = y;
    }

}
