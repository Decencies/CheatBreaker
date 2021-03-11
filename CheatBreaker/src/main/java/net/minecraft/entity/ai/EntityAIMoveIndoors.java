package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveIndoors extends EntityAIBase
{
    private EntityCreature entityObj;
    private VillageDoorInfo doorInfo;
    private int insidePosX = -1;
    private int insidePosZ = -1;


    public EntityAIMoveIndoors(EntityCreature p_i1637_1_)
    {
        this.entityObj = p_i1637_1_;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        int var1 = MathHelper.floor_double(this.entityObj.posX);
        int var2 = MathHelper.floor_double(this.entityObj.posY);
        int var3 = MathHelper.floor_double(this.entityObj.posZ);

        if ((!this.entityObj.worldObj.isDaytime() || this.entityObj.worldObj.isRaining() || !this.entityObj.worldObj.getBiomeGenForCoords(var1, var3).canSpawnLightningBolt()) && !this.entityObj.worldObj.provider.hasNoSky)
        {
            if (this.entityObj.getRNG().nextInt(50) != 0)
            {
                return false;
            }
            else if (this.insidePosX != -1 && this.entityObj.getDistanceSq((double)this.insidePosX, this.entityObj.posY, (double)this.insidePosZ) < 4.0D)
            {
                return false;
            }
            else
            {
                Village var4 = this.entityObj.worldObj.villageCollectionObj.findNearestVillage(var1, var2, var3, 14);

                if (var4 == null)
                {
                    return false;
                }
                else
                {
                    this.doorInfo = var4.findNearestDoorUnrestricted(var1, var2, var3);
                    return this.doorInfo != null;
                }
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.entityObj.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.insidePosX = -1;

        if (this.entityObj.getDistanceSq((double)this.doorInfo.getInsidePosX(), (double)this.doorInfo.posY, (double)this.doorInfo.getInsidePosZ()) > 256.0D)
        {
            Vec3 var1 = RandomPositionGenerator.findRandomTargetBlockTowards(this.entityObj, 14, 3, Vec3.createVectorHelper((double)this.doorInfo.getInsidePosX() + 0.5D, (double)this.doorInfo.getInsidePosY(), (double)this.doorInfo.getInsidePosZ() + 0.5D));

            if (var1 != null)
            {
                this.entityObj.getNavigator().tryMoveToXYZ(var1.xCoord, var1.yCoord, var1.zCoord, 1.0D);
            }
        }
        else
        {
            this.entityObj.getNavigator().tryMoveToXYZ((double)this.doorInfo.getInsidePosX() + 0.5D, (double)this.doorInfo.getInsidePosY(), (double)this.doorInfo.getInsidePosZ() + 0.5D, 1.0D);
        }
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.insidePosX = this.doorInfo.getInsidePosX();
        this.insidePosZ = this.doorInfo.getInsidePosZ();
        this.doorInfo = null;
    }
}
