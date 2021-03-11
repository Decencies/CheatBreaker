package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityHugeExplodeFX extends EntityFX
{
    private int timeSinceStart;

    /** the maximum time for the explosion */
    private int maximumTime = 8;


    public EntityHugeExplodeFX(World p_i1214_1_, double p_i1214_2_, double p_i1214_4_, double p_i1214_6_, double p_i1214_8_, double p_i1214_10_, double p_i1214_12_)
    {
        super(p_i1214_1_, p_i1214_2_, p_i1214_4_, p_i1214_6_, 0.0D, 0.0D, 0.0D);
    }

    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {}

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        for (int var1 = 0; var1 < 6; ++var1)
        {
            double var2 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
            double var4 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
            double var6 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
            this.worldObj.spawnParticle("largeexplode", var2, var4, var6, (double)((float)this.timeSinceStart / (float)this.maximumTime), 0.0D, 0.0D);
        }

        ++this.timeSinceStart;

        if (this.timeSinceStart == this.maximumTime)
        {
            this.setDead();
        }
    }

    public int getFXLayer()
    {
        return 1;
    }
}
