package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandTime extends CommandBase
{


    public String getCommandName()
    {
        return "time";
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
        return "commands.time.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length > 1)
        {
            int var3;

            if (p_71515_2_[0].equals("set"))
            {
                if (p_71515_2_[1].equals("day"))
                {
                    var3 = 1000;
                }
                else if (p_71515_2_[1].equals("night"))
                {
                    var3 = 13000;
                }
                else
                {
                    var3 = parseIntWithMin(p_71515_1_, p_71515_2_[1], 0);
                }

                this.setTime(p_71515_1_, var3);
                func_152373_a(p_71515_1_, this, "commands.time.set", new Object[] {Integer.valueOf(var3)});
                return;
            }

            if (p_71515_2_[0].equals("add"))
            {
                var3 = parseIntWithMin(p_71515_1_, p_71515_2_[1], 0);
                this.addTime(p_71515_1_, var3);
                func_152373_a(p_71515_1_, this, "commands.time.added", new Object[] {Integer.valueOf(var3)});
                return;
            }
        }

        throw new WrongUsageException("commands.time.usage", new Object[0]);
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, new String[] {"set", "add"}): (p_71516_2_.length == 2 && p_71516_2_[0].equals("set") ? getListOfStringsMatchingLastWord(p_71516_2_, new String[] {"day", "night"}): null);
    }

    /**
     * Set the time in the server object.
     */
    protected void setTime(ICommandSender p_71552_1_, int p_71552_2_)
    {
        for (int var3 = 0; var3 < MinecraftServer.getServer().worldServers.length; ++var3)
        {
            MinecraftServer.getServer().worldServers[var3].setWorldTime((long)p_71552_2_);
        }
    }

    /**
     * Adds (or removes) time in the server object.
     */
    protected void addTime(ICommandSender p_71553_1_, int p_71553_2_)
    {
        for (int var3 = 0; var3 < MinecraftServer.getServer().worldServers.length; ++var3)
        {
            WorldServer var4 = MinecraftServer.getServer().worldServers[var3];
            var4.setWorldTime(var4.getWorldTime() + (long)p_71553_2_);
        }
    }
}
