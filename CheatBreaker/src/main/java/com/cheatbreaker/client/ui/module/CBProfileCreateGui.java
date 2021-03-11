package com.cheatbreaker.client.ui.module;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Profile;
import com.cheatbreaker.client.ui.element.profile.ProfileElement;
import com.cheatbreaker.client.ui.element.profile.ProfilesListElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.nio.file.Files;

public class CBProfileCreateGui
        extends GuiScreen {
    private final GuiScreen guiScreen;
    private final float IlllIIIlIlllIllIlIIlllIlI;
    private final int IIIIllIlIIIllIlllIlllllIl;
    private final ProfilesListElement parent;
    private GuiTextField lIIIIIIIIIlIllIIllIlIIlIl = null;
    private String IlIlIIIlllIIIlIlllIlIllIl = "";
    private boolean IIIllIllIlIlllllllIlIlIII = false;
    private Profile profile;

    public CBProfileCreateGui(Profile profile, GuiScreen guiScreen, ProfilesListElement parent, int n, float f) {
        this(guiScreen, parent, n, f);
        this.profile = profile;
    }

    public CBProfileCreateGui(GuiScreen guiScreen, ProfilesListElement parent, int n, float f) {
        this.guiScreen = guiScreen;
        this.IlllIIIlIlllIllIlIIlllIlI = f;
        this.parent = parent;
        this.IIIIllIlIIIllIlllIlllllIl = n;
        this.IIIllIllIlIlllllllIlIlIII = true;
    }

    @Override
    public void updateScreen() {
        this.lIIIIIIIIIlIllIIllIlIIlIl.updateCursorCounter();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        if (!this.IIIllIllIlIlllllllIlIlIII) {
            this.mc.displayGuiScreen(this.guiScreen);
            ((CBModulesGui) this.guiScreen).currentScrollableElement = ((CBModulesGui) this.guiScreen).profilesElement;
        } else {
            this.IIIllIllIlIlllllllIlIlIII = false;
            this.lIIIIIIIIIlIllIIllIlIIlIl = new GuiTextField(this.mc.fontRenderer, this.width / 2 - 70, this.height / 2 - 6, 140, 10);
            if (this.profile != null) {
                this.lIIIIIIIIIlIllIIllIlIIlIl.setText(this.profile.getName());
            }
            this.lIIIIIIIIIlIllIIllIlIIlIl.setFocused(true);
        }
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.guiScreen.drawScreen(n, n2, f);
        this.drawDefaultBackground();
        Gui.drawRect(this.width / 2f - 73, this.height / 2f - 19, this.width / 2f + 73, this.height / 2f + 8, -11250604);
        Gui.drawRect(this.width / 2f - 72, this.height / 2f - 18, this.width / 2f + 72, this.height / 2f + 7, -3881788);
        GL11.glPushMatrix();
        GL11.glScalef(this.IlllIIIlIlllIllIlIIlllIlI, this.IlllIIIlIlllIllIlIIlllIlI, this.IlllIIIlIlllIllIlIIlllIlI);
        int n3 = (int) ((float) this.width / this.IlllIIIlIlllIllIlIIlllIlI);
        int n4 = (int) ((float) this.height / this.IlllIIIlIlllIllIlIIlllIlI);
        CheatBreaker.getInstance().ubuntuMedium16px.drawString("Profile Name: ", (float) (n3 / 2) - (float) 70 / this.IlllIIIlIlllIllIlIIlllIlI, (float) (n4 / 2) - (float) 17 / this.IlllIIIlIlllIllIlIIlllIlI, 0x6F000000);
        CheatBreaker.getInstance().ubuntuMedium16px.drawString(this.IlIlIIIlllIIIlIlllIlIllIl, (float) (n3 / 2) - (float) 72 / this.IlllIIIlIlllIllIlIIlllIlI, (float) (n4 / 2) + (float) 8 / this.IlllIIIlIlllIllIlIIlllIlI, -1358954496);
        GL11.glPopMatrix();
        this.lIIIIIIIIIlIllIIllIlIIlIl.drawTextBox();
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        super.mouseClicked(n, n2, n3);
        this.lIIIIIIIIIlIllIIllIlIIlIl.mouseClicked(n, n2, n3);
    }

    @Override
    public void mouseMovedOrUp(int n, int n2, int n3) {
        super.mouseMovedOrUp(n, n2, n3);
    }

    @Override
    public void keyTyped(char c, int n) {
        switch (n) {
            case 1: {
                this.mc.displayGuiScreen(this.guiScreen);
                ((CBModulesGui) this.guiScreen).currentScrollableElement = ((CBModulesGui) this.guiScreen).profilesElement;
                break;
            }
            case 28: {
                if (this.lIIIIIIIIIlIllIIllIlIIlIl.getText().length() < 3) {
                    this.IlIlIIIlllIIIlIlllIlIllIl = EnumChatFormatting.RED + "Name must be at least 3 characters long.";
                    break;
                }
                if (this.lIIIIIIIIIlIllIIllIlIIlIl.getText().equalsIgnoreCase("default")) {
                    this.IlIlIIIlllIIIlIlllIlIllIl = EnumChatFormatting.RED + "That name is already in use.";
                    break;
                }
                if (!this.lIIIIIIIIIlIllIIllIlIIlIl.getText().matches("([a-zA-Z0-9-_ \\]\\[]+)")) {
                    this.IlIlIIIlllIIIlIlllIlIllIl = EnumChatFormatting.RED + "Illegal characters in name.";
                    break;
                }
                if (this.profile != null && this.profile.isEditable()) {
                    File file = new File(Minecraft.getMinecraft().mcDataDir, "config" + File.separator + "client" + File.separator + "profiles" + File.separator + this.profile.getName() + ".cfg");
                    File file2 = new File(Minecraft.getMinecraft().mcDataDir, "config" + File.separator + "client" + File.separator + "profiles" + File.separator + this.lIIIIIIIIIlIllIIllIlIIlIl.getText() + ".cfg");
                    if (!file.exists()) break;
                    try {
                        Files.copy(file.toPath(), file2.toPath());
                        Files.delete(file.toPath());
                        this.profile.setName(this.lIIIIIIIIIlIllIIllIlIIlIl.getText());
                        this.mc.displayGuiScreen(this.guiScreen);
                        ((CBModulesGui) this.guiScreen).currentScrollableElement = ((CBModulesGui) this.guiScreen).profilesElement;
                    } catch (Exception exception) {
                        this.IlIlIIIlllIIIlIlllIlIllIl = EnumChatFormatting.RED + "Could not save profile.";
                        exception.printStackTrace();
                    }
                    break;
                }
                Profile ilIIlIIlIIlllIlIIIlIllIIl = null;
                for (Profile ilIIlIIlIIlllIlIIIlIllIIl2 : CheatBreaker.getInstance().profiles) {
                    if (!ilIIlIIlIIlllIlIIIlIllIIl2.getName().toLowerCase().equalsIgnoreCase(this.lIIIIIIIIIlIllIIllIlIIlIl.getText()))
                        continue;
                    ilIIlIIlIIlllIlIIIlIllIIl = ilIIlIIlIIlllIlIIIlIllIIl2;
                    break;
                }
                if (ilIIlIIlIIlllIlIIIlIllIIl == null) {
                    CheatBreaker.getInstance().configManager.writeProfile(CheatBreaker.getInstance().activeProfile.getName());
                    Profile ilIIlIIlIIlllIlIIIlIllIIl3 = new Profile(this.lIIIIIIIIIlIllIIllIlIIlIl.getText(), true);
                    CheatBreaker.getInstance().profiles.add(ilIIlIIlIIlllIlIIIlIllIIl3);
                    CheatBreaker.getInstance().activeProfile = ilIIlIIlIIlllIlIIIlIllIIl3;
                    this.parent.lIIIIlIIllIIlIIlIIIlIIllI.add(new ProfileElement(this.parent, this.IIIIllIlIIIllIlllIlllllIl, ilIIlIIlIIlllIlIIIlIllIIl3, this.IlllIIIlIlllIllIlIIlllIlI));
                    CheatBreaker.getInstance().configManager.writeProfile(CheatBreaker.getInstance().activeProfile.getName());
                    this.mc.displayGuiScreen(this.guiScreen);
                    ((CBModulesGui) this.guiScreen).currentScrollableElement = ((CBModulesGui) this.guiScreen).profilesElement;
                    break;
                }
                this.IlIlIIIlllIIIlIlllIlIllIl = EnumChatFormatting.RED + "That name is already in use.";
                break;
            }
            default: {
                this.lIIIIIIIIIlIllIIllIlIIlIl.textboxKeyTyped(c, n);
            }
        }
    }
}
