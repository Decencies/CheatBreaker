package net.minecraft.client.shader;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.util.JsonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShaderLinkHelper
{
    private static final Logger logger = LogManager.getLogger();
    private static ShaderLinkHelper staticShaderLinkHelper;


    public static void setNewStaticShaderLinkHelper()
    {
        staticShaderLinkHelper = new ShaderLinkHelper();
    }

    public static ShaderLinkHelper getStaticShaderLinkHelper()
    {
        return staticShaderLinkHelper;
    }

    public void func_148077_a(ShaderManager p_148077_1_)
    {
        p_148077_1_.func_147994_f().func_148054_b(p_148077_1_);
        p_148077_1_.func_147989_e().func_148054_b(p_148077_1_);
        OpenGlHelper.func_153187_e(p_148077_1_.func_147986_h());
    }

    public int func_148078_c() throws JsonException
    {
        int var1 = OpenGlHelper.func_153183_d();

        if (var1 <= 0)
        {
            throw new JsonException("Could not create shader program (returned program ID " + var1 + ")");
        }
        else
        {
            return var1;
        }
    }

    public void func_148075_b(ShaderManager p_148075_1_)
    {
        p_148075_1_.func_147994_f().func_148056_a(p_148075_1_);
        p_148075_1_.func_147989_e().func_148056_a(p_148075_1_);
        OpenGlHelper.func_153179_f(p_148075_1_.func_147986_h());
        int var2 = OpenGlHelper.func_153175_a(p_148075_1_.func_147986_h(), OpenGlHelper.field_153207_o);

        if (var2 == 0)
        {
            logger.warn("Error encountered when linking program containing VS " + p_148075_1_.func_147989_e().func_148055_a() + " and FS " + p_148075_1_.func_147994_f().func_148055_a() + ". Log output:");
            logger.warn(OpenGlHelper.func_153166_e(p_148075_1_.func_147986_h(), 32768));
        }
    }
}
