package com.cheatbreaker.client.event.type;

import com.cheatbreaker.client.event.EventBus;

public class PluginMessageEvent extends EventBus.Event {

    private final String channel;
    private final byte[] payload;

    public PluginMessageEvent(String channel, byte[] payload) {
        this.channel = channel;
        this.payload = payload;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public String getChannel() {
        return this.channel;
    }
}
