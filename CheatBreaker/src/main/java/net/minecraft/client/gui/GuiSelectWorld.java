package net.minecraft.client.gui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveFormatComparator;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiSelectWorld extends GuiScreen implements GuiYesNoCallback
{
    private static final Logger logger = LogManager.getLogger();
    private final DateFormat field_146633_h = new SimpleDateFormat();
    protected GuiScreen field_146632_a;
    protected String field_146628_f = "Select world";
    private boolean field_146634_i;
    private int field_146640_r;
    private java.util.List field_146639_s;
    private GuiSelectWorld.List field_146638_t;
    private String field_146637_u;
    private String field_146636_v;
    private String[] field_146635_w = new String[3];
    private boolean field_146643_x;
    private GuiButton field_146642_y;
    private GuiButton field_146641_z;
    private GuiButton field_146630_A;
    private GuiButton field_146631_B;


    public GuiSelectWorld(GuiScreen p_i1054_1_)
    {
        this.field_146632_a = p_i1054_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.field_146628_f = I18n.format("selectWorld.title", new Object[0]);

        try
        {
            this.func_146627_h();
        }
        catch (AnvilConverterException var2)
        {
            logger.error("Couldn\'t load level list", var2);
            this.mc.displayGuiScreen(new GuiErrorScreen("Unable to load worlds", var2.getMessage()));
            return;
        }

        this.field_146637_u = I18n.format("selectWorld.world", new Object[0]);
        this.field_146636_v = I18n.format("selectWorld.conversion", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.SURVIVAL.getID()] = I18n.format("gameMode.survival", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.CREATIVE.getID()] = I18n.format("gameMode.creative", new Object[0]);
        this.field_146635_w[WorldSettings.GameType.ADVENTURE.getID()] = I18n.format("gameMode.adventure", new Object[0]);
        this.field_146638_t = new GuiSelectWorld.List();
        this.field_146638_t.func_148134_d(4, 5);
        this.func_146618_g();
    }

    private void func_146627_h() throws AnvilConverterException
    {
        ISaveFormat var1 = this.mc.getSaveLoader();
        this.field_146639_s = var1.getSaveList();
        Collections.sort(this.field_146639_s);
        this.field_146640_r = -1;
    }

    protected String func_146621_a(int p_146621_1_)
    {
        return ((SaveFormatComparator)this.field_146639_s.get(p_146621_1_)).getFileName();
    }

    protected String func_146614_d(int p_146614_1_)
    {
        String var2 = ((SaveFormatComparator)this.field_146639_s.get(p_146614_1_)).getDisplayName();

        if (var2 == null || MathHelper.stringNullOrLengthZero(var2))
        {
            var2 = I18n.format("selectWorld.world", new Object[0]) + " " + (p_146614_1_ + 1);
        }

        return var2;
    }

    public void func_146618_g()
    {
        this.buttonList.add(this.field_146641_z = new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, I18n.format("selectWorld.select", new Object[0])));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4, this.height - 52, 150, 20, I18n.format("selectWorld.create", new Object[0])));
        this.buttonList.add(this.field_146630_A = new GuiButton(6, this.width / 2 - 154, this.height - 28, 72, 20, I18n.format("selectWorld.rename", new Object[0])));
        this.buttonList.add(this.field_146642_y = new GuiButton(2, this.width / 2 - 76, this.height - 28, 72, 20, I18n.format("selectWorld.delete", new Object[0])));
        this.buttonList.add(this.field_146631_B = new GuiButton(7, this.width / 2 + 4, this.height - 28, 72, 20, I18n.format("selectWorld.recreate", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 82, this.height - 28, 72, 20, I18n.format("gui.cancel", new Object[0])));
        this.field_146641_z.enabled = false;
        this.field_146642_y.enabled = false;
        this.field_146630_A.enabled = false;
        this.field_146631_B.enabled = false;
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.enabled)
        {
            if (p_146284_1_.id == 2)
            {
                String var2 = this.func_146614_d(this.field_146640_r);

                if (var2 != null)
                {
                    this.field_146643_x = true;
                    GuiYesNo var3 = func_152129_a(this, var2, this.field_146640_r);
                    this.mc.displayGuiScreen(var3);
                }
            }
            else if (p_146284_1_.id == 1)
            {
                this.func_146615_e(this.field_146640_r);
            }
            else if (p_146284_1_.id == 3)
            {
                this.mc.displayGuiScreen(new GuiCreateWorld(this));
            }
            else if (p_146284_1_.id == 6)
            {
                this.mc.displayGuiScreen(new GuiRenameWorld(this, this.func_146621_a(this.field_146640_r)));
            }
            else if (p_146284_1_.id == 0)
            {
                this.mc.displayGuiScreen(this.field_146632_a);
            }
            else if (p_146284_1_.id == 7)
            {
                GuiCreateWorld var5 = new GuiCreateWorld(this);
                ISaveHandler var6 = this.mc.getSaveLoader().getSaveLoader(this.func_146621_a(this.field_146640_r), false);
                WorldInfo var4 = var6.loadWorldInfo();
                var6.flush();
                var5.func_146318_a(var4);
                this.mc.displayGuiScreen(var5);
            }
            else
            {
                this.field_146638_t.func_148147_a(p_146284_1_);
            }
        }
    }

    public void func_146615_e(int p_146615_1_)
    {
        this.mc.displayGuiScreen((GuiScreen)null);

        if (!this.field_146634_i)
        {
            this.field_146634_i = true;
            String var2 = this.func_146621_a(p_146615_1_);

            if (var2 == null)
            {
                var2 = "World" + p_146615_1_;
            }

            String var3 = this.func_146614_d(p_146615_1_);

            if (var3 == null)
            {
                var3 = "World" + p_146615_1_;
            }

            if (this.mc.getSaveLoader().canLoadWorld(var2))
            {
                this.mc.launchIntegratedServer(var2, var3, (WorldSettings)null);
            }
        }
    }

    public void confirmClicked(boolean p_73878_1_, int p_73878_2_)
    {
        if (this.field_146643_x)
        {
            this.field_146643_x = false;

            if (p_73878_1_)
            {
                ISaveFormat var3 = this.mc.getSaveLoader();
                var3.flushCache();
                var3.deleteWorldDirectory(this.func_146621_a(p_73878_2_));

                try
                {
                    this.func_146627_h();
                }
                catch (AnvilConverterException var5)
                {
                    logger.error("Couldn\'t load level list", var5);
                }
            }

            this.mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.field_146638_t.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.drawCenteredString(this.fontRendererObj, this.field_146628_f, this.width / 2, 20, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    public static GuiYesNo func_152129_a(GuiYesNoCallback p_152129_0_, String p_152129_1_, int p_152129_2_)
    {
        String var3 = I18n.format("selectWorld.deleteQuestion", new Object[0]);
        String var4 = "\'" + p_152129_1_ + "\' " + I18n.format("selectWorld.deleteWarning", new Object[0]);
        String var5 = I18n.format("selectWorld.deleteButton", new Object[0]);
        String var6 = I18n.format("gui.cancel", new Object[0]);
        GuiYesNo var7 = new GuiYesNo(p_152129_0_, var3, var4, var5, var6, p_152129_2_);
        return var7;
    }

    class List extends GuiSlot
    {


        public List()
        {
            super(GuiSelectWorld.this.mc, GuiSelectWorld.this.width, GuiSelectWorld.this.height, 32, GuiSelectWorld.this.height - 64, 36);
        }

        protected int getSize()
        {
            return GuiSelectWorld.this.field_146639_s.size();
        }

        protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_)
        {
            GuiSelectWorld.this.field_146640_r = p_148144_1_;
            boolean var5 = GuiSelectWorld.this.field_146640_r >= 0 && GuiSelectWorld.this.field_146640_r < this.getSize();
            GuiSelectWorld.this.field_146641_z.enabled = var5;
            GuiSelectWorld.this.field_146642_y.enabled = var5;
            GuiSelectWorld.this.field_146630_A.enabled = var5;
            GuiSelectWorld.this.field_146631_B.enabled = var5;

            if (p_148144_2_ && var5)
            {
                GuiSelectWorld.this.func_146615_e(p_148144_1_);
            }
        }

        protected boolean isSelected(int p_148131_1_)
        {
            return p_148131_1_ == GuiSelectWorld.this.field_146640_r;
        }

        protected int func_148138_e()
        {
            return GuiSelectWorld.this.field_146639_s.size() * 36;
        }

        protected void drawBackground()
        {
            GuiSelectWorld.this.drawDefaultBackground();
        }

        protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_)
        {
            SaveFormatComparator var8 = (SaveFormatComparator)GuiSelectWorld.this.field_146639_s.get(p_148126_1_);
            String var9 = var8.getDisplayName();

            if (var9 == null || MathHelper.stringNullOrLengthZero(var9))
            {
                var9 = GuiSelectWorld.this.field_146637_u + " " + (p_148126_1_ + 1);
            }

            String var10 = var8.getFileName();
            var10 = var10 + " (" + GuiSelectWorld.this.field_146633_h.format(new Date(var8.getLastTimePlayed()));
            var10 = var10 + ")";
            String var11 = "";

            if (var8.requiresConversion())
            {
                var11 = GuiSelectWorld.this.field_146636_v + " " + var11;
            }
            else
            {
                var11 = GuiSelectWorld.this.field_146635_w[var8.getEnumGameType().getID()];

                if (var8.isHardcoreModeEnabled())
                {
                    var11 = EnumChatFormatting.DARK_RED + I18n.format("gameMode.hardcore", new Object[0]) + EnumChatFormatting.RESET;
                }

                if (var8.getCheatsEnabled())
                {
                    var11 = var11 + ", " + I18n.format("selectWorld.cheats", new Object[0]);
                }
            }

            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, var9, p_148126_2_ + 2, p_148126_3_ + 1, 16777215);
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, var10, p_148126_2_ + 2, p_148126_3_ + 12, 8421504);
            GuiSelectWorld.this.drawString(GuiSelectWorld.this.fontRendererObj, var11, p_148126_2_ + 2, p_148126_3_ + 12 + 10, 8421504);
        }
    }
}
