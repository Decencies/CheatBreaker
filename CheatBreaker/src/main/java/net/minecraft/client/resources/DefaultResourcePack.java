package net.minecraft.client.resources;

import com.google.common.collect.ImmutableSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.src.ReflectorForge;
import net.minecraft.util.ResourceLocation;

public class DefaultResourcePack implements IResourcePack
{
    public static final Set defaultResourceDomains = ImmutableSet.of("minecraft", "realms");
    private final Map field_152781_b;



    public DefaultResourcePack(Map par1GuiScreen)
    {
        this.field_152781_b = par1GuiScreen;
    }

    public InputStream getInputStream(ResourceLocation par1ResourceLocation) throws IOException
    {
        InputStream var2 = this.getResourceStream(par1ResourceLocation);

        if (var2 != null)
        {
            return var2;
        }
        else
        {
            InputStream var3 = this.func_152780_c(par1ResourceLocation);

            if (var3 != null)
            {
                return var3;
            }
            else
            {
                throw new FileNotFoundException(par1ResourceLocation.getResourcePath());
            }
        }
    }

    public InputStream func_152780_c(ResourceLocation p_152780_1_) throws IOException
    {
        File var2 = (File)this.field_152781_b.get(p_152780_1_.toString());
        return var2 != null && var2.isFile() ? new FileInputStream(var2) : null;
    }

    private InputStream getResourceStream(ResourceLocation par1ResourceLocation)
    {
        String path = "/assets/" + par1ResourceLocation.getResourceDomain() + "/" + par1ResourceLocation.getResourcePath();
        InputStream is = ReflectorForge.getOptiFineResourceStream(path);
        return is != null ? is : DefaultResourcePack.class.getResourceAsStream("/assets/" + par1ResourceLocation.getResourceDomain() + "/" + par1ResourceLocation.getResourcePath());
    }

    public boolean resourceExists(ResourceLocation par1ResourceLocation)
    {
        return this.getResourceStream(par1ResourceLocation) != null || this.field_152781_b.containsKey(par1ResourceLocation.toString());
    }

    public Set getResourceDomains()
    {
        return defaultResourceDomains;
    }

    public IMetadataSection getPackMetadata(IMetadataSerializer par1MetadataSerializer, String par2Str) throws IOException
    {
        try
        {
            FileInputStream var5 = new FileInputStream((File)this.field_152781_b.get("pack.mcmeta"));
            return AbstractResourcePack.readMetadata(par1MetadataSerializer, var5, par2Str);
        }
        catch (RuntimeException var4)
        {
            return null;
        }
        catch (FileNotFoundException var51)
        {
            return null;
        }
    }

    public BufferedImage getPackImage() throws IOException
    {
        return ImageIO.read(DefaultResourcePack.class.getResourceAsStream("/" + (new ResourceLocation("pack.png")).getResourcePath()));
    }

    public String getPackName()
    {
        return "Default";
    }
}
