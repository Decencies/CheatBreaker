package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public abstract class MovingSound extends PositionedSound implements ITickableSound
{
    protected boolean field_147668_j = false;


    protected MovingSound(ResourceLocation p_i45104_1_)
    {
        super(p_i45104_1_);
    }

    public boolean func_147667_k()
    {
        return this.field_147668_j;
    }
}
