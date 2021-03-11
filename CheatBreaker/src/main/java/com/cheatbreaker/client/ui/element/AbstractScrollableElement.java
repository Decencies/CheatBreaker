package com.cheatbreaker.client.ui.element;

import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.util.RenderUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class AbstractScrollableElement extends AbstractModulesGuiElement {
    public int lIIIIllIIlIlIllIIIlIllIlI = 0;
    public int IlIlllIIIIllIllllIllIIlIl;
    public boolean lIIlIlIllIIlIIIlIIIlllIII = false;
    protected double IllIIIIIIIlIlIllllIIllIII = 0.0;
    protected int IlllIllIlIIIIlIIlIIllIIIl = 0;
    protected int llIIlllIIIIlllIllIlIlllIl;
    private boolean hovered = false;
    private float IIIlllIIIllIllIlIIIIIIlII;

    public AbstractScrollableElement(float scaleFactor, int n, int n2, int n3, int n4) {
        super(scaleFactor);
        this.IlIlllIIIIllIllllIllIIlIl = n;
        this.llIIlllIIIIlllIllIlIlllIl = n2;
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.height = n4;
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
    }

    public void preDraw(int n, int n2) {
        if (this.isMouseInside(n, n2)) {
            double d = Math.round(this.IllIIIIIIIlIlIllllIIllIII / (double) 25);
            this.IllIIIIIIIlIlIllllIIllIII -= d;
            if (this.IllIIIIIIIlIlIllllIIllIII != 0.0) {
                this.lIIIIllIIlIlIllIIIlIllIlI = (int) ((double) this.lIIIIllIIlIlIllIIIlIllIlI + d);
            }
        } else {
            this.IllIIIIIIIlIlIllllIIllIII = 0.0;
        }
        if (this.lIIlIlIllIIlIIIlIIIlllIII) {
            if (this.lIIIIllIIlIlIllIIIlIllIlI < -this.IlllIllIlIIIIlIIlIIllIIIl + this.height) {
                this.lIIIIllIIlIlIllIIIlIllIlI = -this.IlllIllIlIIIIlIIlIIllIIIl + this.height;
                this.IllIIIIIIIlIlIllllIIllIII = 0.0;
            }
            if (this.lIIIIllIIlIlIllIIIlIllIlI > 0) {
                this.lIIIIllIIlIlIllIIIlIllIlI = 0;
                this.IllIIIIIIIlIlIllllIIllIII = 0.0;
            }
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, this.lIIIIllIIlIlIllIIIlIllIlI, 0.0f);
    }

    public void postDraw(int n, int n2) {
        boolean bl;
        this.lIIlIlIllIIlIIIlIIIlllIII = true;
        GL11.glPopMatrix();
        boolean bl2 = bl = this.IlllIllIlIIIIlIIlIIllIIIl > this.height;
        if (!(!this.hovered || Mouse.isButtonDown(0) && this.isMouseInside(n, n2))) {
            this.hovered = false;
        }
        double d = this.height - 10;
        double d2 = this.IlllIllIlIIIIlIIlIIllIIIl;
        double d3 = d / d2 * (double) 100;
        double d4 = d / (double) 100 * d3;
        double d5 = (double) this.lIIIIllIIlIlIllIIIlIllIlI / (double) 100 * d3;
        if (bl) {
            boolean bl3;
            int n3 = this.height;
            boolean bl4 = (float) n > (float) (this.x + this.width - 9) * this.scale && (float) n < (float) (this.x + this.width - 3) * this.scale && (double) n2 > ((double) (this.y + 11) - d5) * (double) this.scale && (double) n2 < ((double) (this.y + 8) + d4 - d5) * (double) this.scale;
            boolean bl5 = bl3 = (float) n > (float) (this.x + this.width - 9) * this.scale && (float) n < (float) (this.x + this.width - 3) * this.scale && (float) n2 > (float) (this.y + 11) * this.scale && (double) n2 < ((double) (this.y + 6) + d - (double) 3) * (double) this.scale;
            if (Mouse.isButtonDown(0) && !this.hovered && bl3) {
                this.hovered = true;
            }
            if (this.hovered) {
                if ((float) this.lIIIIllIIlIlIllIIIlIllIlI != this.IIIlllIIIllIllIlIIIIIIlII && (double) this.IIIlllIIIllIllIlIIIIIIlII != d4 / (double) 2 && (double) this.IIIlllIIIllIllIlIIIIIIlII != d4 / (double) 2 + (double) (-this.IlllIllIlIIIIlIIlIIllIIIl) + (double) n3) {
                    if ((double) n2 > ((double) (this.y + 11) + d4 - d4 / (double) 4 - d5) * (double) this.scale) {
                        this.lIIIIllIIlIlIllIIIlIllIlI = (int) ((double) this.lIIIIllIIlIlIllIIIlIllIlI - d2 / (double) 7);
                    } else if ((double) n2 < ((double) (this.y + 11) + d4 / (double) 4 - d5) * (double) this.scale) {
                        this.lIIIIllIIlIlIllIIIlIllIlI = (int) ((double) this.lIIIIllIIlIlIllIIIlIllIlI + d2 / (double) 7);
                    }
                    this.IIIlllIIIllIllIlIIIIIIlII = this.lIIIIllIIlIlIllIIIlIllIlI;
                } else if ((double) n2 > ((double) (this.y + 11) + d4 - d4 / (double) 4 - d5) * (double) this.scale || (double) n2 < ((double) (this.y + 11) + d4 / (double) 4 - d5) * (double) this.scale) {
                    this.IIIlllIIIllIllIlIIIIIIlII = 1.0f;
                }
            }
            if (this.lIIIIllIIlIlIllIIIlIllIlI < -this.IlllIllIlIIIIlIIlIIllIIIl + n3) {
                this.lIIIIllIIlIlIllIIIlIllIlI = -this.IlllIllIlIIIIlIIlIIllIIIl + n3;
                this.IllIIIIIIIlIlIllllIIllIII = 0.0;
            }
            if (this.lIIIIllIIlIlIllIIIlIllIlI > 0) {
                this.lIIIIllIIlIlIllIIIlIllIlI = 0;
                this.IllIIIIIIIlIlIllllIIllIII = 0.0;
            }
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double) (this.x + this.width - 6), (double) (this.y + 11), (double) (this.x + this.width - 4), (double) (this.y + 6) + d - (double) 3, (double) 2, bl3 && !bl4 ? 0x6F000000 : 0x3F000000);
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double) (this.x + this.width - 7), (double) (this.y + 11) - d5, (double) (this.x + this.width - 3), (double) (this.y + 8) + d4 - d5, (double) 4, bl4 || this.hovered ? -16629505 : -12418828);
        }
        if (!bl && this.lIIIIllIIlIlIllIIIlIllIlI != 0) {
            this.lIIIIllIIlIlIllIIIlIllIlI = 0;
        }
    }

    @Override
    public void onScroll(int amount) {
        if (amount != 0 && this.IlllIllIlIIIIlIIlIIllIIIl >= this.height) {
            this.IllIIIIIIIlIlIllllIIllIII += (double) (amount / 2);
        }
    }

    public abstract boolean lIIIIlIIllIIlIIlIIIlIIllI(AbstractModule var1);

    public abstract void lIIIIIIIIIlIllIIllIlIIlIl(AbstractModule var1);
}
