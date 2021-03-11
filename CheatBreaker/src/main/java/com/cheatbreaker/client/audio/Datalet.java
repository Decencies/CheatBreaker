package com.cheatbreaker.client.audio;

import java.util.UUID;

public class Datalet
{
    public final UUID id;
    public final byte[] data;
    public int volume;
    public boolean direct;

    public Datalet(final UUID id, final byte[] data) {
        this.id = id;
        this.data = data;
        this.direct = true;
    }
}
