package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListBansEntry;

public class CommandBanPlayer extends CommandBase
{


    public String getCommandName()
    {
        return "ban";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "commands.ban.usage";
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
    {
        return MinecraftServer.getServer().getConfigurationManager().func_152608_h().func_152689_b() && super.canCommandSenderUseCommand(p_71519_1_);
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length >= 1 && p_71515_2_[0].length() > 0)
        {
            MinecraftServer var3 = MinecraftServer.getServer();
            GameProfile var4 = var3.func_152358_ax().func_152655_a(p_71515_2_[0]);

            if (var4 == null)
            {
                throw new CommandException("commands.ban.failed", new Object[] {p_71515_2_[0]});
            }
            else
            {
                String var5 = null;

                if (p_71515_2_.length >= 2)
                {
                    var5 = func_147178_a(p_71515_1_, p_71515_2_, 1).getUnformattedText();
                }

                UserListBansEntry var6 = new UserListBansEntry(var4, (Date)null, p_71515_1_.getCommandSenderName(), (Date)null, var5);
                var3.getConfigurationManager().func_152608_h().func_152687_a(var6);
                EntityPlayerMP var7 = var3.getConfigurationManager().func_152612_a(p_71515_2_[0]);

                if (var7 != null)
                {
                    var7.playerNetServerHandler.kickPlayerFromServer("You are banned from this server.");
                }

                func_152373_a(p_71515_1_, this, "commands.ban.success", new Object[] {p_71515_2_[0]});
            }
        }
        else
        {
            throw new WrongUsageException("commands.ban.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length >= 1 ? getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames()) : null;
    }
}
