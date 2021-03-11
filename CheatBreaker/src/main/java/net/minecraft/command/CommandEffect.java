package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

public class CommandEffect extends CommandBase
{


    public String getCommandName()
    {
        return "effect";
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
        return "commands.effect.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length < 2)
        {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = getPlayer(p_71515_1_, p_71515_2_[0]);

            if (p_71515_2_[1].equals("clear"))
            {
                if (var3.getActivePotionEffects().isEmpty())
                {
                    throw new CommandException("commands.effect.failure.notActive.all", new Object[] {var3.getCommandSenderName()});
                }

                var3.clearActivePotions();
                func_152373_a(p_71515_1_, this, "commands.effect.success.removed.all", new Object[] {var3.getCommandSenderName()});
            }
            else
            {
                int var4 = parseIntWithMin(p_71515_1_, p_71515_2_[1], 1);
                int var5 = 600;
                int var6 = 30;
                int var7 = 0;

                if (var4 < 0 || var4 >= Potion.potionTypes.length || Potion.potionTypes[var4] == null)
                {
                    throw new NumberInvalidException("commands.effect.notFound", new Object[] {Integer.valueOf(var4)});
                }

                if (p_71515_2_.length >= 3)
                {
                    var6 = parseIntBounded(p_71515_1_, p_71515_2_[2], 0, 1000000);

                    if (Potion.potionTypes[var4].isInstant())
                    {
                        var5 = var6;
                    }
                    else
                    {
                        var5 = var6 * 20;
                    }
                }
                else if (Potion.potionTypes[var4].isInstant())
                {
                    var5 = 1;
                }

                if (p_71515_2_.length >= 4)
                {
                    var7 = parseIntBounded(p_71515_1_, p_71515_2_[3], 0, 255);
                }

                if (var6 == 0)
                {
                    if (!var3.isPotionActive(var4))
                    {
                        throw new CommandException("commands.effect.failure.notActive", new Object[] {new ChatComponentTranslation(Potion.potionTypes[var4].getName(), new Object[0]), var3.getCommandSenderName()});
                    }

                    var3.removePotionEffect(var4);
                    func_152373_a(p_71515_1_, this, "commands.effect.success.removed", new Object[] {new ChatComponentTranslation(Potion.potionTypes[var4].getName(), new Object[0]), var3.getCommandSenderName()});
                }
                else
                {
                    PotionEffect var8 = new PotionEffect(var4, var5, var7);
                    var3.addPotionEffect(var8);
                    func_152373_a(p_71515_1_, this, "commands.effect.success", new Object[] {new ChatComponentTranslation(var8.getEffectName(), new Object[0]), Integer.valueOf(var4), Integer.valueOf(var7), var3.getCommandSenderName(), Integer.valueOf(var6)});
                }
            }
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, this.getAllUsernames()) : null;
    }

    protected String[] getAllUsernames()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
    {
        return p_82358_2_ == 0;
    }
}
