package com.cheatbreaker.client.util.voicechat;

import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VoiceChannel {
    private final UUID uuid;
    private final String string;
    private final List<VoiceUser> voiceUsers = new ArrayList<>();
    private final List<UUID> listeningList = new ArrayList<>();

    public VoiceChannel(UUID uuid, String string) {
        this.uuid = uuid;
        this.string = string;
    }

    public VoiceUser getOrCreateVoiceUser(UUID uuid, String string) {
        VoiceUser voiceUser = null;
        if (!this.isInChannel(uuid)) {
            System.out.println("[CB Voice] Created the user client side (" + uuid.toString() + ", " + string + ").");
            voiceUser = new VoiceUser(uuid, EnumChatFormatting.getTextWithoutFormattingCodes(string));
            this.listeningList.add(voiceUser.getUUID());
            this.voiceUsers.add(voiceUser);
        }
        return voiceUser;
    }

    public boolean lIIIIlIIllIIlIIlIIIlIIllI(UUID uUID) {
        return this.voiceUsers.removeIf(voiceUser -> voiceUser.getUUID().equals(uUID));
    }

    public void addToListening(UUID uuid, String string) {
        if (this.isInChannel(uuid)) {
            this.listeningList.add(uuid);
        }
    }

    public boolean removeListener(UUID uUID) {
        return this.listeningList.removeIf(uUID2 -> uUID2.equals(uUID));
    }

    public boolean isListening(UUID uUID) {
        return this.listeningList.stream().anyMatch(uUID2 -> uUID2.equals(uUID));
    }

    public boolean isInChannel(UUID uUID) {
        return this.voiceUsers.stream().anyMatch(voiceUser -> voiceUser.getUUID().equals(uUID));
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String lIIIIIIIIIlIllIIllIlIIlIl() {
        return this.string;
    }

    public List<VoiceUser> getUsers() {
        return this.voiceUsers;
    }
}