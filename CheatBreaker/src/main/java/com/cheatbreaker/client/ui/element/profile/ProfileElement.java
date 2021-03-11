package com.cheatbreaker.client.ui.element.profile;

import java.io.File;
import java.util.Collections;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Profile;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.element.AbstractScrollableElement;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.module.CBProfileCreateGui;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

// IIlIlllllIIIlIIllIllIlIlI
public class ProfileElement
        extends AbstractModulesGuiElement {
    private final int IllIIIIIIIlIlIllllIIllIII;
    public final Profile profile;
    private final AbstractScrollableElement parent;
    private int IlllIllIlIIIIlIIlIIllIIIl = 0;
    private final ResourceLocation deleteIcon = new ResourceLocation("client/icons/delete-64.png");
    private final ResourceLocation arrowIcon = new ResourceLocation("client/icons/right.png");
    private final ResourceLocation pencilIcon = new ResourceLocation("client/icons/pencil-64.png");

    public ProfileElement(AbstractScrollableElement parent, int n, Profile profile, float f) {
        super(f);
        this.parent = parent;
        this.IllIIIIIIIlIlIllllIIllIII = n;
        this.profile = profile;
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        boolean bl;
        boolean bl2;
        float f2;
        boolean bl3 = mouseX > this.x + 12 && this.isMouseInside(mouseX, mouseY);
        int n3 = 75;
        Gui.drawRect(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, 0x2F2F2F2F);
        if (bl3) {
            if (this.IlllIllIlIIIIlIIlIIllIIIl < n3) {
                f2 = CBModulesGui.getSmoothFloat(790);
                this.IlllIllIlIIIIlIIlIIllIIIl = (int)((float)this.IlllIllIlIIIIlIIlIIllIIIl + f2);
                if (this.IlllIllIlIIIIlIIlIIllIIIl > n3) {
                    this.IlllIllIlIIIIlIIlIIllIIIl = n3;
                }
            }
        } else if (this.IlllIllIlIIIIlIIlIIllIIIl > 0) {
            f2 = CBModulesGui.getSmoothFloat(790);
            this.IlllIllIlIIIIlIIlIIllIIIl = (float)this.IlllIllIlIIIIlIIlIIllIIIl - f2 < 0.0f ? 0 : (int)((float)this.IlllIllIlIIIIlIIlIIllIIIl - f2);
        }
        if (this.IlllIllIlIIIIlIIlIIllIIIl > 0) {
            f2 = (float)this.IlllIllIlIIIIlIIlIIllIIIl / (float)n3 * (float)100;
            Gui.drawRect(this.x + 12, (int)((float)this.y + ((float)this.height - (float)this.height * f2 / (float)100)), this.x + this.width - (this.profile.isEditable() ? 0 : 30), this.y + this.height, this.IllIIIIIIIlIlIllllIIllIII);
        }
        boolean bl4 = (float) mouseX > (float)this.x * this.scale && (float) mouseX < (float)(this.x + 12) * this.scale && (float) mouseY >= (float)(this.y + this.yOffset) * this.scale && (float) mouseY <= (float)(this.y + this.height / 2 + this.yOffset) * this.scale;
        boolean bl5 = (float) mouseX > (float)this.x * this.scale && (float) mouseX < (float)(this.x + 12) * this.scale && (float) mouseY > (float)(this.y + this.height / 2 + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + this.height + this.yOffset) * this.scale;
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.1919192f * 1.8236842f);
        float f3 = 6.571429f * 0.38043478f;
        if (this.profile.isEditable()) {
            bl2 = false;
            bl = false;
            ProfilesListElement parent = (ProfilesListElement)this.parent;
            if (parent.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this) != 0 && parent.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this) > 1) {
                bl2 = true;
                GL11.glPushMatrix();
                if (bl4) {
                    GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.14444444f * 4.5f);
                }
                GL11.glTranslatef((float)(this.x + 6) - f3, (float)this.y + (float)5, 0.0f);
                GL11.glRotatef(-90, 0.0f, 0.0f, 1.0f);
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.arrowIcon, f3, (float)-1, 0.0f);
                GL11.glPopMatrix();
                GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0952381f * 0.3195652f);
            }
            if (parent.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this) != parent.lIIIIlIIllIIlIIlIIIlIIllI.size() - 1) {
                bl = true;
                GL11.glPushMatrix();
                if (bl5) {
                    GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.2112676f * 0.5366279f);
                }
                GL11.glTranslatef((float)(this.x + 6) + f3, (float)this.y + (float)7, 0.0f);
                GL11.glRotatef(90, 0.0f, 0.0f, 1.0f);
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.arrowIcon, f3, 2.0f, 0.0f);
                GL11.glPopMatrix();
            }
            if (!bl2 && !bl) {
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.arrowIcon, 1.173913f * 2.1296296f, (float)(this.x + 4), (float)this.y + (float)6);
            }
        } else {
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.arrowIcon, 6.6666665f * 0.375f, (float)(this.x + 4), (float)this.y + (float)6);
        }
        if (CheatBreaker.getInstance().activeProfile == this.profile) {
            CheatBreaker.getInstance().playBold18px.drawString(this.profile.getName().toUpperCase(), (float)this.x + (float)16, (float)(this.y + 4), -818991313);
        } else {
            CheatBreaker.getInstance().playRegular16px.drawString(this.profile.getName().toUpperCase(), (float)this.x + (float)16, (float)this.y + 4, -818991313);
        }
        if (CheatBreaker.getInstance().activeProfile == this.profile) {
            CheatBreaker.getInstance().playRegular14px.drawString(" (Active)", (float)this.x + (float)17 + (float) CheatBreaker.getInstance().playBold18px.getStringWidth(this.profile.getName().toUpperCase()), (float)this.y + (float)7, 0x6F2F2F2F);
        }
        if (this.profile.isEditable()) {
            bl2 = (float) mouseX > (float)(this.x + this.width - 30) * this.scale && (float) mouseX < (float)(this.x + this.width - 13) * this.scale && (float) mouseY > (float)(this.y + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + this.height + this.yOffset) * this.scale;
            GL11.glColor4f(bl2 ? 0.0f : 1.1707317f * 0.21354167f, bl2 ? 0.0f : 0.101648346f * 2.4594595f, bl2 ? 0.48876402f * 1.0229886f : 0.5647059f * 0.4427083f, 0.5675676f * 1.145238f);
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.pencilIcon, (float)5, (float)(this.x + this.width - 26), (float)this.y + 5.1916666f * 0.6741573f);
            bl = (float) mouseX > (float)(this.x + this.width - 17) * this.scale && (float) mouseX < (float)(this.x + this.width - 2) * this.scale && (float) mouseY > (float)(this.y + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + this.height + this.yOffset) * this.scale;
            GL11.glColor4f(bl ? 1.4181818f * 0.5641026f : 0.96875f * 0.2580645f, bl ? 0.0f : 0.17553192f * 1.4242424f, bl ? 0.0f : 15.250001f * 0.016393442f, 0.44444445f * 1.4625f);
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.deleteIcon, (float)5, (float)(this.x + this.width - 13), (float)this.y + 0.7653061f * 4.5733333f);
        }
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
        boolean bl = (float) mouseX > (float)(this.x + this.width - 17) * this.scale && (float) mouseX < (float)(this.x + this.width - 2) * this.scale && (float) mouseY > (float)(this.y + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + this.height + this.yOffset) * this.scale;
        boolean bl2 = (float) mouseX > (float)(this.x + this.width - 30) * this.scale && (float) mouseX < (float)(this.x + this.width - 13) * this.scale && (float) mouseY > (float)(this.y + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + this.height + this.yOffset) * this.scale;
        boolean bl3 = (float) mouseX > (float)this.x * this.scale && (float) mouseX < (float)(this.x + 12) * this.scale && (float) mouseY >= (float)(this.y + this.yOffset) * this.scale && (float) mouseY <= (float)(this.y + this.height / 2 + this.yOffset) * this.scale;
        boolean bl4 = (float) mouseX > (float)this.x * this.scale && (float) mouseX < (float)(this.x + 12) * this.scale && (float) mouseY > (float)(this.y + this.height / 2 + this.yOffset) * this.scale && (float) mouseY < (float)(this.y + this.height + this.yOffset) * this.scale;
        ProfilesListElement iIlIlIlllIllIIlIllIIlIIlI = (ProfilesListElement)this.parent;
        if (this.profile.isEditable() && (bl3 || bl4)) {
            if (bl3 && ((ProfilesListElement)this.parent).lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this) != 0 && ((ProfilesListElement)this.parent).lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this) > 1) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                this.profile.setIndex(iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this) - 1);
                iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.get((int)(iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf((Object)this) - 1)).profile.setIndex(iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this));
                Collections.swap(iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI, iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this), iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this) - 1);
            }
            if (bl4 && iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this) != iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.size() - 1) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                this.profile.setIndex(iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this) + 1);
                iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.get((int)(iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf((Object)this) + 1)).profile.setIndex(iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this));
                Collections.swap(iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI, iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this), iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.indexOf(this) + 1);
            }
        } else if (this.profile.isEditable() && bl) {
            File file;
            File file2;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            if (CheatBreaker.getInstance().activeProfile == this.profile) {
                CheatBreaker.getInstance().activeProfile = CheatBreaker.getInstance().profiles.get(0);
                CheatBreaker.getInstance().configManager.readProfile(CheatBreaker.getInstance().activeProfile.getName());
                CheatBreaker.getInstance().moduleManager.keyStrokes.initialize();
            }
            if (this.profile.isEditable() && (file2 = (file = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "config" + File.separator + "client" + File.separator + "profiles")).exists() || file.mkdirs() ? new File(file + File.separator + this.profile.getName().toLowerCase() + ".cfg") : null).exists() && file2.delete()) {
                CheatBreaker.getInstance().profiles.removeIf(ilIIlIIlIIlllIlIIIlIllIIl -> ilIIlIIlIIlllIlIIIlIllIIl == this.profile);
                iIlIlIlllIllIIlIllIIlIIlI.lIIIIlIIllIIlIIlIIIlIIllI.removeIf(iIlIlllllIIIlIIllIllIlIlI -> iIlIlllllIIIlIIllIllIlIlI == this);
            }
        } else if (this.profile.isEditable() && bl2) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            Minecraft.getMinecraft().displayGuiScreen(new CBProfileCreateGui(this.profile, CBModulesGui.instance, (ProfilesListElement)this.parent, this.IllIIIIIIIlIlIllllIIllIII, this.scale));
        } else if (CheatBreaker.getInstance().activeProfile != this.profile) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            CheatBreaker.getInstance().configManager.writeProfile(CheatBreaker.getInstance().activeProfile.getName());
            CheatBreaker.getInstance().activeProfile = this.profile;
            CheatBreaker.getInstance().configManager.readProfile(CheatBreaker.getInstance().activeProfile.getName());
            CheatBreaker.getInstance().moduleManager.keyStrokes.initialize();
        }
    }
}