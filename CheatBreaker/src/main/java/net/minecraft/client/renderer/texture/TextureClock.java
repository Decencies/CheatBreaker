package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;

public class TextureClock extends TextureAtlasSprite
{
    private double field_94239_h;
    private double field_94240_i;


    public TextureClock(String p_i1285_1_)
    {
        super(p_i1285_1_);
    }

    public void updateAnimation()
    {
        if (!this.framesTextureData.isEmpty())
        {
            Minecraft var1 = Minecraft.getMinecraft();
            double var2 = 0.0D;

            if (var1.theWorld != null && var1.thePlayer != null)
            {
                float var4 = var1.theWorld.getCelestialAngle(1.0F);
                var2 = (double)var4;

                if (!var1.theWorld.provider.isSurfaceWorld())
                {
                    var2 = Math.random();
                }
            }

            double var7;

            for (var7 = var2 - this.field_94239_h; var7 < -0.5D; ++var7)
            {
                ;
            }

            while (var7 >= 0.5D)
            {
                --var7;
            }

            if (var7 < -1.0D)
            {
                var7 = -1.0D;
            }

            if (var7 > 1.0D)
            {
                var7 = 1.0D;
            }

            this.field_94240_i += var7 * 0.1D;
            this.field_94240_i *= 0.8D;
            this.field_94239_h += this.field_94240_i;
            int var6;

            for (var6 = (int)((this.field_94239_h + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); var6 < 0; var6 = (var6 + this.framesTextureData.size()) % this.framesTextureData.size())
            {
                ;
            }

            if (var6 != this.frameCounter)
            {
                this.frameCounter = var6;
                TextureUtil.func_147955_a((int[][])this.framesTextureData.get(this.frameCounter), this.width, this.height, this.originX, this.originY, false, false);
            }
        }
    }
}
