package net.minecraft.util;

import org.apache.commons.lang3.Validate;

public class ResourceLocation
{
    private final String resourceDomain;
    private final String resourcePath;


    public ResourceLocation(String p_i1292_1_, String p_i1292_2_)
    {
        Validate.notNull(p_i1292_2_);

        if (p_i1292_1_ != null && p_i1292_1_.length() != 0)
        {
            this.resourceDomain = p_i1292_1_;
        }
        else
        {
            this.resourceDomain = "minecraft";
        }

        this.resourcePath = p_i1292_2_;
    }

    public ResourceLocation(String p_i1293_1_)
    {
        String var2 = "minecraft";
        String var3 = p_i1293_1_;
        int var4 = p_i1293_1_.indexOf(58);

        if (var4 >= 0)
        {
            var3 = p_i1293_1_.substring(var4 + 1, p_i1293_1_.length());

            if (var4 > 1)
            {
                var2 = p_i1293_1_.substring(0, var4);
            }
        }

        this.resourceDomain = var2.toLowerCase();
        this.resourcePath = var3;
    }

    public String getResourcePath()
    {
        return this.resourcePath;
    }

    public String getResourceDomain()
    {
        return this.resourceDomain;
    }

    public String toString()
    {
        return this.resourceDomain + ":" + this.resourcePath;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ResourceLocation))
        {
            return false;
        }
        else
        {
            ResourceLocation var2 = (ResourceLocation)p_equals_1_;
            return this.resourceDomain.equals(var2.resourceDomain) && this.resourcePath.equals(var2.resourcePath);
        }
    }

    public int hashCode()
    {
        return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
    }
}
