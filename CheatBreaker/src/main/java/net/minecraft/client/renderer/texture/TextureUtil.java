package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.src.Mipmaps;
import net.minecraft.src.Reflector;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL12;

public class TextureUtil
{
    private static final Logger logger = LogManager.getLogger();
    private static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
    public static final DynamicTexture missingTexture = new DynamicTexture(16, 16);
    public static final int[] missingTextureData = missingTexture.getTextureData();
    private static int field_147958_e = -1;
    private static int field_147956_f = -1;
    private static float field_152779_g = -1.0F;
    private static final int[] field_147957_g;



    public static int glGenTextures()
    {
        return GL11.glGenTextures();
    }

    public static void deleteTexture(int p_147942_0_)
    {
        GL11.glDeleteTextures(p_147942_0_);
    }

    public static int uploadTextureImage(int par0, BufferedImage par1BufferedImage)
    {
        return uploadTextureImageAllocate(par0, par1BufferedImage, false, false);
    }

    public static void uploadTexture(int par0, int[] par1ArrayOfInteger, int par2, int par3)
    {
        bindTexture(par0);
        func_147947_a(0, par1ArrayOfInteger, par2, par3, 0, 0, false, false, false);
    }

    public static int[][] func_147949_a(int p_147949_0_, int p_147949_1_, int[][] p_147949_2_)
    {
        int[][] var3 = new int[p_147949_0_ + 1][];
        var3[0] = p_147949_2_[0];

        if (p_147949_0_ > 0)
        {
            boolean var4 = false;
            int var5;

            for (var5 = 0; var5 < p_147949_2_.length; ++var5)
            {
                if (p_147949_2_[0][var5] >> 24 == 0)
                {
                    var4 = true;
                    break;
                }
            }

            for (var5 = 1; var5 <= p_147949_0_; ++var5)
            {
                if (p_147949_2_[var5] != null)
                {
                    var3[var5] = p_147949_2_[var5];
                }
                else
                {
                    int[] var6 = var3[var5 - 1];
                    int[] var7 = new int[var6.length >> 2];
                    int var8 = p_147949_1_ >> var5;
                    int var9 = var7.length / var8;
                    int var10 = var8 << 1;

                    for (int var11 = 0; var11 < var8; ++var11)
                    {
                        for (int var12 = 0; var12 < var9; ++var12)
                        {
                            int var13 = 2 * (var11 + var12 * var10);
                            var7[var11 + var12 * var8] = func_147943_a(var6[var13 + 0], var6[var13 + 1], var6[var13 + 0 + var10], var6[var13 + 1 + var10], var4);
                        }
                    }

                    var3[var5] = var7;
                }
            }
        }

        return var3;
    }

    private static int func_147943_a(int p_147943_0_, int p_147943_1_, int p_147943_2_, int p_147943_3_, boolean p_147943_4_)
    {
        return Mipmaps.alphaBlend(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_);
    }

