package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChunkCoordinates;

public class CommandSetDefaultSpawnpoint extends CommandBase
{
    

    public String getCommandName()
    {
        return "setworldspawn";
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
        return "commands.setworldspawn.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length == 3)
        {
            if (p_71515_1_.getEntityWorld() == null)
            {
                throw new WrongUsageException("commands.setworldspawn.usage", new Object[0]);
            }

            byte var3 = 0;
            int var7 = var3 + 1;
            int var4 = parseIntBounded(p_71515_1_, p_71515_2_[var3], -30000000, 30000000);
            int var5 = parseIntBounded(p_71515_1_, p_71515_2_[var7++], 0, 256);
            int var6 = parseIntBounded(p_71515_1_, p_71515_2_[var7++], -30000000, 30000000);
            p_71515_1_.getEntityWorld().setSpawnLocation(var4, var5, var6);
            func_152373_a(p_71515_1_, this, "commands.setworldspawn.success", new Object[] {Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var6)});
        }
        else
        {
            if (p_71515_2_.length != 0)
            {
                throw new WrongUsageException("commands.setworldspawn.usage", new Object[0]);
            }

            ChunkCoordinates var8 = getCommandSenderAsPlayer(p_71515_1_).getPlayerCoordinates();
            p_71515_1_.getEntityWorld().setSpawnLocation(var8.posX, var8.posY, var8.posZ);
            func_152373_a(p_71515_1_, this, "commands.setworldspawn.success", new Object[] {Integer.valueOf(var8.posX), Integer.valueOf(var8.posY), Integer.valueOf(var8.posZ)});
        }
    }
}
