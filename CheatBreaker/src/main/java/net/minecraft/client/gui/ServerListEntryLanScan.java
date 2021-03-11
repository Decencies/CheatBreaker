package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanScan implements GuiListExtended.IGuiListEntry
{
    private final Minecraft field_148288_a = Minecraft.getMinecraft();


    public void func_148279_a(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
    {
        int var10 = p_148279_3_ + p_148279_5_ / 2 - this.field_148288_a.fontRenderer.FONT_HEIGHT / 2;
        this.field_148288_a.fontRenderer.drawString(I18n.format("lanServer.scanning", new Object[0]), this.field_148288_a.currentScreen.width / 2 - this.field_148288_a.fontRenderer.getStringWidth(I18n.format("lanServer.scanning", new Object[0])) / 2, var10, 16777215);
        String var11;

        switch ((int)(Minecraft.getSystemTime() / 300L % 4L))
        {
            case 0:
            default:
                var11 = "O o o";
                break;

            case 1:
            case 3:
                var11 = "o O o";
                break;

            case 2:
                var11 = "o o O";
        }

        this.field_148288_a.fontRenderer.drawString(var11, this.field_148288_a.currentScreen.width / 2 - this.field_148288_a.fontRenderer.getStringWidth(var11) / 2, var10 + this.field_148288_a.fontRenderer.FONT_HEIGHT, 8421504);
    }

    public boolean func_148278_a(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        return false;
    }

    public void func_148277_b(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {}
}
