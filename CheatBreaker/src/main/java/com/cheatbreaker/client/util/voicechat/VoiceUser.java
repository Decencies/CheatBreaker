package com.cheatbreaker.client.util.voicechat;

import java.util.UUID;

public class VoiceUser {
    private UUID uuid;
    private String username;

    public VoiceUser(UUID uuid, String string) {
        this.uuid = uuid;
        this.username = string;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }
}
 