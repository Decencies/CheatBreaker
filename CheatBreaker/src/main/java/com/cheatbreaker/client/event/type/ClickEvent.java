package com.cheatbreaker.client.event.type;

import com.cheatbreaker.client.event.EventBus;

public class ClickEvent extends EventBus.Event {

    private final int mouseButton;

    public ClickEvent(int mouseButton) {
        this.mouseButton = mouseButton;
    }

    public int getMouseButton() {
        return mouseButton;
    }
}
