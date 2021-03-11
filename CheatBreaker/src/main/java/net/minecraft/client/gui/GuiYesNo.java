package net.minecraft.client.gui;

import java.util.Iterator;
import net.minecraft.client.resources.I18n;

public class GuiYesNo extends GuiScreen
{
    protected GuiYesNoCallback field_146355_a;
    protected String field_146351_f;
    private String field_146354_r;
    protected String field_146352_g;
    protected String field_146356_h;
    protected int field_146357_i;
    private int field_146353_s;


    public GuiYesNo(GuiYesNoCallback p_i1082_1_, String p_i1082_2_, String p_i1082_3_, int p_i1082_4_)
    {
        this.field_146355_a = p_i1082_1_;
        this.field_146351_f = p_i1082_2_;
        this.field_146354_r = p_i1082_3_;
        this.field_146357_i = p_i1082_4_;
        this.field_146352_g = I18n.format("gui.yes", new Object[0]);
        this.field_146356_h = I18n.format("gui.no", new Object[0]);
    }

    public GuiYesNo(GuiYesNoCallback p_i1083_1_, String p_i1083_2_, String p_i1083_3_, String p_i1083_4_, String p_i1083_5_, int p_i1083_6_)
    {
        this.field_146355_a = p_i1083_1_;
        this.field_146351_f = p_i1083_2_;
        this.field_146354_r = p_i1083_3_;
        this.field_146352_g = p_i1083_4_;
        this.field_146356_h = p_i1083_5_;
        this.field_146357_i = p_i1083_6_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.add(new GuiOptionButton(0, this.width / 2 - 155, this.height / 6 + 96, this.field_146352_g));
        this.buttonList.add(new GuiOptionButton(1, this.width / 2 - 155 + 160, this.height / 6 + 96, this.field_146356_h));
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        this.field_146355_a.confirmClicked(p_146284_1_.id == 0, this.field_146357_i);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.field_146351_f, this.width / 2, 70, 16777215);
        this.drawCenteredString(this.fontRendererObj, this.field_146354_r, this.width / 2, 90, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public void func_146350_a(int p_146350_1_)
    {
        this.field_146353_s = p_146350_1_;
        GuiButton var3;

        for (Iterator var2 = this.buttonList.iterator(); var2.hasNext(); var3.enabled = false)
        {
            var3 = (GuiButton)var2.next();
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        GuiButton var2;

        if (--this.field_146353_s == 0)
        {
            for (Iterator var1 = this.buttonList.iterator(); var1.hasNext(); var2.enabled = true)
            {
                var2 = (GuiButton)var1.next();
            }
        }
    }
}
