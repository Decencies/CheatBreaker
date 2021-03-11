package com.cheatbreaker.client.event.type;

import com.cheatbreaker.client.event.EventBus;

public class KeyboardEvent extends EventBus.Event {

    private final int keyboardKey;

    public KeyboardEvent(int keyboardKey) {
        this.keyboardKey = keyboardKey;
    }

    public int getKeyboardKey() {
        return keyboardKey;
    }

}
