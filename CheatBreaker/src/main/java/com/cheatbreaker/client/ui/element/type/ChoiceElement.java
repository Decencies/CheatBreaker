package com.cheatbreaker.client.ui.element.type;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ChoiceElement
        extends AbstractModulesGuiElement {
    private final Setting setting;
    private final ResourceLocation leftIcon = new ResourceLocation("client/icons/left.png");
    private final ResourceLocation rightIcon = new ResourceLocation("client/icons/right.png");
    private int IlllIllIlIIIIlIIlIIllIIIl = 0;
    private float IlIlllIIIIllIllllIllIIlIl = 0.0f;
    private String llIIlllIIIIlllIllIlIlllIl;

    public ChoiceElement(Setting cBSetting, float f) {
        super(f);
        this.setting = cBSetting;
        this.height = 12;
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        boolean leftHovered = (float) mouseX > (float)(this.x + this.width - 92) * this.scale && (float) mouseX < (float)(this.x + this.width - 48) * this.scale && (float) mouseY > (float)(this.y + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + 14 + this.yOffset) * this.scale;
        boolean rightHovered = (float) mouseX > (float)(this.x + this.width - 48) * this.scale && (float) mouseX < (float)(this.x + this.width - 10) * this.scale && (float) mouseY > (float)(this.y + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + 14 + this.yOffset) * this.scale;
        CheatBreaker.getInstance().ubuntuMedium16px.drawString(this.setting.getLabel().toUpperCase(), this.x + 10, (float)(this.y + 4), leftHovered || rightHovered ? -1090519040 : -1895825408);
        boolean bl3 = this.setting.getLabel().toLowerCase().endsWith("color");
        String value = this.setting.getValue().toString();
        String[] split;
        if ((split = value.split(" ")).length > 1) {
            value = split[1].substring(0, Math.min(split[1].length(), 5)).trim() + "...";
        }
        if (!bl3) {
            if (this.IlllIllIlIIIIlIIlIIllIIIl == 0) {
                CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString(value, this.x + this.width - 48, this.y + 4, -1895825408);
            } else {
                boolean bl4 = this.IlllIllIlIIIIlIIlIIllIIIl == 1;
                CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString(value, (float)(this.x + this.width - 48) - (bl4 ? -this.IlIlllIIIIllIllllIllIIlIl : this.IlIlllIIIIllIllllIllIIlIl), this.y + 4, -1895825408);
                if (bl4) {
                    CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString(value, (float)(this.x + this.width - 98) + this.IlIlllIIIIllIllllIllIIlIl, this.y + 4, -1895825408);
                } else {
                    CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString(value, (float)(this.x + this.width + 2) - this.IlIlllIIIIllIllllIllIIlIl, this.y + 4, -1895825408);
                }
                if (this.IlIlllIIIIllIllllIllIIlIl >= (float)50) {
                    this.IlllIllIlIIIIlIIlIIllIIIl = 0;
                    this.IlIlllIIIIllIllllIllIIlIl = 0.0f;
                } else {
                    float f2 = CBModulesGui.getSmoothFloat((float)50 + this.IlIlllIIIIllIllllIllIIlIl * (float)15);
                    this.IlIlllIIIIllIllllIllIIlIl = this.IlIlllIIIIllIllllIllIIlIl + f2 >= (float)50 ? (float)50 : (this.IlIlllIIIIllIllllIllIIlIl += f2);
                }
                Gui.drawRect(this.x + this.width - 130, this.y + 2, this.x + this.width - 72, this.y + 12, -723724); // -723724
                Gui.drawRect(this.x + this.width - 22, this.y + 2, this.x + this.width + 4, this.y + 12, -723724); // -723724
            }
        } else if (this.IlllIllIlIIIIlIIlIIllIIIl == 0) {
            float f3 = CheatBreaker.getInstance().ubuntuMedium16px.getStringWidth(value);
            CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString(value, (float)(this.x + this.width) - 44.738373f * 1.0617284f - f3 / 2.0f, (float)this.y + 4, -16777216);
            CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString("§" + value + value, (float)(this.x + this.width - 48) - f3 / 2.0f, (float)(this.y + 4), -16777216);
        } else {
            boolean bl5 = this.IlllIllIlIIIIlIIlIIllIIIl == 1;
            CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString(this.llIIlllIIIIlllIllIlIlllIl, (float)(this.x + this.width - 48) - (bl5 ? -this.IlIlllIIIIllIllllIllIIlIl : this.IlIlllIIIIllIllllIllIIlIl), this.y + 4, -1895825408);
            float f4 = CheatBreaker.getInstance().ubuntuMedium16px.getStringWidth(value);
            if (bl5) {
                CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString(value, (float)(this.x + this.width) - 110.21739f * 0.88461536f - f4 / 2.0f + this.IlIlllIIIIllIllllIllIIlIl, (float)this.y + 4, -16777216);
                CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString("§" + value + value, (float)(this.x + this.width - 98) - f4 / 2.0f + this.IlIlllIIIIllIllllIllIIlIl, (float)(this.y + 4), -16777216);
            } else {
                CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString(value, (float)(this.x + this.width) - 2.6296296f * 0.57042253f - f4 / 2.0f - this.IlIlllIIIIllIllllIllIIlIl, (float)this.y + 4, -16777216);
                CheatBreaker.getInstance().ubuntuMedium16px.drawCenteredString("§" + value + value, (float)(this.x + this.width - 2) - f4 / 2.0f - this.IlIlllIIIIllIllllIllIIlIl, (float)(this.y + 4), -16777216);
            }
            if (this.IlIlllIIIIllIllllIllIIlIl >= (float)50) {
                this.IlllIllIlIIIIlIIlIIllIIIl = 0;
                this.IlIlllIIIIllIllllIllIIlIl = 0.0f;
            } else {
                float f5 = CBModulesGui.getSmoothFloat((float)50 + this.IlIlllIIIIllIllllIllIIlIl * (float)15);
                this.IlIlllIIIIllIllllIllIIlIl = this.IlIlllIIIIllIllllIllIIlIl + f5 >= (float)50 ? (float)50 : (this.IlIlllIIIIllIllllIllIIlIl += f5);
            }
            Gui.drawRect(this.x + this.width - 130, this.y + 2, this.x + this.width - 72, this.y + 12, -723724);
            Gui.drawRect(this.x + this.width - 22, this.y + 2, this.x + this.width + 4, this.y + 12, -723724);
        }
        GL11.glColor4f(0.0f, 0.0f, 0.0f, leftHovered ? 0.6857143f * 1.1666666f : 0.5416667f * 0.8307692f);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.leftIcon, (float)4, (float)(this.x + this.width - 82), (float)(this.y + 4));
        GL11.glColor4f(0.0f, 0.0f, 0.0f, rightHovered ? 0.82580644f * 0.96875f : 3.3793104f * 0.13316326f);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.rightIcon, (float)4, (float)(this.x + this.width - 22), (float)(this.y + 4));
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
        boolean leftHovered = (float) mouseX > (float) (this.x + this.width - 92) * this.scale && (float) mouseX < (float) (this.x + this.width - 48) * this.scale && (float) mouseY > (float) (this.y + this.yOffset) * this.scale && (float) mouseY < (float) (this.y + 14 + this.yOffset) * this.scale;
        boolean rightHovered = (float) mouseX > (float)(this.x + this.width - 48) * this.scale && (float) mouseX < (float)(this.x + this.width - 10) * this.scale && (float) mouseY > (float)(this.y + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + 14 + this.yOffset) * this.scale;
        if ((leftHovered || rightHovered) && this.IlllIllIlIIIIlIIlIIllIIIl == 0) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            for (int i = 0; i < this.setting.getAcceptedValues().length; ++i) {
                if (!((String[])this.setting.getAcceptedValues())[i].toLowerCase().equalsIgnoreCase((String)this.setting.getValue())) continue;
                this.llIIlllIIIIlllIllIlIlllIl = (String)this.setting.getValue();
                if (rightHovered) {
                    if (i + 1 >= this.setting.getAcceptedValues().length) {
                        this.IlllIllIlIIIIlIIlIIllIIIl = 2;
                        this.setting.setValue(((String[])this.setting.getAcceptedValues())[0]);
                        break;
                    }
                    this.IlllIllIlIIIIlIIlIIllIIIl = 2;
                    this.setting.setValue(((String[])this.setting.getAcceptedValues())[i + 1]);
                    break;
                }
                if (i - 1 < 0) {
                    this.IlllIllIlIIIIlIIlIIllIIIl = 1;
                    this.setting.setValue(((String[])this.setting.getAcceptedValues())[this.setting.getAcceptedValues().length - 1]);
                    break;
                }
                this.IlllIllIlIIIIlIIlIIllIIIl = 1;
                this.setting.setValue(((String[])this.setting.getAcceptedValues())[i - 1]);
                break;
            }
            if (this.setting == CheatBreaker.getInstance().globalSettings.clearGlass) {
                Minecraft.getMinecraft().renderGlobal.loadRenderers();
            }
        }
    }
}