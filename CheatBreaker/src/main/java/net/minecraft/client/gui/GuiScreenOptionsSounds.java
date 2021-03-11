package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiScreenOptionsSounds extends GuiScreen
{
    private final GuiScreen field_146505_f;
    private final GameSettings field_146506_g;
    protected String field_146507_a = "Options";
    private String field_146508_h;
    

    public GuiScreenOptionsSounds(GuiScreen p_i45025_1_, GameSettings p_i45025_2_)
    {
        this.field_146505_f = p_i45025_1_;
        this.field_146506_g = p_i45025_2_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        byte var1 = 0;
        this.field_146507_a = I18n.format("options.sounds.title", new Object[0]);
        this.field_146508_h = I18n.format("options.off", new Object[0]);
        this.buttonList.add(new GuiScreenOptionsSounds.Button(SoundCategory.MASTER.getCategoryId(), this.width / 2 - 155 + var1 % 2 * 160, this.height / 6 - 12 + 24 * (var1 >> 1), SoundCategory.MASTER, true));
        int var6 = var1 + 2;
        SoundCategory[] var2 = SoundCategory.values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            SoundCategory var5 = var2[var4];

            if (var5 != SoundCategory.MASTER)
            {
                this.buttonList.add(new GuiScreenOptionsSounds.Button(var5.getCategoryId(), this.width / 2 - 155 + var6 % 2 * 160, this.height / 6 - 12 + 24 * (var6 >> 1), var5, false));
                ++var6;
            }
        }

        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168, I18n.format("gui.done", new Object[0])));
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.enabled)
        {
            if (p_146284_1_.id == 200)
            {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.field_146505_f);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_146507_a, this.width / 2, 15, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    protected String func_146504_a(SoundCategory p_146504_1_)
    {
        float var2 = this.field_146506_g.getSoundLevel(p_146504_1_);
        return var2 == 0.0F ? this.field_146508_h : (int)(var2 * 100.0F) + "%";
    }

    class Button extends GuiButton
    {
        private final SoundCategory field_146153_r;
        private final String field_146152_s;
        public float field_146156_o = 1.0F;
        public boolean field_146155_p;
        

        public Button(int p_i45024_2_, int p_i45024_3_, int p_i45024_4_, SoundCategory p_i45024_5_, boolean p_i45024_6_)
        {
            super(p_i45024_2_, p_i45024_3_, p_i45024_4_, p_i45024_6_ ? 310 : 150, 20, "");
            this.field_146153_r = p_i45024_5_;
            this.field_146152_s = I18n.format("soundCategory." + p_i45024_5_.getCategoryName(), new Object[0]);
            this.displayString = this.field_146152_s + ": " + GuiScreenOptionsSounds.this.func_146504_a(p_i45024_5_);
            this.field_146156_o = GuiScreenOptionsSounds.this.field_146506_g.getSoundLevel(p_i45024_5_);
        }

        public int getHoverState(boolean p_146114_1_)
        {
            return 0;
        }

        protected void mouseDragged(Minecraft p_146119_1_, int p_146119_2_, int p_146119_3_)
        {
            if (this.field_146125_m)
            {
                if (this.field_146155_p)
                {
                    this.field_146156_o = (float)(p_146119_2_ - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);

                    if (this.field_146156_o < 0.0F)
                    {
                        this.field_146156_o = 0.0F;
                    }

                    if (this.field_146156_o > 1.0F)
                    {
                        this.field_146156_o = 1.0F;
                    }

                    p_146119_1_.gameSettings.setSoundLevel(this.field_146153_r, this.field_146156_o);
                    p_146119_1_.gameSettings.saveOptions();
                    this.displayString = this.field_146152_s + ": " + GuiScreenOptionsSounds.this.func_146504_a(this.field_146153_r);
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawTexturedModalRect(this.field_146128_h + (int)(this.field_146156_o * (float)(this.field_146120_f - 8)), this.field_146129_i, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.field_146128_h + (int)(this.field_146156_o * (float)(this.field_146120_f - 8)) + 4, this.field_146129_i, 196, 66, 4, 20);
            }
        }

        public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_)
        {
            if (super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_))
            {
                this.field_146156_o = (float)(p_146116_2_ - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);

                if (this.field_146156_o < 0.0F)
                {
                    this.field_146156_o = 0.0F;
                }

                if (this.field_146156_o > 1.0F)
                {
                    this.field_146156_o = 1.0F;
                }

                p_146116_1_.gameSettings.setSoundLevel(this.field_146153_r, this.field_146156_o);
                p_146116_1_.gameSettings.saveOptions();
                this.displayString = this.field_146152_s + ": " + GuiScreenOptionsSounds.this.func_146504_a(this.field_146153_r);
                this.field_146155_p = true;
                return true;
            }
            else
            {
                return false;
            }
        }

        public void func_146113_a(SoundHandler p_146113_1_) {}

        public void mouseReleased(int p_146118_1_, int p_146118_2_)
        {
            if (this.field_146155_p)
            {
                if (this.field_146153_r == SoundCategory.MASTER)
                {
                    float var10000 = 1.0F;
                }
                else
                {
                    GuiScreenOptionsSounds.this.field_146506_g.getSoundLevel(this.field_146153_r);
                }

                GuiScreenOptionsSounds.this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
            }

            this.field_146155_p = false;
        }
    }
}
