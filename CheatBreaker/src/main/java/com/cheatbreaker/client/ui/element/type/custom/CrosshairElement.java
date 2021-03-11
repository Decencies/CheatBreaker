package com.cheatbreaker.client.ui.element.type.custom;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.GlobalSettings;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CrosshairElement extends AbstractModulesGuiElement {
    public CrosshairElement(float f) {
        super(f);
        this.height = 50;
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(this.x + (this.width / 2 - 15) - 41, this.y + 4, this.x + (this.width / 2 - 15) + 41, this.y + 51, -16777216);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(new ResourceLocation("client/defaults/crosshair.png"), (float)(this.x + (this.width / 2 - 15) - 40), (float)(this.y + 5), (float)80, 45);
        GlobalSettings globalSettings = CheatBreaker.getInstance().globalSettings;
        GL11.glPushMatrix();
        float f2 = 1.0f / CheatBreaker.getScaleFactor();
        GL11.glScalef(f2, f2, f2);
        float f3 = (Float) globalSettings.crosshairSize.getValue();
        float f4 = (Float) globalSettings.crosshairGap.getValue();
        float f5 = (Float) globalSettings.crosshairThickness.getValue();
        int n3 = globalSettings.crosshairColor.getColorValue();
        boolean bl = (Boolean)globalSettings.crosshairOutline.getValue();
        int n4 = this.x + this.width / 2 - 15;
        int n5 = this.y + this.height / 2 + 3;
        if (bl) {
            Gui.lIIIIIIIIIlIllIIllIlIIlIl((float)n4 - f4 - f3, (float)n5 - f5 / 2.0f, (float)n4 - f4, (float)n5 + f5 / 2.0f, 0.3380282f * 1.4791666f, -1358954496, n3);
            Gui.lIIIIIIIIIlIllIIllIlIIlIl((float)n4 + f4, (float)n5 - f5 / 2.0f, (float)n4 + f4 + f3, (float)n5 + f5 / 2.0f, 3.909091f * 0.12790698f, -1358954496, n3);
            Gui.lIIIIIIIIIlIllIIllIlIIlIl((float)n4 - f5 / 2.0f, (float)n5 - f4 - f3, (float)n4 + f5 / 2.0f, (float)n5 - f4, 0.39506173f * 1.265625f, -1358954496, n3);
            Gui.lIIIIIIIIIlIllIIllIlIIlIl((float)n4 - f5 / 2.0f, (float)n5 + f4, (float)n4 + f5 / 2.0f, (float)n5 + f4 + f3, 5.5f * 0.09090909f, -1358954496, n3);
        } else {
            Gui.drawRect((float)n4 - f4 - f3, (float)n5 - f5 / 2.0f, (float)n4 - f4, (float)n5 + f5 / 2.0f, n3);
            Gui.drawRect((float)n4 + f4, (float)n5 - f5 / 2.0f, (float)n4 + f4 + f3, (float)n5 + f5 / 2.0f, n3);
            Gui.drawRect((float)n4 - f5 / 2.0f, (float)n5 - f4 - f3, (float)n4 + f5 / 2.0f, (float)n5 - f4, n3);
            Gui.drawRect((float)n4 - f5 / 2.0f, (float)n5 + f4, (float)n4 + f5 / 2.0f, (float)n5 + f4 + f3, n3);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
    }
}
