package com.cheatbreaker.client.websocket.server;

import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.WSPacket;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class WSPacketBulkFriends
        extends WSPacket {
    private String rawString;
    private JsonArray bulkArray;

    public WSPacketBulkFriends() {
    }

    public WSPacketBulkFriends(String string) {
        this.rawString = string;
    }

    @Override
    public void write(ByteBufWrapper buf) {
        buf.writeString(this.rawString);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.rawString = buf.readStringFromBuffer(32767);
        JsonObject jsonObject = new JsonParser().parse(this.rawString).getAsJsonObject();
        this.bulkArray = jsonObject.getAsJsonArray("bulk");
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
        lIIlllIIlllIlIllIIlIIIIll2.handleBulkFriends(this);
    }

    public JsonArray getBulkArray() {
        return this.bulkArray;
    }
}
