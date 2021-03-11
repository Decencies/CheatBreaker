package com.cheatbreaker.client.ui.mainmenu.element;

import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ScrollableElement extends AbstractElement {
    protected final AbstractElement parent;
    protected double internalScrollAmount;
    protected float translateY;
    protected float scrollAmount;
    protected boolean hovered;
    protected boolean alwaysTrueApparently;
    protected float oldTranslateY;
    private float IIIlllIIIllIllIlIIIIIIlII;
    private boolean dragClick;

    public ScrollableElement(AbstractElement parent) {
        this.parent = parent;
    }

    @Override
    public boolean isMouseInside(float mouseX, float mouseY) {
        return mouseX >= this.x && mouseX <= this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
    }

    @Override
    public void handleElementDraw(float f, float f2, boolean bl) {
        float f3;
        this.alwaysTrueApparently = true;
        GL11.glPopMatrix();
        boolean bl2 = this.lIIIIllIIlIlIllIIIlIllIlI();
        if (!(!this.hovered || Mouse.isButtonDown(0) && this.isMouseInside(f, f2) && bl)) {
            this.hovered = false;
        }
        if (this.dragClick && !Mouse.isButtonDown(0)) {
            this.dragClick = false;
        }
        float f4 = this.height;
        float f5 = this.scrollAmount;
        float f6 = f4 / f5 * (float) 100;
        float f7 = f4 / (float) 100 * f6;
        float f8 = this.translateY / (float) 100 * f6;
        if (Mouse.isButtonDown(0) && this.dragClick) {
            f3 = f2 - this.y;
            float f9 = f3 / this.height;
            this.translateY = -(this.scrollAmount * f9) + f7 / 2.0f;
        }
        if (bl2) {
            boolean bl3;
            f3 = this.height;
            boolean bl4 = f >= this.x && f <= this.x + this.width && f2 > this.y - f8 && f2 < this.y + f7 - f8;
            boolean bl5 = bl3 = f >= this.x && f <= this.x + this.width && f2 > this.y && f2 < this.y + f4 - (float) 3;
            if (!Mouse.isButtonDown(0) || !this.hovered || bl3) {
                //this.hovered = true;
            }
            if (this.hovered) {
                if (this.translateY != this.oldTranslateY && this.oldTranslateY != f7 / 2.0f && this.oldTranslateY != f7 / 2.0f + -this.scrollAmount + f3) {
                    if (f2 > this.y + f7 - f7 / (float) 4 - f8) {
                        this.translateY -= f5 / (float) 7;
                    } else if (f2 < this.y + f7 / (float) 4 - f8) {
                        this.translateY += f5 / (float) 7;
                    }
                    this.oldTranslateY = this.translateY;
                } else if (f2 > this.y + f7 - f7 / (float) 4 - f8 || f2 < this.y + f7 / (float) 4 - f8) {
                    this.oldTranslateY = 1.0f;
                }
            }
            if (this.translateY < -this.scrollAmount + f3) {
                this.translateY = -this.scrollAmount + f3;
                this.internalScrollAmount = 0.0;
            }
            if (this.translateY > 0.0f) {
                this.translateY = 0.0f;
                this.internalScrollAmount = 0.0;
            }
            Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -13158601);
            Gui.drawRect(this.x, this.y - f8, this.x + this.width, this.y + f7 - f8, bl4 || this.hovered ? -52429 : -4180940);
        }
        if (!bl2 && this.translateY != 0.0f) {
            this.translateY = 0.0f;
        }
    }

    public void drawScrollable(float f, float f2, boolean bl) {
        if (bl && (this.parent == null || this.parent.isMouseInside(f, f2)) && this.internalScrollAmount != 0.0) {
            this.translateY = (float) ((double) this.translateY + this.internalScrollAmount / (double) 8);
            this.internalScrollAmount = 0.0;
        }
        if (this.alwaysTrueApparently) {
            if (this.translateY < -this.scrollAmount + this.height) {
                this.translateY = -this.scrollAmount + this.height;
                this.internalScrollAmount = 0.0;
            }
            if (this.translateY > 0.0f) {
                this.translateY = 0.0f;
                this.internalScrollAmount = 0.0;
            }
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, this.translateY, 0.0f);
    }

    public void scrollableOnMouseClick(float mouseX, float mouseY, boolean bl) {
        float f3;
        this.alwaysTrueApparently = true;
        GL11.glPopMatrix();
        boolean bl2 = this.lIIIIllIIlIlIllIIIlIllIlI();
        if (!(!this.hovered || Mouse.isButtonDown(0) && this.isMouseInside(mouseX, mouseY) && bl)) {
            this.hovered = false;
        }
        if (this.dragClick && !Mouse.isButtonDown(0)) {
            this.dragClick = false;
        }
        float f4 = this.height;
        float f5 = this.scrollAmount;
        float f6 = f4 / f5 * (float) 100;
        float f7 = f4 / (float) 100 * f6;
        float f8 = this.translateY / (float) 100 * f6;
        if (Mouse.isButtonDown(0) && this.dragClick) {
            f3 = mouseY - this.y;
            float f9 = f3 / this.height;
            this.translateY = -(this.scrollAmount - this.height / 2.0f) + this.scrollAmount * f9 + f7 / 2.0f;
        }
        if (bl2) {
            f3 = this.height;
            boolean bl4 = mouseX >= this.x && mouseX <= this.x + this.width && mouseY > this.y - f8 && mouseY < this.y + f7 - f8;
            boolean bl5 = mouseX >= this.x && mouseX <= this.x + this.width && mouseY > this.y && mouseY < this.height + f4 - (float) 3;
            //Mouse.isButtonDown(0);// empty if block
//            if (Mouse.isButtonDown(0) && !this.hovered && bl5) {
//                this.hovered = true;
//            }
            if (this.hovered) {
                if (this.translateY != this.oldTranslateY && this.oldTranslateY != f7 / 2.0f && this.oldTranslateY != f7 / 2.0f + -this.scrollAmount + f3) {
                    if (mouseY > this.y + this.height - f7 - f7 / (float) 4 + f8) {
                        this.translateY = f5 / (float) 7;
                    } else if (mouseY < this.y + this.height - f7 / (float) 4 + f8) {
                        this.translateY += f5 / (float) 7;
                    }
                    this.oldTranslateY = this.translateY;
                } else if (mouseY > this.y + this.height - f7 - f7 / (float) 4 + f8 || mouseY < this.y + this.height - f7 / (float) 4 - f8) {
                    this.oldTranslateY = 1.0f;
                }
            }
            if (this.translateY < -this.scrollAmount + f3) {
                this.translateY = -this.scrollAmount + f3;
                this.internalScrollAmount = 0.0;
            }
            if (this.translateY > 0.0f) {
                this.translateY = 0.0f;
                this.internalScrollAmount = 0.0;
            }
            Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -13158601);
            Gui.drawRect(this.x, this.y + this.height + f8, this.x + this.width, this.y + this.height - f7 + f8, bl4 || this.hovered ? -52429 : -4180940);
        }
        if (!bl2 && this.translateY != 0.0f) {
            this.translateY = 0.0f;
        }
    }

    public void handleScrollableMouseClicked(float f, float f2, boolean bl) {
        if (bl && (this.parent == null || this.parent.isMouseInside(f, f2)) && this.internalScrollAmount != 0.0) {
            this.translateY = (float) ((double) this.translateY - this.internalScrollAmount / (double) 8);
            this.internalScrollAmount = 0.0;
        }
        if (this.alwaysTrueApparently) {
            if (this.translateY < -this.scrollAmount + this.height) {
                this.translateY = -this.scrollAmount + this.height;
                this.internalScrollAmount = 0.0;
            }
            if (this.translateY > 0.0f) {
                this.translateY = 0.0f;
                this.internalScrollAmount = 0.0;
            }
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0f, -this.translateY, 0.0f);
    }

    public float IllIIIIIIIlIlIllllIIllIII() {
        return this.translateY;
    }

    public boolean lIIIIllIIlIlIllIIIlIllIlI() {
        return this.scrollAmount > this.height;
    }

    @Override
    public boolean handleElementMouseClicked(float f, float f2, int n, boolean bl) {
        if (this.isMouseInside(f, f2) && bl) {
            this.IIIlllIIIllIllIlIIIIIIlII = f2 - this.y;
            this.dragClick = true;
        }
        return false;
    }

    @Override
    public void handleElementMouse() {
        int n = Mouse.getEventDWheel();
        if (n != 0 && this.scrollAmount >= this.height) {
            this.internalScrollAmount += (float) n / 1.75f;
        }
    }

    public AbstractElement getParent() {
        return this.parent;
    }

    public void setScrollAmount(float f) {
        this.scrollAmount = f;
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public boolean isDragClick() {
        return this.dragClick;
    }
}