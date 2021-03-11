package net.minecraft.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandHandler implements ICommandManager
{
    private static final Logger logger = LogManager.getLogger();

    /** Map of Strings to the ICommand objects they represent */
    private final Map commandMap = new HashMap();

    /** The set of ICommand objects currently loaded. */
    private final Set commandSet = new HashSet();


    public int executeCommand(ICommandSender p_71556_1_, String p_71556_2_)
    {
        p_71556_2_ = p_71556_2_.trim();

        if (p_71556_2_.startsWith("/"))
        {
            p_71556_2_ = p_71556_2_.substring(1);
        }

        String[] var3 = p_71556_2_.split(" ");
        String var4 = var3[0];
        var3 = dropFirstString(var3);
        ICommand var5 = (ICommand)this.commandMap.get(var4);
        int var6 = this.getUsernameIndex(var5, var3);
        int var7 = 0;
        ChatComponentTranslation var9;

        try
        {
            if (var5 == null)
            {
                throw new CommandNotFoundException();
            }

            if (var5.canCommandSenderUseCommand(p_71556_1_))
            {
                if (var6 > -1)
                {
                    EntityPlayerMP[] var8 = PlayerSelector.matchPlayers(p_71556_1_, var3[var6]);
                    String var22 = var3[var6];
                    EntityPlayerMP[] var10 = var8;
                    int var11 = var8.length;

                    for (int var12 = 0; var12 < var11; ++var12)
                    {
                        EntityPlayerMP var13 = var10[var12];
                        var3[var6] = var13.getCommandSenderName();

                        try
                        {
                            var5.processCommand(p_71556_1_, var3);
                            ++var7;
                        }
                        catch (CommandException var17)
                        {
                            ChatComponentTranslation var15 = new ChatComponentTranslation(var17.getMessage(), var17.getErrorOjbects());
                            var15.getChatStyle().setColor(EnumChatFormatting.RED);
                            p_71556_1_.addChatMessage(var15);
                        }
                    }

                    var3[var6] = var22;
                }
                else
                {
                    try
                    {
                        var5.processCommand(p_71556_1_, var3);
                        ++var7;
                    }
                    catch (CommandException var16)
                    {
                        var9 = new ChatComponentTranslation(var16.getMessage(), var16.getErrorOjbects());
                        var9.getChatStyle().setColor(EnumChatFormatting.RED);
                        p_71556_1_.addChatMessage(var9);
                    }
                }
            }
            else
            {
                ChatComponentTranslation var21 = new ChatComponentTranslation("commands.generic.permission", new Object[0]);
                var21.getChatStyle().setColor(EnumChatFormatting.RED);
                p_71556_1_.addChatMessage(var21);
            }
        }
        catch (WrongUsageException var18)
        {
            var9 = new ChatComponentTranslation("commands.generic.usage", new Object[] {new ChatComponentTranslation(var18.getMessage(), var18.getErrorOjbects())});
            var9.getChatStyle().setColor(EnumChatFormatting.RED);
            p_71556_1_.addChatMessage(var9);
        }
        catch (CommandException var19)
        {
            var9 = new ChatComponentTranslation(var19.getMessage(), var19.getErrorOjbects());
            var9.getChatStyle().setColor(EnumChatFormatting.RED);
            p_71556_1_.addChatMessage(var9);
        }
        catch (Throwable var20)
        {
            var9 = new ChatComponentTranslation("commands.generic.exception", new Object[0]);
            var9.getChatStyle().setColor(EnumChatFormatting.RED);
            p_71556_1_.addChatMessage(var9);
            logger.error("Couldn\'t process command: \'" + p_71556_2_ + "\'", var20);
        }

        return var7;
    }

    /**
     * adds the command and any aliases it has to the internal map of available commands
     */
    public ICommand registerCommand(ICommand p_71560_1_)
    {
        List var2 = p_71560_1_.getCommandAliases();
        this.commandMap.put(p_71560_1_.getCommandName(), p_71560_1_);
        this.commandSet.add(p_71560_1_);

        if (var2 != null)
        {
            Iterator var3 = var2.iterator();

            while (var3.hasNext())
            {
                String var4 = (String)var3.next();
                ICommand var5 = (ICommand)this.commandMap.get(var4);

                if (var5 == null || !var5.getCommandName().equals(var4))
                {
                    this.commandMap.put(var4, p_71560_1_);
                }
            }
        }

        return p_71560_1_;
    }

    /**
     * creates a new array and sets elements 0..n-2 to be 0..n-1 of the input (n elements)
     */
    private static String[] dropFirstString(String[] p_71559_0_)
    {
        String[] var1 = new String[p_71559_0_.length - 1];

        for (int var2 = 1; var2 < p_71559_0_.length; ++var2)
        {
            var1[var2 - 1] = p_71559_0_[var2];
        }

        return var1;
    }

    /**
     * Performs a "begins with" string match on each token in par2. Only returns commands that par1 can use.
     */
    public List getPossibleCommands(ICommandSender p_71558_1_, String p_71558_2_)
    {
        String[] var3 = p_71558_2_.split(" ", -1);
        String var4 = var3[0];

        if (var3.length == 1)
        {
            ArrayList var8 = new ArrayList();
            Iterator var6 = this.commandMap.entrySet().iterator();

            while (var6.hasNext())
            {
                Entry var7 = (Entry)var6.next();

                if (CommandBase.doesStringStartWith(var4, (String)var7.getKey()) && ((ICommand)var7.getValue()).canCommandSenderUseCommand(p_71558_1_))
                {
                    var8.add(var7.getKey());
                }
            }

            return var8;
        }
        else
        {
            if (var3.length > 1)
            {
                ICommand var5 = (ICommand)this.commandMap.get(var4);

                if (var5 != null)
                {
                    return var5.addTabCompletionOptions(p_71558_1_, dropFirstString(var3));
                }
            }

            return null;
        }
    }

    /**
     * returns all commands that the commandSender can use
     */
    public List getPossibleCommands(ICommandSender p_71557_1_)
    {
        ArrayList var2 = new ArrayList();
        Iterator var3 = this.commandSet.iterator();

        while (var3.hasNext())
        {
            ICommand var4 = (ICommand)var3.next();

            if (var4.canCommandSenderUseCommand(p_71557_1_))
            {
                var2.add(var4);
            }
        }

        return var2;
    }

    /**
     * returns a map of string to commads. All commands are returned, not just ones which someone has permission to use.
     */
    public Map getCommands()
    {
        return this.commandMap;
    }

    /**
     * Return a command's first parameter index containing a valid username.
     */
    private int getUsernameIndex(ICommand p_82370_1_, String[] p_82370_2_)
    {
        if (p_82370_1_ == null)
        {
            return -1;
        }
        else
        {
            for (int var3 = 0; var3 < p_82370_2_.length; ++var3)
            {
                if (p_82370_1_.isUsernameIndex(p_82370_2_, var3) && PlayerSelector.matchesMultiplePlayers(p_82370_2_[var3]))
                {
                    return var3;
                }
            }

            return -1;
        }
    }
}
