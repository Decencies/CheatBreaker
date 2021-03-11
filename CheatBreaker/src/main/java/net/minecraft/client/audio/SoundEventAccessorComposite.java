package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.util.ResourceLocation;

public class SoundEventAccessorComposite implements ISoundEventAccessor
{
    private final List field_148736_a = Lists.newArrayList();
    private final Random field_148734_b = new Random();
    private final ResourceLocation field_148735_c;
    private final SoundCategory field_148732_d;
    private double field_148733_e;
    private double field_148731_f;


    public SoundEventAccessorComposite(ResourceLocation p_i45120_1_, double p_i45120_2_, double p_i45120_4_, SoundCategory p_i45120_6_)
    {
        this.field_148735_c = p_i45120_1_;
        this.field_148731_f = p_i45120_4_;
        this.field_148733_e = p_i45120_2_;
        this.field_148732_d = p_i45120_6_;
    }

    public int func_148721_a()
    {
        int var1 = 0;
        ISoundEventAccessor var3;

        for (Iterator var2 = this.field_148736_a.iterator(); var2.hasNext(); var1 += var3.func_148721_a())
        {
            var3 = (ISoundEventAccessor)var2.next();
        }

        return var1;
    }

    public SoundPoolEntry func_148720_g()
    {
        int var1 = this.func_148721_a();

        if (!this.field_148736_a.isEmpty() && var1 != 0)
        {
            int var2 = this.field_148734_b.nextInt(var1);
            Iterator var3 = this.field_148736_a.iterator();
            ISoundEventAccessor var4;

            do
            {
                if (!var3.hasNext())
                {
                    return SoundHandler.field_147700_a;
                }

                var4 = (ISoundEventAccessor)var3.next();
                var2 -= var4.func_148721_a();
            }
            while (var2 >= 0);

            SoundPoolEntry var5 = (SoundPoolEntry)var4.func_148720_g();
            var5.func_148651_a(var5.func_148650_b() * this.field_148733_e);
            var5.func_148647_b(var5.func_148649_c() * this.field_148731_f);
            return var5;
        }
        else
        {
            return SoundHandler.field_147700_a;
        }
    }

    public void func_148727_a(ISoundEventAccessor p_148727_1_)
    {
        this.field_148736_a.add(p_148727_1_);
    }

    public ResourceLocation func_148729_c()
    {
        return this.field_148735_c;
    }

    public SoundCategory func_148728_d()
    {
        return this.field_148732_d;
    }
}
