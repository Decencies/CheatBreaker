package net.minecraft.entity.passive;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityHorse extends EntityAnimal implements IInvBasic
{
    private static final IEntitySelector horseBreedingSelector = new IEntitySelector()
    {

        public boolean isEntityApplicable(Entity p_82704_1_)
        {
            return p_82704_1_ instanceof EntityHorse && ((EntityHorse)p_82704_1_).func_110205_ce();
        }
    };
    private static final IAttribute horseJumpStrength = (new RangedAttribute("horse.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
    private static final String[] horseArmorTextures = new String[] {null, "textures/entity/horse/armor/horse_armor_iron.png", "textures/entity/horse/armor/horse_armor_gold.png", "textures/entity/horse/armor/horse_armor_diamond.png"};
    private static final String[] field_110273_bx = new String[] {"", "meo", "goo", "dio"};
    private static final int[] armorValues = new int[] {0, 5, 7, 11};
    private static final String[] horseTextures = new String[] {"textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png"};
    private static final String[] field_110269_bA = new String[] {"hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb"};
    private static final String[] horseMarkingTextures = new String[] {null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png"};
    private static final String[] field_110292_bC = new String[] {"", "wo_", "wmo", "wdo", "bdo"};
    private int eatingHaystackCounter;
    private int openMouthCounter;
    private int jumpRearingCounter;
    public int field_110278_bp;
    public int field_110279_bq;
    protected boolean horseJumping;
    private AnimalChest horseChest;
    private boolean hasReproduced;

    /**
     * "The higher this value, the more likely the horse is to be tamed next time a player rides it."
     */
    protected int temper;
    protected float jumpPower;
    private boolean field_110294_bI;
    private float headLean;
    private float prevHeadLean;
    private float rearingAmount;
    private float prevRearingAmount;
    private float mouthOpenness;
    private float prevMouthOpenness;
    private int field_110285_bP;
    private String field_110286_bQ;
    private String[] field_110280_bR = new String[3];


    public EntityHorse(World p_i1685_1_)
    {
        super(p_i1685_1_);
        this.setSize(1.4F, 1.6F);
        this.isImmuneToFire = false;
        this.setChested(false);
        this.getNavigator().setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.2D));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWander(this, 0.7D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.func_110226_cD();
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Integer.valueOf(0));
        this.dataWatcher.addObject(19, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(20, Integer.valueOf(0));
        this.dataWatcher.addObject(21, String.valueOf(""));
        this.dataWatcher.addObject(22, Integer.valueOf(0));
    }

    public void setHorseType(int p_110214_1_)
    {
        this.dataWatcher.updateObject(19, Byte.valueOf((byte)p_110214_1_));
        this.func_110230_cF();
    }

    /**
     * returns the horse type
     */
    public int getHorseType()
    {
        return this.dataWatcher.getWatchableObjectByte(19);
    }

    public void setHorseVariant(int p_110235_1_)
    {
        this.dataWatcher.updateObject(20, Integer.valueOf(p_110235_1_));
        this.func_110230_cF();
    }

    public int getHorseVariant()
    {
        return this.dataWatcher.getWatchableObjectInt(20);
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        if (this.hasCustomNameTag())
        {
            return this.getCustomNameTag();
        }
        else
        {
            int var1 = this.getHorseType();

            switch (var1)
            {
                case 0:
                default:
                    return StatCollector.translateToLocal("entity.horse.name");

                case 1:
                    return StatCollector.translateToLocal("entity.donkey.name");

                case 2:
                    return StatCollector.translateToLocal("entity.mule.name");

                case 3:
                    return StatCollector.translateToLocal("entity.zombiehorse.name");

                case 4:
                    return StatCollector.translateToLocal("entity.skeletonhorse.name");
            }
        }
    }

    private boolean getHorseWatchableBoolean(int p_110233_1_)
    {
        return (this.dataWatcher.getWatchableObjectInt(16) & p_110233_1_) != 0;
    }

    private void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_)
    {
        int var3 = this.dataWatcher.getWatchableObjectInt(16);

        if (p_110208_2_)
        {
            this.dataWatcher.updateObject(16, Integer.valueOf(var3 | p_110208_1_));
        }
        else
        {
            this.dataWatcher.updateObject(16, Integer.valueOf(var3 & ~p_110208_1_));
        }
    }

    public boolean isAdultHorse()
    {
        return !this.isChild();
    }

    public boolean isTame()
    {
        return this.getHorseWatchableBoolean(2);
    }

    public boolean func_110253_bW()
    {
        return this.isAdultHorse();
    }

    public String func_152119_ch()
    {
        return this.dataWatcher.getWatchableObjectString(21);
    }

    public void func_152120_b(String p_152120_1_)
    {
        this.dataWatcher.updateObject(21, p_152120_1_);
    }

    public float getHorseSize()
    {
        int var1 = this.getGrowingAge();
        return var1 >= 0 ? 1.0F : 0.5F + (float)(-24000 - var1) / -24000.0F * 0.5F;
    }

    /**
     * "Sets the scale for an ageable entity according to the boolean parameter, which says if it's a child."
     */
    public void setScaleForAge(boolean p_98054_1_)
    {
        if (p_98054_1_)
        {
            this.setScale(this.getHorseSize());
        }
        else
        {
            this.setScale(1.0F);
        }
    }

    public boolean isHorseJumping()
    {
        return this.horseJumping;
    }

    public void setHorseTamed(boolean p_110234_1_)
    {
        this.setHorseWatchableBoolean(2, p_110234_1_);
    }

    public void setHorseJumping(boolean p_110255_1_)
    {
        this.horseJumping = p_110255_1_;
    }

    public boolean allowLeashing()
    {
        return !this.func_110256_cu() && super.allowLeashing();
    }

    protected void func_142017_o(float p_142017_1_)
    {
        if (p_142017_1_ > 6.0F && this.isEatingHaystack())
        {
            this.setEatingHaystack(false);
        }
    }

    public boolean isChested()
    {
        return this.getHorseWatchableBoolean(8);
    }

    public int func_110241_cb()
    {
        return this.dataWatcher.getWatchableObjectInt(22);
    }

    /**
     * 0 = iron, 1 = gold, 2 = diamond
     */
    private int getHorseArmorIndex(ItemStack p_110260_1_)
    {
        if (p_110260_1_ == null)
        {
            return 0;
        }
        else
        {
            Item var2 = p_110260_1_.getItem();
            return var2 == Items.iron_horse_armor ? 1 : (var2 == Items.golden_horse_armor ? 2 : (var2 == Items.diamond_horse_armor ? 3 : 0));
        }
    }

    public boolean isEatingHaystack()
    {
        return this.getHorseWatchableBoolean(32);
    }

    public boolean isRearing()
    {
        return this.getHorseWatchableBoolean(64);
    }

    public boolean func_110205_ce()
    {
        return this.getHorseWatchableBoolean(16);
    }

    public boolean getHasReproduced()
    {
        return this.hasReproduced;
    }

    public void func_146086_d(ItemStack p_146086_1_)
    {
        this.dataWatcher.updateObject(22, Integer.valueOf(this.getHorseArmorIndex(p_146086_1_)));
        this.func_110230_cF();
    }

    public void func_110242_l(boolean p_110242_1_)
    {
        this.setHorseWatchableBoolean(16, p_110242_1_);
    }

    public void setChested(boolean p_110207_1_)
    {
        this.setHorseWatchableBoolean(8, p_110207_1_);
    }

    public void setHasReproduced(boolean p_110221_1_)
    {
        this.hasReproduced = p_110221_1_;
    }

    public void setHorseSaddled(boolean p_110251_1_)
    {
        this.setHorseWatchableBoolean(4, p_110251_1_);
    }

    public int getTemper()
    {
        return this.temper;
    }

    public void setTemper(int p_110238_1_)
    {
        this.temper = p_110238_1_;
    }

    public int increaseTemper(int p_110198_1_)
    {
        int var2 = MathHelper.clamp_int(this.getTemper() + p_110198_1_, 0, this.getMaxTemper());
        this.setTemper(var2);
        return var2;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        Entity var3 = p_70097_1_.getEntity();
        return this.riddenByEntity != null && this.riddenByEntity.equals(var3) ? false : super.attackEntityFrom(p_70097_1_, p_70097_2_);
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue()
    {
        return armorValues[this.func_110241_cb()];
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return this.riddenByEntity == null;
    }

    public boolean prepareChunkForSpawn()
    {
        int var1 = MathHelper.floor_double(this.posX);
        int var2 = MathHelper.floor_double(this.posZ);
        this.worldObj.getBiomeGenForCoords(var1, var2);
        return true;
    }

    public void dropChests()
    {
        if (!this.worldObj.isClient && this.isChested())
        {
            this.func_145779_a(Item.getItemFromBlock(Blocks.chest), 1);
            this.setChested(false);
        }
    }

    private void func_110266_cB()
    {
        this.openHorseMouth();
        this.worldObj.playSoundAtEntity(this, "eating", 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float p_70069_1_)
    {
        if (p_70069_1_ > 1.0F)
        {
            this.playSound("mob.horse.land", 0.4F, 1.0F);
        }

        int var2 = MathHelper.ceiling_float_int(p_70069_1_ * 0.5F - 3.0F);

        if (var2 > 0)
        {
            this.attackEntityFrom(DamageSource.fall, (float)var2);

            if (this.riddenByEntity != null)
            {
                this.riddenByEntity.attackEntityFrom(DamageSource.fall, (float)var2);
            }

            Block var3 = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.2D - (double)this.prevRotationYaw), MathHelper.floor_double(this.posZ));

            if (var3.getMaterial() != Material.air)
            {
                Block.SoundType var4 = var3.stepSound;
                this.worldObj.playSoundAtEntity(this, var4.func_150498_e(), var4.func_150497_c() * 0.5F, var4.func_150494_d() * 0.75F);
            }
        }
    }

    private int func_110225_cC()
    {
        int var1 = this.getHorseType();
        return this.isChested() && (var1 == 1 || var1 == 2) ? 17 : 2;
    }

    private void func_110226_cD()
    {
        AnimalChest var1 = this.horseChest;
        this.horseChest = new AnimalChest("HorseChest", this.func_110225_cC());
        this.horseChest.func_110133_a(this.getCommandSenderName());

        if (var1 != null)
        {
            var1.func_110132_b(this);
            int var2 = Math.min(var1.getSizeInventory(), this.horseChest.getSizeInventory());

            for (int var3 = 0; var3 < var2; ++var3)
            {
                ItemStack var4 = var1.getStackInSlot(var3);

                if (var4 != null)
                {
                    this.horseChest.setInventorySlotContents(var3, var4.copy());
                }
            }

            var1 = null;
        }

        this.horseChest.func_110134_a(this);
        this.func_110232_cE();
    }

    private void func_110232_cE()
    {
        if (!this.worldObj.isClient)
        {
            this.setHorseSaddled(this.horseChest.getStackInSlot(0) != null);

            if (this.func_110259_cr())
            {
                this.func_146086_d(this.horseChest.getStackInSlot(1));
            }
        }
    }

    /**
     * Called by InventoryBasic.onInventoryChanged() on a array that is never filled.
     */
    public void onInventoryChanged(InventoryBasic p_76316_1_)
    {
        int var2 = this.func_110241_cb();
        boolean var3 = this.isHorseSaddled();
        this.func_110232_cE();

        if (this.ticksExisted > 20)
        {
            if (var2 == 0 && var2 != this.func_110241_cb())
            {
                this.playSound("mob.horse.armor", 0.5F, 1.0F);
            }
            else if (var2 != this.func_110241_cb())
            {
                this.playSound("mob.horse.armor", 0.5F, 1.0F);
            }

            if (!var3 && this.isHorseSaddled())
            {
                this.playSound("mob.horse.leather", 0.5F, 1.0F);
            }
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        this.prepareChunkForSpawn();
        return super.getCanSpawnHere();
    }

    protected EntityHorse getClosestHorse(Entity p_110250_1_, double p_110250_2_)
    {
        double var4 = Double.MAX_VALUE;
        Entity var6 = null;
        List var7 = this.worldObj.getEntitiesWithinAABBExcludingEntity(p_110250_1_, p_110250_1_.boundingBox.addCoord(p_110250_2_, p_110250_2_, p_110250_2_), horseBreedingSelector);
        Iterator var8 = var7.iterator();

        while (var8.hasNext())
        {
            Entity var9 = (Entity)var8.next();
            double var10 = var9.getDistanceSq(p_110250_1_.posX, p_110250_1_.posY, p_110250_1_.posZ);

            if (var10 < var4)
            {
                var6 = var9;
                var4 = var10;
            }
        }

        return (EntityHorse)var6;
    }

    public double getHorseJumpStrength()
    {
        return this.getEntityAttribute(horseJumpStrength).getAttributeValue();
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        this.openHorseMouth();
        int var1 = this.getHorseType();
        return var1 == 3 ? "mob.horse.zombie.death" : (var1 == 4 ? "mob.horse.skeleton.death" : (var1 != 1 && var1 != 2 ? "mob.horse.death" : "mob.horse.donkey.death"));
    }

    protected Item func_146068_u()
    {
        boolean var1 = this.rand.nextInt(4) == 0;
        int var2 = this.getHorseType();
        return var2 == 4 ? Items.bone : (var2 == 3 ? (var1 ? Item.getItemById(0) : Items.rotten_flesh) : Items.leather);
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        this.openHorseMouth();

        if (this.rand.nextInt(3) == 0)
        {
            this.makeHorseRear();
        }

        int var1 = this.getHorseType();
        return var1 == 3 ? "mob.horse.zombie.hit" : (var1 == 4 ? "mob.horse.skeleton.hit" : (var1 != 1 && var1 != 2 ? "mob.horse.hit" : "mob.horse.donkey.hit"));
    }

    public boolean isHorseSaddled()
    {
        return this.getHorseWatchableBoolean(4);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        this.openHorseMouth();

        if (this.rand.nextInt(10) == 0 && !this.isMovementBlocked())
        {
            this.makeHorseRear();
        }

        int var1 = this.getHorseType();
        return var1 == 3 ? "mob.horse.zombie.idle" : (var1 == 4 ? "mob.horse.skeleton.idle" : (var1 != 1 && var1 != 2 ? "mob.horse.idle" : "mob.horse.donkey.idle"));
    }

    protected String getAngrySoundName()
    {
        this.openHorseMouth();
        this.makeHorseRear();
        int var1 = this.getHorseType();
        return var1 != 3 && var1 != 4 ? (var1 != 1 && var1 != 2 ? "mob.horse.angry" : "mob.horse.donkey.angry") : null;
    }

    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
        Block.SoundType var5 = p_145780_4_.stepSound;

        if (this.worldObj.getBlock(p_145780_1_, p_145780_2_ + 1, p_145780_3_) == Blocks.snow_layer)
        {
            var5 = Blocks.snow_layer.stepSound;
        }

        if (!p_145780_4_.getMaterial().isLiquid())
        {
            int var6 = this.getHorseType();

            if (this.riddenByEntity != null && var6 != 1 && var6 != 2)
            {
                ++this.field_110285_bP;

                if (this.field_110285_bP > 5 && this.field_110285_bP % 3 == 0)
                {
                    this.playSound("mob.horse.gallop", var5.func_150497_c() * 0.15F, var5.func_150494_d());

                    if (var6 == 0 && this.rand.nextInt(10) == 0)
                    {
                        this.playSound("mob.horse.breathe", var5.func_150497_c() * 0.6F, var5.func_150494_d());
                    }
                }
                else if (this.field_110285_bP <= 5)
                {
                    this.playSound("mob.horse.wood", var5.func_150497_c() * 0.15F, var5.func_150494_d());
                }
            }
            else if (var5 == Block.soundTypeWood)
            {
                this.playSound("mob.horse.wood", var5.func_150497_c() * 0.15F, var5.func_150494_d());
            }
            else
            {
                this.playSound("mob.horse.soft", var5.func_150497_c() * 0.15F, var5.func_150494_d());
            }
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(horseJumpStrength);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(53.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22499999403953552D);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 6;
    }

    public int getMaxTemper()
    {
        return 100;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.8F;
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return 400;
    }

    public boolean func_110239_cn()
    {
        return this.getHorseType() == 0 || this.func_110241_cb() > 0;
    }

    private void func_110230_cF()
    {
        this.field_110286_bQ = null;
    }

    private void setHorseTexturePaths()
    {
        this.field_110286_bQ = "horse/";
        this.field_110280_bR[0] = null;
        this.field_110280_bR[1] = null;
        this.field_110280_bR[2] = null;
        int var1 = this.getHorseType();
        int var2 = this.getHorseVariant();
        int var3;

        if (var1 == 0)
        {
            var3 = var2 & 255;
            int var4 = (var2 & 65280) >> 8;
            this.field_110280_bR[0] = horseTextures[var3];
            this.field_110286_bQ = this.field_110286_bQ + field_110269_bA[var3];
            this.field_110280_bR[1] = horseMarkingTextures[var4];
            this.field_110286_bQ = this.field_110286_bQ + field_110292_bC[var4];
        }
        else
        {
            this.field_110280_bR[0] = "";
            this.field_110286_bQ = this.field_110286_bQ + "_" + var1 + "_";
        }

        var3 = this.func_110241_cb();
        this.field_110280_bR[2] = horseArmorTextures[var3];
        this.field_110286_bQ = this.field_110286_bQ + field_110273_bx[var3];
    }

    public String getHorseTexture()
    {
        if (this.field_110286_bQ == null)
        {
            this.setHorseTexturePaths();
        }

        return this.field_110286_bQ;
    }

    public String[] getVariantTexturePaths()
    {
        if (this.field_110286_bQ == null)
        {
            this.setHorseTexturePaths();
        }

        return this.field_110280_bR;
    }

    public void openGUI(EntityPlayer p_110199_1_)
    {
        if (!this.worldObj.isClient && (this.riddenByEntity == null || this.riddenByEntity == p_110199_1_) && this.isTame())
        {
            this.horseChest.func_110133_a(this.getCommandSenderName());
            p_110199_1_.displayGUIHorse(this, this.horseChest);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer p_70085_1_)
    {
        ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

        if (var2 != null && var2.getItem() == Items.spawn_egg)
        {
            return super.interact(p_70085_1_);
        }
        else if (!this.isTame() && this.func_110256_cu())
        {
            return false;
        }
        else if (this.isTame() && this.isAdultHorse() && p_70085_1_.isSneaking())
        {
            this.openGUI(p_70085_1_);
            return true;
        }
        else if (this.func_110253_bW() && this.riddenByEntity != null)
        {
            return super.interact(p_70085_1_);
        }
        else
        {
            if (var2 != null)
            {
                boolean var3 = false;

                if (this.func_110259_cr())
                {
                    byte var4 = -1;

                    if (var2.getItem() == Items.iron_horse_armor)
                    {
                        var4 = 1;
                    }
                    else if (var2.getItem() == Items.golden_horse_armor)
                    {
                        var4 = 2;
                    }
                    else if (var2.getItem() == Items.diamond_horse_armor)
                    {
                        var4 = 3;
                    }

                    if (var4 >= 0)
                    {
                        if (!this.isTame())
                        {
                            this.makeHorseRearWithSound();
                            return true;
                        }

                        this.openGUI(p_70085_1_);
                        return true;
                    }
                }

                if (!var3 && !this.func_110256_cu())
                {
                    float var7 = 0.0F;
                    short var5 = 0;
                    byte var6 = 0;

                    if (var2.getItem() == Items.wheat)
                    {
                        var7 = 2.0F;
                        var5 = 60;
                        var6 = 3;
                    }
                    else if (var2.getItem() == Items.sugar)
                    {
                        var7 = 1.0F;
                        var5 = 30;
                        var6 = 3;
                    }
                    else if (var2.getItem() == Items.bread)
                    {
                        var7 = 7.0F;
                        var5 = 180;
                        var6 = 3;
                    }
                    else if (Block.getBlockFromItem(var2.getItem()) == Blocks.hay_block)
                    {
                        var7 = 20.0F;
                        var5 = 180;
                    }
                    else if (var2.getItem() == Items.apple)
                    {
                        var7 = 3.0F;
                        var5 = 60;
                        var6 = 3;
                    }
                    else if (var2.getItem() == Items.golden_carrot)
                    {
                        var7 = 4.0F;
                        var5 = 60;
                        var6 = 5;

                        if (this.isTame() && this.getGrowingAge() == 0)
                        {
                            var3 = true;
                            this.func_146082_f(p_70085_1_);
                        }
                    }
                    else if (var2.getItem() == Items.golden_apple)
                    {
                        var7 = 10.0F;
                        var5 = 240;
                        var6 = 10;

                        if (this.isTame() && this.getGrowingAge() == 0)
                        {
                            var3 = true;
                            this.func_146082_f(p_70085_1_);
                        }
                    }

                    if (this.getHealth() < this.getMaxHealth() && var7 > 0.0F)
                    {
                        this.heal(var7);
                        var3 = true;
                    }

                    if (!this.isAdultHorse() && var5 > 0)
                    {
                        this.addGrowth(var5);
                        var3 = true;
                    }

                    if (var6 > 0 && (var3 || !this.isTame()) && var6 < this.getMaxTemper())
                    {
                        var3 = true;
                        this.increaseTemper(var6);
                    }

                    if (var3)
                    {
                        this.func_110266_cB();
                    }
                }

                if (!this.isTame() && !var3)
                {
                    if (var2 != null && var2.interactWithEntity(p_70085_1_, this))
                    {
                        return true;
                    }

                    this.makeHorseRearWithSound();
                    return true;
                }

                if (!var3 && this.func_110229_cs() && !this.isChested() && var2.getItem() == Item.getItemFromBlock(Blocks.chest))
                {
                    this.setChested(true);
                    this.playSound("mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                    var3 = true;
                    this.func_110226_cD();
                }

                if (!var3 && this.func_110253_bW() && !this.isHorseSaddled() && var2.getItem() == Items.saddle)
                {
                    this.openGUI(p_70085_1_);
                    return true;
                }

                if (var3)
                {
                    if (!p_70085_1_.capabilities.isCreativeMode && --var2.stackSize == 0)
                    {
                        p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack)null);
                    }

                    return true;
                }
            }

            if (this.func_110253_bW() && this.riddenByEntity == null)
            {
                if (var2 != null && var2.interactWithEntity(p_70085_1_, this))
                {
                    return true;
                }
                else
                {
                    this.func_110237_h(p_70085_1_);
                    return true;
                }
            }
            else
            {
                return super.interact(p_70085_1_);
            }
        }
    }

    private void func_110237_h(EntityPlayer p_110237_1_)
    {
        p_110237_1_.rotationYaw = this.rotationYaw;
        p_110237_1_.rotationPitch = this.rotationPitch;
        this.setEatingHaystack(false);
        this.setRearing(false);

        if (!this.worldObj.isClient)
        {
            p_110237_1_.mountEntity(this);
        }
    }

    public boolean func_110259_cr()
    {
        return this.getHorseType() == 0;
    }

    public boolean func_110229_cs()
    {
        int var1 = this.getHorseType();
        return var1 == 2 || var1 == 1;
    }

    /**
     * Dead and sleeping entities cannot move
     */
    protected boolean isMovementBlocked()
    {
        return this.riddenByEntity != null && this.isHorseSaddled() ? true : this.isEatingHaystack() || this.isRearing();
    }

    public boolean func_110256_cu()
    {
        int var1 = this.getHorseType();
        return var1 == 3 || var1 == 4;
    }

    public boolean func_110222_cv()
    {
        return this.func_110256_cu() || this.getHorseType() == 2;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack p_70877_1_)
    {
        return false;
    }

    private void func_110210_cH()
    {
        this.field_110278_bp = 1;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource p_70645_1_)
    {
        super.onDeath(p_70645_1_);

        if (!this.worldObj.isClient)
        {
            this.dropChestItems();
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (this.rand.nextInt(200) == 0)
        {
            this.func_110210_cH();
        }

        super.onLivingUpdate();

        if (!this.worldObj.isClient)
        {
            if (this.rand.nextInt(900) == 0 && this.deathTime == 0)
            {
                this.heal(1.0F);
            }

            if (!this.isEatingHaystack() && this.riddenByEntity == null && this.rand.nextInt(300) == 0 && this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) - 1, MathHelper.floor_double(this.posZ)) == Blocks.grass)
            {
                this.setEatingHaystack(true);
            }

            if (this.isEatingHaystack() && ++this.eatingHaystackCounter > 50)
            {
                this.eatingHaystackCounter = 0;
                this.setEatingHaystack(false);
            }

            if (this.func_110205_ce() && !this.isAdultHorse() && !this.isEatingHaystack())
            {
                EntityHorse var1 = this.getClosestHorse(this, 16.0D);

                if (var1 != null && this.getDistanceSqToEntity(var1) > 4.0D)
                {
                    PathEntity var2 = this.worldObj.getPathEntityToEntity(this, var1, 16.0F, true, false, false, true);
                    this.setPathToEntity(var2);
                }
            }
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.worldObj.isClient && this.dataWatcher.hasChanges())
        {
            this.dataWatcher.func_111144_e();
            this.func_110230_cF();
        }

        if (this.openMouthCounter > 0 && ++this.openMouthCounter > 30)
        {
            this.openMouthCounter = 0;
            this.setHorseWatchableBoolean(128, false);
        }

        if (!this.worldObj.isClient && this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20)
        {
            this.jumpRearingCounter = 0;
            this.setRearing(false);
        }

        if (this.field_110278_bp > 0 && ++this.field_110278_bp > 8)
        {
            this.field_110278_bp = 0;
        }

        if (this.field_110279_bq > 0)
        {
            ++this.field_110279_bq;

            if (this.field_110279_bq > 300)
            {
                this.field_110279_bq = 0;
            }
        }

        this.prevHeadLean = this.headLean;

        if (this.isEatingHaystack())
        {
            this.headLean += (1.0F - this.headLean) * 0.4F + 0.05F;

            if (this.headLean > 1.0F)
            {
                this.headLean = 1.0F;
            }
        }
        else
        {
            this.headLean += (0.0F - this.headLean) * 0.4F - 0.05F;

            if (this.headLean < 0.0F)
            {
                this.headLean = 0.0F;
            }
        }

        this.prevRearingAmount = this.rearingAmount;

        if (this.isRearing())
        {
            this.prevHeadLean = this.headLean = 0.0F;
            this.rearingAmount += (1.0F - this.rearingAmount) * 0.4F + 0.05F;

            if (this.rearingAmount > 1.0F)
            {
                this.rearingAmount = 1.0F;
            }
        }
        else
        {
            this.field_110294_bI = false;
            this.rearingAmount += (0.8F * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6F - 0.05F;

            if (this.rearingAmount < 0.0F)
            {
                this.rearingAmount = 0.0F;
            }
        }

        this.prevMouthOpenness = this.mouthOpenness;

        if (this.getHorseWatchableBoolean(128))
        {
            this.mouthOpenness += (1.0F - this.mouthOpenness) * 0.7F + 0.05F;

            if (this.mouthOpenness > 1.0F)
            {
                this.mouthOpenness = 1.0F;
            }
        }
        else
        {
            this.mouthOpenness += (0.0F - this.mouthOpenness) * 0.7F - 0.05F;

            if (this.mouthOpenness < 0.0F)
            {
                this.mouthOpenness = 0.0F;
            }
        }
    }

    private void openHorseMouth()
    {
        if (!this.worldObj.isClient)
        {
            this.openMouthCounter = 1;
            this.setHorseWatchableBoolean(128, true);
        }
    }

    private boolean func_110200_cJ()
    {
        return this.riddenByEntity == null && this.ridingEntity == null && this.isTame() && this.isAdultHorse() && !this.func_110222_cv() && this.getHealth() >= this.getMaxHealth();
    }

    public void setEating(boolean p_70019_1_)
    {
        this.setHorseWatchableBoolean(32, p_70019_1_);
    }

    public void setEatingHaystack(boolean p_110227_1_)
    {
        this.setEating(p_110227_1_);
    }

    public void setRearing(boolean p_110219_1_)
    {
        if (p_110219_1_)
        {
            this.setEatingHaystack(false);
        }

        this.setHorseWatchableBoolean(64, p_110219_1_);
    }

    private void makeHorseRear()
    {
        if (!this.worldObj.isClient)
        {
            this.jumpRearingCounter = 1;
            this.setRearing(true);
        }
    }

    public void makeHorseRearWithSound()
    {
        this.makeHorseRear();
        String var1 = this.getAngrySoundName();

        if (var1 != null)
        {
            this.playSound(var1, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    public void dropChestItems()
    {
        this.dropItemsInChest(this, this.horseChest);
        this.dropChests();
    }

    private void dropItemsInChest(Entity p_110240_1_, AnimalChest p_110240_2_)
    {
        if (p_110240_2_ != null && !this.worldObj.isClient)
        {
            for (int var3 = 0; var3 < p_110240_2_.getSizeInventory(); ++var3)
            {
                ItemStack var4 = p_110240_2_.getStackInSlot(var3);

                if (var4 != null)
                {
                    this.entityDropItem(var4, 0.0F);
                }
            }
        }
    }

    public boolean setTamedBy(EntityPlayer p_110263_1_)
    {
        this.func_152120_b(p_110263_1_.getUniqueID().toString());
        this.setHorseTamed(true);
        return true;
    }

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
    {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase && this.isHorseSaddled())
        {
            this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
            this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
            p_70612_1_ = ((EntityLivingBase)this.riddenByEntity).moveStrafing * 0.5F;
            p_70612_2_ = ((EntityLivingBase)this.riddenByEntity).moveForward;

            if (p_70612_2_ <= 0.0F)
            {
                p_70612_2_ *= 0.25F;
                this.field_110285_bP = 0;
            }

            if (this.onGround && this.jumpPower == 0.0F && this.isRearing() && !this.field_110294_bI)
            {
                p_70612_1_ = 0.0F;
                p_70612_2_ = 0.0F;
            }

            if (this.jumpPower > 0.0F && !this.isHorseJumping() && this.onGround)
            {
                this.motionY = this.getHorseJumpStrength() * (double)this.jumpPower;

                if (this.isPotionActive(Potion.jump))
                {
                    this.motionY += (double)((float)(this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                }

                this.setHorseJumping(true);
                this.isAirBorne = true;

                if (p_70612_2_ > 0.0F)
                {
                    float var3 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
                    float var4 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
                    this.motionX += (double)(-0.4F * var3 * this.jumpPower);
                    this.motionZ += (double)(0.4F * var4 * this.jumpPower);
                    this.playSound("mob.horse.jump", 0.4F, 1.0F);
                }

                this.jumpPower = 0.0F;
            }

            this.stepHeight = 1.0F;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

            if (!this.worldObj.isClient)
            {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
            }

            if (this.onGround)
            {
                this.jumpPower = 0.0F;
                this.setHorseJumping(false);
            }

            this.prevLimbSwingAmount = this.limbSwingAmount;
            double var8 = this.posX - this.prevPosX;
            double var5 = this.posZ - this.prevPosZ;
            float var7 = MathHelper.sqrt_double(var8 * var8 + var5 * var5) * 4.0F;

            if (var7 > 1.0F)
            {
                var7 = 1.0F;
            }

            this.limbSwingAmount += (var7 - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }
        else
        {
            this.stepHeight = 0.5F;
            this.jumpMovementFactor = 0.02F;
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setBoolean("EatingHaystack", this.isEatingHaystack());
        p_70014_1_.setBoolean("ChestedHorse", this.isChested());
        p_70014_1_.setBoolean("HasReproduced", this.getHasReproduced());
        p_70014_1_.setBoolean("Bred", this.func_110205_ce());
        p_70014_1_.setInteger("Type", this.getHorseType());
        p_70014_1_.setInteger("Variant", this.getHorseVariant());
        p_70014_1_.setInteger("Temper", this.getTemper());
        p_70014_1_.setBoolean("Tame", this.isTame());
        p_70014_1_.setString("OwnerUUID", this.func_152119_ch());

        if (this.isChested())
        {
            NBTTagList var2 = new NBTTagList();

            for (int var3 = 2; var3 < this.horseChest.getSizeInventory(); ++var3)
            {
                ItemStack var4 = this.horseChest.getStackInSlot(var3);

                if (var4 != null)
                {
                    NBTTagCompound var5 = new NBTTagCompound();
                    var5.setByte("Slot", (byte)var3);
                    var4.writeToNBT(var5);
                    var2.appendTag(var5);
                }
            }

            p_70014_1_.setTag("Items", var2);
        }

        if (this.horseChest.getStackInSlot(1) != null)
        {
            p_70014_1_.setTag("ArmorItem", this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
        }

        if (this.horseChest.getStackInSlot(0) != null)
        {
            p_70014_1_.setTag("SaddleItem", this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        super.readEntityFromNBT(p_70037_1_);
        this.setEatingHaystack(p_70037_1_.getBoolean("EatingHaystack"));
        this.func_110242_l(p_70037_1_.getBoolean("Bred"));
        this.setChested(p_70037_1_.getBoolean("ChestedHorse"));
        this.setHasReproduced(p_70037_1_.getBoolean("HasReproduced"));
        this.setHorseType(p_70037_1_.getInteger("Type"));
        this.setHorseVariant(p_70037_1_.getInteger("Variant"));
        this.setTemper(p_70037_1_.getInteger("Temper"));
        this.setHorseTamed(p_70037_1_.getBoolean("Tame"));

        if (p_70037_1_.func_150297_b("OwnerUUID", 8))
        {
            this.func_152120_b(p_70037_1_.getString("OwnerUUID"));
        }

        IAttributeInstance var2 = this.getAttributeMap().getAttributeInstanceByName("Speed");

        if (var2 != null)
        {
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(var2.getBaseValue() * 0.25D);
        }

        if (this.isChested())
        {
            NBTTagList var3 = p_70037_1_.getTagList("Items", 10);
            this.func_110226_cD();

            for (int var4 = 0; var4 < var3.tagCount(); ++var4)
            {
                NBTTagCompound var5 = var3.getCompoundTagAt(var4);
                int var6 = var5.getByte("Slot") & 255;

                if (var6 >= 2 && var6 < this.horseChest.getSizeInventory())
                {
                    this.horseChest.setInventorySlotContents(var6, ItemStack.loadItemStackFromNBT(var5));
                }
            }
        }

        ItemStack var7;

        if (p_70037_1_.func_150297_b("ArmorItem", 10))
        {
            var7 = ItemStack.loadItemStackFromNBT(p_70037_1_.getCompoundTag("ArmorItem"));

            if (var7 != null && func_146085_a(var7.getItem()))
            {
                this.horseChest.setInventorySlotContents(1, var7);
            }
        }

        if (p_70037_1_.func_150297_b("SaddleItem", 10))
        {
            var7 = ItemStack.loadItemStackFromNBT(p_70037_1_.getCompoundTag("SaddleItem"));

            if (var7 != null && var7.getItem() == Items.saddle)
            {
                this.horseChest.setInventorySlotContents(0, var7);
            }
        }
        else if (p_70037_1_.getBoolean("Saddle"))
        {
            this.horseChest.setInventorySlotContents(0, new ItemStack(Items.saddle));
        }

        this.func_110232_cE();
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal p_70878_1_)
    {
        if (p_70878_1_ == this)
        {
            return false;
        }
        else if (p_70878_1_.getClass() != this.getClass())
        {
            return false;
        }
        else
        {
            EntityHorse var2 = (EntityHorse)p_70878_1_;

            if (this.func_110200_cJ() && var2.func_110200_cJ())
            {
                int var3 = this.getHorseType();
                int var4 = var2.getHorseType();
                return var3 == var4 || var3 == 0 && var4 == 1 || var3 == 1 && var4 == 0;
            }
            else
            {
                return false;
            }
        }
    }

    public EntityAgeable createChild(EntityAgeable p_90011_1_)
    {
        EntityHorse var2 = (EntityHorse)p_90011_1_;
        EntityHorse var3 = new EntityHorse(this.worldObj);
        int var4 = this.getHorseType();
        int var5 = var2.getHorseType();
        int var6 = 0;

        if (var4 == var5)
        {
            var6 = var4;
        }
        else if (var4 == 0 && var5 == 1 || var4 == 1 && var5 == 0)
        {
            var6 = 2;
        }

        if (var6 == 0)
        {
            int var8 = this.rand.nextInt(9);
            int var7;

            if (var8 < 4)
            {
                var7 = this.getHorseVariant() & 255;
            }
            else if (var8 < 8)
            {
                var7 = var2.getHorseVariant() & 255;
            }
            else
            {
                var7 = this.rand.nextInt(7);
            }

            int var9 = this.rand.nextInt(5);

            if (var9 < 2)
            {
                var7 |= this.getHorseVariant() & 65280;
            }
            else if (var9 < 4)
            {
                var7 |= var2.getHorseVariant() & 65280;
            }
            else
            {
                var7 |= this.rand.nextInt(5) << 8 & 65280;
            }

            var3.setHorseVariant(var7);
        }

        var3.setHorseType(var6);
        double var13 = this.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + p_90011_1_.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + (double)this.func_110267_cL();
        var3.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(var13 / 3.0D);
        double var14 = this.getEntityAttribute(horseJumpStrength).getBaseValue() + p_90011_1_.getEntityAttribute(horseJumpStrength).getBaseValue() + this.func_110245_cM();
        var3.getEntityAttribute(horseJumpStrength).setBaseValue(var14 / 3.0D);
        double var11 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + p_90011_1_.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + this.func_110203_cN();
        var3.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(var11 / 3.0D);
        return var3;
    }

    public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
    {
        Object p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);
        boolean var2 = false;
        int var3 = 0;
        int var7;

        if (p_110161_1_1 instanceof EntityHorse.GroupData)
        {
            var7 = ((EntityHorse.GroupData)p_110161_1_1).field_111107_a;
            var3 = ((EntityHorse.GroupData)p_110161_1_1).field_111106_b & 255 | this.rand.nextInt(5) << 8;
        }
        else
        {
            if (this.rand.nextInt(10) == 0)
            {
                var7 = 1;
            }
            else
            {
                int var4 = this.rand.nextInt(7);
                int var5 = this.rand.nextInt(5);
                var7 = 0;
                var3 = var4 | var5 << 8;
            }

            p_110161_1_1 = new EntityHorse.GroupData(var7, var3);
        }

        this.setHorseType(var7);
        this.setHorseVariant(var3);

        if (this.rand.nextInt(5) == 0)
        {
            this.setGrowingAge(-24000);
        }

        if (var7 != 4 && var7 != 3)
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)this.func_110267_cL());

            if (var7 == 0)
            {
                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.func_110203_cN());
            }
            else
            {
                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.17499999701976776D);
            }
        }
        else
        {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15.0D);
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
        }

        if (var7 != 2 && var7 != 1)
        {
            this.getEntityAttribute(horseJumpStrength).setBaseValue(this.func_110245_cM());
        }
        else
        {
            this.getEntityAttribute(horseJumpStrength).setBaseValue(0.5D);
        }

        this.setHealth(this.getMaxHealth());
        return (IEntityLivingData)p_110161_1_1;
    }

    public float getGrassEatingAmount(float p_110258_1_)
    {
        return this.prevHeadLean + (this.headLean - this.prevHeadLean) * p_110258_1_;
    }

    public float getRearingAmount(float p_110223_1_)
    {
        return this.prevRearingAmount + (this.rearingAmount - this.prevRearingAmount) * p_110223_1_;
    }

    public float func_110201_q(float p_110201_1_)
    {
        return this.prevMouthOpenness + (this.mouthOpenness - this.prevMouthOpenness) * p_110201_1_;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return true;
    }

    public void setJumpPower(int p_110206_1_)
    {
        if (this.isHorseSaddled())
        {
            if (p_110206_1_ < 0)
            {
                p_110206_1_ = 0;
            }
            else
            {
                this.field_110294_bI = true;
                this.makeHorseRear();
            }

            if (p_110206_1_ >= 90)
            {
                this.jumpPower = 1.0F;
            }
            else
            {
                this.jumpPower = 0.4F + 0.4F * (float)p_110206_1_ / 90.0F;
            }
        }
    }

    /**
     * "Spawns particles for the horse entity. par1 tells whether to spawn hearts. If it is false, it spawns smoke."
     */
    protected void spawnHorseParticles(boolean p_110216_1_)
    {
        String var2 = p_110216_1_ ? "heart" : "smoke";

        for (int var3 = 0; var3 < 7; ++var3)
        {
            double var4 = this.rand.nextGaussian() * 0.02D;
            double var6 = this.rand.nextGaussian() * 0.02D;
            double var8 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(var2, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var4, var6, var8);
        }
    }

    public void handleHealthUpdate(byte p_70103_1_)
    {
        if (p_70103_1_ == 7)
        {
            this.spawnHorseParticles(true);
        }
        else if (p_70103_1_ == 6)
        {
            this.spawnHorseParticles(false);
        }
        else
        {
            super.handleHealthUpdate(p_70103_1_);
        }
    }

    public void updateRiderPosition()
    {
        super.updateRiderPosition();

        if (this.prevRearingAmount > 0.0F)
        {
            float var1 = MathHelper.sin(this.renderYawOffset * (float)Math.PI / 180.0F);
            float var2 = MathHelper.cos(this.renderYawOffset * (float)Math.PI / 180.0F);
            float var3 = 0.7F * this.prevRearingAmount;
            float var4 = 0.15F * this.prevRearingAmount;
            this.riddenByEntity.setPosition(this.posX + (double)(var3 * var1), this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset() + (double)var4, this.posZ - (double)(var3 * var2));

            if (this.riddenByEntity instanceof EntityLivingBase)
            {
                ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
            }
        }
    }

    private float func_110267_cL()
    {
        return 15.0F + (float)this.rand.nextInt(8) + (float)this.rand.nextInt(9);
    }

    private double func_110245_cM()
    {
        return 0.4000000059604645D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D;
    }

    private double func_110203_cN()
    {
        return (0.44999998807907104D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D) * 0.25D;
    }

    public static boolean func_146085_a(Item p_146085_0_)
    {
        return p_146085_0_ == Items.iron_horse_armor || p_146085_0_ == Items.golden_horse_armor || p_146085_0_ == Items.diamond_horse_armor;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return false;
    }

    public static class GroupData implements IEntityLivingData
    {
        public int field_111107_a;
        public int field_111106_b;


        public GroupData(int p_i1684_1_, int p_i1684_2_)
        {
            this.field_111107_a = p_i1684_1_;
            this.field_111106_b = p_i1684_2_;
        }
    }
}
