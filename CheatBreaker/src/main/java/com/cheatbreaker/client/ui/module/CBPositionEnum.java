package com.cheatbreaker.client.ui.module;

public enum CBPositionEnum {
    BOTTOM("BOTTOM"),
    TOP("TOP"),
    CENTER("CENTER"),
    LEFT("LEFT"),
    RIGHT("RIGHT");

    private final String identifier;

    private CBPositionEnum(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }
}
