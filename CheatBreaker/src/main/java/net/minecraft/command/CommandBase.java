package net.minecraft.command;

import com.google.common.primitives.Doubles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public abstract class CommandBase implements ICommand
{
    private static IAdminCommand theAdmin;


    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    public List getCommandAliases()
    {
        return null;
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
    {
        return p_71519_1_.canCommandSenderUseCommand(this.getRequiredPermissionLevel(), this.getCommandName());
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return null;
    }

    /**
     * Parses an int from the given string.
     */
    public static int parseInt(ICommandSender p_71526_0_, String p_71526_1_)
    {
        try
        {
            return Integer.parseInt(p_71526_1_);
        }
        catch (NumberFormatException var3)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {p_71526_1_});
        }
    }

    /**
     * Parses an int from the given sring with a specified minimum.
     */
    public static int parseIntWithMin(ICommandSender p_71528_0_, String p_71528_1_, int p_71528_2_)
    {
        return parseIntBounded(p_71528_0_, p_71528_1_, p_71528_2_, Integer.MAX_VALUE);
    }

    /**
     * Parses an int from the given string within a specified bound.
     */
    public static int parseIntBounded(ICommandSender p_71532_0_, String p_71532_1_, int p_71532_2_, int p_71532_3_)
    {
        int var4 = parseInt(p_71532_0_, p_71532_1_);

        if (var4 < p_71532_2_)
        {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] {Integer.valueOf(var4), Integer.valueOf(p_71532_2_)});
        }
        else if (var4 > p_71532_3_)
        {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] {Integer.valueOf(var4), Integer.valueOf(p_71532_3_)});
        }
        else
        {
            return var4;
        }
    }

    /**
     * Parses a double from the given string or throws an exception if it's not a double.
     */
    public static double parseDouble(ICommandSender p_82363_0_, String p_82363_1_)
    {
        try
        {
            double var2 = Double.parseDouble(p_82363_1_);

            if (!Doubles.isFinite(var2))
            {
                throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {p_82363_1_});
            }
            else
            {
                return var2;
            }
        }
        catch (NumberFormatException var4)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {p_82363_1_});
        }
    }

    /**
     * Parses a double from the given string.  Throws if the string could not be parsed as a double, or if it's less
     * than the given minimum value.
     */
    public static double parseDoubleWithMin(ICommandSender p_110664_0_, String p_110664_1_, double p_110664_2_)
    {
        return parseDoubleBounded(p_110664_0_, p_110664_1_, p_110664_2_, Double.MAX_VALUE);
    }

    /**
     * Parses a double from the given string.  Throws if the string could not be parsed as a double, or if it's not
     * between the given min and max values.
     */
    public static double parseDoubleBounded(ICommandSender p_110661_0_, String p_110661_1_, double p_110661_2_, double p_110661_4_)
    {
        double var6 = parseDouble(p_110661_0_, p_110661_1_);

        if (var6 < p_110661_2_)
        {
            throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(var6), Double.valueOf(p_110661_2_)});
        }
        else if (var6 > p_110661_4_)
        {
            throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(var6), Double.valueOf(p_110661_4_)});
        }
        else
        {
            return var6;
        }
    }

    /**
     * Parses a boolean value from the given string.  Throws if the string does not contain a boolean value.  Accepted
     * values are (case-sensitive): "true", "false", "0", "1".
     */
    public static boolean parseBoolean(ICommandSender p_110662_0_, String p_110662_1_)
    {
        if (!p_110662_1_.equals("true") && !p_110662_1_.equals("1"))
        {
            if (!p_110662_1_.equals("false") && !p_110662_1_.equals("0"))
            {
                throw new CommandException("commands.generic.boolean.invalid", new Object[] {p_110662_1_});
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns the given ICommandSender as a EntityPlayer or throw an exception.
     */
    public static EntityPlayerMP getCommandSenderAsPlayer(ICommandSender p_71521_0_)
    {
        if (p_71521_0_ instanceof EntityPlayerMP)
        {
            return (EntityPlayerMP)p_71521_0_;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }

    public static EntityPlayerMP getPlayer(ICommandSender p_82359_0_, String p_82359_1_)
    {
        EntityPlayerMP var2 = PlayerSelector.matchOnePlayer(p_82359_0_, p_82359_1_);

        if (var2 != null)
        {
            return var2;
        }
        else
        {
            var2 = MinecraftServer.getServer().getConfigurationManager().func_152612_a(p_82359_1_);

            if (var2 == null)
            {
                throw new PlayerNotFoundException();
            }
            else
            {
                return var2;
            }
        }
    }

    public static String func_96332_d(ICommandSender p_96332_0_, String p_96332_1_)
    {
        EntityPlayerMP var2 = PlayerSelector.matchOnePlayer(p_96332_0_, p_96332_1_);

        if (var2 != null)
        {
            return var2.getCommandSenderName();
        }
        else if (PlayerSelector.hasArguments(p_96332_1_))
        {
            throw new PlayerNotFoundException();
        }
        else
        {
            return p_96332_1_;
        }
    }

    public static IChatComponent func_147178_a(ICommandSender p_147178_0_, String[] p_147178_1_, int p_147178_2_)
    {
        return func_147176_a(p_147178_0_, p_147178_1_, p_147178_2_, false);
    }

    public static IChatComponent func_147176_a(ICommandSender p_147176_0_, String[] p_147176_1_, int p_147176_2_, boolean p_147176_3_)
    {
        ChatComponentText var4 = new ChatComponentText("");

        for (int var5 = p_147176_2_; var5 < p_147176_1_.length; ++var5)
        {
            if (var5 > p_147176_2_)
            {
                var4.appendText(" ");
            }

            Object var6 = new ChatComponentText(p_147176_1_[var5]);

            if (p_147176_3_)
            {
                IChatComponent var7 = PlayerSelector.func_150869_b(p_147176_0_, p_147176_1_[var5]);

                if (var7 != null)
                {
                    var6 = var7;
                }
                else if (PlayerSelector.hasArguments(p_147176_1_[var5]))
                {
                    throw new PlayerNotFoundException();
                }
            }

            var4.appendSibling((IChatComponent)var6);
        }

        return var4;
    }

    public static String func_82360_a(ICommandSender p_82360_0_, String[] p_82360_1_, int p_82360_2_)
    {
        StringBuilder var3 = new StringBuilder();

        for (int var4 = p_82360_2_; var4 < p_82360_1_.length; ++var4)
        {
            if (var4 > p_82360_2_)
            {
                var3.append(" ");
            }

            String var5 = p_82360_1_[var4];
            var3.append(var5);
        }

        return var3.toString();
    }

    public static double func_110666_a(ICommandSender p_110666_0_, double p_110666_1_, String p_110666_3_)
    {
        return func_110665_a(p_110666_0_, p_110666_1_, p_110666_3_, -30000000, 30000000);
    }

    public static double func_110665_a(ICommandSender p_110665_0_, double p_110665_1_, String p_110665_3_, int p_110665_4_, int p_110665_5_)
    {
        boolean var6 = p_110665_3_.startsWith("~");

        if (var6 && Double.isNaN(p_110665_1_))
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {Double.valueOf(p_110665_1_)});
        }
        else
        {
            double var7 = var6 ? p_110665_1_ : 0.0D;

            if (!var6 || p_110665_3_.length() > 1)
            {
                boolean var9 = p_110665_3_.contains(".");

                if (var6)
                {
                    p_110665_3_ = p_110665_3_.substring(1);
                }

                var7 += parseDouble(p_110665_0_, p_110665_3_);

                if (!var9 && !var6)
                {
                    var7 += 0.5D;
                }
            }

            if (p_110665_4_ != 0 || p_110665_5_ != 0)
            {
                if (var7 < (double)p_110665_4_)
                {
                    throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[] {Double.valueOf(var7), Integer.valueOf(p_110665_4_)});
                }

                if (var7 > (double)p_110665_5_)
                {
                    throw new NumberInvalidException("commands.generic.double.tooBig", new Object[] {Double.valueOf(var7), Integer.valueOf(p_110665_5_)});
                }
            }

            return var7;
        }
    }

    /**
     * Gets the Item specified by the given text string.  First checks the item registry, then tries by parsing the
     * string as an integer ID (deprecated).  Warns the sender if we matched by parsing the ID.  Throws if the item
     * wasn't found.  Returns the item if it was found.
     */
    public static Item getItemByText(ICommandSender p_147179_0_, String p_147179_1_)
    {
        Item var2 = (Item)Item.itemRegistry.getObject(p_147179_1_);

        if (var2 == null)
        {
            try
            {
                Item var3 = Item.getItemById(Integer.parseInt(p_147179_1_));

                if (var3 != null)
                {
                    ChatComponentTranslation var4 = new ChatComponentTranslation("commands.generic.deprecatedId", new Object[] {Item.itemRegistry.getNameForObject(var3)});
                    var4.getChatStyle().setColor(EnumChatFormatting.GRAY);
                    p_147179_0_.addChatMessage(var4);
                }

                var2 = var3;
            }
            catch (NumberFormatException var5)
            {
                ;
            }
        }

        if (var2 == null)
        {
            throw new NumberInvalidException("commands.give.notFound", new Object[] {p_147179_1_});
        }
        else
        {
            return var2;
        }
    }

    /**
     * Gets the Block specified by the given text string.  First checks the block registry, then tries by parsing the
     * string as an integer ID (deprecated).  Warns the sender if we matched by parsing the ID.  Throws if the block
     * wasn't found.  Returns the block if it was found.
     */
    public static Block getBlockByText(ICommandSender p_147180_0_, String p_147180_1_)
    {
        if (Block.blockRegistry.containsKey(p_147180_1_))
        {
            return (Block)Block.blockRegistry.getObject(p_147180_1_);
        }
        else
        {
            try
            {
                int var2 = Integer.parseInt(p_147180_1_);

                if (Block.blockRegistry.containsID(var2))
                {
                    Block var3 = Block.getBlockById(var2);
                    ChatComponentTranslation var4 = new ChatComponentTranslation("commands.generic.deprecatedId", new Object[] {Block.blockRegistry.getNameForObject(var3)});
                    var4.getChatStyle().setColor(EnumChatFormatting.GRAY);
                    p_147180_0_.addChatMessage(var4);
                    return var3;
                }
            }
            catch (NumberFormatException var5)
            {
                ;
            }

            throw new NumberInvalidException("commands.give.notFound", new Object[] {p_147180_1_});
        }
    }

    /**
     * Creates a linguistic series joining the input objects together.  Examples: 1) {} --> "",  2) {"Steve"} -->
     * "Steve",  3) {"Steve", "Phil"} --> "Steve and Phil",  4) {"Steve", "Phil", "Mark"} --> "Steve, Phil and Mark"
     */
    public static String joinNiceString(Object[] p_71527_0_)
    {
        StringBuilder var1 = new StringBuilder();

        for (int var2 = 0; var2 < p_71527_0_.length; ++var2)
        {
            String var3 = p_71527_0_[var2].toString();

            if (var2 > 0)
            {
                if (var2 == p_71527_0_.length - 1)
                {
                    var1.append(" and ");
                }
                else
                {
                    var1.append(", ");
                }
            }

            var1.append(var3);
        }

        return var1.toString();
    }

    /**
     * Creates a linguistic series joining the input chat components.  Examples: 1) {} --> "",  2) {"Steve"} -->
     * "Steve",  3) {"Steve", "Phil"} --> "Steve and Phil",  4) {"Steve", "Phil", "Mark"} --> "Steve, Phil and Mark"
     */
    public static IChatComponent joinNiceString(IChatComponent[] p_147177_0_)
    {
        ChatComponentText var1 = new ChatComponentText("");

        for (int var2 = 0; var2 < p_147177_0_.length; ++var2)
        {
            if (var2 > 0)
            {
                if (var2 == p_147177_0_.length - 1)
                {
                    var1.appendText(" and ");
                }
                else if (var2 > 0)
                {
                    var1.appendText(", ");
                }
            }

            var1.appendSibling(p_147177_0_[var2]);
        }

        return var1;
    }

    /**
     * Creates a linguistic series joining together the elements of the given collection.  Examples: 1) {} --> "",  2)
     * {"Steve"} --> "Steve",  3) {"Steve", "Phil"} --> "Steve and Phil",  4) {"Steve", "Phil", "Mark"} --> "Steve, Phil
     * and Mark"
     */
    public static String joinNiceStringFromCollection(Collection p_96333_0_)
    {
        return joinNiceString(p_96333_0_.toArray(new String[p_96333_0_.size()]));
    }

    /**
     * Returns true if the given substring is exactly equal to the start of the given string (case insensitive).
     */
    public static boolean doesStringStartWith(String p_71523_0_, String p_71523_1_)
    {
        return p_71523_1_.regionMatches(true, 0, p_71523_0_, 0, p_71523_0_.length());
    }

    /**
     * Returns a List of strings (chosen from the given strings) which the last word in the given string array is a
     * beginning-match for. (Tab completion).
     */
    public static List getListOfStringsMatchingLastWord(String[] p_71530_0_, String ... p_71530_1_)
    {
        String var2 = p_71530_0_[p_71530_0_.length - 1];
        ArrayList var3 = new ArrayList();
        String[] var4 = p_71530_1_;
        int var5 = p_71530_1_.length;

        for (int var6 = 0; var6 < var5; ++var6)
        {
            String var7 = var4[var6];

            if (doesStringStartWith(var2, var7))
            {
                var3.add(var7);
            }
        }

        return var3;
    }

    /**
     * Returns a List of strings (chosen from the given string iterable) which the last word in the given string array
     * is a beginning-match for. (Tab completion).
     */
    public static List getListOfStringsFromIterableMatchingLastWord(String[] p_71531_0_, Iterable p_71531_1_)
    {
        String var2 = p_71531_0_[p_71531_0_.length - 1];
        ArrayList var3 = new ArrayList();
        Iterator var4 = p_71531_1_.iterator();

        while (var4.hasNext())
        {
            String var5 = (String)var4.next();

            if (doesStringStartWith(var2, var5))
            {
                var3.add(var5);
            }
        }

        return var3;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
    {
        return false;
    }

    public static void func_152373_a(ICommandSender p_152373_0_, ICommand p_152373_1_, String p_152373_2_, Object ... p_152373_3_)
    {
        func_152374_a(p_152373_0_, p_152373_1_, 0, p_152373_2_, p_152373_3_);
    }

    public static void func_152374_a(ICommandSender p_152374_0_, ICommand p_152374_1_, int p_152374_2_, String p_152374_3_, Object ... p_152374_4_)
    {
        if (theAdmin != null)
        {
            theAdmin.func_152372_a(p_152374_0_, p_152374_1_, p_152374_2_, p_152374_3_, p_152374_4_);
        }
    }

    /**
     * Sets the static IAdminCommander.
     */
    public static void setAdminCommander(IAdminCommand p_71529_0_)
    {
        theAdmin = p_71529_0_;
    }

    public int compareTo(ICommand p_compareTo_1_)
    {
        return this.getCommandName().compareTo(p_compareTo_1_.getCommandName());
    }

    public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((ICommand)p_compareTo_1_);
    }
}
