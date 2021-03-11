package net.minecraft.realms;

import com.mojang.authlib.GameProfile;
import java.net.Proxy;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.world.WorldSettings;

public class Realms
{


    public static boolean isTouchScreen()
    {
        return Minecraft.getMinecraft().gameSettings.touchscreen;
    }

    public static Proxy getProxy()
    {
        return Minecraft.getMinecraft().getProxy();
    }

    public static String sessionId()
    {
        Session var0 = Minecraft.getMinecraft().getSession();
        return var0 == null ? null : var0.getSessionID();
    }

    public static String userName()
    {
        Session var0 = Minecraft.getMinecraft().getSession();
        return var0 == null ? null : var0.getUsername();
    }

    public static long currentTimeMillis()
    {
        return Minecraft.getSystemTime();
    }

    public static String getSessionId()
    {
        return Minecraft.getMinecraft().getSession().getSessionID();
    }

    public static String getName()
    {
        return Minecraft.getMinecraft().getSession().getUsername();
    }

    public static String uuidToName(String p_uuidToName_0_)
    {
        return Minecraft.getMinecraft().func_152347_ac().fillProfileProperties(new GameProfile(UUID.fromString(p_uuidToName_0_.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5")), (String)null), false).getName();
    }

    public static void setScreen(RealmsScreen p_setScreen_0_)
    {
        Minecraft.getMinecraft().displayGuiScreen(p_setScreen_0_.getProxy());
    }

    public static String getGameDirectoryPath()
    {
        return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
    }

    public static int survivalId()
    {
        return WorldSettings.GameType.SURVIVAL.getID();
    }

    public static int creativeId()
    {
        return WorldSettings.GameType.CREATIVE.getID();
    }

    public static int adventureId()
    {
        return WorldSettings.GameType.ADVENTURE.getID();
    }
}
