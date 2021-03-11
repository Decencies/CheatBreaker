package net.minecraft.client.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class GuiWinGame extends GuiScreen
{
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation field_146576_f = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation field_146577_g = new ResourceLocation("textures/misc/vignette.png");
    private int field_146581_h;
    private List field_146582_i;
    private int field_146579_r;
    private float field_146578_s = 0.5F;


    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        ++this.field_146581_h;
        float var1 = (float)(this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;

        if ((float)this.field_146581_h > var1)
        {
            this.func_146574_g();
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (p_73869_2_ == 1)
        {
            this.func_146574_g();
        }
    }

    private void func_146574_g()
    {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        this.mc.displayGuiScreen((GuiScreen)null);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return true;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        if (this.field_146582_i == null)
        {
            this.field_146582_i = new ArrayList();

            try
            {
                String var1 = "";
                String var2 = "" + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN + EnumChatFormatting.AQUA;
                short var3 = 274;
                BufferedReader var4 = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream(), Charsets.UTF_8));
                Random var5 = new Random(8124371L);
                int var6;

                while ((var1 = var4.readLine()) != null)
                {
                    String var7;
                    String var8;

                    for (var1 = var1.replaceAll("PLAYERNAME", this.mc.getSession().getUsername()); var1.contains(var2); var1 = var7 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, var5.nextInt(4) + 3) + var8)
                    {
                        var6 = var1.indexOf(var2);
                        var7 = var1.substring(0, var6);
                        var8 = var1.substring(var6 + var2.length());
                    }

                    this.field_146582_i.addAll(this.mc.fontRenderer.listFormattedStringToWidth(var1, var3));
                    this.field_146582_i.add("");
                }

                for (var6 = 0; var6 < 8; ++var6)
                {
                    this.field_146582_i.add("");
                }

                var4 = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream(), Charsets.UTF_8));

                while ((var1 = var4.readLine()) != null)
                {
                    var1 = var1.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    var1 = var1.replaceAll("\t", "    ");
                    this.field_146582_i.addAll(this.mc.fontRenderer.listFormattedStringToWidth(var1, var3));
                    this.field_146582_i.add("");
                }

                this.field_146579_r = this.field_146582_i.size() * 12;
            }
            catch (Exception var9)
            {
                logger.error("Couldn\'t load credits", var9);
            }
        }
    }

    private void func_146575_b(int p_146575_1_, int p_146575_2_, float p_146575_3_)
    {
        Tessellator var4 = Tessellator.instance;
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        var4.startDrawingQuads();
        var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int var5 = this.width;
        float var6 = 0.0F - ((float)this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
        float var7 = (float)this.height - ((float)this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
        float var8 = 0.015625F;
        float var9 = ((float)this.field_146581_h + p_146575_3_ - 0.0F) * 0.02F;
        float var10 = (float)(this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        float var11 = (var10 - 20.0F - ((float)this.field_146581_h + p_146575_3_)) * 0.005F;

        if (var11 < var9)
        {
            var9 = var11;
        }

        if (var9 > 1.0F)
        {
            var9 = 1.0F;
        }

        var9 *= var9;
        var9 = var9 * 96.0F / 255.0F;
        var4.setColorOpaque_F(var9, var9, var9);
        var4.addVertexWithUV(0.0D, (double)this.height, (double)this.zLevel, 0.0D, (double)(var6 * var8));
        var4.addVertexWithUV((double)var5, (double)this.height, (double)this.zLevel, (double)((float)var5 * var8), (double)(var6 * var8));
        var4.addVertexWithUV((double)var5, 0.0D, (double)this.zLevel, (double)((float)var5 * var8), (double)(var7 * var8));
        var4.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, 0.0D, (double)(var7 * var8));
        var4.draw();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.func_146575_b(p_73863_1_, p_73863_2_, p_73863_3_);
        Tessellator var4 = Tessellator.instance;
        short var5 = 274;
        int var6 = this.width / 2 - var5 / 2;
        int var7 = this.height + 50;
        float var8 = -((float)this.field_146581_h + p_73863_3_) * this.field_146578_s;
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, var8, 0.0F);
        this.mc.getTextureManager().bindTexture(field_146576_f);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(var6, var7, 0, 0, 155, 44);
        this.drawTexturedModalRect(var6 + 155, var7, 0, 45, 155, 44);
        var4.setColorOpaque_I(16777215);
        int var9 = var7 + 200;
        int var10;

        for (var10 = 0; var10 < this.field_146582_i.size(); ++var10)
        {
            if (var10 == this.field_146582_i.size() - 1)
            {
                float var11 = (float)var9 + var8 - (float)(this.height / 2 - 6);

                if (var11 < 0.0F)
                {
                    GL11.glTranslatef(0.0F, -var11, 0.0F);
                }
            }

            if ((float)var9 + var8 + 12.0F + 8.0F > 0.0F && (float)var9 + var8 < (float)this.height)
            {
                String var12 = (String)this.field_146582_i.get(var10);

                if (var12.startsWith("[C]"))
                {
                    this.fontRendererObj.drawStringWithShadow(var12.substring(3), var6 + (var5 - this.fontRendererObj.getStringWidth(var12.substring(3))) / 2, var9, 16777215);
                }
                else
                {
                    this.fontRendererObj.fontRandom.setSeed((long)var10 * 4238972211L + (long)(this.field_146581_h / 4));
                    this.fontRendererObj.drawStringWithShadow(var12, var6, var9, 16777215);
                }
            }

            var9 += 12;
        }

        GL11.glPopMatrix();
        this.mc.getTextureManager().bindTexture(field_146577_g);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
        var4.startDrawingQuads();
        var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        var10 = this.width;
        int var13 = this.height;
        var4.addVertexWithUV(0.0D, (double)var13, (double)this.zLevel, 0.0D, 1.0D);
        var4.addVertexWithUV((double)var10, (double)var13, (double)this.zLevel, 1.0D, 1.0D);
        var4.addVertexWithUV((double)var10, 0.0D, (double)this.zLevel, 1.0D, 0.0D);
        var4.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, 0.0D, 0.0D);
        var4.draw();
        GL11.glDisable(GL11.GL_BLEND);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
