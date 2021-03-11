package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCreeper extends RenderLiving
{
    private static final ResourceLocation armoredCreeperTextures = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private static final ResourceLocation creeperTextures = new ResourceLocation("textures/entity/creeper/creeper.png");

    /** The creeper model. */
    private ModelBase creeperModel = new ModelCreeper(2.0F);


    public RenderCreeper()
    {
        super(new ModelCreeper(), 0.5F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityCreeper p_77041_1_, float p_77041_2_)
    {
        float var3 = p_77041_1_.getCreeperFlashIntensity(p_77041_2_);
        float var4 = 1.0F + MathHelper.sin(var3 * 100.0F) * var3 * 0.01F;

        if (var3 < 0.0F)
        {
            var3 = 0.0F;
        }

        if (var3 > 1.0F)
        {
            var3 = 1.0F;
        }

        var3 *= var3;
        var3 *= var3;
        float var5 = (1.0F + var3 * 0.4F) * var4;
        float var6 = (1.0F + var3 * 0.1F) / var4;
        GL11.glScalef(var5, var6, var5);
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityCreeper p_77030_1_, float p_77030_2_, float p_77030_3_)
    {
        float var4 = p_77030_1_.getCreeperFlashIntensity(p_77030_3_);

        if ((int)(var4 * 10.0F) % 2 == 0)
        {
            return 0;
        }
        else
        {
            int var5 = (int)(var4 * 0.2F * 255.0F);

            if (var5 < 0)
            {
                var5 = 0;
            }

            if (var5 > 255)
            {
                var5 = 255;
            }

            short var6 = 255;
            short var7 = 255;
            short var8 = 255;
            return var5 << 24 | var6 << 16 | var7 << 8 | var8;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityCreeper p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        if (p_77032_1_.getPowered())
        {
            if (p_77032_1_.isInvisible())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

            if (p_77032_2_ == 1)
            {
                float var4 = (float)p_77032_1_.ticksExisted + p_77032_3_;
                this.bindTexture(armoredCreeperTextures);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                float var5 = var4 * 0.01F;
                float var6 = var4 * 0.01F;
                GL11.glTranslatef(var5, var6, 0.0F);
                this.setRenderPassModel(this.creeperModel);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_BLEND);
                float var7 = 0.5F;
                GL11.glColor4f(var7, var7, var7, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                return 1;
            }

            if (p_77032_2_ == 2)
            {
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }

        return -1;
    }

    protected int inheritRenderPass(EntityCreeper p_77035_1_, int p_77035_2_, float p_77035_3_)
    {
        return -1;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityCreeper p_110775_1_)
    {
        return creeperTextures;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    public void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
    {
        this.preRenderCallback((EntityCreeper)p_77041_1_, p_77041_2_);
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLivingBase p_77030_1_, float p_77030_2_, float p_77030_3_)
    {
        return this.getColorMultiplier((EntityCreeper)p_77030_1_, p_77030_2_, p_77030_3_);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    public int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        return this.shouldRenderPass((EntityCreeper)p_77032_1_, p_77032_2_, p_77032_3_);
    }

    protected int inheritRenderPass(EntityLivingBase p_77035_1_, int p_77035_2_, float p_77035_3_)
    {
        return this.inheritRenderPass((EntityCreeper)p_77035_1_, p_77035_2_, p_77035_3_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    public ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityCreeper)p_110775_1_);
    }
}
