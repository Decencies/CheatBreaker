package net.minecraft.command;

import java.util.List;
import java.util.Random;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;

public class CommandWeather extends CommandBase
{


    public String getCommandName()
    {
        return "weather";
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
        return "commands.weather.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length >= 1 && p_71515_2_.length <= 2)
        {
            int var3 = (300 + (new Random()).nextInt(600)) * 20;

            if (p_71515_2_.length >= 2)
            {
                var3 = parseIntBounded(p_71515_1_, p_71515_2_[1], 1, 1000000) * 20;
            }

            WorldServer var4 = MinecraftServer.getServer().worldServers[0];
            WorldInfo var5 = var4.getWorldInfo();

            if ("clear".equalsIgnoreCase(p_71515_2_[0]))
            {
                var5.setRainTime(0);
                var5.setThunderTime(0);
                var5.setRaining(false);
                var5.setThundering(false);
                func_152373_a(p_71515_1_, this, "commands.weather.clear", new Object[0]);
            }
            else if ("rain".equalsIgnoreCase(p_71515_2_[0]))
            {
                var5.setRainTime(var3);
                var5.setRaining(true);
                var5.setThundering(false);
                func_152373_a(p_71515_1_, this, "commands.weather.rain", new Object[0]);
            }
            else
            {
                if (!"thunder".equalsIgnoreCase(p_71515_2_[0]))
                {
                    throw new WrongUsageException("commands.weather.usage", new Object[0]);
                }

                var5.setRainTime(var3);
                var5.setThunderTime(var3);
                var5.setRaining(true);
                var5.setThundering(true);
                func_152373_a(p_71515_1_, this, "commands.weather.thunder", new Object[0]);
            }
        }
        else
        {
            throw new WrongUsageException("commands.weather.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, new String[] {"clear", "rain", "thunder"}): null;
    }
}
