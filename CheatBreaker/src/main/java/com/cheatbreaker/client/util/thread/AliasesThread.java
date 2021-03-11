package com.cheatbreaker.client.util.thread;

import com.cheatbreaker.client.ui.overlay.element.AliasesElement;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.util.EnumChatFormatting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

public class AliasesThread extends Thread {
    private final AliasesElement parent;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public AliasesThread(AliasesElement parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        try {
            URL uRL = new URL("https://api.mojang.com/user/profiles/" + this.parent.getFriend().getPlayerId().replaceAll("-", "") + "/names");
            URLConnection uRLConnection = uRL.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnection.getInputStream()));
            String string = bufferedReader.readLine();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse("{\"Names\": " + string + "}");
            for (Map.Entry entry : jsonElement.getAsJsonObject().entrySet()) {
                if (!((String)entry.getKey()).equalsIgnoreCase("Names")) continue;
                Iterator iterator = ((JsonElement)entry.getValue()).getAsJsonArray().iterator();
                while (iterator.hasNext()) {
                    JsonElement jsonElement2 = (JsonElement)iterator.next();
                    String string2 = jsonElement2.getAsJsonObject().get("name").getAsString();
                    if (jsonElement2.getAsJsonObject().has("changedToAt")) {
                        long l = jsonElement2.getAsJsonObject().get("changedToAt").getAsLong();
                        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault());
                        this.parent.getAliases().add(EnumChatFormatting.GRAY + localDateTime.format(this.format) + EnumChatFormatting.RESET + " " + string2);
                        continue;
                    }
                    this.parent.getAliases().add(string2);
                }
            }
            Collections.reverse(this.parent.getAliases());
            this.parent.setElementSize(
                    this.parent.getX(),
                    this.parent.getY(),
                    this.parent.getWidth(),
                    this.parent.getHeight() + (float)(this.parent.getAliases().size() * 10) - (float)10);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}