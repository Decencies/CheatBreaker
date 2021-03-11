package com.cheatbreaker.client.ui.element.type.custom;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class WorldTimeElement extends AbstractModulesGuiElement {
    private Setting setting;
    private float time = -1;
    private float lIIIIllIIlIlIllIIIlIllIlI;
    private boolean IlllIllIlIIIIlIIlIIllIIIl = false;
    private ResourceLocation sunIcon = new ResourceLocation("client/icons/sun-64.png");
    private ResourceLocation moonIcon = new ResourceLocation("client/icons/moon-64.png");

    public WorldTimeElement(Setting cBSetting, float f) {
        super(f);
        this.setting = cBSetting;
        this.height = 22;
        this.time = Float.parseFloat("" + cBSetting.getValue());
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
        boolean bl;
        int n4 = 170;
        bl = (float) mouseX > (float) (this.x + 170) * this.scale && (float) mouseX < (float) (this.x + 170 + n4 - 2) * this.scale && (float) mouseY > (float) (this.y + 4 + this.yOffset) * this.scale && (float) mouseY < (float) (this.y + 20 + this.yOffset) * this.scale;
        if (button == 0 && bl) {
            this.IlllIllIlIIIIlIIlIIllIIIl = true;
        }
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        float f2;
        float f3;
        int n3 = 148;
        CheatBreaker.getInstance().ubuntuMedium16px.drawString(this.setting.getLabel().toUpperCase(), this.x + 8, (float)(this.y + 8), -1895825408);
        if (this.IlllIllIlIIIIlIIlIIllIIIl && !Mouse.isButtonDown(0)) {
            this.IlllIllIlIIIIlIIlIIllIIIl = false;
        }
        CheatBreaker.getInstance().ubuntuMedium16px.drawString("SERVER", this.x + 172 + n3 / 2f, this.y - 2, -1895825408);
        Gui.drawRect((float)(this.x + 172 + n3 / 2) - 1.2580645f * 0.3974359f, this.y + 8, (float)(this.x + 172 + n3 / 2) + 0.33333334f * 1.5f, this.y + 14, 0x6F000000);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.moonIcon, (float)(this.x + 180) - 1.3170732f * 2.4675925f, (float)(this.y + 3), 6.346154f * 1.1818181f, 47.307693f * 0.15853658f);
        Gui.drawRect((float)(this.x + 180) - 0.4509804f * 1.1086956f, this.y + 12, (float)(this.x + 180) + 0.45652175f * 1.0952381f, this.y + 14, 0x6F000000);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.sunIcon, (float)(this.x + 170 + n3 - 10) - (float)5, (float)(this.y + 2), (float)10, 10);
        Gui.drawRect((float)(this.x + 170 + n3 - 10) - 1.1875f * 0.42105263f, this.y + 12, (float)(this.x + 170 + n3 - 10) + 0.4673913f * 1.0697675f, this.y + 14, 0x6F000000);
        boolean bl = (float) mouseX > (float)(this.x + 170) * this.scale && (float) mouseX < (float)(this.x + 170 + n3 - 2) * this.scale && (float) mouseY > (float)(this.y + 4 + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + 20 + this.yOffset) * this.scale;
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double)(this.x + 174), (double)(this.y + 16), (double)(this.x + 170 + n3 - 4), (double)(this.y + 18), 1.0, bl ? -1895825408 : 0x6F000000);
        double d = n3 - 18;
        float f4 = Float.parseFloat("" + this.setting.getMinimumValue());
        float f5 = Float.parseFloat("" + this.setting.getMaximumValue());
        if (this.IlllIllIlIIIIlIIlIIllIIIl) {
            this.lIIIIllIIlIlIllIIIlIllIlI = (float)Math.round(((double)f4 + (double)((float) mouseX - (float)(this.x + 180) * this.scale) * ((double)(f5 - f4) / (d * (double)this.scale))) * (double)100) / (float)100;
            if (this.lIIIIllIIlIlIllIIIlIllIlI < (float)-13490 && this.lIIIIllIIlIlIllIIIlIllIlI > (float)-15490) {
                this.lIIIIllIIlIlIllIIIlIllIlI = -14490;
            }
            if (this.setting.getType().equals((Object) Setting.Type.INTEGER)) {
                this.lIIIIllIIlIlIllIIIlIllIlI = Math.round(this.lIIIIllIIlIlIllIIIlIllIlI);
            }
            if (this.lIIIIllIIlIlIllIIIlIllIlI < f4) {
                this.lIIIIllIIlIlIllIIIlIllIlI = f4;
            } else if (this.lIIIIllIIlIlIllIIIlIllIlI > f5) {
                this.lIIIIllIIlIlIllIIIlIllIlI = f5;
            }
            Minecraft.getMinecraft().theWorld.setWorldTime((long) (Integer) CheatBreaker.getInstance().globalSettings.worldTime.getValue());
            switch (this.setting.getType()) {
                case INTEGER: {
                    this.setting.setValue(Integer.parseInt((int)this.lIIIIllIIlIlIllIIIlIllIlI + ""));
                    break;
                }
                case FLOAT: {
                    this.setting.setValue(this.lIIIIllIIlIlIllIIIlIllIlI);
                    break;
                }
                case DOUBLE: {
                    this.setting.setValue(Double.parseDouble(this.lIIIIllIIlIlIllIIIlIllIlI + ""));
                }
            }
        }
        f3 = (f3 = Float.parseFloat(this.setting.getValue() + "")) < this.time ? this.time - f3 : (f3 -= this.time);
        float f6 = ((f5 - f4) / (float)20 + f3 * (float)8) / (float)(Minecraft.debugFPS + 1);
        if ((double)f6 < (double)0.18f * 5.555555334797621E-4) {
            f6 = 2.1333334f * 4.6874997E-5f;
        }
        if (this.time < (f2 = Float.parseFloat(this.setting.getValue() + ""))) {
            this.time = this.time + f6 <= f2 ? (this.time += f6) : f2;
        } else if (this.time > f2) {
            this.time = this.time - f6 >= f2 ? (this.time -= f6) : f2;
        }
        double d2 = (float)100 * ((this.time - f4) / (f5 - f4));
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double)(this.x + 174), (double)(this.y + 16), (double)(this.x + 180) + d * d2 / (double)100, (double)(this.y + 18), (double)4, -12418828);
        GL11.glColor4f(0.6666667f * 0.375f, 0.45f, 1.0f, 1.0f);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double)((float)this.x + 1359.3749f * 0.13333334f) + d * d2 / (double)100, (float)this.y + 18.818182f * 0.9166667f, 31.125001159496648 * 0.14457830786705017);
        if (this.time == (float)-14490) {
            GL11.glColor4f(0.7738095f * 0.32307693f, 0.037499998f * 12.0f, 1.0f, 1.0f);
        } else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double)((float)this.x + 39.5f * 4.588608f) + d * d2 / (double)100, (float)this.y + 1.2763158f * 13.515464f, 1.6875000046566129 * (double)1.6f);
    }
}
