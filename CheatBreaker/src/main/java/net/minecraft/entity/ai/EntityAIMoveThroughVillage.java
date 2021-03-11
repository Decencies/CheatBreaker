package net.minecraft.entity.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveThroughVillage extends EntityAIBase
{
    private EntityCreature theEntity;
    private double movementSpeed;

    /** The PathNavigate of our entity. */
    private PathEntity entityPathNavigate;
    private VillageDoorInfo doorInfo;
    private boolean isNocturnal;
    private List doorList = new ArrayList();


    public EntityAIMoveThroughVillage(EntityCreature p_i1638_1_, double p_i1638_2_, boolean p_i1638_4_)
    {
        this.theEntity = p_i1638_1_;
        this.movementSpeed = p_i1638_2_;
        this.isNocturnal = p_i1638_4_;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        this.func_75414_f();

        if (this.isNocturnal && this.theEntity.worldObj.isDaytime())
        {
            return false;
        }
        else
        {
            Village var1 = this.theEntity.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.theEntity.posX), MathHelper.floor_double(this.theEntity.posY), MathHelper.floor_double(this.theEntity.posZ), 0);

            if (var1 == null)
            {
                return false;
            }
            else
            {
                this.doorInfo = this.func_75412_a(var1);

                if (this.doorInfo == null)
                {
                    return false;
                }
                else
                {
                    boolean var2 = this.theEntity.getNavigator().getCanBreakDoors();
                    this.theEntity.getNavigator().setBreakDoors(false);
                    this.entityPathNavigate = this.theEntity.getNavigator().getPathToXYZ((double)this.doorInfo.posX, (double)this.doorInfo.posY, (double)this.doorInfo.posZ);
                    this.theEntity.getNavigator().setBreakDoors(var2);

                    if (this.entityPathNavigate != null)
                    {
                        return true;
                    }
                    else
                    {
                        Vec3 var3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 10, 7, Vec3.createVectorHelper((double)this.doorInfo.posX, (double)this.doorInfo.posY, (double)this.doorInfo.posZ));

                        if (var3 == null)
                        {
                            return false;
                        }
                        else
                        {
                            this.theEntity.getNavigator().setBreakDoors(false);
                            this.entityPathNavigate = this.theEntity.getNavigator().getPathToXYZ(var3.xCoord, var3.yCoord, var3.zCoord);
                            this.theEntity.getNavigator().setBreakDoors(var2);
                            return this.entityPathNavigate != null;
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (this.theEntity.getNavigator().noPath())
        {
            return false;
        }
        else
        {
            float var1 = this.theEntity.width + 4.0F;
            return this.theEntity.getDistanceSq((double)this.doorInfo.posX, (double)this.doorInfo.posY, (double)this.doorInfo.posZ) > (double)(var1 * var1);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.theEntity.getNavigator().setPath(this.entityPathNavigate, this.movementSpeed);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        if (this.theEntity.getNavigator().noPath() || this.theEntity.getDistanceSq((double)this.doorInfo.posX, (double)this.doorInfo.posY, (double)this.doorInfo.posZ) < 16.0D)
        {
            this.doorList.add(this.doorInfo);
        }
    }

    private VillageDoorInfo func_75412_a(Village p_75412_1_)
    {
        VillageDoorInfo var2 = null;
        int var3 = Integer.MAX_VALUE;
        List var4 = p_75412_1_.getVillageDoorInfoList();
        Iterator var5 = var4.iterator();

        while (var5.hasNext())
        {
            VillageDoorInfo var6 = (VillageDoorInfo)var5.next();
            int var7 = var6.getDistanceSquared(MathHelper.floor_double(this.theEntity.posX), MathHelper.floor_double(this.theEntity.posY), MathHelper.floor_double(this.theEntity.posZ));

            if (var7 < var3 && !this.func_75413_a(var6))
            {
                var2 = var6;
                var3 = var7;
            }
        }

        return var2;
    }

    private boolean func_75413_a(VillageDoorInfo p_75413_1_)
    {
        Iterator var2 = this.doorList.iterator();
        VillageDoorInfo var3;

        do
        {
            if (!var2.hasNext())
            {
                return false;
            }

            var3 = (VillageDoorInfo)var2.next();
        }
        while (p_75413_1_.posX != var3.posX || p_75413_1_.posY != var3.posY || p_75413_1_.posZ != var3.posZ);

        return true;
    }

    private void func_75414_f()
    {
        if (this.doorList.size() > 15)
        {
            this.doorList.remove(0);
        }
    }
}
