package com.cheatbreaker.client.event.type;

import com.cheatbreaker.client.event.EventBus;
import net.minecraft.client.gui.ScaledResolution;

public class GuiDrawEvent extends EventBus.Event {

    private final ScaledResolution resolution;

    public GuiDrawEvent(ScaledResolution resolution) {
        this.resolution = resolution;
    }

    public ScaledResolution getResolution() {
        return resolution;
    }
}
