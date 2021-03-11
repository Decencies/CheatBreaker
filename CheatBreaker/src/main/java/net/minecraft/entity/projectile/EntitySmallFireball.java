package net.minecraft.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySmallFireball extends EntityFireball
{


    public EntitySmallFireball(World p_i1770_1_)
    {
        super(p_i1770_1_);
        this.setSize(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World p_i1771_1_, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_)
    {
        super(p_i1771_1_, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
        this.setSize(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World p_i1772_1_, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_)
    {
        super(p_i1772_1_, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_);
        this.setSize(0.3125F, 0.3125F);
    }

    /**
     * Called when this EntityFireball hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition p_70227_1_)
    {
        if (!this.worldObj.isClient)
        {
            if (p_70227_1_.entityHit != null)
            {
                if (!p_70227_1_.entityHit.isImmuneToFire() && p_70227_1_.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F))
                {
                    p_70227_1_.entityHit.setFire(5);
                }
            }
            else
            {
                int var2 = p_70227_1_.blockX;
                int var3 = p_70227_1_.blockY;
                int var4 = p_70227_1_.blockZ;

                switch (p_70227_1_.sideHit)
                {
                    case 0:
                        --var3;
                        break;

                    case 1:
                        ++var3;
                        break;

                    case 2:
                        --var4;
                        break;

                    case 3:
                        ++var4;
                        break;

                    case 4:
                        --var2;
                        break;

                    case 5:
                        ++var2;
                }

                if (this.worldObj.isAirBlock(var2, var3, var4))
                {
                    this.worldObj.setBlock(var2, var3, var4, Blocks.fire);
                }
            }

            this.setDead();
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        return false;
    }
}
