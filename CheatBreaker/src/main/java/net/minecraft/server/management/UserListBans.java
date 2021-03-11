package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListBans extends UserList
{


    public UserListBans(File p_i1138_1_)
    {
        super(p_i1138_1_);
    }

    protected UserListEntry func_152682_a(JsonObject p_152682_1_)
    {
        return new UserListBansEntry(p_152682_1_);
    }

    public boolean func_152702_a(GameProfile p_152702_1_)
    {
        return this.func_152692_d(p_152702_1_);
    }

    public String[] func_152685_a()
    {
        String[] var1 = new String[this.func_152688_e().size()];
        int var2 = 0;
        UserListBansEntry var4;

        for (Iterator var3 = this.func_152688_e().values().iterator(); var3.hasNext(); var1[var2++] = ((GameProfile)var4.func_152640_f()).getName())
        {
            var4 = (UserListBansEntry)var3.next();
        }

        return var1;
    }

    protected String func_152701_b(GameProfile p_152701_1_)
    {
        return p_152701_1_.getId().toString();
    }

    public GameProfile func_152703_a(String p_152703_1_)
    {
        Iterator var2 = this.func_152688_e().values().iterator();
        UserListBansEntry var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (UserListBansEntry)var2.next();
        }
        while (!p_152703_1_.equalsIgnoreCase(((GameProfile)var3.func_152640_f()).getName()));

        return (GameProfile)var3.func_152640_f();
    }

    protected String func_152681_a(Object p_152681_1_)
    {
        return this.func_152701_b((GameProfile)p_152681_1_);
    }
}
