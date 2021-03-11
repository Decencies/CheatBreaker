package com.cheatbreaker.client.util.dash;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.cheatbreaker.client.CheatBreaker;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDeviceBase;

public class DashPlayer extends AudioDeviceBase {

    private SourceDataLine sourceDataLine = null;
    private AudioFormat audioFormat = null;
    private byte[] byteArray = new byte[4096];

    protected void setAudioFormat(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    protected AudioFormat getAudioFormat() {
        if (this.audioFormat == null) {
            Decoder decoder = this.getDecoder();
            this.audioFormat = new AudioFormat(decoder.getOutputFrequency(), 16, decoder.getOutputChannels(), true, false);
        }
        return this.audioFormat;
    }

    protected DataLine.Info getInfo() {
        AudioFormat audioFormat = this.getAudioFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        return info;
    }

    public void open(AudioFormat audioFormat) {
        if (!this.isOpen()) {
            this.setAudioFormat(audioFormat);
            this.openImpl();
            this.setOpen(true);
        }
    }

    protected void openImpl() {
    }

    protected void start() throws JavaLayerException {
        Throwable throwable = null;
        try {
            Line line = AudioSystem.getLine(this.getInfo());
            if (line instanceof SourceDataLine) {
                this.sourceDataLine = (SourceDataLine)line;
                this.sourceDataLine.open(this.audioFormat);
                this.sourceDataLine.start();
                this.setFloatControlValue((float)(CheatBreaker.getInstance().getGlobalSettings().radioVolume.getValue()));
            }
        }
        catch (RuntimeException runtimeException) {
            throwable = runtimeException;
        }
        catch (LinkageError linkageError) {
            throwable = linkageError;
        }
        catch (LineUnavailableException lineUnavailableException) {
            throwable = lineUnavailableException;
        }
        if (this.sourceDataLine == null) {
            throw new JavaLayerException("cannot obtain source audio line", throwable);
        }
    }

    public int lIIIIlIIllIIlIIlIIIlIIllI(AudioFormat audioFormat, int n) {
        return (int)((double)((float)n * audioFormat.getSampleRate() * (float)audioFormat.getChannels() * (float)audioFormat.getSampleSizeInBits()) / (double)8000);
    }

    protected void closeImpl() {
        if (this.sourceDataLine != null) {
            this.sourceDataLine.close();
        }
    }

    protected void writeImpl(short[] arrs, int n, int n2) {
        if (this.sourceDataLine == null) {
            try {
                this.start();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
        byte[] arrby = this.lIIIIlIIllIIlIIlIIIlIIllI(arrs, n, n2);
        this.sourceDataLine.write(arrby, 0, n2 * 2);
    }

    protected byte[] lIIIIlIIllIIlIIlIIIlIIllI(int n) {
        if (this.byteArray.length < n) {
            this.byteArray = new byte[n + 1024];
        }
        return this.byteArray;
    }

    protected byte[] lIIIIlIIllIIlIIlIIIlIIllI(short[] arrs, int n, int n2) {
        byte[] arrby = this.lIIIIlIIllIIlIIlIIIlIIllI(n2 * 2);
        int n3 = 0;
        while (n2-- > 0) {
            short s = arrs[n++];
            arrby[n3++] = (byte)s;
            arrby[n3++] = (byte)(s >>> 8);
        }
        return arrby;
    }

    protected void flushImpl() {
        if (this.sourceDataLine != null) {
            this.sourceDataLine.drain();
        }
    }

    public int getPosition() {
        int n = 0;
        if (this.sourceDataLine != null) {
            n = (int)(this.sourceDataLine.getMicrosecondPosition() / 1000L);
        }
        return n;
    }

    public void testDevice() {
        try {
            this.open(new AudioFormat(22050, 16, 1, true, false));
            short[] arrs = new short[2205];
            this.write(arrs, 0, arrs.length);
            this.flush();
            this.close();
        }
        catch (RuntimeException runtimeException) {
            try {
                throw new JavaLayerException("Device test failed: " + runtimeException);
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }

    public void setFloatControlValue(float f) {
        if (this.sourceDataLine != null) {
            FloatControl floatControl = (FloatControl)this.sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            float f2 = floatControl.getMaximum() - floatControl.getMinimum();
            float f3 = f2 * (f / (float)100) + floatControl.getMinimum();
            if (f <= (float)58) {
                f3 = -80;
            }
            floatControl.setValue(f3);
        } else {
            System.err.println("dataline is null");
        }
    }
}
 