package net.minecraft.command;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

public class CommandDefaultGameMode extends CommandGameMode
{


    public String getCommandName()
    {
        return "defaultgamemode";
    }

    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "commands.defaultgamemode.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length > 0)
        {
            WorldSettings.GameType var3 = this.getGameModeFromCommand(p_71515_1_, p_71515_2_[0]);
            this.setGameType(var3);
            func_152373_a(p_71515_1_, this, "commands.defaultgamemode.success", new Object[] {new ChatComponentTranslation("gameMode." + var3.getName(), new Object[0])});
        }
        else
        {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
        }
    }

    protected void setGameType(WorldSettings.GameType p_71541_1_)
    {
        MinecraftServer var2 = MinecraftServer.getServer();
        var2.setGameType(p_71541_1_);
        EntityPlayerMP var4;

        if (var2.getForceGamemode())
        {
            for (Iterator var3 = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator(); var3.hasNext(); var4.fallDistance = 0.0F)
            {
                var4 = (EntityPlayerMP)var3.next();
                var4.setGameType(p_71541_1_);
            }
        }
    }
}
