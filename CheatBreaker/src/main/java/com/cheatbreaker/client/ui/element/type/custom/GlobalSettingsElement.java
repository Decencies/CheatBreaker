package com.cheatbreaker.client.ui.element.type.custom;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.element.AbstractScrollableElement;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GlobalSettingsElement
        extends AbstractModulesGuiElement {
    private final int lIIIIlIIllIIlIIlIIIlIIllI;
    private final AbstractScrollableElement IllIIIIIIIlIlIllllIIllIII;
    private int lIIIIllIIlIlIllIIIlIllIlI = 0;
    private ResourceLocation IlllIllIlIIIIlIIlIIllIIIl = new ResourceLocation("client/icons/right.png");

    public GlobalSettingsElement(AbstractScrollableElement lllIllIllIlIllIlIIllllIIl2, int n, float f) {
        super(f);
        this.IllIIIIIIIlIlIllllIIllIII = lllIllIllIlIllIlIIllllIIl2;
        this.lIIIIlIIllIIlIIlIIIlIIllI = n;
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        boolean bl = this.isMouseInside(mouseX, mouseY);
        int n3 = 75;
        Gui.drawRect(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, 0x2F2F2F2F);
        float f2 = CBModulesGui.getSmoothFloat(790);
        if (bl) {
            if (this.lIIIIllIIlIlIllIIIlIllIlI < n3) {
                this.lIIIIllIIlIlIllIIIlIllIlI = (int)((float)this.lIIIIllIIlIlIllIIIlIllIlI + f2);
                if (this.lIIIIllIIlIlIllIIIlIllIlI > n3) {
                    this.lIIIIllIIlIlIllIIIlIllIlI = n3;
                }
            }
        } else if (this.lIIIIllIIlIlIllIIIlIllIlI > 0) {
            this.lIIIIllIIlIlIllIIIlIllIlI = (float)this.lIIIIllIIlIlIllIIIlIllIlI - f2 < 0.0f ? 0 : (int)((float)this.lIIIIllIIlIlIllIIIlIllIlI - f2);
        }
        if (this.lIIIIllIIlIlIllIIIlIllIlI > 0) {
            float f3 = (float)this.lIIIIllIIlIlIllIIIlIllIlI / (float)n3 * (float)100;
            Gui.drawRect(this.x, (int)((float)this.y + ((float)this.height - (float)this.height * f3 / (float)100)), this.x + this.width, this.y + this.height, this.lIIIIlIIllIIlIIlIIIlIIllI);
        }
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.4666667f * 0.23863636f);
        RenderUtil.drawIcon(this.IlllIllIlIIIIlIIlIIllIIIl, 2.2f * 1.1363636f, (float)(this.x + 6), (float)this.y + (float)6);
        CheatBreaker.getInstance().playBold18px.drawString("CheatBreaker Settings".toUpperCase(), (float)this.x + (float)14, (float)this.y + (float)3, -818991313);
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
    }
}
