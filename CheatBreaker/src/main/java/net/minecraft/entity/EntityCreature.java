package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityCreature extends EntityLiving
{
    public static final UUID field_110179_h = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
    public static final AttributeModifier field_110181_i = (new AttributeModifier(field_110179_h, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
    private PathEntity pathToEntity;

    /** The Entity this EntityCreature is set to attack. */
    protected Entity entityToAttack;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;

    /** Used to make a creature speed up and wander away when hit. */
    protected int fleeingTick;
    private ChunkCoordinates homePosition = new ChunkCoordinates(0, 0, 0);

    /** If -1 there is no maximum distance */
    private float maximumHomeDistance = -1.0F;
    private EntityAIBase field_110178_bs = new EntityAIMoveTowardsRestriction(this, 1.0D);
    private boolean field_110180_bt;


    public EntityCreature(World p_i1602_1_)
    {
        super(p_i1602_1_);
    }

    /**
     * Disables a mob's ability to move on its own while true.
     */
    protected boolean isMovementCeased()
    {
        return false;
    }

    protected void updateEntityActionState()
    {
        this.worldObj.theProfiler.startSection("ai");

        if (this.fleeingTick > 0 && --this.fleeingTick == 0)
        {
            IAttributeInstance var1 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            var1.removeModifier(field_110181_i);
        }

        this.hasAttacked = this.isMovementCeased();
        float var21 = 16.0F;

        if (this.entityToAttack == null)
        {
            this.entityToAttack = this.findPlayerToAttack();

            if (this.entityToAttack != null)
            {
                this.pathToEntity = this.worldObj.getPathEntityToEntity(this, this.entityToAttack, var21, true, false, false, true);
            }
        }
        else if (this.entityToAttack.isEntityAlive())
        {
            float var2 = this.entityToAttack.getDistanceToEntity(this);

            if (this.canEntityBeSeen(this.entityToAttack))
            {
                this.attackEntity(this.entityToAttack, var2);
            }
        }
        else
        {
            this.entityToAttack = null;
        }

        if (this.entityToAttack instanceof EntityPlayerMP && ((EntityPlayerMP)this.entityToAttack).theItemInWorldManager.isCreative())
        {
            this.entityToAttack = null;
        }

        this.worldObj.theProfiler.endSection();

        if (!this.hasAttacked && this.entityToAttack != null && (this.pathToEntity == null || this.rand.nextInt(20) == 0))
        {
            this.pathToEntity = this.worldObj.getPathEntityToEntity(this, this.entityToAttack, var21, true, false, false, true);
        }
        else if (!this.hasAttacked && (this.pathToEntity == null && this.rand.nextInt(180) == 0 || this.rand.nextInt(120) == 0 || this.fleeingTick > 0) && this.entityAge < 100)
        {
            this.updateWanderPath();
        }

        int var22 = MathHelper.floor_double(this.boundingBox.minY + 0.5D);
        boolean var3 = this.isInWater();
        boolean var4 = this.handleLavaMovement();
        this.rotationPitch = 0.0F;

        if (this.pathToEntity != null && this.rand.nextInt(100) != 0)
        {
            this.worldObj.theProfiler.startSection("followpath");
            Vec3 var5 = this.pathToEntity.getPosition(this);
            double var6 = (double)(this.width * 2.0F);

            while (var5 != null && var5.squareDistanceTo(this.posX, var5.yCoord, this.posZ) < var6 * var6)
            {
                this.pathToEntity.incrementPathIndex();

                if (this.pathToEntity.isFinished())
                {
                    var5 = null;
                    this.pathToEntity = null;
                }
                else
                {
                    var5 = this.pathToEntity.getPosition(this);
                }
            }

            this.isJumping = false;

            if (var5 != null)
            {
                double var8 = var5.xCoord - this.posX;
                double var10 = var5.zCoord - this.posZ;
                double var12 = var5.yCoord - (double)var22;
                float var14 = (float)(Math.atan2(var10, var8) * 180.0D / Math.PI) - 90.0F;
                float var15 = MathHelper.wrapAngleTo180_float(var14 - this.rotationYaw);
                this.moveForward = (float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();

                if (var15 > 30.0F)
                {
                    var15 = 30.0F;
                }

                if (var15 < -30.0F)
                {
                    var15 = -30.0F;
                }

                this.rotationYaw += var15;

                if (this.hasAttacked && this.entityToAttack != null)
                {
                    double var16 = this.entityToAttack.posX - this.posX;
                    double var18 = this.entityToAttack.posZ - this.posZ;
                    float var20 = this.rotationYaw;
                    this.rotationYaw = (float)(Math.atan2(var18, var16) * 180.0D / Math.PI) - 90.0F;
                    var15 = (var20 - this.rotationYaw + 90.0F) * (float)Math.PI / 180.0F;
                    this.moveStrafing = -MathHelper.sin(var15) * this.moveForward * 1.0F;
                    this.moveForward = MathHelper.cos(var15) * this.moveForward * 1.0F;
                }

                if (var12 > 0.0D)
                {
                    this.isJumping = true;
                }
            }

            if (this.entityToAttack != null)
            {
                this.faceEntity(this.entityToAttack, 30.0F, 30.0F);
            }

            if (this.isCollidedHorizontally && !this.hasPath())
            {
                this.isJumping = true;
            }

            if (this.rand.nextFloat() < 0.8F && (var3 || var4))
            {
                this.isJumping = true;
            }

            this.worldObj.theProfiler.endSection();
        }
        else
        {
            super.updateEntityActionState();
            this.pathToEntity = null;
        }
    }

    /**
     * Time remaining during which the Animal is sped up and flees.
     */
    protected void updateWanderPath()
    {
        this.worldObj.theProfiler.startSection("stroll");
        boolean var1 = false;
        int var2 = -1;
        int var3 = -1;
        int var4 = -1;
        float var5 = -99999.0F;

        for (int var6 = 0; var6 < 10; ++var6)
        {
            int var7 = MathHelper.floor_double(this.posX + (double)this.rand.nextInt(13) - 6.0D);
            int var8 = MathHelper.floor_double(this.posY + (double)this.rand.nextInt(7) - 3.0D);
            int var9 = MathHelper.floor_double(this.posZ + (double)this.rand.nextInt(13) - 6.0D);
            float var10 = this.getBlockPathWeight(var7, var8, var9);

            if (var10 > var5)
            {
                var5 = var10;
                var2 = var7;
                var3 = var8;
                var4 = var9;
                var1 = true;
            }
        }

        if (var1)
        {
            this.pathToEntity = this.worldObj.getEntityPathToXYZ(this, var2, var3, var4, 10.0F, true, false, false, true);
        }

        this.worldObj.theProfiler.endSection();
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {}

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to path to the block.
     * Args: x, y, z
     */
    public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_)
    {
        return 0.0F;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        return null;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.boundingBox.minY);
        int var3 = MathHelper.floor_double(this.posZ);
        return super.getCanSpawnHere() && this.getBlockPathWeight(var1, var2, var3) >= 0.0F;
    }

    /**
     * if the entity got a PathEntity it returns true, else false
     */
    public boolean hasPath()
    {
        return this.pathToEntity != null;
    }

    /**
     * sets the pathToEntity
     */
    public void setPathToEntity(PathEntity p_70778_1_)
    {
        this.pathToEntity = p_70778_1_;
    }

    /**
     * returns the target Entity
     */
    public Entity getEntityToAttack()
    {
        return this.entityToAttack;
    }

    /**
     * Sets the entity which is to be attacked.
     */
    public void setTarget(Entity p_70784_1_)
    {
        this.entityToAttack = p_70784_1_;
    }

    public boolean isWithinHomeDistanceCurrentPosition()
    {
        return this.isWithinHomeDistance(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
    }

    public boolean isWithinHomeDistance(int p_110176_1_, int p_110176_2_, int p_110176_3_)
    {
        return this.maximumHomeDistance == -1.0F ? true : this.homePosition.getDistanceSquared(p_110176_1_, p_110176_2_, p_110176_3_) < this.maximumHomeDistance * this.maximumHomeDistance;
    }

    public void setHomeArea(int p_110171_1_, int p_110171_2_, int p_110171_3_, int p_110171_4_)
    {
        this.homePosition.set(p_110171_1_, p_110171_2_, p_110171_3_);
        this.maximumHomeDistance = (float)p_110171_4_;
    }

    /**
     * Returns the chunk coordinate object of the home position.
     */
    public ChunkCoordinates getHomePosition()
    {
        return this.homePosition;
    }

    public float func_110174_bM()
    {
        return this.maximumHomeDistance;
    }

    public void detachHome()
    {
        this.maximumHomeDistance = -1.0F;
    }

    /**
     * Returns whether a home area is defined for this entity.
     */
    public boolean hasHome()
    {
        return this.maximumHomeDistance != -1.0F;
    }

    /**
     * Applies logic related to leashes, for example dragging the entity or breaking the leash.
     */
    protected void updateLeashedState()
    {
        super.updateLeashedState();

        if (this.getLeashed() && this.getLeashedToEntity() != null && this.getLeashedToEntity().worldObj == this.worldObj)
        {
            Entity var1 = this.getLeashedToEntity();
            this.setHomeArea((int)var1.posX, (int)var1.posY, (int)var1.posZ, 5);
            float var2 = this.getDistanceToEntity(var1);

            if (this instanceof EntityTameable && ((EntityTameable)this).isSitting())
            {
                if (var2 > 10.0F)
                {
                    this.clearLeashed(true, true);
                }

                return;
            }

            if (!this.field_110180_bt)
            {
                this.tasks.addTask(2, this.field_110178_bs);
                this.getNavigator().setAvoidsWater(false);
                this.field_110180_bt = true;
            }

            this.func_142017_o(var2);

            if (var2 > 4.0F)
            {
                this.getNavigator().tryMoveToEntityLiving(var1, 1.0D);
            }

            if (var2 > 6.0F)
            {
                double var3 = (var1.posX - this.posX) / (double)var2;
                double var5 = (var1.posY - this.posY) / (double)var2;
                double var7 = (var1.posZ - this.posZ) / (double)var2;
                this.motionX += var3 * Math.abs(var3) * 0.4D;
                this.motionY += var5 * Math.abs(var5) * 0.4D;
                this.motionZ += var7 * Math.abs(var7) * 0.4D;
            }

            if (var2 > 10.0F)
            {
                this.clearLeashed(true, true);
            }
        }
        else if (!this.getLeashed() && this.field_110180_bt)
        {
            this.field_110180_bt = false;
            this.tasks.removeTask(this.field_110178_bs);
            this.getNavigator().setAvoidsWater(true);
            this.detachHome();
        }
    }

    protected void func_142017_o(float p_142017_1_) {}
}
