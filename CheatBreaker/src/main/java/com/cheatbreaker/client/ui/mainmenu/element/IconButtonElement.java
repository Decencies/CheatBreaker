package com.cheatbreaker.client.ui.mainmenu.element;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.ui.fading.ColorFade;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class IconButtonElement
        extends AbstractElement {
    private ResourceLocation IIIllIllIlIlllllllIlIlIII;
    private String IllIIIIIIIlIlIllllIIllIII;
    private boolean lIIIIllIIlIlIllIIIlIllIlI;
    private final ColorFade IlllIllIlIIIIlIIlIIllIIIl;
    private final ColorFade IlIlllIIIIllIllllIllIIlIl;
    private final ColorFade llIIlllIIIIlllIllIlIlllIl;
    private float lIIlIlIllIIlIIIlIIIlllIII = 4;

    public IconButtonElement(ResourceLocation resourceLocation) {
        this.IIIllIllIlIlllllllIlIlIII = resourceLocation;
        this.IlllIllIlIIIIlIIlIIllIIIl = new ColorFade(0x4FFFFFFF, -1353670564);
        this.IlIlllIIIIllIllllIllIIlIl = new ColorFade(444958085, 1063565678);
        this.llIIlllIIIIlllIllIlIlllIl = new ColorFade(444958085, 1062577506);
    }

    public IconButtonElement(float f, ResourceLocation resourceLocation) {
        this.IIIllIllIlIlllllllIlIlIII = resourceLocation;
        this.lIIlIlIllIIlIIIlIIIlllIII = f;
        this.IlllIllIlIIIIlIIlIIllIIIl = new ColorFade(0x4FFFFFFF, -1353670564);
        this.IlIlllIIIIllIllllIllIIlIl = new ColorFade(444958085, 1063565678);
        this.llIIlllIIIIlllIllIlIlllIl = new ColorFade(444958085, 1062577506);
    }

    public IconButtonElement(String string) {
        this.IllIIIIIIIlIlIllllIIllIII = string;
        this.lIIIIllIIlIlIllIIIlIllIlI = true;
        this.IlllIllIlIIIIlIIlIIllIIIl = new ColorFade(0x4FFFFFFF, -1353670564);
        this.IlIlllIIIIllIllllIllIIlIl = new ColorFade(444958085, 1063565678);
        this.llIIlllIIIIlllIllIlIlllIl = new ColorFade(444958085, 1062577506);
    }

    @Override
    protected void handleElementDraw(float f, float f2, boolean bl) {
        boolean bl2 = bl && this.isMouseInside(f, f2);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.x, this.y, this.x + this.width, this.y + this.height, this.IlllIllIlIIIIlIIlIIllIIIl.lIIIIIIIIIlIllIIllIlIIlIl(bl2).getRGB(), this.IlIlllIIIIllIllllIllIIlIl.lIIIIIIIIIlIllIIllIlIIlIl(bl2).getRGB(), this.llIIlllIIIIlllIllIlIlllIl.lIIIIIIIIIlIllIIllIlIIlIl(bl2).getRGB());
        if (this.lIIIIllIIlIlIllIIIlIllIlI) {
            CheatBreaker.getInstance().robotoRegular13px.drawString(this.IllIIIIIIIlIlIllllIIllIII, this.x + this.width / 2.0f, this.y + 2.0f, -1);
        } else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.4444444f * 0.5538462f);
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.IIIllIllIlIlllllllIlIlIII, this.lIIlIlIllIIlIIIlIIIlllIII, this.x + this.width / 2.0f - this.lIIlIlIllIIlIIIlIIIlllIII, this.y + this.height / 2.0f - this.lIIlIlIllIIlIIIlIIIlllIII);
        }
    }

    public float IllIIIIIIIlIlIllllIIllIII() {
        return 22 + CheatBreaker.getInstance().robotoRegular13px.getStringWidth(this.IllIIIIIIIlIlIllllIIllIII) + 6;
    }

    @Override
    public boolean handleElementMouseClicked(float f, float f2, int n, boolean bl) {
        return false;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(ResourceLocation resourceLocation) {
        this.IIIllIllIlIlllllllIlIlIII = resourceLocation;
    }

    public void lIIIIlIIllIIlIIlIIIlIIllI(String string) {
        this.IllIIIIIIIlIlIllllIIllIII = string;
    }
}