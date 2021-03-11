package com.cheatbreaker.client.ui.element.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.module.staff.StaffModule;
import com.cheatbreaker.client.ui.element.*;
import com.cheatbreaker.client.ui.element.type.*;
import com.cheatbreaker.client.ui.element.type.custom.*;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class ModuleListElement extends AbstractScrollableElement {
    private final int colour;
    protected final List<ModuleSettingsElement> moduleSettingsElementList;
    private final boolean lIIIIIllllIIIIlIlIIIIlIlI;
    private final GlobalSettingsElement globalSettingsElement;
    public AbstractScrollableElement scrollable;
    public boolean llIlIIIlIIIIlIlllIlIIIIll = false;
    public AbstractModule module;
    private final ModulesGuiButtonElement backButton;
    private final ModulesGuiButtonElement applyToAllTextButton;
    private final Map<AbstractModule, List<AbstractModulesGuiElement>> moduleElementListMap;
    private final List<AbstractModulesGuiElement> elementsList;

    public ModuleListElement(List<AbstractModule> list, float f, int n, int n2, int n3, int n4) {
        super(f, n, n2, n3, n4);
        this.lIIIIIllllIIIIlIlIIIIlIlI = list == CheatBreaker.getInstance().moduleManager.staffModules;
        this.colour = -12418828;
        this.globalSettingsElement = new GlobalSettingsElement(this, this.colour, f);
        this.moduleSettingsElementList = new ArrayList<>();
        for (Object object : list) {
            if (((AbstractModule)object).isStaffModule() && !((AbstractModule)object).isStaffEnabledModule()) continue;
            this.moduleSettingsElementList.add(new ModuleSettingsElement(this, this.colour, (AbstractModule)object, f));
        }
        this.backButton = new ModulesGuiButtonElement(null, "arrow-64.png", this.x + 2, this.y + 4, 28, 28, -12418828, f);
        this.module = null;
        this.moduleElementListMap = new HashMap<>();
        for (AbstractModule object : list) {
            if (object.isStaffModule() && !object.isStaffEnabledModule() || object == CheatBreaker.getInstance().moduleManager.minmap) continue;
            ArrayList<AbstractModulesGuiElement> object2 = new ArrayList<>();
            for (Setting cBSetting : object.getSettingsList()) {
                switch (cBSetting.getType()) {
                    case BOOLEAN: {
                        object2.add(new ToggleElement(cBSetting, f));
                        break;
                    }
                    case DOUBLE:
                    case INTEGER:
                    case FLOAT: {
                        if (object.isStaffModule() && cBSetting == ((StaffModule)object).getKeybindSetting() || object.isStaffModule() && cBSetting == object.scale) break;
                        if (cBSetting.getType().equals(Setting.Type.INTEGER) && cBSetting.getLabel().toLowerCase().contains("color")) {
                            object2.add(new ColorPickerElement(cBSetting, f));
                            break;
                        }
                        object2.add(new SliderElement(cBSetting, f));
                        break;
                    }
                    case STRING_ARRAY: {
                        object2.add(new ChoiceElement(cBSetting, f));
                        break;
                    }
                    case STRING: {
                        if (!cBSetting.getLabel().equalsIgnoreCase("label")) break;
                        object2.add(new LabelElement(cBSetting, f));
                    }
                }
            }
            if (object.isStaffModule()) {
                object2.add(new KeybindElement(((StaffModule)object).getKeybindSetting(), f));
                if (object == CheatBreaker.getInstance().moduleManager.xray) {
                    object2.add(new XRayOptionsElement(CheatBreaker.getInstance().moduleManager.xray.lIllIllIlIIllIllIlIlIIlIl(), "Blocks", f));
                }
            }
            this.moduleElementListMap.put(object, object2);
        }
        this.elementsList = new ArrayList<>();
        for (Setting object : CheatBreaker.getInstance().globalSettings.settingsList) {
            switch (object.getType()) {
                case BOOLEAN: {
                    if (object == CheatBreaker.getInstance().globalSettings.clearGlass) continue;
                    this.elementsList.add(new ToggleElement(object, f));
                    break;
                }
                case DOUBLE:
                case INTEGER:
                case FLOAT: {
                    if (object.getType().equals(Setting.Type.INTEGER) && object.getLabel().toLowerCase().contains("color")) {
                        this.elementsList.add(new ColorPickerElement(object, f));
                        break;
                    }
                    if (object.getLabel().equals("World Time")) {
                        this.elementsList.add(new WorldTimeElement(object, f));
                        break;
                    }
                    this.elementsList.add(new SliderElement(object, f));
                    break;
                }
                case STRING_ARRAY: {
                    if (object == CheatBreaker.getInstance().globalSettings.clearGlass) continue;
                    this.elementsList.add(new ChoiceElement(object, f));
                    break;
                }
                case STRING: {
                    if (!object.getLabel().equalsIgnoreCase("label")) break;
                    this.elementsList.add(new LabelElement(object, f));
                    if (!CheatBreaker.getInstance().globalSettings.IIIIllIlIIIllIlllIlllllIl().getValue().equals(object.getValue())) break;
                    this.elementsList.add(new CrosshairElement(f));
                }
            }
        }
        int n5 = 25;
        for (Object object2 : this.elementsList) {
            n5 += ((AbstractModulesGuiElement)object2).getHeight();
        }
        this.applyToAllTextButton = new ModulesGuiButtonElement(CheatBreaker.getInstance().playBold18px, null, "Apply to all text", this.x + n3 - 120, this.y + n5 + 4, 110, 28, -12418828, f);
    }

    @Override
    public void handleDrawElement(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.x, this.y, this.x + this.width, this.y + this.height + 2, (double)8, -657931);
        this.preDraw(mouseX, mouseY);
        if (this.module == null && !this.llIlIIIlIIIIlIlllIlIIIIll) {
            this.IlllIllIlIIIIlIIlIIllIIIl = 52;
            if (!this.lIIIIIllllIIIIlIlIIIIlIlI) {
                this.globalSettingsElement.setDimensions(this.x + 4, this.y + 4, this.width - 12, 18);
                this.globalSettingsElement.yOffset = this.lIIIIllIIlIlIllIIIlIllIlI;
                this.globalSettingsElement.handleDrawElement(mouseX, mouseY, partialTicks);
            }
            for (int i = 0; i < this.moduleSettingsElementList.size(); ++i) {
                ModuleSettingsElement moduleSettingsElement = this.moduleSettingsElementList.get(i);
                moduleSettingsElement.setDimensions(this.x + 4, this.y + (this.lIIIIIllllIIIIlIlIIIIlIlI ? 4 : 24) + i * 20, this.width - 12, 18);
                moduleSettingsElement.yOffset = this.lIIIIllIIlIlIllIIIlIllIlI;
                moduleSettingsElement.handleDrawElement(mouseX, mouseY, partialTicks);
                this.IlllIllIlIIIIlIIlIIllIIIl += moduleSettingsElement.getHeight();
            }
        } else if (this.llIlIIIlIIIIlIlllIlIIIIll && !this.lIIIIIllllIIIIlIlIIIIlIlI) {
            Gui.drawRect(this.x + 32, this.y + 4, this.x + 33, this.y + this.height - 4, 0x2F2F2F2F);
            this.IlllIllIlIIIIlIIlIIllIIIl = 25;
            this.backButton.setDimensions(this.x + 2, this.y + 2, 28, 28);
            this.backButton.yOffset = this.lIIIIllIIlIlIllIIIlIllIlI;
            this.backButton.handleDrawElement(mouseX, mouseY, partialTicks);
            CheatBreaker.getInstance().ubuntuMedium16px.drawString("CheatBreaker Settings".toUpperCase(), this.x + 38, (float)(this.y + 9), -1358954496);
            Gui.drawRect(this.x + 38, this.y + 17, this.x + this.width - 6, this.y + 18, 0x2F2F2F2F);
            int n3 = 0;
            for (AbstractModulesGuiElement abstractElement : this.elementsList) {
                abstractElement.setDimensions(this.x + 38, this.y + 22 + n3, this.width - 40, abstractElement.getHeight());
                abstractElement.yOffset = this.lIIIIllIIlIlIllIIIlIllIlI;
                abstractElement.handleDrawElement(mouseX, mouseY, partialTicks);
                n3 += 2 + abstractElement.getHeight();
                this.IlllIllIlIIIIlIIlIIllIIIl += 2 + abstractElement.getHeight();
            }
            this.applyToAllTextButton.yOffset = this.lIIIIllIIlIlIllIIIlIllIlI;
            this.applyToAllTextButton.setDimensions(this.x + this.width - 118, this.y + this.IlllIllIlIIIIlIIlIIllIIIl, 100, 20);
            this.applyToAllTextButton.handleDrawElement(mouseX, mouseY, partialTicks);
            this.IlllIllIlIIIIlIIlIIllIIIl += 24;
        } else {
            Gui.drawRect(this.x + 32, this.y + 4, this.x + 33, this.y + (Math.max(this.height, this.IlllIllIlIIIIlIIlIIllIIIl)) - 4, 0x2F2F2F2F);
            this.IlllIllIlIIIIlIIlIIllIIIl = 37;
            this.backButton.setDimensions(this.x + 2, this.y + 2, 28, 28);
            this.backButton.yOffset = this.lIIIIllIIlIlIllIIIlIllIlI;
            this.backButton.handleDrawElement(mouseX, mouseY, partialTicks);
            CheatBreaker.getInstance().ubuntuMedium16px.drawString((this.module.getName() + " Settings").toUpperCase(), this.x + 38, (float)(this.y + 6), -1358954496);
            Gui.drawRect(this.x + 38, this.y + 17, this.x + this.width - 12, this.y + 18, 0x2F2F2F2F);
            if (this.module == CheatBreaker.getInstance().moduleManager.minmap) {
                try {
                    String string = Keyboard.getKeyName(CheatBreaker.getInstance().moduleManager.minmap.getVoxelMap().getMapOptions().keyBindMenu.getKeyCode());
                    CheatBreaker.getInstance().ubuntuMedium16px.drawString(("PRESS '" + string + "' INGAME FOR ZAN'S MINIMAP OPTIONS.").toUpperCase(), this.x + 38, (float)(this.y + 22), -1895825408);
                }
                catch (Exception exception) {
                    CheatBreaker.getInstance().ubuntuMedium16px.drawString("PRESS 'M' INGAME FOR ZAN'S MINIMAP OPTIONS.".toUpperCase(), this.x + 38, (float)(this.y + 22), -1895825408);
                }
                this.postDraw(mouseX, mouseY);
                return;
            }
            if (this.module.getSettingsList().isEmpty()) {
                CheatBreaker.getInstance().ubuntuMedium16px.drawString((this.module.getName().toUpperCase() + " DOES NOT HAVE ANY OPTIONS.").toUpperCase(), this.x + 38, (float)(this.y + 22), -1895825408);
            }
            int n4 = 0;
            // fixed gay zans minimap crash shid
            if (this.module != null && moduleElementListMap.containsKey(this.module)) {
                for (AbstractModulesGuiElement abstractElement : this.moduleElementListMap.get(this.module)) {
                    abstractElement.setDimensions(this.x + 38, this.y + 22 + n4, this.width - 40, abstractElement.getHeight());
                    abstractElement.yOffset = this.lIIIIllIIlIlIllIIIlIllIlI;
                    abstractElement.handleDrawElement(mouseX, mouseY, partialTicks);
                    n4 += 2 + abstractElement.getHeight();
                    this.IlllIllIlIIIIlIIlIIllIIIl += 2 + abstractElement.getHeight();
                }
            }
        }
        this.postDraw(mouseX, mouseY);
    }

    @Override
    public void handleMouseClick(int mouseX, int mouseY, int button) {
        block13: {
            block14: {
                block12: {
                    if (this.module != null || this.llIlIIIlIIIIlIlllIlIIIIll) break block12;
                    if (this.globalSettingsElement.isMouseInside(mouseX, mouseY) && !this.lIIIIIllllIIIIlIlIIIIlIlI) {
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                        this.llIlIIIlIIIIlIlllIlIIIIll = true;
                        this.lIIIIllIIlIlIllIIIlIllIlI = 0;
                        this.IllIIIIIIIlIlIllllIIllIII = 0.0;
                        this.yOffset = 0;
                    } else {
                        for (ModuleSettingsElement moduleSettingsElement : this.moduleSettingsElementList) {
                            if (!moduleSettingsElement.isMouseInside(mouseX, mouseY) || !this.lIIIIlIIllIIlIIlIIIlIIllI(moduleSettingsElement.module)) continue;
                            moduleSettingsElement.handleMouseClick(mouseX, mouseY, button);
                        }
                    }
                    break block13;
                }
                if (!this.backButton.isMouseInside(mouseX, mouseY)) break block14;
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                this.module = null;
                this.llIlIIIlIIIIlIlllIlIIIIll = false;
                if (this.scrollable == null) break block13;
                CBModulesGui.instance.currentScrollableElement = this.scrollable;
                break block13;
            }
            if (this.module != null && moduleElementListMap.containsKey(this.module)) {
                for (AbstractModulesGuiElement abstractElement : this.moduleElementListMap.get(this.module)) {
                    if (!abstractElement.isMouseInside(mouseX, mouseY)) continue;
                    abstractElement.handleMouseClick(mouseX, mouseY, button);
                }
            } else if (this.llIlIIIlIIIIlIlllIlIIIIll) {
                if (this.applyToAllTextButton.isMouseInside(mouseX, mouseY)) {
                    for (AbstractModule cBModule : CheatBreaker.getInstance().moduleManager.modules) {
                        for (Setting cBSetting : cBModule.getSettingsList()) {
                            if (cBSetting.getType() != Setting.Type.INTEGER || !cBSetting.getLabel().toLowerCase().contains("color") || cBSetting.getLabel().toLowerCase().contains("background")) continue;
                            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                            cBSetting.setValue(CheatBreaker.getInstance().globalSettings.defaultColor.getValue());
                        }
                    }
                } else {
                    for (AbstractModulesGuiElement abstractElement : this.elementsList) {
                        if (!abstractElement.isMouseInside(mouseX, mouseY)) continue;
                        abstractElement.handleMouseClick(mouseX, mouseY, button);
                    }
                }
            }
        }
    }

    @Override
    public boolean lIIIIlIIllIIlIIlIIIlIIllI(AbstractModule cBModule) {
        return !cBModule.getSettingsList().isEmpty() || cBModule.getName().contains("Zans");
    }

    @Override
    public void lIIIIIIIIIlIllIIllIlIIlIl(AbstractModule cBModule) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
        this.lIIIIllIIlIlIllIIIlIllIlI = 0;
        this.IllIIIIIIIlIlIllllIIllIII = 0.0;
        this.yOffset = 0;
        this.module = cBModule;
        this.scrollable = null;
    }
}
