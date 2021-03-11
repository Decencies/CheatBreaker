package com.cheatbreaker.client.audio;

import javax.sound.sampled.*;

public class JitterBuffer
{
    public byte[] buffer;
    public final AudioFormat format;
    public int threshold;

    public JitterBuffer(final AudioFormat format, final int jitter) {
        this.format = format;
        this.updateJitter(jitter);
    }

    public void clearBuffer(final int jitterSize) {
        this.buffer = new byte[0];
        this.updateJitter(jitterSize);
    }

    public byte[] get() {
        return this.buffer;
    }

    private int getSizeInBytes(final AudioFormat fmt, final int size) {
        final int s = (int)(fmt.getSampleRate() / 1000.0f);
        final int sampleSize = (int)(fmt.getSampleSizeInBits() / 8 * 0.49f);
        return (sampleSize != 0) ? (s * size / sampleSize) : 0;
    }

    public boolean isReady() {
        return this.buffer.length > this.threshold;
    }

    public void push(final byte[] data) {
        this.write(data);
    }

    public void updateJitter(final int size) {
        this.threshold = this.getSizeInBytes(this.format, size);
        if (this.buffer == null) {
            this.buffer = ((this.threshold != 0) ? new byte[3 * this.threshold] : new byte[320]);
        }
    }

    private void write(final byte[] write) {
        final byte[] result = new byte[this.buffer.length + write.length];
        System.arraycopy(this.buffer, 0, result, 0, this.buffer.length);
        System.arraycopy(write, 0, result, this.buffer.length, write.length);
        this.buffer = result;
    }
}
