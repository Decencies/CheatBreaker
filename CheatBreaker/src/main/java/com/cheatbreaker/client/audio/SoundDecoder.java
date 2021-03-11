package com.cheatbreaker.client.audio;

import com.cheatbreaker.client.CheatBreaker;
import org.xiph.speex.SpeexDecoder;

import javax.sound.sampled.AudioFormat;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SoundDecoder {
    SpeexDecoder decoder;
    byte[] buffer;

    public static List<byte[]> divideArray(final byte[] source, final int chunksize) {
        final List<byte[]> result = new ArrayList<byte[]>();
        for (int start = 0; start < source.length; start += chunksize) {
            final int end = Math.min(source.length, start + chunksize);
            result.add(Arrays.copyOfRange(source, start, end));
        }
        return result;
    }

    public boolean process(UUID id, final byte[] encodedSamples, final int chunkSize) {
        if (chunkSize > encodedSamples.length) {
            System.err.println("Sound Pre-Processor has been given incorrect data from network, sample pieces cannot be bigger than whole sample. ");
            return false;
        }
        AudioFormat audioFormat = CheatBreaker.universalAudioFormat;
        if (this.decoder == null) {
            (this.decoder = new SpeexDecoder()).init(0, (int) audioFormat.getSampleRate(), audioFormat.getChannels(), true);
        }
        byte[] decodedData;
        if (encodedSamples.length <= chunkSize) {
            try {
                this.decoder.processData(encodedSamples, 0, encodedSamples.length);
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
                return false;
            }
            decodedData = new byte[this.decoder.getProcessedDataByteSize()];
            this.decoder.getProcessedData(decodedData, 0);
        } else {
            final List<byte[]> samplesList = divideArray(encodedSamples, chunkSize);
            this.buffer = new byte[0];
            for (final byte[] sample : samplesList) {
                final SpeexDecoder tempDecoder = new SpeexDecoder();
                tempDecoder.init(0, (int) audioFormat.getSampleRate(), audioFormat.getChannels(), true);
                try {
                    this.decoder.processData(sample, 0, sample.length);
                } catch (StreamCorruptedException e2) {
                    e2.printStackTrace();
                    return false;
                }
                final byte[] sampleBuffer = new byte[this.decoder.getProcessedDataByteSize()];
                this.decoder.getProcessedData(sampleBuffer, 0);
                this.write(sampleBuffer);
            }
            decodedData = this.buffer;
        }
        if (decodedData != null) {
            CheatBreaker.getInstance().getVoiceChatManager().addQueue(decodedData, id);
            this.buffer = new byte[0];
            return true;
        }
        return false;
    }

    private void write(final byte[] write) {
        final byte[] result = new byte[this.buffer.length + write.length];
        System.arraycopy(this.buffer, 0, result, 0, this.buffer.length);
        System.arraycopy(write, 0, result, this.buffer.length, write.length);
        this.buffer = result;
    }
}

