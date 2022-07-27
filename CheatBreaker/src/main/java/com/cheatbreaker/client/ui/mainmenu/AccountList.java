package com.cheatbreaker.client.ui.mainmenu;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.mainmenu.element.ScrollableElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.ui.fading.MinMaxFade;
import com.cheatbreaker.client.ui.fading.ColorFade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class AccountList extends AbstractElement {
    private ResourceLocation IIIllIllIlIlllllllIlIlIII;
    private String IllIIIIIIIlIlIllllIIllIII;
    private final ColorFade lIIIIllIIlIlIllIIIlIllIlI;
    private final ColorFade IlllIllIlIIIIlIIlIIllIIIl;
    private final ColorFade IlIlllIIIIllIllllIllIIlIl;
    private final MinMaxFade llIIlllIIIIlllIllIlIlllIl;
    private float lIIlIlIllIIlIIIlIIIlllIII;
    private boolean IIIlllIIIllIllIlIIIIIIlII;
    private final MainMenuBase llIlIIIlIIIIlIlllIlIIIIll;
    private final ScrollableElement IIIlIIllllIIllllllIlIIIll;
    private float lllIIIIIlIllIlIIIllllllII;

    public AccountList(MainMenuBase lIIIlIlIIllIIlllIIIlIIllI2, String string, ResourceLocation resourceLocation) {
        this.llIlIIIlIIIIlIlllIlIIIIll = lIIIlIlIIllIIlllIIIlIIllI2;
        this.IIIllIllIlIlllllllIlIlIII = resourceLocation;
        this.IllIIIIIIIlIlIllllIIllIII = string;
        this.IIIlIIllllIIllllllIlIIIll = new ScrollableElement(this);
        this.lIIIIllIIlIlIllIIIlIllIlI = new ColorFade(0x4FFFFFFF, -1353670564);
        this.IlllIllIlIIIIlIIlIIllIIIl = new ColorFade(444958085, 1063565678);
        this.IlIlllIIIIllIllllIllIIlIl = new ColorFade(444958085, 1062577506);
        this.llIIlllIIIIlllIllIlIlllIl = new MinMaxFade(300L);
    }

    public void IllIIIIIIIlIlIllllIIllIII() {
        this.setElementSize(this.x, this.y, this.width, this.height);
    }

    @Override
    public void setElementSize(float x, float y, float width, float height) {
        super.setElementSize(x, y, width, height);
        if (this.lIIlIlIllIIlIIIlIIIlllIII == 0.0f) {
            this.lIIlIlIllIIlIIIlIIIlllIII = height;
        }
        this.lllIIIIIlIllIlIIIllllllII = Math.min(this.llIlIIIlIIIIlIlllIlIIIIll.getAccounts().size() * 16 + 12, 120);
        this.IIIlIIllllIIllllllIlIIIll.setElementSize(x + width - (float)5, y + this.lIIlIlIllIIlIIIlIIIlllIII + (float)6, (float)4, this.lllIIIIIlIllIlIIIllllllII - (float)7);
        this.IIIlIIllllIIllllllIlIIIll.setScrollAmount(this.llIlIIIlIIIIlIlllIlIIIIll.getAccounts().size() * 16 + 4);
    }

    @Override
    public void handleElementMouse() {
        this.IIIlIIllllIIllllllIlIIIll.handleElementMouse();
    }

    @Override
    protected void handleElementDraw(float f, float f2, boolean bl) {
        boolean bl2 = bl && this.isMouseInside(f, f2);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.x, this.y, this.x + this.width, this.y + this.lIIlIlIllIIlIIIlIIIlllIII, this.lIIIIllIIlIlIllIIIlIllIlI.lIIIIIIIIIlIllIIllIlIIlIl(bl2).getRGB(), this.IlllIllIlIIIIlIIlIIllIIIl.lIIIIIIIIIlIllIIllIlIIlIl(bl2).getRGB(), this.IlIlllIIIIllIllllIllIIlIl.lIIIIIIIIIlIllIIllIlIIlIl(bl2).getRGB());
        float f3 = 6;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.drawIcon(this.IIIllIllIlIlllllllIlIlIII, f3, this.x + (float)4, this.y + this.lIIlIlIllIIlIIIlIIIlllIII / 2.0f - f3);
        CheatBreaker.getInstance().robotoRegular13px.drawString(this.IllIIIIIIIlIlIllllIIllIII, this.x + (float)22, this.y + 1.56f * 2.8846154f, -1342177281);
        float f4 = this.llIIlllIIIIlllIllIlIlllIl.lIIIIlIIllIIlIIlIIIlIIllI(this.isMouseInside(f, f2) && bl);
        if (this.llIIlllIIIIlllIllIlIlllIl.IIIllIllIlIlllllllIlIlIII()) {
            this.setElementSize(this.x, this.y, this.width, this.lIIlIlIllIIlIIIlIIIlllIII + this.lllIIIIIlIllIlIIIllllllII * f4);
            this.IIIlllIIIllIllIlIIIIIIlII = true;
        } else if (!this.llIIlllIIIIlllIllIlIlllIl.IIIllIllIlIlllllllIlIlIII() && !this.isMouseInside(f, f2)) {
            this.IIIlllIIIllIllIlIIIIIIlII = false;
        }
        if (this.IIIlllIIIllIllIlIIIIIIlII) {
            float f5 = 0.6122449f * 0.81666666f;
            float f6 = this.y + this.height + f5;
            float f7 = this.y + (float)5 + this.lIIlIlIllIIlIIIlIIIlllIII;
            if (f6 > f7) {
                Gui.drawBoxWithOutLine(this.x + 1.0f, f7, this.x + this.width - 1.0f, f6, f5, 0x4FFFFFFF, 444958085);
            }
            GL11.glPushMatrix();
            GL11.glEnable(0xc11);
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(
                    (int)this.x,
                    (int)(this.y + this.lIIlIlIllIIlIIIlIIIlllIII),
                    (int)(this.x + this.width),
                    (int)(this.y + this.lIIlIlIllIIlIIIlIIIlllIII + (float)7 + (this.height - this.lIIlIlIllIIlIIIlIIIlllIII - (float)6) * f4),
                    (float)((int)((float)this.llIlIIIlIIIIlIlllIlIIIIll.getResolution().getScaleFactor() * this.llIlIIIlIIIIlIlllIlIIIIll.getScaleFactor())),
                    (int)this.llIlIIIlIIIIlIlllIlIIIIll.getScaledHeight()
            );
            this.IIIlIIllllIIllllllIlIIIll.drawScrollable(f, f2, bl);
            int n = 1;
            for (Account account : this.llIlIIIlIIIIlIlllIlIIIIll.getAccounts()) {
                float f8 = this.x;
                float f9 = this.x + this.width;
                float f10 = this.y + this.lIIlIlIllIIlIIIlIIIlllIII + (float)(n * 16) - (float)8;
                float f11 = f10 + (float)16;
                boolean hovered = f > f8 && f < f9 && f2 - this.IIIlIIllllIIllllllIlIIIll.IllIIIIIIIlIlIllllIIllIII() > f10 && f2 - this.IIIlIIllllIIllllllIlIIIll.IllIIIIIIIlIlIllllIIllIII() < f11 && bl && !this.IIIlIIllllIIllllllIlIIIll.isMouseInside(f, f2) && !this.IIIlIIllllIIllllllIlIIIll.isDragClick();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, hovered ? 1.0f : 0.8148148f * 0.8590909f);
                RenderUtil.drawIcon(account.getHeadLocation(), f3, this.x + (float)4, f10 + (float)8 - f3);
                CheatBreaker.getInstance().robotoRegular13px.drawString(account.getDisplayName(), this.x + (float)22, f10 + (float)4, hovered ? -1 : -1342177281);
                ++n;
            }
            this.IIIlIIllllIIllllllIlIIIll.handleElementDraw(f, f2, bl);
            GL11.glDisable(0xc11);
            GL11.glPopMatrix();
        }
    }

    public float IIIIllIIllIIIIllIllIIIlIl(float f) {
        return (float)22 + f + (float)10;
    }

    @Override
    public boolean handleElementMouseClicked(float f, float f2, int n, boolean bl) {
        if (!bl) {
            return false;
        }
        if (this.llIIlllIIIIlllIllIlIlllIl.IllIllIIIlIIlllIIIllIllII()) {
            this.IIIlIIllllIIllllllIlIIIll.handleElementMouseClicked(f, f2, n, bl);
            int n2 = 1;
            for (Account illIIIllIlIIlIllIIIllllIl : this.llIlIIIlIIIIlIlllIlIIIIll.getAccounts()) {
                boolean bl2;
                float f3 = this.x;
                float f4 = this.x + this.width;
                float f5 = this.y + this.lIIlIlIllIIlIIIlIIIlllIII + (float)(n2 * 16) - (float)8;
                float f6 = f5 + (float)16;
                boolean bl3 = bl2 = f > f3 && f < f4 && f2 - this.IIIlIIllllIIllllllIlIIIll.IllIIIIIIIlIlIllllIIllIII() > f5 && f2 - this.IIIlIIllllIIllllllIlIIIll.IllIIIIIIIlIlIllllIIllIII() < f6 && bl && !this.IIIlIIllllIIllllllIlIIIll.isMouseInside(f, f2) && !this.IIIlIIllllIIllllllIlIIIll.isDragClick();
                if (bl2) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                    this.llIlIIIlIIIIlIlllIlIIIIll.login(illIIIllIlIIlIllIIIllllIl.getDisplayName());
                }
                ++n2;
            }
        }
        return false;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(ResourceLocation resourceLocation) {
        this.IIIllIllIlIlllllllIlIlIII = resourceLocation;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(String string) {
        this.IllIIIIIIIlIlIllllIIllIII = string;
    }

}
