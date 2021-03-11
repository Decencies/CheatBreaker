package net.minecraft.command;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;

public class CommandKill extends CommandBase
{


    public String getCommandName()
    {
        return "kill";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getCommandUsage(ICommandSender p_71518_1_)
    {
        return "commands.kill.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        EntityPlayerMP var3 = getCommandSenderAsPlayer(p_71515_1_);
        var3.attackEntityFrom(DamageSource.outOfWorld, Float.MAX_VALUE);
        p_71515_1_.addChatMessage(new ChatComponentTranslation("commands.kill.success", new Object[0]));
    }
}
