package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MovingSoundMinecart extends MovingSound
{
    private final EntityMinecart field_147670_k;
    private float field_147669_l = 0.0F;


    public MovingSoundMinecart(EntityMinecart p_i45105_1_)
    {
        super(new ResourceLocation("minecraft:minecart.base"));
        this.field_147670_k = p_i45105_1_;
        this.field_147659_g = true;
        this.field_147665_h = 0;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        if (this.field_147670_k.isDead)
        {
            this.field_147668_j = true;
        }
        else
        {
            this.field_147660_d = (float)this.field_147670_k.posX;
            this.field_147661_e = (float)this.field_147670_k.posY;
            this.field_147658_f = (float)this.field_147670_k.posZ;
            float var1 = MathHelper.sqrt_double(this.field_147670_k.motionX * this.field_147670_k.motionX + this.field_147670_k.motionZ * this.field_147670_k.motionZ);

            if ((double)var1 >= 0.01D)
            {
                this.field_147669_l = MathHelper.clamp_float(this.field_147669_l + 0.0025F, 0.0F, 1.0F);
                this.field_147662_b = 0.0F + MathHelper.clamp_float(var1, 0.0F, 0.5F) * 0.7F;
            }
            else
            {
                this.field_147669_l = 0.0F;
                this.field_147662_b = 0.0F;
            }
        }
    }
}
