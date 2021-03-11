package net.minecraft.client.shader;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonBlendingMode;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class ShaderManager
{
    private static final Logger logger = LogManager.getLogger();
    private static final ShaderDefault defaultShaderUniform = new ShaderDefault();
    private static ShaderManager staticShaderManager = null;
    private static int field_147999_d = -1;
    private static boolean field_148000_e = true;
    private final Map field_147997_f = Maps.newHashMap();
    private final List field_147998_g = Lists.newArrayList();
    private final List field_148010_h = Lists.newArrayList();
    private final List field_148011_i = Lists.newArrayList();
    private final List field_148008_j = Lists.newArrayList();
    private final Map field_148009_k = Maps.newHashMap();
    private final int field_148006_l;
    private final String field_148007_m;
    private final boolean field_148004_n;
    private boolean field_148005_o;
    private final JsonBlendingMode field_148016_p;
    private final List field_148015_q;
    private final List field_148014_r;
    private final ShaderLoader field_148013_s;
    private final ShaderLoader field_148012_t;


    public ShaderManager(IResourceManager p_i45087_1_, String p_i45087_2_) throws JsonException
    {
        JsonParser var3 = new JsonParser();
        ResourceLocation var4 = new ResourceLocation("shaders/program/" + p_i45087_2_ + ".json");
        this.field_148007_m = p_i45087_2_;
        InputStream var5 = null;

        try
        {
            var5 = p_i45087_1_.getResource(var4).getInputStream();
            JsonObject var6 = var3.parse(IOUtils.toString(var5, Charsets.UTF_8)).getAsJsonObject();
            String var7 = JsonUtils.getJsonObjectStringFieldValue(var6, "vertex");
            String var28 = JsonUtils.getJsonObjectStringFieldValue(var6, "fragment");
            JsonArray var9 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var6, "samplers", (JsonArray)null);

            if (var9 != null)
            {
                int var10 = 0;

                for (Iterator var11 = var9.iterator(); var11.hasNext(); ++var10)
                {
                    JsonElement var12 = (JsonElement)var11.next();

                    try
                    {
                        this.func_147996_a(var12);
                    }
                    catch (Exception var25)
                    {
                        JsonException var14 = JsonException.func_151379_a(var25);
                        var14.func_151380_a("samplers[" + var10 + "]");
                        throw var14;
                    }
                }
            }

            JsonArray var29 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var6, "attributes", (JsonArray)null);
            Iterator var32;

            if (var29 != null)
            {
                int var30 = 0;
                this.field_148015_q = Lists.newArrayListWithCapacity(var29.size());
                this.field_148014_r = Lists.newArrayListWithCapacity(var29.size());

                for (var32 = var29.iterator(); var32.hasNext(); ++var30)
                {
                    JsonElement var13 = (JsonElement)var32.next();

                    try
                    {
                        this.field_148014_r.add(JsonUtils.getJsonElementStringValue(var13, "attribute"));
                    }
                    catch (Exception var24)
                    {
                        JsonException var15 = JsonException.func_151379_a(var24);
                        var15.func_151380_a("attributes[" + var30 + "]");
                        throw var15;
                    }
                }
            }
            else
            {
                this.field_148015_q = null;
                this.field_148014_r = null;
            }

            JsonArray var31 = JsonUtils.getJsonObjectJsonArrayFieldOrDefault(var6, "uniforms", (JsonArray)null);

            if (var31 != null)
            {
                int var33 = 0;

                for (Iterator var34 = var31.iterator(); var34.hasNext(); ++var33)
                {
                    JsonElement var36 = (JsonElement)var34.next();

                    try
                    {
                        this.func_147987_b(var36);
                    }
                    catch (Exception var23)
                    {
                        JsonException var16 = JsonException.func_151379_a(var23);
                        var16.func_151380_a("uniforms[" + var33 + "]");
                        throw var16;
                    }
                }
            }

            this.field_148016_p = JsonBlendingMode.func_148110_a(JsonUtils.getJsonObjectFieldOrDefault(var6, "blend", (JsonObject)null));
            this.field_148004_n = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(var6, "cull", true);
            this.field_148013_s = ShaderLoader.func_148057_a(p_i45087_1_, ShaderLoader.ShaderType.VERTEX, var7);
            this.field_148012_t = ShaderLoader.func_148057_a(p_i45087_1_, ShaderLoader.ShaderType.FRAGMENT, var28);
            this.field_148006_l = ShaderLinkHelper.getStaticShaderLinkHelper().func_148078_c();
            ShaderLinkHelper.getStaticShaderLinkHelper().func_148075_b(this);
            this.func_147990_i();

            if (this.field_148014_r != null)
            {
                var32 = this.field_148014_r.iterator();

                while (var32.hasNext())
                {
                    String var35 = (String)var32.next();
                    int var37 = OpenGlHelper.func_153164_b(this.field_148006_l, var35);
                    this.field_148015_q.add(Integer.valueOf(var37));
                }
            }
        }
        catch (Exception var26)
        {
            JsonException var8 = JsonException.func_151379_a(var26);
            var8.func_151381_b(var4.getResourcePath());
            throw var8;
        }
        finally
        {
            IOUtils.closeQuietly(var5);
        }

        this.func_147985_d();
    }

    public void func_147988_a()
    {
        ShaderLinkHelper.getStaticShaderLinkHelper().func_148077_a(this);
    }

    public void func_147993_b()
    {
        OpenGlHelper.func_153161_d(0);
        field_147999_d = -1;
        staticShaderManager = null;
        field_148000_e = true;

        for (int var1 = 0; var1 < this.field_148010_h.size(); ++var1)
        {
            if (this.field_147997_f.get(this.field_147998_g.get(var1)) != null)
            {
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + var1);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            }
        }
    }

    public void func_147995_c()
    {
        this.field_148005_o = false;
        staticShaderManager = this;
        this.field_148016_p.func_148109_a();

        if (this.field_148006_l != field_147999_d)
        {
            OpenGlHelper.func_153161_d(this.field_148006_l);
            field_147999_d = this.field_148006_l;
        }

        if (field_148000_e != this.field_148004_n)
        {
            field_148000_e = this.field_148004_n;

            if (this.field_148004_n)
            {
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
            else
            {
                GL11.glDisable(GL11.GL_CULL_FACE);
            }
        }

        for (int var1 = 0; var1 < this.field_148010_h.size(); ++var1)
        {
            if (this.field_147997_f.get(this.field_147998_g.get(var1)) != null)
            {
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + var1);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                Object var2 = this.field_147997_f.get(this.field_147998_g.get(var1));
                int var3 = -1;

                if (var2 instanceof FrameBuffer)
                {
                    var3 = ((FrameBuffer)var2).framebufferTexture;
                }
                else if (var2 instanceof ITextureObject)
                {
                    var3 = ((ITextureObject)var2).getGlTextureId();
                }
                else if (var2 instanceof Integer)
                {
                    var3 = ((Integer)var2).intValue();
                }

                if (var3 != -1)
                {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, var3);
                    OpenGlHelper.func_153163_f(OpenGlHelper.func_153194_a(this.field_148006_l, (CharSequence)this.field_147998_g.get(var1)), var1);
                }
            }
        }

        Iterator var4 = this.field_148011_i.iterator();

        while (var4.hasNext())
        {
            ShaderUniform var5 = (ShaderUniform)var4.next();
            var5.func_148093_b();
        }
    }

    public void func_147985_d()
    {
        this.field_148005_o = true;
    }

    public ShaderUniform func_147991_a(String p_147991_1_)
    {
        return this.field_148009_k.containsKey(p_147991_1_) ? (ShaderUniform)this.field_148009_k.get(p_147991_1_) : null;
    }

    public ShaderUniform func_147984_b(String p_147984_1_)
    {
        return (ShaderUniform)(this.field_148009_k.containsKey(p_147984_1_) ? (ShaderUniform)this.field_148009_k.get(p_147984_1_) : defaultShaderUniform);
    }

    private void func_147990_i()
    {
        int var1 = 0;
        String var3;
        int var4;

        for (int var2 = 0; var1 < this.field_147998_g.size(); ++var2)
        {
            var3 = (String)this.field_147998_g.get(var1);
            var4 = OpenGlHelper.func_153194_a(this.field_148006_l, var3);

            if (var4 == -1)
            {
                logger.warn("Shader " + this.field_148007_m + "could not find sampler named " + var3 + " in the specified shader program.");
                this.field_147997_f.remove(var3);
                this.field_147998_g.remove(var2);
                --var2;
            }
            else
            {
                this.field_148010_h.add(Integer.valueOf(var4));
            }

            ++var1;
        }

        Iterator var5 = this.field_148011_i.iterator();

        while (var5.hasNext())
        {
            ShaderUniform var6 = (ShaderUniform)var5.next();
            var3 = var6.func_148086_a();
            var4 = OpenGlHelper.func_153194_a(this.field_148006_l, var3);

            if (var4 == -1)
            {
                logger.warn("Could not find uniform named " + var3 + " in the specified" + " shader program.");
            }
            else
            {
                this.field_148008_j.add(Integer.valueOf(var4));
                var6.func_148084_b(var4);
                this.field_148009_k.put(var3, var6);
            }
        }
    }

    private void func_147996_a(JsonElement p_147996_1_)
    {
        JsonObject var2 = JsonUtils.getJsonElementAsJsonObject(p_147996_1_, "sampler");
        String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");

        if (!JsonUtils.jsonObjectFieldTypeIsString(var2, "file"))
        {
            this.field_147997_f.put(var3, (Object)null);
            this.field_147998_g.add(var3);
        }
        else
        {
            this.field_147998_g.add(var3);
        }
    }

    public void func_147992_a(String p_147992_1_, Object p_147992_2_)
    {
        if (this.field_147997_f.containsKey(p_147992_1_))
        {
            this.field_147997_f.remove(p_147992_1_);
        }

        this.field_147997_f.put(p_147992_1_, p_147992_2_);
        this.func_147985_d();
    }

    private void func_147987_b(JsonElement p_147987_1_) throws JsonException
    {
        JsonObject var2 = JsonUtils.getJsonElementAsJsonObject(p_147987_1_, "uniform");
        String var3 = JsonUtils.getJsonObjectStringFieldValue(var2, "name");
        int var4 = ShaderUniform.func_148085_a(JsonUtils.getJsonObjectStringFieldValue(var2, "type"));
        int var5 = JsonUtils.getJsonObjectIntegerFieldValue(var2, "count");
        float[] var6 = new float[Math.max(var5, 16)];
        JsonArray var7 = JsonUtils.getJsonObjectJsonArrayField(var2, "values");

        if (var7.size() != var5 && var7.size() > 1)
        {
            throw new JsonException("Invalid amount of values specified (expected " + var5 + ", found " + var7.size() + ")");
        }
        else
        {
            int var8 = 0;

            for (Iterator var9 = var7.iterator(); var9.hasNext(); ++var8)
            {
                JsonElement var10 = (JsonElement)var9.next();

                try
                {
                    var6[var8] = JsonUtils.getJsonElementFloatValue(var10, "value");
                }
                catch (Exception var13)
                {
                    JsonException var12 = JsonException.func_151379_a(var13);
                    var12.func_151380_a("values[" + var8 + "]");
                    throw var12;
                }
            }

            if (var5 > 1 && var7.size() == 1)
            {
                while (var8 < var5)
                {
                    var6[var8] = var6[0];
                    ++var8;
                }
            }

            int var14 = var5 > 1 && var5 <= 4 && var4 < 8 ? var5 - 1 : 0;
            ShaderUniform var15 = new ShaderUniform(var3, var4 + var14, var5, this);

            if (var4 <= 3)
            {
                var15.func_148083_a((int)var6[0], (int)var6[1], (int)var6[2], (int)var6[3]);
            }
            else if (var4 <= 7)
            {
                var15.func_148092_b(var6[0], var6[1], var6[2], var6[3]);
            }
            else
            {
                var15.func_148097_a(var6);
            }

            this.field_148011_i.add(var15);
        }
    }

    public ShaderLoader func_147989_e()
    {
        return this.field_148013_s;
    }

    public ShaderLoader func_147994_f()
    {
        return this.field_148012_t;
    }

    public int func_147986_h()
    {
        return this.field_148006_l;
    }
}
