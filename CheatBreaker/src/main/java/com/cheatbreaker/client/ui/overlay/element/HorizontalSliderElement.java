package com.cheatbreaker.client.ui.overlay.element;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.ui.fading.AbstractFade;
import com.cheatbreaker.client.ui.fading.MinMaxFade;
import com.cheatbreaker.client.ui.mainmenu.AbstractElement;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

public class HorizontalSliderElement
        extends AbstractElement {
    private final Setting IIIllIllIlIlllllllIlIlIII;
    private final AbstractFade IllIIIIIIIlIlIllllIIllIII;
    private Number lIIIIllIIlIlIllIIIlIllIlI;

    public HorizontalSliderElement(Setting cBSetting) {
        this.IIIllIllIlIlllllllIlIlIII = cBSetting;
        this.IllIIIIIIIlIlIllllIIllIII = new MinMaxFade(200L);
        this.lIIIIllIIlIlIllIIIlIllIlI = (Number)cBSetting.getValue();
    }

    @Override
    protected void handleElementDraw(float f, float f2, boolean bl) {
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -13158601);
        if (!this.IllIIIIIIIlIlIllllIIllIII.IIIIllIlIIIllIlllIlllllIl()) {
            this.lIIIIllIIlIlIllIIIlIllIlI = (Number)this.IIIllIllIlIlllllllIlIlIII.getValue();
        }
        float f3 = ((Number)this.IIIllIllIlIlllllllIlIlIII.getValue()).floatValue();
        float f4 = this.IIIllIllIlIlllllllIlIlIII.getMinimumValue().floatValue();
        float f5 = this.IIIllIllIlIlllllllIlIlIII.getMaximumValue().floatValue();
        float f6 = f3 - this.lIIIIllIIlIlIllIIIlIllIlI.floatValue();
        float f7 = (float)100 * ((this.lIIIIllIIlIlIllIIIlIllIlI.floatValue() + f6 * this.IllIIIIIIIlIlIllllIIllIII.IllIIIIIIIlIlIllllIIllIII() - f4) / (f5 - f4));
        Gui.drawRect(this.x, this.y, this.x + this.width / (float)100 * f7, this.y + this.height, -52429);
    }

    @Override
    public boolean handleElementMouseClicked(float f, float f2, int n, boolean bl) {
        if (!bl) {
            return false;
        }
        if (Mouse.isButtonDown((int)0) && this.isMouseInside(f, f2)) {
            this.IllIIIIIIIlIlIllllIIllIII.lIIIIIIIIIlIllIIllIlIIlIl();
            this.lIIIIllIIlIlIllIIIlIllIlI = (Number)this.IIIllIllIlIlllllllIlIlIII.getValue();
            float f3 = ((Number)this.IIIllIllIlIlllllllIlIlIII.getMinimumValue()).floatValue();
            float f4 = ((Number)this.IIIllIllIlIlllllllIlIlIII.getMaximumValue()).floatValue();
            if (f - this.x > this.width / 2.0f) {
                f += 2.0f;
            }
            float f5 = f3 + (f - this.x) * ((f4 - f3) / this.width);
            switch (this.IIIllIllIlIlllllllIlIlIII.getType()) {
                case INTEGER: {
                    this.IIIllIllIlIlllllllIlIlIII.setValue(this.lIIIIlIIllIIlIIlIIIlIIllI((Object)Integer.parseInt((int)f5 + "")));
                    break;
                }
                case FLOAT: {
                    this.IIIllIllIlIlllllllIlIlIII.setValue(this.lIIIIlIIllIIlIIlIIIlIIllI(f5));
                    break;
                }
                case DOUBLE: {
                    this.IIIllIllIlIlllllllIlIlIII.setValue(this.lIIIIlIIllIIlIIlIIIlIIllI(Double.parseDouble((double)f5 + "")));
                }
            }
        }
        return super.handleElementMouseClicked(f, f2, n, bl);
    }

    private Object lIIIIlIIllIIlIIlIIIlIIllI(Object object) {
        try {
            return object;
        }
        catch (ClassCastException classCastException) {
            return null;
        }
    }

    public Setting IllIIIIIIIlIlIllllIIllIII() {
        return this.IIIllIllIlIlllllllIlIlIII;
    }
}
