package com.cheatbreaker.client.ui.element.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Profile;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.element.AbstractModulesGuiElement;
import com.cheatbreaker.client.ui.element.AbstractScrollableElement;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.module.CBProfileCreateGui;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ProfilesListElement extends AbstractScrollableElement {
    private final int IIIlllIIIllIllIlIIIIIIlII;
    public final List<ProfileElement> lIIIIlIIllIIlIIlIIIlIIllI;
    private final ResourceLocation plusIcon = new ResourceLocation("client/icons/plus-64.png");

    public ProfilesListElement(float f, int n, int n2, int n3, int n4) {
        super(f, n, n2, n3, n4);
        this.IIIlllIIIllIllIlIIIIIIlII = -12418828;
        this.lIIIIlIIllIIlIIlIIIlIIllI = new ArrayList<>();
        this.lIIIIIIIIIlIllIIllIlIIlIl();
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        Object object;
        int n3;
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((double)this.x, (double)this.y, (double)(this.x + this.width), (double)(this.y + this.height + 2), (double)8, -657931);
        this.preDraw(mouseX, mouseY);
        this.IlllIllIlIIIIlIIlIIllIIIl = 15;
        for (n3 = 0; n3 < this.lIIIIlIIllIIlIIlIIIlIIllI.size(); ++n3) {
            object = this.lIIIIlIIllIIlIIlIIIlIIllI.get(n3);
            ((AbstractModulesGuiElement)object).setDimensions(this.x + 4, this.y + 4 + n3 * 18, this.width - 12, 18);
            ((ProfileElement)object).yOffset = this.lIIIIllIIlIlIllIIIlIllIlI;
            ((ProfileElement)object).handleDrawElement(mouseX, mouseY, partialTicks);
            this.IlllIllIlIIIIlIIlIIllIIIl += ((AbstractModulesGuiElement)object).getHeight();
        }
        n3 = (float) mouseX > (float)(this.x + this.width - 92) * this.scale && (float) mouseX < (float)(this.x + this.width - 6) * this.scale && (float) mouseY > (float)(this.y + this.IlllIllIlIIIIlIIlIIllIIIl - 10 + this.lIIIIllIIlIlIllIIIlIllIlI) * this.scale && (float) mouseY < (float)(this.y + this.IlllIllIlIIIIlIIlIIllIIIl + 3 + this.lIIIIllIIlIlIllIIIlIllIlI) * this.scale ? 1 : 0;
        GL11.glColor4f(n3 != 0 ? 0.0f : 0.22590362f * 1.1066667f, n3 != 0 ? 1.4117647f * 0.56666666f : 1.3333334f * 0.1875f, n3 != 0 ? 0.0f : 0.14423077f * 1.7333333f, 1.7058823f * 0.38103446f);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.plusIcon, 3.4435484f * 1.0163934f, (float)(this.x + this.width - 15), (float)(this.y + this.IlllIllIlIIIIlIIlIIllIIIl) - 0.6506024f * 9.990741f);
        object = (n3 != 0 ? "(COPIES CURRENT PROFILE) " : "") + "ADD NEW PROFILE";
        CheatBreaker.getInstance().ubuntuMedium16px.drawString((String)object, this.x + this.width - 17 - CheatBreaker.getInstance().ubuntuMedium16px.getStringWidth((String)object), (float)(this.y + this.IlllIllIlIIIIlIIlIIllIIIl) - 64.28571f * 0.11666667f, n3 != 0 ? 0x7F007F00 : 0x7F000000);
        this.IlllIllIlIIIIlIIlIIllIIIl += 10;
        this.postDraw(mouseX, mouseY);
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
        for (ProfileElement iIlIlllllIIIlIIllIllIlIlI : this.lIIIIlIIllIIlIIlIIIlIIllI) {
            if (!iIlIlllllIIIlIIllIllIlIlI.isMouseInside(mouseX, mouseY)) continue;
            iIlIlllllIIIlIIllIllIlIlI.handleMouseClick(mouseX, mouseY, button);
            return;
        }
        boolean bl = (float) mouseX > (float) (this.x + this.width - 92) * this.scale && (float) mouseX < (float) (this.x + this.width - 6) * this.scale && (float) mouseY > (float) (this.y + this.IlllIllIlIIIIlIIlIIllIIIl - 20 + this.lIIIIllIIlIlIllIIIlIllIlI) * this.scale && (float) mouseY < (float) (this.y + this.IlllIllIlIIIIlIIlIIllIIIl - 7 + this.lIIIIllIIlIlIllIIIlIllIlI) * this.scale;
        if (bl) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            Minecraft.getMinecraft().displayGuiScreen(new CBProfileCreateGui(CBModulesGui.instance, this, this.IIIlllIIIllIllIlIIIIIIlII, this.scale));
        }
    }

    @Override
    public boolean lIIIIlIIllIIlIIlIIIlIIllI(AbstractModule cBModule) {
        return true;
    }

    @Override
    public void lIIIIIIIIIlIllIIllIlIIlIl(AbstractModule cBModule) {
    }

    public void lIIIIIIIIIlIllIIllIlIIlIl() {
        new Thread(() -> {
            this.lIIIIlIIllIIlIIlIIIlIIllI.clear();
            File file = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "config" + File.separator + "client" + File.separator + "profiles");
            if (file.exists()) {
                for (File file2 : file.listFiles()) {
                    if (!file2.getName().endsWith(".cfg")) continue;
                    Profile ilIIlIIlIIlllIlIIIlIllIIl = null;
                    for (Profile ilIIlIIlIIlllIlIIIlIllIIl2 : CheatBreaker.getInstance().profiles) {
                        if (!file2.getName().equals(ilIIlIIlIIlllIlIIIlIllIIl2.getName() + ".cfg")) continue;
                        ilIIlIIlIIlllIlIIIlIllIIl = ilIIlIIlIIlllIlIIIlIllIIl2;
                    }
                    if (ilIIlIIlIIlllIlIIIlIllIIl != null) continue;
                    CheatBreaker.getInstance().profiles.add(new Profile(file2.getName().replace(".cfg", ""), false));
                }
            }
            for (Profile ilIIlIIlIIlllIlIIIlIllIIl : CheatBreaker.getInstance().profiles) {
                this.lIIIIlIIllIIlIIlIIIlIIllI.add(new ProfileElement(this, this.IIIlllIIIllIllIlIIIIIIlII, ilIIlIIlIIlllIlIIIlIllIIl, this.scale));
            }
            this.lIIIIlIIllIIlIIlIIIlIIllI.sort((iIlIlllllIIIlIIllIllIlIlI, iIlIlllllIIIlIIllIllIlIlI2) -> {
                if (iIlIlllllIIIlIIllIllIlIlI.profile.getName().equalsIgnoreCase("default")) {
                    return 0;
                }
                if (iIlIlllllIIIlIIllIllIlIlI.profile.getIndex() < iIlIlllllIIIlIIllIllIlIlI2.profile.getIndex()) {
                    return -1;
                }
                return 1;
            });
        }).start();
    }
}