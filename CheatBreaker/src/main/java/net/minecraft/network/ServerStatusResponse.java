package net.minecraft.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.util.UUID;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;

public class ServerStatusResponse
{
    private IChatComponent field_151326_a;
    private ServerStatusResponse.PlayerCountData field_151324_b;
    private ServerStatusResponse.MinecraftProtocolVersionIdentifier field_151325_c;
    private String field_151323_d;
    

    public IChatComponent func_151317_a()
    {
        return this.field_151326_a;
    }

    public void func_151315_a(IChatComponent p_151315_1_)
    {
        this.field_151326_a = p_151315_1_;
    }

    public ServerStatusResponse.PlayerCountData func_151318_b()
    {
        return this.field_151324_b;
    }

    public void func_151319_a(ServerStatusResponse.PlayerCountData p_151319_1_)
    {
        this.field_151324_b = p_151319_1_;
    }

    public ServerStatusResponse.MinecraftProtocolVersionIdentifier func_151322_c()
    {
        return this.field_151325_c;
    }

    public void func_151321_a(ServerStatusResponse.MinecraftProtocolVersionIdentifier p_151321_1_)
    {
        this.field_151325_c = p_151321_1_;
    }

    public void func_151320_a(String p_151320_1_)
    {
        this.field_151323_d = p_151320_1_;
    }

    public String func_151316_d()
    {
        return this.field_151323_d;
    }

    public static class MinecraftProtocolVersionIdentifier
    {
        private final String field_151306_a;
        private final int field_151305_b;
        

        public MinecraftProtocolVersionIdentifier(String p_i45275_1_, int p_i45275_2_)
        {
            this.field_151306_a = p_i45275_1_;
            this.field_151305_b = p_i45275_2_;
        }

        public String func_151303_a()
        {
            return this.field_151306_a;
        }

        public int func_151304_b()
        {
            return this.field_151305_b;
        }

        public static class Serializer implements JsonDeserializer, JsonSerializer
        {
            

            public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                JsonObject var4 = JsonUtils.getJsonElementAsJsonObject(p_deserialize_1_, "version");
                return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(JsonUtils.getJsonObjectStringFieldValue(var4, "name"), JsonUtils.getJsonObjectIntegerFieldValue(var4, "protocol"));
            }

            public JsonElement serialize(ServerStatusResponse.MinecraftProtocolVersionIdentifier p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                JsonObject var4 = new JsonObject();
                var4.addProperty("name", p_serialize_1_.func_151303_a());
                var4.addProperty("protocol", Integer.valueOf(p_serialize_1_.func_151304_b()));
                return var4;
            }

