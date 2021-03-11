package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandGive extends CommandBase
{


    public String getCommandName()
    {
        return "give";
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
        return "commands.give.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length < 2)
        {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP var3 = getPlayer(p_71515_1_, p_71515_2_[0]);
            Item var4 = getItemByText(p_71515_1_, p_71515_2_[1]);
            int var5 = 1;
            int var6 = 0;

            if (p_71515_2_.length >= 3)
            {
                var5 = parseIntBounded(p_71515_1_, p_71515_2_[2], 1, 64);
            }

            if (p_71515_2_.length >= 4)
            {
                var6 = parseInt(p_71515_1_, p_71515_2_[3]);
            }

            ItemStack var7 = new ItemStack(var4, var5, var6);

            if (p_71515_2_.length >= 5)
            {
                String var8 = func_147178_a(p_71515_1_, p_71515_2_, 4).getUnformattedText();

                try
                {
                    NBTBase var9 = JsonToNBT.func_150315_a(var8);

                    if (!(var9 instanceof NBTTagCompound))
                    {
                        func_152373_a(p_71515_1_, this, "commands.give.tagError", new Object[] {"Not a valid tag"});
                        return;
                    }

                    var7.setTagCompound((NBTTagCompound)var9);
                }
                catch (NBTException var10)
                {
                    func_152373_a(p_71515_1_, this, "commands.give.tagError", new Object[] {var10.getMessage()});
                    return;
                }
            }

            EntityItem var11 = var3.dropPlayerItemWithRandomChoice(var7, false);
            var11.delayBeforeCanPickup = 0;
            var11.func_145797_a(var3.getCommandSenderName());
            func_152373_a(p_71515_1_, this, "commands.give.success", new Object[] {var7.func_151000_E(), Integer.valueOf(var5), var3.getCommandSenderName()});
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length == 1 ? getListOfStringsMatchingLastWord(p_71516_2_, this.getPlayers()) : (p_71516_2_.length == 2 ? getListOfStringsFromIterableMatchingLastWord(p_71516_2_, Item.itemRegistry.getKeys()) : null);
    }

    protected String[] getPlayers()
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
