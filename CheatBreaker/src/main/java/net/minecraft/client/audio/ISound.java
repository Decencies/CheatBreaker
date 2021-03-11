package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public interface ISound
{
    ResourceLocation func_147650_b();

    boolean func_147657_c();

    int func_147652_d();

    float func_147653_e();

    float func_147655_f();

    float func_147649_g();

    float func_147654_h();

    float func_147651_i();

    ISound.AttenuationType func_147656_j();

    public static enum AttenuationType
    {
        NONE("NONE", 0, 0),
        LINEAR("LINEAR", 1, 2);
        private final int field_148589_c;

        private static final ISound.AttenuationType[] $VALUES = new ISound.AttenuationType[]{NONE, LINEAR};


        private AttenuationType(String p_i45110_1_, int p_i45110_2_, int p_i45110_3_)
        {
            this.field_148589_c = p_i45110_3_;
        }

        public int func_148586_a()
        {
            return this.field_148589_c;
        }
    }
}
