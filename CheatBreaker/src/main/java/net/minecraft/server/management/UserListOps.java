package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListOps extends UserList
{


    public UserListOps(File p_i1152_1_)
    {
        super(p_i1152_1_);
    }

    protected UserListEntry func_152682_a(JsonObject p_152682_1_)
    {
        return new UserListOpsEntry(p_152682_1_);
    }

    public String[] func_152685_a()
    {
        String[] var1 = new String[this.func_152688_e().size()];
        int var2 = 0;
        UserListOpsEntry var4;

        for (Iterator var3 = this.func_152688_e().values().iterator(); var3.hasNext(); var1[var2++] = ((GameProfile)var4.func_152640_f()).getName())
        {
            var4 = (UserListOpsEntry)var3.next();
        }

        return var1;
    }

    protected String func_152699_b(GameProfile p_152699_1_)
    {
        return p_152699_1_.getId().toString();
    }

    public GameProfile func_152700_a(String p_152700_1_)
    {
        Iterator var2 = this.func_152688_e().values().iterator();
        UserListOpsEntry var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (UserListOpsEntry)var2.next();
        }
        while (!p_152700_1_.equalsIgnoreCase(((GameProfile)var3.func_152640_f()).getName()));

        return (GameProfile)var3.func_152640_f();
    }

    protected String func_152681_a(Object p_152681_1_)
    {
        return this.func_152699_b((GameProfile)p_152681_1_);
    }
}
