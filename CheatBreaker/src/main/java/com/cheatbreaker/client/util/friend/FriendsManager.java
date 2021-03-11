package com.cheatbreaker.client.util.friend;

import com.cheatbreaker.client.ui.overlay.friend.FriendRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FriendsManager {
    private final Map<String, Friend> friendsMap = new ConcurrentHashMap<>();
    private final Map<String, FriendRequest> friendRequestsMap = new ConcurrentHashMap<>();
    private final Map<String, ArrayList<String>> messages = new ConcurrentHashMap<>();
    private final Map<String, ArrayList<String>> unreadMessages = new ConcurrentHashMap<>();
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void addUnreadMessage(String playerId, String message) {
        Friend friend = this.getFriend(playerId);
        if (friend == null) {
            return;
        }
        if (!this.unreadMessages.containsKey(playerId)) {
            this.unreadMessages.put(playerId, new ArrayList<>());
        }
        String prefix = EnumChatFormatting.GRAY + LocalDateTime.now().format(this.timeFormatter);
        String suffix = EnumChatFormatting.GREEN + friend.getName() + EnumChatFormatting.RESET;
        String concat = prefix + " " + suffix + ": " + message;
        this.unreadMessages.get(playerId).add(concat);
    }

    public void addMessage(String playerId, String message) {
        Friend friend = this.getFriend(playerId);
        if (friend == null) {
            return;
        }
        if (!this.messages.containsKey(playerId)) {
            this.messages.put(playerId, new ArrayList<>());
        }
        this.messages.get(playerId).add(message);
    }

    public void addOutgoingMessage(String playerId, String message) {
        Friend friend = this.getFriend(playerId);
        if (friend == null) {
            return;
        }
        String prefix = EnumChatFormatting.GRAY + LocalDateTime.now().format(this.timeFormatter);
        String suffix = EnumChatFormatting.AQUA + Minecraft.getMinecraft().getSession().getUsername() + EnumChatFormatting.RESET;
        String concat = prefix + " " + suffix + ": " + message;
        this.addMessage(playerId, concat);
    }

    public void readMessages(String playerId) {
        if (this.unreadMessages.containsKey(playerId)) {
            List<String> list = this.unreadMessages.get(playerId);
            if (!this.messages.containsKey(playerId)) {
                this.messages.put(playerId, new ArrayList<>());
            }
            this.messages.get(playerId).addAll(list);
            this.unreadMessages.remove(playerId);
        }
    }

    public Friend getFriend(String playerId) {
        for (Friend friend : this.friendsMap.values()) {
            if (!friend.getPlayerId().equals(playerId)) continue;
            return friend;
        }
        return null;
    }

    public Map<String, Friend> getFriends() {
        return this.friendsMap;
    }

    public Map<String, FriendRequest> getFriendRequests() {
        return this.friendRequestsMap;
    }

    public Map<String, ArrayList<String>> getMessages() {
        return this.messages;
    }

    public Map<String, ArrayList<String>> getUnreadMessages() {
        return this.unreadMessages;
    }
}