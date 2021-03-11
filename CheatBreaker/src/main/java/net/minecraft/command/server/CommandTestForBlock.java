package net.minecraft.command.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CommandTestForBlock extends CommandBase
{


    public String getCommandName()
    {
        return "testforblock";
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
        return "commands.testforblock.usage";
    }

    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_)
    {
        if (p_71515_2_.length >= 4)
        {
            int var3 = p_71515_1_.getPlayerCoordinates().posX;
            int var4 = p_71515_1_.getPlayerCoordinates().posY;
            int var5 = p_71515_1_.getPlayerCoordinates().posZ;
            var3 = MathHelper.floor_double(func_110666_a(p_71515_1_, (double)var3, p_71515_2_[0]));
            var4 = MathHelper.floor_double(func_110666_a(p_71515_1_, (double)var4, p_71515_2_[1]));
            var5 = MathHelper.floor_double(func_110666_a(p_71515_1_, (double)var5, p_71515_2_[2]));
            Block var6 = Block.getBlockFromName(p_71515_2_[3]);

            if (var6 == null)
            {
                throw new NumberInvalidException("commands.setblock.notFound", new Object[] {p_71515_2_[3]});
            }
            else
            {
                int var7 = -1;

                if (p_71515_2_.length >= 5)
                {
                    var7 = parseIntBounded(p_71515_1_, p_71515_2_[4], -1, 15);
                }

                World var8 = p_71515_1_.getEntityWorld();

                if (!var8.blockExists(var3, var4, var5))
                {
                    throw new CommandException("commands.testforblock.outOfWorld", new Object[0]);
                }
                else
                {
                    NBTTagCompound var9 = new NBTTagCompound();
                    boolean var10 = false;

                    if (p_71515_2_.length >= 6 && var6.hasTileEntity())
                    {
                        String var11 = func_147178_a(p_71515_1_, p_71515_2_, 5).getUnformattedText();

                        try
                        {
                            NBTBase var12 = JsonToNBT.func_150315_a(var11);

                            if (!(var12 instanceof NBTTagCompound))
                            {
                                throw new CommandException("commands.setblock.tagError", new Object[] {"Not a valid tag"});
                            }

                            var9 = (NBTTagCompound)var12;
                            var10 = true;
                        }
                        catch (NBTException var14)
                        {
                            throw new CommandException("commands.setblock.tagError", new Object[] {var14.getMessage()});
                        }
                    }

                    Block var15 = var8.getBlock(var3, var4, var5);

                    if (var15 != var6)
                    {
                        throw new CommandException("commands.testforblock.failed.tile", new Object[] {Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5), var15.getLocalizedName(), var6.getLocalizedName()});
                    }
                    else
                    {
                        if (var7 > -1)
                        {
                            int var16 = var8.getBlockMetadata(var3, var4, var5);

                            if (var16 != var7)
                            {
                                throw new CommandException("commands.testforblock.failed.data", new Object[] {Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var16), Integer.valueOf(var7)});
                            }
                        }

                        if (var10)
                        {
                            TileEntity var17 = var8.getTileEntity(var3, var4, var5);

                            if (var17 == null)
                            {
                                throw new CommandException("commands.testforblock.failed.tileEntity", new Object[] {Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5)});
                            }

                            NBTTagCompound var13 = new NBTTagCompound();
                            var17.writeToNBT(var13);

                            if (!this.func_147181_a(var9, var13))
                            {
                                throw new CommandException("commands.testforblock.failed.nbt", new Object[] {Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5)});
                            }
                        }

                        p_71515_1_.addChatMessage(new ChatComponentTranslation("commands.testforblock.success", new Object[] {Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5)}));
                    }
                }
            }
        }
        else
        {
            throw new WrongUsageException("commands.testforblock.usage", new Object[0]);
        }
    }

    public boolean func_147181_a(NBTBase p_147181_1_, NBTBase p_147181_2_)
    {
        if (p_147181_1_ == p_147181_2_)
        {
            return true;
        }
        else if (p_147181_1_ == null)
        {
            return true;
        }
        else if (p_147181_2_ == null)
        {
            return false;
        }
        else if (!p_147181_1_.getClass().equals(p_147181_2_.getClass()))
        {
            return false;
        }
        else if (p_147181_1_ instanceof NBTTagCompound)
        {
            NBTTagCompound var3 = (NBTTagCompound)p_147181_1_;
            NBTTagCompound var4 = (NBTTagCompound)p_147181_2_;
            Iterator var5 = var3.func_150296_c().iterator();
            String var6;
            NBTBase var7;

            do
            {
                if (!var5.hasNext())
                {
                    return true;
                }

                var6 = (String)var5.next();
                var7 = var3.getTag(var6);
            }
            while (this.func_147181_a(var7, var4.getTag(var6)));

            return false;
        }
        else
        {
            return p_147181_1_.equals(p_147181_2_);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
    {
        return p_71516_2_.length == 4 ? getListOfStringsFromIterableMatchingLastWord(p_71516_2_, Block.blockRegistry.getKeys()) : null;
    }
}
