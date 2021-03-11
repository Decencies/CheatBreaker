package com.cheatbreaker.client.audio;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.util.voicechat.VoiceChatManager;
import net.minecraft.client.Minecraft;
import paulscode.sound.SoundSystem;

import javax.vecmath.Vector3f;
import java.util.Map;
import java.util.UUID;

public class ThreadUpdateStream implements Runnable
{
    private static final int ARBITRARY_TIMEOUT = 325;
    private final Minecraft mc;
    private final VoiceChatManager manager;

    public ThreadUpdateStream(final VoiceChatManager manager) {
        this.manager = manager;
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void run() {
        while (true) {
            if (!manager.talking.isEmpty()) {
                for (Map.Entry<UUID, ClientStream> entry : manager.talking.entrySet()) {
                    ClientStream stream = entry.getValue();
                    String source = "" + entry.getKey().hashCode();
                    final SoundSystem sndSystem = this.mc.getSoundHandler().field_147694_f.field_148620_e;
                    if ((stream.needsEnd || stream.getLastTimeUpdatedMS() > ARBITRARY_TIMEOUT) && !sndSystem.playing(source)) {
                        this.manager.killStream(stream);
                    }
                    if (stream.dirty) {
                        if (stream.volume >= 0) {
                            sndSystem.setVolume(source, (Integer) CheatBreaker.getInstance().globalSettings.speakerVolume.getValue()/100f * stream.volume * 0.01f);
                        }
                        else {
                            sndSystem.setVolume(source, (Integer) CheatBreaker.getInstance().globalSettings.speakerVolume.getValue()/100f);
                        }
                        sndSystem.setAttenuation(source, 2);
                        sndSystem.setDistOrRoll(source, (float)63);
                        stream.dirty = false;
                    }
                    final Vector3f vector = stream.proxy.position();

                    sndSystem.setPosition(source, vector.x, vector.y, vector.z);
                    if (stream.volume >= 0) {
                        sndSystem.setVolume(source, (Integer) CheatBreaker.getInstance().globalSettings.speakerVolume.getValue()/100f * stream.volume * 0.01f);
                    }
                    mc.func_152344_a(() -> stream.proxy.update(ThreadUpdateStream.this.mc.theWorld));
                }
                try {
                    synchronized (this) {
                        this.wait(34L);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    synchronized (this) {
                        this.wait(2L);
                    }
                }
                catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
