package net.minecraft.command.server;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.IPBanEntry;
import net.minecraft.util.IChatComponent;

public class CommandBanIp extends CommandBase
{
    public static final Pattern field_147211_a = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");


    public String getCommandName()
    {
        return "ban-ip";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
    {
        return MinecraftServer.getServer().getConfigurationManager().getBannedIPs().func_152689_b() && super.canCommandSenderUseCommand(p_71519_1_);
    }

    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "commands.banip.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length >= 1 && p_71515_2_[0].length() > 1)
        {
            Matcher var3 = field_147211_a.matcher(p_71515_2_[0]);
            IChatComponent var4 = null;

            if (p_71515_2_.length >= 2)
            {
                var4 = func_147178_a(p_71515_1_, p_71515_2_, 1);
            }

            if (var3.matches())
            {
                this.func_147210_a(p_71515_1_, p_71515_2_[0], var4 == null ? null : var4.getUnformattedText());
            }
            else
            {
                EntityPlayerMP var5 = MinecraftServer.getServer().getConfigurationManager().func_152612_a(p_71515_2_[0]);

                if (var5 == null)
                {
                    throw new PlayerNotFoundException("commands.banip.invalid", new Object[0]);
                }

                this.func_147210_a(p_71515_1_, var5.getPlayerIP(), var4 == null ? null : var4.getUnformattedText());
            }
        }
        else
        {
            throw new WrongUsageException("commands.banip.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    protected void func_147210_a(ICommandSender p_147210_1_, String p_147210_2_, String p_147210_3_)
    {
        IPBanEntry var4 = new IPBanEntry(p_147210_2_, (Date)null, p_147210_1_.getCommandSenderName(), (Date)null, p_147210_3_);
        MinecraftServer.getServer().getConfigurationManager().getBannedIPs().func_152687_a(var4);
        List var5 = MinecraftServer.getServer().getConfigurationManager().getPlayerList(p_147210_2_);
        String[] var6 = new String[var5.size()];
        int var7 = 0;
        EntityPlayerMP var9;

        for (Iterator var8 = var5.iterator(); var8.hasNext(); var6[var7++] = var9.getCommandSenderName())
        {
            var9 = (EntityPlayerMP)var8.next();
            var9.playerNetServerHandler.kickPlayerFromServer("You have been IP banned.");
        }

        if (var5.isEmpty())
        {
            func_152373_a(p_147210_1_, this, "commands.banip.success", new Object[] {p_147210_2_});
        }
        else
        {
            func_152373_a(p_147210_1_, this, "commands.banip.success.players", new Object[] {p_147210_2_, joinNiceString(var6)});
        }
    }
}
