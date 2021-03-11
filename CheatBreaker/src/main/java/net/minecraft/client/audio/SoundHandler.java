package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundHandler implements IResourceManagerReloadListener, IUpdatePlayerListBox
{
    private static final Logger logger = LogManager.getLogger();
    private static final Gson field_147699_c = (new GsonBuilder()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
    private static final ParameterizedType field_147696_d = new ParameterizedType()
    {

        public Type[] getActualTypeArguments()
        {
            return new Type[] {String.class, SoundList.class};
        }
        public Type getRawType()
        {
            return Map.class;
        }
        public Type getOwnerType()
        {
            return null;
        }
    };
    public static final SoundPoolEntry field_147700_a = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"), 0.0D, 0.0D, false);
    private final SoundRegistry field_147697_e = new SoundRegistry();
    public final SoundManager field_147694_f;
    private final IResourceManager field_147695_g;


    public SoundHandler(IResourceManager p_i45122_1_, GameSettings p_i45122_2_)
    {
        this.field_147695_g = p_i45122_1_;
        this.field_147694_f = new SoundManager(this, p_i45122_2_);
    }

    public void onResourceManagerReload(IResourceManager p_110549_1_)
    {
        this.field_147694_f.func_148596_a();
        this.field_147697_e.func_148763_c();
        Iterator var2 = p_110549_1_.getResourceDomains().iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();

            try
            {
                List var4 = p_110549_1_.getAllResources(new ResourceLocation(var3, "sounds.json"));
                Iterator var5 = var4.iterator();

                while (var5.hasNext())
                {
                    IResource var6 = (IResource)var5.next();

                    try
                    {
                        Map var7 = (Map)field_147699_c.fromJson(new InputStreamReader(var6.getInputStream()), field_147696_d);
                        Iterator var8 = var7.entrySet().iterator();

                        while (var8.hasNext())
                        {
                            Entry var9 = (Entry)var8.next();
                            this.func_147693_a(new ResourceLocation(var3, (String)var9.getKey()), (SoundList)var9.getValue());
                        }
                    }
                    catch (RuntimeException var10)
                    {
                        logger.warn("Invalid sounds.json", var10);
                    }
                }
            }
            catch (IOException var11)
            {
                ;
            }
        }
    }

    private void func_147693_a(ResourceLocation p_147693_1_, SoundList p_147693_2_)
    {
        SoundEventAccessorComposite var3;

        if (this.field_147697_e.containsKey(p_147693_1_) && !p_147693_2_.func_148574_b())
        {
            var3 = (SoundEventAccessorComposite)this.field_147697_e.getObject(p_147693_1_);
        }
        else
        {
            logger.debug("Registered/replaced new sound event location {}", new Object[] {p_147693_1_});
            var3 = new SoundEventAccessorComposite(p_147693_1_, 1.0D, 1.0D, p_147693_2_.func_148573_c());
            this.field_147697_e.func_148762_a(var3);
        }

        Iterator var4 = p_147693_2_.func_148570_a().iterator();

        while (var4.hasNext())
        {
            final SoundList.SoundEntry var5 = (SoundList.SoundEntry)var4.next();
            String var6 = var5.func_148556_a();
            ResourceLocation var7 = new ResourceLocation(var6);
            final String var8 = var6.contains(":") ? var7.getResourceDomain() : p_147693_1_.getResourceDomain();
            Object var9;

            switch (SoundHandler.SwitchType.field_148765_a[var5.func_148563_e().ordinal()])
            {
                case 1:
                    ResourceLocation var10 = new ResourceLocation(var8, "sounds/" + var7.getResourcePath() + ".ogg");

                    try
                    {
                        this.field_147695_g.getResource(var10);
                    }
                    catch (FileNotFoundException var12)
                    {
                        logger.warn("File {} does not exist, cannot add it to event {}", new Object[] {var10, p_147693_1_});
                        continue;
                    }
                    catch (IOException var13)
                    {
                        logger.warn("Could not load sound file " + var10 + ", cannot add it to event " + p_147693_1_, var13);
                        continue;
                    }

                    var9 = new SoundEventAccessor(new SoundPoolEntry(var10, (double)var5.func_148560_c(), (double)var5.func_148558_b(), var5.func_148552_f()), var5.func_148555_d());
                    break;

                case 2:
                    var9 = new ISoundEventAccessor()
                    {
                        final ResourceLocation field_148726_a = new ResourceLocation(var8, var5.func_148556_a());

                        public int func_148721_a()
                        {
                            SoundEventAccessorComposite var1 = (SoundEventAccessorComposite)SoundHandler.this.field_147697_e.getObject(this.field_148726_a);
                            return var1 == null ? 0 : var1.func_148721_a();
                        }
                        public SoundPoolEntry func_148720_g()
                        {
                            SoundEventAccessorComposite var1 = (SoundEventAccessorComposite)SoundHandler.this.field_147697_e.getObject(this.field_148726_a);
                            return var1 == null ? SoundHandler.field_147700_a : var1.func_148720_g();
                        }
                    };

                    break;
                default:
                    throw new IllegalStateException("IN YOU FACE");
            }

            var3.func_148727_a((ISoundEventAccessor)var9);
        }
    }

    public SoundEventAccessorComposite func_147680_a(ResourceLocation p_147680_1_)
    {
        return (SoundEventAccessorComposite)this.field_147697_e.getObject(p_147680_1_);
    }

    /**
     * Play a sound
     */
    public void playSound(ISound p_147682_1_)
    {
        this.field_147694_f.func_148611_c(p_147682_1_);
    }

    /**
     * Plays the sound in n ticks
     */
    public void playDelayedSound(ISound p_147681_1_, int p_147681_2_)
    {
        this.field_147694_f.func_148599_a(p_147681_1_, p_147681_2_);
    }

    public void func_147691_a(EntityPlayer p_147691_1_, float p_147691_2_)
    {
        this.field_147694_f.func_148615_a(p_147691_1_, p_147691_2_);
    }

    public void func_147689_b()
    {
        this.field_147694_f.func_148610_e();
    }

    public void func_147690_c()
    {
        this.field_147694_f.func_148614_c();
    }

    public void func_147685_d()
    {
        this.field_147694_f.func_148613_b();
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        this.field_147694_f.func_148605_d();
    }

    public void func_147687_e()
    {
        this.field_147694_f.func_148604_f();
    }

    public void setSoundLevel(SoundCategory p_147684_1_, float p_147684_2_)
    {
        if (p_147684_1_ == SoundCategory.MASTER && p_147684_2_ <= 0.0F)
        {
            this.func_147690_c();
        }

        this.field_147694_f.func_148601_a(p_147684_1_, p_147684_2_);
    }

    public void func_147683_b(ISound p_147683_1_)
    {
        this.field_147694_f.func_148602_b(p_147683_1_);
    }

    public SoundEventAccessorComposite func_147686_a(SoundCategory ... p_147686_1_)
    {
        ArrayList var2 = Lists.newArrayList();
        Iterator var3 = this.field_147697_e.getKeys().iterator();

        while (var3.hasNext())
        {
            ResourceLocation var4 = (ResourceLocation)var3.next();
            SoundEventAccessorComposite var5 = (SoundEventAccessorComposite)this.field_147697_e.getObject(var4);

            if (ArrayUtils.contains(p_147686_1_, var5.func_148728_d()))
            {
                var2.add(var5);
            }
        }

        if (var2.isEmpty())
        {
            return null;
        }
        else
        {
            return (SoundEventAccessorComposite)var2.get((new Random()).nextInt(var2.size()));
        }
    }

    public boolean func_147692_c(ISound p_147692_1_)
    {
        return this.field_147694_f.func_148597_a(p_147692_1_);
    }

    static final class SwitchType
    {
        static final int[] field_148765_a = new int[SoundList.SoundEntry.Type.values().length];


        static
        {
            try
            {
                field_148765_a[SoundList.SoundEntry.Type.FILE.ordinal()] = 1;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_148765_a[SoundList.SoundEntry.Type.SOUND_EVENT.ordinal()] = 2;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
