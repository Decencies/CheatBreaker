package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout extends CommandBase
{


    public String getCommandName()
    {
        return "setidletimeout";
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
        return "commands.setidletimeout.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length == 1)
        {
            int var3 = parseIntWithMin(p_71515_1_, p_71515_2_[0], 0);
            MinecraftServer.getServer().func_143006_e(var3);
            func_152373_a(p_71515_1_, this, "commands.setidletimeout.success", new Object[] {Integer.valueOf(var3)});
        }
        else
        {
            throw new WrongUsageException("commands.setidletimeout.usage", new Object[0]);
        }
    }
}
