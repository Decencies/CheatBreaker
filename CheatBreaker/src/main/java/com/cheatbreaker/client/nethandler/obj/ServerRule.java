package com.cheatbreaker.client.nethandler.obj;

import lombok.Getter;

@Getter
public enum ServerRule {

    VOICE_ENABLED("voiceEnabled", Boolean.class),
    MINIMAP_STATUS("minimapStatus", String.class),
    SERVER_HANDLES_WAYPOINTS("serverHandlesWaypoints", Boolean.class),
    COMPETITIVE_GAMEMODE("competitiveGame", Boolean.class);

    private final String ruleName;
    private final Class value;

    public static ServerRule getRuleName(String name) {
        ServerRule rule = null;
        for (ServerRule sr : ServerRule.values()) {
            if (!sr.getRuleName().equals(name)) continue;
            rule = sr;
        }
        return rule;
    }

    ServerRule(String rule, Class value) {
        this.ruleName = rule;
        this.value = value;
    }

}