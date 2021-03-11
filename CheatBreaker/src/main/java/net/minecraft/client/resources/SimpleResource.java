package net.minecraft.client.resources;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class SimpleResource implements IResource
{
    private final Map mapMetadataSections = Maps.newHashMap();
    private final ResourceLocation srResourceLocation;
    private final InputStream resourceInputStream;
    private final InputStream mcmetaInputStream;
    private final IMetadataSerializer srMetadataSerializer;
    private boolean mcmetaJsonChecked;
    private JsonObject mcmetaJson;


    public SimpleResource(ResourceLocation p_i1300_1_, InputStream p_i1300_2_, InputStream p_i1300_3_, IMetadataSerializer p_i1300_4_)
    {
        this.srResourceLocation = p_i1300_1_;
        this.resourceInputStream = p_i1300_2_;
        this.mcmetaInputStream = p_i1300_3_;
        this.srMetadataSerializer = p_i1300_4_;
    }

    public InputStream getInputStream()
    {
        return this.resourceInputStream;
    }

    public boolean hasMetadata()
    {
        return this.mcmetaInputStream != null;
    }

    public IMetadataSection getMetadata(String p_110526_1_)
    {
        if (!this.hasMetadata())
        {
            return null;
        }
        else
        {
            if (this.mcmetaJson == null && !this.mcmetaJsonChecked)
            {
                this.mcmetaJsonChecked = true;
                BufferedReader var2 = null;

                try
                {
                    var2 = new BufferedReader(new InputStreamReader(this.mcmetaInputStream));
                    this.mcmetaJson = (new JsonParser()).parse(var2).getAsJsonObject();
                }
                finally
                {
                    IOUtils.closeQuietly(var2);
                }
            }

            IMetadataSection var6 = (IMetadataSection)this.mapMetadataSections.get(p_110526_1_);

            if (var6 == null)
            {
                var6 = this.srMetadataSerializer.parseMetadataSection(p_110526_1_, this.mcmetaJson);
            }

            return var6;
        }
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ instanceof SimpleResource)
        {
            SimpleResource var2 = (SimpleResource)p_equals_1_;
            return this.srResourceLocation != null ? this.srResourceLocation.equals(var2.srResourceLocation) : var2.srResourceLocation == null;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return this.srResourceLocation == null ? 0 : this.srResourceLocation.hashCode();
    }
}
