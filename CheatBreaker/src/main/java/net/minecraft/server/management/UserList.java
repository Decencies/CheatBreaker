package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserList
{
    protected static final Logger field_152693_a = LogManager.getLogger();
    protected final Gson field_152694_b;
    private final File field_152695_c;
    private final Map field_152696_d = Maps.newHashMap();
    private boolean field_152697_e = true;
    private static final ParameterizedType field_152698_f = new ParameterizedType()
    {
        
        public Type[] getActualTypeArguments()
        {
            return new Type[] {UserListEntry.class};
        }
        public Type getRawType()
        {
            return List.class;
        }
        public Type getOwnerType()
        {
            return null;
        }
    };
    

    public UserList(File p_i1144_1_)
    {
        this.field_152695_c = p_i1144_1_;
        GsonBuilder var2 = (new GsonBuilder()).setPrettyPrinting();
        var2.registerTypeHierarchyAdapter(UserListEntry.class, new UserList.Serializer(null));
        this.field_152694_b = var2.create();
    }

    public boolean func_152689_b()
    {
        return this.field_152697_e;
    }

    public void func_152686_a(boolean p_152686_1_)
    {
        this.field_152697_e = p_152686_1_;
    }

    public void func_152687_a(UserListEntry p_152687_1_)
    {
        this.field_152696_d.put(this.func_152681_a(p_152687_1_.func_152640_f()), p_152687_1_);

        try
        {
            this.func_152678_f();
        }
        catch (IOException var3)
        {
            field_152693_a.warn("Could not save the list after adding a user.", var3);
        }
    }

    public UserListEntry func_152683_b(Object p_152683_1_)
    {
        this.func_152680_h();
        return (UserListEntry)this.field_152696_d.get(this.func_152681_a(p_152683_1_));
    }

    public void func_152684_c(Object p_152684_1_)
    {
        this.field_152696_d.remove(this.func_152681_a(p_152684_1_));

        try
        {
            this.func_152678_f();
        }
        catch (IOException var3)
        {
            field_152693_a.warn("Could not save the list after removing a user.", var3);
        }
    }

    public String[] func_152685_a()
    {
        return (String[])this.field_152696_d.keySet().toArray(new String[this.field_152696_d.size()]);
    }

    protected String func_152681_a(Object p_152681_1_)
    {
        return p_152681_1_.toString();
    }

    protected boolean func_152692_d(Object p_152692_1_)
    {
        return this.field_152696_d.containsKey(this.func_152681_a(p_152692_1_));
    }

    private void func_152680_h()
    {
        ArrayList var1 = Lists.newArrayList();
        Iterator var2 = this.field_152696_d.values().iterator();

        while (var2.hasNext())
        {
            UserListEntry var3 = (UserListEntry)var2.next();

            if (var3.hasBanExpired())
            {
                var1.add(var3.func_152640_f());
            }
        }

        var2 = var1.iterator();

        while (var2.hasNext())
        {
            Object var4 = var2.next();
            this.field_152696_d.remove(var4);
        }
    }

    protected UserListEntry func_152682_a(JsonObject p_152682_1_)
    {
        return new UserListEntry((Object)null, p_152682_1_);
    }

    protected Map func_152688_e()
    {
        return this.field_152696_d;
    }

    public void func_152678_f() throws IOException
    {
        Collection var1 = this.field_152696_d.values();
        String var2 = this.field_152694_b.toJson(var1);
        BufferedWriter var3 = null;

        try
        {
            var3 = Files.newWriter(this.field_152695_c, Charsets.UTF_8);
            var3.write(var2);
        }
        finally
        {
            IOUtils.closeQuietly(var3);
        }
    }

    class Serializer implements JsonDeserializer, JsonSerializer
    {
        

        private Serializer() {}

        public JsonElement func_152751_a(UserListEntry p_152751_1_, Type p_152751_2_, JsonSerializationContext p_152751_3_)
        {
            JsonObject var4 = new JsonObject();
            p_152751_1_.func_152641_a(var4);
            return var4;
        }

        public UserListEntry func_152750_a(JsonElement p_152750_1_, Type p_152750_2_, JsonDeserializationContext p_152750_3_)
        {
            if (p_152750_1_.isJsonObject())
            {
                JsonObject var4 = p_152750_1_.getAsJsonObject();
                UserListEntry var5 = UserList.this.func_152682_a(var4);
                return var5;
            }
            else
            {
                return null;
            }
        }

        public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            return this.func_152751_a((UserListEntry)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }

        public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.func_152750_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }

        Serializer(Object p_i1141_2_)
        {
            this();
        }
    }
}
