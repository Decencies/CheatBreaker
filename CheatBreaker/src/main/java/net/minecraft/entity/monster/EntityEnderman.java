package net.minecraft.entity.monster;

import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityEnderman extends EntityMob
{
    private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
    private static final AttributeModifier attackingSpeedBoostModifier = (new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 6.199999809265137D, 0)).setSaved(false);
    private static boolean[] carriableBlocks = new boolean[256];

    /**
     * Counter to delay the teleportation of an enderman towards the currently attacked target
     */
    private int teleportDelay;

    /**
     * A player must stare at an enderman for 5 ticks before it becomes aggressive. This field counts those ticks.
     */
    private int stareTimer;
    private Entity lastEntityToAttack;
    private boolean isAggressive;


    public EntityEnderman(World p_i1734_1_)
    {
        super(p_i1734_1_);
        this.setSize(0.6F, 2.9F);
        this.stepHeight = 1.0F;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
        this.dataWatcher.addObject(17, new Byte((byte)0));
        this.dataWatcher.addObject(18, new Byte((byte)0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setShort("carried", (short)Block.getIdFromBlock(this.func_146080_bZ()));
        p_70014_1_.setShort("carriedData", (short)this.getCarryingData());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        super.readEntityFromNBT(p_70037_1_);
        this.func_146081_a(Block.getBlockById(p_70037_1_.getShort("carried")));
        this.setCarryingData(p_70037_1_.getShort("carriedData"));
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 64.0D);

        if (var1 != null)
        {
            if (this.shouldAttackPlayer(var1))
            {
                this.isAggressive = true;

                if (this.stareTimer == 0)
                {
                    this.worldObj.playSoundEffect(var1.posX, var1.posY, var1.posZ, "mob.endermen.stare", 1.0F, 1.0F);
                }

                if (this.stareTimer++ == 5)
                {
                    this.stareTimer = 0;
                    this.setScreaming(true);
                    return var1;
                }
            }
            else
            {
                this.stareTimer = 0;
            }
        }

        return null;
    }

    /**
     * Checks to see if this enderman should be attacking this player
     */
    private boolean shouldAttackPlayer(EntityPlayer p_70821_1_)
    {
        ItemStack var2 = p_70821_1_.inventory.armorInventory[3];

        if (var2 != null && var2.getItem() == Item.getItemFromBlock(Blocks.pumpkin))
        {
            return false;
        }
        else
        {
            Vec3 var3 = p_70821_1_.getLook(1.0F).normalize();
            Vec3 var4 = Vec3.createVectorHelper(this.posX - p_70821_1_.posX, this.boundingBox.minY + (double)(this.height / 2.0F) - (p_70821_1_.posY + (double)p_70821_1_.getEyeHeight()), this.posZ - p_70821_1_.posZ);
            double var5 = var4.lengthVector();
            var4 = var4.normalize();
            double var7 = var3.dotProduct(var4);
            return var7 > 1.0D - 0.025D / var5 && p_70821_1_.canEntityBeSeen(this);
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.isWet())
        {
            this.attackEntityFrom(DamageSource.drown, 1.0F);
        }

        if (this.lastEntityToAttack != this.entityToAttack)
        {
            IAttributeInstance var1 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
            var1.removeModifier(attackingSpeedBoostModifier);

            if (this.entityToAttack != null)
            {
                var1.applyModifier(attackingSpeedBoostModifier);
            }
        }

        this.lastEntityToAttack = this.entityToAttack;
        int var6;

        if (!this.worldObj.isClient && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
        {
            int var2;
            int var3;
            Block var4;

            if (this.func_146080_bZ().getMaterial() == Material.air)
            {
                if (this.rand.nextInt(20) == 0)
                {
                    var6 = MathHelper.floor_double(this.posX - 2.0D + this.rand.nextDouble() * 4.0D);
                    var2 = MathHelper.floor_double(this.posY + this.rand.nextDouble() * 3.0D);
                    var3 = MathHelper.floor_double(this.posZ - 2.0D + this.rand.nextDouble() * 4.0D);
                    var4 = this.worldObj.getBlock(var6, var2, var3);

                    if (carriableBlocks[Block.getIdFromBlock(var4)])
                    {
                        this.func_146081_a(var4);
                        this.setCarryingData(this.worldObj.getBlockMetadata(var6, var2, var3));
                        this.worldObj.setBlock(var6, var2, var3, Blocks.air);
                    }
                }
            }
            else if (this.rand.nextInt(2000) == 0)
            {
                var6 = MathHelper.floor_double(this.posX - 1.0D + this.rand.nextDouble() * 2.0D);
                var2 = MathHelper.floor_double(this.posY + this.rand.nextDouble() * 2.0D);
                var3 = MathHelper.floor_double(this.posZ - 1.0D + this.rand.nextDouble() * 2.0D);
                var4 = this.worldObj.getBlock(var6, var2, var3);
                Block var5 = this.worldObj.getBlock(var6, var2 - 1, var3);

                if (var4.getMaterial() == Material.air && var5.getMaterial() != Material.air && var5.renderAsNormalBlock())
                {
                    this.worldObj.setBlock(var6, var2, var3, this.func_146080_bZ(), this.getCarryingData(), 3);
                    this.func_146081_a(Blocks.air);
                }
            }
        }

        for (var6 = 0; var6 < 2; ++var6)
        {
            this.worldObj.spawnParticle("portal", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
        }

        if (this.worldObj.isDaytime() && !this.worldObj.isClient)
        {
            float var7 = this.getBrightness(1.0F);

            if (var7 > 0.5F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) && this.rand.nextFloat() * 30.0F < (var7 - 0.4F) * 2.0F)
            {
                this.entityToAttack = null;
                this.setScreaming(false);
                this.isAggressive = false;
                this.teleportRandomly();
            }
        }

        if (this.isWet() || this.isBurning())
        {
            this.entityToAttack = null;
            this.setScreaming(false);
            this.isAggressive = false;
            this.teleportRandomly();
        }

        if (this.isScreaming() && !this.isAggressive && this.rand.nextInt(100) == 0)
        {
            this.setScreaming(false);
        }

        this.isJumping = false;

        if (this.entityToAttack != null)
        {
            this.faceEntity(this.entityToAttack, 100.0F, 100.0F);
        }

        if (!this.worldObj.isClient && this.isEntityAlive())
        {
            if (this.entityToAttack != null)
            {
                if (this.entityToAttack instanceof EntityPlayer && this.shouldAttackPlayer((EntityPlayer)this.entityToAttack))
                {
                    if (this.entityToAttack.getDistanceSqToEntity(this) < 16.0D)
                    {
                        this.teleportRandomly();
                    }

                    this.teleportDelay = 0;
                }
                else if (this.entityToAttack.getDistanceSqToEntity(this) > 256.0D && this.teleportDelay++ >= 30 && this.teleportToEntity(this.entityToAttack))
                {
                    this.teleportDelay = 0;
                }
            }
            else
            {
                this.setScreaming(false);
                this.teleportDelay = 0;
            }
        }

        super.onLivingUpdate();
    }

    /**
     * Teleport the enderman to a random nearby position
     */
    protected boolean teleportRandomly()
    {
        double var1 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double var3 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double var5 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(var1, var3, var5);
    }

    /**
     * Teleport the enderman to another entity
     */
    protected boolean teleportToEntity(Entity p_70816_1_)
    {
        Vec3 var2 = Vec3.createVectorHelper(this.posX - p_70816_1_.posX, this.boundingBox.minY + (double)(this.height / 2.0F) - p_70816_1_.posY + (double)p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        var2 = var2.normalize();
        double var3 = 16.0D;
        double var5 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - var2.xCoord * var3;
        double var7 = this.posY + (double)(this.rand.nextInt(16) - 8) - var2.yCoord * var3;
        double var9 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - var2.zCoord * var3;
        return this.teleportTo(var5, var7, var9);
    }

    /**
     * Teleport the enderman
     */
    protected boolean teleportTo(double p_70825_1_, double p_70825_3_, double p_70825_5_)
    {
        double var7 = this.posX;
        double var9 = this.posY;
        double var11 = this.posZ;
        this.posX = p_70825_1_;
        this.posY = p_70825_3_;
        this.posZ = p_70825_5_;
        boolean var13 = false;
        int var14 = MathHelper.floor_double(this.posX);
        int var15 = MathHelper.floor_double(this.posY);
        int var16 = MathHelper.floor_double(this.posZ);

        if (this.worldObj.blockExists(var14, var15, var16))
        {
            boolean var17 = false;

            while (!var17 && var15 > 0)
            {
                Block var18 = this.worldObj.getBlock(var14, var15 - 1, var16);

                if (var18.getMaterial().blocksMovement())
                {
                    var17 = true;
                }
                else
                {
                    --this.posY;
                    --var15;
                }
            }

            if (var17)
            {
                this.setPosition(this.posX, this.posY, this.posZ);

                if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox))
                {
                    var13 = true;
                }
            }
        }

        if (!var13)
        {
            this.setPosition(var7, var9, var11);
            return false;
        }
        else
        {
            short var30 = 128;

            for (int var31 = 0; var31 < var30; ++var31)
            {
                double var19 = (double)var31 / ((double)var30 - 1.0D);
                float var21 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var22 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                float var23 = (this.rand.nextFloat() - 0.5F) * 0.2F;
                double var24 = var7 + (this.posX - var7) * var19 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
                double var26 = var9 + (this.posY - var9) * var19 + this.rand.nextDouble() * (double)this.height;
                double var28 = var11 + (this.posZ - var11) * var19 + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D;
                this.worldObj.spawnParticle("portal", var24, var26, var28, (double)var21, (double)var22, (double)var23);
            }

            this.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
            this.playSound("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.endermen.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.endermen.death";
    }

    protected Item func_146068_u()
    {
        return Items.ender_pearl;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        Item var3 = this.func_146068_u();

        if (var3 != null)
        {
            int var4 = this.rand.nextInt(2 + p_70628_2_);

            for (int var5 = 0; var5 < var4; ++var5)
            {
                this.func_145779_a(var3, 1);
            }
        }
    }

    public void func_146081_a(Block p_146081_1_)
    {
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)(Block.getIdFromBlock(p_146081_1_) & 255)));
    }

    public Block func_146080_bZ()
    {
        return Block.getBlockById(this.dataWatcher.getWatchableObjectByte(16));
    }

    /**
     * Set the metadata of the block an enderman carries
     */
    public void setCarryingData(int p_70817_1_)
    {
        this.dataWatcher.updateObject(17, Byte.valueOf((byte)(p_70817_1_ & 255)));
    }

    /**
     * Get the metadata of the block an enderman carries
     */
    public int getCarryingData()
    {
        return this.dataWatcher.getWatchableObjectByte(17);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            this.setScreaming(true);

            if (p_70097_1_ instanceof EntityDamageSource && p_70097_1_.getEntity() instanceof EntityPlayer)
            {
                this.isAggressive = true;
            }

            if (p_70097_1_ instanceof EntityDamageSourceIndirect)
            {
                this.isAggressive = false;

                for (int var3 = 0; var3 < 64; ++var3)
                {
                    if (this.teleportRandomly())
                    {
                        return true;
                    }
                }

                return false;
            }
            else
            {
                return super.attackEntityFrom(p_70097_1_, p_70097_2_);
            }
        }
    }

    public boolean isScreaming()
    {
        return this.dataWatcher.getWatchableObjectByte(18) > 0;
    }

    public void setScreaming(boolean p_70819_1_)
    {
        this.dataWatcher.updateObject(18, Byte.valueOf((byte)(p_70819_1_ ? 1 : 0)));
    }

    static
    {
        carriableBlocks[Block.getIdFromBlock(Blocks.grass)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.dirt)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.sand)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.gravel)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.yellow_flower)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.red_flower)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.brown_mushroom)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.red_mushroom)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.tnt)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.cactus)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.clay)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.pumpkin)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.melon_block)] = true;
        carriableBlocks[Block.getIdFromBlock(Blocks.mycelium)] = true;
    }
}
