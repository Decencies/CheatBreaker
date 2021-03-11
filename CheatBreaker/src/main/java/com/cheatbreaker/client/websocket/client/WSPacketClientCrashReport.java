package com.cheatbreaker.client.websocket.client;

import com.cheatbreaker.client.nethandler.ByteBufWrapper;
import com.cheatbreaker.client.websocket.AssetsWebSocket;
import com.cheatbreaker.client.websocket.WSPacket;

import java.beans.ConstructorProperties;
import java.io.IOException;

public class WSPacketClientCrashReport
        extends WSPacket {
    private String crashId;
    private String version;
    private String osInfo;
    private String memoryInfo;
    private String stackTrace;

    public WSPacketClientCrashReport() {
    }

    @ConstructorProperties(value = {"crashId", "version", "osInfo", "memoryInfo", "stackTrace"})
    public WSPacketClientCrashReport(String string, String string2, String string3, String string4, String string5) {
        this.crashId = string;
        this.version = string2;
        this.osInfo = string3;
        this.memoryInfo = string4;
        this.stackTrace = string5;
    }

    @Override
    public void write(ByteBufWrapper buf) {
        buf.writeString(this.crashId);
        buf.writeString(this.version);
        buf.writeString(this.osInfo);
        buf.writeString(this.memoryInfo);
        buf.writeString(this.stackTrace);
    }

    @Override
    public void read(ByteBufWrapper buf) throws IOException {
        this.crashId = buf.readStringFromBuffer(100);
        this.version = buf.readStringFromBuffer(100);
        this.osInfo = buf.readStringFromBuffer(500);
        this.memoryInfo = buf.readStringFromBuffer(500);
        this.stackTrace = buf.readStringFromBuffer(10000);
    }

    @Override
    public void handle(AssetsWebSocket lIIlllIIlllIlIllIIlIIIIll2) {
    }
}