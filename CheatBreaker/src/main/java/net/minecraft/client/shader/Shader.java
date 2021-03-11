package net.minecraft.client.shader;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import org.lwjgl.opengl.GL11;

public class Shader
{
    private final ShaderManager manager;
    public final FrameBuffer frameBufferIn;
    public final FrameBuffer frameBufferOut;
    private final List listAuxFramebuffers = Lists.newArrayList();
    private final List listAuxNames = Lists.newArrayList();
    private final List listAuxWidths = Lists.newArrayList();
    private final List listAuxHeights = Lists.newArrayList();
    private Matrix4f projectionMatrix;


    public Shader(IResourceManager p_i45089_1_, String p_i45089_2_, FrameBuffer p_i45089_3_, FrameBuffer p_i45089_4_) throws JsonException
    {
        this.manager = new ShaderManager(p_i45089_1_, p_i45089_2_);
        this.frameBufferIn = p_i45089_3_;
        this.frameBufferOut = p_i45089_4_;
    }

    public void deleteShader()
    {
        this.manager.func_147988_a();
    }

    public void addAuxFramebuffer(String p_148041_1_, Object p_148041_2_, int p_148041_3_, int p_148041_4_)
    {
        this.listAuxNames.add(this.listAuxNames.size(), p_148041_1_);
        this.listAuxFramebuffers.add(this.listAuxFramebuffers.size(), p_148041_2_);
        this.listAuxWidths.add(this.listAuxWidths.size(), Integer.valueOf(p_148041_3_));
        this.listAuxHeights.add(this.listAuxHeights.size(), Integer.valueOf(p_148041_4_));
    }

    private void preLoadShader()
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public void setProjectionMatrix(Matrix4f p_148045_1_)
    {
        this.projectionMatrix = p_148045_1_;
    }

    public void loadShader(float p_148042_1_)
    {
        this.preLoadShader();
        this.frameBufferIn.unbindFramebuffer();
        float var2 = (float)this.frameBufferOut.framebufferTextureWidth;
        float var3 = (float)this.frameBufferOut.framebufferTextureHeight;
        GL11.glViewport(0, 0, (int)var2, (int)var3);
        this.manager.func_147992_a("DiffuseSampler", this.frameBufferIn);

        for (int var4 = 0; var4 < this.listAuxFramebuffers.size(); ++var4)
        {
            this.manager.func_147992_a((String)this.listAuxNames.get(var4), this.listAuxFramebuffers.get(var4));
            this.manager.func_147984_b("AuxSize" + var4).func_148087_a((float)((Integer)this.listAuxWidths.get(var4)).intValue(), (float)((Integer)this.listAuxHeights.get(var4)).intValue());
        }

        this.manager.func_147984_b("ProjMat").func_148088_a(this.projectionMatrix);
        this.manager.func_147984_b("InSize").func_148087_a((float)this.frameBufferIn.framebufferTextureWidth, (float)this.frameBufferIn.framebufferTextureHeight);
        this.manager.func_147984_b("OutSize").func_148087_a(var2, var3);
        this.manager.func_147984_b("Time").func_148090_a(p_148042_1_);
        Minecraft var8 = Minecraft.getMinecraft();
        this.manager.func_147984_b("ScreenSize").func_148087_a((float)var8.displayWidth, (float)var8.displayHeight);
        this.manager.func_147995_c();
        this.frameBufferOut.framebufferClear();
        this.frameBufferOut.bindFramebuffer(false);
        GL11.glDepthMask(false);
        GL11.glColorMask(true, true, true, false);
        Tessellator var5 = Tessellator.instance;
        var5.startDrawingQuads();
        var5.setColorOpaque_I(-1);
        var5.addVertex(0.0D, (double)var3, 500.0D);
        var5.addVertex((double)var2, (double)var3, 500.0D);
        var5.addVertex((double)var2, 0.0D, 500.0D);
        var5.addVertex(0.0D, 0.0D, 500.0D);
        var5.draw();
        GL11.glDepthMask(true);
        GL11.glColorMask(true, true, true, true);
        this.manager.func_147993_b();
        this.frameBufferOut.unbindFramebuffer();
        this.frameBufferIn.unbindFramebufferTexture();
        Iterator var6 = this.listAuxFramebuffers.iterator();

        while (var6.hasNext())
        {
            Object var7 = var6.next();

            if (var7 instanceof FrameBuffer)
            {
                ((FrameBuffer)var7).unbindFramebufferTexture();
            }
        }
    }

    public ShaderManager getShaderManager()
    {
        return this.manager;
    }
}
