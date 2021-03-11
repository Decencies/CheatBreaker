package com.cheatbreaker.client.audio;

import javax.sound.sampled.*;

import java.nio.*;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.nethandler.client.PacketClientVoice;
import org.xiph.speex.*;

public class MicrophoneRecroder implements Runnable
{
    private boolean recording;
    private Thread thread;
    private final AudioDevice audioDevice;
    byte[] buffer;
    public float microphoneVolume;

    public MicrophoneRecroder(AudioDevice audioDevice) {
        this.audioDevice = audioDevice;
    }

    private byte[] boostVolume(final byte[] data) {
        final ByteBuffer buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        final ByteBuffer newBuf = ByteBuffer.allocate(data.length).order(ByteOrder.LITTLE_ENDIAN);
        while (buf.hasRemaining()) {
            int sample = buf.getShort() & 0xFFFF;
            sample *= (microphoneVolume / 100) + 5;
            System.out.println((1 + microphoneVolume * 10) + 1 + ": " + (microphoneVolume / 100) + 5);
            newBuf.putShort((short)(sample & 0xFFFF));
        }
        return newBuf.array();
    }

    @Override
    public void run() {
        final AudioFormat format = CheatBreaker.universalAudioFormat;
        final TargetDataLine recordingLine = audioDevice.getDataLine();
        if (recordingLine == null) {
            System.err.println("[Voice Chat] Attempted to record input device, but failed! Java Sound System hasn't found any microphones, check your input devices and restart Minecraft.");
            return;
        }
        if (!this.startLine(recordingLine)) {
            //this.voiceChat.setRecorderActive(false);
            this.stop();
            return;
        }
        final SpeexEncoder encoder = new SpeexEncoder();
        encoder.init(0, 10, (int)format.getSampleRate(), format.getChannels());
        final int blockSize = encoder.getFrameSize() * format.getChannels() * 2;
        final byte[] normBuffer = new byte[blockSize * 2];
        recordingLine.start();
        this.buffer = new byte[0];
        byte pieceSize = 0;
        while (this.recording) {
            final int read = recordingLine.read(normBuffer, 0, blockSize);
            if (read == -1) {
                break;
            }
            final byte[] boostedBuffer = this.boostVolume(normBuffer);
            if (!encoder.processData(boostedBuffer, 0, blockSize)) {
                break;
            }
            final int encoded = encoder.getProcessedData(boostedBuffer, 0);
            final byte[] encoded_data = new byte[encoded];
            System.arraycopy(boostedBuffer, 0, encoded_data, 0, encoded);
            pieceSize = (byte)encoded;
            this.write(encoded_data);
            if (this.buffer.length < 144) {
                continue;
            }
            CheatBreaker.getInstance().getNetHandler().sendPacketToQueue(new PacketClientVoice(buffer));
            this.buffer = new byte[0];
        }
        if (this.buffer.length > 0) {
            CheatBreaker.getInstance().getNetHandler().sendPacketToQueue(new PacketClientVoice(buffer));
        }
        recordingLine.stop();
        recordingLine.close();
    }

    public void set(final boolean toggle) {
        if (toggle) {
            this.start();
        }
        else {
            this.stop();
        }
    }

    public void start() {
        this.thread = new Thread(this, "Input Device Recorder");
        this.recording = true;
        this.thread.start();
    }

    private boolean startLine(final TargetDataLine recordingLine) {
        try {
            recordingLine.open();
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
            System.err.println("[Voice Chat] Failed to open recording line! " + recordingLine.getFormat());
            return false;
        }
        return true;
    }

    public void stop() {
        this.recording = false;
        this.thread = null;
    }

    private void write(final byte[] write) {
        final byte[] result = new byte[this.buffer.length + write.length];
        System.arraycopy(this.buffer, 0, result, 0, this.buffer.length);
        System.arraycopy(write, 0, result, this.buffer.length, write.length);
        this.buffer = result;
    }
}
