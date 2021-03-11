package net.minecraft.client.gui.stream;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IngestServerTester;
import net.minecraft.util.EnumChatFormatting;
import tv.twitch.broadcast.IngestServer;

public class GuiIngestServers extends GuiScreen
{
    private final GuiScreen field_152309_a;
    private String field_152310_f;
    private GuiIngestServers.ServerList field_152311_g;


    public GuiIngestServers(GuiScreen p_i46312_1_)
    {
        this.field_152309_a = p_i46312_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.field_152310_f = I18n.format("options.stream.ingest.title", new Object[0]);
        this.field_152311_g = new GuiIngestServers.ServerList();

        if (!this.mc.func_152346_Z().func_152908_z())
        {
            this.mc.func_152346_Z().func_152909_x();
        }

        this.buttonList.add(new GuiButton(1, this.width / 2 - 155, this.height - 24 - 6, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 5, this.height - 24 - 6, 150, 20, I18n.format("options.stream.ingest.reset", new Object[0])));
    }

    /**
     * "Called when the screen is unloaded. Used to disable keyboard repeat events."
     */
    public void onGuiClosed()
    {
        if (this.mc.func_152346_Z().func_152908_z())
        {
            this.mc.func_152346_Z().func_152932_y().func_153039_l();
        }
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.enabled)
        {
            if (p_146284_1_.id == 1)
            {
                this.mc.displayGuiScreen(this.field_152309_a);
            }
            else
            {
                this.mc.gameSettings.field_152407_Q = "";
                this.mc.gameSettings.saveOptions();
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        this.field_152311_g.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
        this.drawCenteredString(this.fontRendererObj, this.field_152310_f, this.width / 2, 20, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    class ServerList extends GuiSlot
    {


        public ServerList()
        {
            super(GuiIngestServers.this.mc, GuiIngestServers.this.width, GuiIngestServers.this.height, 32, GuiIngestServers.this.height - 35, (int)((double)GuiIngestServers.this.mc.fontRenderer.FONT_HEIGHT * 3.5D));
            this.func_148130_a(false);
        }

        protected int getSize()
        {
            return GuiIngestServers.this.mc.func_152346_Z().func_152925_v().length;
        }

        protected void elementClicked(int p_148144_1_, boolean p_148144_2_, int p_148144_3_, int p_148144_4_)
        {
            GuiIngestServers.this.mc.gameSettings.field_152407_Q = GuiIngestServers.this.mc.func_152346_Z().func_152925_v()[p_148144_1_].serverUrl;
            GuiIngestServers.this.mc.gameSettings.saveOptions();
        }

        protected boolean isSelected(int p_148131_1_)
        {
            return GuiIngestServers.this.mc.func_152346_Z().func_152925_v()[p_148131_1_].serverUrl.equals(GuiIngestServers.this.mc.gameSettings.field_152407_Q);
        }

        protected void drawBackground() {}

        protected void drawSlot(int p_148126_1_, int p_148126_2_, int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_, int p_148126_6_, int p_148126_7_)
        {
            IngestServer var8 = GuiIngestServers.this.mc.func_152346_Z().func_152925_v()[p_148126_1_];
            String var9 = var8.serverUrl.replaceAll("\\{stream_key\\}", "");
            String var10 = (int)var8.bitrateKbps + " kbps";
            String var11 = null;
            IngestServerTester var12 = GuiIngestServers.this.mc.func_152346_Z().func_152932_y();

            if (var12 != null)
            {
                if (var8 == var12.func_153040_c())
                {
                    var9 = EnumChatFormatting.GREEN + var9;
                    var10 = (int)(var12.func_153030_h() * 100.0F) + "%";
                }
                else if (p_148126_1_ < var12.func_153028_p())
                {
                    if (var8.bitrateKbps == 0.0F)
                    {
                        var10 = EnumChatFormatting.RED + "Down!";
                    }
                }
                else
                {
                    var10 = EnumChatFormatting.OBFUSCATED + "1234" + EnumChatFormatting.RESET + " kbps";
                }
            }
            else if (var8.bitrateKbps == 0.0F)
            {
                var10 = EnumChatFormatting.RED + "Down!";
            }

            p_148126_2_ -= 15;

            if (this.isSelected(p_148126_1_))
            {
                var11 = EnumChatFormatting.BLUE + "(Preferred)";
            }
            else if (var8.defaultServer)
            {
                var11 = EnumChatFormatting.GREEN + "(Default)";
            }

            GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, var8.serverName, p_148126_2_ + 2, p_148126_3_ + 5, 16777215);
            GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, var9, p_148126_2_ + 2, p_148126_3_ + GuiIngestServers.this.fontRendererObj.FONT_HEIGHT + 5 + 3, 3158064);
            GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, var10, this.func_148137_d() - 5 - GuiIngestServers.this.fontRendererObj.getStringWidth(var10), p_148126_3_ + 5, 8421504);

            if (var11 != null)
            {
                GuiIngestServers.this.drawString(GuiIngestServers.this.fontRendererObj, var11, this.func_148137_d() - 5 - GuiIngestServers.this.fontRendererObj.getStringWidth(var11), p_148126_3_ + 5 + 3 + GuiIngestServers.this.fontRendererObj.FONT_HEIGHT, 8421504);
            }
        }

        protected int func_148137_d()
        {
            return super.func_148137_d() + 15;
        }
    }
}
