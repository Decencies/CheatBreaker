package net.minecraft.server.integrated;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.crash.CrashReport;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Reflector;
import net.minecraft.src.WorldServerMultiOF;
import net.minecraft.src.WorldServerOF;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer extends MinecraftServer
{
    private static final Logger logger = LogManager.getLogger();

    /** The Minecraft instance. */
    private final Minecraft mc;
    private final WorldSettings theWorldSettings;
    private boolean isGamePaused;
    private boolean isPublic;
    private ThreadLanServerPing lanServerPing;



    public IntegratedServer(Minecraft par1Minecraft, String par2Str, String par3Str, WorldSettings par4WorldSettings)
    {
        super(new File(par1Minecraft.mcDataDir, "saves"), par1Minecraft.getProxy());
        this.setServerOwner(par1Minecraft.getSession().getUsername());
        this.setFolderName(par2Str);
        this.setWorldName(par3Str);
        this.setDemo(par1Minecraft.isDemo());
        this.canCreateBonusChest(par4WorldSettings.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.func_152361_a(new IntegratedPlayerList(this));
        this.mc = par1Minecraft;
        this.theWorldSettings = par4WorldSettings;
        Reflector.callVoid(Reflector.ModLoader_registerServer, new Object[] {this});
    }

    protected void loadAllWorlds(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str)
    {
        this.convertMapIfNeeded(par1Str);
        ISaveHandler var7 = this.getActiveAnvilConverter().getSaveLoader(par1Str, true);

        if (Reflector.DimensionManager.exists())
        {
            Object var8 = this.isDemo() ? new DemoWorldServer(this, var7, par2Str, 0, this.theProfiler) : new WorldServerOF(this, var7, par2Str, 0, this.theWorldSettings, this.theProfiler);
            Integer[] var9 = (Integer[])((Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs, new Object[0]));
            Integer[] arr$ = var9;
            int len$ = var9.length;

            for (int i$ = 0; i$ < len$; ++i$)
            {
                int dim = arr$[i$].intValue();
                Object world = dim == 0 ? var8 : new WorldServerMultiOF(this, var7, par2Str, dim, this.theWorldSettings, (WorldServer)var8, this.theProfiler);
                ((WorldServer)world).addWorldAccess(new WorldManager(this, (WorldServer)world));

                if (!this.isSinglePlayer())
                {
                    ((WorldServer)world).getWorldInfo().setGameType(this.getGameType());
                }

                if (Reflector.EventBus.exists())
                {
                    Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, new Object[] {world});
                }
            }

            this.getConfigurationManager().setPlayerManager(new WorldServer[] {(WorldServer)var8});
        }
        else
        {
            this.worldServers = new WorldServer[3];
            this.timeOfLastDimensionTick = new long[this.worldServers.length][100];

            for (int var15 = 0; var15 < this.worldServers.length; ++var15)
            {
                byte var16 = 0;

                if (var15 == 1)
                {
                    var16 = -1;
                }

                if (var15 == 2)
                {
                    var16 = 1;
                }

                if (var15 == 0)
                {
                    if (this.isDemo())
                    {
                        this.worldServers[var15] = new DemoWorldServer(this, var7, par2Str, var16, this.theProfiler);
                    }
                    else
                    {
                        this.worldServers[var15] = new WorldServerOF(this, var7, par2Str, var16, this.theWorldSettings, this.theProfiler);
                    }
                }
                else
                {
                    this.worldServers[var15] = new WorldServerMultiOF(this, var7, par2Str, var16, this.theWorldSettings, this.worldServers[0], this.theProfiler);
                }

                this.worldServers[var15].addWorldAccess(new WorldManager(this, this.worldServers[var15]));
                this.getConfigurationManager().setPlayerManager(this.worldServers);
            }
        }

        this.func_147139_a(this.func_147135_j());
        this.initialWorldChunkLoad();
    }

    /**
     * Initialises the server and starts it.
     */
    protected boolean startServer() throws IOException
    {
        logger.info("Starting integrated minecraft server version 1.7.10");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.createNewKeyPair());
        Object inst;

        if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists())
        {
            inst = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);

            if (!Reflector.callBoolean(inst, Reflector.FMLCommonHandler_handleServerAboutToStart, new Object[] {this}))
            {
                return false;
            }
        }

        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.func_82749_j());
        this.setMOTD(this.getServerOwner() + " - " + this.worldServers[0].getWorldInfo().getWorldName());

        if (Reflector.FMLCommonHandler_handleServerStarting.exists())
        {
            inst = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);

            if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == Boolean.TYPE)
            {
                return Reflector.callBoolean(inst, Reflector.FMLCommonHandler_handleServerStarting, new Object[] {this});
            }

            Reflector.callVoid(inst, Reflector.FMLCommonHandler_handleServerStarting, new Object[] {this});
        }

        return true;
    }

    /**
     * Main function called by run() every loop.
     */
    public void tick()
    {
        boolean var1 = this.isGamePaused;
        this.isGamePaused = Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().func_147113_T();

        if (!var1 && this.isGamePaused)
        {
            logger.info("Saving and pausing game...");
            this.getConfigurationManager().saveAllPlayerData();
            this.saveAllWorlds(false);
        }

        if (!this.isGamePaused)
        {
            super.tick();

            if (this.mc.gameSettings.renderDistanceChunks != this.getConfigurationManager().getViewDistance())
            {
                logger.info("Changing view distance to {}, from {}", new Object[] {Integer.valueOf(this.mc.gameSettings.renderDistanceChunks), Integer.valueOf(this.getConfigurationManager().getViewDistance())});
                this.getConfigurationManager().func_152611_a(this.mc.gameSettings.renderDistanceChunks);
            }
        }
    }

    public boolean canStructuresSpawn()
    {
        return false;
    }

    public WorldSettings.GameType getGameType()
    {
        return this.theWorldSettings.getGameType();
    }

    public EnumDifficulty func_147135_j()
    {
        return this.mc.gameSettings.difficulty;
    }

    /**
     * Defaults to false.
     */
    public boolean isHardcore()
    {
        return this.theWorldSettings.getHardcoreEnabled();
    }

    public boolean func_152363_m()
    {
        return false;
    }

    protected File getDataDirectory()
    {
        return this.mc.mcDataDir;
    }

    public boolean isDedicatedServer()
    {
        return false;
    }

    /**
     * Called on exit from the main run() loop.
     */
    protected void finalTick(CrashReport par1CrashReport)
    {
        this.mc.crashed(par1CrashReport);
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
        par1CrashReport.getCategory().addCrashSectionCallable("Type", new Callable()
        {


            public String call()
            {
                return "Integrated Server (map_client.txt)";
            }
        });
        par1CrashReport.getCategory().addCrashSectionCallable("Is Modded", new Callable()
        {


            public String call()
            {
                String var1 = ClientBrandRetriever.getClientModName();

                if (!var1.equals("vanilla"))
                {
                    return "Definitely; Client brand changed to \'" + var1 + "\'";
                }
                else
                {
                    var1 = IntegratedServer.this.getServerModName();
                    return !var1.equals("vanilla") ? "Definitely; Server brand changed to \'" + var1 + "\'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both com.cheatbreaker.client + server brands are untouched.");
                }
            }
        });
        return par1CrashReport;
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        super.addServerStatsToSnooper(par1PlayerUsageSnooper);
        par1PlayerUsageSnooper.func_152768_a("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }

    /**
     * On dedicated does nothing. On integrated, sets commandsAllowedForAll, gameType and allows external connections.
     */
    public String shareToLAN(WorldSettings.GameType par1EnumGameType, boolean par2)
    {
        try
        {
            int var6 = -1;

            try
            {
                var6 = HttpUtil.func_76181_a();
            }
            catch (IOException var5)
            {
                ;
            }

            if (var6 <= 0)
            {
                var6 = 25564;
            }

            this.func_147137_ag().addLanEndpoint((InetAddress)null, var6);
            logger.info("Started on " + var6);
            this.isPublic = true;
            this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), var6 + "");
            this.lanServerPing.start();
            this.getConfigurationManager().func_152604_a(par1EnumGameType);
            this.getConfigurationManager().setCommandsAllowedForAll(par2);
            return var6 + "";
        }
        catch (IOException var61)
        {
            return null;
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        super.stopServer();

        if (this.lanServerPing != null)
        {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to shut down.
     */
    public void initiateShutdown()
    {
        super.initiateShutdown();

        if (this.lanServerPing != null)
        {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }

    /**
     * Returns true if this integrated server is open to LAN
     */
    public boolean getPublic()
    {
        return this.isPublic;
    }

    /**
     * Sets the game type for all worlds.
     */
    public void setGameType(WorldSettings.GameType par1EnumGameType)
    {
        this.getConfigurationManager().func_152604_a(par1EnumGameType);
    }

    /**
     * Return whether command blocks are enabled.
     */
    public boolean isCommandBlockEnabled()
    {
        return true;
    }

    public int func_110455_j()
    {
        return 4;
    }
}
