package com.cheatbreaker.client.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a profile to be stored to the disk.
 * @author Decencies
 */
@Getter
@Setter
public class Profile {

    private int index = 0;
    private final boolean editable;
    private String name;

    public Profile(String name, boolean editable) {
        this.name = name;
        this.editable = editable;
    }

}
