package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Mipmaps;
import net.minecraft.util.IIcon;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TextureAtlasSprite implements IIcon
{
    private final String iconName;
    protected List framesTextureData = Lists.newArrayList();
    private AnimationMetadataSection animationMetadata;
    protected boolean rotated;
    private boolean field_147966_k;
    protected int originX;
    protected int originY;
    protected int width;
    protected int height;
    private float minU;
    private float maxU;
    private float minV;
    private float maxV;
    protected int frameCounter;
    protected int tickCounter;

    private int indexInMap = -1;
    public float baseU;
    public float baseV;
    public int sheetWidth;
    public int sheetHeight;
    private boolean mipmapActive = false;
    public int glOwnTextureId = -1;
    private int uploadedFrameIndex = -1;
    private int uploadedOwnFrameIndex = -1;
    public IntBuffer[] frameBuffers;
    public Mipmaps[] frameMipmaps;


    protected TextureAtlasSprite(String par1Str)
    {
        this.iconName = par1Str;
    }

    public void initSprite(int par1, int par2, int par3, int par4, boolean par5)
    {
        this.originX = par3;
        this.originY = par4;
        this.rotated = par5;
        float var6 = (float)(0.009999999776482582D / (double)par1);
        float var7 = (float)(0.009999999776482582D / (double)par2);
        this.minU = (float)par3 / (float)((double)par1) + var6;
        this.maxU = (float)(par3 + this.width) / (float)((double)par1) - var6;
        this.minV = (float)par4 / (float)par2 + var7;
        this.maxV = (float)(par4 + this.height) / (float)par2 - var7;

        if (this.field_147966_k)
        {
            float var8 = 8.0F / (float)par1;
            float var9 = 8.0F / (float)par2;
            this.minU += var8;
            this.maxU -= var8;
            this.minV += var9;
            this.maxV -= var9;
        }

        this.baseU = Math.min(this.minU, this.maxU);
        this.baseV = Math.min(this.minV, this.maxV);
    }

    public void copyFrom(TextureAtlasSprite par1TextureAtlasSprite)
    {
        this.originX = par1TextureAtlasSprite.originX;
        this.originY = par1TextureAtlasSprite.originY;
        this.width = par1TextureAtlasSprite.width;
        this.height = par1TextureAtlasSprite.height;
        this.rotated = par1TextureAtlasSprite.rotated;
        this.minU = par1TextureAtlasSprite.minU;
        this.maxU = par1TextureAtlasSprite.maxU;
        this.minV = par1TextureAtlasSprite.minV;
        this.maxV = par1TextureAtlasSprite.maxV;
        this.baseU = Math.min(this.minU, this.maxU);
        this.baseV = Math.min(this.minV, this.maxV);
    }

    /**
     * Returns the X position of this icon on its texture sheet, in pixels.
     */
    public int getOriginX()
    {
        return this.originX;
    }

    /**
     * Returns the Y position of this icon on its texture sheet, in pixels.
     */
    public int getOriginY()
    {
        return this.originY;
    }

    /**
     * Returns the width of the icon, in pixels.
     */
    public int getIconWidth()
    {
        return this.width;
    }

    /**
     * Returns the height of the icon, in pixels.
     */
    public int getIconHeight()
    {
        return this.height;
    }

    /**
     * Returns the minimum U coordinate to use when rendering with this icon.
     */
    public float getMinU()
    {
        return this.minU;
    }

    /**
     * Returns the maximum U coordinate to use when rendering with this icon.
     */
    public float getMaxU()
    {
        return this.maxU;
    }

    /**
     * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other arguments return in-between values.
     */
    public float getInterpolatedU(double par1)
    {
        float var3 = this.maxU - this.minU;
        return this.minU + var3 * (float)par1 / 16.0F;
    }

    /**
     * Returns the minimum V coordinate to use when rendering with this icon.
     */
    public float getMinV()
    {
        return this.minV;
    }

    /**
     * Returns the maximum V coordinate to use when rendering with this icon.
     */
    public float getMaxV()
    {
        return this.maxV;
    }

    /**
     * Gets a V coordinate on the icon. 0 returns vMin and 16 returns vMax. Other arguments return in-between values.
     */
    public float getInterpolatedV(double par1)
    {
        float var3 = this.maxV - this.minV;
        return this.minV + var3 * ((float)par1 / 16.0F);
    }

    public String getIconName()
    {
        return this.iconName;
    }

    public void updateAnimation()
    {
        ++this.tickCounter;

        if (this.tickCounter >= this.animationMetadata.getFrameTimeSingle(this.frameCounter))
        {
            int var1 = this.animationMetadata.getFrameIndex(this.frameCounter);
            int var2 = this.animationMetadata.getFrameCount() == 0 ? this.framesTextureData.size() : this.animationMetadata.getFrameCount();
            this.frameCounter = (this.frameCounter + 1) % var2;
            this.tickCounter = 0;
            int var3 = this.animationMetadata.getFrameIndex(this.frameCounter);

            if (var1 != var3 && var3 >= 0 && var3 < this.framesTextureData.size())
            {
                TextureUtil.func_147955_a((int[][])((int[][])this.framesTextureData.get(var3)), this.width, this.height, this.originX, this.originY, false, false);
                this.uploadedFrameIndex = var3;
            }
        }
    }

    public int[][] func_147965_a(int p_147965_1_)
    {
        return (int[][])((int[][])this.framesTextureData.get(p_147965_1_));
    }

    public int getFrameCount()
    {
        return this.framesTextureData.size();
    }

    public void setIconWidth(int par1)
    {
        this.width = par1;
    }

    public void setIconHeight(int par1)
    {
        this.height = par1;
    }

    public void func_147964_a(BufferedImage[] p_147964_1_, AnimationMetadataSection p_147964_2_, boolean p_147964_3_)
    {
        this.resetSprite();
        this.field_147966_k = p_147964_3_;
        int var4 = p_147964_1_[0].getWidth();
        int var5 = p_147964_1_[0].getHeight();
        this.width = var4;
        this.height = var5;

        if (p_147964_3_)
        {
            this.width += 16;
            this.height += 16;
        }

        int[][] var6 = new int[p_147964_1_.length][];
        int var7;

        for (var7 = 0; var7 < p_147964_1_.length; ++var7)
        {
            BufferedImage var12 = p_147964_1_[var7];

            if (var12 != null)
            {
                if (var7 > 0 && (var12.getWidth() != var4 >> var7 || var12.getHeight() != var5 >> var7))
                {
                    throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", new Object[] {Integer.valueOf(var7), Integer.valueOf(var12.getWidth()), Integer.valueOf(var12.getHeight()), Integer.valueOf(var4 >> var7), Integer.valueOf(var5 >> var7)}));
                }

                var6[var7] = new int[var12.getWidth() * var12.getHeight()];
                var12.getRGB(0, 0, var12.getWidth(), var12.getHeight(), var6[var7], 0, var12.getWidth());
            }
        }

        if (p_147964_2_ == null)
        {
            if (var5 != var4)
            {
                throw new RuntimeException("broken aspect ratio and not an animation");
            }

            this.func_147961_a(var6);
            this.framesTextureData.add(this.func_147960_a(var6, var4, var5));
        }
        else
        {
            var7 = var5 / var4;
            int var121 = var4;
            int var9 = var4;
            this.height = this.width;
            int var11;

            if (p_147964_2_.getFrameCount() > 0)
            {
                Iterator var13 = p_147964_2_.getFrameIndexSet().iterator();

                while (var13.hasNext())
                {
                    var11 = ((Integer)var13.next()).intValue();

                    if (var11 >= var7)
                    {
                        throw new RuntimeException("invalid frameindex " + var11);
                    }

                    this.allocateFrameTextureData(var11);
                    this.framesTextureData.set(var11, this.func_147960_a(func_147962_a(var6, var121, var9, var11), var121, var9));
                }

                this.animationMetadata = p_147964_2_;
            }
            else
            {
                ArrayList var131 = Lists.newArrayList();

                for (var11 = 0; var11 < var7; ++var11)
                {
                    this.framesTextureData.add(this.func_147960_a(func_147962_a(var6, var121, var9, var11), var121, var9));
                    var131.add(new AnimationFrame(var11, -1));
                }

                this.animationMetadata = new AnimationMetadataSection(var131, this.width, this.height, p_147964_2_.getFrameTime());
            }
        }
    }

    public void func_147963_d(int p_147963_1_)
    {
        ArrayList var2 = Lists.newArrayList();

        for (int var3 = 0; var3 < this.framesTextureData.size(); ++var3)
        {
            final int[][] var4 = (int[][])((int[][])this.framesTextureData.get(var3));

            if (var4 != null)
            {
                try
                {
                    var2.add(TextureUtil.func_147949_a(p_147963_1_, this.width, var4));
                }
                catch (Throwable var8)
                {
                    CrashReport var6 = CrashReport.makeCrashReport(var8, "Generating mipmaps for frame");
                    CrashReportCategory var7 = var6.makeCategory("Frame being iterated");
                    var7.addCrashSection("Frame index", Integer.valueOf(var3));
                    var7.addCrashSectionCallable("Frame sizes", new Callable()
                    {


                        public String call()
                        {
                            StringBuilder var1 = new StringBuilder();
                            int[][] var2 = var4;
                            int var3 = var2.length;

                            for (int var4x = 0; var4x < var3; ++var4x)
                            {
                                int[] var5 = var2[var4x];

                                if (var1.length() > 0)
                                {
                                    var1.append(", ");
                                }

                                var1.append(var5 == null ? "null" : Integer.valueOf(var5.length));
                            }

                            return var1.toString();
                        }
                    });
                    throw new ReportedException(var6);
                }
            }
        }

        this.setFramesTextureData(var2);
    }

    private void func_147961_a(int[][] p_147961_1_)
    {
        int[] var2 = p_147961_1_[0];
        int var3 = 0;
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;
        int var7;

        for (var7 = 0; var7 < var2.length; ++var7)
        {
            if ((var2[var7] & -16777216) != 0)
            {
                var4 += var2[var7] >> 16 & 255;
                var5 += var2[var7] >> 8 & 255;
                var6 += var2[var7] >> 0 & 255;
                ++var3;
            }
        }

        if (var3 != 0)
        {
            var4 /= var3;
            var5 /= var3;
            var6 /= var3;

            for (var7 = 0; var7 < var2.length; ++var7)
            {
                if ((var2[var7] & -16777216) == 0)
                {
                    var2[var7] = var4 << 16 | var5 << 8 | var6;
                }
            }
        }
    }

    private int[][] func_147960_a(int[][] p_147960_1_, int p_147960_2_, int p_147960_3_)
    {
        if (!this.field_147966_k)
        {
            return p_147960_1_;
        }
        else
        {
            int[][] var4 = new int[p_147960_1_.length][];

            for (int var5 = 0; var5 < p_147960_1_.length; ++var5)
            {
                int[] var6 = p_147960_1_[var5];

                if (var6 != null)
                {
                    int[] var7 = new int[(p_147960_2_ + 16 >> var5) * (p_147960_3_ + 16 >> var5)];
                    System.arraycopy(var6, 0, var7, 0, var6.length);
                    var4[var5] = TextureUtil.func_147948_a(var7, p_147960_2_ >> var5, p_147960_3_ >> var5, 8 >> var5);
                }
            }

            return var4;
        }
    }

    private void allocateFrameTextureData(int par1)
    {
        if (this.framesTextureData.size() <= par1)
        {
            for (int var2 = this.framesTextureData.size(); var2 <= par1; ++var2)
            {
                this.framesTextureData.add((Object)null);
            }
        }
    }

    private static int[][] func_147962_a(int[][] p_147962_0_, int p_147962_1_, int p_147962_2_, int p_147962_3_)
    {
        int[][] var4 = new int[p_147962_0_.length][];

        for (int var5 = 0; var5 < p_147962_0_.length; ++var5)
        {
            int[] var6 = p_147962_0_[var5];

            if (var6 != null)
            {
                var4[var5] = new int[(p_147962_1_ >> var5) * (p_147962_2_ >> var5)];
                System.arraycopy(var6, p_147962_3_ * var4[var5].length, var4[var5], 0, var4[var5].length);
            }
        }

        return var4;
    }

    public void clearFramesTextureData()
    {
        this.framesTextureData.clear();
    }

    public boolean hasAnimationMetadata()
    {
        return this.animationMetadata != null;
    }

    public void setFramesTextureData(List par1List)
    {
        this.framesTextureData = par1List;

        for (int i = 0; i < this.framesTextureData.size(); ++i)
        {
            int[][] datas = (int[][])((int[][])this.framesTextureData.get(i));

            if (datas != null && !this.iconName.startsWith("leaves_"))
            {
                for (int di = 0; di < datas.length; ++di)
                {
                    int[] data = datas[di];

                    if (data != null)
                    {
                        this.fixTransparentColor(data);
                    }
                }
            }
        }
    }

    private void resetSprite()
    {
        this.animationMetadata = null;
        this.setFramesTextureData(Lists.newArrayList());
        this.frameCounter = 0;
        this.tickCounter = 0;
        this.deleteOwnTexture();
        this.uploadedFrameIndex = -1;
        this.uploadedOwnFrameIndex = -1;
        this.frameBuffers = null;
        this.frameMipmaps = null;
    }

    public String toString()
    {
        return "TextureAtlasSprite{name=\'" + this.iconName + '\'' + ", frameCount=" + this.framesTextureData.size() + ", rotated=" + this.rotated + ", x=" + this.originX + ", y=" + this.originY + ", height=" + this.height + ", width=" + this.width + ", u0=" + this.minU + ", u1=" + this.maxU + ", v0=" + this.minV + ", v1=" + this.maxV + '}';
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getIndexInMap()
    {
        return this.indexInMap;
    }

    public void setIndexInMap(int indexInMap)
    {
        this.indexInMap = indexInMap;
    }

    public void setMipmapActive(boolean mipmapActive)
    {
        this.mipmapActive = mipmapActive;
        this.frameMipmaps = null;
    }

    public void uploadFrameTexture()
    {
        this.uploadFrameTexture(this.frameCounter, this.originX, this.originY);
    }

    public void uploadFrameTexture(int frameIndex, int xPos, int yPos) {}

    private void uploadFrameMipmaps(int frameIndex, int xPos, int yPos) {}

    public void bindOwnTexture() {}

    public void bindUploadOwnTexture()
    {
        this.bindOwnTexture();
        this.uploadFrameTexture(this.frameCounter, 0, 0);
    }

    public void uploadOwnAnimation()
    {
        if (this.uploadedFrameIndex != this.uploadedOwnFrameIndex)
        {
            TextureUtil.bindTexture(this.glOwnTextureId);
            this.uploadFrameTexture(this.uploadedFrameIndex, 0, 0);
            this.uploadedOwnFrameIndex = this.uploadedFrameIndex;
        }
    }

    public void deleteOwnTexture()
    {
        if (this.glOwnTextureId >= 0)
        {
            GL11.glDeleteTextures(this.glOwnTextureId);
            this.glOwnTextureId = -1;
        }
    }

    private void fixTransparentColor(int[] data)
    {
        long redSum = 0L;
        long greenSum = 0L;
        long blueSum = 0L;
        long count = 0L;
        int redAvg;
        int greenAvg;
        int blueAvg;
        int colAvg;
        int i;
        int col;

        for (redAvg = 0; redAvg < data.length; ++redAvg)
        {
            greenAvg = data[redAvg];
            blueAvg = greenAvg >> 24 & 255;

            if (blueAvg >= 16)
            {
                colAvg = greenAvg >> 16 & 255;
                i = greenAvg >> 8 & 255;
                col = greenAvg & 255;
                redSum += (long)colAvg;
                greenSum += (long)i;
                blueSum += (long)col;
                ++count;
            }
        }

        if (count > 0L)
        {
            redAvg = (int)(redSum / count);
            greenAvg = (int)(greenSum / count);
            blueAvg = (int)(blueSum / count);
            colAvg = redAvg << 16 | greenAvg << 8 | blueAvg;

            for (i = 0; i < data.length; ++i)
            {
                col = data[i];
                int alpha = col >> 24 & 255;

                if (alpha <= 16)
                {
                    data[i] = colAvg;
                }
            }
        }
    }

    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location)
    {
        return false;
    }

    public boolean load(IResourceManager manager, ResourceLocation location)
    {
        return true;
    }
}
