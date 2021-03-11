package net.minecraft.command.server;

import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandTeleport extends CommandBase
{


    public String getCommandName()
    {
        return "tp";
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
        return "commands.tp.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length < 1)
        {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3;

            if (p_71515_2_.length != 2 && p_71515_2_.length != 4)
            {
                var3 = getCommandSenderAsPlayer(p_71515_1_);
            }
            else
            {
                var3 = getPlayer(p_71515_1_, p_71515_2_[0]);

                if (var3 == null)
                {
                    throw new PlayerNotFoundException();
                }
            }

            if (p_71515_2_.length != 3 && p_71515_2_.length != 4)
            {
                if (p_71515_2_.length == 1 || p_71515_2_.length == 2)
                {
                    EntityPlayerMP var11 = getPlayer(p_71515_1_, p_71515_2_[p_71515_2_.length - 1]);

                    if (var11 == null)
                    {
                        throw new PlayerNotFoundException();
                    }

                    if (var11.worldObj != var3.worldObj)
                    {
                        func_152373_a(p_71515_1_, this, "commands.tp.notSameDimension", new Object[0]);
                        return;
                    }

                    var3.mountEntity((Entity)null);
                    var3.playerNetServerHandler.setPlayerLocation(var11.posX, var11.posY, var11.posZ, var11.rotationYaw, var11.rotationPitch);
                    func_152373_a(p_71515_1_, this, "commands.tp.success", new Object[] {var3.getCommandSenderName(), var11.getCommandSenderName()});
                }
            }
            else if (var3.worldObj != null)
            {
                int var4 = p_71515_2_.length - 3;
                double var5 = func_110666_a(p_71515_1_, var3.posX, p_71515_2_[var4++]);
                double var7 = func_110665_a(p_71515_1_, var3.posY, p_71515_2_[var4++], 0, 0);
                double var9 = func_110666_a(p_71515_1_, var3.posZ, p_71515_2_[var4++]);
                var3.mountEntity((Entity)null);
                var3.setPositionAndUpdate(var5, var7, var9);
                func_152373_a(p_71515_1_, this, "commands.tp.success.coordinates", new Object[] {var3.getCommandSenderName(), Double.valueOf(var5), Double.valueOf(var7), Double.valueOf(var9)});
            }
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length != 1 && p_71516_2_.length != 2 ? null : getListOfStringsMatchingLastWord(p_71516_2_, MinecraftServer.getServer().getAllUsernames());
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
    {
        return p_82358_2_ == 0;
    }
}
