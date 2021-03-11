package net.minecraft.client.gui.achievement;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiAchievements extends GuiScreen implements IProgressMeter
{
    private static final int field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
    private static final int field_146571_z = AchievementList.minDisplayRow * 24 - 112;
    private static final int field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
    private static final int field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
    private static final ResourceLocation field_146561_C = new ResourceLocation("textures/gui/achievement/achievement_background.png");
    protected GuiScreen field_146562_a;
    protected int field_146555_f = 256;
    protected int field_146557_g = 202;
    protected int field_146563_h;
    protected int field_146564_i;
    protected float field_146570_r = 1.0F;
    protected double field_146569_s;
    protected double field_146568_t;
    protected double field_146567_u;
    protected double field_146566_v;
    protected double field_146565_w;
    protected double field_146573_x;
    private int field_146554_D;
    private StatFileWriter field_146556_E;
    private boolean field_146558_F = true;


    public GuiAchievements(GuiScreen p_i45026_1_, StatFileWriter p_i45026_2_)
    {
        this.field_146562_a = p_i45026_1_;
        this.field_146556_E = p_i45026_2_;
        short var3 = 141;
        short var4 = 141;
        this.field_146569_s = this.field_146567_u = this.field_146565_w = (double)(AchievementList.openInventory.displayColumn * 24 - var3 / 2 - 12);
        this.field_146568_t = this.field_146566_v = this.field_146573_x = (double)(AchievementList.openInventory.displayRow * 24 - var4 / 2);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.REQUEST_STATS));
        this.buttonList.clear();
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 + 24, this.height / 2 + 74, 80, 20, I18n.format("gui.done", new Object[0])));
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (!this.field_146558_F)
        {
            if (p_146284_1_.id == 1)
            {
                this.mc.displayGuiScreen(this.field_146562_a);
            }
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (p_73869_2_ == this.mc.gameSettings.keyBindInventory.getKeyCode())
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
        }
        else
        {
            super.keyTyped(p_73869_1_, p_73869_2_);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        if (this.field_146558_F)
        {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingStats", new Object[0]), this.width / 2, this.height / 2, 16777215);
            this.drawCenteredString(this.fontRendererObj, field_146510_b_[(int)(Minecraft.getSystemTime() / 150L % (long)field_146510_b_.length)], this.width / 2, this.height / 2 + this.fontRendererObj.FONT_HEIGHT * 2, 16777215);
        }
        else
        {
            int var4;

            if (Mouse.isButtonDown(0))
            {
                var4 = (this.width - this.field_146555_f) / 2;
                int var5 = (this.height - this.field_146557_g) / 2;
                int var6 = var4 + 8;
                int var7 = var5 + 17;

                if ((this.field_146554_D == 0 || this.field_146554_D == 1) && p_73863_1_ >= var6 && p_73863_1_ < var6 + 224 && p_73863_2_ >= var7 && p_73863_2_ < var7 + 155)
                {
                    if (this.field_146554_D == 0)
                    {
                        this.field_146554_D = 1;
                    }
                    else
                    {
                        this.field_146567_u -= (double)((float)(p_73863_1_ - this.field_146563_h) * this.field_146570_r);
                        this.field_146566_v -= (double)((float)(p_73863_2_ - this.field_146564_i) * this.field_146570_r);
                        this.field_146565_w = this.field_146569_s = this.field_146567_u;
                        this.field_146573_x = this.field_146568_t = this.field_146566_v;
                    }

                    this.field_146563_h = p_73863_1_;
                    this.field_146564_i = p_73863_2_;
                }
            }
            else
            {
                this.field_146554_D = 0;
            }

            var4 = Mouse.getDWheel();
            float var11 = this.field_146570_r;

            if (var4 < 0)
            {
                this.field_146570_r += 0.25F;
            }
            else if (var4 > 0)
            {
                this.field_146570_r -= 0.25F;
            }

            this.field_146570_r = MathHelper.clamp_float(this.field_146570_r, 1.0F, 2.0F);

            if (this.field_146570_r != var11)
            {
                float var10000 = var11 - this.field_146570_r;
                float var12 = var11 * (float)this.field_146555_f;
                float var8 = var11 * (float)this.field_146557_g;
                float var9 = this.field_146570_r * (float)this.field_146555_f;
                float var10 = this.field_146570_r * (float)this.field_146557_g;
                this.field_146567_u -= (double)((var9 - var12) * 0.5F);
                this.field_146566_v -= (double)((var10 - var8) * 0.5F);
                this.field_146565_w = this.field_146569_s = this.field_146567_u;
                this.field_146573_x = this.field_146568_t = this.field_146566_v;
            }

            if (this.field_146565_w < (double)field_146572_y)
            {
                this.field_146565_w = (double)field_146572_y;
            }

            if (this.field_146573_x < (double)field_146571_z)
            {
                this.field_146573_x = (double)field_146571_z;
            }

            if (this.field_146565_w >= (double)field_146559_A)
            {
                this.field_146565_w = (double)(field_146559_A - 1);
            }

            if (this.field_146573_x >= (double)field_146560_B)
            {
                this.field_146573_x = (double)(field_146560_B - 1);
            }

            this.drawDefaultBackground();
            this.func_146552_b(p_73863_1_, p_73863_2_, p_73863_3_);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            this.func_146553_h();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }

    public void func_146509_g()
    {
        if (this.field_146558_F)
        {
            this.field_146558_F = false;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (!this.field_146558_F)
        {
            this.field_146569_s = this.field_146567_u;
            this.field_146568_t = this.field_146566_v;
            double var1 = this.field_146565_w - this.field_146567_u;
            double var3 = this.field_146573_x - this.field_146566_v;

            if (var1 * var1 + var3 * var3 < 4.0D)
            {
                this.field_146567_u += var1;
                this.field_146566_v += var3;
            }
            else
            {
                this.field_146567_u += var1 * 0.85D;
                this.field_146566_v += var3 * 0.85D;
            }
        }
    }

    protected void func_146553_h()
    {
        int var1 = (this.width - this.field_146555_f) / 2;
        int var2 = (this.height - this.field_146557_g) / 2;
        this.fontRendererObj.drawString(I18n.format("gui.achievements", new Object[0]), var1 + 15, var2 + 5, 4210752);
    }

    protected void func_146552_b(int p_146552_1_, int p_146552_2_, float p_146552_3_)
    {
        int var4 = MathHelper.floor_double(this.field_146569_s + (this.field_146567_u - this.field_146569_s) * (double)p_146552_3_);
        int var5 = MathHelper.floor_double(this.field_146568_t + (this.field_146566_v - this.field_146568_t) * (double)p_146552_3_);

        if (var4 < field_146572_y)
        {
            var4 = field_146572_y;
        }

        if (var5 < field_146571_z)
        {
            var5 = field_146571_z;
        }

        if (var4 >= field_146559_A)
        {
            var4 = field_146559_A - 1;
        }

        if (var5 >= field_146560_B)
        {
            var5 = field_146560_B - 1;
        }

        int var6 = (this.width - this.field_146555_f) / 2;
        int var7 = (this.height - this.field_146557_g) / 2;
        int var8 = var6 + 16;
        int var9 = var7 + 17;
        this.zLevel = 0.0F;
        GL11.glDepthFunc(GL11.GL_GEQUAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var8, (float)var9, -200.0F);
        GL11.glScalef(1.0F / this.field_146570_r, 1.0F / this.field_146570_r, 0.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        int var10 = var4 + 288 >> 4;
        int var11 = var5 + 288 >> 4;
        int var12 = (var4 + 288) % 16;
        int var13 = (var5 + 288) % 16;
        boolean var14 = true;
        boolean var15 = true;
        boolean var16 = true;
        boolean var17 = true;
        boolean var18 = true;
        Random var19 = new Random();
        float var20 = 16.0F / this.field_146570_r;
        float var21 = 16.0F / this.field_146570_r;
        int var22;
        int var24;
        int var25;

        for (var22 = 0; (float)var22 * var20 - (float)var13 < 155.0F; ++var22)
        {
            float var23 = 0.6F - (float)(var11 + var22) / 25.0F * 0.3F;
            GL11.glColor4f(var23, var23, var23, 1.0F);

            for (var24 = 0; (float)var24 * var21 - (float)var12 < 224.0F; ++var24)
            {
                var19.setSeed((long)(this.mc.getSession().getPlayerID().hashCode() + var10 + var24 + (var11 + var22) * 16));
                var25 = var19.nextInt(1 + var11 + var22) + (var11 + var22) / 2;
                IIcon var26 = Blocks.sand.getIcon(0, 0);

                if (var25 <= 37 && var11 + var22 != 35)
                {
                    if (var25 == 22)
                    {
                        if (var19.nextInt(2) == 0)
                        {
                            var26 = Blocks.diamond_ore.getIcon(0, 0);
                        }
                        else
                        {
                            var26 = Blocks.redstone_ore.getIcon(0, 0);
                        }
                    }
                    else if (var25 == 10)
                    {
                        var26 = Blocks.iron_ore.getIcon(0, 0);
                    }
                    else if (var25 == 8)
                    {
                        var26 = Blocks.coal_ore.getIcon(0, 0);
                    }
                    else if (var25 > 4)
                    {
                        var26 = Blocks.stone.getIcon(0, 0);
                    }
                    else if (var25 > 0)
                    {
                        var26 = Blocks.dirt.getIcon(0, 0);
                    }
                }
                else
                {
                    var26 = Blocks.bedrock.getIcon(0, 0);
                }

                this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                this.drawTexturedModelRectFromIcon(var24 * 16 - var12, var22 * 16 - var13, var26, 16, 16);
            }
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        this.mc.getTextureManager().bindTexture(field_146561_C);
        int var30;
        int var31;
        int var39;

        for (var22 = 0; var22 < AchievementList.achievementList.size(); ++var22)
        {
            Achievement var35 = (Achievement)AchievementList.achievementList.get(var22);

            if (var35.parentAchievement != null)
            {
                var24 = var35.displayColumn * 24 - var4 + 11;
                var25 = var35.displayRow * 24 - var5 + 11;
                var39 = var35.parentAchievement.displayColumn * 24 - var4 + 11;
                int var27 = var35.parentAchievement.displayRow * 24 - var5 + 11;
                boolean var28 = this.field_146556_E.hasAchievementUnlocked(var35);
                boolean var29 = this.field_146556_E.canUnlockAchievement(var35);
                var30 = this.field_146556_E.func_150874_c(var35);

                if (var30 <= 4)
                {
                    var31 = -16777216;

                    if (var28)
                    {
                        var31 = -6250336;
                    }
                    else if (var29)
                    {
                        var31 = -16711936;
                    }

                    this.drawHorizontalLine(var24, var39, var25, var31);
                    this.drawVerticalLine(var39, var25, var27, var31);

                    if (var24 > var39)
                    {
                        this.drawTexturedModalRect(var24 - 11 - 7, var25 - 5, 114, 234, 7, 11);
                    }
                    else if (var24 < var39)
                    {
                        this.drawTexturedModalRect(var24 + 11, var25 - 5, 107, 234, 7, 11);
                    }
                    else if (var25 > var27)
                    {
                        this.drawTexturedModalRect(var24 - 5, var25 - 11 - 7, 96, 234, 11, 7);
                    }
                    else if (var25 < var27)
                    {
                        this.drawTexturedModalRect(var24 - 5, var25 + 11, 96, 241, 11, 7);
                    }
                }
            }
        }

        Achievement var34 = null;
        RenderItem var36 = new RenderItem();
        float var37 = (float)(p_146552_1_ - var8) * this.field_146570_r;
        float var38 = (float)(p_146552_2_ - var9) * this.field_146570_r;
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        int var43;
        int var44;

        for (var39 = 0; var39 < AchievementList.achievementList.size(); ++var39)
        {
            Achievement var41 = (Achievement)AchievementList.achievementList.get(var39);
            var43 = var41.displayColumn * 24 - var4;
            var44 = var41.displayRow * 24 - var5;

            if (var43 >= -24 && var44 >= -24 && (float)var43 <= 224.0F * this.field_146570_r && (float)var44 <= 155.0F * this.field_146570_r)
            {
                var30 = this.field_146556_E.func_150874_c(var41);
                float var45;

                if (this.field_146556_E.hasAchievementUnlocked(var41))
                {
                    var45 = 0.75F;
                    GL11.glColor4f(var45, var45, var45, 1.0F);
                }
                else if (this.field_146556_E.canUnlockAchievement(var41))
                {
                    var45 = 1.0F;
                    GL11.glColor4f(var45, var45, var45, 1.0F);
                }
                else if (var30 < 3)
                {
                    var45 = 0.3F;
                    GL11.glColor4f(var45, var45, var45, 1.0F);
                }
                else if (var30 == 3)
                {
                    var45 = 0.2F;
                    GL11.glColor4f(var45, var45, var45, 1.0F);
                }
                else
                {
                    if (var30 != 4)
                    {
                        continue;
                    }

                    var45 = 0.1F;
                    GL11.glColor4f(var45, var45, var45, 1.0F);
                }

                this.mc.getTextureManager().bindTexture(field_146561_C);

                if (var41.getSpecial())
                {
                    this.drawTexturedModalRect(var43 - 2, var44 - 2, 26, 202, 26, 26);
                }
                else
                {
                    this.drawTexturedModalRect(var43 - 2, var44 - 2, 0, 202, 26, 26);
                }

                if (!this.field_146556_E.canUnlockAchievement(var41))
                {
                    var45 = 0.1F;
                    GL11.glColor4f(var45, var45, var45, 1.0F);
                    var36.renderWithColor = false;
                }

                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_CULL_FACE);
                var36.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), var41.theItemStack, var43 + 3, var44 + 3);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDisable(GL11.GL_LIGHTING);

                if (!this.field_146556_E.canUnlockAchievement(var41))
                {
                    var36.renderWithColor = true;
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                if (var37 >= (float)var43 && var37 <= (float)(var43 + 22) && var38 >= (float)var44 && var38 <= (float)(var44 + 22))
                {
                    var34 = var41;
                }
            }
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_146561_C);
        this.drawTexturedModalRect(var6, var7, 0, 0, this.field_146555_f, this.field_146557_g);
        this.zLevel = 0.0F;
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        super.drawScreen(p_146552_1_, p_146552_2_, p_146552_3_);

        if (var34 != null)
        {
            String var40 = var34.func_150951_e().getUnformattedText();
            String var42 = var34.getDescription();
            var43 = p_146552_1_ + 12;
            var44 = p_146552_2_ - 4;
            var30 = this.field_146556_E.func_150874_c(var34);

            if (!this.field_146556_E.canUnlockAchievement(var34))
            {
                String var32;
                int var33;

                if (var30 == 3)
                {
                    var40 = I18n.format("achievement.unknown", new Object[0]);
                    var31 = Math.max(this.fontRendererObj.getStringWidth(var40), 120);
                    var32 = (new ChatComponentTranslation("achievement.requires", new Object[] {var34.parentAchievement.func_150951_e()})).getUnformattedText();
                    var33 = this.fontRendererObj.splitStringWidth(var32, var31);
                    this.drawGradientRect(var43 - 3, var44 - 3, var43 + var31 + 3, var44 + var33 + 12 + 3, -1073741824, -1073741824);
                    this.fontRendererObj.drawSplitString(var32, var43, var44 + 12, var31, -9416624);
                }
                else if (var30 < 3)
                {
                    var31 = Math.max(this.fontRendererObj.getStringWidth(var40), 120);
                    var32 = (new ChatComponentTranslation("achievement.requires", new Object[] {var34.parentAchievement.func_150951_e()})).getUnformattedText();
                    var33 = this.fontRendererObj.splitStringWidth(var32, var31);
                    this.drawGradientRect(var43 - 3, var44 - 3, var43 + var31 + 3, var44 + var33 + 12 + 3, -1073741824, -1073741824);
                    this.fontRendererObj.drawSplitString(var32, var43, var44 + 12, var31, -9416624);
                }
                else
                {
                    var40 = null;
                }
            }
            else
            {
                var31 = Math.max(this.fontRendererObj.getStringWidth(var40), 120);
                int var46 = this.fontRendererObj.splitStringWidth(var42, var31);

                if (this.field_146556_E.hasAchievementUnlocked(var34))
                {
                    var46 += 12;
                }

                this.drawGradientRect(var43 - 3, var44 - 3, var43 + var31 + 3, var44 + var46 + 3 + 12, -1073741824, -1073741824);
                this.fontRendererObj.drawSplitString(var42, var43, var44 + 12, var31, -6250336);

                if (this.field_146556_E.hasAchievementUnlocked(var34))
                {
                    this.fontRendererObj.drawStringWithShadow(I18n.format("achievement.taken", new Object[0]), var43, var44 + var46 + 4, -7302913);
                }
            }

            if (var40 != null)
            {
                this.fontRendererObj.drawStringWithShadow(var40, var43, var44, this.field_146556_E.canUnlockAchievement(var34) ? (var34.getSpecial() ? -128 : -1) : (var34.getSpecial() ? -8355776 : -8355712));
            }
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        RenderHelper.disableStandardItemLighting();
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return !this.field_146558_F;
    }
}
