package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntitySplashFX extends EntityRainFX
{


    public EntitySplashFX(World p_i1230_1_, double p_i1230_2_, double p_i1230_4_, double p_i1230_6_, double p_i1230_8_, double p_i1230_10_, double p_i1230_12_)
    {
        super(p_i1230_1_, p_i1230_2_, p_i1230_4_, p_i1230_6_);
        this.particleGravity = 0.04F;
        this.nextTextureIndexX();

        if (p_i1230_10_ == 0.0D && (p_i1230_8_ != 0.0D || p_i1230_12_ != 0.0D))
        {
            this.motionX = p_i1230_8_;
            this.motionY = p_i1230_10_ + 0.1D;
            this.motionZ = p_i1230_12_;
        }
    }
}
