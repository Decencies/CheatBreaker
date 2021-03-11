package com.cheatbreaker.client.websocket;

import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.client.*;
import com.cheatbreaker.client.websocket.server.*;
import com.cheatbreaker.client.websocket.shared.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public abstract class WSPacket {

    public static BiMap<Class<? extends WSPacket>, Integer> REGISTRY = HashBiMap.create();

    public abstract void write(ByteBufWrapper var1);

    public abstract void read(ByteBufWrapper var1) throws IOException;

    public abstract void handle(AssetsWebSocket var1);

    protected void writeKey(ByteBuf byteBuf, byte[] key) {
        byteBuf.writeShort(key.length);
        byteBuf.writeBytes(key);
    }

    protected byte[] readKey(ByteBuf byteBuf) {
        short keySize = byteBuf.readShort();
        if (keySize >= 0) {
            byte[] key = new byte[keySize];
            byteBuf.readBytes(key);
            return key;
        }
        System.out.println("[WS] Key was smaller than nothing!  Weird key!");
        return new byte[0];
    }

    static {
        REGISTRY.put(WSPacketJoinServer.class, 0);
        REGISTRY.put(WSPacketClientJoinServerResponse.class, 1);
        REGISTRY.put(WSPacketConsole.class, 2);
        REGISTRY.put(WSPacketFormattedConsoleOutput.class, 3);
        REGISTRY.put(WSPacketFriendsUpdate.class, 4);
        REGISTRY.put(WSPacketMessage.class, 5);
        REGISTRY.put(WSPacketServerUpdate.class, 6);
        REGISTRY.put(WSPacketBulkFriends.class, 7);
        REGISTRY.put(WSPacketCosmetics.class, 8);
        REGISTRY.put(WSPacketFriendRequest.class, 9);
        REGISTRY.put(WSPacketFriendStatusUpdate.class, 16);
        REGISTRY.put(WSPacketClientFriendRemove.class, 17);
        REGISTRY.put(WSPacketFriendUpdate.class, 18);
        REGISTRY.put(WSPacketClientPlayerJoin.class, 19);
        REGISTRY.put(WSPacketClientCosmetics.class, 20);
        REGISTRY.put(WSPacketClientFriendRequestUpdate.class, 21);
        REGISTRY.put(WSPacketClientRequestsStatus.class, 22);
        REGISTRY.put(WSPacketClientCrashReport.class, 23);
        REGISTRY.put(WSPacketClientSync.class, 24);
        REGISTRY.put(WSPacketClientKeyResponse.class, 25);
        REGISTRY.put(WSPacketKeyRequest.class, 32);
        REGISTRY.put(WSPacketForceCrash.class, 33);
        REGISTRY.put(WSPacketClientProfilesExist.class, 34);
        REGISTRY.put(WSPacketRequestProcessList.class, 35);
        REGISTRY.put(WSPacketClientProcessList.class, 36);
        REGISTRY.put(WSPacketClientKeySync.class, 37);
    }

}
