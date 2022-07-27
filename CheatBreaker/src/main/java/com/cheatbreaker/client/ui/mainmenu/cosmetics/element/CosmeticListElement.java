package com.cheatbreaker.client.ui.mainmenu.cosmetics.element;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.util.cosmetic.Cosmetic;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CosmeticListElement extends AbstractModulesGuiElement {
    private final Cosmetic cosmetic;
    private final ResourceLocation checkmarkIcon = new ResourceLocation("client/icons/checkmark-32.png");

    public CosmeticListElement(Cosmetic cosmetic, float f) {
        super(f);
        this.height = 30;
        this.cosmetic = cosmetic;
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float scaleFactor) {
        boolean bl;
        boolean bl2 = bl = mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
        if (bl) {
            Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, 0x2F000000);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.cosmetic.getName().equals("cape")) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.cosmetic.getLocation());
            GL11.glPushMatrix();
            GL11.glTranslatef(this.x + 20, this.y + 7, 0.0f);
            GL11.glScalef(0.29591838f * 0.8448276f, 8.571428f * 0.015166666f, 0.22222222f * 1.125f);
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(0.0f, 0.0f, 2.0f, (float)7, 44, 120);
            GL11.glPopMatrix();
        } else {
            RenderUtil.drawIcon(this.cosmetic.getPreviewLocation(), (float)8, (float)(this.x + 20), (float)(this.y + 7));
        }
        CheatBreaker.getInstance().playRegular14px.drawString(this.cosmetic.getName().replace("_", " ").toUpperCase(), this.x + 42, (float)(this.y + this.height / 2 - 5), -1342177281);
        if (this.cosmetic.isEquipped()) {
            GL11.glColor4f(0.0f, 0.65542173f * 1.2205882f, 0.0f, 0.48423913f * 0.9292929f);
        } else {
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.07462687f * 3.35f);
        }
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.x + 8, this.y + this.height / 2, 3);
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
        boolean bl;
        boolean bl2 = bl = mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
        if (bl) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            if (this.cosmetic.isEquipped()) {
                this.cosmetic.setEquipped(false);
            } else if (this.cosmetic.getName().equals("cape")) {
                this.cosmetic.setEquipped(true);
                for (Cosmetic cosmetic : CheatBreaker.getInstance().getCosmetics()) {
                    if (cosmetic == this.cosmetic || !cosmetic.getName().equals("cape")) continue;
                    cosmetic.setEquipped(false);
                }
                this.cosmetic.setEquipped(true);
            } else {
                this.cosmetic.setEquipped(true);
                for (Cosmetic cosmetic : CheatBreaker.getInstance().getCosmetics()) {
                    if (cosmetic == this.cosmetic || cosmetic.getName().equals("cape")) continue;
                    cosmetic.setEquipped(false);
                }
                this.cosmetic.setEquipped(true);
            }
            //CBClient.getInstance().lIllIllIlIIllIllIlIlIIlIl().updateTick();
        }
    }
}