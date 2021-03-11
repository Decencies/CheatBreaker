package net.minecraft.command;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandDebug extends CommandBase
{
    private static final Logger logger = LogManager.getLogger();
    private long field_147206_b;
    private int field_147207_c;


    public String getCommandName()
    {
        return "debug";
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
        return "commands.debug.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length == 1)
        {
            if (p_71515_2_[0].equals("start"))
            {
                func_152373_a(p_71515_1_, this, "commands.debug.start", new Object[0]);
                MinecraftServer.getServer().enableProfiling();
                this.field_147206_b = MinecraftServer.getSystemTimeMillis();
                this.field_147207_c = MinecraftServer.getServer().getTickCounter();
                return;
            }

            if (p_71515_2_[0].equals("stop"))
            {
                if (!MinecraftServer.getServer().theProfiler.profilingEnabled)
                {
                    throw new CommandException("commands.debug.notStarted", new Object[0]);
                }

                long var3 = MinecraftServer.getSystemTimeMillis();
                int var5 = MinecraftServer.getServer().getTickCounter();
                long var6 = var3 - this.field_147206_b;
                int var8 = var5 - this.field_147207_c;
                this.func_147205_a(var6, var8);
                MinecraftServer.getServer().theProfiler.profilingEnabled = false;
                func_152373_a(p_71515_1_, this, "commands.debug.stop", new Object[] {Float.valueOf((float)var6 / 1000.0F), Integer.valueOf(var8)});
                return;
            }
        }

        throw new WrongUsageException("commands.debug.usage", new Object[0]);
    }

    private void func_147205_a(long p_147205_1_, int p_147205_3_)
    {
        File var4 = new File(MinecraftServer.getServer().getFile("debug"), "profile-results-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + ".txt");
        var4.getParentFile().mkdirs();

        try
        {
            FileWriter var5 = new FileWriter(var4);
            var5.write(this.func_147204_b(p_147205_1_, p_147205_3_));
            var5.close();
        }
        catch (Throwable var6)
        {
            logger.error("Could not save profiler results to " + var4, var6);
        }
    }

    private String func_147204_b(long p_147204_1_, int p_147204_3_)
    {
        StringBuilder var4 = new StringBuilder();
        var4.append("---- Minecraft Profiler Results ----\n");
        var4.append("// ");
        var4.append(func_147203_d());
        var4.append("\n\n");
        var4.append("Time span: ").append(p_147204_1_).append(" ms\n");
        var4.append("Tick span: ").append(p_147204_3_).append(" ticks\n");
        var4.append("// This is approximately ").append(String.format("%.2f", new Object[] {Float.valueOf((float)p_147204_3_ / ((float)p_147204_1_ / 1000.0F))})).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        var4.append("--- BEGIN PROFILE DUMP ---\n\n");
        this.func_147202_a(0, "root", var4);
        var4.append("--- END PROFILE DUMP ---\n\n");
        return var4.toString();
    }

    private void func_147202_a(int p_147202_1_, String p_147202_2_, StringBuilder p_147202_3_)
    {
        List var4 = MinecraftServer.getServer().theProfiler.getProfilingData(p_147202_2_);

        if (var4 != null && var4.size() >= 3)
        {
            for (int var5 = 1; var5 < var4.size(); ++var5)
            {
                Profiler.Result var6 = (Profiler.Result)var4.get(var5);
                p_147202_3_.append(String.format("[%02d] ", new Object[] {Integer.valueOf(p_147202_1_)}));

                for (int var7 = 0; var7 < p_147202_1_; ++var7)
                {
                    p_147202_3_.append(" ");
                }

                p_147202_3_.append(var6.field_76331_c);
                p_147202_3_.append(" - ");
                p_147202_3_.append(String.format("%.2f", new Object[] {Double.valueOf(var6.field_76332_a)}));
                p_147202_3_.append("%/");
                p_147202_3_.append(String.format("%.2f", new Object[] {Double.valueOf(var6.field_76330_b)}));
                p_147202_3_.append("%\n");

                if (!var6.field_76331_c.equals("unspecified"))
                {
                    try
                    {
                        this.func_147202_a(p_147202_1_ + 1, p_147202_2_ + "." + var6.field_76331_c, p_147202_3_);
                    }
                    catch (Exception var8)
                    {
                        p_147202_3_.append("[[ EXCEPTION " + var8 + " ]]");
                    }
                }
            }
        }
    }

    private static String func_147203_d()
    {
        String[] var0 = new String[] {"Shiny numbers!", "Am I not running fast enough? :(", "I\'m working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it\'ll have more motivation to work faster! Poor server."};

        try
        {
            return var0[(int)(System.nanoTime() % (long)var0.length)];
        }
        catch (Throwable var2)
        {
            return "Witty comment unavailable :(";
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, new String[] {"start", "stop"}): null;
    }
}
