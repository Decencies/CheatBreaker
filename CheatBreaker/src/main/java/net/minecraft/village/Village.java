package net.minecraft.village;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Village
{
    private World worldObj;

    /** list of VillageDoorInfo objects */
    private final List villageDoorInfoList = new ArrayList();

    /**
     * This is the sum of all door coordinates and used to calculate the actual village center by dividing by the number
     * of doors.
     */
    private final ChunkCoordinates centerHelper = new ChunkCoordinates(0, 0, 0);

    /** This is the actual village center. */
    private final ChunkCoordinates center = new ChunkCoordinates(0, 0, 0);
    private int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numVillagers;

    /** Timestamp of tick count when villager last bred */
    private int noBreedTicks;

    /** List of player reputations with this village */
    private TreeMap playerReputation = new TreeMap();
    private List villageAgressors = new ArrayList();
    private int numIronGolems;


    public Village() {}

    public Village(World p_i1675_1_)
    {
        this.worldObj = p_i1675_1_;
    }

    public void func_82691_a(World p_82691_1_)
    {
        this.worldObj = p_82691_1_;
    }

    /**
     * Called periodically by VillageCollection
     */
    public void tick(int p_75560_1_)
    {
        this.tickCounter = p_75560_1_;
        this.removeDeadAndOutOfRangeDoors();
        this.removeDeadAndOldAgressors();

        if (p_75560_1_ % 20 == 0)
        {
            this.updateNumVillagers();
        }

        if (p_75560_1_ % 30 == 0)
        {
            this.updateNumIronGolems();
        }

        int var2 = this.numVillagers / 10;

        if (this.numIronGolems < var2 && this.villageDoorInfoList.size() > 20 && this.worldObj.rand.nextInt(7000) == 0)
        {
            Vec3 var3 = this.tryGetIronGolemSpawningLocation(MathHelper.floor_float((float)this.center.posX), MathHelper.floor_float((float)this.center.posY), MathHelper.floor_float((float)this.center.posZ), 2, 4, 2);

            if (var3 != null)
            {
                EntityIronGolem var4 = new EntityIronGolem(this.worldObj);
                var4.setPosition(var3.xCoord, var3.yCoord, var3.zCoord);
                this.worldObj.spawnEntityInWorld(var4);
                ++this.numIronGolems;
            }
        }
    }

    /**
     * Tries up to 10 times to get a valid spawning location before eventually failing and returning null.
     */
    private Vec3 tryGetIronGolemSpawningLocation(int p_75559_1_, int p_75559_2_, int p_75559_3_, int p_75559_4_, int p_75559_5_, int p_75559_6_)
    {
        for (int var7 = 0; var7 < 10; ++var7)
        {
            int var8 = p_75559_1_ + this.worldObj.rand.nextInt(16) - 8;
            int var9 = p_75559_2_ + this.worldObj.rand.nextInt(6) - 3;
            int var10 = p_75559_3_ + this.worldObj.rand.nextInt(16) - 8;

            if (this.isInRange(var8, var9, var10) && this.isValidIronGolemSpawningLocation(var8, var9, var10, p_75559_4_, p_75559_5_, p_75559_6_))
            {
                return Vec3.createVectorHelper((double)var8, (double)var9, (double)var10);
            }
        }

        return null;
    }

    private boolean isValidIronGolemSpawningLocation(int p_75563_1_, int p_75563_2_, int p_75563_3_, int p_75563_4_, int p_75563_5_, int p_75563_6_)
    {
        if (!World.doesBlockHaveSolidTopSurface(this.worldObj, p_75563_1_, p_75563_2_ - 1, p_75563_3_))
        {
            return false;
        }
        else
        {
            int var7 = p_75563_1_ - p_75563_4_ / 2;
            int var8 = p_75563_3_ - p_75563_6_ / 2;

            for (int var9 = var7; var9 < var7 + p_75563_4_; ++var9)
            {
                for (int var10 = p_75563_2_; var10 < p_75563_2_ + p_75563_5_; ++var10)
                {
                    for (int var11 = var8; var11 < var8 + p_75563_6_; ++var11)
                    {
                        if (this.worldObj.getBlock(var9, var10, var11).isNormalCube())
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private void updateNumIronGolems()
    {
        List var1 = this.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, AxisAlignedBB.getBoundingBox((double)(this.center.posX - this.villageRadius), (double)(this.center.posY - 4), (double)(this.center.posZ - this.villageRadius), (double)(this.center.posX + this.villageRadius), (double)(this.center.posY + 4), (double)(this.center.posZ + this.villageRadius)));
        this.numIronGolems = var1.size();
    }

    private void updateNumVillagers()
    {
        List var1 = this.worldObj.getEntitiesWithinAABB(EntityVillager.class, AxisAlignedBB.getBoundingBox((double)(this.center.posX - this.villageRadius), (double)(this.center.posY - 4), (double)(this.center.posZ - this.villageRadius), (double)(this.center.posX + this.villageRadius), (double)(this.center.posY + 4), (double)(this.center.posZ + this.villageRadius)));
        this.numVillagers = var1.size();

        if (this.numVillagers == 0)
        {
            this.playerReputation.clear();
        }
    }

    public ChunkCoordinates getCenter()
    {
        return this.center;
    }

    public int getVillageRadius()
    {
        return this.villageRadius;
    }

    /**
     * Actually get num village door info entries, but that boils down to number of doors. Called by
     * EntityAIVillagerMate and VillageSiege
     */
    public int getNumVillageDoors()
    {
        return this.villageDoorInfoList.size();
    }

    public int getTicksSinceLastDoorAdding()
    {
        return this.tickCounter - this.lastAddDoorTimestamp;
    }

    public int getNumVillagers()
    {
        return this.numVillagers;
    }

    /**
     * Returns true, if the given coordinates are within the bounding box of the village.
     */
    public boolean isInRange(int p_75570_1_, int p_75570_2_, int p_75570_3_)
    {
        return this.center.getDistanceSquared(p_75570_1_, p_75570_2_, p_75570_3_) < (float)(this.villageRadius * this.villageRadius);
    }

    /**
     * called only by class EntityAIMoveThroughVillage
     */
    public List getVillageDoorInfoList()
    {
        return this.villageDoorInfoList;
    }

    public VillageDoorInfo findNearestDoor(int p_75564_1_, int p_75564_2_, int p_75564_3_)
    {
        VillageDoorInfo var4 = null;
        int var5 = Integer.MAX_VALUE;
        Iterator var6 = this.villageDoorInfoList.iterator();

        while (var6.hasNext())
        {
            VillageDoorInfo var7 = (VillageDoorInfo)var6.next();
            int var8 = var7.getDistanceSquared(p_75564_1_, p_75564_2_, p_75564_3_);

            if (var8 < var5)
            {
                var4 = var7;
                var5 = var8;
            }
        }

        return var4;
    }

    /**
     * Find a door suitable for shelter. If there are more doors in a distance of 16 blocks, then the least restricted
     * one (i.e. the one protecting the lowest number of villagers) of them is chosen, else the nearest one regardless
     * of restriction.
     */
    public VillageDoorInfo findNearestDoorUnrestricted(int p_75569_1_, int p_75569_2_, int p_75569_3_)
    {
        VillageDoorInfo var4 = null;
        int var5 = Integer.MAX_VALUE;
        Iterator var6 = this.villageDoorInfoList.iterator();

        while (var6.hasNext())
        {
            VillageDoorInfo var7 = (VillageDoorInfo)var6.next();
            int var8 = var7.getDistanceSquared(p_75569_1_, p_75569_2_, p_75569_3_);

            if (var8 > 256)
            {
                var8 *= 1000;
            }
            else
            {
                var8 = var7.getDoorOpeningRestrictionCounter();
            }

            if (var8 < var5)
            {
                var4 = var7;
                var5 = var8;
            }
        }

        return var4;
    }

    public VillageDoorInfo getVillageDoorAt(int p_75578_1_, int p_75578_2_, int p_75578_3_)
    {
        if (this.center.getDistanceSquared(p_75578_1_, p_75578_2_, p_75578_3_) > (float)(this.villageRadius * this.villageRadius))
        {
            return null;
        }
        else
        {
            Iterator var4 = this.villageDoorInfoList.iterator();
            VillageDoorInfo var5;

            do
            {
                if (!var4.hasNext())
                {
                    return null;
                }

                var5 = (VillageDoorInfo)var4.next();
            }
            while (var5.posX != p_75578_1_ || var5.posZ != p_75578_3_ || Math.abs(var5.posY - p_75578_2_) > 1);

            return var5;
        }
    }

    public void addVillageDoorInfo(VillageDoorInfo p_75576_1_)
    {
        this.villageDoorInfoList.add(p_75576_1_);
        this.centerHelper.posX += p_75576_1_.posX;
        this.centerHelper.posY += p_75576_1_.posY;
        this.centerHelper.posZ += p_75576_1_.posZ;
        this.updateVillageRadiusAndCenter();
        this.lastAddDoorTimestamp = p_75576_1_.lastActivityTimestamp;
    }

    /**
     * Returns true, if there is not a single village door left. Called by VillageCollection
     */
    public boolean isAnnihilated()
    {
        return this.villageDoorInfoList.isEmpty();
    }

    public void addOrRenewAgressor(EntityLivingBase p_75575_1_)
    {
        Iterator var2 = this.villageAgressors.iterator();
        Village.VillageAgressor var3;

        do
        {
            if (!var2.hasNext())
            {
                this.villageAgressors.add(new Village.VillageAgressor(p_75575_1_, this.tickCounter));
                return;
            }

            var3 = (Village.VillageAgressor)var2.next();
        }
        while (var3.agressor != p_75575_1_);

        var3.agressionTime = this.tickCounter;
    }

    public EntityLivingBase findNearestVillageAggressor(EntityLivingBase p_75571_1_)
    {
        double var2 = Double.MAX_VALUE;
        Village.VillageAgressor var4 = null;

        for (int var5 = 0; var5 < this.villageAgressors.size(); ++var5)
        {
            Village.VillageAgressor var6 = (Village.VillageAgressor)this.villageAgressors.get(var5);
            double var7 = var6.agressor.getDistanceSqToEntity(p_75571_1_);

            if (var7 <= var2)
            {
                var4 = var6;
                var2 = var7;
            }
        }

        return var4 != null ? var4.agressor : null;
    }

    public EntityPlayer func_82685_c(EntityLivingBase p_82685_1_)
    {
        double var2 = Double.MAX_VALUE;
        EntityPlayer var4 = null;
        Iterator var5 = this.playerReputation.keySet().iterator();

        while (var5.hasNext())
        {
            String var6 = (String)var5.next();

            if (this.isPlayerReputationTooLow(var6))
            {
                EntityPlayer var7 = this.worldObj.getPlayerEntityByName(var6);

                if (var7 != null)
                {
                    double var8 = var7.getDistanceSqToEntity(p_82685_1_);

                    if (var8 <= var2)
                    {
                        var4 = var7;
                        var2 = var8;
                    }
                }
            }
        }

        return var4;
    }

    private void removeDeadAndOldAgressors()
    {
        Iterator var1 = this.villageAgressors.iterator();

        while (var1.hasNext())
        {
            Village.VillageAgressor var2 = (Village.VillageAgressor)var1.next();

            if (!var2.agressor.isEntityAlive() || Math.abs(this.tickCounter - var2.agressionTime) > 300)
            {
                var1.remove();
            }
        }
    }

    private void removeDeadAndOutOfRangeDoors()
    {
        boolean var1 = false;
        boolean var2 = this.worldObj.rand.nextInt(50) == 0;
        Iterator var3 = this.villageDoorInfoList.iterator();

        while (var3.hasNext())
        {
            VillageDoorInfo var4 = (VillageDoorInfo)var3.next();

            if (var2)
            {
                var4.resetDoorOpeningRestrictionCounter();
            }

            if (!this.isBlockDoor(var4.posX, var4.posY, var4.posZ) || Math.abs(this.tickCounter - var4.lastActivityTimestamp) > 1200)
            {
                this.centerHelper.posX -= var4.posX;
                this.centerHelper.posY -= var4.posY;
                this.centerHelper.posZ -= var4.posZ;
                var1 = true;
                var4.isDetachedFromVillageFlag = true;
                var3.remove();
            }
        }

        if (var1)
        {
            this.updateVillageRadiusAndCenter();
        }
    }

    private boolean isBlockDoor(int p_75574_1_, int p_75574_2_, int p_75574_3_)
    {
        return this.worldObj.getBlock(p_75574_1_, p_75574_2_, p_75574_3_) == Blocks.wooden_door;
    }

    private void updateVillageRadiusAndCenter()
    {
        int var1 = this.villageDoorInfoList.size();

        if (var1 == 0)
        {
            this.center.set(0, 0, 0);
            this.villageRadius = 0;
        }
        else
        {
            this.center.set(this.centerHelper.posX / var1, this.centerHelper.posY / var1, this.centerHelper.posZ / var1);
            int var2 = 0;
            VillageDoorInfo var4;

            for (Iterator var3 = this.villageDoorInfoList.iterator(); var3.hasNext(); var2 = Math.max(var4.getDistanceSquared(this.center.posX, this.center.posY, this.center.posZ), var2))
            {
                var4 = (VillageDoorInfo)var3.next();
            }

            this.villageRadius = Math.max(32, (int)Math.sqrt((double)var2) + 1);
        }
    }

    /**
     * Return the village reputation for a player
     */
    public int getReputationForPlayer(String p_82684_1_)
    {
        Integer var2 = (Integer)this.playerReputation.get(p_82684_1_);
        return var2 != null ? var2.intValue() : 0;
    }

    /**
     * Set the village reputation for a player.
     */
    public int setReputationForPlayer(String p_82688_1_, int p_82688_2_)
    {
        int var3 = this.getReputationForPlayer(p_82688_1_);
        int var4 = MathHelper.clamp_int(var3 + p_82688_2_, -30, 10);
        this.playerReputation.put(p_82688_1_, Integer.valueOf(var4));
        return var4;
    }

    /**
     * Return whether this player has a too low reputation with this village.
     */
    public boolean isPlayerReputationTooLow(String p_82687_1_)
    {
        return this.getReputationForPlayer(p_82687_1_) <= -15;
    }

    /**
     * Read this village's data from NBT.
     */
    public void readVillageDataFromNBT(NBTTagCompound p_82690_1_)
    {
        this.numVillagers = p_82690_1_.getInteger("PopSize");
        this.villageRadius = p_82690_1_.getInteger("Radius");
        this.numIronGolems = p_82690_1_.getInteger("Golems");
        this.lastAddDoorTimestamp = p_82690_1_.getInteger("Stable");
        this.tickCounter = p_82690_1_.getInteger("Tick");
        this.noBreedTicks = p_82690_1_.getInteger("MTick");
        this.center.posX = p_82690_1_.getInteger("CX");
        this.center.posY = p_82690_1_.getInteger("CY");
        this.center.posZ = p_82690_1_.getInteger("CZ");
        this.centerHelper.posX = p_82690_1_.getInteger("ACX");
        this.centerHelper.posY = p_82690_1_.getInteger("ACY");
        this.centerHelper.posZ = p_82690_1_.getInteger("ACZ");
        NBTTagList var2 = p_82690_1_.getTagList("Doors", 10);

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            VillageDoorInfo var5 = new VillageDoorInfo(var4.getInteger("X"), var4.getInteger("Y"), var4.getInteger("Z"), var4.getInteger("IDX"), var4.getInteger("IDZ"), var4.getInteger("TS"));
            this.villageDoorInfoList.add(var5);
        }

        NBTTagList var6 = p_82690_1_.getTagList("Players", 10);

        for (int var7 = 0; var7 < var6.tagCount(); ++var7)
        {
            NBTTagCompound var8 = var6.getCompoundTagAt(var7);
            this.playerReputation.put(var8.getString("Name"), Integer.valueOf(var8.getInteger("S")));
        }
    }

    /**
     * Write this village's data to NBT.
     */
    public void writeVillageDataToNBT(NBTTagCompound p_82689_1_)
    {
        p_82689_1_.setInteger("PopSize", this.numVillagers);
        p_82689_1_.setInteger("Radius", this.villageRadius);
        p_82689_1_.setInteger("Golems", this.numIronGolems);
        p_82689_1_.setInteger("Stable", this.lastAddDoorTimestamp);
        p_82689_1_.setInteger("Tick", this.tickCounter);
        p_82689_1_.setInteger("MTick", this.noBreedTicks);
        p_82689_1_.setInteger("CX", this.center.posX);
        p_82689_1_.setInteger("CY", this.center.posY);
        p_82689_1_.setInteger("CZ", this.center.posZ);
        p_82689_1_.setInteger("ACX", this.centerHelper.posX);
        p_82689_1_.setInteger("ACY", this.centerHelper.posY);
        p_82689_1_.setInteger("ACZ", this.centerHelper.posZ);
        NBTTagList var2 = new NBTTagList();
        Iterator var3 = this.villageDoorInfoList.iterator();

        while (var3.hasNext())
        {
            VillageDoorInfo var4 = (VillageDoorInfo)var3.next();
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setInteger("X", var4.posX);
            var5.setInteger("Y", var4.posY);
            var5.setInteger("Z", var4.posZ);
            var5.setInteger("IDX", var4.insideDirectionX);
            var5.setInteger("IDZ", var4.insideDirectionZ);
            var5.setInteger("TS", var4.lastActivityTimestamp);
            var2.appendTag(var5);
        }

        p_82689_1_.setTag("Doors", var2);
        NBTTagList var7 = new NBTTagList();
        Iterator var8 = this.playerReputation.keySet().iterator();

        while (var8.hasNext())
        {
            String var9 = (String)var8.next();
            NBTTagCompound var6 = new NBTTagCompound();
            var6.setString("Name", var9);
            var6.setInteger("S", ((Integer)this.playerReputation.get(var9)).intValue());
            var7.appendTag(var6);
        }

        p_82689_1_.setTag("Players", var7);
    }

    /**
     * Prevent villager breeding for a fixed interval of time
     */
    public void endMatingSeason()
    {
        this.noBreedTicks = this.tickCounter;
    }

    /**
     * Return whether villagers mating refractory period has passed
     */
    public boolean isMatingSeason()
    {
        return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
    }

    public void setDefaultPlayerReputation(int p_82683_1_)
    {
        Iterator var2 = this.playerReputation.keySet().iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();
            this.setReputationForPlayer(var3, p_82683_1_);
        }
    }

    class VillageAgressor
    {
        public EntityLivingBase agressor;
        public int agressionTime;


        VillageAgressor(EntityLivingBase p_i1674_2_, int p_i1674_3_)
        {
            this.agressor = p_i1674_2_;
            this.agressionTime = p_i1674_3_;
        }
    }
}
