package com.cheatbreaker.client.ui.element.type;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class SliderElement extends AbstractModulesGuiElement {
    private Setting setting;
    private float IllIIIIIIIlIlIllllIIllIII = -1;
    private float val;
    private boolean IlllIllIlIIIIlIIlIIllIIIl = false;

    public SliderElement(Setting cBSetting, float f) {
        super(f);
        this.setting = cBSetting;
        this.height = 14;
        this.IllIIIIIIIlIlIllllIIllIII = Float.parseFloat("" + cBSetting.getValue());
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
        int offset = 170;
        int width = 170;
        boolean bl2 = (float) mouseX > (float)(this.x + offset) * this.scale && (float) mouseX < (float)(this.x + offset + width - 2) * this.scale && (float) mouseY > (float)(this.y + 4 + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + 10 + this.yOffset) * this.scale;
        if (button == 0 && bl2) {
            this.IlllIllIlIIIIlIIlIIllIIIl = true;
        }
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        float f2;
        float f3;
        int n3 = 148;
        CheatBreaker.getInstance().ubuntuMedium16px.drawString(this.setting.getLabel().toUpperCase(), this.x + 10, (float)(this.y + 2), -1895825408);
        if (this.IlllIllIlIIIIlIIlIIllIIIl && !Mouse.isButtonDown(0)) {
            this.IlllIllIlIIIIlIIlIIllIIIl = false;
        }
        String string = this.setting.getValue() + "";
        CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString(string, this.x + 154, (float)(this.y + 2), -1895825408);
        boolean bl = (float) mouseX > (float)(this.x + 172) * this.scale && (float) mouseX < (float)(this.x + 172 + n3 - 2) * this.scale && (float) mouseY > (float)(this.y + 4 + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + 10 + this.yOffset) * this.scale;
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double)(this.x + 174), (double)(this.y + 6), (double)(this.x + 170 + n3 - 4), (double)(this.y + 8), 1.0, bl ? -1895825408 : 0x6F000000);
        double d = n3 - 18;
        float minVal = Float.parseFloat("" + this.setting.getMinimumValue());
        float maxVal = Float.parseFloat("" + this.setting.getMaximumValue());
        if (this.IlllIllIlIIIIlIIlIIllIIIl) {
            this.val = (float)Math.round(((double)minVal + (double)((float) mouseX - (float)(this.x + 180) * this.scale) * ((double)(maxVal - minVal) / (d * (double)this.scale))) * (double)100) / (float)100;
            if (this.setting.getType().equals(Setting.Type.INTEGER)) {
                this.val = Math.round(this.val);
            }
            if (this.val < minVal) {
                this.val = minVal;
            } else if (this.val > maxVal) {
                this.val = maxVal;
            }
            switch (this.setting.getType()) {
                case INTEGER: {
                    this.setting.setValue(Integer.parseInt((int)this.val + ""));
                    break;
                }
                case FLOAT: {
                    this.setting.setValue(this.val);
                    break;
                }
                case DOUBLE: {
                    this.setting.setValue(Double.parseDouble(this.val + ""));
                }
            }
            CheatBreaker.getInstance().moduleManager.keyStrokes.initialize();
        }
        f3 = (f3 = Float.parseFloat(this.setting.getValue() + "")) < this.IllIIIIIIIlIlIllllIIllIII ? this.IllIIIIIIIlIlIllllIIllIII - f3 : (f3 -= this.IllIIIIIIIlIlIllllIIllIII);
        float f6 = ((maxVal - minVal) / (float)20 + f3 * (float)8) / (float)(Minecraft.debugFPS + 1);
        if ((double)f6 < 43.5 * 2.2988505747126437E-6) {
            f6 = 1.9523809f * 5.121951E-5f;
        }
        if (this.IllIIIIIIIlIlIllllIIllIII < (f2 = Float.parseFloat(this.setting.getValue() + ""))) {
            this.IllIIIIIIIlIlIllllIIllIII = this.IllIIIIIIIlIlIllllIIllIII + f6 <= f2 ? (this.IllIIIIIIIlIlIllllIIllIII += f6) : f2;
        } else if (this.IllIIIIIIIlIlIllllIIllIII > f2) {
            this.IllIIIIIIIlIlIllllIIllIII = this.IllIIIIIIIlIlIllllIIllIII - f6 >= f2 ? (this.IllIIIIIIIlIlIllllIIllIII -= f6) : f2;
        }
        double d2 = (float)100 * ((this.IllIIIIIIIlIlIllllIIllIII - minVal) / (maxVal - minVal));
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double)(this.x + 174), (double)(this.y + 6), (double)(this.x + 180) + d * d2 / (double)100, (double)(this.y + 8), (double)4, -12418828);
        GL11.glColor4f(0.5714286f * 0.4375f, 0.45849055f * 0.9814815f, 1.0f, 1.0f);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double)((float)this.x + 543.75f * 0.33333334f) + d * d2 / (double)100, (float)this.y + 0.6666667f * 10.875f, 2.531249981140718 * 1.7777777910232544);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double)((float)this.x + 0.8804348f * 205.8642f) + d * d2 / (double)100, (float)this.y + 0.13043478f * 55.583332f, 2.639325754971479 * 1.0229885578155518);
    }
}