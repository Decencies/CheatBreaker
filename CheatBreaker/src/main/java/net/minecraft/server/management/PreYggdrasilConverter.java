package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreYggdrasilConverter
{
    private static final Logger field_152732_e = LogManager.getLogger();
    public static final File field_152728_a = new File("banned-ips.txt");
    public static final File field_152729_b = new File("banned-players.txt");
    public static final File field_152730_c = new File("ops.txt");
    public static final File field_152731_d = new File("white-list.txt");


    private static void func_152717_a(MinecraftServer p_152717_0_, Collection p_152717_1_, ProfileLookupCallback p_152717_2_)
    {
        String[] var3 = (String[])Iterators.toArray(Iterators.filter(p_152717_1_.iterator(), new Predicate()
        {

            public boolean func_152733_a(String p_152733_1_)
            {
                return !StringUtils.isNullOrEmpty(p_152733_1_);
            }
            public boolean apply(Object p_apply_1_)
            {
                return this.func_152733_a((String)p_apply_1_);
            }
        }), String.class);

        if (p_152717_0_.isServerInOnlineMode())
        {
            p_152717_0_.func_152359_aw().findProfilesByNames(var3, Agent.MINECRAFT, p_152717_2_);
        }
        else
        {
            String[] var4 = var3;
            int var5 = var3.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                String var7 = var4[var6];
                UUID var8 = EntityPlayer.func_146094_a(new GameProfile((UUID)null, var7));
                GameProfile var9 = new GameProfile(var8, var7);
                p_152717_2_.onProfileLookupSucceeded(var9);
            }
        }
    }

    public static String func_152719_a(String p_152719_0_)
    {
        if (!StringUtils.isNullOrEmpty(p_152719_0_) && p_152719_0_.length() <= 16)
        {
            final MinecraftServer var1 = MinecraftServer.getServer();
            GameProfile var2 = var1.func_152358_ax().func_152655_a(p_152719_0_);

            if (var2 != null && var2.getId() != null)
            {
                return var2.getId().toString();
            }
            else if (!var1.isSinglePlayer() && var1.isServerInOnlineMode())
            {
                final ArrayList var3 = Lists.newArrayList();
                ProfileLookupCallback var4 = new ProfileLookupCallback()
                {

                    public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_)
                    {
                        var1.func_152358_ax().func_152649_a(p_onProfileLookupSucceeded_1_);
                        var3.add(p_onProfileLookupSucceeded_1_);
                    }
                    public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_)
                    {
                        PreYggdrasilConverter.field_152732_e.warn("Could not lookup user whitelist entry for " + p_onProfileLookupFailed_1_.getName(), p_onProfileLookupFailed_2_);
                    }
                };
                func_152717_a(var1, Lists.newArrayList(new String[] {p_152719_0_}), var4);
                return var3.size() > 0 && ((GameProfile)var3.get(0)).getId() != null ? ((GameProfile)var3.get(0)).getId().toString() : "";
            }
            else
            {
                return EntityPlayer.func_146094_a(new GameProfile((UUID)null, p_152719_0_)).toString();
            }
        }
        else
        {
            return p_152719_0_;
        }
    }
}
