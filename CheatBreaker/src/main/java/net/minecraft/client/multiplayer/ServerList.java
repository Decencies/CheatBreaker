package net.minecraft.client.multiplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cheatbreaker.client.CheatBreaker;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerList
{
    private static final Logger logger = LogManager.getLogger();

    /** The Minecraft instance. */
    private final Minecraft mc;

    /** List of ServerData instances. */
    private final List servers = new ArrayList();


    public ServerList(Minecraft p_i1194_1_)
    {
        this.mc = p_i1194_1_;
        this.loadServerList();
    }

    /**
     * Loads a list of servers from servers.dat, by running ServerData.getServerDataFromNBTCompound on each NBT compound
     * found in the "servers" tag list.
     */
    public void loadServerList()
    {
        try
        {
            this.servers.clear();
            NBTTagCompound var1 = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));

            if (var1 == null)
            {
                return;
            }

            NBTTagList var2 = var1.getTagList("servers", 10);
            for (String[] server : CheatBreaker.getInstance().getGlobalSettings().pinnedServers) {
                this.servers.add(new ServerData(true, server[0], server[1]));
            }

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                this.servers.add(ServerData.getServerDataFromNBTCompound(var2.getCompoundTagAt(var3)));
            }
        }
        catch (Exception var4)
        {
            logger.error("Couldn\'t load server list", var4);
        }
    }

    /**
     * Runs getNBTCompound on each ServerData instance, puts everything into a "servers" NBT list and writes it to
     * servers.dat.
     */
    public void saveServerList()
    {
        try
        {
            NBTTagList var1 = new NBTTagList();
            Iterator var2 = this.servers.iterator();

            while (var2.hasNext())
            {
                ServerData var3 = (ServerData)var2.next();
                if (var3.continueOnSave) continue;
                var1.appendTag(var3.getNBTCompound());
            }

            NBTTagCompound var5 = new NBTTagCompound();
            var5.setTag("servers", var1);
            CompressedStreamTools.safeWrite(var5, new File(this.mc.mcDataDir, "servers.dat"));
        }
        catch (Exception var4)
        {
            logger.error("Couldn\'t save server list", var4);
        }
    }

    /**
     * Gets the ServerData instance stored for the given index in the list.
     */
    public ServerData getServerData(int p_78850_1_)
    {
        return (ServerData)this.servers.get(p_78850_1_);
    }

    /**
     * Removes the ServerData instance stored for the given index in the list.
     */
    public void removeServerData(int p_78851_1_)
    {
        this.servers.remove(p_78851_1_);
    }

    /**
     * Adds the given ServerData instance to the list.
     */
    public void addServerData(ServerData p_78849_1_)
    {
        this.servers.add(p_78849_1_);
    }

    /**
     * Counts the number of ServerData instances in the list.
     */
    public int countServers()
    {
        return this.servers.size();
    }

    /**
     * Takes two list indexes, and swaps their order around.
     */
    public void swapServers(int p_78857_1_, int p_78857_2_)
    {
        ServerData var3 = this.getServerData(p_78857_1_);
        ServerData var4 = this.getServerData(p_78857_2_);

        if (!var3.continueOnSave && !var4.continueOnSave) {
            this.servers.set(p_78857_1_, var4);
            this.servers.set(p_78857_2_, var3);
            this.saveServerList();
        }
    }

    public void func_147413_a(int p_147413_1_, ServerData p_147413_2_)
    {
        this.servers.set(p_147413_1_, p_147413_2_);
    }

    public static void func_147414_b(ServerData p_147414_0_)
    {
        ServerList var1 = new ServerList(Minecraft.getMinecraft());
        var1.loadServerList();

        for (int var2 = 0; var2 < var1.countServers(); ++var2)
        {
            ServerData var3 = var1.getServerData(var2);

            if (var3.serverName.equals(p_147414_0_.serverName) && var3.serverIP.equals(p_147414_0_.serverIP))
            {
                var1.func_147413_a(var2, p_147414_0_);
                break;
            }
        }

        var1.saveServerList();
    }
}
