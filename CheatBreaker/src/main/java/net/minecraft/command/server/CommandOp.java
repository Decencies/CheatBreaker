package net.minecraft.command.server;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CommandOp extends CommandBase
{


    public String getCommandName()
    {
        return "op";
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
        return "commands.op.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length == 1 && p_71515_2_[0].length() > 0)
        {
            MinecraftServer var3 = MinecraftServer.getServer();
            GameProfile var4 = var3.func_152358_ax().func_152655_a(p_71515_2_[0]);

            if (var4 == null)
            {
                throw new CommandException("commands.op.failed", new Object[] {p_71515_2_[0]});
            }
            else
            {
                var3.getConfigurationManager().func_152605_a(var4);
                func_152373_a(p_71515_1_, this, "commands.op.success", new Object[] {p_71515_2_[0]});
            }
        }
        else
        {
            throw new WrongUsageException("commands.op.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        if (p_71516_2_.length == 1)
        {
            String var3 = p_71516_2_[p_71516_2_.length - 1];
            ArrayList var4 = new ArrayList();
            GameProfile[] var5 = MinecraftServer.getServer().func_152357_F();
            int var6 = var5.length;

            for (int var7 = 0; var7 < var6; ++var7)
            {
                GameProfile var8 = var5[var7];

                if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(var8) && doesStringStartWith(var3, var8.getName()))
                {
                    var4.add(var8.getName());
                }
            }

            return var4;
        }
        else
        {
            return null;
        }
    }
}
