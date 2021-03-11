package net.minecraft.server.management;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.src.CompactArrayList;
import net.minecraft.src.Config;
import net.minecraft.src.Reflector;
import net.minecraft.src.ReflectorForge;
import net.minecraft.src.WorldServerOF;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerManager
{
    private static final Logger field_152627_a = LogManager.getLogger();
    private final WorldServer theWorldServer;

    /** players in the current instance */
    private final List players = new ArrayList();

    /**
     * A map of chunk position (two ints concatenated into a long) to PlayerInstance
     */
    private final LongHashMap playerInstances = new LongHashMap();

    /**
     * contains a PlayerInstance for every chunk they can see. the "player instance" cotains a list of all players who
     * can also that chunk
     */
    private final List chunkWatcherWithPlayers = new ArrayList();

    /** This field is using when chunk should be processed (every 8000 ticks) */
    private final List playerInstanceList = new ArrayList();
    public CompactArrayList chunkCoordsNotLoaded = new CompactArrayList(100, 0.8F);

    /**
     * Number of chunks the server sends to the com.cheatbreaker.client. Valid 3<=x<=15. In server.properties.
     */
    private int playerViewRadius;

    /** time what is using to check if InhabitedTime should be calculated */
    private long previousTotalWorldTime;

    /** x, z direction vectors: east, south, west, north */
    private final int[][] xzDirectionsConst = new int[][] {{1, 0}, {0, 1}, { -1, 0}, {0, -1}};



    public PlayerManager(WorldServer par1Minecraft)
    {
        this.theWorldServer = par1Minecraft;
        this.func_152622_a(par1Minecraft.func_73046_m().getConfigurationManager().getViewDistance());
    }

    public WorldServer getWorldServer()
    {
        return this.theWorldServer;
    }

    /**
     * updates all the player instances that need to be updated
     */
    public void updatePlayerInstances()
    {
        long var1 = this.theWorldServer.getTotalWorldTime();
        int var3;
        PlayerManager.PlayerInstance var4;

        if (var1 - this.previousTotalWorldTime > 8000L)
        {
            this.previousTotalWorldTime = var1;

            for (var3 = 0; var3 < this.playerInstanceList.size(); ++var3)
            {
                var4 = (PlayerManager.PlayerInstance)this.playerInstanceList.get(var3);
                var4.sendChunkUpdate();
                var4.processChunk();
            }
        }
        else
        {
            for (var3 = 0; var3 < this.chunkWatcherWithPlayers.size(); ++var3)
            {
                var4 = (PlayerManager.PlayerInstance)this.chunkWatcherWithPlayers.get(var3);
                var4.sendChunkUpdate();
            }
        }

        this.chunkWatcherWithPlayers.clear();

        if (this.players.isEmpty())
        {
            WorldProvider ip = this.theWorldServer.provider;

            if (!ip.canRespawnHere())
            {
                this.theWorldServer.theChunkProviderServer.unloadAllChunks();
            }
        }

        if (this.chunkCoordsNotLoaded.size() > 0)
        {
            for (int var22 = 0; var22 < this.players.size(); ++var22)
            {
                EntityPlayerMP player = (EntityPlayerMP)this.players.get(var22);
                int px = player.chunkCoordX;
                int pz = player.chunkCoordZ;
                int maxRadius = this.playerViewRadius + 1;
                int maxRadius2 = maxRadius / 2;
                int maxDistSq = maxRadius * maxRadius + maxRadius2 * maxRadius2;
                int bestDistSq = maxDistSq;
                int bestIndex = -1;
                PlayerManager.PlayerInstance bestCw = null;
                ChunkCoordIntPair bestCoords = null;

                for (int chunk = 0; chunk < this.chunkCoordsNotLoaded.size(); ++chunk)
                {
                    ChunkCoordIntPair coords = (ChunkCoordIntPair)this.chunkCoordsNotLoaded.get(chunk);

                    if (coords != null)
                    {
                        PlayerManager.PlayerInstance cw = this.getOrCreateChunkWatcher(coords.chunkXPos, coords.chunkZPos, false);

                        if (cw != null && !cw.chunkLoaded)
                        {
                            int dx = px - coords.chunkXPos;
                            int dz = pz - coords.chunkZPos;
                            int distSq = dx * dx + dz * dz;

                            if (distSq < bestDistSq)
                            {
                                bestDistSq = distSq;
                                bestIndex = chunk;
                                bestCw = cw;
                                bestCoords = coords;
                            }
                        }
                        else
                        {
                            this.chunkCoordsNotLoaded.set(chunk, (Object)null);
                        }
                    }
                }

                if (bestIndex >= 0)
                {
                    this.chunkCoordsNotLoaded.set(bestIndex, (Object)null);
                }

                if (bestCw != null)
                {
                    bestCw.chunkLoaded = true;
                    this.getWorldServer().theChunkProviderServer.loadChunk(bestCoords.chunkXPos, bestCoords.chunkZPos);
                    bestCw.sendThisChunkToAllPlayers();
                    break;
                }
            }

            this.chunkCoordsNotLoaded.compact();
        }
    }

    public boolean func_152621_a(int p_152621_1_, int p_152621_2_)
    {
        long var3 = (long)p_152621_1_ + 2147483647L | (long)p_152621_2_ + 2147483647L << 32;
        return this.playerInstances.getValueByKey(var3) != null;
    }

    public PlayerManager.PlayerInstance getOrCreateChunkWatcher(int par1, int par2, boolean par3)
    {
        return this.getOrCreateChunkWatcher(par1, par2, par3, false);
    }

    public PlayerManager.PlayerInstance getOrCreateChunkWatcher(int par1, int par2, boolean par3, boolean lazy)
    {
        long var4 = (long)par1 + 2147483647L | (long)par2 + 2147483647L << 32;
        PlayerManager.PlayerInstance var6 = (PlayerManager.PlayerInstance)this.playerInstances.getValueByKey(var4);

        if (var6 == null && par3)
        {
            var6 = new PlayerManager.PlayerInstance(par1, par2, lazy);
            this.playerInstances.add(var4, var6);
            this.playerInstanceList.add(var6);
        }

        return var6;
    }

    public void func_151250_a(int p_151250_1_, int p_151250_2_, int p_151250_3_)
    {
        int var4 = p_151250_1_ >> 4;
        int var5 = p_151250_3_ >> 4;
        PlayerManager.PlayerInstance var6 = this.getOrCreateChunkWatcher(var4, var5, false);

        if (var6 != null)
        {
            var6.func_151253_a(p_151250_1_ & 15, p_151250_2_, p_151250_3_ & 15);
        }
    }

    /**
     * Adds an EntityPlayerMP to the PlayerManager.
     */
    public void addPlayer(EntityPlayerMP par1EntityPlayerMP)
    {
        int var2 = (int)par1EntityPlayerMP.posX >> 4;
        int var3 = (int)par1EntityPlayerMP.posZ >> 4;
        par1EntityPlayerMP.managedPosX = par1EntityPlayerMP.posX;
        par1EntityPlayerMP.managedPosZ = par1EntityPlayerMP.posZ;
        ArrayList spawnList = new ArrayList(1);

        for (int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; ++var4)
        {
            for (int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; ++var5)
            {
                this.getOrCreateChunkWatcher(var4, var5, true).addPlayer(par1EntityPlayerMP);

                if (var4 >= var2 - 1 && var4 <= var2 + 1 && var5 >= var3 - 1 && var5 <= var3 + 1)
                {
                    Chunk spawnChunk = this.getWorldServer().theChunkProviderServer.loadChunk(var4, var5);
                    spawnList.add(spawnChunk);
                }
            }
        }

        par1EntityPlayerMP.playerNetServerHandler.sendPacket(new S26PacketMapChunkBulk(spawnList));
        this.players.add(par1EntityPlayerMP);
        this.filterChunkLoadQueue(par1EntityPlayerMP);
    }

    /**
     * Removes all chunks from the given player's chunk load queue that are not in viewing range of the player.
     */
    public void filterChunkLoadQueue(EntityPlayerMP par1EntityPlayerMP)
    {
        ArrayList var2 = new ArrayList(par1EntityPlayerMP.loadedChunks);
        int var3 = 0;
        int var4 = this.playerViewRadius;
        int var5 = (int)par1EntityPlayerMP.posX >> 4;
        int var6 = (int)par1EntityPlayerMP.posZ >> 4;
        int var7 = 0;
        int var8 = 0;
        ChunkCoordIntPair var9 = this.getOrCreateChunkWatcher(var5, var6, true).chunkLocation;
        par1EntityPlayerMP.loadedChunks.clear();

        if (var2.contains(var9))
        {
            par1EntityPlayerMP.loadedChunks.add(var9);
        }

        int var10;

        for (var10 = 1; var10 <= var4 * 2; ++var10)
        {
            for (int var11 = 0; var11 < 2; ++var11)
            {
                int[] var12 = this.xzDirectionsConst[var3++ % 4];

                for (int var13 = 0; var13 < var10; ++var13)
                {
                    var7 += var12[0];
                    var8 += var12[1];
                    var9 = this.getOrCreateChunkWatcher(var5 + var7, var6 + var8, true).chunkLocation;

                    if (var2.contains(var9))
                    {
                        par1EntityPlayerMP.loadedChunks.add(var9);
                    }
                }
            }
        }

        var3 %= 4;

        for (var10 = 0; var10 < var4 * 2; ++var10)
        {
            var7 += this.xzDirectionsConst[var3][0];
            var8 += this.xzDirectionsConst[var3][1];
            var9 = this.getOrCreateChunkWatcher(var5 + var7, var6 + var8, true).chunkLocation;

            if (var2.contains(var9))
            {
                par1EntityPlayerMP.loadedChunks.add(var9);
            }
        }
    }

    /**
     * Removes an EntityPlayerMP from the PlayerManager.
     */
    public void removePlayer(EntityPlayerMP par1EntityPlayerMP)
    {
        int var2 = (int)par1EntityPlayerMP.managedPosX >> 4;
        int var3 = (int)par1EntityPlayerMP.managedPosZ >> 4;

        for (int var4 = var2 - this.playerViewRadius; var4 <= var2 + this.playerViewRadius; ++var4)
        {
            for (int var5 = var3 - this.playerViewRadius; var5 <= var3 + this.playerViewRadius; ++var5)
            {
                PlayerManager.PlayerInstance var6 = this.getOrCreateChunkWatcher(var4, var5, false);

                if (var6 != null)
                {
                    var6.removePlayer(par1EntityPlayerMP, false);
                }
            }
        }

        this.players.remove(par1EntityPlayerMP);
    }

    /**
     * Determine if two rectangles centered at the given points overlap for the provided radius. Arguments: x1, z1, x2,
     * z2, radius.
     */
    private boolean overlaps(int par1, int par2, int par3, int par4, int par5)
    {
        int var6 = par1 - par3;
        int var7 = par2 - par4;
        return var6 >= -par5 && var6 <= par5 ? var7 >= -par5 && var7 <= par5 : false;
    }

    /**
     * update chunks around a player being moved by server logic (e.g. cart, boat)
     */
    public void updateMountedMovingPlayer(EntityPlayerMP par1EntityPlayerMP)
    {
        int var2 = (int)par1EntityPlayerMP.posX >> 4;
        int var3 = (int)par1EntityPlayerMP.posZ >> 4;
        double var4 = par1EntityPlayerMP.managedPosX - par1EntityPlayerMP.posX;
        double var6 = par1EntityPlayerMP.managedPosZ - par1EntityPlayerMP.posZ;
        double var8 = var4 * var4 + var6 * var6;

        if (var8 >= 64.0D)
        {
            int var10 = (int)par1EntityPlayerMP.managedPosX >> 4;
            int var11 = (int)par1EntityPlayerMP.managedPosZ >> 4;
            int var12 = this.playerViewRadius;
            int var13 = var2 - var10;
            int var14 = var3 - var11;

            if (var13 != 0 || var14 != 0)
            {
                WorldServerOF worldServerOf = null;

                if (this.theWorldServer instanceof WorldServerOF)
                {
                    worldServerOf = (WorldServerOF)this.theWorldServer;
                }

                for (int var15 = var2 - var12; var15 <= var2 + var12; ++var15)
                {
                    for (int var16 = var3 - var12; var16 <= var3 + var12; ++var16)
                    {
                        if (!this.overlaps(var15, var16, var10, var11, var12))
                        {
                            this.getOrCreateChunkWatcher(var15, var16, true, true).addPlayer(par1EntityPlayerMP);

                            if (worldServerOf != null)
                            {
                                worldServerOf.addChunkToTickOnce(var15, var16);
                            }
                        }

                        if (!this.overlaps(var15 - var13, var16 - var14, var2, var3, var12))
                        {
                            PlayerManager.PlayerInstance var17 = this.getOrCreateChunkWatcher(var15 - var13, var16 - var14, false);

                            if (var17 != null)
                            {
                                var17.removePlayer(par1EntityPlayerMP);
                            }
                        }
                    }
                }

                this.filterChunkLoadQueue(par1EntityPlayerMP);
                par1EntityPlayerMP.managedPosX = par1EntityPlayerMP.posX;
                par1EntityPlayerMP.managedPosZ = par1EntityPlayerMP.posZ;
            }
        }
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP par1EntityPlayerMP, int par2, int par3)
    {
        PlayerManager.PlayerInstance var4 = this.getOrCreateChunkWatcher(par2, par3, false);
        return var4 != null && var4.playersWatchingChunk.contains(par1EntityPlayerMP) && !par1EntityPlayerMP.loadedChunks.contains(var4.chunkLocation);
    }

    public void func_152622_a(int p_152622_1_)
    {
        p_152622_1_ = MathHelper.clamp_int(p_152622_1_, 3, 32);

        if (p_152622_1_ != this.playerViewRadius)
        {
            int var2 = p_152622_1_ - this.playerViewRadius;
            Iterator var3 = this.players.iterator();

            while (var3.hasNext())
            {
                EntityPlayerMP var4 = (EntityPlayerMP)var3.next();
                int var5 = (int)var4.posX >> 4;
                int var6 = (int)var4.posZ >> 4;
                int var7;
                int var8;

                if (var2 > 0)
                {
                    for (var7 = var5 - p_152622_1_; var7 <= var5 + p_152622_1_; ++var7)
                    {
                        for (var8 = var6 - p_152622_1_; var8 <= var6 + p_152622_1_; ++var8)
                        {
                            PlayerManager.PlayerInstance var9 = this.getOrCreateChunkWatcher(var7, var8, true, true);

                            if (!var9.playersWatchingChunk.contains(var4))
                            {
                                var9.addPlayer(var4);
                            }
                        }
                    }
                }
                else
                {
                    for (var7 = var5 - this.playerViewRadius; var7 <= var5 + this.playerViewRadius; ++var7)
                    {
                        for (var8 = var6 - this.playerViewRadius; var8 <= var6 + this.playerViewRadius; ++var8)
                        {
                            if (!this.overlaps(var7, var8, var5, var6, p_152622_1_))
                            {
                                this.getOrCreateChunkWatcher(var7, var8, true).removePlayer(var4);
                            }
                        }
                    }
                }
            }

            this.playerViewRadius = p_152622_1_;
        }
    }

    /**
     * Get the furthest viewable block given player's view distance
     */
    public static int getFurthestViewableBlock(int par0)
    {
        return par0 * 16 - 16;
    }

    public class PlayerInstance
    {
        private final List playersWatchingChunk;
        private final ChunkCoordIntPair chunkLocation;
        private short[] field_151254_d;
        private int numberOfTilesToUpdate;
        private int flagsYAreasToUpdate;
        private long previousWorldTime;

        public boolean chunkLoaded;


        public PlayerInstance(int par2, int par3)
        {
            this(par2, par3, false);
        }

        public PlayerInstance(int par2, int par3, boolean lazy)
        {
            this.playersWatchingChunk = new ArrayList();
            this.field_151254_d = new short[64];
            this.chunkLoaded = false;
            this.chunkLocation = new ChunkCoordIntPair(par2, par3);
            boolean useLazy = lazy && Config.isLazyChunkLoading();

            if (useLazy && !PlayerManager.this.getWorldServer().theChunkProviderServer.chunkExists(par2, par3))
            {
                PlayerManager.this.chunkCoordsNotLoaded.add(this.chunkLocation);
                this.chunkLoaded = false;
            }
            else
            {
                PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(par2, par3);
                this.chunkLoaded = true;
            }
        }

        public void addPlayer(EntityPlayerMP par1EntityPlayerMP)
        {
            if (this.playersWatchingChunk.contains(par1EntityPlayerMP))
            {
                PlayerManager.field_152627_a.debug("Failed to add player. {} already is in chunk {}, {}", new Object[] {par1EntityPlayerMP, Integer.valueOf(this.chunkLocation.chunkXPos), Integer.valueOf(this.chunkLocation.chunkZPos)});
            }
            else
            {
                if (this.playersWatchingChunk.isEmpty())
                {
                    this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
                }

                this.playersWatchingChunk.add(par1EntityPlayerMP);
                par1EntityPlayerMP.loadedChunks.add(this.chunkLocation);
            }
        }

        public void removePlayer(EntityPlayerMP par1EntityPlayerMP)
        {
            this.removePlayer(par1EntityPlayerMP, true);
        }

        public void removePlayer(EntityPlayerMP par1EntityPlayerMP, boolean sendData)
        {
            if (this.playersWatchingChunk.contains(par1EntityPlayerMP))
            {
                Chunk var2 = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos);

                if (sendData && var2.func_150802_k())
                {
                    par1EntityPlayerMP.playerNetServerHandler.sendPacket(new S21PacketChunkData(var2, true, 0));
                }

                this.playersWatchingChunk.remove(par1EntityPlayerMP);
                par1EntityPlayerMP.loadedChunks.remove(this.chunkLocation);

                if (Reflector.EventBus.exists())
                {
                    Reflector.postForgeBusEvent(Reflector.ChunkWatchEvent_UnWatch_Constructor, new Object[] {this.chunkLocation, par1EntityPlayerMP});
                }

                if (this.playersWatchingChunk.isEmpty())
                {
                    long var3 = (long)this.chunkLocation.chunkXPos + 2147483647L | (long)this.chunkLocation.chunkZPos + 2147483647L << 32;
                    this.increaseInhabitedTime(var2);
                    PlayerManager.this.playerInstances.remove(var3);
                    PlayerManager.this.playerInstanceList.remove(this);

                    if (this.numberOfTilesToUpdate > 0)
                    {
                        PlayerManager.this.chunkWatcherWithPlayers.remove(this);
                    }

                    if (this.chunkLoaded)
                    {
                        PlayerManager.this.getWorldServer().theChunkProviderServer.unloadChunksIfNotNearSpawn(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos);
                    }
                }
            }
        }

        public void processChunk()
        {
            this.increaseInhabitedTime(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos));
        }

        private void increaseInhabitedTime(Chunk par1Chunk)
        {
            par1Chunk.inhabitedTime += PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime;
            this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
        }

        public void func_151253_a(int p_151253_1_, int p_151253_2_, int p_151253_3_)
        {
            if (this.numberOfTilesToUpdate == 0)
            {
                PlayerManager.this.chunkWatcherWithPlayers.add(this);
            }

            this.flagsYAreasToUpdate |= 1 << (p_151253_2_ >> 4);

            if (this.numberOfTilesToUpdate < 64)
            {
                short var4 = (short)(p_151253_1_ << 12 | p_151253_3_ << 8 | p_151253_2_);

                for (int var5 = 0; var5 < this.numberOfTilesToUpdate; ++var5)
                {
                    if (this.field_151254_d[var5] == var4)
                    {
                        return;
                    }
                }

                this.field_151254_d[this.numberOfTilesToUpdate++] = var4;
            }
        }

        public void func_151251_a(Packet p_151251_1_)
        {
            for (int var2 = 0; var2 < this.playersWatchingChunk.size(); ++var2)
            {
                EntityPlayerMP var3 = (EntityPlayerMP)this.playersWatchingChunk.get(var2);

                if (!var3.loadedChunks.contains(this.chunkLocation))
                {
                    var3.playerNetServerHandler.sendPacket(p_151251_1_);
                }
            }
        }

        public void sendChunkUpdate()
        {
            if (this.numberOfTilesToUpdate != 0)
            {
                int var1;
                int var2;
                int var3;

                if (this.numberOfTilesToUpdate == 1)
                {
                    var1 = this.chunkLocation.chunkXPos * 16 + (this.field_151254_d[0] >> 12 & 15);
                    var2 = this.field_151254_d[0] & 255;
                    var3 = this.chunkLocation.chunkZPos * 16 + (this.field_151254_d[0] >> 8 & 15);
                    this.func_151251_a(new S23PacketBlockChange(var1, var2, var3, PlayerManager.this.theWorldServer));

                    if (ReflectorForge.blockHasTileEntity(PlayerManager.this.theWorldServer, var1, var2, var3))
                    {
                        this.func_151252_a(PlayerManager.this.theWorldServer.getTileEntity(var1, var2, var3));
                    }
                }
                else
                {
                    int var4;

                    if (this.numberOfTilesToUpdate == 64)
                    {
                        var1 = this.chunkLocation.chunkXPos * 16;
                        var2 = this.chunkLocation.chunkZPos * 16;
                        this.func_151251_a(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos), false, this.flagsYAreasToUpdate));

                        for (var3 = 0; var3 < 16; ++var3)
                        {
                            if ((this.flagsYAreasToUpdate & 1 << var3) != 0)
                            {
                                var4 = var3 << 4;
                                List var5 = PlayerManager.this.theWorldServer.func_147486_a(var1, var4, var2, var1 + 16, var4 + 16, var2 + 16);

                                for (int var6 = 0; var6 < var5.size(); ++var6)
                                {
                                    this.func_151252_a((TileEntity)var5.get(var6));
                                }
                            }
                        }
                    }
                    else
                    {
                        this.func_151251_a(new S22PacketMultiBlockChange(this.numberOfTilesToUpdate, this.field_151254_d, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos)));

                        for (var1 = 0; var1 < this.numberOfTilesToUpdate; ++var1)
                        {
                            var2 = this.chunkLocation.chunkXPos * 16 + (this.field_151254_d[var1] >> 12 & 15);
                            var3 = this.field_151254_d[var1] & 255;
                            var4 = this.chunkLocation.chunkZPos * 16 + (this.field_151254_d[var1] >> 8 & 15);

                            if (ReflectorForge.blockHasTileEntity(PlayerManager.this.theWorldServer, var2, var3, var4))
                            {
                                this.func_151252_a(PlayerManager.this.theWorldServer.getTileEntity(var2, var3, var4));
                            }
                        }
                    }
                }

                this.numberOfTilesToUpdate = 0;
                this.flagsYAreasToUpdate = 0;
            }
        }

        private void func_151252_a(TileEntity p_151252_1_)
        {
            if (p_151252_1_ != null)
            {
                Packet var2 = p_151252_1_.getDescriptionPacket();

                if (var2 != null)
                {
                    this.func_151251_a(var2);
                }
            }
        }

        public void sendThisChunkToAllPlayers()
        {
            for (int i = 0; i < this.playersWatchingChunk.size(); ++i)
            {
                EntityPlayerMP player = (EntityPlayerMP)this.playersWatchingChunk.get(i);
                Chunk chunk = PlayerManager.this.getWorldServer().getChunkFromChunkCoords(this.chunkLocation.chunkXPos, this.chunkLocation.chunkZPos);
                ArrayList list = new ArrayList(1);
                list.add(chunk);
                player.playerNetServerHandler.sendPacket(new S26PacketMapChunkBulk(list));
            }
        }
    }
}
