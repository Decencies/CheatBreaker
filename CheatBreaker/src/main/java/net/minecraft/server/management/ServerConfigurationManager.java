package net.minecraft.server.management;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.demo.DemoWorldManager;
import net.minecraft.world.storage.IPlayerFileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ServerConfigurationManager
{
    public static final File field_152613_a = new File("banned-players.json");
    public static final File field_152614_b = new File("banned-ips.json");
    public static final File field_152615_c = new File("ops.json");
    public static final File field_152616_d = new File("whitelist.json");
    private static final Logger logger = LogManager.getLogger();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd \'at\' HH:mm:ss z");

    /** Reference to the MinecraftServer object. */
    private final MinecraftServer mcServer;

    /** A list of player entities that exist on this server. */
    public final List playerEntityList = new ArrayList();
    private final UserListBans bannedPlayers;
    private final BanList bannedIPs;

    /** A set containing the OPs. */
    private final UserListOps ops;

    /** The Set of all whitelisted players. */
    private final UserListWhitelist whiteListedPlayers;
    private final Map field_148547_k;

    /** Reference to the PlayerNBTManager object. */
    private IPlayerFileData playerNBTManagerObj;

    /**
     * Server setting to only allow OPs and whitelisted players to join the server.
     */
    private boolean whiteListEnforced;

    /** The maximum number of players that can be connected at a time. */
    protected int maxPlayers;
    private int viewDistance;
    private WorldSettings.GameType gameType;

    /** True if all players are allowed to use commands (cheats). */
    private boolean commandsAllowedForAll;

    /**
     * index into playerEntities of player to ping, updated every tick; currently hardcoded to max at 200 players
     */
    private int playerPingIndex;


    public ServerConfigurationManager(MinecraftServer p_i1500_1_)
    {
        this.bannedPlayers = new UserListBans(field_152613_a);
        this.bannedIPs = new BanList(field_152614_b);
        this.ops = new UserListOps(field_152615_c);
        this.whiteListedPlayers = new UserListWhitelist(field_152616_d);
        this.field_148547_k = Maps.newHashMap();
        this.mcServer = p_i1500_1_;
        this.bannedPlayers.func_152686_a(false);
        this.bannedIPs.func_152686_a(false);
        this.maxPlayers = 8;
    }

    public void initializeConnectionToPlayer(NetworkManager p_72355_1_, EntityPlayerMP p_72355_2_)
    {
        GameProfile var3 = p_72355_2_.getGameProfile();
        PlayerProfileCache var4 = this.mcServer.func_152358_ax();
        GameProfile var5 = var4.func_152652_a(var3.getId());
        String var6 = var5 == null ? var3.getName() : var5.getName();
        var4.func_152649_a(var3);
        NBTTagCompound var7 = this.readPlayerDataFromFile(p_72355_2_);
        p_72355_2_.setWorld(this.mcServer.worldServerForDimension(p_72355_2_.dimension));
        p_72355_2_.theItemInWorldManager.setWorld((WorldServer)p_72355_2_.worldObj);
        String var8 = "local";

        if (p_72355_1_.getSocketAddress() != null)
        {
            var8 = p_72355_1_.getSocketAddress().toString();
        }

        logger.info(p_72355_2_.getCommandSenderName() + "[" + var8 + "] logged in with entity id " + p_72355_2_.getEntityId() + " at (" + p_72355_2_.posX + ", " + p_72355_2_.posY + ", " + p_72355_2_.posZ + ")");
        WorldServer var9 = this.mcServer.worldServerForDimension(p_72355_2_.dimension);
        ChunkCoordinates var10 = var9.getSpawnPoint();
        this.func_72381_a(p_72355_2_, (EntityPlayerMP)null, var9);
        NetHandlerPlayServer var11 = new NetHandlerPlayServer(this.mcServer, p_72355_1_, p_72355_2_);
        var11.sendPacket(new S01PacketJoinGame(p_72355_2_.getEntityId(), p_72355_2_.theItemInWorldManager.getGameType(), var9.getWorldInfo().isHardcoreModeEnabled(), var9.provider.dimensionId, var9.difficultySetting, this.getMaxPlayers(), var9.getWorldInfo().getTerrainType()));
        var11.sendPacket(new S3FPacketCustomPayload("MC|Brand", this.getServerInstance().getServerModName().getBytes(Charsets.UTF_8)));
        var11.sendPacket(new S05PacketSpawnPosition(var10.posX, var10.posY, var10.posZ));
        var11.sendPacket(new S39PacketPlayerAbilities(p_72355_2_.capabilities));
        var11.sendPacket(new S09PacketHeldItemChange(p_72355_2_.inventory.currentItem));
        p_72355_2_.func_147099_x().func_150877_d();
        p_72355_2_.func_147099_x().func_150884_b(p_72355_2_);
        this.func_96456_a((ServerScoreboard)var9.getScoreboard(), p_72355_2_);
        this.mcServer.func_147132_au();
        ChatComponentTranslation var12;

        if (!p_72355_2_.getCommandSenderName().equalsIgnoreCase(var6))
        {
            var12 = new ChatComponentTranslation("multiplayer.player.joined.renamed", new Object[] {p_72355_2_.func_145748_c_(), var6});
        }
        else
        {
            var12 = new ChatComponentTranslation("multiplayer.player.joined", new Object[] {p_72355_2_.func_145748_c_()});
        }

        var12.getChatStyle().setColor(EnumChatFormatting.YELLOW);
        this.func_148539_a(var12);
        this.playerLoggedIn(p_72355_2_);
        var11.setPlayerLocation(p_72355_2_.posX, p_72355_2_.posY, p_72355_2_.posZ, p_72355_2_.rotationYaw, p_72355_2_.rotationPitch);
        this.updateTimeAndWeatherForPlayer(p_72355_2_, var9);

        if (this.mcServer.func_147133_T().length() > 0)
        {
            p_72355_2_.func_147095_a(this.mcServer.func_147133_T());
        }

        Iterator var13 = p_72355_2_.getActivePotionEffects().iterator();

        while (var13.hasNext())
        {
            PotionEffect var14 = (PotionEffect)var13.next();
            var11.sendPacket(new S1DPacketEntityEffect(p_72355_2_.getEntityId(), var14));
        }

        p_72355_2_.addSelfToInternalCraftingInventory();

        if (var7 != null && var7.func_150297_b("Riding", 10))
        {
            Entity var15 = EntityList.createEntityFromNBT(var7.getCompoundTag("Riding"), var9);

            if (var15 != null)
            {
                var15.forceSpawn = true;
                var9.spawnEntityInWorld(var15);
                p_72355_2_.mountEntity(var15);
                var15.forceSpawn = false;
            }
        }
    }

    protected void func_96456_a(ServerScoreboard p_96456_1_, EntityPlayerMP p_96456_2_)
    {
        HashSet var3 = new HashSet();
        Iterator var4 = p_96456_1_.getTeams().iterator();

        while (var4.hasNext())
        {
            ScorePlayerTeam var5 = (ScorePlayerTeam)var4.next();
            p_96456_2_.playerNetServerHandler.sendPacket(new S3EPacketTeams(var5, 0));
        }

        for (int var9 = 0; var9 < 3; ++var9)
        {
            ScoreObjective var10 = p_96456_1_.func_96539_a(var9);

            if (var10 != null && !var3.contains(var10))
            {
                List var6 = p_96456_1_.func_96550_d(var10);
                Iterator var7 = var6.iterator();

                while (var7.hasNext())
                {
                    Packet var8 = (Packet)var7.next();
                    p_96456_2_.playerNetServerHandler.sendPacket(var8);
                }

                var3.add(var10);
            }
        }
    }

    /**
     * Sets the NBT manager to the one for the WorldServer given.
     */
    public void setPlayerManager(WorldServer[] p_72364_1_)
    {
        this.playerNBTManagerObj = p_72364_1_[0].getSaveHandler().getSaveHandler();
    }

    public void func_72375_a(EntityPlayerMP p_72375_1_, WorldServer p_72375_2_)
    {
        WorldServer var3 = p_72375_1_.getServerForPlayer();

        if (p_72375_2_ != null)
        {
            p_72375_2_.getPlayerManager().removePlayer(p_72375_1_);
        }

        var3.getPlayerManager().addPlayer(p_72375_1_);
        var3.theChunkProviderServer.loadChunk((int)p_72375_1_.posX >> 4, (int)p_72375_1_.posZ >> 4);
    }

    public int getEntityViewDistance()
    {
        return PlayerManager.getFurthestViewableBlock(this.getViewDistance());
    }

    /**
     * called during player login. reads the player information from disk.
     */
    public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP p_72380_1_)
    {
        NBTTagCompound var2 = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
        NBTTagCompound var3;

        if (p_72380_1_.getCommandSenderName().equals(this.mcServer.getServerOwner()) && var2 != null)
        {
            p_72380_1_.readFromNBT(var2);
            var3 = var2;
            logger.debug("loading single player");
        }
        else
        {
            var3 = this.playerNBTManagerObj.readPlayerData(p_72380_1_);
        }

        return var3;
    }

    /**
     * also stores the NBTTags if this is an intergratedPlayerList
     */
    protected void writePlayerData(EntityPlayerMP p_72391_1_)
    {
        this.playerNBTManagerObj.writePlayerData(p_72391_1_);
        StatisticsFile var2 = (StatisticsFile)this.field_148547_k.get(p_72391_1_.getUniqueID());

        if (var2 != null)
        {
            var2.func_150883_b();
        }
    }

    /**
     * Called when a player successfully logs in. Reads player data from disk and inserts the player into the world.
     */
    public void playerLoggedIn(EntityPlayerMP p_72377_1_)
    {
        this.func_148540_a(new S38PacketPlayerListItem(p_72377_1_.getCommandSenderName(), true, 1000));
        this.playerEntityList.add(p_72377_1_);
        WorldServer var2 = this.mcServer.worldServerForDimension(p_72377_1_.dimension);
        var2.spawnEntityInWorld(p_72377_1_);
        this.func_72375_a(p_72377_1_, (WorldServer)null);

        for (int var3 = 0; var3 < this.playerEntityList.size(); ++var3)
        {
            EntityPlayerMP var4 = (EntityPlayerMP)this.playerEntityList.get(var3);
            p_72377_1_.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(var4.getCommandSenderName(), true, var4.ping));
        }
    }

    /**
     * using player's dimension, update their movement when in a vehicle (e.g. cart, boat)
     */
    public void serverUpdateMountedMovingPlayer(EntityPlayerMP p_72358_1_)
    {
        p_72358_1_.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(p_72358_1_);
    }

    /**
     * Called when a player disconnects from the game. Writes player data to disk and removes them from the world.
     */
    public void playerLoggedOut(EntityPlayerMP p_72367_1_)
    {
        p_72367_1_.triggerAchievement(StatList.leaveGameStat);
        this.writePlayerData(p_72367_1_);
        WorldServer var2 = p_72367_1_.getServerForPlayer();

        if (p_72367_1_.ridingEntity != null)
        {
            var2.removePlayerEntityDangerously(p_72367_1_.ridingEntity);
            logger.debug("removing player mount");
        }

        var2.removeEntity(p_72367_1_);
        var2.getPlayerManager().removePlayer(p_72367_1_);
        this.playerEntityList.remove(p_72367_1_);
        this.field_148547_k.remove(p_72367_1_.getUniqueID());
        this.func_148540_a(new S38PacketPlayerListItem(p_72367_1_.getCommandSenderName(), false, 9999));
    }

    public String func_148542_a(SocketAddress p_148542_1_, GameProfile p_148542_2_)
    {
        String var4;

        if (this.bannedPlayers.func_152702_a(p_148542_2_))
        {
            UserListBansEntry var5 = (UserListBansEntry)this.bannedPlayers.func_152683_b(p_148542_2_);
            var4 = "You are banned from this server!\nReason: " + var5.getBanReason();

            if (var5.getBanEndDate() != null)
            {
                var4 = var4 + "\nYour ban will be removed on " + dateFormat.format(var5.getBanEndDate());
            }

            return var4;
        }
        else if (!this.func_152607_e(p_148542_2_))
        {
            return "You are not white-listed on this server!";
        }
        else if (this.bannedIPs.func_152708_a(p_148542_1_))
        {
            IPBanEntry var3 = this.bannedIPs.func_152709_b(p_148542_1_);
            var4 = "Your IP address is banned from this server!\nReason: " + var3.getBanReason();

            if (var3.getBanEndDate() != null)
            {
                var4 = var4 + "\nYour ban will be removed on " + dateFormat.format(var3.getBanEndDate());
            }

            return var4;
        }
        else
        {
            return this.playerEntityList.size() >= this.maxPlayers ? "The server is full!" : null;
        }
    }

    public EntityPlayerMP func_148545_a(GameProfile p_148545_1_)
    {
        UUID var2 = EntityPlayer.func_146094_a(p_148545_1_);
        ArrayList var3 = Lists.newArrayList();
        EntityPlayerMP var5;

        for (int var4 = 0; var4 < this.playerEntityList.size(); ++var4)
        {
            var5 = (EntityPlayerMP)this.playerEntityList.get(var4);

            if (var5.getUniqueID().equals(var2))
            {
                var3.add(var5);
            }
        }

        Iterator var6 = var3.iterator();

        while (var6.hasNext())
        {
            var5 = (EntityPlayerMP)var6.next();
            var5.playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
        }

        Object var7;

        if (this.mcServer.isDemo())
        {
            var7 = new DemoWorldManager(this.mcServer.worldServerForDimension(0));
        }
        else
        {
            var7 = new ItemInWorldManager(this.mcServer.worldServerForDimension(0));
        }

        return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), p_148545_1_, (ItemInWorldManager)var7);
    }

    /**
     * creates and returns a respawned player based on the provided PlayerEntity. Args are the PlayerEntityMP to
     * respawn, an INT for the dimension to respawn into (usually 0), and a boolean value that is true if the player
     * beat the game rather than dying
     */
    public EntityPlayerMP respawnPlayer(EntityPlayerMP p_72368_1_, int p_72368_2_, boolean p_72368_3_)
    {
        p_72368_1_.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(p_72368_1_);
        p_72368_1_.getServerForPlayer().getEntityTracker().removeEntityFromAllTrackingPlayers(p_72368_1_);
        p_72368_1_.getServerForPlayer().getPlayerManager().removePlayer(p_72368_1_);
        this.playerEntityList.remove(p_72368_1_);
        this.mcServer.worldServerForDimension(p_72368_1_.dimension).removePlayerEntityDangerously(p_72368_1_);
        ChunkCoordinates var4 = p_72368_1_.getBedLocation();
        boolean var5 = p_72368_1_.isSpawnForced();
        p_72368_1_.dimension = p_72368_2_;
        Object var6;

        if (this.mcServer.isDemo())
        {
            var6 = new DemoWorldManager(this.mcServer.worldServerForDimension(p_72368_1_.dimension));
        }
        else
        {
            var6 = new ItemInWorldManager(this.mcServer.worldServerForDimension(p_72368_1_.dimension));
        }

        EntityPlayerMP var7 = new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(p_72368_1_.dimension), p_72368_1_.getGameProfile(), (ItemInWorldManager)var6);
        var7.playerNetServerHandler = p_72368_1_.playerNetServerHandler;
        var7.clonePlayer(p_72368_1_, p_72368_3_);
        var7.setEntityId(p_72368_1_.getEntityId());
        WorldServer var8 = this.mcServer.worldServerForDimension(p_72368_1_.dimension);
        this.func_72381_a(var7, p_72368_1_, var8);
        ChunkCoordinates var9;

        if (var4 != null)
        {
            var9 = EntityPlayer.verifyRespawnCoordinates(this.mcServer.worldServerForDimension(p_72368_1_.dimension), var4, var5);

            if (var9 != null)
            {
                var7.setLocationAndAngles((double)((float)var9.posX + 0.5F), (double)((float)var9.posY + 0.1F), (double)((float)var9.posZ + 0.5F), 0.0F, 0.0F);
                var7.setSpawnChunk(var4, var5);
            }
            else
            {
                var7.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0F));
            }
        }

        var8.theChunkProviderServer.loadChunk((int)var7.posX >> 4, (int)var7.posZ >> 4);

        while (!var8.getCollidingBoundingBoxes(var7, var7.boundingBox).isEmpty())
        {
            var7.setPosition(var7.posX, var7.posY + 1.0D, var7.posZ);
        }

        var7.playerNetServerHandler.sendPacket(new S07PacketRespawn(var7.dimension, var7.worldObj.difficultySetting, var7.worldObj.getWorldInfo().getTerrainType(), var7.theItemInWorldManager.getGameType()));
        var9 = var8.getSpawnPoint();
        var7.playerNetServerHandler.setPlayerLocation(var7.posX, var7.posY, var7.posZ, var7.rotationYaw, var7.rotationPitch);
        var7.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(var9.posX, var9.posY, var9.posZ));
        var7.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(var7.experience, var7.experienceTotal, var7.experienceLevel));
        this.updateTimeAndWeatherForPlayer(var7, var8);
        var8.getPlayerManager().addPlayer(var7);
        var8.spawnEntityInWorld(var7);
        this.playerEntityList.add(var7);
        var7.addSelfToInternalCraftingInventory();
        var7.setHealth(var7.getHealth());
        return var7;
    }

    public void transferPlayerToDimension(EntityPlayerMP p_72356_1_, int p_72356_2_)
    {
        int var3 = p_72356_1_.dimension;
        WorldServer var4 = this.mcServer.worldServerForDimension(p_72356_1_.dimension);
        p_72356_1_.dimension = p_72356_2_;
        WorldServer var5 = this.mcServer.worldServerForDimension(p_72356_1_.dimension);
        p_72356_1_.playerNetServerHandler.sendPacket(new S07PacketRespawn(p_72356_1_.dimension, p_72356_1_.worldObj.difficultySetting, p_72356_1_.worldObj.getWorldInfo().getTerrainType(), p_72356_1_.theItemInWorldManager.getGameType()));
        var4.removePlayerEntityDangerously(p_72356_1_);
        p_72356_1_.isDead = false;
        this.transferEntityToWorld(p_72356_1_, var3, var4, var5);
        this.func_72375_a(p_72356_1_, var4);
        p_72356_1_.playerNetServerHandler.setPlayerLocation(p_72356_1_.posX, p_72356_1_.posY, p_72356_1_.posZ, p_72356_1_.rotationYaw, p_72356_1_.rotationPitch);
        p_72356_1_.theItemInWorldManager.setWorld(var5);
        this.updateTimeAndWeatherForPlayer(p_72356_1_, var5);
        this.syncPlayerInventory(p_72356_1_);
        Iterator var6 = p_72356_1_.getActivePotionEffects().iterator();

        while (var6.hasNext())
        {
            PotionEffect var7 = (PotionEffect)var6.next();
            p_72356_1_.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(p_72356_1_.getEntityId(), var7));
        }
    }

    /**
     * Transfers an entity from a world to another world.
     */
    public void transferEntityToWorld(Entity p_82448_1_, int p_82448_2_, WorldServer p_82448_3_, WorldServer p_82448_4_)
    {
        double var5 = p_82448_1_.posX;
        double var7 = p_82448_1_.posZ;
        double var9 = 8.0D;
        double var11 = p_82448_1_.posX;
        double var13 = p_82448_1_.posY;
        double var15 = p_82448_1_.posZ;
        float var17 = p_82448_1_.rotationYaw;
        p_82448_3_.theProfiler.startSection("moving");

        if (p_82448_1_.dimension == -1)
        {
            var5 /= var9;
            var7 /= var9;
            p_82448_1_.setLocationAndAngles(var5, p_82448_1_.posY, var7, p_82448_1_.rotationYaw, p_82448_1_.rotationPitch);

            if (p_82448_1_.isEntityAlive())
            {
                p_82448_3_.updateEntityWithOptionalForce(p_82448_1_, false);
            }
        }
        else if (p_82448_1_.dimension == 0)
        {
            var5 *= var9;
            var7 *= var9;
            p_82448_1_.setLocationAndAngles(var5, p_82448_1_.posY, var7, p_82448_1_.rotationYaw, p_82448_1_.rotationPitch);

            if (p_82448_1_.isEntityAlive())
            {
                p_82448_3_.updateEntityWithOptionalForce(p_82448_1_, false);
            }
        }
        else
        {
            ChunkCoordinates var18;

            if (p_82448_2_ == 1)
            {
                var18 = p_82448_4_.getSpawnPoint();
            }
            else
            {
                var18 = p_82448_4_.getEntrancePortalLocation();
            }

            var5 = (double)var18.posX;
            p_82448_1_.posY = (double)var18.posY;
            var7 = (double)var18.posZ;
            p_82448_1_.setLocationAndAngles(var5, p_82448_1_.posY, var7, 90.0F, 0.0F);

            if (p_82448_1_.isEntityAlive())
            {
                p_82448_3_.updateEntityWithOptionalForce(p_82448_1_, false);
            }
        }

        p_82448_3_.theProfiler.endSection();

        if (p_82448_2_ != 1)
        {
            p_82448_3_.theProfiler.startSection("placing");
            var5 = (double)MathHelper.clamp_int((int)var5, -29999872, 29999872);
            var7 = (double)MathHelper.clamp_int((int)var7, -29999872, 29999872);

            if (p_82448_1_.isEntityAlive())
            {
                p_82448_1_.setLocationAndAngles(var5, p_82448_1_.posY, var7, p_82448_1_.rotationYaw, p_82448_1_.rotationPitch);
                p_82448_4_.getDefaultTeleporter().placeInPortal(p_82448_1_, var11, var13, var15, var17);
                p_82448_4_.spawnEntityInWorld(p_82448_1_);
                p_82448_4_.updateEntityWithOptionalForce(p_82448_1_, false);
            }

            p_82448_3_.theProfiler.endSection();
        }

        p_82448_1_.setWorld(p_82448_4_);
    }

    /**
     * sends 1 player per tick, but only sends a player once every 600 ticks
     */
    public void sendPlayerInfoToAllPlayers()
    {
        if (++this.playerPingIndex > 600)
        {
            this.playerPingIndex = 0;
        }

        if (this.playerPingIndex < this.playerEntityList.size())
        {
            EntityPlayerMP var1 = (EntityPlayerMP)this.playerEntityList.get(this.playerPingIndex);
            this.func_148540_a(new S38PacketPlayerListItem(var1.getCommandSenderName(), true, var1.ping));
        }
    }

    public void func_148540_a(Packet p_148540_1_)
    {
        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2)
        {
            ((EntityPlayerMP)this.playerEntityList.get(var2)).playerNetServerHandler.sendPacket(p_148540_1_);
        }
    }

    public void func_148537_a(Packet p_148537_1_, int p_148537_2_)
    {
        for (int var3 = 0; var3 < this.playerEntityList.size(); ++var3)
        {
            EntityPlayerMP var4 = (EntityPlayerMP)this.playerEntityList.get(var3);

            if (var4.dimension == p_148537_2_)
            {
                var4.playerNetServerHandler.sendPacket(p_148537_1_);
            }
        }
    }

    public String func_152609_b(boolean p_152609_1_)
    {
        String var2 = "";
        ArrayList var3 = Lists.newArrayList(this.playerEntityList);

        for (int var4 = 0; var4 < var3.size(); ++var4)
        {
            if (var4 > 0)
            {
                var2 = var2 + ", ";
            }

            var2 = var2 + ((EntityPlayerMP)var3.get(var4)).getCommandSenderName();

            if (p_152609_1_)
            {
                var2 = var2 + " (" + ((EntityPlayerMP)var3.get(var4)).getUniqueID().toString() + ")";
            }
        }

        return var2;
    }

    /**
     * Returns an array of the usernames of all the connected players.
     */
    public String[] getAllUsernames()
    {
        String[] var1 = new String[this.playerEntityList.size()];

        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2)
        {
            var1[var2] = ((EntityPlayerMP)this.playerEntityList.get(var2)).getCommandSenderName();
        }

        return var1;
    }

    public GameProfile[] func_152600_g()
    {
        GameProfile[] var1 = new GameProfile[this.playerEntityList.size()];

        for (int var2 = 0; var2 < this.playerEntityList.size(); ++var2)
        {
            var1[var2] = ((EntityPlayerMP)this.playerEntityList.get(var2)).getGameProfile();
        }

        return var1;
    }

    public UserListBans func_152608_h()
    {
        return this.bannedPlayers;
    }

    public BanList getBannedIPs()
    {
        return this.bannedIPs;
    }

    public void func_152605_a(GameProfile p_152605_1_)
    {
        this.ops.func_152687_a(new UserListOpsEntry(p_152605_1_, this.mcServer.func_110455_j()));
    }

    public void func_152610_b(GameProfile p_152610_1_)
    {
        this.ops.func_152684_c(p_152610_1_);
    }

    public boolean func_152607_e(GameProfile p_152607_1_)
    {
        return !this.whiteListEnforced || this.ops.func_152692_d(p_152607_1_) || this.whiteListedPlayers.func_152692_d(p_152607_1_);
    }

    public boolean func_152596_g(GameProfile p_152596_1_)
    {
        return this.ops.func_152692_d(p_152596_1_) || this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(p_152596_1_.getName()) || this.commandsAllowedForAll;
    }

    public EntityPlayerMP func_152612_a(String p_152612_1_)
    {
        Iterator var2 = this.playerEntityList.iterator();
        EntityPlayerMP var3;

        do
        {
            if (!var2.hasNext())
            {
                return null;
            }

            var3 = (EntityPlayerMP)var2.next();
        }
        while (!var3.getCommandSenderName().equalsIgnoreCase(p_152612_1_));

        return var3;
    }

    /**
     * Find all players in a specified range and narrowing down by other parameters
     */
    public List findPlayers(ChunkCoordinates p_82449_1_, int p_82449_2_, int p_82449_3_, int p_82449_4_, int p_82449_5_, int p_82449_6_, int p_82449_7_, Map p_82449_8_, String p_82449_9_, String p_82449_10_, World p_82449_11_)
    {
        if (this.playerEntityList.isEmpty())
        {
            return Collections.emptyList();
        }
        else
        {
            Object var12 = new ArrayList();
            boolean var13 = p_82449_4_ < 0;
            boolean var14 = p_82449_9_ != null && p_82449_9_.startsWith("!");
            boolean var15 = p_82449_10_ != null && p_82449_10_.startsWith("!");
            int var16 = p_82449_2_ * p_82449_2_;
            int var17 = p_82449_3_ * p_82449_3_;
            p_82449_4_ = MathHelper.abs_int(p_82449_4_);

            if (var14)
            {
                p_82449_9_ = p_82449_9_.substring(1);
            }

            if (var15)
            {
                p_82449_10_ = p_82449_10_.substring(1);
            }

            for (int var18 = 0; var18 < this.playerEntityList.size(); ++var18)
            {
                EntityPlayerMP var19 = (EntityPlayerMP)this.playerEntityList.get(var18);

                if ((p_82449_11_ == null || var19.worldObj == p_82449_11_) && (p_82449_9_ == null || var14 != p_82449_9_.equalsIgnoreCase(var19.getCommandSenderName())))
                {
                    if (p_82449_10_ != null)
                    {
                        Team var20 = var19.getTeam();
                        String var21 = var20 == null ? "" : var20.getRegisteredName();

                        if (var15 == p_82449_10_.equalsIgnoreCase(var21))
                        {
                            continue;
                        }
                    }

                    if (p_82449_1_ != null && (p_82449_2_ > 0 || p_82449_3_ > 0))
                    {
                        float var22 = p_82449_1_.getDistanceSquaredToChunkCoordinates(var19.getPlayerCoordinates());

                        if (p_82449_2_ > 0 && var22 < (float)var16 || p_82449_3_ > 0 && var22 > (float)var17)
                        {
                            continue;
                        }
                    }

                    if (this.func_96457_a(var19, p_82449_8_) && (p_82449_5_ == WorldSettings.GameType.NOT_SET.getID() || p_82449_5_ == var19.theItemInWorldManager.getGameType().getID()) && (p_82449_6_ <= 0 || var19.experienceLevel >= p_82449_6_) && var19.experienceLevel <= p_82449_7_)
                    {
                        ((List)var12).add(var19);
                    }
                }
            }

            if (p_82449_1_ != null)
            {
                Collections.sort((List)var12, new PlayerPositionComparator(p_82449_1_));
            }

            if (var13)
            {
                Collections.reverse((List)var12);
            }

            if (p_82449_4_ > 0)
            {
                var12 = ((List)var12).subList(0, Math.min(p_82449_4_, ((List)var12).size()));
            }

            return (List)var12;
        }
    }

    private boolean func_96457_a(EntityPlayer p_96457_1_, Map p_96457_2_)
    {
        if (p_96457_2_ != null && p_96457_2_.size() != 0)
        {
            Iterator var3 = p_96457_2_.entrySet().iterator();
            Entry var4;
            boolean var6;
            int var10;

            do
            {
                if (!var3.hasNext())
                {
                    return true;
                }

                var4 = (Entry)var3.next();
                String var5 = (String)var4.getKey();
                var6 = false;

                if (var5.endsWith("_min") && var5.length() > 4)
                {
                    var6 = true;
                    var5 = var5.substring(0, var5.length() - 4);
                }

                Scoreboard var7 = p_96457_1_.getWorldScoreboard();
                ScoreObjective var8 = var7.getObjective(var5);

                if (var8 == null)
                {
                    return false;
                }

                Score var9 = p_96457_1_.getWorldScoreboard().func_96529_a(p_96457_1_.getCommandSenderName(), var8);
                var10 = var9.getScorePoints();

                if (var10 < ((Integer)var4.getValue()).intValue() && var6)
                {
                    return false;
                }
            }
            while (var10 <= ((Integer)var4.getValue()).intValue() || var6);

            return false;
        }
        else
        {
            return true;
        }
    }

    public void func_148541_a(double p_148541_1_, double p_148541_3_, double p_148541_5_, double p_148541_7_, int p_148541_9_, Packet p_148541_10_)
    {
        this.func_148543_a((EntityPlayer)null, p_148541_1_, p_148541_3_, p_148541_5_, p_148541_7_, p_148541_9_, p_148541_10_);
    }

    public void func_148543_a(EntityPlayer p_148543_1_, double p_148543_2_, double p_148543_4_, double p_148543_6_, double p_148543_8_, int p_148543_10_, Packet p_148543_11_)
    {
        for (int var12 = 0; var12 < this.playerEntityList.size(); ++var12)
        {
            EntityPlayerMP var13 = (EntityPlayerMP)this.playerEntityList.get(var12);

            if (var13 != p_148543_1_ && var13.dimension == p_148543_10_)
            {
                double var14 = p_148543_2_ - var13.posX;
                double var16 = p_148543_4_ - var13.posY;
                double var18 = p_148543_6_ - var13.posZ;

                if (var14 * var14 + var16 * var16 + var18 * var18 < p_148543_8_ * p_148543_8_)
                {
                    var13.playerNetServerHandler.sendPacket(p_148543_11_);
                }
            }
        }
    }

    /**
     * Saves all of the players' current states.
     */
    public void saveAllPlayerData()
    {
        for (int var1 = 0; var1 < this.playerEntityList.size(); ++var1)
        {
            this.writePlayerData((EntityPlayerMP)this.playerEntityList.get(var1));
        }
    }

    public void func_152601_d(GameProfile p_152601_1_)
    {
        this.whiteListedPlayers.func_152687_a(new UserListWhitelistEntry(p_152601_1_));
    }

    public void func_152597_c(GameProfile p_152597_1_)
    {
        this.whiteListedPlayers.func_152684_c(p_152597_1_);
    }

    public UserListWhitelist func_152599_k()
    {
        return this.whiteListedPlayers;
    }

    public String[] func_152598_l()
    {
        return this.whiteListedPlayers.func_152685_a();
    }

    public UserListOps func_152603_m()
    {
        return this.ops;
    }

    public String[] func_152606_n()
    {
        return this.ops.func_152685_a();
    }

    /**
     * Either does nothing, or calls readWhiteList.
     */
    public void loadWhiteList() {}

    /**
     * Updates the time and weather for the given player to those of the given world
     */
    public void updateTimeAndWeatherForPlayer(EntityPlayerMP p_72354_1_, WorldServer p_72354_2_)
    {
        p_72354_1_.playerNetServerHandler.sendPacket(new S03PacketTimeUpdate(p_72354_2_.getTotalWorldTime(), p_72354_2_.getWorldTime(), p_72354_2_.getGameRules().getGameRuleBooleanValue("doDaylightCycle")));

        if (p_72354_2_.isRaining())
        {
            p_72354_1_.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0F));
            p_72354_1_.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(7, p_72354_2_.getRainStrength(1.0F)));
            p_72354_1_.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(8, p_72354_2_.getWeightedThunderStrength(1.0F)));
        }
    }

    /**
     * sends the players inventory to himself
     */
    public void syncPlayerInventory(EntityPlayerMP p_72385_1_)
    {
        p_72385_1_.sendContainerToPlayer(p_72385_1_.inventoryContainer);
        p_72385_1_.setPlayerHealthUpdated();
        p_72385_1_.playerNetServerHandler.sendPacket(new S09PacketHeldItemChange(p_72385_1_.inventory.currentItem));
    }

    /**
     * Returns the number of players currently on the server.
     */
    public int getCurrentPlayerCount()
    {
        return this.playerEntityList.size();
    }

    /**
     * Returns the maximum number of players allowed on the server.
     */
    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    /**
     * Returns an array of usernames for which player.dat exists for.
     */
    public String[] getAvailablePlayerDat()
    {
        return this.mcServer.worldServers[0].getSaveHandler().getSaveHandler().getAvailablePlayerDat();
    }

    public void setWhiteListEnabled(boolean p_72371_1_)
    {
        this.whiteListEnforced = p_72371_1_;
    }

    public List getPlayerList(String p_72382_1_)
    {
        ArrayList var2 = new ArrayList();
        Iterator var3 = this.playerEntityList.iterator();

        while (var3.hasNext())
        {
            EntityPlayerMP var4 = (EntityPlayerMP)var3.next();

            if (var4.getPlayerIP().equals(p_72382_1_))
            {
                var2.add(var4);
            }
        }

        return var2;
    }

    /**
     * Gets the View Distance.
     */
    public int getViewDistance()
    {
        return this.viewDistance;
    }

    public MinecraftServer getServerInstance()
    {
        return this.mcServer;
    }

    /**
     * On integrated servers, returns the host's player data to be written to level.dat.
     */
    public NBTTagCompound getHostPlayerData()
    {
        return null;
    }

    public void func_152604_a(WorldSettings.GameType p_152604_1_)
    {
        this.gameType = p_152604_1_;
    }

    private void func_72381_a(EntityPlayerMP p_72381_1_, EntityPlayerMP p_72381_2_, World p_72381_3_)
    {
        if (p_72381_2_ != null)
        {
            p_72381_1_.theItemInWorldManager.setGameType(p_72381_2_.theItemInWorldManager.getGameType());
        }
        else if (this.gameType != null)
        {
            p_72381_1_.theItemInWorldManager.setGameType(this.gameType);
        }

        p_72381_1_.theItemInWorldManager.initializeGameType(p_72381_3_.getWorldInfo().getGameType());
    }

    /**
     * Sets whether all players are allowed to use commands (cheats) on the server.
     */
    public void setCommandsAllowedForAll(boolean p_72387_1_)
    {
        this.commandsAllowedForAll = p_72387_1_;
    }

    /**
     * Kicks everyone with "Server closed" as reason.
     */
    public void removeAllPlayers()
    {
        for (int var1 = 0; var1 < this.playerEntityList.size(); ++var1)
        {
            ((EntityPlayerMP)this.playerEntityList.get(var1)).playerNetServerHandler.kickPlayerFromServer("Server closed");
        }
    }

    public void func_148544_a(IChatComponent p_148544_1_, boolean p_148544_2_)
    {
        this.mcServer.addChatMessage(p_148544_1_);
        this.func_148540_a(new S02PacketChat(p_148544_1_, p_148544_2_));
    }

    public void func_148539_a(IChatComponent p_148539_1_)
    {
        this.func_148544_a(p_148539_1_, true);
    }

    public StatisticsFile func_152602_a(EntityPlayer p_152602_1_)
    {
        UUID var2 = p_152602_1_.getUniqueID();
        StatisticsFile var3 = var2 == null ? null : (StatisticsFile)this.field_148547_k.get(var2);

        if (var3 == null)
        {
            File var4 = new File(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
            File var5 = new File(var4, var2.toString() + ".json");

            if (!var5.exists())
            {
                File var6 = new File(var4, p_152602_1_.getCommandSenderName() + ".json");

                if (var6.exists() && var6.isFile())
                {
                    var6.renameTo(var5);
                }
            }

            var3 = new StatisticsFile(this.mcServer, var5);
            var3.func_150882_a();
            this.field_148547_k.put(var2, var3);
        }

        return var3;
    }

    public void func_152611_a(int p_152611_1_)
    {
        this.viewDistance = p_152611_1_;

        if (this.mcServer.worldServers != null)
        {
            WorldServer[] var2 = this.mcServer.worldServers;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                WorldServer var5 = var2[var4];

                if (var5 != null)
                {
                    var5.getPlayerManager().func_152622_a(p_152611_1_);
                }
            }
        }
    }
}
