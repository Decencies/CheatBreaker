package net.minecraft.client.renderer.entity;

import java.util.Random;
import java.util.concurrent.Callable;
import com.cheatbreaker.client.CheatBreaker;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderItem extends Render
{
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private RenderBlocks field_147913_i = new RenderBlocks();

    /** The RNG used in RenderItem (for bobbing itemstacks on the ground) */
    private Random random = new Random();
    public boolean renderWithColor = true;

    /** Defines the zLevel of rendering of item on GUI. */
    public float zLevel;
    public static boolean renderInFrame;


    public RenderItem()
    {
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityItem p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        ItemStack var10 = p_76986_1_.getEntityItem();

        if (var10.getItem() != null)
        {
            this.bindEntityTexture(p_76986_1_);
            TextureUtil.func_152777_a(false, false, 1.0F);
            this.random.setSeed(187L);
            GL11.glPushMatrix();
            float var11 = MathHelper.sin(((float)p_76986_1_.age + p_76986_9_) / 10.0F + p_76986_1_.hoverStart) * 0.1F + 0.1F;
            float var12 = (((float)p_76986_1_.age + p_76986_9_) / 20.0F + p_76986_1_.hoverStart) * (180F / (float)Math.PI);
            byte var13 = 1;

            if (p_76986_1_.getEntityItem().stackSize > 1)
            {
                var13 = 2;
            }

            if (p_76986_1_.getEntityItem().stackSize > 5)
            {
                var13 = 3;
            }

            if (p_76986_1_.getEntityItem().stackSize > 20)
            {
                var13 = 4;
            }

            if (p_76986_1_.getEntityItem().stackSize > 40)
            {
                var13 = 5;
            }

            GL11.glTranslatef((float)p_76986_2_, (float)p_76986_4_ + var11, (float)p_76986_6_);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float var18;
            float var19;
            int var25;

            if (var10.getItemSpriteNumber() == 0 && var10.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(var10.getItem()).getRenderType()))
            {
                Block var22 = Block.getBlockFromItem(var10.getItem());
                GL11.glRotatef(var12, 0.0F, 1.0F, 0.0F);

                if (renderInFrame)
                {
                    GL11.glScalef(1.25F, 1.25F, 1.25F);
                    GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                float var24 = 0.25F;
                var25 = var22.getRenderType();

                if (var25 == 1 || var25 == 19 || var25 == 12 || var25 == 2)
                {
                    var24 = 0.5F;
                }

                if (var22.getRenderBlockPass() > 0)
                {
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    GL11.glEnable(GL11.GL_BLEND);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                }

                GL11.glScalef(var24, var24, var24);

                for (int var26 = 0; var26 < var13; ++var26)
                {
                    GL11.glPushMatrix();

                    if (var26 > 0)
                    {
                        var18 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                        var19 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                        float var20 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                        GL11.glTranslatef(var18, var19, var20);
                    }

                    this.field_147913_i.renderBlockAsItem(var22, var10.getItemDamage(), 1.0F);
                    GL11.glPopMatrix();
                }

                if (var22.getRenderBlockPass() > 0)
                {
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
            else
            {
                float var17;

                if (var10.getItemSpriteNumber() == 1 && var10.getItem().requiresMultipleRenderPasses())
                {
                    if (renderInFrame)
                    {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    }
                    else
                    {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    for (int var21 = 0; var21 <= 1; ++var21)
                    {
                        this.random.setSeed(187L);
                        IIcon var23 = var10.getItem().getIconFromDamageForRenderPass(var10.getItemDamage(), var21);

                        if (this.renderWithColor)
                        {
                            var25 = var10.getItem().getColorFromItemStack(var10, var21);
                            var17 = (float)(var25 >> 16 & 255) / 255.0F;
                            var18 = (float)(var25 >> 8 & 255) / 255.0F;
                            var19 = (float)(var25 & 255) / 255.0F;
                            GL11.glColor4f(var17, var18, var19, 1.0F);
                            this.renderDroppedItem(p_76986_1_, var23, var13, p_76986_9_, var17, var18, var19);
                        }
                        else
                        {
                            this.renderDroppedItem(p_76986_1_, var23, var13, p_76986_9_, 1.0F, 1.0F, 1.0F);
                        }
                    }
                }
                else
                {
                    if (var10 != null && var10.getItem() instanceof ItemCloth)
                    {
                        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                        GL11.glEnable(GL11.GL_BLEND);
                        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    }

                    if (renderInFrame)
                    {
                        GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                        GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                    }
                    else
                    {
                        GL11.glScalef(0.5F, 0.5F, 0.5F);
                    }

                    IIcon var14 = var10.getIconIndex();

                    if (this.renderWithColor)
                    {
                        int var15 = var10.getItem().getColorFromItemStack(var10, 0);
                        float var16 = (float)(var15 >> 16 & 255) / 255.0F;
                        var17 = (float)(var15 >> 8 & 255) / 255.0F;
                        var18 = (float)(var15 & 255) / 255.0F;
                        this.renderDroppedItem(p_76986_1_, var14, var13, p_76986_9_, var16, var17, var18);
                    }
                    else
                    {
                        this.renderDroppedItem(p_76986_1_, var14, var13, p_76986_9_, 1.0F, 1.0F, 1.0F);
                    }

                    if (var10 != null && var10.getItem() instanceof ItemCloth)
                    {
                        GL11.glDisable(GL11.GL_BLEND);
                    }
                }
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            this.bindEntityTexture(p_76986_1_);
            TextureUtil.func_147945_b();
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityItem p_110775_1_)
    {
        return this.renderManager.renderEngine.getResourceLocation(p_110775_1_.getEntityItem().getItemSpriteNumber());
    }

    /**
     * Renders a dropped item
     */
    private void renderDroppedItem(EntityItem p_77020_1_, IIcon p_77020_2_, int p_77020_3_, float p_77020_4_, float p_77020_5_, float p_77020_6_, float p_77020_7_)
    {
        Tessellator var8 = Tessellator.instance;

        if (p_77020_2_ == null)
        {
            TextureManager var9 = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation var10 = var9.getResourceLocation(p_77020_1_.getEntityItem().getItemSpriteNumber());
            p_77020_2_ = ((TextureMap)var9.getTexture(var10)).getAtlasSprite("missingno");
        }

        float var25 = ((IIcon)p_77020_2_).getMinU();
        float var26 = ((IIcon)p_77020_2_).getMaxU();
        float var11 = ((IIcon)p_77020_2_).getMinV();
        float var12 = ((IIcon)p_77020_2_).getMaxV();
        float var13 = 1.0F;
        float var14 = 0.5F;
        float var15 = 0.25F;
        float var17;

        if (this.renderManager.options.fancyGraphics)
        {
            GL11.glPushMatrix();

            if (renderInFrame)
            {
                GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GL11.glRotatef((((float)p_77020_1_.age + p_77020_4_) / 20.0F + p_77020_1_.hoverStart) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            float var16 = 0.0625F;
            var17 = 0.021875F;
            ItemStack var18 = p_77020_1_.getEntityItem();
            int var19 = var18.stackSize;
            byte var24;

            if (var19 < 2)
            {
                var24 = 1;
            }
            else if (var19 < 16)
            {
                var24 = 2;
            }
            else if (var19 < 32)
            {
                var24 = 3;
            }
            else
            {
                var24 = 4;
            }

            GL11.glTranslatef(-var14, -var15, -((var16 + var17) * (float)var24 / 2.0F));

            for (int var20 = 0; var20 < var24; ++var20)
            {
                GL11.glTranslatef(0.0F, 0.0F, var16 + var17);

                if (var18.getItemSpriteNumber() == 0)
                {
                    this.bindTexture(TextureMap.locationBlocksTexture);
                }
                else
                {
                    this.bindTexture(TextureMap.locationItemsTexture);
                }

                GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
                ItemRenderer.renderItemIn2D(var8, var26, var11, var25, var12, ((IIcon)p_77020_2_).getIconWidth(), ((IIcon)p_77020_2_).getIconHeight(), var16);

                if (var18.hasEffect())
                {
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    this.renderManager.renderEngine.bindTexture(RES_ITEM_GLINT);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                    float var21 = 0.76F;
                    GL11.glColor4f(0.5F * var21, 0.25F * var21, 0.8F * var21, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glPushMatrix();
                    float var22 = 0.125F;
                    GL11.glScalef(var22, var22, var22);
                    float var23 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
                    GL11.glTranslatef(var23, 0.0F, 0.0F);
                    GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var16);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(var22, var22, var22);
                    var23 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
                    GL11.glTranslatef(-var23, 0.0F, 0.0F);
                    GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(var8, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, var16);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }
            }

            GL11.glPopMatrix();
        }
        else
        {
            for (int var27 = 0; var27 < p_77020_3_; ++var27)
            {
                GL11.glPushMatrix();

                if (var27 > 0)
                {
                    var17 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var28 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float var29 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(var17, var28, var29);
                }

                if (!renderInFrame)
                {
                    GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(p_77020_5_, p_77020_6_, p_77020_7_, 1.0F);
                var8.startDrawingQuads();
                var8.setNormal(0.0F, 1.0F, 0.0F);
                var8.addVertexWithUV((double)(0.0F - var14), (double)(0.0F - var15), 0.0D, (double)var25, (double)var12);
                var8.addVertexWithUV((double)(var13 - var14), (double)(0.0F - var15), 0.0D, (double)var26, (double)var12);
                var8.addVertexWithUV((double)(var13 - var14), (double)(1.0F - var15), 0.0D, (double)var26, (double)var11);
                var8.addVertexWithUV((double)(0.0F - var14), (double)(1.0F - var15), 0.0D, (double)var25, (double)var11);
                var8.draw();
                GL11.glPopMatrix();
            }
        }
    }

    /**
     * Renders the item's icon or block into the UI at the specified position.
     */
    public void renderItemIntoGUI(FontRenderer p_77015_1_, TextureManager p_77015_2_, ItemStack p_77015_3_, int p_77015_4_, int p_77015_5_)
    {
        int var6 = p_77015_3_.getItemDamage();
        Object var7 = p_77015_3_.getIconIndex();
        int var9;
        float var12;
        float var17;
        float var18;

        if (p_77015_3_.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(p_77015_3_.getItem()).getRenderType()))
        {
            p_77015_2_.bindTexture(TextureMap.locationBlocksTexture);
            Block var16 = Block.getBlockFromItem(p_77015_3_.getItem());
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (var16.getRenderBlockPass() != 0)
            {
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            }
            else
            {
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
                GL11.glDisable(GL11.GL_BLEND);
            }

            GL11.glPushMatrix();
            GL11.glTranslatef((float)(p_77015_4_ - 2), (float)(p_77015_5_ + 3), -3.0F + this.zLevel);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            var9 = p_77015_3_.getItem().getColorFromItemStack(p_77015_3_, 0);
            var17 = (float)(var9 >> 16 & 255) / 255.0F;
            var18 = (float)(var9 >> 8 & 255) / 255.0F;
            var12 = (float)(var9 & 255) / 255.0F;

            if (this.renderWithColor)
            {
                GL11.glColor4f(var17, var18, var12, 1.0F);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.field_147913_i.useInventoryTint = this.renderWithColor;
            this.field_147913_i.renderBlockAsItem(var16, var6, 1.0F);
            this.field_147913_i.useInventoryTint = true;

            if (var16.getRenderBlockPass() == 0)
            {
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            }

            GL11.glPopMatrix();
        }
        else if (p_77015_3_.getItem().requiresMultipleRenderPasses())
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            p_77015_2_.bindTexture(TextureMap.locationItemsTexture);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(0, 0, 0, 0);
            GL11.glColorMask(false, false, false, true);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            Tessellator var8 = Tessellator.instance;
            var8.startDrawingQuads();
            var8.setColorOpaque_I(-1);
            var8.addVertex((double)(p_77015_4_ - 2), (double)(p_77015_5_ + 18), (double)this.zLevel);
            var8.addVertex((double)(p_77015_4_ + 18), (double)(p_77015_5_ + 18), (double)this.zLevel);
            var8.addVertex((double)(p_77015_4_ + 18), (double)(p_77015_5_ - 2), (double)this.zLevel);
            var8.addVertex((double)(p_77015_4_ - 2), (double)(p_77015_5_ - 2), (double)this.zLevel);
            var8.draw();
            GL11.glColorMask(true, true, true, true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);

            for (var9 = 0; var9 <= 1; ++var9)
            {
                IIcon var10 = p_77015_3_.getItem().getIconFromDamageForRenderPass(var6, var9);
                int var11 = p_77015_3_.getItem().getColorFromItemStack(p_77015_3_, var9);
                var12 = (float)(var11 >> 16 & 255) / 255.0F;
                float var13 = (float)(var11 >> 8 & 255) / 255.0F;
                float var14 = (float)(var11 & 255) / 255.0F;

                if (this.renderWithColor)
                {
                    GL11.glColor4f(var12, var13, var14, 1.0F);
                }

                this.renderIcon(p_77015_4_, p_77015_5_, var10, 16, 16);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
        }
        else
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            ResourceLocation var15 = p_77015_2_.getResourceLocation(p_77015_3_.getItemSpriteNumber());
            p_77015_2_.bindTexture(var15);

            if (var7 == null)
            {
                var7 = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(var15)).getAtlasSprite("missingno");
            }

            var9 = p_77015_3_.getItem().getColorFromItemStack(p_77015_3_, 0);
            var17 = (float)(var9 >> 16 & 255) / 255.0F;
            var18 = (float)(var9 >> 8 & 255) / 255.0F;
            var12 = (float)(var9 & 255) / 255.0F;

            if (this.renderWithColor)
            {
                GL11.glColor4f(var17, var18, var12, 1.0F);
            }

            this.renderIcon(p_77015_4_, p_77015_5_, (IIcon)var7, 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    /**
     * Render the item's icon or block into the GUI, including the glint effect.
     */
    public void renderItemAndEffectIntoGUI(FontRenderer p_82406_1_, TextureManager p_82406_2_, final ItemStack p_82406_3_, int p_82406_4_, int p_82406_5_)
    {
        if (p_82406_3_ != null)
        {
            this.zLevel += 50.0F;

            try
            {
                this.renderItemIntoGUI(p_82406_1_, p_82406_2_, p_82406_3_, p_82406_4_, p_82406_5_);
            }
            catch (Throwable var9)
            {
                CrashReport var7 = CrashReport.makeCrashReport(var9, "Rendering item");
                CrashReportCategory var8 = var7.makeCategory("Item being rendered");
                var8.addCrashSectionCallable("Item Type", new Callable()
                {

                    public String call()
                    {
                        return String.valueOf(p_82406_3_.getItem());
                    }
                });
                var8.addCrashSectionCallable("Item Aux", new Callable()
                {

                    public String call()
                    {
                        return String.valueOf(p_82406_3_.getItemDamage());
                    }
                });
                var8.addCrashSectionCallable("Item NBT", new Callable()
                {

                    public String call()
                    {
                        return String.valueOf(p_82406_3_.getTagCompound());
                    }
                });
                var8.addCrashSectionCallable("Item Foil", new Callable()
                {

                    public String call()
                    {
                        return String.valueOf(p_82406_3_.hasEffect());
                    }
                });
                throw new ReportedException(var7);
            }

            if (p_82406_3_.hasEffect())
            {
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                p_82406_2_.bindTexture(RES_ITEM_GLINT);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
                this.renderGlint(p_82406_4_ * 431278612 + p_82406_5_ * 32178161, p_82406_4_ - 2, p_82406_5_ - 2, 20, 20);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }

            this.zLevel -= 50.0F;
        }
    }

    private void renderGlint(int p_77018_1_, int p_77018_2_, int p_77018_3_, int p_77018_4_, int p_77018_5_)
    {
        for (int var6 = 0; var6 < 2; ++var6)
        {
            OpenGlHelper.glBlendFunc((Boolean) CheatBreaker.getInstance().globalSettings.shinyPots.getValue() ? 773 : 772, 1, 0, 0);
            float var7 = 0.00390625F;
            float var8 = 0.00390625F;
            float var9 = (float)(Minecraft.getSystemTime() % (long)(3000 + var6 * 1873)) / (3000.0F + (float)(var6 * 1873)) * 256.0F;
            float var10 = 0.0F;
            Tessellator var11 = Tessellator.instance;
            float var12 = 4.0F;

            if (var6 == 1)
            {
                var12 = -1.0F;
            }

            var11.startDrawingQuads();
            var11.addVertexWithUV((double)(p_77018_2_ + 0), (double)(p_77018_3_ + p_77018_5_), (double)this.zLevel, (double)((var9 + (float)p_77018_5_ * var12) * var7), (double)((var10 + (float)p_77018_5_) * var8));
            var11.addVertexWithUV((double)(p_77018_2_ + p_77018_4_), (double)(p_77018_3_ + p_77018_5_), (double)this.zLevel, (double)((var9 + (float)p_77018_4_ + (float)p_77018_5_ * var12) * var7), (double)((var10 + (float)p_77018_5_) * var8));
            var11.addVertexWithUV((double)(p_77018_2_ + p_77018_4_), (double)(p_77018_3_ + 0), (double)this.zLevel, (double)((var9 + (float)p_77018_4_) * var7), (double)((var10 + 0.0F) * var8));
            var11.addVertexWithUV((double)(p_77018_2_ + 0), (double)(p_77018_3_ + 0), (double)this.zLevel, (double)((var9 + 0.0F) * var7), (double)((var10 + 0.0F) * var8));
            var11.draw();
        }
    }

    /**
     * Renders the item's overlay information. Examples being stack count or damage on top of the item's image at the
     * specified position.
     */
    public void renderItemOverlayIntoGUI(FontRenderer p_77021_1_, TextureManager p_77021_2_, ItemStack p_77021_3_, int p_77021_4_, int p_77021_5_)
    {
        this.renderItemOverlayIntoGUI(p_77021_1_, p_77021_2_, p_77021_3_, p_77021_4_, p_77021_5_, (String)null);
    }

    public void renderItemOverlayIntoGUI(FontRenderer p_94148_1_, TextureManager p_94148_2_, ItemStack p_94148_3_, int p_94148_4_, int p_94148_5_, String p_94148_6_)
    {
        if (p_94148_3_ != null)
        {
            if (p_94148_3_.stackSize > 1 || p_94148_6_ != null)
            {
                String var7 = p_94148_6_ == null ? String.valueOf(p_94148_3_.stackSize) : p_94148_6_;
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_BLEND);
                p_94148_1_.drawStringWithShadow(var7, p_94148_4_ + 19 - 2 - p_94148_1_.getStringWidth(var7), p_94148_5_ + 6 + 3, 16777215);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }

            if (p_94148_3_.isItemDamaged())
            {
                int var12 = (int)Math.round(13.0D - (double)p_94148_3_.getItemDamageForDisplay() * 13.0D / (double)p_94148_3_.getMaxDamage());
                int var8 = (int)Math.round(255.0D - (double)p_94148_3_.getItemDamageForDisplay() * 255.0D / (double)p_94148_3_.getMaxDamage());
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_BLEND);
                Tessellator var9 = Tessellator.instance;
                int var10 = 255 - var8 << 16 | var8 << 8;
                int var11 = (255 - var8) / 4 << 16 | 16128;
                this.renderQuad(var9, p_94148_4_ + 2, p_94148_5_ + 13, 13, 2, 0);
                this.renderQuad(var9, p_94148_4_ + 2, p_94148_5_ + 13, 12, 1, var11);
                this.renderQuad(var9, p_94148_4_ + 2, p_94148_5_ + 13, var12, 1, var10);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Adds a quad to the tesselator at the specified position with the set width and height and color.  Args:
     * tessellator, x, y, width, height, color
     */
    private void renderQuad(Tessellator p_77017_1_, int p_77017_2_, int p_77017_3_, int p_77017_4_, int p_77017_5_, int p_77017_6_)
    {
        p_77017_1_.startDrawingQuads();
        p_77017_1_.setColorOpaque_I(p_77017_6_);
        p_77017_1_.addVertex((double)(p_77017_2_ + 0), (double)(p_77017_3_ + 0), 0.0D);
        p_77017_1_.addVertex((double)(p_77017_2_ + 0), (double)(p_77017_3_ + p_77017_5_), 0.0D);
        p_77017_1_.addVertex((double)(p_77017_2_ + p_77017_4_), (double)(p_77017_3_ + p_77017_5_), 0.0D);
        p_77017_1_.addVertex((double)(p_77017_2_ + p_77017_4_), (double)(p_77017_3_ + 0), 0.0D);
        p_77017_1_.draw();
    }

    public void renderIcon(int p_94149_1_, int p_94149_2_, IIcon p_94149_3_, int p_94149_4_, int p_94149_5_)
    {
        Tessellator var6 = Tessellator.instance;
        var6.startDrawingQuads();
        var6.addVertexWithUV((double)(p_94149_1_ + 0), (double)(p_94149_2_ + p_94149_5_), (double)this.zLevel, (double)p_94149_3_.getMinU(), (double)p_94149_3_.getMaxV());
        var6.addVertexWithUV((double)(p_94149_1_ + p_94149_4_), (double)(p_94149_2_ + p_94149_5_), (double)this.zLevel, (double)p_94149_3_.getMaxU(), (double)p_94149_3_.getMaxV());
        var6.addVertexWithUV((double)(p_94149_1_ + p_94149_4_), (double)(p_94149_2_ + 0), (double)this.zLevel, (double)p_94149_3_.getMaxU(), (double)p_94149_3_.getMinV());
        var6.addVertexWithUV((double)(p_94149_1_ + 0), (double)(p_94149_2_ + 0), (double)this.zLevel, (double)p_94149_3_.getMinU(), (double)p_94149_3_.getMinV());
        var6.draw();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    public ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityItem)p_110775_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
    {
        this.doRender((EntityItem)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
