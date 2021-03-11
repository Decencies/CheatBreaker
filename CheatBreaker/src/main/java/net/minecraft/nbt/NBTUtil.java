package net.minecraft.nbt;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.util.StringUtils;

public final class NBTUtil
{


    public static GameProfile func_152459_a(NBTTagCompound p_152459_0_)
    {
        String var1 = null;
        String var2 = null;

        if (p_152459_0_.func_150297_b("Name", 8))
        {
            var1 = p_152459_0_.getString("Name");
        }

        if (p_152459_0_.func_150297_b("Id", 8))
        {
            var2 = p_152459_0_.getString("Id");
        }

        if (StringUtils.isNullOrEmpty(var1) && StringUtils.isNullOrEmpty(var2))
        {
            return null;
        }
        else
        {
            UUID var3;

            try
            {
                var3 = UUID.fromString(var2);
            }
            catch (Throwable var12)
            {
                var3 = null;
            }

            GameProfile var4 = new GameProfile(var3, var1);

            if (p_152459_0_.func_150297_b("Properties", 10))
            {
                NBTTagCompound var5 = p_152459_0_.getCompoundTag("Properties");
                Iterator var6 = var5.func_150296_c().iterator();

                while (var6.hasNext())
                {
                    String var7 = (String)var6.next();
                    NBTTagList var8 = var5.getTagList(var7, 10);

                    for (int var9 = 0; var9 < var8.tagCount(); ++var9)
                    {
                        NBTTagCompound var10 = var8.getCompoundTagAt(var9);
                        String var11 = var10.getString("Value");

                        if (var10.func_150297_b("Signature", 8))
                        {
                            var4.getProperties().put(var7, new Property(var7, var11, var10.getString("Signature")));
                        }
                        else
                        {
                            var4.getProperties().put(var7, new Property(var7, var11));
                        }
                    }
                }
            }

            return var4;
        }
    }

    public static void func_152460_a(NBTTagCompound p_152460_0_, GameProfile p_152460_1_)
    {
        if (!StringUtils.isNullOrEmpty(p_152460_1_.getName()))
        {
            p_152460_0_.setString("Name", p_152460_1_.getName());
        }

        if (p_152460_1_.getId() != null)
        {
            p_152460_0_.setString("Id", p_152460_1_.getId().toString());
        }

        if (!p_152460_1_.getProperties().isEmpty())
        {
            NBTTagCompound var2 = new NBTTagCompound();
            Iterator var3 = p_152460_1_.getProperties().keySet().iterator();

            while (var3.hasNext())
            {
                String var4 = (String)var3.next();
                NBTTagList var5 = new NBTTagList();
                NBTTagCompound var8;

                for (Iterator var6 = p_152460_1_.getProperties().get(var4).iterator(); var6.hasNext(); var5.appendTag(var8))
                {
                    Property var7 = (Property)var6.next();
                    var8 = new NBTTagCompound();
                    var8.setString("Value", var7.getValue());

                    if (var7.hasSignature())
                    {
                        var8.setString("Signature", var7.getSignature());
                    }
                }

                var2.setTag(var4, var5);
            }

            p_152460_0_.setTag("Properties", var2);
        }
    }
}
