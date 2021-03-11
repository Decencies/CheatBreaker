package net.minecraft.world.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class MapData extends WorldSavedData
{
    public int xCenter;
    public int zCenter;
    public byte dimension;
    public byte scale;

    /** colours */
    public byte[] colors = new byte[16384];

    /**
     * Holds a reference to the MapInfo of the players who own a copy of the map
     */
    public List playersArrayList = new ArrayList();

    /**
     * Holds a reference to the players who own a copy of the map and a reference to their MapInfo
     */
    private Map playersHashMap = new HashMap();
    public Map playersVisibleOnMap = new LinkedHashMap();


    public MapData(String p_i2140_1_)
    {
        super(p_i2140_1_);
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     */
    public void readFromNBT(NBTTagCompound p_76184_1_)
    {
        this.dimension = p_76184_1_.getByte("dimension");
        this.xCenter = p_76184_1_.getInteger("xCenter");
        this.zCenter = p_76184_1_.getInteger("zCenter");
        this.scale = p_76184_1_.getByte("scale");

        if (this.scale < 0)
        {
            this.scale = 0;
        }

        if (this.scale > 4)
        {
            this.scale = 4;
        }

        short var2 = p_76184_1_.getShort("width");
        short var3 = p_76184_1_.getShort("height");

        if (var2 == 128 && var3 == 128)
        {
            this.colors = p_76184_1_.getByteArray("colors");
        }
        else
        {
            byte[] var4 = p_76184_1_.getByteArray("colors");
            this.colors = new byte[16384];
            int var5 = (128 - var2) / 2;
            int var6 = (128 - var3) / 2;

            for (int var7 = 0; var7 < var3; ++var7)
            {
                int var8 = var7 + var6;

                if (var8 >= 0 || var8 < 128)
                {
                    for (int var9 = 0; var9 < var2; ++var9)
                    {
                        int var10 = var9 + var5;

                        if (var10 >= 0 || var10 < 128)
                        {
                            this.colors[var10 + var8 * 128] = var4[var9 + var7 * var2];
                        }
                    }
                }
            }
        }
    }

    /**
     * write data to NBTTagCompound from this MapDataBase, similar to Entities and TileEntities
     */
    public void writeToNBT(NBTTagCompound p_76187_1_)
    {
        p_76187_1_.setByte("dimension", this.dimension);
        p_76187_1_.setInteger("xCenter", this.xCenter);
        p_76187_1_.setInteger("zCenter", this.zCenter);
        p_76187_1_.setByte("scale", this.scale);
        p_76187_1_.setShort("width", (short)128);
        p_76187_1_.setShort("height", (short)128);
        p_76187_1_.setByteArray("colors", this.colors);
    }

    /**
     * Adds the player passed to the list of visible players and checks to see which players are visible
     */
    public void updateVisiblePlayers(EntityPlayer p_76191_1_, ItemStack p_76191_2_)
    {
        if (!this.playersHashMap.containsKey(p_76191_1_))
        {
            MapData.MapInfo var3 = new MapData.MapInfo(p_76191_1_);
            this.playersHashMap.put(p_76191_1_, var3);
            this.playersArrayList.add(var3);
        }

        if (!p_76191_1_.inventory.hasItemStack(p_76191_2_))
        {
            this.playersVisibleOnMap.remove(p_76191_1_.getCommandSenderName());
        }

        for (int var5 = 0; var5 < this.playersArrayList.size(); ++var5)
        {
            MapData.MapInfo var4 = (MapData.MapInfo)this.playersArrayList.get(var5);

            if (!var4.entityplayerObj.isDead && (var4.entityplayerObj.inventory.hasItemStack(p_76191_2_) || p_76191_2_.isOnItemFrame()))
            {
                if (!p_76191_2_.isOnItemFrame() && var4.entityplayerObj.dimension == this.dimension)
                {
                    this.func_82567_a(0, var4.entityplayerObj.worldObj, var4.entityplayerObj.getCommandSenderName(), var4.entityplayerObj.posX, var4.entityplayerObj.posZ, (double)var4.entityplayerObj.rotationYaw);
                }
            }
            else
            {
                this.playersHashMap.remove(var4.entityplayerObj);
                this.playersArrayList.remove(var4);
            }
        }

        if (p_76191_2_.isOnItemFrame())
        {
            this.func_82567_a(1, p_76191_1_.worldObj, "frame-" + p_76191_2_.getItemFrame().getEntityId(), (double)p_76191_2_.getItemFrame().field_146063_b, (double)p_76191_2_.getItemFrame().field_146062_d, (double)(p_76191_2_.getItemFrame().hangingDirection * 90));
        }
    }

    private void func_82567_a(int p_82567_1_, World p_82567_2_, String p_82567_3_, double p_82567_4_, double p_82567_6_, double p_82567_8_)
    {
        int var10 = 1 << this.scale;
        float var11 = (float)(p_82567_4_ - (double)this.xCenter) / (float)var10;
        float var12 = (float)(p_82567_6_ - (double)this.zCenter) / (float)var10;
        byte var13 = (byte)((int)((double)(var11 * 2.0F) + 0.5D));
        byte var14 = (byte)((int)((double)(var12 * 2.0F) + 0.5D));
        byte var16 = 63;
        byte var15;

        if (var11 >= (float)(-var16) && var12 >= (float)(-var16) && var11 <= (float)var16 && var12 <= (float)var16)
        {
            p_82567_8_ += p_82567_8_ < 0.0D ? -8.0D : 8.0D;
            var15 = (byte)((int)(p_82567_8_ * 16.0D / 360.0D));

            if (this.dimension < 0)
            {
                int var17 = (int)(p_82567_2_.getWorldInfo().getWorldTime() / 10L);
                var15 = (byte)(var17 * var17 * 34187121 + var17 * 121 >> 15 & 15);
            }
        }
        else
        {
            if (Math.abs(var11) >= 320.0F || Math.abs(var12) >= 320.0F)
            {
                this.playersVisibleOnMap.remove(p_82567_3_);
                return;
            }

            p_82567_1_ = 6;
            var15 = 0;

            if (var11 <= (float)(-var16))
            {
                var13 = (byte)((int)((double)(var16 * 2) + 2.5D));
            }

            if (var12 <= (float)(-var16))
            {
                var14 = (byte)((int)((double)(var16 * 2) + 2.5D));
            }

            if (var11 >= (float)var16)
            {
                var13 = (byte)(var16 * 2 + 1);
            }

            if (var12 >= (float)var16)
            {
                var14 = (byte)(var16 * 2 + 1);
            }
        }

        this.playersVisibleOnMap.put(p_82567_3_, new MapData.MapCoord((byte)p_82567_1_, var13, var14, var15));
    }

    /**
     * Get byte array of packet data to send to players on map for updating map data
     */
    public byte[] getUpdatePacketData(ItemStack p_76193_1_, World p_76193_2_, EntityPlayer p_76193_3_)
    {
        MapData.MapInfo var4 = (MapData.MapInfo)this.playersHashMap.get(p_76193_3_);
        return var4 == null ? null : var4.getPlayersOnMap(p_76193_1_);
    }

    /**
     * Marks a vertical range of pixels as being modified so they will be resent to clients. Parameters: X, lowest Y,
     * highest Y
     */
    public void setColumnDirty(int p_76194_1_, int p_76194_2_, int p_76194_3_)
    {
        super.markDirty();

        for (int var4 = 0; var4 < this.playersArrayList.size(); ++var4)
        {
            MapData.MapInfo var5 = (MapData.MapInfo)this.playersArrayList.get(var4);

            if (var5.field_76209_b[p_76194_1_] < 0 || var5.field_76209_b[p_76194_1_] > p_76194_2_)
            {
                var5.field_76209_b[p_76194_1_] = p_76194_2_;
            }

            if (var5.field_76210_c[p_76194_1_] < 0 || var5.field_76210_c[p_76194_1_] < p_76194_3_)
            {
                var5.field_76210_c[p_76194_1_] = p_76194_3_;
            }
        }
    }

    /**
     * Updates the com.cheatbreaker.client's map with information from other players in MP
     */
    public void updateMPMapData(byte[] p_76192_1_)
    {
        int var2;

        if (p_76192_1_[0] == 0)
        {
            var2 = p_76192_1_[1] & 255;
            int var3 = p_76192_1_[2] & 255;

            for (int var4 = 0; var4 < p_76192_1_.length - 3; ++var4)
            {
                this.colors[(var4 + var3) * 128 + var2] = p_76192_1_[var4 + 3];
            }

            this.markDirty();
        }
        else if (p_76192_1_[0] == 1)
        {
            this.playersVisibleOnMap.clear();

            for (var2 = 0; var2 < (p_76192_1_.length - 1) / 3; ++var2)
            {
                byte var7 = (byte)(p_76192_1_[var2 * 3 + 1] >> 4);
                byte var8 = p_76192_1_[var2 * 3 + 2];
                byte var5 = p_76192_1_[var2 * 3 + 3];
                byte var6 = (byte)(p_76192_1_[var2 * 3 + 1] & 15);
                this.playersVisibleOnMap.put("icon-" + var2, new MapData.MapCoord(var7, var8, var5, var6));
            }
        }
        else if (p_76192_1_[0] == 2)
        {
            this.scale = p_76192_1_[1];
        }
    }

    public MapData.MapInfo func_82568_a(EntityPlayer p_82568_1_)
    {
        MapData.MapInfo var2 = (MapData.MapInfo)this.playersHashMap.get(p_82568_1_);

        if (var2 == null)
        {
            var2 = new MapData.MapInfo(p_82568_1_);
            this.playersHashMap.put(p_82568_1_, var2);
            this.playersArrayList.add(var2);
        }

        return var2;
    }

    public class MapCoord
    {
        public byte iconSize;
        public byte centerX;
        public byte centerZ;
        public byte iconRotation;


        public MapCoord(byte p_i2139_2_, byte p_i2139_3_, byte p_i2139_4_, byte p_i2139_5_)
        {
            this.iconSize = p_i2139_2_;
            this.centerX = p_i2139_3_;
            this.centerZ = p_i2139_4_;
            this.iconRotation = p_i2139_5_;
        }
    }

    public class MapInfo
    {
        public final EntityPlayer entityplayerObj;
        public int[] field_76209_b = new int[128];
        public int[] field_76210_c = new int[128];
        private int currentRandomNumber;
        private int ticksUntilPlayerLocationMapUpdate;
        private byte[] lastPlayerLocationOnMap;
        public int field_82569_d;
        private boolean field_82570_i;


        public MapInfo(EntityPlayer p_i2138_2_)
        {
            this.entityplayerObj = p_i2138_2_;

            for (int var3 = 0; var3 < this.field_76209_b.length; ++var3)
            {
                this.field_76209_b[var3] = 0;
                this.field_76210_c[var3] = 127;
            }
        }

        public byte[] getPlayersOnMap(ItemStack p_76204_1_)
        {
            byte[] var2;

            if (!this.field_82570_i)
            {
                var2 = new byte[] {(byte)2, MapData.this.scale};
                this.field_82570_i = true;
                return var2;
            }
            else
            {
                int var3;
                int var11;

                if (--this.ticksUntilPlayerLocationMapUpdate < 0)
                {
                    this.ticksUntilPlayerLocationMapUpdate = 4;
                    var2 = new byte[MapData.this.playersVisibleOnMap.size() * 3 + 1];
                    var2[0] = 1;
                    var3 = 0;

                    for (Iterator var4 = MapData.this.playersVisibleOnMap.values().iterator(); var4.hasNext(); ++var3)
                    {
                        MapData.MapCoord var5 = (MapData.MapCoord)var4.next();
                        var2[var3 * 3 + 1] = (byte)(var5.iconSize << 4 | var5.iconRotation & 15);
                        var2[var3 * 3 + 2] = var5.centerX;
                        var2[var3 * 3 + 3] = var5.centerZ;
                    }

                    boolean var9 = !p_76204_1_.isOnItemFrame();

                    if (this.lastPlayerLocationOnMap != null && this.lastPlayerLocationOnMap.length == var2.length)
                    {
                        for (var11 = 0; var11 < var2.length; ++var11)
                        {
                            if (var2[var11] != this.lastPlayerLocationOnMap[var11])
                            {
                                var9 = false;
                                break;
                            }
                        }
                    }
                    else
                    {
                        var9 = false;
                    }

                    if (!var9)
                    {
                        this.lastPlayerLocationOnMap = var2;
                        return var2;
                    }
                }

                for (int var8 = 0; var8 < 1; ++var8)
                {
                    var3 = this.currentRandomNumber++ * 11 % 128;

                    if (this.field_76209_b[var3] >= 0)
                    {
                        int var10 = this.field_76210_c[var3] - this.field_76209_b[var3] + 1;
                        var11 = this.field_76209_b[var3];
                        byte[] var6 = new byte[var10 + 3];
                        var6[0] = 0;
                        var6[1] = (byte)var3;
                        var6[2] = (byte)var11;

                        for (int var7 = 0; var7 < var6.length - 3; ++var7)
                        {
                            var6[var7 + 3] = MapData.this.colors[(var7 + var11) * 128 + var3];
                        }

                        this.field_76210_c[var3] = -1;
                        this.field_76209_b[var3] = -1;
                        return var6;
                    }
                }

                return null;
            }
        }
    }
}
