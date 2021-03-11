package com.cheatbreaker.client.event.type;

import com.cheatbreaker.client.event.EventBus;
import net.minecraft.client.multiplayer.WorldClient;

public class LoadWorldEvent extends EventBus.Event {

    private final WorldClient world;

    public LoadWorldEvent(WorldClient world) {
        this.world = world;
    }

    public WorldClient getWorld() {
        return world;
    }
}