    private static int func_147944_a(int p_147944_0_, int p_147944_1_, int p_147944_2_, int p_147944_3_, int p_147944_4_)
    {
        float var5 = (float)Math.pow((double)((float)(p_147944_0_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
        float var6 = (float)Math.pow((double)((float)(p_147944_1_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
        float var7 = (float)Math.pow((double)((float)(p_147944_2_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
        float var8 = (float)Math.pow((double)((float)(p_147944_3_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
        float var9 = (float)Math.pow((double)(var5 + var6 + var7 + var8) * 0.25D, 0.45454545454545453D);
        return (int)((double)var9 * 255.0D);
    }

    public static void func_147955_a(int[][] p_147955_0_, int p_147955_1_, int p_147955_2_, int p_147955_3_, int p_147955_4_, boolean p_147955_5_, boolean p_147955_6_)
    {
        for (int var7 = 0; var7 < p_147955_0_.length; ++var7)
        {
            int[] var8 = p_147955_0_[var7];
            func_147947_a(var7, var8, p_147955_1_ >> var7, p_147955_2_ >> var7, p_147955_3_ >> var7, p_147955_4_ >> var7, p_147955_5_, p_147955_6_, p_147955_0_.length > 1);
        }
    }

    private static void func_147947_a(int p_147947_0_, int[] p_147947_1_, int p_147947_2_, int p_147947_3_, int p_147947_4_, int p_147947_5_, boolean p_147947_6_, boolean p_147947_7_, boolean p_147947_8_)
    {
        int var9 = 4194304 / p_147947_2_;
        func_147954_b(p_147947_6_, p_147947_8_);
        setTextureClamped(p_147947_7_);
        int var12;

        for (int var10 = 0; var10 < p_147947_2_ * p_147947_3_; var10 += p_147947_2_ * var12)
        {
            int var11 = var10 / p_147947_2_;
            var12 = Math.min(var9, p_147947_3_ - var11);
            int var13 = p_147947_2_ * var12;
            copyToBufferPos(p_147947_1_, var10, var13);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, p_147947_0_, p_147947_4_, p_147947_5_ + var11, p_147947_2_, var12, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, dataBuffer);
        }
    }

    public static int uploadTextureImageAllocate(int par0, BufferedImage par1BufferedImage, boolean par2, boolean par3)
    {
        allocateTexture(par0, par1BufferedImage.getWidth(), par1BufferedImage.getHeight());
        return uploadTextureImageSub(par0, par1BufferedImage, 0, 0, par2, par3);
    }

    public static void allocateTexture(int par0, int par1, int par2)
    {
        func_147946_a(par0, 0, par1, par2, 1.0F);
    }

    public static void func_147946_a(int p_147946_0_, int p_147946_1_, int p_147946_2_, int p_147946_3_, float p_147946_4_)
    {
        Class monitor = TextureUtil.class;

        if (Reflector.SplashScreen.exists())
        {
            monitor = Reflector.SplashScreen.getTargetClass();
        }

        synchronized (monitor)
        {
            deleteTexture(p_147946_0_);
            bindTexture(p_147946_0_);
        }

        if (OpenGlHelper.anisotropicFilteringSupported)
        {
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34046, p_147946_4_);
        }

        if (p_147946_1_ > 0)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, p_147946_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MIN_LOD, 0.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LOD, (float)p_147946_1_);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0.0F);
        }

        for (int var5 = 0; var5 <= p_147946_1_; ++var5)
        {
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, var5, GL11.GL_RGBA, p_147946_2_ >> var5, p_147946_3_ >> var5, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer)null);
        }
    }

    public static int uploadTextureImageSub(int par0, BufferedImage par1BufferedImage, int par2, int par3, boolean par4, boolean par5)
    {
        bindTexture(par0);
        uploadTextureImageSubImpl(par1BufferedImage, par2, par3, par4, par5);
        return par0;
    }

    private static void uploadTextureImageSubImpl(BufferedImage par0BufferedImage, int par1, int par2, boolean par3, boolean par4)
    {
        int var5 = par0BufferedImage.getWidth();
        int var6 = par0BufferedImage.getHeight();
        int var7 = 4194304 / var5;
        int[] var8 = new int[var7 * var5];
        func_147951_b(par3);
        setTextureClamped(par4);

        for (int var9 = 0; var9 < var5 * var6; var9 += var5 * var7)
        {
            int var10 = var9 / var5;
            int var11 = Math.min(var7, var6 - var10);
            int var12 = var5 * var11;
            par0BufferedImage.getRGB(0, var10, var5, var11, var8, 0, var5);
            copyToBuffer(var8, var12);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, par1, par2 + var10, var5, var11, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, dataBuffer);
        }
    }

    private static void setTextureClamped(boolean par0)
    {
        if (par0)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        }
    }

    private static void func_147951_b(boolean p_147951_0_)
    {
        func_147954_b(p_147951_0_, false);
    }

    public static void func_152777_a(boolean p_152777_0_, boolean p_152777_1_, float p_152777_2_)
    {
        field_147958_e = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER);
        field_147956_f = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER);
        field_152779_g = GL11.glGetTexParameterf(GL11.GL_TEXTURE_2D, 34046);
        func_147954_b(p_152777_0_, p_152777_1_);
        func_152778_a(p_152777_2_);
    }

    public static void func_147945_b()
    {
        if (field_147958_e >= 0 && field_147956_f >= 0 && field_152779_g >= 0.0F)
        {
            func_147952_b(field_147958_e, field_147956_f);
            func_152778_a(field_152779_g);
            field_152779_g = -1.0F;
            field_147958_e = -1;
            field_147956_f = -1;
        }
    }

    private static void func_147952_b(int p_147952_0_, int p_147952_1_)
    {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, p_147952_0_);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, p_147952_1_);
    }

    private static void func_152778_a(float p_152778_0_)
    {
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34046, p_152778_0_);
    }

    private static void func_147954_b(boolean p_147954_0_, boolean p_147954_1_)
    {
        if (p_147954_0_)
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, p_147954_1_ ? GL11.GL_LINEAR_MIPMAP_LINEAR : GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }
        else
        {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, p_147954_1_ ? GL11.GL_NEAREST_MIPMAP_LINEAR : GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
    }

    private static void copyToBuffer(int[] par0ArrayOfInteger, int par1)
    {
        copyToBufferPos(par0ArrayOfInteger, 0, par1);
    }

    private static void copyToBufferPos(int[] par0ArrayOfInteger, int par1, int par2)
    {
        int[] var3 = par0ArrayOfInteger;

        if (Minecraft.getMinecraft().gameSettings.anaglyph)
        {
            var3 = updateAnaglyph(par0ArrayOfInteger);
        }

        dataBuffer.clear();
        dataBuffer.put(var3, par1, par2);
        dataBuffer.position(0).limit(par2);
    }

    static void bindTexture(int par0)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, par0);
    }

    public static int[] readImageData(IResourceManager par0ResourceManager, ResourceLocation par1ResourceLocation) throws IOException
    {
        BufferedImage var2 = ImageIO.read(par0ResourceManager.getResource(par1ResourceLocation).getInputStream());

        if (var2 == null)
        {
            return null;
        }
        else
        {
            int var3 = var2.getWidth();
            int var4 = var2.getHeight();
            int[] var5 = new int[var3 * var4];
            var2.getRGB(0, 0, var3, var4, var5, 0, var3);
            return var5;
        }
    }

    public static int[] updateAnaglyph(int[] par0ArrayOfInteger)
    {
        int[] var1 = new int[par0ArrayOfInteger.length];

        for (int var2 = 0; var2 < par0ArrayOfInteger.length; ++var2)
        {
            int var3 = par0ArrayOfInteger[var2] >> 24 & 255;
            int var4 = par0ArrayOfInteger[var2] >> 16 & 255;
            int var5 = par0ArrayOfInteger[var2] >> 8 & 255;
            int var6 = par0ArrayOfInteger[var2] & 255;
            int var7 = (var4 * 30 + var5 * 59 + var6 * 11) / 100;
            int var8 = (var4 * 30 + var5 * 70) / 100;
            int var9 = (var4 * 30 + var6 * 70) / 100;
            var1[var2] = var3 << 24 | var7 << 16 | var8 << 8 | var9;
        }

        return var1;
    }

    public static int[] func_147948_a(int[] p_147948_0_, int p_147948_1_, int p_147948_2_, int p_147948_3_)
    {
        int var4 = p_147948_1_ + 2 * p_147948_3_;
        int var5;
        int var6;

        for (var5 = p_147948_2_ - 1; var5 >= 0; --var5)
        {
            var6 = var5 * p_147948_1_;
            int var7 = p_147948_3_ + (var5 + p_147948_3_) * var4;
            int var8;

            for (var8 = 0; var8 < p_147948_3_; var8 += p_147948_1_)
            {
                int var9 = Math.min(p_147948_1_, p_147948_3_ - var8);
                System.arraycopy(p_147948_0_, var6 + p_147948_1_ - var9, p_147948_0_, var7 - var8 - var9, var9);
            }

            System.arraycopy(p_147948_0_, var6, p_147948_0_, var7, p_147948_1_);

            for (var8 = 0; var8 < p_147948_3_; var8 += p_147948_1_)
            {
                System.arraycopy(p_147948_0_, var6, p_147948_0_, var7 + p_147948_1_ + var8, Math.min(p_147948_1_, p_147948_3_ - var8));
            }
        }

        for (var5 = 0; var5 < p_147948_3_; var5 += p_147948_2_)
        {
            var6 = Math.min(p_147948_2_, p_147948_3_ - var5);
            System.arraycopy(p_147948_0_, (p_147948_3_ + p_147948_2_ - var6) * var4, p_147948_0_, (p_147948_3_ - var5 - var6) * var4, var4 * var6);
        }

        for (var5 = 0; var5 < p_147948_3_; var5 += p_147948_2_)
        {
            var6 = Math.min(p_147948_2_, p_147948_3_ - var5);
            System.arraycopy(p_147948_0_, p_147948_3_ * var4, p_147948_0_, (p_147948_2_ + p_147948_3_ + var5) * var4, var4 * var6);
        }

        return p_147948_0_;
    }

    public static void func_147953_a(int[] p_147953_0_, int p_147953_1_, int p_147953_2_)
    {
        int[] var3 = new int[p_147953_1_];
        int var4 = p_147953_2_ / 2;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            System.arraycopy(p_147953_0_, var5 * p_147953_1_, var3, 0, p_147953_1_);
            System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - var5) * p_147953_1_, p_147953_0_, var5 * p_147953_1_, p_147953_1_);
            System.arraycopy(var3, 0, p_147953_0_, (p_147953_2_ - 1 - var5) * p_147953_1_, p_147953_1_);
        }
    }

    static
    {
        int var0 = -16777216;
        int var1 = -524040;
        int[] var2 = new int[] { -524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
        int[] var3 = new int[] { -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
        int var4 = var2.length;

        for (int var5 = 0; var5 < 16; ++var5)
        {
            System.arraycopy(var5 < var4 ? var2 : var3, 0, missingTextureData, 16 * var5, var4);
            System.arraycopy(var5 < var4 ? var3 : var2, 0, missingTextureData, 16 * var5 + var4, var4);
        }

        missingTexture.updateDynamicTexture();
        field_147957_g = new int[4];
    }
}
