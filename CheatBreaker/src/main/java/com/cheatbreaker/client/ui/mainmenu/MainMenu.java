package com.cheatbreaker.client.ui.mainmenu;

import java.awt.Color;

import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.ui.fading.MinMaxFade;
import com.cheatbreaker.client.ui.fading.CosineFade;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class MainMenu extends MainMenuBase {
    private final ResourceLocation outerLogo = new ResourceLocation("client/logo_255_outer.png");
    private final ResourceLocation innerLogo = new ResourceLocation("client/logo_108_inner.png");
    private GradientTextButton singleplayerButton = new GradientTextButton("SINGLEPLAYER");
    private GradientTextButton multiplayerButton = new GradientTextButton("MULTIPLAYER");
    private final MinMaxFade lIIIIllIIlIlIllIIIlIllIlI = new MinMaxFade(750L);
    private final CosineFade IlllIllIlIIIIlIIlIIllIIIl;
    private final MinMaxFade IlIlllIIIIllIllllIllIIlIl = new MinMaxFade(400L);
    private static int lIllIlIlllIIlIIllIIlIIlII;

    public MainMenu() {
        this.IlllIllIlIIIIlIIlIIllIIIl = new CosineFade(4000L);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.llIlIIIlIIIIlIlllIlIIIIll() && !this.lIIIIllIIlIlIllIIIlIllIlI.IIIIllIlIIIllIlllIlllllIl()) {
            this.lIIIIllIIlIlIllIIIlIllIlI.lIIIIIIIIIlIllIIllIlIIlIl();
        }
        if (!(this.llIlIIIlIIIIlIlllIlIIIIll() && !this.lIIIIllIIlIlIllIIIlIllIlI.IIIIllIIllIIIIllIllIIIlIl() || this.IlllIllIlIIIIlIIlIIllIIIl.IIIIllIlIIIllIlllIlllllIl())) {
            this.IlIlllIIIIllIllllIllIIlIl.lIIIIIIIIIlIllIIllIlIIlIl();
            this.IlllIllIlIIIIlIIlIIllIIIl.lIIIIIIIIIlIllIIllIlIIlIl();
            this.IlllIllIlIIIIlIIlIIllIIIl.IlllIIIlIlllIllIlIIlllIlI();
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        this.singleplayerButton.setElementSize(this.getScaledWidth() / 2.0f - (float)50, this.getScaledHeight() / 2.0f + (float)5, (float)100, 12);
        this.multiplayerButton.setElementSize(this.getScaledWidth() / 2.0f - (float)50, this.getScaledHeight() / 2.0f + (float)24, (float)100, 12);
        ++lIllIlIlllIIlIIllIIlIIlII;
    }

    @Override
    public void drawMenu(float f, float f2) {
        float f3;
        super.drawMenu(f, f2);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.singleplayerButton.drawElement(f, f2, true);
        this.multiplayerButton.drawElement(f, f2, true);
        drawRect(this.singleplayerButton.getX() - (float)20, this.getScaledHeight() / 2.0f - (float)80, this.singleplayerButton.getX() + this.singleplayerButton.getWidth() + (float)20, this.multiplayerButton.getY() + this.multiplayerButton.getHeight() + (float)14, 0x2F000000);
        float f4 = f3 = this.llIlIIIlIIIIlIlllIlIIIIll() ? this.lIIIIllIIlIlIllIIIlIllIlI.IllIIIIIIIlIlIllllIIllIII() : 1.0f;
        if (this.llIlIIIlIIIIlIlllIlIIIIll()) {
            drawRect(0.0f, 0.0f, this.getScaledWidth(), this.getScaledHeight(), new Color(1.0f, 1.0f, 1.0f, 1.0f - 1.0f * this.IlIlllIIIIllIllllIllIIlIl.IllIIIIIIIlIlIllllIIllIII()).getRGB());
        }
        this.lIIIIlIIllIIlIIlIIIlIIllI((double)this.getScaledWidth(), (double)this.getScaledHeight(), f3);
        float f5 = this.getScaledWidth() / 2.0f - (float)80;
        float f6 = this.getScaledHeight() - (float)40;
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double)f5, (double)f6, (double)(f5 + (float)160), (double)(f6 + (float)10), (double)8, new Color(218, 66, 83, (int)((float)255 * (1.0f - f3))).getRGB());
    }

    private void lIIIIlIIllIIlIIlIIIlIIllI(double d, double d2, float f) {
        float f2 = 27;
        double d3 = d / (double)2 - (double)f2;
        double d4 = d2 / (double)2 - (double)f2 - (double)((float)35 * f);
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslatef((float)d3, (float)d4, 1.0f);
        GL11.glTranslatef(f2, f2, f2);
        GL11.glRotatef((float)180 * this.IlllIllIlIIIIlIIlIIllIIIl.IllIIIIIIIlIlIllllIIllIII(), 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-f2, -f2, -f2);
        RenderUtil.lIIIIIIIIIlIllIIllIlIIlIl(this.outerLogo, f2, 0.0f, 0.0f);
        GL11.glPopMatrix();
        RenderUtil.lIIIIIIIIIlIllIIllIlIIlIl(this.innerLogo, f2, (float)d3, (float)d4);
    }

    @Override
    public void onMouseClicked(float mouseX, float mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        this.singleplayerButton.handleElementMouseClicked(mouseX, mouseY, button, true);
        this.multiplayerButton.handleElementMouseClicked(mouseX, mouseY, button, true);
        if (this.singleplayerButton.isMouseInside(mouseX, mouseY)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        } else if (this.multiplayerButton.isMouseInside(mouseX, mouseY)) {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
    }

    public boolean llIlIIIlIIIIlIlllIlIIIIll() {
        return lIllIlIlllIIlIIllIIlIIlII == 1;
    }

    public MinMaxFade IIIlIIllllIIllllllIlIIIll() {
        return this.IlIlllIIIIllIllllIllIIlIl;
    }
}
