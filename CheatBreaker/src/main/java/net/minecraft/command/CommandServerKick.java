package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandServerKick extends CommandBase
{


    public String getCommandName()
    {
        return "kick";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "commands.kick.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length > 0 && p_71515_2_[0].length() > 1)
        {
            EntityPlayerMP var3 = MinecraftServer.getServer().getConfigurationManager().func_152612_a(p_71515_2_[0]);
            String var4 = "Kicked by an operator.";
            boolean var5 = false;

            if (var3 == null)
            {
                throw new PlayerNotFoundException();
            }
            else
            {
                if (p_71515_2_.length >= 2)
                {
                    var4 = func_147178_a(p_71515_1_, p_71515_2_, 1).getUnformattedText();
                    var5 = true;
                }

                var3.playerNetServerHandler.kickPlayerFromServer(var4);

                if (var5)
                {
                    func_152373_a(p_71515_1_, this, "commands.kick.success.reason", new Object[] {var3.getCommandSenderName(), var4});
                }
                else
                {
                    func_152373_a(p_71515_1_, this, "commands.kick.success", new Object[] {var3.getCommandSenderName()});
                }
            }
        }
        else
        {
            throw new WrongUsageException("commands.kick.usage", new Object[0]);
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
