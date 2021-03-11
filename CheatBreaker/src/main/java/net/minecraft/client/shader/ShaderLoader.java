package net.minecraft.client.shader;

import com.google.common.collect.Maps;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.BufferUtils;

public class ShaderLoader
{
    private final ShaderLoader.ShaderType field_148061_a;
    private final String field_148059_b;
    private int field_148060_c;
    private int field_148058_d = 0;


    private ShaderLoader(ShaderLoader.ShaderType p_i45091_1_, int p_i45091_2_, String p_i45091_3_)
    {
        this.field_148061_a = p_i45091_1_;
        this.field_148060_c = p_i45091_2_;
        this.field_148059_b = p_i45091_3_;
    }

    public void func_148056_a(ShaderManager p_148056_1_)
    {
        ++this.field_148058_d;
        OpenGlHelper.func_153178_b(p_148056_1_.func_147986_h(), this.field_148060_c);
    }

    public void func_148054_b(ShaderManager p_148054_1_)
    {
        --this.field_148058_d;

        if (this.field_148058_d <= 0)
        {
            OpenGlHelper.func_153180_a(this.field_148060_c);
            this.field_148061_a.func_148064_d().remove(this.field_148059_b);
        }
    }

    public String func_148055_a()
    {
        return this.field_148059_b;
    }

    public static ShaderLoader func_148057_a(IResourceManager p_148057_0_, ShaderLoader.ShaderType p_148057_1_, String p_148057_2_) throws IOException
    {
        ShaderLoader var3 = (ShaderLoader)p_148057_1_.func_148064_d().get(p_148057_2_);

        if (var3 == null)
        {
            ResourceLocation var4 = new ResourceLocation("shaders/program/" + p_148057_2_ + p_148057_1_.func_148063_b());
            BufferedInputStream var5 = new BufferedInputStream(p_148057_0_.getResource(var4).getInputStream());
            byte[] var6 = IOUtils.toByteArray(var5);
            ByteBuffer var7 = BufferUtils.createByteBuffer(var6.length);
            var7.put(var6);
            var7.position(0);
            int var8 = OpenGlHelper.func_153195_b(p_148057_1_.func_148065_c());
            OpenGlHelper.func_153169_a(var8, var7);
            OpenGlHelper.func_153170_c(var8);

            if (OpenGlHelper.func_153157_c(var8, OpenGlHelper.field_153208_p) == 0)
            {
                String var9 = StringUtils.trim(OpenGlHelper.func_153158_d(var8, 32768));
                JsonException var10 = new JsonException("Couldn\'t compile " + p_148057_1_.func_148062_a() + " program: " + var9);
                var10.func_151381_b(var4.getResourcePath());
                throw var10;
            }

            var3 = new ShaderLoader(p_148057_1_, var8, p_148057_2_);
            p_148057_1_.func_148064_d().put(p_148057_2_, var3);
        }

        return var3;
    }

    public static enum ShaderType
    {
        VERTEX("VERTEX", 0, "vertex", ".vsh", OpenGlHelper.field_153209_q),
        FRAGMENT("FRAGMENT", 1, "fragment", ".fsh", OpenGlHelper.field_153210_r);
        private final String field_148072_c;
        private final String field_148069_d;
        private final int field_148070_e;
        private final Map field_148067_f = Maps.newHashMap();

        private static final ShaderLoader.ShaderType[] $VALUES = new ShaderLoader.ShaderType[]{VERTEX, FRAGMENT};


        private ShaderType(String p_i45090_1_, int p_i45090_2_, String p_i45090_3_, String p_i45090_4_, int p_i45090_5_)
        {
            this.field_148072_c = p_i45090_3_;
            this.field_148069_d = p_i45090_4_;
            this.field_148070_e = p_i45090_5_;
        }

        public String func_148062_a()
        {
            return this.field_148072_c;
        }

        protected String func_148063_b()
        {
            return this.field_148069_d;
        }

        protected int func_148065_c()
        {
            return this.field_148070_e;
        }

        protected Map func_148064_d()
        {
            return this.field_148067_f;
        }
    }
}
