package com.cheatbreaker.client.audio;

import javax.sound.sampled.TargetDataLine;

public class AudioDevice {

    private final String name;
    private final String descriptor;
    private final TargetDataLine dataLine;

    public AudioDevice(String name, String descriptor, TargetDataLine dataLine) {
        this.name = name;
        this.descriptor = descriptor;
        this.dataLine = dataLine;
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public TargetDataLine getDataLine() {
        return dataLine;
    }

}
