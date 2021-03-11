package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityCrit2FX extends EntityFX
{
    /** Entity that had been hit and done the Critical hit on. */
    private Entity theEntity;
    private int currentLife;
    private int maximumLife;
    private String particleName;


    public EntityCrit2FX(World p_i1199_1_, Entity p_i1199_2_)
    {
        this(p_i1199_1_, p_i1199_2_, "crit");
    }

    public EntityCrit2FX(World p_i1200_1_, Entity p_i1200_2_, String p_i1200_3_)
    {
        super(p_i1200_1_, p_i1200_2_.posX, p_i1200_2_.boundingBox.minY + (double)(p_i1200_2_.height / 2.0F), p_i1200_2_.posZ, p_i1200_2_.motionX, p_i1200_2_.motionY, p_i1200_2_.motionZ);
        this.theEntity = p_i1200_2_;
        this.maximumLife = 3;
        this.particleName = p_i1200_3_;
        this.onUpdate();
    }

    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {}

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        for (int var1 = 0; var1 < 16; ++var1)
        {
            double var2 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
            double var4 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);
            double var6 = (double)(this.rand.nextFloat() * 2.0F - 1.0F);

            if (var2 * var2 + var4 * var4 + var6 * var6 <= 1.0D)
            {
                double var8 = this.theEntity.posX + var2 * (double)this.theEntity.width / 4.0D;
                double var10 = this.theEntity.boundingBox.minY + (double)(this.theEntity.height / 2.0F) + var4 * (double)this.theEntity.height / 4.0D;
                double var12 = this.theEntity.posZ + var6 * (double)this.theEntity.width / 4.0D;
                this.worldObj.spawnParticle(this.particleName, var8, var10, var12, var2, var4 + 0.2D, var6);
            }
        }

        ++this.currentLife;

        if (this.currentLife >= this.maximumLife)
        {
            this.setDead();
        }
    }

    public int getFXLayer()
    {
        return 3;
    }
}
