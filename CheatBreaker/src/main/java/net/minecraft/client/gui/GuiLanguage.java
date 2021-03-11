package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage extends GuiScreen
{
    protected GuiScreen field_146453_a;
    private GuiLanguage.List field_146450_f;
    private final GameSettings field_146451_g;
    private final LanguageManager field_146454_h;
    private GuiOptionButton field_146455_i;
    private GuiOptionButton field_146452_r;


    public GuiLanguage(GuiScreen p_i1043_1_, GameSettings p_i1043_2_, LanguageManager p_i1043_3_)
    {
        this.field_146453_a = p_i1043_1_;
        this.field_146451_g = p_i1043_2_;
        this.field_146454_h = p_i1043_3_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        boolean var1 = false;

        if (this.field_146455_i != null)
        {
            ;
        }

        this.buttonList.add(this.field_146455_i = new GuiOptionButton(100, this.width / 2 - 155, this.height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.field_146451_g.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
        this.buttonList.add(this.field_146452_r = new GuiOptionButton(6, this.width / 2 - 155 + 160, this.height - 38, I18n.format("gui.done", new Object[0])));
        this.field_146450_f = new GuiLanguage.List();
        this.field_146450_f.func_148134_d(7, 8);
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.enabled)
        {
            switch (p_146284_1_.id)
            {
                case 5:
                    break;

                case 6:
                    this.mc.displayGuiScreen(this.field_146453_a);
                    break;

                case 100:
                    if (p_146284_1_ instanceof GuiOptionButton)
                    {
                        this.field_146451_g.setOptionValue(((GuiOptionButton)p_146284_1_).func_146136_c(), 1);
                        p_146284_1_.displayString = this.field_146451_g.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
                        ScaledResolution var2 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                        int var3 = var2.getScaledWidth();
                        int var4 = var2.getScaledHeight();
                        this.setWorldAndResolution(this.mc, var3, var4);
                    }

                    break;

                default:
                    this.field_146450_f.func_148147_a(p_146284_1_);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.field_146450_f.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.drawCenteredString(this.fontRendererObj, I18n.format("options.language", new Object[0]), this.width / 2, 16, 16777215);
        this.drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning", new Object[0]) + ")", this.width / 2, this.height - 56, 8421504);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    class List extends GuiSlot
    {
        private final java.util.List field_148176_l = Lists.newArrayList();
        private final Map field_148177_m = Maps.newHashMap();


        public List()
        {
            super(GuiLanguage.this.mc, GuiLanguage.this.width, GuiLanguage.this.height, 32, GuiLanguage.this.height - 65 + 4, 18);
            Iterator var2 = GuiLanguage.this.field_146454_h.getLanguages().iterator();

            while (var2.hasNext())
            {
                Language var3 = (Language)var2.next();
                this.field_148177_m.put(var3.getLanguageCode(), var3);
                this.field_148176_l.add(var3.getLanguageCode());
            }
        }

        protected int getSize()
        {
            return this.field_148176_l.size();
        }

        protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_)
        {
            Language var5 = (Language)this.field_148177_m.get(this.field_148176_l.get(p_148144_1_));
            GuiLanguage.this.field_146454_h.setCurrentLanguage(var5);
            GuiLanguage.this.field_146451_g.language = var5.getLanguageCode();
            GuiLanguage.this.mc.refreshResources();
            GuiLanguage.this.fontRendererObj.setUnicodeFlag(GuiLanguage.this.field_146454_h.isCurrentLocaleUnicode() || GuiLanguage.this.field_146451_g.forceUnicodeFont);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.field_146454_h.isCurrentLanguageBidirectional());
            GuiLanguage.this.field_146452_r.displayString = I18n.format("gui.done", new Object[0]);
            GuiLanguage.this.field_146455_i.displayString = GuiLanguage.this.field_146451_g.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
            GuiLanguage.this.field_146451_g.saveOptions();
        }

        protected boolean isSelected(int p_148131_1_)
        {
            return ((String)this.field_148176_l.get(p_148131_1_)).equals(GuiLanguage.this.field_146454_h.getCurrentLanguage().getLanguageCode());
        }

        protected int func_148138_e()
        {
            return this.getSize() * 18;
        }

        protected void drawBackground()
        {
            GuiLanguage.this.drawDefaultBackground();
        }

        protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_)
        {
            GuiLanguage.this.fontRendererObj.setBidiFlag(true);
            GuiLanguage.this.drawCenteredString(GuiLanguage.this.fontRendererObj, ((Language)this.field_148177_m.get(this.field_148176_l.get(p_148126_1_))).toString(), this.field_148155_a / 2, p_148126_3_ + 1, 16777215);
            GuiLanguage.this.fontRendererObj.setBidiFlag(GuiLanguage.this.field_146454_h.getCurrentLanguage().isBidirectional());
        }
    }
}
