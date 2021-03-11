package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class GuiControls extends GuiScreen
{
    private static final GameSettings.Options[] field_146492_g = new GameSettings.Options[] {GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN};
    private GuiScreen field_146496_h;
    protected String field_146495_a = "Controls";
    private GameSettings field_146497_i;
    public KeyBinding field_146491_f = null;
    public long field_152177_g;
    private GuiKeyBindingList field_146494_r;
    private GuiButton field_146493_s;


    public GuiControls(GuiScreen p_i1027_1_, GameSettings p_i1027_2_)
    {
        this.field_146496_h = p_i1027_1_;
        this.field_146497_i = p_i1027_2_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.field_146494_r = new GuiKeyBindingList(this, this.mc);
        this.buttonList.add(new GuiButton(200, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.field_146493_s = new GuiButton(201, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("controls.resetAll", new Object[0])));
        this.field_146495_a = I18n.format("controls.title", new Object[0]);
        int var1 = 0;
        GameSettings.Options[] var2 = field_146492_g;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            GameSettings.Options var5 = var2[var4];

            if (var5.getEnumFloat())
            {
                this.buttonList.add(new GuiOptionSlider(var5.returnEnumOrdinal(), this.width / 2 - 155 + var1 % 2 * 160, 18 + 24 * (var1 >> 1), var5));
            }
            else
            {
                this.buttonList.add(new GuiOptionButton(var5.returnEnumOrdinal(), this.width / 2 - 155 + var1 % 2 * 160, 18 + 24 * (var1 >> 1), var5, this.field_146497_i.getKeyBinding(var5)));
            }

            ++var1;
        }
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.id == 200)
        {
            this.mc.displayGuiScreen(this.field_146496_h);
        }
        else if (p_146284_1_.id == 201)
        {
            KeyBinding[] var2 = this.mc.gameSettings.keyBindings;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                KeyBinding var5 = var2[var4];
                var5.setKeyCode(var5.getKeyCodeDefault());
            }

            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (p_146284_1_.id < 100 && p_146284_1_ instanceof GuiOptionButton)
        {
            this.field_146497_i.setOptionValue(((GuiOptionButton)p_146284_1_).func_146136_c(), 1);
            p_146284_1_.displayString = this.field_146497_i.getKeyBinding(GameSettings.Options.getEnumOptions(p_146284_1_.id));
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        if (this.field_146491_f != null)
        {
            this.field_146497_i.setKeyCodeSave(this.field_146491_f, -100 + p_73864_3_);
            this.field_146491_f = null;
            KeyBinding.resetKeyBindingArrayAndHash();
        }
        else if (p_73864_3_ != 0 || !this.field_146494_r.func_148179_a(p_73864_1_, p_73864_2_, p_73864_3_))
        {
            super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        }
    }

    protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        if (p_146286_3_ != 0 || !this.field_146494_r.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_))
        {
            super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (this.field_146491_f != null)
        {
            if (p_73869_2_ == 1)
            {
                this.field_146497_i.setKeyCodeSave(this.field_146491_f, 0);
            }
            else
            {
                this.field_146497_i.setKeyCodeSave(this.field_146491_f, p_73869_2_);
            }

            this.field_146491_f = null;
            this.field_152177_g = Minecraft.getSystemTime();
            KeyBinding.resetKeyBindingArrayAndHash();
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
        this.drawDefaultBackground();
        this.field_146494_r.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.drawCenteredString(this.fontRendererObj, this.field_146495_a, this.width / 2, 8, 16777215);
        boolean var4 = true;
        KeyBinding[] var5 = this.field_146497_i.keyBindings;
        int var6 = var5.length;

        for (int var7 = 0; var7 < var6; ++var7)
        {
            KeyBinding var8 = var5[var7];

            if (var8.getKeyCode() != var8.getKeyCodeDefault())
            {
                var4 = false;
                break;
            }
        }

        this.field_146493_s.enabled = !var4;
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
