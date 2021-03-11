package net.minecraft.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

public class PotionEffect
{
    /** ID value of the potion this effect matches. */
    private int potionID;

    /** The duration of the potion effect */
    private int duration;

    /** The amplifier of the potion effect */
    private int amplifier;

    /** Whether the potion is a splash potion */
    private boolean isSplashPotion;

    /** Whether the potion effect came from a beacon */
    private boolean isAmbient;

    /** True if potion effect duration is at maximum, false otherwise. */
    private boolean isPotionDurationMax;


    public PotionEffect(int p_i1574_1_, int p_i1574_2_)
    {
        this(p_i1574_1_, p_i1574_2_, 0);
    }

    public PotionEffect(int p_i1575_1_, int p_i1575_2_, int p_i1575_3_)
    {
        this(p_i1575_1_, p_i1575_2_, p_i1575_3_, false);
    }

    public PotionEffect(int p_i1576_1_, int p_i1576_2_, int p_i1576_3_, boolean p_i1576_4_)
    {
        this.potionID = p_i1576_1_;
        this.duration = p_i1576_2_;
        this.amplifier = p_i1576_3_;
        this.isAmbient = p_i1576_4_;
    }

    public PotionEffect(PotionEffect p_i1577_1_)
    {
        this.potionID = p_i1577_1_.potionID;
        this.duration = p_i1577_1_.duration;
        this.amplifier = p_i1577_1_.amplifier;
    }

    /**
     * merges the input PotionEffect into this one if this.amplifier <= tomerge.amplifier. The duration in the supplied
     * potion effect is assumed to be greater.
     */
    public void combine(PotionEffect p_76452_1_)
    {
        if (this.potionID != p_76452_1_.potionID)
        {
            System.err.println("This method should only be called for matching effects!");
        }

        if (p_76452_1_.amplifier > this.amplifier)
        {
            this.amplifier = p_76452_1_.amplifier;
            this.duration = p_76452_1_.duration;
        }
        else if (p_76452_1_.amplifier == this.amplifier && this.duration < p_76452_1_.duration)
        {
            this.duration = p_76452_1_.duration;
        }
        else if (!p_76452_1_.isAmbient && this.isAmbient)
        {
            this.isAmbient = p_76452_1_.isAmbient;
        }
    }

    /**
     * Retrieve the ID of the potion this effect matches.
     */
    public int getPotionID()
    {
        return this.potionID;
    }

    public int getDuration()
    {
        return this.duration;
    }

    public int getAmplifier()
    {
        return this.amplifier;
    }

    /**
     * Set whether this potion is a splash potion.
     */
    public void setSplashPotion(boolean p_82721_1_)
    {
        this.isSplashPotion = p_82721_1_;
    }

    /**
     * Gets whether this potion effect originated from a beacon
     */
    public boolean getIsAmbient()
    {
        return this.isAmbient;
    }

    public boolean onUpdate(EntityLivingBase p_76455_1_)
    {
        if (this.duration > 0)
        {
            if (Potion.potionTypes[this.potionID].isReady(this.duration, this.amplifier))
            {
                this.performEffect(p_76455_1_);
            }

            this.deincrementDuration();
        }

        return this.duration > 0;
    }

    private int deincrementDuration()
    {
        return --this.duration;
    }

    public void performEffect(EntityLivingBase p_76457_1_)
    {
        if (this.duration > 0)
        {
            Potion.potionTypes[this.potionID].performEffect(p_76457_1_, this.amplifier);
        }
    }

    public String getEffectName()
    {
        return Potion.potionTypes[this.potionID].getName();
    }

    public int hashCode()
    {
        return this.potionID;
    }

    public String toString()
    {
        String var1 = "";

        if (this.getAmplifier() > 0)
        {
            var1 = this.getEffectName() + " x " + (this.getAmplifier() + 1) + ", Duration: " + this.getDuration();
        }
        else
        {
            var1 = this.getEffectName() + ", Duration: " + this.getDuration();
        }

        if (this.isSplashPotion)
        {
            var1 = var1 + ", Splash: true";
        }

        return Potion.potionTypes[this.potionID].isUsable() ? "(" + var1 + ")" : var1;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (!(p_equals_1_ instanceof PotionEffect))
        {
            return false;
        }
        else
        {
            PotionEffect var2 = (PotionEffect)p_equals_1_;
            return this.potionID == var2.potionID && this.amplifier == var2.amplifier && this.duration == var2.duration && this.isSplashPotion == var2.isSplashPotion && this.isAmbient == var2.isAmbient;
        }
    }

    /**
     * Write a custom potion effect to a potion item's NBT data.
     */
    public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound p_82719_1_)
    {
        p_82719_1_.setByte("Id", (byte)this.getPotionID());
        p_82719_1_.setByte("Amplifier", (byte)this.getAmplifier());
        p_82719_1_.setInteger("Duration", this.getDuration());
        p_82719_1_.setBoolean("Ambient", this.getIsAmbient());
        return p_82719_1_;
    }

    /**
     * Read a custom potion effect from a potion item's NBT data.
     */
    public static PotionEffect readCustomPotionEffectFromNBT(NBTTagCompound p_82722_0_)
    {
        byte var1 = p_82722_0_.getByte("Id");

        if (var1 >= 0 && var1 < Potion.potionTypes.length && Potion.potionTypes[var1] != null)
        {
            byte var2 = p_82722_0_.getByte("Amplifier");
            int var3 = p_82722_0_.getInteger("Duration");
            boolean var4 = p_82722_0_.getBoolean("Ambient");
            return new PotionEffect(var1, var3, var2, var4);
        }
        else
        {
            return null;
        }
    }

    /**
     * Toggle the isPotionDurationMax field.
     */
    public void setPotionDurationMax(boolean p_100012_1_)
    {
        this.isPotionDurationMax = p_100012_1_;
    }

    public boolean getIsPotionDurationMax()
    {
        return this.isPotionDurationMax;
    }
}