            public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                return this.serialize((ServerStatusResponse.MinecraftProtocolVersionIdentifier)p_serialize_1_, p_serialize_2_, p_serialize_3_);
            }
        }
    }

    public static class PlayerCountData
    {
        private final int field_151336_a;
        private final int field_151334_b;
        private GameProfile[] field_151335_c;
        

        public PlayerCountData(int p_i45274_1_, int p_i45274_2_)
        {
            this.field_151336_a = p_i45274_1_;
            this.field_151334_b = p_i45274_2_;
        }

        public int func_151332_a()
        {
            return this.field_151336_a;
        }

        public int func_151333_b()
        {
            return this.field_151334_b;
        }

        public GameProfile[] func_151331_c()
        {
            return this.field_151335_c;
        }

        public void func_151330_a(GameProfile[] p_151330_1_)
        {
            this.field_151335_c = p_151330_1_;
        }

        public static class Serializer implements JsonDeserializer, JsonSerializer
        {
            

            public ServerStatusResponse.PlayerCountData deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                JsonObject var4 = JsonUtils.getJsonElementAsJsonObject(p_deserialize_1_, "players");
                ServerStatusResponse.PlayerCountData var5 = new ServerStatusResponse.PlayerCountData(JsonUtils.getJsonObjectIntegerFieldValue(var4, "max"), JsonUtils.getJsonObjectIntegerFieldValue(var4, "online"));

                if (JsonUtils.jsonObjectFieldTypeIsArray(var4, "sample"))
                {
                    JsonArray var6 = JsonUtils.getJsonObjectJsonArrayField(var4, "sample");

                    if (var6.size() > 0)
                    {
                        GameProfile[] var7 = new GameProfile[var6.size()];

                        for (int var8 = 0; var8 < var7.length; ++var8)
                        {
                            JsonObject var9 = JsonUtils.getJsonElementAsJsonObject(var6.get(var8), "player[" + var8 + "]");
                            String var10 = JsonUtils.getJsonObjectStringFieldValue(var9, "id");
                            var7[var8] = new GameProfile(UUID.fromString(var10), JsonUtils.getJsonObjectStringFieldValue(var9, "name"));
                        }

                        var5.func_151330_a(var7);
                    }
                }

                return var5;
            }

            public JsonElement serialize(ServerStatusResponse.PlayerCountData p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                JsonObject var4 = new JsonObject();
                var4.addProperty("max", Integer.valueOf(p_serialize_1_.func_151332_a()));
                var4.addProperty("online", Integer.valueOf(p_serialize_1_.func_151333_b()));

                if (p_serialize_1_.func_151331_c() != null && p_serialize_1_.func_151331_c().length > 0)
                {
                    JsonArray var5 = new JsonArray();

                    for (int var6 = 0; var6 < p_serialize_1_.func_151331_c().length; ++var6)
                    {
                        JsonObject var7 = new JsonObject();
                        UUID var8 = p_serialize_1_.func_151331_c()[var6].getId();
                        var7.addProperty("id", var8 == null ? "" : var8.toString());
                        var7.addProperty("name", p_serialize_1_.func_151331_c()[var6].getName());
                        var5.add(var7);
                    }

                    var4.add("sample", var5);
                }

                return var4;
            }

            public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                return this.serialize((ServerStatusResponse.PlayerCountData)p_serialize_1_, p_serialize_2_, p_serialize_3_);
            }
        }
    }

    public static class Serializer implements JsonDeserializer, JsonSerializer
    {
        

        public ServerStatusResponse deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            JsonObject var4 = JsonUtils.getJsonElementAsJsonObject(p_deserialize_1_, "status");
            ServerStatusResponse var5 = new ServerStatusResponse();

            if (var4.has("description"))
            {
                var5.func_151315_a((IChatComponent)p_deserialize_3_.deserialize(var4.get("description"), IChatComponent.class));
            }

            if (var4.has("players"))
            {
                var5.func_151319_a((ServerStatusResponse.PlayerCountData)p_deserialize_3_.deserialize(var4.get("players"), ServerStatusResponse.PlayerCountData.class));
            }

            if (var4.has("version"))
            {
                var5.func_151321_a((ServerStatusResponse.MinecraftProtocolVersionIdentifier)p_deserialize_3_.deserialize(var4.get("version"), ServerStatusResponse.MinecraftProtocolVersionIdentifier.class));
            }

            if (var4.has("favicon"))
            {
                var5.func_151320_a(JsonUtils.getJsonObjectStringFieldValue(var4, "favicon"));
            }

            return var5;
        }

        public JsonElement serialize(ServerStatusResponse p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            JsonObject var4 = new JsonObject();

            if (p_serialize_1_.func_151317_a() != null)
            {
                var4.add("description", p_serialize_3_.serialize(p_serialize_1_.func_151317_a()));
            }

            if (p_serialize_1_.func_151318_b() != null)
            {
                var4.add("players", p_serialize_3_.serialize(p_serialize_1_.func_151318_b()));
            }

            if (p_serialize_1_.func_151322_c() != null)
            {
                var4.add("version", p_serialize_3_.serialize(p_serialize_1_.func_151322_c()));
            }

            if (p_serialize_1_.func_151316_d() != null)
            {
                var4.addProperty("favicon", p_serialize_1_.func_151316_d());
            }

            return var4;
        }

        public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            return this.serialize((ServerStatusResponse)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }
    }
}
