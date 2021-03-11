package net.minecraft.entity.boss;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEndPortal;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityDragon extends EntityLiving implements IBossDisplayData, IEntityMultiPart, IMob
{
    public double targetX;
    public double targetY;
    public double targetZ;

    /**
     * Ring buffer array for the last 64 Y-positions and yaw rotations. Used to calculate offsets for the animations.
     */
    public double[][] ringBuffer = new double[64][3];

    /**
     * Index into the ring buffer. Incremented once per tick and restarts at 0 once it reaches the end of the buffer.
     */
    public int ringBufferIndex = -1;

    /** An array containing all body parts of this dragon */
    public EntityDragonPart[] dragonPartArray;

    /** The head bounding box of a dragon */
    public EntityDragonPart dragonPartHead;

    /** The body bounding box of a dragon */
    public EntityDragonPart dragonPartBody;
    public EntityDragonPart dragonPartTail1;
    public EntityDragonPart dragonPartTail2;
    public EntityDragonPart dragonPartTail3;
    public EntityDragonPart dragonPartWing1;
    public EntityDragonPart dragonPartWing2;

    /** Animation time at previous tick. */
    public float prevAnimTime;

    /**
     * Animation time, used to control the speed of the animation cycles (wings flapping, jaw opening, etc.)
     */
    public float animTime;

    /** Force selecting a new flight target at next tick if set to true. */
    public boolean forceNewTarget;

    /**
     * Activated if the dragon is flying though obsidian, white stone or bedrock. Slows movement and animation speed.
     */
    public boolean slowed;
    private Entity target;
    public int deathTicks;

    /** The current endercrystal that is healing this dragon */
    public EntityEnderCrystal healingEnderCrystal;
    

    public EntityDragon(World p_i1700_1_)
    {
        super(p_i1700_1_);
        this.dragonPartArray = new EntityDragonPart[] {this.dragonPartHead = new EntityDragonPart(this, "head", 6.0F, 6.0F), this.dragonPartBody = new EntityDragonPart(this, "body", 8.0F, 8.0F), this.dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0F, 4.0F), this.dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0F, 4.0F)};
        this.setHealth(this.getMaxHealth());
        this.setSize(16.0F, 8.0F);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.targetY = 100.0D;
        this.ignoreFrustumCheck = true;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
    }

    protected void entityInit()
    {
        super.entityInit();
    }

    /**
     * Returns a double[3] array with movement offsets, used to calculate trailing tail/neck positions. [0] = yaw
     * offset, [1] = y offset, [2] = unused, always 0. Parameters: buffer index offset, partial ticks.
     */
    public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_)
    {
        if (this.getHealth() <= 0.0F)
        {
            p_70974_2_ = 0.0F;
        }

        p_70974_2_ = 1.0F - p_70974_2_;
        int var3 = this.ringBufferIndex - p_70974_1_ * 1 & 63;
        int var4 = this.ringBufferIndex - p_70974_1_ * 1 - 1 & 63;
        double[] var5 = new double[3];
        double var6 = this.ringBuffer[var3][0];
        double var8 = MathHelper.wrapAngleTo180_double(this.ringBuffer[var4][0] - var6);
        var5[0] = var6 + var8 * (double)p_70974_2_;
        var6 = this.ringBuffer[var3][1];
        var8 = this.ringBuffer[var4][1] - var6;
        var5[1] = var6 + var8 * (double)p_70974_2_;
        var5[2] = this.ringBuffer[var3][2] + (this.ringBuffer[var4][2] - this.ringBuffer[var3][2]) * (double)p_70974_2_;
        return var5;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        float var1;
        float var2;

        if (this.worldObj.isClient)
        {
            var1 = MathHelper.cos(this.animTime * (float)Math.PI * 2.0F);
            var2 = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0F);

            if (var2 <= -0.3F && var1 >= -0.3F)
            {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
            }
        }

        this.prevAnimTime = this.animTime;
        float var3;

        if (this.getHealth() <= 0.0F)
        {
            var1 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            var2 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            var3 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.worldObj.spawnParticle("largeexplode", this.posX + (double)var1, this.posY + 2.0D + (double)var2, this.posZ + (double)var3, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            this.updateDragonEnderCrystal();
            var1 = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
            var1 *= (float)Math.pow(2.0D, this.motionY);

            if (this.slowed)
            {
                this.animTime += var1 * 0.5F;
            }
            else
            {
                this.animTime += var1;
            }

            this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);

            if (this.ringBufferIndex < 0)
            {
                for (int var25 = 0; var25 < this.ringBuffer.length; ++var25)
                {
                    this.ringBuffer[var25][0] = (double)this.rotationYaw;
                    this.ringBuffer[var25][1] = this.posY;
                }
            }

            if (++this.ringBufferIndex == this.ringBuffer.length)
            {
                this.ringBufferIndex = 0;
            }

            this.ringBuffer[this.ringBufferIndex][0] = (double)this.rotationYaw;
            this.ringBuffer[this.ringBufferIndex][1] = this.posY;
            double var4;
            double var6;
            double var8;
            double var26;
            float var31;

            if (this.worldObj.isClient)
            {
                if (this.newPosRotationIncrements > 0)
                {
                    var26 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
                    var4 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
                    var6 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;
                    var8 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double)this.rotationYaw);
                    this.rotationYaw = (float)((double)this.rotationYaw + var8 / (double)this.newPosRotationIncrements);
                    this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                    --this.newPosRotationIncrements;
                    this.setPosition(var26, var4, var6);
                    this.setRotation(this.rotationYaw, this.rotationPitch);
                }
            }
            else
            {
                var26 = this.targetX - this.posX;
                var4 = this.targetY - this.posY;
                var6 = this.targetZ - this.posZ;
                var8 = var26 * var26 + var4 * var4 + var6 * var6;

                if (this.target != null)
                {
                    this.targetX = this.target.posX;
                    this.targetZ = this.target.posZ;
                    double var10 = this.targetX - this.posX;
                    double var12 = this.targetZ - this.posZ;
                    double var14 = Math.sqrt(var10 * var10 + var12 * var12);
                    double var16 = 0.4000000059604645D + var14 / 80.0D - 1.0D;

                    if (var16 > 10.0D)
                    {
                        var16 = 10.0D;
                    }

                    this.targetY = this.target.boundingBox.minY + var16;
                }
                else
                {
                    this.targetX += this.rand.nextGaussian() * 2.0D;
                    this.targetZ += this.rand.nextGaussian() * 2.0D;
                }

                if (this.forceNewTarget || var8 < 100.0D || var8 > 22500.0D || this.isCollidedHorizontally || this.isCollidedVertically)
                {
                    this.setNewTarget();
                }

                var4 /= (double)MathHelper.sqrt_double(var26 * var26 + var6 * var6);
                var31 = 0.6F;

                if (var4 < (double)(-var31))
                {
                    var4 = (double)(-var31);
                }

                if (var4 > (double)var31)
                {
                    var4 = (double)var31;
                }

                this.motionY += var4 * 0.10000000149011612D;
                this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
                double var11 = 180.0D - Math.atan2(var26, var6) * 180.0D / Math.PI;
                double var13 = MathHelper.wrapAngleTo180_double(var11 - (double)this.rotationYaw);

                if (var13 > 50.0D)
                {
                    var13 = 50.0D;
                }

                if (var13 < -50.0D)
                {
                    var13 = -50.0D;
                }

                Vec3 var15 = Vec3.createVectorHelper(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
                Vec3 var39 = Vec3.createVectorHelper((double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), this.motionY, (double)(-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F))).normalize();
                float var17 = (float)(var39.dotProduct(var15) + 0.5D) / 1.5F;

                if (var17 < 0.0F)
                {
                    var17 = 0.0F;
                }

                this.randomYawVelocity *= 0.8F;
                float var18 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
                double var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;

                if (var19 > 40.0D)
                {
                    var19 = 40.0D;
                }

                this.randomYawVelocity = (float)((double)this.randomYawVelocity + var13 * (0.699999988079071D / var19 / (double)var18));
                this.rotationYaw += this.randomYawVelocity * 0.1F;
                float var21 = (float)(2.0D / (var19 + 1.0D));
                float var22 = 0.06F;
                this.moveFlying(0.0F, -1.0F, var22 * (var17 * var21 + (1.0F - var21)));

                if (this.slowed)
                {
                    this.moveEntity(this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
                }
                else
                {
                    this.moveEntity(this.motionX, this.motionY, this.motionZ);
                }

                Vec3 var23 = Vec3.createVectorHelper(this.motionX, this.motionY, this.motionZ).normalize();
                float var24 = (float)(var23.dotProduct(var39) + 1.0D) / 2.0F;
                var24 = 0.8F + 0.15F * var24;
                this.motionX *= (double)var24;
                this.motionZ *= (double)var24;
                this.motionY *= 0.9100000262260437D;
            }

            this.renderYawOffset = this.rotationYaw;
            this.dragonPartHead.width = this.dragonPartHead.height = 3.0F;
            this.dragonPartTail1.width = this.dragonPartTail1.height = 2.0F;
            this.dragonPartTail2.width = this.dragonPartTail2.height = 2.0F;
            this.dragonPartTail3.width = this.dragonPartTail3.height = 2.0F;
            this.dragonPartBody.height = 3.0F;
            this.dragonPartBody.width = 5.0F;
            this.dragonPartWing1.height = 2.0F;
            this.dragonPartWing1.width = 4.0F;
            this.dragonPartWing2.height = 3.0F;
            this.dragonPartWing2.width = 4.0F;
            var2 = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F * (float)Math.PI;
            var3 = MathHelper.cos(var2);
            float var27 = -MathHelper.sin(var2);
            float var5 = this.rotationYaw * (float)Math.PI / 180.0F;
            float var28 = MathHelper.sin(var5);
            float var7 = MathHelper.cos(var5);
            this.dragonPartBody.onUpdate();
            this.dragonPartBody.setLocationAndAngles(this.posX + (double)(var28 * 0.5F), this.posY, this.posZ - (double)(var7 * 0.5F), 0.0F, 0.0F);
            this.dragonPartWing1.onUpdate();
            this.dragonPartWing1.setLocationAndAngles(this.posX + (double)(var7 * 4.5F), this.posY + 2.0D, this.posZ + (double)(var28 * 4.5F), 0.0F, 0.0F);
            this.dragonPartWing2.onUpdate();
            this.dragonPartWing2.setLocationAndAngles(this.posX - (double)(var7 * 4.5F), this.posY + 2.0D, this.posZ - (double)(var28 * 4.5F), 0.0F, 0.0F);

            if (!this.worldObj.isClient && this.hurtTime == 0)
            {
                this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.boundingBox.expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
                this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.boundingBox.expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
                this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.boundingBox.expand(1.0D, 1.0D, 1.0D)));
            }

            double[] var29 = this.getMovementOffsets(5, 1.0F);
            double[] var9 = this.getMovementOffsets(0, 1.0F);
            var31 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
            float var33 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
            this.dragonPartHead.onUpdate();
            this.dragonPartHead.setLocationAndAngles(this.posX + (double)(var31 * 5.5F * var3), this.posY + (var9[1] - var29[1]) * 1.0D + (double)(var27 * 5.5F), this.posZ - (double)(var33 * 5.5F * var3), 0.0F, 0.0F);

            for (int var30 = 0; var30 < 3; ++var30)
            {
                EntityDragonPart var32 = null;

                if (var30 == 0)
                {
                    var32 = this.dragonPartTail1;
                }

                if (var30 == 1)
                {
                    var32 = this.dragonPartTail2;
                }

                if (var30 == 2)
                {
                    var32 = this.dragonPartTail3;
                }

                double[] var34 = this.getMovementOffsets(12 + var30 * 2, 1.0F);
                float var35 = this.rotationYaw * (float)Math.PI / 180.0F + this.simplifyAngle(var34[0] - var29[0]) * (float)Math.PI / 180.0F * 1.0F;
                float var36 = MathHelper.sin(var35);
                float var37 = MathHelper.cos(var35);
                float var38 = 1.5F;
                float var40 = (float)(var30 + 1) * 2.0F;
                var32.onUpdate();
                var32.setLocationAndAngles(this.posX - (double)((var28 * var38 + var36 * var40) * var3), this.posY + (var34[1] - var29[1]) * 1.0D - (double)((var40 + var38) * var27) + 1.5D, this.posZ + (double)((var7 * var38 + var37 * var40) * var3), 0.0F, 0.0F);
            }

            if (!this.worldObj.isClient)
            {
                this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.boundingBox) | this.destroyBlocksInAABB(this.dragonPartBody.boundingBox);
            }
        }
    }

    /**
     * Updates the state of the enderdragon's current endercrystal.
     */
    private void updateDragonEnderCrystal()
    {
        if (this.healingEnderCrystal != null)
        {
            if (this.healingEnderCrystal.isDead)
            {
                if (!this.worldObj.isClient)
                {
                    this.attackEntityFromPart(this.dragonPartHead, DamageSource.setExplosionSource((Explosion)null), 10.0F);
                }

                this.healingEnderCrystal = null;
            }
            else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth())
            {
                this.setHealth(this.getHealth() + 1.0F);
            }
        }

        if (this.rand.nextInt(10) == 0)
        {
            float var1 = 32.0F;
            List var2 = this.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, this.boundingBox.expand((double)var1, (double)var1, (double)var1));
            EntityEnderCrystal var3 = null;
            double var4 = Double.MAX_VALUE;
            Iterator var6 = var2.iterator();

            while (var6.hasNext())
            {
                EntityEnderCrystal var7 = (EntityEnderCrystal)var6.next();
                double var8 = var7.getDistanceSqToEntity(this);

                if (var8 < var4)
                {
                    var4 = var8;
                    var3 = var7;
                }
            }

            this.healingEnderCrystal = var3;
        }
    }

    /**
     * Pushes all entities inside the list away from the enderdragon.
     */
    private void collideWithEntities(List p_70970_1_)
    {
        double var2 = (this.dragonPartBody.boundingBox.minX + this.dragonPartBody.boundingBox.maxX) / 2.0D;
        double var4 = (this.dragonPartBody.boundingBox.minZ + this.dragonPartBody.boundingBox.maxZ) / 2.0D;
        Iterator var6 = p_70970_1_.iterator();

        while (var6.hasNext())
        {
            Entity var7 = (Entity)var6.next();

            if (var7 instanceof EntityLivingBase)
            {
                double var8 = var7.posX - var2;
                double var10 = var7.posZ - var4;
                double var12 = var8 * var8 + var10 * var10;
                var7.addVelocity(var8 / var12 * 4.0D, 0.20000000298023224D, var10 / var12 * 4.0D);
            }
        }
    }

    /**
     * Attacks all entities inside this list, dealing 5 hearts of damage.
     */
    private void attackEntitiesInList(List p_70971_1_)
    {
        for (int var2 = 0; var2 < p_70971_1_.size(); ++var2)
        {
            Entity var3 = (Entity)p_70971_1_.get(var2);

            if (var3 instanceof EntityLivingBase)
            {
                var3.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
            }
        }
    }

    /**
     * Sets a new target for the flight AI. It can be a random coordinate or a nearby player.
     */
    private void setNewTarget()
    {
        this.forceNewTarget = false;

        if (this.rand.nextInt(2) == 0 && !this.worldObj.playerEntities.isEmpty())
        {
            this.target = (Entity)this.worldObj.playerEntities.get(this.rand.nextInt(this.worldObj.playerEntities.size()));
        }
        else
        {
            boolean var1 = false;

            do
            {
                this.targetX = 0.0D;
                this.targetY = (double)(70.0F + this.rand.nextFloat() * 50.0F);
                this.targetZ = 0.0D;
                this.targetX += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
                this.targetZ += (double)(this.rand.nextFloat() * 120.0F - 60.0F);
                double var2 = this.posX - this.targetX;
                double var4 = this.posY - this.targetY;
                double var6 = this.posZ - this.targetZ;
                var1 = var2 * var2 + var4 * var4 + var6 * var6 > 100.0D;
            }
            while (!var1);

            this.target = null;
        }
    }

    /**
     * Simplifies the value of a number by adding/subtracting 180 to the point that the number is between -180 and 180.
     */
    private float simplifyAngle(double p_70973_1_)
    {
        return (float)MathHelper.wrapAngleTo180_double(p_70973_1_);
    }

    /**
     * Destroys all blocks that aren't associated with 'The End' inside the given bounding box.
     */
    private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_)
    {
        int var2 = MathHelper.floor_double(p_70972_1_.minX);
        int var3 = MathHelper.floor_double(p_70972_1_.minY);
        int var4 = MathHelper.floor_double(p_70972_1_.minZ);
        int var5 = MathHelper.floor_double(p_70972_1_.maxX);
        int var6 = MathHelper.floor_double(p_70972_1_.maxY);
        int var7 = MathHelper.floor_double(p_70972_1_.maxZ);
        boolean var8 = false;
        boolean var9 = false;

        for (int var10 = var2; var10 <= var5; ++var10)
        {
            for (int var11 = var3; var11 <= var6; ++var11)
            {
                for (int var12 = var4; var12 <= var7; ++var12)
                {
                    Block var13 = this.worldObj.getBlock(var10, var11, var12);

                    if (var13.getMaterial() != Material.air)
                    {
                        if (var13 != Blocks.obsidian && var13 != Blocks.end_stone && var13 != Blocks.bedrock && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
                        {
                            var9 = this.worldObj.setBlockToAir(var10, var11, var12) || var9;
                        }
                        else
                        {
                            var8 = true;
                        }
                    }
                }
            }
        }

        if (var9)
        {
            double var16 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * (double)this.rand.nextFloat();
            double var17 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * (double)this.rand.nextFloat();
            double var14 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * (double)this.rand.nextFloat();
            this.worldObj.spawnParticle("largeexplode", var16, var17, var14, 0.0D, 0.0D, 0.0D);
        }

        return var8;
    }

    public boolean attackEntityFromPart(EntityDragonPart p_70965_1_, DamageSource p_70965_2_, float p_70965_3_)
    {
        if (p_70965_1_ != this.dragonPartHead)
        {
            p_70965_3_ = p_70965_3_ / 4.0F + 1.0F;
        }

        float var4 = this.rotationYaw * (float)Math.PI / 180.0F;
        float var5 = MathHelper.sin(var4);
        float var6 = MathHelper.cos(var4);
        this.targetX = this.posX + (double)(var5 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
        this.targetY = this.posY + (double)(this.rand.nextFloat() * 3.0F) + 1.0D;
        this.targetZ = this.posZ - (double)(var6 * 5.0F) + (double)((this.rand.nextFloat() - 0.5F) * 2.0F);
        this.target = null;

        if (p_70965_2_.getEntity() instanceof EntityPlayer || p_70965_2_.isExplosion())
        {
            this.func_82195_e(p_70965_2_, p_70965_3_);
        }

        return true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        return false;
    }

    protected boolean func_82195_e(DamageSource p_82195_1_, float p_82195_2_)
    {
        return super.attackEntityFrom(p_82195_1_, p_82195_2_);
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        ++this.deathTicks;

        if (this.deathTicks >= 180 && this.deathTicks <= 200)
        {
            float var1 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            float var2 = (this.rand.nextFloat() - 0.5F) * 4.0F;
            float var3 = (this.rand.nextFloat() - 0.5F) * 8.0F;
            this.worldObj.spawnParticle("hugeexplosion", this.posX + (double)var1, this.posY + 2.0D + (double)var2, this.posZ + (double)var3, 0.0D, 0.0D, 0.0D);
        }

        int var4;
        int var5;

        if (!this.worldObj.isClient)
        {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0)
            {
                var4 = 1000;

                while (var4 > 0)
                {
                    var5 = EntityXPOrb.getXPSplit(var4);
                    var4 -= var5;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var5));
                }
            }

            if (this.deathTicks == 1)
            {
                this.worldObj.playBroadcastSound(1018, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
            }
        }

        this.moveEntity(0.0D, 0.10000000149011612D, 0.0D);
        this.renderYawOffset = this.rotationYaw += 20.0F;

        if (this.deathTicks == 200 && !this.worldObj.isClient)
        {
            var4 = 2000;

            while (var4 > 0)
            {
                var5 = EntityXPOrb.getXPSplit(var4);
                var4 -= var5;
                this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var5));
            }

            this.createEnderPortal(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
            this.setDead();
        }
    }

    /**
     * Creates the ender portal leading back to the normal world after defeating the enderdragon.
     */
    private void createEnderPortal(int p_70975_1_, int p_70975_2_)
    {
        byte var3 = 64;
        BlockEndPortal.field_149948_a = true;
        byte var4 = 4;

        for (int var5 = var3 - 1; var5 <= var3 + 32; ++var5)
        {
            for (int var6 = p_70975_1_ - var4; var6 <= p_70975_1_ + var4; ++var6)
            {
                for (int var7 = p_70975_2_ - var4; var7 <= p_70975_2_ + var4; ++var7)
                {
                    double var8 = (double)(var6 - p_70975_1_);
                    double var10 = (double)(var7 - p_70975_2_);
                    double var12 = var8 * var8 + var10 * var10;

                    if (var12 <= ((double)var4 - 0.5D) * ((double)var4 - 0.5D))
                    {
                        if (var5 < var3)
                        {
                            if (var12 <= ((double)(var4 - 1) - 0.5D) * ((double)(var4 - 1) - 0.5D))
                            {
                                this.worldObj.setBlock(var6, var5, var7, Blocks.bedrock);
                            }
                        }
                        else if (var5 > var3)
                        {
                            this.worldObj.setBlock(var6, var5, var7, Blocks.air);
                        }
                        else if (var12 > ((double)(var4 - 1) - 0.5D) * ((double)(var4 - 1) - 0.5D))
                        {
                            this.worldObj.setBlock(var6, var5, var7, Blocks.bedrock);
                        }
                        else
                        {
                            this.worldObj.setBlock(var6, var5, var7, Blocks.end_portal);
                        }
                    }
                }
            }
        }

        this.worldObj.setBlock(p_70975_1_, var3 + 0, p_70975_2_, Blocks.bedrock);
        this.worldObj.setBlock(p_70975_1_, var3 + 1, p_70975_2_, Blocks.bedrock);
        this.worldObj.setBlock(p_70975_1_, var3 + 2, p_70975_2_, Blocks.bedrock);
        this.worldObj.setBlock(p_70975_1_ - 1, var3 + 2, p_70975_2_, Blocks.torch);
        this.worldObj.setBlock(p_70975_1_ + 1, var3 + 2, p_70975_2_, Blocks.torch);
        this.worldObj.setBlock(p_70975_1_, var3 + 2, p_70975_2_ - 1, Blocks.torch);
        this.worldObj.setBlock(p_70975_1_, var3 + 2, p_70975_2_ + 1, Blocks.torch);
        this.worldObj.setBlock(p_70975_1_, var3 + 3, p_70975_2_, Blocks.bedrock);
        this.worldObj.setBlock(p_70975_1_, var3 + 4, p_70975_2_, Blocks.dragon_egg);
        BlockEndPortal.field_149948_a = false;
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    public void despawnEntity() {}

    /**
     * Return the Entity parts making up this Entity (currently only for dragons)
     */
    public Entity[] getParts()
    {
        return this.dragonPartArray;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    public World func_82194_d()
    {
        return this.worldObj;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.enderdragon.growl";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.enderdragon.hit";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 5.0F;
    }
}
