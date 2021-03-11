package net.minecraft.client.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class ResourcePackRepository
{
    protected static final FileFilter resourcePackFilter = new FileFilter()
    {

        public boolean accept(File p_accept_1_)
        {
            boolean var2 = p_accept_1_.isFile() && p_accept_1_.getName().endsWith(".zip");
            boolean var3 = p_accept_1_.isDirectory() && (new File(p_accept_1_, "pack.mcmeta")).isFile();
            return var2 || var3;
        }
    };
    private final File dirResourcepacks;
    public final IResourcePack rprDefaultResourcePack;
    private final File field_148534_e;
    public final IMetadataSerializer rprMetadataSerializer;
    private IResourcePack field_148532_f;
    private boolean field_148533_g;
    private List repositoryEntriesAll = Lists.newArrayList();
    private List repositoryEntries = Lists.newArrayList();


    public ResourcePackRepository(File p_i45101_1_, File p_i45101_2_, IResourcePack p_i45101_3_, IMetadataSerializer p_i45101_4_, GameSettings p_i45101_5_)
    {
        this.dirResourcepacks = p_i45101_1_;
        this.field_148534_e = p_i45101_2_;
        this.rprDefaultResourcePack = p_i45101_3_;
        this.rprMetadataSerializer = p_i45101_4_;
        this.fixDirResourcepacks();
        this.updateRepositoryEntriesAll();
        Iterator var6 = p_i45101_5_.resourcePacks.iterator();

        while (var6.hasNext())
        {
            String var7 = (String)var6.next();
            Iterator var8 = this.repositoryEntriesAll.iterator();

            while (var8.hasNext())
            {
                ResourcePackRepository.Entry var9 = (ResourcePackRepository.Entry)var8.next();

                if (var9.getResourcePackName().equals(var7))
                {
                    this.repositoryEntries.add(var9);
                    break;
                }
            }
        }
    }

    private void fixDirResourcepacks()
    {
        if (!this.dirResourcepacks.isDirectory())
        {
            this.dirResourcepacks.delete();
            this.dirResourcepacks.mkdirs();
        }
    }

    private List getResourcePackFiles()
    {
        return this.dirResourcepacks.isDirectory() ? Arrays.asList(this.dirResourcepacks.listFiles(resourcePackFilter)) : Collections.emptyList();
    }

    public void updateRepositoryEntriesAll()
    {
        ArrayList var1 = Lists.newArrayList();
        Iterator var2 = this.getResourcePackFiles().iterator();

        while (var2.hasNext())
        {
            File var3 = (File)var2.next();
            ResourcePackRepository.Entry var4 = new ResourcePackRepository.Entry(var3, null);

            if (!this.repositoryEntriesAll.contains(var4))
            {
                try
                {
                    var4.updateResourcePack();
                    var1.add(var4);
                }
                catch (Exception var6)
                {
                    var1.remove(var4);
                }
            }
            else
            {
                int var5 = this.repositoryEntriesAll.indexOf(var4);

                if (var5 > -1 && var5 < this.repositoryEntriesAll.size())
                {
                    var1.add(this.repositoryEntriesAll.get(var5));
                }
            }
        }

        this.repositoryEntriesAll.removeAll(var1);
        var2 = this.repositoryEntriesAll.iterator();

        while (var2.hasNext())
        {
            ResourcePackRepository.Entry var7 = (ResourcePackRepository.Entry)var2.next();
            var7.closeResourcePack();
        }

        this.repositoryEntriesAll = var1;
    }

    public List getRepositoryEntriesAll()
    {
        return ImmutableList.copyOf(this.repositoryEntriesAll);
    }

    public List getRepositoryEntries()
    {
        return ImmutableList.copyOf(this.repositoryEntries);
    }

    public void func_148527_a(List p_148527_1_)
    {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(p_148527_1_);
    }

    public File getDirResourcepacks()
    {
        return this.dirResourcepacks;
    }

    public void func_148526_a(String p_148526_1_)
    {
        String var2 = p_148526_1_.substring(p_148526_1_.lastIndexOf("/") + 1);

        if (var2.contains("?"))
        {
            var2 = var2.substring(0, var2.indexOf("?"));
        }

        if (var2.endsWith(".zip"))
        {
            File var3 = new File(this.field_148534_e, var2.replaceAll("\\W", ""));
            this.func_148529_f();
            this.func_148528_a(p_148526_1_, var3);
        }
    }

    private void func_148528_a(String p_148528_1_, File p_148528_2_)
    {
        HashMap var3 = Maps.newHashMap();
        GuiScreenWorking var4 = new GuiScreenWorking();
        var3.put("X-Minecraft-Username", Minecraft.getMinecraft().getSession().getUsername());
        var3.put("X-Minecraft-UUID", Minecraft.getMinecraft().getSession().getPlayerID());
        var3.put("X-Minecraft-Version", "1.7.10");
        this.field_148533_g = true;
        Minecraft.getMinecraft().displayGuiScreen(var4);
        HttpUtil.func_151223_a(p_148528_2_, p_148528_1_, new HttpUtil.DownloadListener()
        {

            public void func_148522_a(File p_148522_1_)
            {
                if (ResourcePackRepository.this.field_148533_g)
                {
                    ResourcePackRepository.this.field_148533_g = false;
                    ResourcePackRepository.this.field_148532_f = new FileResourcePack(p_148522_1_);
                    Minecraft.getMinecraft().scheduleResourcesRefresh();
                }
            }
        }, var3, 52428800, var4, Minecraft.getMinecraft().getProxy());
    }

    public IResourcePack func_148530_e()
    {
        return this.field_148532_f;
    }

    public void func_148529_f()
    {
        this.field_148532_f = null;
        this.field_148533_g = false;
    }

    public class Entry
    {
        private final File resourcePackFile;
        private IResourcePack reResourcePack;
        private PackMetadataSection rePackMetadataSection;
        private BufferedImage texturePackIcon;
        private ResourceLocation locationTexturePackIcon;


        private Entry(File p_i1295_2_)
        {
            this.resourcePackFile = p_i1295_2_;
        }

        public void updateResourcePack() throws IOException
        {
            this.reResourcePack = (IResourcePack)(this.resourcePackFile.isDirectory() ? new FolderResourcePack(this.resourcePackFile) : new FileResourcePack(this.resourcePackFile));
            this.rePackMetadataSection = (PackMetadataSection)this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");

            try
            {
                this.texturePackIcon = this.reResourcePack.getPackImage();
            }
            catch (IOException var2)
            {
                ;
            }

            if (this.texturePackIcon == null)
            {
                this.texturePackIcon = ResourcePackRepository.this.rprDefaultResourcePack.getPackImage();
            }

            this.closeResourcePack();
        }

        public void bindTexturePackIcon(TextureManager p_110518_1_)
        {
            if (this.locationTexturePackIcon == null)
            {
                this.locationTexturePackIcon = p_110518_1_.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
            }

            p_110518_1_.bindTexture(this.locationTexturePackIcon);
        }

        public void closeResourcePack()
        {
            if (this.reResourcePack instanceof Closeable)
            {
                IOUtils.closeQuietly((Closeable)this.reResourcePack);
            }
        }

        public IResourcePack getResourcePack()
        {
            return this.reResourcePack;
        }

        public String getResourcePackName()
        {
            return this.reResourcePack.getPackName();
        }

        public String getTexturePackDescription()
        {
            return this.rePackMetadataSection == null ? EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing \'pack\' section)" : this.rePackMetadataSection.func_152805_a().getFormattedText();
        }

        public boolean equals(Object p_equals_1_)
        {
            return this == p_equals_1_ ? true : (p_equals_1_ instanceof ResourcePackRepository.Entry ? this.toString().equals(p_equals_1_.toString()) : false);
        }

        public int hashCode()
        {
            return this.toString().hashCode();
        }

        public String toString()
        {
            return String.format("%s:%s:%d", new Object[] {this.resourcePackFile.getName(), this.resourcePackFile.isDirectory() ? "folder" : "zip", Long.valueOf(this.resourcePackFile.lastModified())});
        }

        Entry(File p_i1296_2_, Object p_i1296_3_)
        {
            this(p_i1296_2_);
        }
    }
}
