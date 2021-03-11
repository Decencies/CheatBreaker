package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;

public class CommandSaveAll extends CommandBase
{


    public String getCommandName()
    {
        return "save-all";
    }

    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "commands.save.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        MinecraftServer var3 = MinecraftServer.getServer();
        p_71515_1_.addChatMessage(new ChatComponentTranslation("commands.save.start", new Object[0]));

        if (var3.getConfigurationManager() != null)
        {
            var3.getConfigurationManager().saveAllPlayerData();
        }

        try
        {
            int var4;
            WorldServer var5;
            boolean var6;

            for (var4 = 0; var4 < var3.worldServers.length; ++var4)
            {
                if (var3.worldServers[var4] != null)
                {
                    var5 = var3.worldServers[var4];
                    var6 = var5.levelSaving;
                    var5.levelSaving = false;
                    var5.saveAllChunks(true, (IProgressUpdate)null);
                    var5.levelSaving = var6;
                }
            }

            if (p_71515_2_.length > 0 && "flush".equals(p_71515_2_[0]))
            {
                p_71515_1_.addChatMessage(new ChatComponentTranslation("commands.save.flushStart", new Object[0]));

                for (var4 = 0; var4 < var3.worldServers.length; ++var4)
                {
                    if (var3.worldServers[var4] != null)
                    {
                        var5 = var3.worldServers[var4];
                        var6 = var5.levelSaving;
                        var5.levelSaving = false;
                        var5.saveChunkData();
                        var5.levelSaving = var6;
                    }
                }

                p_71515_1_.addChatMessage(new ChatComponentTranslation("commands.save.flushEnd", new Object[0]));
            }
        }
        catch (MinecraftException var7)
        {
            func_152373_a(p_71515_1_, this, "commands.save.failed", new Object[] {var7.getMessage()});
            return;
        }

        func_152373_a(p_71515_1_, this, "commands.save.success", new Object[0]);
    }
}
