package com.cheatbreaker.client.ui.overlay.friend;

public class FriendRequest {
    private final String username;
    private final String playerId;
    private boolean friend;

    public FriendRequest(String username, String playerId) {
        this.username = username;
        this.playerId = playerId;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public boolean isFriend() {
        return this.friend;
    }

    public void setFriend(boolean bl) {
        this.friend = bl;
    }
}