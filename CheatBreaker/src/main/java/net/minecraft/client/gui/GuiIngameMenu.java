package net.minecraft.client.gui;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.util.SessionServer;
import com.cheatbreaker.client.ui.fading.CosineFade;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiIngameMenu extends GuiScreen
{
    private int field_146445_a;
    private int field_146444_f;
    private GuiButton IlllIIIlIlllIllIlIIlllIlI;
    private final ResourceLocation IIIIllIlIIIllIlllIlllllIl = new ResourceLocation("client/logo_white.png");
    private final ResourceLocation IIIIllIIllIIIIllIllIIIlIl = new ResourceLocation("client/logo_255_outer.png");
    private final ResourceLocation IlIlIIIlllIIIlIlllIlIllIl = new ResourceLocation("client/logo_108_inner.png");
    private final CosineFade IIIllIllIlIlllllllIlIlIII = new CosineFade(4000L);
    private long IllIIIIIIIlIlIllllIIllIII;
    private boolean lIIIIllIIlIlIllIIIlIllIlI = false;
    private CosineFade IlllIllIlIIIIlIIlIIllIIIl = new CosineFade(1500L);
    private GuiButton modsButton;

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.field_146445_a = 0;
        this.buttonList.clear();
        byte var1 = -16;
        boolean var2 = true;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + var1, I18n.format("menu.returnToMenu", new Object[0])));

        if (!this.mc.isIntegratedServerRunning())
        {
            ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
        }

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + var1, I18n.format("menu.returnToGame", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + var1, 98, 20, I18n.format("menu.options", new Object[0])));
        GuiButton var3 = new GuiButton(7, this.width / 2 + 2, this.height / 4 + 96 + var1, 98, 20, I18n.format("menu.shareToLan", new Object[0]));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + var1, 98, 20, I18n.format("gui.achievements", new Object[0])));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + var1, 98, 20, I18n.format("gui.stats", new Object[0])));
        var3.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();

        if (!var3.enabled) {
            this.modsButton = new GuiButton(10, this.width / 2 + 2, this.height / 4 + 96 + var1, 98, 20, "Mods");
            this.buttonList.add(this.modsButton);
            this.buttonList.add(new GuiButton(16, this.width / 2 - 100, this.height / 4 + 72 + var1, 200, 20, "Server List"));
        } else {
            this.buttonList.add(var3);
            this.buttonList.add(new GuiButton(16, this.width / 2 - 100, this.height / 4 + 72 + var1, 98, 20, "Server List"));
            this.modsButton = new GuiButton(10, this.width / 2 + 2, this.height / 4 + 72 + var1, 98, 20, "Mods");
            this.buttonList.add(this.modsButton);
        }

    }

    private void renderRotatingLogo(double d, double d2) {
        try {
            if (!this.IIIllIllIlIlllllllIlIlIII.IIIIllIlIIIllIlllIlllllIl()) {
                this.IIIllIllIlIlllllllIlIlIII.lIIIIIIIIIlIllIIllIlIIlIl();
                this.IIIllIllIlIlllllllIlIlIII.IlllIIIlIlllIllIlIIlllIlI();
            }
            float f = 18;
            double d3 = d / (double)2 - (double)f;
            double d4 = this.buttonList.size() > 2 ? (double)((float)((GuiButton)this.buttonList.get((int)1)).field_146129_i - f - (float)32) : (double)-100;
            GL11.glPushMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glTranslatef((float)d3, (float)d4, 1.0f);
            GL11.glTranslatef(f, f, f);
            GL11.glRotatef((float)180 * this.IIIllIllIlIlllllllIlIlIII.IllIIIIIIIlIlIllllIIllIII(), 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef(-f, -f, -f);
            RenderUtil.lIIIIIIIIIlIllIIllIlIIlIl(this.IIIIllIIllIIIIllIllIIIlIl, f, 0.0f, 0.0f);
            GL11.glPopMatrix();
            RenderUtil.lIIIIIIIIIlIllIIllIlIIlIl(this.IlIlIIIlllIIIlIlllIlIllIl, f, (float)d3, (float)d4);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }


    protected void actionPerformed(GuiButton p_146284_1_)
    {
        switch (p_146284_1_.id)
        {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;

            case 1:
                p_146284_1_.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld(null);
                this.mc.displayGuiScreen(new GuiMainMenu());

            case 2:
            case 3:
            default:
                break;
            case 4:
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;
            case 5:
                this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.func_146107_m()));
                break;
            case 6:
                this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.func_146107_m()));
                break;
            case 7:
                this.mc.displayGuiScreen(new GuiShareToLan(this));
                break;
            case 10:
                this.mc.displayGuiScreen(new CBModulesGui());
                break;
            case 16:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.field_146444_f;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        this.drawDefaultBackground();
        int n3 = 600;
        int n4 = 356;
        double d = (double)Math.min(this.width, this.height) / ((double)n3 * (double)9);
        this.renderRotatingLogo(this.width, this.height);
        boolean bl = false;
        for (SessionServer server : CheatBreaker.getInstance().statusServers) {
            if (server.getStatus() != SessionServer.Status.UP) continue;
            bl = true;
        }
        if (bl) {
            if (!this.IlllIllIlIIIIlIIlIIllIIIl.IIIIllIlIIIllIlllIlllllIl()) {
                this.IlllIllIlIIIIlIIlIIllIIIl.lIIIIIIIIIlIllIIllIlIIlIl();
            }
            this.IlllIllIlIIIIlIIlIIllIIIl.IlllIIIlIlllIllIlIIlllIlI();
            Gui.drawRect(this.width / 2f - 100, this.height / 4f + 128, this.width / 2f + 100, this.height / 4 + 142, 0x6F000000);
            Gui.drawRect(this.width / 2f - 100, this.height / 4f + 128, this.width / 2f + 100, this.height / 4 + 142, new Color(1.0f, 0.2f * 0.75f, 10.6f * 0.014150944f, 1.4142857f * 0.45959595f * this.IlllIllIlIIIIlIIlIIllIIIl.IllIIIIIIIlIlIllllIIllIII()).getRGB());
            CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString("Some login services might be offline".toUpperCase(), this.width / 2, this.height / 4 + 130, -1);
        }
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }
}
