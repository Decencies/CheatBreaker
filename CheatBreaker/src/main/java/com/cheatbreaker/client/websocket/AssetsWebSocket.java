package com.cheatbreaker.client.websocket;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Profile;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.ui.overlay.*;
import com.cheatbreaker.client.ui.overlay.element.MessagesElement;
import com.cheatbreaker.client.ui.overlay.friend.FriendRequest;
import com.cheatbreaker.client.ui.overlay.friend.FriendRequestElement;
import com.cheatbreaker.client.util.cosmetic.Cosmetic;
import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import com.cheatbreaker.client.util.friend.Friend;
import com.cheatbreaker.client.util.friend.Status;
import com.cheatbreaker.client.util.thread.WSReconnectThread;
import com.cheatbreaker.client.websocket.client.WSPacketClientJoinServerResponse;
import com.cheatbreaker.client.websocket.client.*;
import com.cheatbreaker.client.websocket.server.*;
import com.cheatbreaker.client.websocket.shared.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.CryptManager;
import net.minecraft.util.EnumChatFormatting;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigInteger;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.*;

public class AssetsWebSocket extends WebSocketClient {
    private final Minecraft minecraft = Minecraft.getMinecraft();
    private final List<String> playersCache = new ArrayList<>();

    public AssetsWebSocket(URI uRI, Map<String,String> map) {
        super(uRI, new Draft_6455(), map, 0);
    }

    public void sentToServer(WSPacket packet) {
        if (!this.isOpen()) {
            return;
        }
        ByteBufWrapper buf = new ByteBufWrapper(Unpooled.buffer());
        buf.writeVarInt(WSPacket.REGISTRY.get(packet.getClass()));
        packet.write(buf);
        this.send(buf.buf().array());
    }

