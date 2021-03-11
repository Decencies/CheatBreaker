package net.minecraft.entity.projectile;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityFishHook extends Entity
{
    private static final List field_146039_d = Arrays.asList(new WeightedRandomFishable[] {(new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10)).func_150709_a(0.9F), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2)).func_150709_a(0.9F), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, 0), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10)});
    private static final List field_146041_e = Arrays.asList(new WeightedRandomFishable[] {new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), (new WeightedRandomFishable(new ItemStack(Items.bow), 1)).func_150709_a(0.25F).func_150707_a(), (new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1)).func_150709_a(0.25F).func_150707_a(), (new WeightedRandomFishable(new ItemStack(Items.book), 1)).func_150707_a()});
    private static final List field_146036_f = Arrays.asList(new WeightedRandomFishable[] {new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.func_150976_a()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.func_150976_a()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.func_150976_a()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.func_150976_a()), 13)});
    private int field_146037_g;
    private int field_146048_h;
    private int field_146050_i;
    private Block field_146046_j;
    private boolean field_146051_au;
    public int field_146044_a;
    public EntityPlayer field_146042_b;
    private int field_146049_av;
    private int field_146047_aw;
    private int field_146045_ax;
    private int field_146040_ay;
    private int field_146038_az;
    private float field_146054_aA;
    public Entity field_146043_c;
    private int field_146055_aB;
    private double field_146056_aC;
    private double field_146057_aD;
    private double field_146058_aE;
    private double field_146059_aF;
    private double field_146060_aG;
    private double field_146061_aH;
    private double field_146052_aI;
    private double field_146053_aJ;


    public EntityFishHook(World p_i1764_1_)
    {
        super(p_i1764_1_);
        this.field_146037_g = -1;
        this.field_146048_h = -1;
        this.field_146050_i = -1;
        this.setSize(0.25F, 0.25F);
        this.ignoreFrustumCheck = true;
    }

    public EntityFishHook(World p_i1765_1_, double p_i1765_2_, double p_i1765_4_, double p_i1765_6_, EntityPlayer p_i1765_8_)
    {
        this(p_i1765_1_);
        this.setPosition(p_i1765_2_, p_i1765_4_, p_i1765_6_);
        this.ignoreFrustumCheck = true;
        this.field_146042_b = p_i1765_8_;
        p_i1765_8_.fishEntity = this;
    }

    public EntityFishHook(World p_i1766_1_, EntityPlayer p_i1766_2_)
    {
        super(p_i1766_1_);
        this.field_146037_g = -1;
        this.field_146048_h = -1;
        this.field_146050_i = -1;
        this.ignoreFrustumCheck = true;
        this.field_146042_b = p_i1766_2_;
        this.field_146042_b.fishEntity = this;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(p_i1766_2_.posX, p_i1766_2_.posY + 1.62D - (double)p_i1766_2_.yOffset, p_i1766_2_.posZ, p_i1766_2_.rotationYaw, p_i1766_2_.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        float var3 = 0.4F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI) * var3);
        this.func_146035_c(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    protected void entityInit() {}

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double p_70112_1_)
    {
        double var3 = this.boundingBox.getAverageEdgeLength() * 4.0D;
        var3 *= 64.0D;
        return p_70112_1_ < var3 * var3;
    }

    public void func_146035_c(double p_146035_1_, double p_146035_3_, double p_146035_5_, float p_146035_7_, float p_146035_8_)
    {
        float var9 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
        p_146035_1_ /= (double)var9;
        p_146035_3_ /= (double)var9;
        p_146035_5_ /= (double)var9;
        p_146035_1_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_146035_8_;
        p_146035_3_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_146035_8_;
        p_146035_5_ += this.rand.nextGaussian() * 0.007499999832361937D * (double)p_146035_8_;
        p_146035_1_ *= (double)p_146035_7_;
        p_146035_3_ *= (double)p_146035_7_;
        p_146035_5_ *= (double)p_146035_7_;
        this.motionX = p_146035_1_;
        this.motionY = p_146035_3_;
        this.motionZ = p_146035_5_;
        float var10 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_146035_1_, p_146035_5_) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_146035_3_, (double)var10) * 180.0D / Math.PI);
        this.field_146049_av = 0;
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_, double p_70056_5_, float p_70056_7_, float p_70056_8_, int p_70056_9_)
    {
        this.field_146056_aC = p_70056_1_;
        this.field_146057_aD = p_70056_3_;
        this.field_146058_aE = p_70056_5_;
        this.field_146059_aF = (double)p_70056_7_;
        this.field_146060_aG = (double)p_70056_8_;
        this.field_146055_aB = p_70056_9_;
        this.motionX = this.field_146061_aH;
        this.motionY = this.field_146052_aI;
        this.motionZ = this.field_146053_aJ;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_)
    {
        this.field_146061_aH = this.motionX = p_70016_1_;
        this.field_146052_aI = this.motionY = p_70016_3_;
        this.field_146053_aJ = this.motionZ = p_70016_5_;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.field_146055_aB > 0)
        {
            double var27 = this.posX + (this.field_146056_aC - this.posX) / (double)this.field_146055_aB;
            double var28 = this.posY + (this.field_146057_aD - this.posY) / (double)this.field_146055_aB;
            double var29 = this.posZ + (this.field_146058_aE - this.posZ) / (double)this.field_146055_aB;
            double var7 = MathHelper.wrapAngleTo180_double(this.field_146059_aF - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.field_146055_aB);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.field_146060_aG - (double)this.rotationPitch) / (double)this.field_146055_aB);
            --this.field_146055_aB;
            this.setPosition(var27, var28, var29);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else
        {
            if (!this.worldObj.isClient)
            {
                ItemStack var1 = this.field_146042_b.getCurrentEquippedItem();

                if (this.field_146042_b.isDead || !this.field_146042_b.isEntityAlive() || var1 == null || var1.getItem() != Items.fishing_rod || this.getDistanceSqToEntity(this.field_146042_b) > 1024.0D)
                {
                    this.setDead();
                    this.field_146042_b.fishEntity = null;
                    return;
                }

                if (this.field_146043_c != null)
                {
                    if (!this.field_146043_c.isDead)
                    {
                        this.posX = this.field_146043_c.posX;
                        this.posY = this.field_146043_c.boundingBox.minY + (double)this.field_146043_c.height * 0.8D;
                        this.posZ = this.field_146043_c.posZ;
                        return;
                    }

                    this.field_146043_c = null;
                }
            }

            if (this.field_146044_a > 0)
            {
                --this.field_146044_a;
            }

            if (this.field_146051_au)
            {
                if (this.worldObj.getBlock(this.field_146037_g, this.field_146048_h, this.field_146050_i) == this.field_146046_j)
                {
                    ++this.field_146049_av;

                    if (this.field_146049_av == 1200)
                    {
                        this.setDead();
                    }

                    return;
                }

                this.field_146051_au = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.field_146049_av = 0;
                this.field_146047_aw = 0;
            }
            else
            {
                ++this.field_146047_aw;
            }

            Vec3 var26 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var26, var2);
            var26 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            var2 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var3 != null)
            {
                var2 = Vec3.createVectorHelper(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
            }

            Entity var4 = null;
            List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var6 = 0.0D;
            double var13;

            for (int var8 = 0; var8 < var5.size(); ++var8)
            {
                Entity var9 = (Entity)var5.get(var8);

                if (var9.canBeCollidedWith() && (var9 != this.field_146042_b || this.field_146047_aw >= 5))
                {
                    float var10 = 0.3F;
                    AxisAlignedBB var11 = var9.boundingBox.expand((double)var10, (double)var10, (double)var10);
                    MovingObjectPosition var12 = var11.calculateIntercept(var26, var2);

                    if (var12 != null)
                    {
                        var13 = var26.distanceTo(var12.hitVec);

                        if (var13 < var6 || var6 == 0.0D)
                        {
                            var4 = var9;
                            var6 = var13;
                        }
                    }
                }
            }

            if (var4 != null)
            {
                var3 = new MovingObjectPosition(var4);
            }

            if (var3 != null)
            {
                if (var3.entityHit != null)
                {
                    if (var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.field_146042_b), 0.0F))
                    {
                        this.field_146043_c = var3.entityHit;
                    }
                }
                else
                {
                    this.field_146051_au = true;
                }
            }

            if (!this.field_146051_au)
            {
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                float var30 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

                for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)var30) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
                {
                    ;
                }

                while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
                {
                    this.prevRotationPitch += 360.0F;
                }

                while (this.rotationYaw - this.prevRotationYaw < -180.0F)
                {
                    this.prevRotationYaw -= 360.0F;
                }

                while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
                {
                    this.prevRotationYaw += 360.0F;
                }

                this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
                this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
                float var31 = 0.92F;

                if (this.onGround || this.isCollidedHorizontally)
                {
                    var31 = 0.5F;
                }

                byte var32 = 5;
                double var33 = 0.0D;

                for (int var34 = 0; var34 < var32; ++var34)
                {
                    double var14 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var34 + 0) / (double)var32 - 0.125D + 0.125D;
                    double var16 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (double)(var34 + 1) / (double)var32 - 0.125D + 0.125D;
                    AxisAlignedBB var18 = AxisAlignedBB.getBoundingBox(this.boundingBox.minX, var14, this.boundingBox.minZ, this.boundingBox.maxX, var16, this.boundingBox.maxZ);

                    if (this.worldObj.isAABBInMaterial(var18, Material.water))
                    {
                        var33 += 1.0D / (double)var32;
                    }
                }

                if (!this.worldObj.isClient && var33 > 0.0D)
                {
                    WorldServer var35 = (WorldServer)this.worldObj;
                    int var36 = 1;

                    if (this.rand.nextFloat() < 0.25F && this.worldObj.canLightningStrikeAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) + 1, MathHelper.floor_double(this.posZ)))
                    {
                        var36 = 2;
                    }

                    if (this.rand.nextFloat() < 0.5F && !this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) + 1, MathHelper.floor_double(this.posZ)))
                    {
                        --var36;
                    }

                    if (this.field_146045_ax > 0)
                    {
                        --this.field_146045_ax;

                        if (this.field_146045_ax <= 0)
                        {
                            this.field_146040_ay = 0;
                            this.field_146038_az = 0;
                        }
                    }
                    else
                    {
                        float var15;
                        float var17;
                        double var20;
                        double var22;
                        float var37;
                        double var38;

                        if (this.field_146038_az > 0)
                        {
                            this.field_146038_az -= var36;

                            if (this.field_146038_az <= 0)
                            {
                                this.motionY -= 0.20000000298023224D;
                                this.playSound("random.splash", 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                                var15 = (float)MathHelper.floor_double(this.boundingBox.minY);
                                var35.func_147487_a("bubble", this.posX, (double)(var15 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                                var35.func_147487_a("wake", this.posX, (double)(var15 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                                this.field_146045_ax = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
                            }
                            else
                            {
                                this.field_146054_aA = (float)((double)this.field_146054_aA + this.rand.nextGaussian() * 4.0D);
                                var15 = this.field_146054_aA * 0.017453292F;
                                var37 = MathHelper.sin(var15);
                                var17 = MathHelper.cos(var15);
                                var38 = this.posX + (double)(var37 * (float)this.field_146038_az * 0.1F);
                                var20 = (double)((float)MathHelper.floor_double(this.boundingBox.minY) + 1.0F);
                                var22 = this.posZ + (double)(var17 * (float)this.field_146038_az * 0.1F);

                                if (this.rand.nextFloat() < 0.15F)
                                {
                                    var35.func_147487_a("bubble", var38, var20 - 0.10000000149011612D, var22, 1, (double)var37, 0.1D, (double)var17, 0.0D);
                                }

                                float var24 = var37 * 0.04F;
                                float var25 = var17 * 0.04F;
                                var35.func_147487_a("wake", var38, var20, var22, 0, (double)var25, 0.01D, (double)(-var24), 1.0D);
                                var35.func_147487_a("wake", var38, var20, var22, 0, (double)(-var25), 0.01D, (double)var24, 1.0D);
                            }
                        }
                        else if (this.field_146040_ay > 0)
                        {
                            this.field_146040_ay -= var36;
                            var15 = 0.15F;

                            if (this.field_146040_ay < 20)
                            {
                                var15 = (float)((double)var15 + (double)(20 - this.field_146040_ay) * 0.05D);
                            }
                            else if (this.field_146040_ay < 40)
                            {
                                var15 = (float)((double)var15 + (double)(40 - this.field_146040_ay) * 0.02D);
                            }
                            else if (this.field_146040_ay < 60)
                            {
                                var15 = (float)((double)var15 + (double)(60 - this.field_146040_ay) * 0.01D);
                            }

                            if (this.rand.nextFloat() < var15)
                            {
                                var37 = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F) * 0.017453292F;
                                var17 = MathHelper.randomFloatClamp(this.rand, 25.0F, 60.0F);
                                var38 = this.posX + (double)(MathHelper.sin(var37) * var17 * 0.1F);
                                var20 = (double)((float)MathHelper.floor_double(this.boundingBox.minY) + 1.0F);
                                var22 = this.posZ + (double)(MathHelper.cos(var37) * var17 * 0.1F);
                                var35.func_147487_a("splash", var38, var20, var22, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                            }

                            if (this.field_146040_ay <= 0)
                            {
                                this.field_146054_aA = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F);
                                this.field_146038_az = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
                            }
                        }
                        else
                        {
                            this.field_146040_ay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
                            this.field_146040_ay -= EnchantmentHelper.func_151387_h(this.field_146042_b) * 20 * 5;
                        }
                    }

                    if (this.field_146045_ax > 0)
                    {
                        this.motionY -= (double)(this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2D;
                    }
                }

                var13 = var33 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * var13;

                if (var33 > 0.0D)
                {
                    var31 = (float)((double)var31 * 0.9D);
                    this.motionY *= 0.8D;
                }

                this.motionX *= (double)var31;
                this.motionY *= (double)var31;
                this.motionZ *= (double)var31;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        p_70014_1_.setShort("xTile", (short)this.field_146037_g);
        p_70014_1_.setShort("yTile", (short)this.field_146048_h);
        p_70014_1_.setShort("zTile", (short)this.field_146050_i);
        p_70014_1_.setByte("inTile", (byte)Block.getIdFromBlock(this.field_146046_j));
        p_70014_1_.setByte("shake", (byte)this.field_146044_a);
        p_70014_1_.setByte("inGround", (byte)(this.field_146051_au ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        this.field_146037_g = p_70037_1_.getShort("xTile");
        this.field_146048_h = p_70037_1_.getShort("yTile");
        this.field_146050_i = p_70037_1_.getShort("zTile");
        this.field_146046_j = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
        this.field_146044_a = p_70037_1_.getByte("shake") & 255;
        this.field_146051_au = p_70037_1_.getByte("inGround") == 1;
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public int func_146034_e()
    {
        if (this.worldObj.isClient)
        {
            return 0;
        }
        else
        {
            byte var1 = 0;

            if (this.field_146043_c != null)
            {
                double var2 = this.field_146042_b.posX - this.posX;
                double var4 = this.field_146042_b.posY - this.posY;
                double var6 = this.field_146042_b.posZ - this.posZ;
                double var8 = (double)MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
                double var10 = 0.1D;
                this.field_146043_c.motionX += var2 * var10;
                this.field_146043_c.motionY += var4 * var10 + (double)MathHelper.sqrt_double(var8) * 0.08D;
                this.field_146043_c.motionZ += var6 * var10;
                var1 = 3;
            }
            else if (this.field_146045_ax > 0)
            {
                EntityItem var13 = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.func_146033_f());
                double var3 = this.field_146042_b.posX - this.posX;
                double var5 = this.field_146042_b.posY - this.posY;
                double var7 = this.field_146042_b.posZ - this.posZ;
                double var9 = (double)MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
                double var11 = 0.1D;
                var13.motionX = var3 * var11;
                var13.motionY = var5 * var11 + (double)MathHelper.sqrt_double(var9) * 0.08D;
                var13.motionZ = var7 * var11;
                this.worldObj.spawnEntityInWorld(var13);
                this.field_146042_b.worldObj.spawnEntityInWorld(new EntityXPOrb(this.field_146042_b.worldObj, this.field_146042_b.posX, this.field_146042_b.posY + 0.5D, this.field_146042_b.posZ + 0.5D, this.rand.nextInt(6) + 1));
                var1 = 1;
            }

            if (this.field_146051_au)
            {
                var1 = 2;
            }

            this.setDead();
            this.field_146042_b.fishEntity = null;
            return var1;
        }
    }

    private ItemStack func_146033_f()
    {
        float var1 = this.worldObj.rand.nextFloat();
        int var2 = EnchantmentHelper.func_151386_g(this.field_146042_b);
        int var3 = EnchantmentHelper.func_151387_h(this.field_146042_b);
        float var4 = 0.1F - (float)var2 * 0.025F - (float)var3 * 0.01F;
        float var5 = 0.05F + (float)var2 * 0.01F - (float)var3 * 0.01F;
        var4 = MathHelper.clamp_float(var4, 0.0F, 1.0F);
        var5 = MathHelper.clamp_float(var5, 0.0F, 1.0F);

        if (var1 < var4)
        {
            this.field_146042_b.addStat(StatList.field_151183_A, 1);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, field_146039_d)).func_150708_a(this.rand);
        }
        else
        {
            var1 -= var4;

            if (var1 < var5)
            {
                this.field_146042_b.addStat(StatList.field_151184_B, 1);
                return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, field_146041_e)).func_150708_a(this.rand);
            }
            else
            {
                float var10000 = var1 - var5;
                this.field_146042_b.addStat(StatList.fishCaughtStat, 1);
                return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, field_146036_f)).func_150708_a(this.rand);
            }
        }
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        super.setDead();

        if (this.field_146042_b != null)
        {
            this.field_146042_b.fishEntity = null;
        }
    }
}
