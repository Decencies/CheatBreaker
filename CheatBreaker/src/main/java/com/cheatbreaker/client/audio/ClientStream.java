package com.cheatbreaker.client.audio;

import com.cheatbreaker.client.CheatBreaker;

import java.util.UUID;

public class ClientStream
{
    public int volume;
    public int special;
    public boolean needsEnd;
    public boolean direct;
    public long lastUpdated;
    public JitterBuffer buffer;
    public PlayerProxy proxy;
    public boolean dirty;
    public UUID id;

    public ClientStream(final PlayerProxy proxy, final UUID id, final boolean direct) {
        this.direct = direct;
        this.id = id;
        this.lastUpdated = System.currentTimeMillis();
        this.proxy = proxy;
        this.buffer = new JitterBuffer(CheatBreaker.universalAudioFormat, 0);
    }

    public int getJitterRate() {
        return this.getLastTimeUpdatedMS();
    }

    public int getLastTimeUpdatedMS() {
        return (int)(System.currentTimeMillis() - this.lastUpdated);
    }

    public void update(final Datalet data, final int l) {
        if (this.direct != data.direct) {
            this.dirty = true;
        }
        this.direct = data.direct;
        this.volume = data.volume;
    }

}