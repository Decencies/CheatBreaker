package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.RegistrySimple;

public class SoundRegistry extends RegistrySimple
{
    private Map field_148764_a;


    /**
     * Creates the Map we will use to map keys to their registered values.
     */
    protected Map createUnderlyingMap()
    {
        this.field_148764_a = Maps.newHashMap();
        return this.field_148764_a;
    }

    public void func_148762_a(SoundEventAccessorComposite p_148762_1_)
    {
        this.putObject(p_148762_1_.func_148729_c(), p_148762_1_);
    }

    public void func_148763_c()
    {
        this.field_148764_a.clear();
    }
}
