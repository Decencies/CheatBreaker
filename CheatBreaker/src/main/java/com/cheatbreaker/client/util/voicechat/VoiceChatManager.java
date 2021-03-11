package com.cheatbreaker.client.util.voicechat;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.audio.*;
import com.cheatbreaker.client.nethandler.server.PacketVoice;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.entity.player.EntityPlayer;
import paulscode.sound.SoundSystem;

import javax.vecmath.Vector3f;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class VoiceChatManager {

    public final Map<UUID, ClientStream> talking;
    private Map<UUID, PlayerProxy> playerData;
    private List<ClientStream> currentStreams;
    private final MicrophoneRecroder recorder;
    private final Minecraft mc;
    private final ThreadSoundQueue threadQueue;
    public ConcurrentLinkedQueue<Datalet> queue;
    private final Thread threadUpdate;
    private final SoundDecoder decoder;
    private boolean volumeControlActive;

    private float WEATHER;
    private float RECORDS;
    private float BLOCKS;
    private float MOBS;
    private float ANIMALS;

    public VoiceChatManager(AudioDevice microphone) {
        this.talking = new HashMap<>();
        this.playerData = new HashMap<>();
        this.currentStreams = new ArrayList<>();
        this.recorder = new MicrophoneRecroder(microphone);
        this.mc = Minecraft.getMinecraft();
        this.queue = new ConcurrentLinkedQueue<>();
        new Thread(
                this.threadQueue = new ThreadSoundQueue(this),
                "Client Stream Queue"
        ).start();
        this.decoder = new SoundDecoder();
        this.threadUpdate = new Thread(new ThreadUpdateStream(this));
        this.threadUpdate.start();
    }

    public void setTalking(boolean talking) {
        recorder.set(talking);
    }

    public void addQueue(final byte[] decoded_data, UUID uniqueId) {
        this.queue.offer(new Datalet(uniqueId, decoded_data));
        synchronized (this.threadQueue) {
            this.threadQueue.notify();
        }
    }

    public void alertEnd(final UUID uniqueId) {
        this.queue.offer(new Datalet(uniqueId, null));
        synchronized (this.threadQueue) {
            this.threadQueue.notify();
        }
    }

    public void handleIncoming(PacketVoice packetVoice) {
        if (currentStreams.isEmpty()) {
            volumeControlStop();
        }
        else if (isVolumeControlActive()) {
            volumeControlStart();
        }
        decoder.process(packetVoice.getUuid(), packetVoice.getData(), 62);
    }

    public boolean newDatalet(Datalet data) {
        return !talking.containsKey(data.id);
    }

    public void createStream(Datalet data) {
        final SoundSystem sndSystem = mc.getSoundHandler().field_147694_f.field_148620_e;
        final String identifier = generateSource(data.id);
        final PlayerProxy player = this.getPlayerData(data.id);
        if (data.direct) {
            final Vector3f position = player.position();
            sndSystem.rawDataStream(
                    CheatBreaker.universalAudioFormat,
                    true,
                    identifier,
                    position.x,
                    position.y,
                    position.z,
                    2,
                    63f // modify later
            );
        }
        else {
            sndSystem.rawDataStream(
                    CheatBreaker.universalAudioFormat,
                    true,
                    identifier,
                    (float)this.mc.thePlayer.posX,
                    (float)this.mc.thePlayer.posY,
                    (float)this.mc.thePlayer.posZ,
                    2,
                    (float)63f // modify later
            );
        }
        sndSystem.setPitch(identifier, 1.0f);
        if (data.volume != -1) {
            sndSystem.setVolume(identifier, data.volume);
        }
        else {
            sndSystem.setVolume(identifier, (Integer) CheatBreaker.getInstance().globalSettings.speakerVolume.getValue()/100f);
        }
        addStreamSafe(new ClientStream(player, data.id, data.direct));
        giveStream(data);
    }

    private void addStreamSafe(ClientStream stream) {
        this.talking.put(stream.id, stream);
        synchronized (this.threadUpdate) {
            this.threadUpdate.notify();
        }
        if (!this.containsStream(stream.id)) {
            this.currentStreams.add(stream);
        }
    }

    public boolean containsStream(final UUID id) {
        final ClientStream currentStream = this.talking.get(id);
        for (final ClientStream stream : this.currentStreams) {
            if (currentStream != null && currentStream.proxy != null) {
                final String currentName = currentStream.proxy.entityName();
                final String otherName = stream.proxy.entityName();
                if (stream.proxy.entityName() != null && currentStream.proxy.entityName() != null && currentName.equals(otherName)) {
                    return true;
                }
            } else {
                System.err.println("stream is null.");
                return false;
            }
            if (stream.id == id) {
                return true;
            }
        }
        return false;
    }

    private PlayerProxy getPlayerData(final UUID entityId) {
        PlayerProxy proxy = this.playerData.get(entityId);
        EntityPlayer entity = null;
        for (EntityPlayer playerEntity : this.mc.theWorld.playerEntities) {
            if (playerEntity.getUniqueID() == entityId) entity = playerEntity;
        }
        if (proxy == null) {
            if (entity != null) {
                proxy = new PlayerProxy(entity, entityId, entity.getCommandSenderName(), entity.posX, entity.posY, entity.posZ);
            }
            else {
                System.err.println("Major error, no entity found for player.");
                proxy = new PlayerProxy(null, entityId, "" + entityId, 0.0, 0.0, 0.0);
            }
            this.playerData.put(entityId, proxy);
        }
        else if (entity != null) {
            proxy.setPlayer(entity);
            proxy.setName(entity.getCommandSenderName());
        }
        return proxy;
    }

    public void giveStream(Datalet data) {
        final SoundSystem sndSystem = mc.getSoundHandler().field_147694_f.field_148620_e;
        ClientStream voiceChatData = talking.get(data.id);
        String identifier = generateSource(data.id);
        if (voiceChatData != null) {
            voiceChatData.update(data, (int)(System.currentTimeMillis() - voiceChatData.lastUpdated));
            voiceChatData.buffer.push(data.data);
            voiceChatData.buffer.updateJitter(voiceChatData.getJitterRate());
            if (voiceChatData.buffer.isReady() || voiceChatData.needsEnd) {
                sndSystem.flush(identifier);
                sndSystem.feedRawAudioData(identifier, voiceChatData.buffer.get());
                voiceChatData.buffer.clearBuffer(voiceChatData.getJitterRate());
            }
            voiceChatData.lastUpdated = System.currentTimeMillis();
        }
    }

    public String generateSource(UUID id) {
        return "" + id.hashCode();
    }

    public void killStream(ClientStream stream) {
        if (stream != null) {
            this.currentStreams.remove(stream);
            this.talking.remove(stream.id);
        }
    }


    public void giveEnd(UUID id) {
        final ClientStream stream = this.talking.get(id);
        if (stream != null) {
            stream.needsEnd = true;
        }
        alertEnd(id);
    }


    public void volumeControlStart() {
        if (!this.volumeControlActive) {
            final float attenuation = (Integer) CheatBreaker.getInstance().getGlobalSettings().attenuation.getValue()/100f;
            this.WEATHER = this.mc.gameSettings.getSoundLevel(SoundCategory.WEATHER);
            this.RECORDS = this.mc.gameSettings.getSoundLevel(SoundCategory.RECORDS);
            this.BLOCKS = this.mc.gameSettings.getSoundLevel(SoundCategory.BLOCKS);
            this.MOBS = this.mc.gameSettings.getSoundLevel(SoundCategory.MOBS);
            this.ANIMALS = this.mc.gameSettings.getSoundLevel(SoundCategory.PLAYERS);
            if (this.mc.gameSettings.getSoundLevel(SoundCategory.WEATHER) > 1.0f - attenuation) {
                this.mc.gameSettings.setSoundLevel(SoundCategory.WEATHER, 1.0f - attenuation);
            }
            if (this.mc.gameSettings.getSoundLevel(SoundCategory.RECORDS) > 1.0f - attenuation) {
                this.mc.gameSettings.setSoundLevel(SoundCategory.RECORDS, 1.0f - attenuation);
            }
            if (this.mc.gameSettings.getSoundLevel(SoundCategory.BLOCKS) > 1.0f - attenuation) {
                this.mc.gameSettings.setSoundLevel(SoundCategory.BLOCKS, 1.0f - attenuation);
            }
            if (this.mc.gameSettings.getSoundLevel(SoundCategory.MOBS) > 1.0f - attenuation) {
                this.mc.gameSettings.setSoundLevel(SoundCategory.MOBS, 1.0f - attenuation);
            }
            if (this.mc.gameSettings.getSoundLevel(SoundCategory.ANIMALS) > 1.0f - attenuation) {
                this.mc.gameSettings.setSoundLevel(SoundCategory.ANIMALS, 1.0f - attenuation);
            }
            this.volumeControlActive = true;
        }
    }

    public void volumeControlStop() {
        if (this.volumeControlActive) {
            this.mc.gameSettings.setSoundLevel(SoundCategory.WEATHER, this.WEATHER);
            this.mc.gameSettings.setSoundLevel(SoundCategory.RECORDS, this.RECORDS);
            this.mc.gameSettings.setSoundLevel(SoundCategory.BLOCKS, this.BLOCKS);
            this.mc.gameSettings.setSoundLevel(SoundCategory.MOBS, this.MOBS);
            this.mc.gameSettings.setSoundLevel(SoundCategory.ANIMALS, this.ANIMALS);
            this.volumeControlActive = false;
        }
    }

    public void handlePosition(UUID uniqueID, double var3, double var5, double var7) {
        if (containsStream(uniqueID)) {
            getPlayerData(uniqueID).setPosition(var3, var5, var7);
        }
    }

}
