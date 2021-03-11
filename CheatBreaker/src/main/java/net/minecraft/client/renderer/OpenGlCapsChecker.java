package net.minecraft.client.renderer;

import org.lwjgl.opengl.GLContext;

public class OpenGlCapsChecker
{


    /**
     * Checks if we support OpenGL occlusion.
     */
    public static boolean checkARBOcclusion()
    {
        return GLContext.getCapabilities().GL_ARB_occlusion_query;
    }
}