    public void handleIncoming(ByteBufWrapper buf) {
        int n = buf.readVarInt();
        Class<? extends WSPacket> packetClass = WSPacket.REGISTRY.inverse().get(n);
        try {
            WSPacket packet = packetClass == null ? null : packetClass.newInstance();
            if (packet == null) {
                return;
            }
            packet.read(buf);
            packet.handle(this);
        }
        catch (Exception exception) {
            System.out.println("Error from: " + packetClass);
            exception.printStackTrace();
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("[CB] Connection established");
//        if (Objects.equals(Minecraft.getMinecraft().getSession().getUsername(), Minecraft.getMinecraft().getSession().getPlayerID())) {
//            this.close();
//        }
    }

    @Override
    public void onMessage(ByteBuffer byteBuffer) {
        this.handleIncoming(new ByteBufWrapper(Unpooled.wrappedBuffer(byteBuffer.array())));
    }

    @Override
    public void onMessage(String message) {

    }

    public void handleConsoleOutput(WSPacketConsole packetRawConsoleOutput) {
        CheatBreaker.getInstance().getConsoleLines().add(packetRawConsoleOutput.getOutput());
        System.out.println(packetRawConsoleOutput.getOutput());
    }

    public void handleFriendRemove(WSPacketClientFriendRemove packetFriendRemove) {
        String string = packetFriendRemove.getPlayerId();
        Friend friend = CheatBreaker.getInstance().getFriendsManager().getFriend(string);
        if (friend != null) {
            CheatBreaker.getInstance().getFriendsManager().getFriends().remove(string);
            OverlayGui.getInstance().handleFriend(friend, false);
        }
    }

    public void handleMessage(WSPacketMessage packetMessage) {
        String playerId = packetMessage.getPlayerId();
        String message = packetMessage.getMessage();
        Friend friend = CheatBreaker.getInstance().getFriendsManager().getFriends().get(playerId);
        if (friend != null) {
            CheatBreaker.getInstance().getFriendsManager().addUnreadMessage(friend.getPlayerId(), message);
            if (CheatBreaker.getInstance().getStatus() != Status.BUSY) {
                CheatBreaker.getInstance().sendSound("message");
                Alert.displayMessage(EnumChatFormatting.GREEN + friend.getName() + EnumChatFormatting.RESET + " says:", message);
            }
            for (AbstractElement element : OverlayGui.getInstance().getElements()) {
                if (!(element instanceof MessagesElement) || ((MessagesElement)element).getFriend() != friend) continue;
                CheatBreaker.getInstance().getFriendsManager().readMessages(friend.getPlayerId());
            }
        }
    }

    public void sendUpdateServer(String string) {
        this.sentToServer(new WSPacketServerUpdate("", string));
    }

    public void handleServerUpdate(WSPacketServerUpdate packetServerUpdate) {
        String playerId = packetServerUpdate.getPlayerId();
        String server = packetServerUpdate.getServer();
        Friend friend = CheatBreaker.getInstance().getFriendsManager().getFriends().get(playerId);
        if (friend != null) {
            friend.setServer(server);
        }
    }

    public void handleBulkFriends(WSPacketBulkFriends packetBulkFriends) {
        CheatBreaker.getInstance().getFriendsManager().getFriendRequests().clear();
        JsonArray bulkArray = packetBulkFriends.getBulkArray();
        for (JsonElement jsonElement : bulkArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String playerId = jsonObject.get("uuid").getAsString();
            String playerName = jsonObject.get("name").getAsString();
            FriendRequest friendRequest = new FriendRequest(playerName, playerId);
            CheatBreaker.getInstance().getFriendsManager().getFriendRequests().put(playerId, friendRequest);
            OverlayGui.getInstance().handleFriendRequest(friendRequest, true);
        }
    }

    public void handleFriendRequest(WSPacket packet, boolean outgoing) {
        if (outgoing) {
            WSPacketFriendStatusUpdate friendStatus = (WSPacketFriendStatusUpdate)packet;
            FriendRequest friendRequest = new FriendRequest(friendStatus.getName(), friendStatus.getPlayerId());
            CheatBreaker.getInstance().getFriendsManager().getFriendRequests().put(friendStatus.getPlayerId(), friendRequest);
            OverlayGui.getInstance().handleFriendRequest(friendRequest, true);
            friendRequest.setFriend(friendStatus.isFriend());
            Alert.displayMessage("Friend Request", "Request has been sent.");
        } else {
            WSPacketFriendRequest friendRequestIncoming = (WSPacketFriendRequest) packet;
            String string = friendRequestIncoming.getPlayerId();
            String string2 = friendRequestIncoming.getName();
            FriendRequest friendRequest = new FriendRequest(string2, string);
            CheatBreaker.getInstance().getFriendsManager().getFriendRequests().put(string, friendRequest);
            OverlayGui.getInstance().handleFriendRequest(friendRequest, true);
            if (CheatBreaker.getInstance().getStatus() != Status.BUSY) {
                CheatBreaker.getInstance().sendSound("message");
                Alert.displayMessage("Friend Request", friendRequest.getUsername() + " wants to be your friend.");
            }
        }
    }

    public void handleFriendUpdate(WSPacketFriendUpdate packetFriendUpdate) {
        String playerId = packetFriendUpdate.getPlayerId();
        String name = packetFriendUpdate.getName();
        boolean online = packetFriendUpdate.isOnline();
        Friend friend = CheatBreaker.getInstance().getFriendsManager().getFriends().get(playerId);
        if (friend == null) {
            friend = Friend.builder()
                    .online(online)
                    .name(name)
                    .playerId(playerId)
                    .online(online)
                    .onlineStatus(Status.ONLINE).build();
            CheatBreaker.getInstance().getFriendsManager().getFriends().put(playerId, friend);
            OverlayGui.getInstance().handleFriend(friend, true);
        }
        if (packetFriendUpdate.getOfflineSince() < 10L) {
            int n = (int)packetFriendUpdate.getOfflineSince();
            Status cBStatusEnum = Status.ONLINE;
            for (Status cBStatusEnum2 : Status.values()) {
                if (cBStatusEnum2.ordinal() != n) continue;
                cBStatusEnum = cBStatusEnum2;
            }
            friend.setOnlineStatus(cBStatusEnum);
        }
        friend.setOnline(online);
        friend.setName(name);
        OverlayGui.getInstance().getFriendsListElement().updateSize();
        if (!online) {
            friend.setOfflineSince(packetFriendUpdate.getOfflineSince());
        }
    }

    public void handleFriendsUpdate(WSPacketFriendsUpdate packetFriendsUpdate) {
        String name;
        String playerId;
        CheatBreaker.getInstance().getFriendsManager().getFriends().clear();
        Map<String, List<String>> onlineMap = packetFriendsUpdate.getOnlineMap();
        Map<String, List<String>> offlineMap = packetFriendsUpdate.getOfflineMap();
        CheatBreaker.getInstance().setConsoleAllowed(packetFriendsUpdate.isConsoleAllowed());
        CheatBreaker.getInstance().setAcceptingFriendRequests(packetFriendsUpdate.isAcceptingFriendRequests());
        for (Map.Entry<String, List<String>> online : onlineMap.entrySet()) {
            playerId = online.getKey();
            name = online.getValue().get(0);
            int statusOrdinal = Integer.parseInt(online.getValue().get(1));
            String server = online.getValue().get(2);
            Status cBStatusEnum = Status.ONLINE;
            for (Status cBStatusEnum2 : Status.values()) {
                if (cBStatusEnum2.ordinal() != statusOrdinal) continue;
                cBStatusEnum = cBStatusEnum2;
            }
            Friend object = Friend.builder()
                    .name(name)
                    .playerId(playerId)
                    .server(server)
                    .onlineStatus(cBStatusEnum)
                    .online(true)
                    .status("Online").build();
            CheatBreaker.getInstance().getFriendsManager().getFriends().put(playerId, object);
            OverlayGui.getInstance().handleFriend(object, true);
        }
        for (Map.Entry<String, List<String>> offline : offlineMap.entrySet()) {
            playerId = offline.getKey();
            name = offline.getValue().get(0);
            Friend friend = Friend.builder()
                    .name(name)
                    .playerId(playerId)
                    .server("")
                    .onlineStatus(Status.ONLINE)
                    .online(false)
                    .status("Online")
                    .offlineSince(Long.parseLong(offline.getValue().get(1))).build();
            CheatBreaker.getInstance().getFriendsManager().getFriends().put(playerId, friend);
            OverlayGui.getInstance().handleFriend(friend, true);
        }
    }

    public void handleCosmetics(WSPacketCosmetics packetCosmetics) {
        String string = packetCosmetics.getPlayerId();
        CheatBreaker.getInstance().getCosmetics().removeIf(ilIlIIIlllIIIlIlllIlIllIl -> ilIlIIIlllIIIlIlllIlIllIl.getPlayerId().equals(string));
        CheatBreaker.getInstance().getCosmetics().removeIf(ilIlIIIlllIIIlIlllIlIllIl -> ilIlIIIlllIIIlIlllIlIllIl.getPlayerId().equals(string));
        CheatBreaker.getInstance().removeCosmeticsFromPlayer(string);
        for (Cosmetic cosmetic : packetCosmetics.getCosmetics()) {
            try {
                if (cosmetic.getName().equals("cape")) {
                    CheatBreaker.getInstance().getCosmetics().add(cosmetic);
                } else {
                    CheatBreaker.getInstance().getCosmetics().add(cosmetic);
                }
                EntityPlayer lIllIIIIlIIlIllIIIlIlIlll2 = this.minecraft.theWorld == null ? null : this.minecraft.theWorld.getPlayerEntityByName(string);
                if (!cosmetic.isEquipped() || !(lIllIIIIlIIlIllIIIlIlIlll2 instanceof AbstractClientPlayer)) continue;
                if (cosmetic.getName().equals("cape")) {
                    ((AbstractClientPlayer)lIllIIIIlIIlIllIIIlIlIlll2).setLocationOfCape(cosmetic.getLocation());
                    //lIllIIIIlIIlIllIIIlIlIlll2.lIIIIlIIllIIlIIlIIIlIIllI(cosmetic);
                    //continue;
                }
                //lIllIIIIlIIlIllIIIlIlIlll2.lIIIIIIIIIlIllIIllIlIIlIl(cosmetic);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void onClose(int n, String string, boolean bl) {
        System.out.println("Close: " + string + " (" + n + ")");
        new WSReconnectThread().start();
        OverlayGui.getInstance().getFriendRequestsElement().getElements().clear();
        OverlayGui.getInstance().getFriendsListElement().getElements().clear();
        CheatBreaker.getInstance().getFriendsManager().getFriends().clear();
        CheatBreaker.getInstance().getFriendsManager().getFriendRequests().clear();
    }

    @Override
    public void onError(Exception exception) {
        System.out.println("Error: " + exception.getMessage());
        exception.printStackTrace();
    }

    public void handleFormattedConsoleOutput(WSPacketFormattedConsoleOutput packetFormattedConsoleOutput) {
        String string = packetFormattedConsoleOutput.getPrefix();
        String string2 = packetFormattedConsoleOutput.getContent();
        CheatBreaker.getInstance().getConsoleLines().add(EnumChatFormatting.DARK_GRAY + "[" + EnumChatFormatting.RESET + packetFormattedConsoleOutput.getPrefix() + EnumChatFormatting.DARK_GRAY + "] " + EnumChatFormatting.RESET + packetFormattedConsoleOutput.getContent());
        Alert.displayMessage(string, string2);
    }

    // checks if cracked?? if it's returning before sending a response
    public void handleJoinServer(WSPacketJoinServer packetJoinServer) {
        SecretKey secretKey = CryptManager.createNewSharedKey();
        PublicKey publicKey = packetJoinServer.getPublicKey();
        String string = new BigInteger(CryptManager.getServerIdHash("", publicKey, secretKey)).toString(16);
        try {
            this.createSessionService().joinServer(this.minecraft.getSession().func_148256_e(), this.minecraft.getSession().getToken(), string);
        }
        catch (AuthenticationUnavailableException authenticationUnavailableException) {
            Alert.displayMessage("Authentication Unavailable", authenticationUnavailableException.getMessage());
            return;
        }
        catch (InvalidCredentialsException invalidCredentialsException) {
            Alert.displayMessage("Invalid Credentials", invalidCredentialsException.getMessage());
            return;
        }
        catch (AuthenticationException authenticationException) {
            Alert.displayMessage("Authentication Error", authenticationException.getMessage());
            return;
        }
        catch (NullPointerException nullPointerException) {
            this.close();
        }
        try {
            ByteBufWrapper buf = new ByteBufWrapper(Unpooled.buffer());
            WSPacketClientJoinServerResponse response = new WSPacketClientJoinServerResponse(secretKey, publicKey, packetJoinServer.lIIIIIIIIIlIllIIllIlIIlIl());
            response.write(buf);
            this.sentToServer(response);
            File file = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "config" + File.separator + "client" + File.separator + "profiles.txt");
            if (file.exists()) {
                this.sentToServer(new WSPacketClientProfilesExist());
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private MinecraftSessionService createSessionService() {
        return new YggdrasilAuthenticationService(this.minecraft.getProxy(), UUID.randomUUID().toString()).createMinecraftSessionService();
    }

    @Override
    public void send(String string) {
        if (!this.isOpen()) {
            return;
        }
        super.send(string);
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(AbstractClientPlayer abstractClientPlayer) {
        if (abstractClientPlayer.getGameProfile() == null || this.minecraft.thePlayer == null) {
            return;
        }
        String string = abstractClientPlayer.getUniqueID().toString();
        if (!this.playersCache.contains(string) && !string.equals(this.minecraft.thePlayer.getUniqueID().toString())) {
            this.playersCache.add(string);
            this.sentToServer(new WSPacketClientPlayerJoin(string));
        }
    }

    public void sendClientCosmetics() {
        this.sentToServer(new WSPacketClientCosmetics(CheatBreaker.getInstance().getCosmetics()));
    }

    public void updateClientStatus() {
        this.sentToServer(new WSPacketFriendUpdate("", "", CheatBreaker.getInstance().getStatus().ordinal(), true));
    }

    public void handleFriendRequestUpdate(WSPacketClientFriendRequestUpdate packetFriendRequestUpdate) {
        if (!packetFriendRequestUpdate.isAdd()) {
            CheatBreaker.getInstance().getFriendsManager().getFriendRequests().remove(packetFriendRequestUpdate.lIIIIIIIIIlIllIIllIlIIlIl());
            FriendRequestElement requestElement = null;
            for (FriendRequestElement friendRequestElement : OverlayGui.getInstance().getFriendRequestsElement().getElements()) {
                if (!friendRequestElement.getFriendRequest().getPlayerId().equals(packetFriendRequestUpdate.lIIIIIIIIIlIllIIllIlIIlIl())) continue;
                requestElement = friendRequestElement;
            }
            if (requestElement != null) {
                OverlayGui.getInstance().getFriendRequestsElement().getFrientRequestElementList().add(requestElement);
                OverlayGui.getInstance().handleFriendRequest(requestElement.getFriendRequest(), false);
            }
        }
    }

    public void handleKeyRequest(WSPacketKeyRequest packetKeyRequest) {
        try {
            byte[] test = "a".getBytes(); // Message.i()
            byte[] data = AssetsWebSocket.getKeyResponse(packetKeyRequest.getPublicKey(), test);
            this.sentToServer(new WSPacketClientKeyResponse(data));
        }
        catch (Exception | UnsatisfiedLinkError throwable) {
            // empty catch block
        }
    }

    public static byte[] getKeyResponse(byte[] publicKey, byte[] privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        PublicKey key = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, key);
        return cipher.doFinal(privateKey);
    }

    // crashes the client in obnoxious ways.
    public void handleForceCrash(WSPacketForceCrash packetForceCrash) {
        Minecraft.getMinecraft().forceCrash = true;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(Profile iIlIllllIIlIlIIIlllIIllIl) {
        try {
            File file = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "config" + File.separator + "client" + File.separator + "profiles.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write("################################");
                bufferedWriter.newLine();
                bufferedWriter.write("# MC_Client: PROFILES");
                bufferedWriter.newLine();
                bufferedWriter.write("################################");
                bufferedWriter.newLine();
                for (Profile ilIIlIIlIIlllIlIIIlIllIIl : CheatBreaker.getInstance().profiles) {
                    bufferedWriter.write(ilIIlIIlIIlllIlIIIlIllIIl.getName() + ":" + ilIIlIIlIIlllIlIIIlIllIIl.getIndex());
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
            }
            catch (Exception exception) {}
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}
