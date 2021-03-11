package com.cheatbreaker.client.ui.module;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.element.*;
import com.cheatbreaker.client.ui.element.module.ModulesGuiButtonElement;
import com.cheatbreaker.client.ui.element.module.ModuleListElement;
import com.cheatbreaker.client.ui.element.module.ModulePreviewContainer;
import com.cheatbreaker.client.ui.element.profile.ProfilesListElement;
import com.cheatbreaker.client.ui.util.RenderUtil;
import com.cheatbreaker.client.ui.util.font.CBFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CBModulesGui extends GuiScreen {

    public static CBModulesGui instance;
    private final ResourceLocation cogIcon = new ResourceLocation("client/icons/cog-64.png");
    private final ResourceLocation deleteIcon = new ResourceLocation("client/icons/delete-64.png");
    private final List<CBModulePosition> positions = new ArrayList<>();
    private final List<AbstractScrollableElement> IllIIlIIlllllIllIIIlllIII = new ArrayList();
    private final List<ModulesGuiButtonElement> buttons = new ArrayList<>();
    private List<AbstractModule> modules;
    private ModulesGuiButtonElement showGuidesButton;
    public ModulesGuiButtonElement helpButton;
    public AbstractScrollableElement IIIIllIIllIIIIllIllIIIlIl;
    public AbstractScrollableElement profilesElement;
    protected AbstractScrollableElement modulesElement;
    protected AbstractScrollableElement staffModulesElement;
    protected AbstractScrollableElement lIIIIllIIlIlIllIIIlIllIlI = null;
    public AbstractScrollableElement currentScrollableElement = null;
    private static AbstractModule draggingModule;
    private boolean IlIlIIIlllllIIIlIlIlIllII = false;
    private float IIlIIllIIIllllIIlllIllIIl;
    private float lllIlIIllllIIIIlIllIlIIII;
    private List<ModuleActionData> undoList;
    private List<ModuleActionData> redo;
    private int someMouseX;
    private int someMouseY;
    private boolean IlIIIIllIIIIIlllIIlIIlllI = false;
    private ModuleDataHolder dataHolder;
    public static boolean IlIlllIIIIllIllllIllIIlIl;
    private int arrowKeyMoves;
    private int IIllIlIllIlIllIIlIllIlIII = 0;

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        //this.mc.entityRenderer.deactivateShader();
    }

    @Override
    public void initGui() {
        //this.IllIllIIIlIIlllIIIllIllII(); shader shid
        Keyboard.enableRepeatEvents(true);
        this.modules = new ArrayList<>();
        this.modules.addAll(CheatBreaker.getInstance().moduleManager.modules);
        this.undoList = new ArrayList<>();
        this.redo = new ArrayList<>();
        this.someMouseX = -1;
        this.someMouseY = -1;
        this.arrowKeyMoves = 0;
        instance = this;
        draggingModule = null;
        IlIlllIIIIllIllllIllIIlIl = false;
        this.lIIIIllIIlIlIllIIIlIllIlI = null;
        this.currentScrollableElement = null;
        this.dataHolder = null;
        IlIlllIIIIllIllllIllIIlIl = false;
        float f = 1.0f / CheatBreaker.getScaleFactor();
        int n = (int)((float)this.width / f);
        int n2 = (int)((float)this.height / f);
        this.IllIIlIIlllllIllIIIlllIII.clear();
        this.buttons.clear();
        List<AbstractModule> modules = CheatBreaker.getInstance().moduleManager.modules;
        List<AbstractModule> staffModules = CheatBreaker.getInstance().moduleManager.staffModules;
        this.modulesElement = new ModulePreviewContainer(f, n / 2 - 565, n2 / 2 + 14, 370, n2 / 2 - 35);
        this.IllIIlIIlllllIllIIIlllIII.add(this.modulesElement);
        this.staffModulesElement = new ModuleListElement(staffModules, f, n / 2 + 195, n2 / 2 + 14, 370, n2 / 2 - 35);
        this.IllIIlIIlllllIllIIIlllIII.add(this.staffModulesElement);
        this.IIIIllIIllIIIIllIllIIIlIl = new ModuleListElement(modules, f, n / 2 + 195, n2 / 2 + 14, 370, n2 / 2 - 35);
        this.IllIIlIIlllllIllIIIlllIII.add(this.IIIIllIIllIIIIllIllIIIlIl);
        this.profilesElement = new ProfilesListElement(f, n / 2 - 565, n2 / 2 + 14, 370, n2 / 2 - 35);
        this.IllIIlIIlllllIllIIIlllIII.add(this.profilesElement);
        this.showGuidesButton = new ModulesGuiButtonElement(null, "eye-64.png", 4, n2 - 32, 28, 28, -12418828, f);
        this.helpButton = new ModulesGuiButtonElement(null, "?", 36, n2 - 32, 28, 28, -12418828, f);
        if (CheatBreaker.getInstance().isUsingStaffModules()) {
            this.buttons.add(new ModulesGuiButtonElement(this.staffModulesElement, "Staff Mods", n / 2 - 50, n2 / 2 - 44, 100, 20, -9442858, f));
        }
        this.buttons.add(new ModulesGuiButtonElement(this.modulesElement, "Mods", n / 2 - 50, n2 / 2 - 19, 100, 28, -13916106, f));
        this.buttons.add(new ModulesGuiButtonElement(this.IIIIllIIllIIIIllIllIIIlIl, "cog-64.png", n / 2 + 54, n2 / 2 - 19, 28, 28, -12418828, f));
        this.buttons.add(new ModulesGuiButtonElement(this.profilesElement, "profiles-64.png", n / 2 - 82, n2 / 2 - 19, 28, 28, -12418828, f));
        IlIlllIIIIllIllllIllIIlIl = false;
        this.lIIIIllIIlIlIllIIIlIllIlI = null;
        this.IIllIlIllIlIllIIlIllIlIII = 5;
    }

    @Override
    public void updateScreen() {
        float f = 1.0f / CheatBreaker.getScaleFactor();
        int n = (int)((float)this.width / f);
        int n2 = (int)((float)this.height / f);
        this.lIIIIlIIllIIlIIlIIIlIIllI(n);
        if (!this.positions.isEmpty()) {
            boolean leftKey = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
            boolean rightDown = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
            boolean upDown = Keyboard.isKeyDown(Keyboard.KEY_UP);
            boolean downDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
            if (leftKey || rightDown || upDown || downDown) {
                ++this.arrowKeyMoves;
                if (this.arrowKeyMoves > 10) {
                    for (CBModulePosition position : this.positions) {
                        AbstractModule module = position.module;
                        if (module == null) continue;
                        if (leftKey) {
                            module.setTranslations((int)module.getXTranslation() - 1, (int)module.getYTranslation());
                            continue;
                        }
                        if (rightDown) {
                            module.setTranslations((int)module.getXTranslation() + 1, (int)module.getYTranslation());
                            continue;
                        }
                        if (upDown) {
                            module.setTranslations((int)module.getXTranslation(), (int)module.getYTranslation() - 1);
                            continue;
                        }
                        module.setTranslations((int)module.getXTranslation(), (int)module.getYTranslation() + 1);
                    }
                }
            }
        }
        float f2 = IIllIlIllIlIllIIlIllIlIII + getSmoothFloat((float) IIllIlIllIlIllIIlIllIlIII);
        this.IIllIlIllIlIllIIlIllIlIII = (float)this.IIllIlIllIlIllIIlIllIlIII + f2 >= (float)255 ? 255 : (int)((float)this.IIllIlIllIlIllIIlIllIlIII + f2);
    }

    private float getIntersectionFloat(Rectangle rectangle, Rectangle rectangle2) {
        float f = Math.max(Math.abs(rectangle.x - rectangle2.x) - rectangle2.width / 2, 0);
        float f2 = Math.max(Math.abs(rectangle.y - rectangle2.y) - rectangle2.height / 2, 0);
        return f * f + f2 * f2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float f) {
        float f2;
        float f3;
        Rectangle object;
        super.drawScreen(mouseX, mouseY, f);
        ///this.lIIlllIIlIlllllllllIIIIIl(); blur shader
        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        float scale = 1.0f / CheatBreaker.getScaleFactor();
        if (draggingModule != null) {
            if (!Mouse.isButtonDown(1)) {
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(2, 0.0, 2.916666637692187 * 0.8571428656578064, this.height, 0.0, -15599126);
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((float)this.width - 5.0f * 0.5f, 0.0, this.width - 2, this.height, 0.0, -15599126);
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(0.0, 2, this.width, 1.1547619104385376 * 2.164948442965692, 0.0, -15599126);
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(0.0, (float)this.height - 1.3529412f * 2.5869565f, this.width, this.height - 3, 0.0, -15599126);
            }
            this.modules.sort((cBModule, cBModule2) -> {
                if (cBModule == draggingModule || cBModule2 == draggingModule || cBModule.getGuiAnchor() == null || cBModule2.getGuiAnchor() == null) {
                    return 0;
                }
                float[] modulePoints = cBModule.getScaledPoints(scaledResolution, true);
                float[] modulePoints2 = cBModule2.getScaledPoints(scaledResolution, true);
                float[] selectedPoints = draggingModule.getScaledPoints(scaledResolution, true);
                Rectangle rectangle = new Rectangle((int)(modulePoints[0] * (Float) cBModule.scale.getValue()), (int)(modulePoints[1] * ((Float)cBModule.scale.getValue()).floatValue()), (int)(cBModule.width * ((Float)cBModule.scale.getValue()).floatValue()), (int)(cBModule.height * ((Float)cBModule.scale.getValue()).floatValue()));
                Rectangle rectangle2 = new Rectangle((int)(modulePoints2[0] * (Float) cBModule2.scale.getValue()), (int)(modulePoints2[1] * ((Float)cBModule2.scale.getValue()).floatValue()), (int)(cBModule2.width * ((Float)cBModule2.scale.getValue()).floatValue()), (int)(cBModule2.height * ((Float)cBModule2.scale.getValue()).floatValue()));
                Rectangle rectangle3 = new Rectangle((int)(selectedPoints[0] * (Float) draggingModule.scale.getValue()), (int)(selectedPoints[1] * (Float) CBModulesGui.draggingModule.scale.getValue()), (int)(CBModulesGui.draggingModule.width * (Float) CBModulesGui.draggingModule.scale.getValue()), (int)(CBModulesGui.draggingModule.height * (Float) CBModulesGui.draggingModule.scale.getValue()));
                try {
                    if (this.getIntersectionFloat(rectangle, rectangle3) > this.getIntersectionFloat(rectangle2, rectangle3)) {
                        return -1;
                    }
                    return 1;
                }
                catch (Exception exception) {
                    return 0;
                }
            });
            CBModulePosition CBModulePosition = this.getModulePosition(draggingModule);
            if (CBModulePosition != null) {
                this.positions.remove(CBModulePosition);
                this.positions.add(CBModulePosition);
            }
            for (CBModulePosition position : this.positions) {
                this.lIIIIlIIllIIlIIlIIIlIIllI(position, mouseX, mouseY, scaledResolution);
                if (!(Boolean) CheatBreaker.getInstance().globalSettings.snapModules.getValue() || !this.IlIlIIIlllllIIIlIlIlIllII || Mouse.isButtonDown(1) || position.module != draggingModule) continue;
                for (AbstractModule cBModule3 : this.modules) {
                    if (this.getModulePosition(cBModule3) != null || cBModule3.getGuiAnchor() == null || !cBModule3.isEnabled() || cBModule3.getName().contains("Zans") && CheatBreaker.getInstance().moduleManager.minmap.getVoxelMap().getMapOptions().hide) continue;
                    float f5 = 18;
                    if (cBModule3.width < f5) {
                        cBModule3.width = (int)f5;
                    }
                    if (cBModule3.height < (float)18) {
                        cBModule3.height = 18;
                    }
                    if (position.module.width < f5) {
                        position.module.width = (int)f5;
                    }
                    if (position.module.height < (float)18) {
                        position.module.height = 18;
                    }
                    boolean bl = true;
                    boolean bl2 = true;
                    float[] arrf = cBModule3.getScaledPoints(scaledResolution, true);
                    float[] scaledPoints = position.module.getScaledPoints(scaledResolution, true);
                    float f6 = arrf[0] * (Float) cBModule3.scale.getValue() - scaledPoints[0] * (Float) position.module.scale.getValue();
                    float f7 = (arrf[0] + cBModule3.width) * (Float) cBModule3.scale.getValue() - (scaledPoints[0] + position.module.width) * (Float) position.module.scale.getValue();
                    float f8 = (arrf[0] + cBModule3.width) * (Float) cBModule3.scale.getValue() - scaledPoints[0] * (Float) position.module.scale.getValue();
                    float f9 = arrf[0] * (Float) cBModule3.scale.getValue() - (scaledPoints[0] + position.module.width) * (Float) position.module.scale.getValue();
                    float f10 = arrf[1] * (Float) cBModule3.scale.getValue() - scaledPoints[1] * (Float) position.module.scale.getValue();
                    f3 = (arrf[1] + cBModule3.height) * (Float) cBModule3.scale.getValue() - (scaledPoints[1] + position.module.height) * (Float) position.module.scale.getValue();
                    f2 = (arrf[1] + cBModule3.height) * (Float) cBModule3.scale.getValue() - scaledPoints[1] * (Float) position.module.scale.getValue();
                    float f11 = arrf[1] * (Float) cBModule3.scale.getValue() - (scaledPoints[1] + position.module.height) * (Float) position.module.scale.getValue();
                    int n3 = 2;
                    if (f6 >= (float)(-n3) && f6 <= (float)n3) {
                        bl = false;
                        this.snapHorizontally(f6);
                    }
                    if (f7 >= (float)(-n3) && f7 <= (float)n3 && bl) {
                        bl = false;
                        this.snapHorizontally(f7);
                    }
                    if (f9 >= (float)(-n3) && f9 <= (float)n3 && bl) {
                        bl = false;
                        this.snapHorizontally(f9);
                    }
                    if (f8 >= (float)(-n3) && f8 <= (float)n3 && bl) {
                        this.snapHorizontally(f8);
                    }
                    if (f10 >= (float)(-n3) && f10 <= (float)n3) {
                        bl2 = false;
                        this.snapVertically(f10);
                    }
                    if (f3 >= (float)(-n3) && f3 <= (float)n3 && bl2) {
                        bl2 = false;
                        this.snapVertically(f3);
                    }
                    if (f11 >= (float)(-n3) && f11 <= (float)n3 && bl2) {
                        bl2 = false;
                        this.snapVertically(f11);
                    }
                    if (!(f2 >= (float)(-n3)) || !(f2 <= (float)n3) || !bl2) continue;
                    this.snapVertically(f2);
                }
            }
        } else if (this.dataHolder != null) {
            float f12 = 1.0f;
            switch (this.dataHolder.unknown) {
                case RIGHT_BOTTOM: {
                    int n4 = mouseY - this.dataHolder.mouseY + (mouseX - this.dataHolder.mouseX);
                    f12 = this.dataHolder.scale - (float)n4 / (float)115;
                    break;
                }
                case LEFT_TOP: {
                    int n4 = mouseY - this.dataHolder.mouseY + (mouseX - this.dataHolder.mouseX);
                    f12 = this.dataHolder.scale + (float)n4 / (float)115;
                    break;
                }
                case RIGHT_TOP: {
                    int n4 = mouseX - this.dataHolder.mouseX - (mouseY - this.dataHolder.mouseY);
                    f12 = this.dataHolder.scale - (float)n4 / (float)115;
                    break;
                }
                case LEFT_BOTTOM: {
                    int n4 = mouseX - this.dataHolder.mouseX - (mouseY - this.dataHolder.mouseY);
                    f12 = this.dataHolder.scale + (float)n4 / (float)115;
                }
            }
            if (f12 >= 1.0421053f * 0.47979796f && f12 <= 1.8962264f * 0.7910448f) {
                this.dataHolder.module.scale.setValue((float) ((double) Math.round((double) f12 * (double) 100) / (double) 100));
            }
        }
        this.lIIIIlIIllIIlIIlIIIlIIllI(scaledResolution);
        boolean bl = true;
        for (Object object2 : this.modules) {
            boolean bl3 = this.lIIIIlIIllIIlIIlIIIlIIllI(scale, (AbstractModule)object2, scaledResolution, mouseX, mouseY, bl);
            if (bl3) continue;
            bl = false;
        }
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        int n5 = (int)((float)this.width / scale);
        int n6 = (int)((float)this.height / scale);
        this.showGuidesButton.handleDrawElement(mouseX, mouseY, f);
        this.helpButton.handleDrawElement(mouseX, mouseY, f);
        float f13 = (float)(this.IIllIlIllIlIllIIlIllIlIII * 8) / (float)255;
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, f13);
        int n7 = 0xFFFFFF;
        if (f13 / (float)4 > 0.0f && f13 / (float)4 < 1.0f) {
            n7 = new Color(1.0f, 1.0f, 1.0f, f13 / (float)4).getRGB();
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, f13);
        if (f13 > 1.0f) {
            GL11.glTranslatef(-((float)(this.IIllIlIllIlIllIIlIllIlIII * 2) - (float)32) / (float)12 - 1.0f, 0.0f, 0.0f);
        }
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(new ResourceLocation("client/logo_white.png"), (float)(n5 / 2 - 14), (float)(n6 / 2 - 47 - (CheatBreaker.getInstance().isUsingStaffModules() ? 22 : 0)), (float)28, 15);
        if (f13 > 2.0f) {
            CheatBreaker.getInstance().playBold18px.drawString("| CHEAT", n5 / 2 + 18, (float)(n6 / 2 - 42 - (CheatBreaker.getInstance().isUsingStaffModules() ? 22 : 0)), n7);
            CheatBreaker.getInstance().playRegular18px.drawString("BREAKER", n5 / 2 + 53, (float)(n6 / 2 - 42 - (CheatBreaker.getInstance().isUsingStaffModules() ? 22 : 0)), n7);
        }
        GL11.glPopMatrix();
        for (ModulesGuiButtonElement llllIIIIIlIlIlIlIllIIIIII2 : this.buttons) {
            llllIIIIIlIlIlIlIllIIIIII2.handleDrawElement(mouseX, mouseY, f);
        }
        if (draggingModule == null) {
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(n5 / 2 - 185, n6 / 2 + 15, n5 / 2 + 185, n6 - 20, (float)scaledResolution.getScaleFactor() * scale, n6);
            for (AbstractScrollableElement lllIllIllIlIllIlIIllllIIl2 : this.IllIIlIIlllllIllIIIlllIII) {
                if (lllIllIllIlIllIlIIllllIIl2 != this.lIIIIllIIlIlIllIIIlIllIlI && lllIllIllIlIllIlIIllllIIl2 != this.currentScrollableElement) continue;
                lllIllIllIlIllIlIIllllIIl2.handleDrawElement(mouseX, mouseY, f);
            }
            GL11.glDisable(3089);
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
        if (this.someMouseX != -1) {
            if (Mouse.isButtonDown(0)) {
                if (this.someMouseX != mouseX && this.someMouseY != mouseY) {
                    Gui.drawRect(mouseX, this.someMouseY, (float)mouseX + 1.1538461f * 0.43333334f, mouseY, -1358888961);
                    Gui.drawRect((float)this.someMouseX - 0.4329897f * 1.1547619f, mouseY, (float)mouseX + 18.2f * 0.027472526f, (float)mouseY + 0.121212125f * 4.125f, -1358888961);
                    Gui.drawRect((float)this.someMouseX - 0.8666667f * 0.5769231f, this.someMouseY, this.someMouseX, mouseY, -1358888961);
                    Gui.drawRect((float)this.someMouseX - 0.557971f * 0.8961039f, (float)this.someMouseY - 0.3611111f * 1.3846154f, (float)mouseX + 1.2692307f * 0.3939394f, this.someMouseY, -1358888961);
                    Gui.drawRect(this.someMouseX, this.someMouseY, mouseX, mouseY, 0x1F00FFFF);
                }
            } else {
                this.positions.clear();
                for (AbstractModule cBModule4 : this.modules) {
                    int n10;
                    int n11;
                    if (cBModule4.getGuiAnchor() == null || !cBModule4.isEnabled() || cBModule4.getName().contains("Zans") && CheatBreaker.getInstance().moduleManager.minmap.getVoxelMap().getMapOptions().hide) continue;
                    float[] arrf = cBModule4.getScaledPoints(scaledResolution, true);
                    float f14 = scale / (Float) cBModule4.scale.getValue();
                    object = new Rectangle((int)(arrf[0] * (Float) cBModule4.scale.getValue() - 2.0f), (int)(arrf[1] * (Float) cBModule4.scale.getValue() - 2.0f), (int)(cBModule4.width * ((Float)cBModule4.scale.getValue()).floatValue() + (float)4), (int)(cBModule4.height * ((Float)cBModule4.scale.getValue()).floatValue() + (float)4));
                    if (!object.intersects(new Rectangle(n11 = Math.min(this.someMouseX, mouseX), n10 = Math.min(this.someMouseY, mouseY), Math.max(this.someMouseX, mouseX) - n11, Math.max(this.someMouseY, mouseY) - n10))) continue;
                    f3 = (float)mouseX - cBModule4.getXTranslation();
                    f2 = (float)mouseY - cBModule4.getYTranslation();
                    this.positions.add(new CBModulePosition(cBModule4, f3, f2));
                }
                this.someMouseX = -1;
                this.someMouseY = -1;
            }
        }
        if (this.helpButton.isMouseInside(mouseX, mouseY) && (this.lIIIIllIIlIlIllIIIlIllIlI == null || !this.lIIIIllIIlIlIllIIIlIllIlI.isMouseInside(mouseX, mouseY))) {
            this.drawHelpMenu(scale);
        }
    }

    private void drawHelpMenu(float f) {
        GL11.glPushMatrix();
        GL11.glTranslatef(4, (float)this.height - (float)185 * f, 0.0f);
        GL11.glScalef(f, f, f);
        Gui.drawRect(0.0f, 0.0f, 240, 140, -1895825408);
        CheatBreaker.getInstance().ubuntuMedium16px.drawString("Shortcuts & Movement", 4, 2.0f, -1);
        Gui.drawRect(4, 12, 234, 2.5815217f * 4.8421054f, 0x4FFFFFFF);
        int n = 16;
        this.renderRoundButton("Mouse1", 6, n);
        CheatBreaker.getInstance().playRegular14px.drawString("| " + EnumChatFormatting.LIGHT_PURPLE + "HOLD" + EnumChatFormatting.LIGHT_PURPLE + " Add mods to selected region", 80, (float)n+3, -1);
        this.renderRoundButton("Mouse1", 6, n += 12);
        CheatBreaker.getInstance().playRegular14px.drawString("| " + EnumChatFormatting.LIGHT_PURPLE + "HOLD" + EnumChatFormatting.LIGHT_PURPLE + " Select & drag mods", 80, (float)n+3, -1);
        this.renderRoundButton("Mouse2", 6, n += 12);
        CheatBreaker.getInstance().playRegular14px.drawString("| " + EnumChatFormatting.LIGHT_PURPLE + "CLICK" + EnumChatFormatting.LIGHT_PURPLE + " Reset mod to closest position", 80, (float)n+3, -1);
        this.renderRoundButton("Mouse2", 6, n += 12);
        CheatBreaker.getInstance().playRegular14px.drawString("| " + EnumChatFormatting.LIGHT_PURPLE + "HOLD" + EnumChatFormatting.LIGHT_PURPLE + " Don't lock mods while dragging", 80, (float)n+3, -1);
        this.renderRoundButton("CTRL", 6, n += 12);
        CheatBreaker.getInstance().playRegular14px.drawString("+", 30, (float)n+3, -1);
        this.renderRoundButton("Mouse1", 36, n);
        CheatBreaker.getInstance().playRegular14px.drawString("| Toggle (multiple) mod selection", 80, (float)n+3, -1);
        this.renderRoundButton("CTRL", 6, n += 12);
        CheatBreaker.getInstance().playRegular14px.drawString("+", 30, (float)n+3, -1);
        this.renderRoundButton("Z", 36, n);
        CheatBreaker.getInstance().playRegular14px.drawString("| Undo mod movements", 80, (float)n+3, -1);
        this.renderRoundButton("CTRL", 6, n += 12);
        CheatBreaker.getInstance().playRegular14px.drawString("+", 30, (float)n+3, -1);
        this.renderRoundButton("Y", 36, n);
        CheatBreaker.getInstance().playRegular14px.drawString("| Redo mod movements", 80, (float)n+3, -1);
        n = 112;
        this.renderRoundButton("Up", 31, n);
        this.renderRoundButton("Left", 6, n += 12);
        this.renderRoundButton("Down", 26, n);
        this.renderRoundButton("Right", 51, n);
        CheatBreaker.getInstance().playRegular14px.drawString("| Move selected mod with precision", 80, (float)n, -1);
        GL11.glPopMatrix();
    }

    private void renderRoundButton(String string, int n, int n2) {
        CBFontRenderer lIlIllIlIlIIIllllIlIllIll2 = CheatBreaker.getInstance().playRegular14px;
        float f = lIlIllIlIlIIIllllIlIllIll2.getStringWidth(string);
        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(n, n2, (float)n + f + (float)4, n2 + 10, (double)2, -1073741825);
        lIlIllIlIlIIIllllIlIllIll2.drawString(string, n + 2, (float)n2+ 3, -16777216);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int n3) {
        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        if (this.lIIIIllIIlIlIllIIIlIllIlI != null && this.lIIIIllIIlIlIllIIIlIllIlI.isMouseInside(mouseX, mouseY)) {
            this.lIIIIllIIlIlIllIIIlIllIlI.handleMouseClick(mouseX, mouseY, n3);
        } else {
            AbstractModule iterator;
            if (!(draggingModule != null && this.IlIlIIIlllllIIIlIlIlIllII || (iterator = this.lIIIIlIIllIIlIIlIIIlIIllI(scaledResolution, mouseX, mouseY)) == null)) {
                boolean bl;
                float[] arrf = iterator.getScaledPoints(scaledResolution, true);
                boolean bl2 = !iterator.getSettingsList().isEmpty() && (float)mouseX >= arrf[0] * ((Float) iterator.scale.getValue()).floatValue() && (float)mouseX <= (arrf[0] + (float)10) * ((Float) iterator.scale.getValue()).floatValue() && (float)mouseY >= (arrf[1] + iterator.height - (float)10) * ((Float) iterator.scale.getValue()).floatValue() && (float)mouseY <= (arrf[1] + iterator.height + 2.0f) * ((Float) iterator.scale.getValue()).floatValue();
                boolean bl3 = bl = (float)mouseX > (arrf[0] + iterator.width - (float)10) * ((Float) iterator.scale.getValue()).floatValue() && (float)mouseX < (arrf[0] + iterator.width + 2.0f) * ((Float) iterator.scale.getValue()).floatValue() && (float)mouseY > (arrf[1] + iterator.height - (float)10) * ((Float) iterator.scale.getValue()).floatValue() && (float)mouseY < (arrf[1] + iterator.height + 2.0f) * ((Float) iterator.scale.getValue()).floatValue();
                if (bl2) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                    ((ModuleListElement)this.IIIIllIIllIIIIllIllIIIlIl).llIlIIIlIIIIlIlllIlIIIIll = false;
                    ((ModuleListElement)this.IIIIllIIllIIIIllIllIIIlIl).module = iterator;
                    this.currentScrollableElement = this.IIIIllIIllIIIIllIllIIIlIl;
                } else if (bl) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                    iterator.setState(false);
                }
                return;
            }
            for (AbstractModule object : this.modules) {
                CBGuiAnchor cBGuiAnchor;
                SomeRandomAssEnum dELETE_ME_D;
                if (object.getGuiAnchor() == null || !object.isEnabled() || object == CheatBreaker.getInstance().moduleManager.minmap) continue;
                float[] scaledPoints = object.getScaledPoints(scaledResolution, true);
                boolean bl4 = (float)mouseX > scaledPoints[0] * (Float) object.scale.getValue() && (float)mouseX < (scaledPoints[0] + object.width) * (Float) object.scale.getValue() && (float)mouseY > scaledPoints[1] * ((Float) object.scale.getValue()).floatValue() && (float)mouseY < (scaledPoints[1] + object.height) * ((Float) object.scale.getValue()).floatValue();
                boolean bl5 = this.dataHolder != null && this.dataHolder.module == object && this.dataHolder.unknown == SomeRandomAssEnum.LEFT_BOTTOM || !bl4 && (float)mouseX >= (scaledPoints[0] + object.width - (float)5) * ((Float) object.scale.getValue()).floatValue() && (float)mouseX <= (scaledPoints[0] + object.width + (float)5) * ((Float) object.scale.getValue()).floatValue() && (float)mouseY >= (scaledPoints[1] - (float)5) * ((Float) object.scale.getValue()).floatValue() && (float)mouseY <= (scaledPoints[1] + (float)5) * (Float) object.scale.getValue();
                boolean bl6 = this.dataHolder != null && this.dataHolder.module == object && this.dataHolder.unknown == SomeRandomAssEnum.RIGHT_TOP || !bl4 && (float)mouseX >= (scaledPoints[0] - (float)5) * (Float) object.scale.getValue() && (float)mouseX <= (scaledPoints[0] + (float)5) * ((Float) object.scale.getValue()).floatValue() && (float)mouseY >= (scaledPoints[1] + object.height - (float)5) * ((Float) object.scale.getValue()).floatValue() && (float)mouseY <= (scaledPoints[1] + object.height + (float)5) * (Float) object.scale.getValue();
                boolean bl7 = this.dataHolder != null && this.dataHolder.module == object && this.dataHolder.unknown == SomeRandomAssEnum.RIGHT_BOTTOM || !bl4 && (float)mouseX >= (scaledPoints[0] - (float)5) * (Float) object.scale.getValue() && (float)mouseX <= (scaledPoints[0] + (float)5) * ((Float) object.scale.getValue()).floatValue() && (float)mouseY >= (scaledPoints[1] - (float)5) * ((Float) object.scale.getValue()).floatValue() && (float)mouseY <= (scaledPoints[1] + (float)5) * ((Float) object.scale.getValue()).floatValue();
                boolean bl = this.dataHolder != null && this.dataHolder.module == object && this.dataHolder.unknown == SomeRandomAssEnum.LEFT_TOP || !bl4 && (float)mouseX >= (scaledPoints[0] + object.width - (float)5) * (Float) object.scale.getValue() && (float)mouseX <= (scaledPoints[0] + object.width + (float)5) * ((Float) object.scale.getValue()).floatValue() && (float)mouseY >= (scaledPoints[1] + object.height - (float)5) * ((Float) object.scale.getValue()).floatValue() && (float)mouseY <= (scaledPoints[1] + object.height + (float)5) * (Float) object.scale.getValue();
                if (this.someMouseX != -1 || !bl5 && !bl6 && !bl7 && !bl) continue;
                if (bl5) {
                    dELETE_ME_D = SomeRandomAssEnum.LEFT_BOTTOM;
                    cBGuiAnchor = CBGuiAnchor.LEFT_BOTTOM;
                } else if (bl6) {
                    dELETE_ME_D = SomeRandomAssEnum.RIGHT_TOP;
                    cBGuiAnchor = CBGuiAnchor.RIGHT_TOP;
                } else if (bl7) {
                    dELETE_ME_D = SomeRandomAssEnum.RIGHT_BOTTOM;
                    cBGuiAnchor = CBGuiAnchor.RIGHT_BOTTOM;
                } else {
                    dELETE_ME_D = SomeRandomAssEnum.LEFT_TOP;
                    cBGuiAnchor = CBGuiAnchor.LEFT_TOP;
                }
                if (this.lIIIIIIIIIlIllIIllIlIIlIl(scaledResolution, mouseX, mouseY)) continue;
                if (n3 == 0) {
                    this.undoList.add(new ModuleActionData(this, this.positions));
                    this.dataHolder = new ModuleDataHolder(this, object, dELETE_ME_D, mouseX, mouseY);
                    this.lIIIIlIIllIIlIIlIIIlIIllI(object, cBGuiAnchor, scaledResolution);
                } else if (n3 == 1) {
                    CBGuiAnchor cBGuiAnchor2 = object.getGuiAnchor();
                    this.lIIIIlIIllIIlIIlIIIlIIllI(object, cBGuiAnchor, scaledResolution);
                    object.scale.setValue(1.0f);
                    this.lIIIIlIIllIIlIIlIIIlIIllI(object, cBGuiAnchor2, scaledResolution);
                }
                return;
            }
            if (draggingModule == null) {
                if (this.showGuidesButton.isMouseInside(mouseX, mouseY)) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                    this.IlIIIIllIIIIIlllIIlIIlllI = !this.IlIIIIllIIIIIlllIIlIIlllI;
                }
                this.IlllIIIlIlllIllIlIIlllIlI(mouseX, mouseY, n3);
                this.lIIIIlIIllIIlIIlIIIlIIllI(scaledResolution, mouseX, mouseY, n3);
            }
            for (Object object : this.buttons) {
                if (!((AbstractModulesGuiElement)object).isMouseInside(mouseX, mouseY)) continue;
                return;
            }
            boolean bl = this.lIIIIIIIIIlIllIIllIlIIlIl(scaledResolution, mouseX, mouseY);
            if (bl) {
                return;
            }
            if (!this.positions.isEmpty()) {
                this.positions.clear();
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            }
            this.someMouseX = mouseX;
            this.someMouseY = mouseY;
        }
        if (!this.positions.isEmpty()) {
            this.arrowKeyMoves = 0;
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int n = Mouse.getEventDWheel();
        if (this.lIIIIllIIlIlIllIIIlIllIlI != null) {
            this.lIIIIllIIlIlIllIIIlIllIlI.onScroll(n);
        }
    }

    private void lIIIIlIIllIIlIIlIIIlIIllI(AbstractModule cBModule, CBGuiAnchor cBGuiAnchor, ScaledResolution scaledResolution) {
        if (cBGuiAnchor != cBModule.getGuiAnchor()) {
            float[] scaledPointsWithTranslations = cBModule.getScaledPoints(scaledResolution, true);
            cBModule.setAnchor(cBGuiAnchor);
            float[] scaledPointsWithoutTranslations = cBModule.getScaledPoints(scaledResolution, false);
            cBModule.setTranslations(
                    scaledPointsWithTranslations[0] * (Float) cBModule.scale.getValue() - scaledPointsWithoutTranslations[0] * (Float) cBModule.scale.getValue(),
                    scaledPointsWithTranslations[1] * (Float) cBModule.scale.getValue() - scaledPointsWithoutTranslations[1] * (Float) cBModule.scale.getValue()
            );
        }
    }

    @Override
    public void mouseMovedOrUp(int mouseX, int mouseY, int button) {
        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        if (this.dataHolder != null && button == 0) {
            this.lIIIIlIIllIIlIIlIIIlIIllI(this.dataHolder.module, this.dataHolder.anchor, scaledResolution);
            this.dataHolder = null;
        }
        if (draggingModule != null && button == 0) {
            if (this.IlIlIIIlllllIIIlIlIlIllII) {
                for (CBModulePosition CBModulePosition : this.positions) {
                    CBGuiAnchor cBGuiAnchor = CBAnchorHelper.getAnchor(mouseX, mouseY, scaledResolution);
                    if (cBGuiAnchor == CBGuiAnchor.MIDDLE_MIDDLE || cBGuiAnchor == CBModulePosition.module.getGuiAnchor() || !this.IlIlIIIlllllIIIlIlIlIllII) continue;
                    this.lIIIIlIIllIIlIIlIIIlIIllI(CBModulePosition.module, cBGuiAnchor, scaledResolution);
                    CBModulePosition.x = (float)mouseX - CBModulePosition.module.getXTranslation();
                    CBModulePosition.y = (float)mouseY - CBModulePosition.module.getYTranslation();
                }
                if (this.getModulePosition(draggingModule) == null) {
                    float x = (float)mouseX - draggingModule.getXTranslation();
                    float y = (float)mouseY - draggingModule.getYTranslation();
                    this.positions.add(new CBModulePosition(draggingModule, x, y));
                }
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            }
            System.out.println(draggingModule.getName());
            draggingModule = null;
        }
    }

    @Override
    public void keyTyped(char c, int n) {
        if (n == 1) {
            CheatBreaker.getInstance().configManager.write();
        }
        super.keyTyped(c, n);
        if (n == Keyboard.KEY_Z && isCtrlKeyDown()) {
            if (!this.undoList.isEmpty()) {
                int n2 = this.undoList.size() - 1;
                ModuleActionData moduleActionData = this.undoList.get(this.undoList.size() - 1);
                for (int i = 0; i < moduleActionData.modules.size(); ++i) {
                    AbstractModule cBModule = moduleActionData.modules.get(i);
                    float f = moduleActionData.xTranslations.get(i);
                    float f2 = moduleActionData.yTranslations.get(i);
                    CBGuiAnchor cBGuiAnchor = moduleActionData.anchors.get(i);
                    Float f3 = moduleActionData.scales.get(i);
                    cBModule.setAnchor(cBGuiAnchor);
                    cBModule.setTranslations(f, f2);
                    cBModule.scale.setValue(f3);
                }
                if (this.redo.size() > 50) {
                    this.redo.remove(0);
                }
                this.redo.add(moduleActionData);
                this.undoList.remove(n2);
            }
        } else if (n == Keyboard.KEY_Y && isCtrlKeyDown()) {
            if (!this.redo.isEmpty()) {
                int n3 = this.redo.size() - 1;
                ModuleActionData moduleActionData = this.redo.get(this.redo.size() - 1);
                for (int i = 0; i < moduleActionData.modules.size(); ++i) {
                    AbstractModule module = moduleActionData.modules.get(i);
                    float xTranslation = moduleActionData.xTranslations.get(i);
                    float yTranslation = moduleActionData.yTranslations.get(i);
                    CBGuiAnchor anchor = moduleActionData.anchors.get(i);
                    float scale = moduleActionData.scales.get(i);
                    module.setAnchor(anchor);
                    module.setTranslations(xTranslation, yTranslation);
                    module.scale.setValue(scale);
                }
                if (this.redo.size() > 50) {
                    this.redo.remove(0);
                }
                this.undoList.add(moduleActionData);
                this.redo.remove(n3);
            }
        } else {
            this.arrowKeyMoves = 0;
            for (CBModulePosition CBModulePosition : this.positions) {
                AbstractModule cBModule = CBModulePosition.module;
                if (cBModule == null) continue;
                switch (n) {
                    case 203: {
                        cBModule.setTranslations(cBModule.getXTranslation() - 1.0f, cBModule.getYTranslation());
                        break;
                    }
                    case 205: {
                        cBModule.setTranslations(cBModule.getXTranslation() + 1.0f, cBModule.getYTranslation());
                        break;
                    }
                    case 200: {
                        cBModule.setTranslations(cBModule.getXTranslation(), cBModule.getYTranslation() - 1.0f);
                        break;
                    }
                    case 208: {
                        cBModule.setTranslations(cBModule.getXTranslation(), cBModule.getYTranslation() + 1.0f);
                    }
                }
            }
        }
    }

    private void snapHorizontally(float f) {
        for (CBModulePosition CBModulePosition : this.positions) {
            CBModulePosition.module.setTranslations(CBModulePosition.module.getXTranslation() + f, CBModulePosition.module.getYTranslation());
        }
    }

    private void snapVertically(float f) {
        for (CBModulePosition CBModulePosition : this.positions) {
            CBModulePosition.module.setTranslations(CBModulePosition.module.getXTranslation(), CBModulePosition.module.getYTranslation() + f);
        }
    }

    private void lIIIIlIIllIIlIIlIIIlIIllI(ScaledResolution scaledResolution, int n, int n2, int n3) {
        for (AbstractModule cBModule : this.modules) {
            boolean bl;
            float[] arrf;
            if (cBModule.getGuiAnchor() == null || !cBModule.isEnabled() || cBModule.getName().contains("Zans") && CheatBreaker.getInstance().moduleManager.minmap.getVoxelMap().getMapOptions().hide)
                continue;
            float f = cBModule.width;
            float f2 = cBModule.height;
            float f3 = 18;
            if (f < f3) {
                cBModule.width = f3;
            }
            if (f2 < (float) 18) {
                cBModule.height = 18;
            }
            if (!((float) n > (arrf = cBModule.getScaledPoints(scaledResolution, true))[0] * (Float) cBModule.scale.getValue() && (float) n < (arrf[0] + cBModule.width) * ((Float) cBModule.scale.getValue()).floatValue() && (float) n2 > arrf[1] * ((Float) cBModule.scale.getValue()).floatValue() && (float) n2 < (arrf[1] + cBModule.height) * ((Float) cBModule.scale.getValue()).floatValue()))
                continue;
            boolean bl3 = !cBModule.getSettingsList().isEmpty() && (float) n >= arrf[0] * (Float) cBModule.scale.getValue() && (float) n <= (arrf[0] + (float) 10) * ((Float) cBModule.scale.getValue()).floatValue() && (float) n2 >= (arrf[1] + cBModule.height - (float) 10) * ((Float) cBModule.scale.getValue()).floatValue() && (float) n2 <= (arrf[1] + cBModule.height + 2.0f) * ((Float) cBModule.scale.getValue()).floatValue();
            boolean bl4 = bl = (float) n > (arrf[0] + cBModule.width - (float) 10) * (Float) cBModule.scale.getValue() && (float) n < (arrf[0] + cBModule.width + 2.0f) * ((Float) cBModule.scale.getValue()).floatValue() && (float) n2 > (arrf[1] + cBModule.height - (float) 10) * ((Float) cBModule.scale.getValue()).floatValue() && (float) n2 < (arrf[1] + cBModule.height + 2.0f) * ((Float) cBModule.scale.getValue()).floatValue();
            if (n3 == 0 && !bl3 && !bl && cBModule != CheatBreaker.getInstance().moduleManager.minmap) {
                boolean bl5 = true;
                if (this.getModulePosition(cBModule) != null) {
                    this.removePositionForModule(cBModule);
                    bl5 = false;
                }
                float f4 = (float) n - cBModule.getXTranslation() * (Float) cBModule.scale.getValue();
                float f5 = (float) n2 - cBModule.getYTranslation() * (Float) cBModule.scale.getValue();
                this.IIlIIllIIIllllIIlllIllIIl = n;
                this.lllIlIIllllIIIIlIllIlIIII = n2;
                this.IlIlIIIlllllIIIlIlIlIllII = false;
                draggingModule = cBModule;
                if (this.getModulePosition(cBModule) == null) {
                    if (!isCtrlKeyDown() && bl5) {
                        this.positions.clear();
                    }
                    if (bl5 || !isCtrlKeyDown()) {
                        this.positions.add(new CBModulePosition(cBModule, f4, f5));
                    }
                }
                this.IlllIIIlIlllIllIlIIlllIlI(scaledResolution, n, n2);
            }
            if (!(n3 != 0 || this.lIIIIllIIlIlIllIIIlIllIlI != null && this.lIIIIllIIlIlIllIIIlIllIlI.isMouseInside(n, n2))) {
                if (bl3) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                    ((ModuleListElement) this.IIIIllIIllIIIIllIllIIIlIl).llIlIIIlIIIIlIlllIlIIIIll = false;
                    ((ModuleListElement) this.IIIIllIIllIIIIllIllIIIlIl).module = cBModule;
                    this.currentScrollableElement = this.IIIIllIIllIIIIllIllIIIlIl;
                } else if (bl) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                    cBModule.setState(false);
                }
            } else if (n3 == 1) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                float[] arrf2 = CBAnchorHelper.getPositions(cBModule.getGuiAnchor());
                cBModule.setTranslations(arrf2[0], arrf2[1]);
            }
            if (cBModule == CheatBreaker.getInstance().moduleManager.minmap) continue;
            break;
        }
    }

    private void IlllIIIlIlllIllIlIIlllIlI(int n, int n2, int n3) {
        for (ModulesGuiButtonElement llllIIIIIlIlIlIlIllIIIIII2 : this.buttons) {
            if (n3 != 0 || !llllIIIIIlIlIlIlIllIIIIII2.isMouseInside(n, n2) || IlIlllIIIIllIllllIllIIlIl) continue;
            if (llllIIIIIlIlIlIlIllIIIIII2.lIIIIllIIlIlIllIIIlIllIlI != null && this.lIIIIllIIlIlIllIIIlIllIlI != llllIIIIIlIlIlIlIllIIIIII2.lIIIIllIIlIlIllIIIlIllIlI && this.currentScrollableElement == null) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
                this.currentScrollableElement = llllIIIIIlIlIlIlIllIIIIII2.lIIIIllIIlIlIllIIIlIllIlI;
                continue;
            }
            if (llllIIIIIlIlIlIlIllIIIIII2.lIIIIllIIlIlIllIIIlIllIlI == null || this.currentScrollableElement != null) continue;
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0f));
            IlIlllIIIIllIllllIllIIlIl = true;
        }
    }

    private AbstractModule lIIIIlIIllIIlIIlIIIlIIllI(ScaledResolution scaledResolution, int n, int n2) {
        for (AbstractModule cBModule : this.modules) {
            if (cBModule.getGuiAnchor() == null) continue;
            float[] arrf = cBModule.getScaledPoints(scaledResolution, true);
            boolean bl = (float) n > (arrf[0] + cBModule.width - (float) 10) * (Float) cBModule.scale.getValue() && (float) n < (arrf[0] + cBModule.width + 2.0f) * (Float) cBModule.scale.getValue() && (float) n2 > (arrf[1] + cBModule.height - (float) 10) * (Float) cBModule.scale.getValue() && (float) n2 < (arrf[1] + cBModule.height + 2.0f) * (Float) cBModule.scale.getValue();
            boolean bl2 = !cBModule.getSettingsList().isEmpty() && (float)n >= arrf[0] * (Float) cBModule.scale.getValue() && (float)n <= (arrf[0] + (float)10) * (Float) cBModule.scale.getValue() && (float)n2 >= (arrf[1] + cBModule.height - (float)10) * (Float) cBModule.scale.getValue() && (float)n2 <= (arrf[1] + cBModule.height + 2.0f) * (Float) cBModule.scale.getValue();
            if (!bl && !bl2) continue;
            return cBModule;
        }
        return null;
    }

    private boolean lIIIIIIIIIlIllIIllIlIIlIl(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        boolean bl = false;
        for (AbstractModule cBModule : this.modules) {
            if (cBModule.getGuiAnchor() == null) continue;
            float[] arrf = cBModule.getScaledPoints(scaledResolution, true);
            boolean bl2 = (float)mouseX > arrf[0] * (Float) cBModule.scale.getValue() && (float)mouseX < (arrf[0] + cBModule.width) * (Float) cBModule.scale.getValue() && (float)mouseY > arrf[1] * (Float) cBModule.scale.getValue() && (float)mouseY < (arrf[1] + cBModule.height) * (Float) cBModule.scale.getValue();
            bl = bl || bl2;
        }
        return bl;
    }

    private boolean lIIIIlIIllIIlIIlIIIlIIllI(float f, AbstractModule cBModule, ScaledResolution scaledResolution, int n, int n2, boolean bl) {
        int n3;
        int n4;
        int n5;
        int n6;
        float[] object;
        boolean bl2;
        if (cBModule.getGuiAnchor() == null || !cBModule.isEnabled() || cBModule == CheatBreaker.getInstance().moduleManager.minmap || !cBModule.isEditable && !cBModule.isRenderHud()) {
            return true;
        }
        boolean bl3 = false;
        float f2 = 18;
        if (cBModule.width < f2) {
            cBModule.width = (int)f2;
        }
        if (cBModule.height < (float)18) {
            cBModule.height = 18;
        }
        GL11.glPushMatrix();
        float[] arrf = cBModule.getScaledPoints(scaledResolution, true);
        cBModule.scaleAndTranslate(scaledResolution);
        bl2 = this.someMouseX != -1;
        if (bl2) {
            Rectangle rectangle1 = new Rectangle((int)(arrf[0] * (Float) cBModule.scale.getValue() - 2.0f), (int)(arrf[1] * ((Float)cBModule.scale.getValue()).floatValue() - 2.0f), (int)(cBModule.width * ((Float)cBModule.scale.getValue()).floatValue() + (float)4), (int)(cBModule.height * ((Float)cBModule.scale.getValue()).floatValue() + (float)4));
            n6 = Math.min(this.someMouseX, n);
            n5 = Math.min(this.someMouseY, n2);
            n4 = Math.max(this.someMouseX, n) - n6;
            n3 = Math.max(this.someMouseY, n2) - n5;
            Rectangle rectangle = new Rectangle(n6, n5, n4, n3);
            bl2 = rectangle1.intersects(rectangle);
        }
        n6 = (float) n > (object = cBModule.getScaledPoints(scaledResolution, true))[0] * (Float) cBModule.scale.getValue() && (float) n < (object[0] + cBModule.width) * ((Float) cBModule.scale.getValue()).floatValue() && (float) n2 > object[1] * ((Float) cBModule.scale.getValue()).floatValue() && (float) n2 < (object[1] + cBModule.height) * ((Float) cBModule.scale.getValue()).floatValue() ? 1 : 0;
        if (!this.IlIIIIllIIIIIlllIIlIIlllI) {
            if (this.getModulePosition(cBModule) != null || bl2) {
                Gui.lIIIIlIIllIIlIIlIIIlIIllI(0.0f, 0.0f, cBModule.width, cBModule.height, 2.064516f * 0.2421875f, -1627324417, 0x1AFFFFFF);
            } else {
                Gui.lIIIIlIIllIIlIIlIIIlIIllI(0.0f, 0.0f, cBModule.width, cBModule.height, 1.2179487f * 0.41052634f, 0x6FFFFFFF, 0x1AFFFFFF);
            }
        }
        if (!this.IlIIIIllIIIIIlllIIlIIlllI && n6 != 0) {
            n5 = !cBModule.getSettingsList().isEmpty() && (float)n >= (object[0] + 2.0f) * (Float) cBModule.scale.getValue() && (float)n <= (object[0] + (float)10) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 >= (object[1] + cBModule.height - (float)8) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 <= (object[1] + cBModule.height - 2.0f) * ((Float)cBModule.scale.getValue()).floatValue() ? 1 : 0;
            int n8 = n4 = (float)n > (object[0] + cBModule.width - (float)10) * (Float) cBModule.scale.getValue() && (float)n < (object[0] + cBModule.width - 2.0f) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 > (object[1] + cBModule.height - (float)8) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 < (object[1] + cBModule.height - 2.0f) * ((Float)cBModule.scale.getValue()).floatValue() ? 1 : 0;
            if (!cBModule.getSettingsList().isEmpty()) {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, n5 != 0 ? 1.0f : 0.20895523f * 2.8714287f);
                RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.cogIcon, (float)3, 2.0f, cBModule.height - 2.162162f * 3.4687502f);
            }
            GL11.glColor4f(1.2952381f * 0.61764705f, 0.4181818f * 0.47826087f, 0.09268292f * 2.1578948f, n4 != 0 ? 1.0f : 2.025f * 0.2962963f);
            RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(this.deleteIcon, (float)3, cBModule.width - (float)8, cBModule.height - 0.2972973f * 25.227272f);
        }
        GL11.glPushMatrix();
        float f3 = f / (Float) cBModule.scale.getValue();
        GL11.glScalef(f3, f3, f3);
        if (bl) {
            n4 = this.dataHolder != null && this.dataHolder.module == cBModule && this.dataHolder.unknown == SomeRandomAssEnum.LEFT_BOTTOM || n6 == 0 && (float)n >= (object[0] + cBModule.width - (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n <= (object[0] + cBModule.width + (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 >= (object[1] - (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 <= (object[1] + (float)5) * ((Float)cBModule.scale.getValue()).floatValue() ? 1 : 0;
            n3 = this.dataHolder != null && this.dataHolder.module == cBModule && this.dataHolder.unknown == SomeRandomAssEnum.RIGHT_TOP || n6 == 0 && (float)n >= (object[0] - (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n <= (object[0] + (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 >= (object[1] + cBModule.height - (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 <= (object[1] + cBModule.height + (float)5) * ((Float)cBModule.scale.getValue()).floatValue() ? 1 : 0;
            boolean bl5 = this.dataHolder != null && this.dataHolder.module == cBModule && this.dataHolder.unknown == SomeRandomAssEnum.RIGHT_BOTTOM || n6 == 0 && (float)n >= (object[0] - (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n <= (object[0] + (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 >= (object[1] - (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 <= (object[1] + (float)5) * ((Float)cBModule.scale.getValue()).floatValue();
            boolean bl6 = this.dataHolder != null && this.dataHolder.module == cBModule && this.dataHolder.unknown == SomeRandomAssEnum.LEFT_TOP || n6 == 0 && (float)n >= (object[0] + cBModule.width - (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n <= (object[0] + cBModule.width + (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 >= (object[1] + cBModule.height - (float)5) * ((Float)cBModule.scale.getValue()).floatValue() && (float)n2 <= (object[1] + cBModule.height + (float)5) * ((Float)cBModule.scale.getValue()).floatValue();
            GL11.glPushMatrix();
            float f4 = 4;
            if (this.someMouseX == -1 && bl5) {
                GL11.glTranslatef(0.0f, 0.0f, 0.0f);
                Gui.drawRect(-f4 / 2.0f, -f4 / 2.0f, f4 / 2.0f, f4 / 2.0f, -16711936);
            }
            if (this.someMouseX == -1 && n4 != 0) {
                GL11.glTranslatef(cBModule.width / f3, 0.0f, 0.0f);
                Gui.drawRect(-f4 / 2.0f, -f4 / 2.0f, f4 / 2.0f, f4 / 2.0f, -16711936);
            }
            if (this.someMouseX == -1 && bl6) {
                GL11.glTranslatef(cBModule.width / f3, cBModule.height / f3, 0.0f);
                Gui.drawRect(-f4 / 2.0f, -f4 / 2.0f, f4 / 2.0f, f4 / 2.0f, -16711936);
            }
            if (this.someMouseX == -1 && n3 != 0) {
                GL11.glTranslatef(0.0f, cBModule.height / f3, 0.0f);
                Gui.drawRect(-f4 / 2.0f, -f4 / 2.0f, f4 / 2.0f, f4 / 2.0f, -16711936);
            }
            GL11.glPopMatrix();
            bl3 = this.someMouseX == -1 && (bl5 || n4 != 0 || n3 != 0 || bl6);
        }
        n4 = arrf[1] - CheatBreaker.getInstance().ubuntuMedium16px.getHeight() - (float)6 < 0.0f ? 1 : 0;
        float f5 = n4 != 0 ? cBModule.height * (Float) cBModule.scale.getValue() / f : (float)(-CheatBreaker.getInstance().ubuntuMedium16px.getHeight() - 4);
        switch (cBModule.getPosition()) {
            case LEFT: {
                float f6 = 0.0f;
                CheatBreaker.getInstance().ubuntuMedium16px.drawString(cBModule.getName(), f6, f5, -1);
                break;
            }
            case CENTER: {
                float f7 = cBModule.width * (Float) cBModule.scale.getValue() / f / 2.0f;
                CheatBreaker.getInstance().ubuntuMedium16px.drawString(cBModule.getName(), f7, f5, -1);
                break;
            }
            case RIGHT: {
                float f8 = cBModule.width * (Float) cBModule.scale.getValue() / f - (float) CheatBreaker.getInstance().ubuntuMedium16px.getStringWidth(cBModule.getName());
                CheatBreaker.getInstance().ubuntuMedium16px.drawString(cBModule.getName(), f8, f5, -1);
            }
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        return !bl3;
    }

    private void lIIIIlIIllIIlIIlIIIlIIllI(ScaledResolution scaledResolution) {
        if (!Mouse.isButtonDown(1) && draggingModule != null) {
            for (CBModulePosition CBModulePosition : this.positions) {
                if (CBModulePosition.module != draggingModule || !(Boolean) CheatBreaker.getInstance().globalSettings.snapModules.getValue()) continue;
                Object var5_5 = null;
                for (AbstractModule cBModule : this.modules) {
                    if (this.getModulePosition(cBModule) != null || cBModule.getGuiAnchor() == null || !cBModule.isEnabled() || cBModule == CheatBreaker.getInstance().moduleManager.minmap || !cBModule.isEditable && !cBModule.isRenderHud()) continue;
                    float f = 18;
                    if (cBModule.width < f) {
                        cBModule.width = (int)f;
                    }
                    if (cBModule.height < (float)18) {
                        cBModule.height = 18;
                    }
                    if (CBModulePosition.module.width < f) {
                        CBModulePosition.module.width = (int)f;
                    }
                    if (CBModulePosition.module.height < (float)18) {
                        CBModulePosition.module.height = 18;
                    }
                    float[] arrf = cBModule.getScaledPoints(scaledResolution, true);
                    float[] arrf2 = CBModulePosition.module.getScaledPoints(scaledResolution, true);
                    boolean bl = false;
                    float f2 = arrf[0] * (Float) cBModule.scale.getValue() - arrf2[0] * (Float) CBModulePosition.module.scale.getValue();
                    float f3 = (arrf[0] + cBModule.width) * (Float) cBModule.scale.getValue() - (arrf2[0] + CBModulePosition.module.width) * (Float) CBModulePosition.module.scale.getValue();
                    float f4 = (arrf[0] + cBModule.width) * (Float) cBModule.scale.getValue() - arrf2[0] * (Float) CBModulePosition.module.scale.getValue();
                    float f5 = arrf[0] * (Float) cBModule.scale.getValue() - (arrf2[0] + CBModulePosition.module.width) * (Float) CBModulePosition.module.scale.getValue();
                    float f6 = arrf[1] * (Float) cBModule.scale.getValue() - arrf2[1] * (Float) CBModulePosition.module.scale.getValue();
                    float f7 = (arrf[1] + cBModule.height) * (Float) cBModule.scale.getValue() - (arrf2[1] + CBModulePosition.module.height) * (Float) CBModulePosition.module.scale.getValue();
                    float f8 = (arrf[1] + cBModule.height) * (Float) cBModule.scale.getValue() - arrf2[1] * (Float) CBModulePosition.module.scale.getValue();
                    float f9 = arrf[1] * (Float) cBModule.scale.getValue() - (arrf2[1] + CBModulePosition.module.height) * (Float) CBModulePosition.module.scale.getValue();
                    int n = 2;
                    if (f2 >= (float)(-n) && f2 <= (float)n) {
                        bl = true;
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(arrf[0] * (Float) cBModule.scale.getValue() - 0.6666667f * 0.75f, 0.0, arrf[0] * (Float) cBModule.scale.getValue(), this.height, 0.0, -3596854);
                    }
                    if (f3 >= (float)(-n) && f3 <= (float)n) {
                        bl = true;
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((arrf[0] + cBModule.width) * (Float) cBModule.scale.getValue(), 0.0, (arrf[0] + cBModule.width) * (Float) cBModule.scale.getValue() + 1.7272727f * 0.28947368f, this.height, 0.0, -3596854);
                    }
                    if (f5 >= (float)(-n) && f5 <= (float)n) {
                        bl = true;
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(arrf[0] * (Float) cBModule.scale.getValue(), 0.0, arrf[0] * (Float) cBModule.scale.getValue() + 0.29775283f * 1.6792452f, this.height, 0.0, -3596854);
                    }
                    if (f4 >= (float)(-n) && f4 <= (float)n) {
                        bl = true;
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI((arrf[0] + cBModule.width) * (Float) cBModule.scale.getValue(), 0.0, (arrf[0] + cBModule.width) * (Float) cBModule.scale.getValue() + 1.5238096f * 0.328125f, this.height, 0.0, -3596854);
                    }
                    if (f6 >= (float)(-n) && f6 <= (float)n) {
                        bl = true;
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(0.0, arrf[1] * (Float) cBModule.scale.getValue(), this.width, arrf[1] * (Float) cBModule.scale.getValue() + 0.3888889f * 1.2857143f, 0.0, -3596854);
                    }
                    if (f7 >= (float)(-n) && f7 <= (float)n) {
                        bl = true;
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(0.0, (arrf[1] + cBModule.height) * (Float) cBModule.scale.getValue(), this.width, (arrf[1] + cBModule.height) * (Float) cBModule.scale.getValue() + 0.51724136f * 0.9666667f, 0.0, -3596854);
                    }
                    if (f9 >= (float)(-n) && f9 <= (float)n) {
                        bl = true;
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(0.0, arrf[1] * (Float) cBModule.scale.getValue(), this.width, arrf[1] * (Float) cBModule.scale.getValue() + 0.16666667f * 3.0f, 0.0, -3596854);
                    }
                    if (f8 >= (float)(-n) && f8 <= (float)n) {
                        bl = true;
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(0.0, (arrf[1] + cBModule.height) * ((Float)cBModule.scale.getValue()).floatValue() - 0.5810811f * 0.8604651f, this.width, (arrf[1] + cBModule.height) * ((Float)cBModule.scale.getValue()).floatValue(), 0.0, -3596854);
                    }
                    if (!bl) continue;
                    GL11.glPushMatrix();
                    cBModule.scaleAndTranslate(scaledResolution);
                    Gui.lIIIIlIIllIIlIIlIIIlIIllI(0.0f, 0.0f, cBModule.width, cBModule.height, 0.01923077f * 26.0f, 0, 449387978);
                    GL11.glPopMatrix();
                }
            }
        }
    }

    private float lIIIIlIIllIIlIIlIIIlIIllI(AbstractModule cBModule, float f, float[] arrf, int n) {
        float f2 = f;
        float padding = 2.0f;
        if (f2 + arrf[0] * (Float) cBModule.scale.getValue() < padding) {
            f2 = -arrf[0] * (Float) cBModule.scale.getValue() + padding;
        } else if (f2 + arrf[0] * (Float) cBModule.scale.getValue() + (float)n > (float)this.width - padding) {
            f2 = (float)this.width - arrf[0] * (Float) cBModule.scale.getValue() - (float)n - padding;
        }
        return f2;
    }

    private float lIIIIIIIIIlIllIIllIlIIlIl(AbstractModule cBModule, float f, float[] arrf, int n) {
        float f2 = f;
        float padding = 2.0f;
        if (f2 + arrf[1] * (Float) cBModule.scale.getValue() < padding) {
            f2 = -arrf[1] * (Float) cBModule.scale.getValue() + padding;
        } else if (f2 + arrf[1] * (Float) cBModule.scale.getValue() + (float)n > (float)this.height - padding) {
            f2 = (float)this.height - arrf[1] * (Float) cBModule.scale.getValue() - (float)n - padding;
        }
        return f2;
    }

    private void lIIIIlIIllIIlIIlIIIlIIllI(CBModulePosition CBModulePosition, int n, int n2, ScaledResolution scaledResolution) {
        if (CBModulePosition.module.getGuiAnchor() == null || !CBModulePosition.module.isEnabled() || CBModulePosition.module == CheatBreaker.getInstance().moduleManager.minmap || !CBModulePosition.module.isEditable && !CBModulePosition.module.isRenderHud()) {
            return;
        }
        float f = (float)n - CBModulePosition.x;
        float f2 = (float)n2 - CBModulePosition.y;
        if (!(this.IlIlIIIlllllIIIlIlIlIllII || CBModulePosition.module != draggingModule || (float)n == this.IIlIIllIIIllllIIlllIllIIl && (float)n2 == this.lllIlIIllllIIIIlIllIlIIII)) {
            if (this.undoList.size() > 50) {
                this.undoList.remove(0);
            }
            this.undoList.add(new ModuleActionData(this, this.positions));
            CheatBreaker.getInstance().createNewProfile();
            this.IlIlIIIlllllIIIlIlIlIllII = true;
        }
        float[] arrf = CBModulePosition.module.getScaledPoints(scaledResolution, false);
        if (!Mouse.isButtonDown(1) && this.IlIlIIIlllllIIIlIlIlIllII && CBModulePosition.module == draggingModule) {
            float f3 = f;
            float f4 = f2;
            f = this.lIIIIlIIllIIlIIlIIIlIIllI(CBModulePosition.module, f, arrf, (int)(CBModulePosition.module.width * (Float) CBModulePosition.module.scale.getValue()));
            f2 = this.lIIIIIIIIIlIllIIllIlIIlIl(CBModulePosition.module, f2, arrf, (int)(CBModulePosition.module.height * (Float) CBModulePosition.module.scale.getValue()));
            float f5 = f3 - f;
            float f6 = f4 - f2;
            for (CBModulePosition dragCache2 : this.positions) {
                if (dragCache2 == CBModulePosition) continue;
                arrf = dragCache2.module.getScaledPoints(scaledResolution, false);
                float f7 = this.lIIIIlIIllIIlIIlIIIlIIllI(dragCache2.module, dragCache2.module.getXTranslation() - f5, arrf, (int)(dragCache2.module.width * (Float) dragCache2.module.scale.getValue()));
                float f8 = this.lIIIIIIIIIlIllIIllIlIIlIl(dragCache2.module, dragCache2.module.getYTranslation() - f6, arrf, (int)(dragCache2.module.height * (Float) dragCache2.module.scale.getValue()));
                dragCache2.module.setTranslations(f7, f8);
            }
        }
        if (this.IlIlIIIlllllIIIlIlIlIllII) {
            CBModulePosition.module.setTranslations(f, f2);
        }
    }

    private void lIIIIlIIllIIlIIlIIIlIIllI(int n) {
        if (IlIlllIIIIllIllllIllIIlIl) {
            if (this.lIIIIllIIlIlIllIIIlIllIlI != null) {
                this.lIIIIlIIllIIlIIlIIIlIIllI(this.lIIIIllIIlIlIllIIIlIllIlI, true, n);
            }
        } else if (this.currentScrollableElement != null) {
            if (this.lIIIIllIIlIlIllIIIlIllIlI != null) {
                this.lIIIIlIIllIIlIIlIIIlIIllI(this.lIIIIllIIlIlIllIIIlIllIlI, true, n);
            }
            this.lIIIIlIIllIIlIIlIIIlIIllI(this.currentScrollableElement, false, n);
        }
    }

    private void lIIIIlIIllIIlIIlIIIlIIllI(AbstractScrollableElement lllIllIllIlIllIlIIllllIIl2, boolean bl, int n) {
        if (bl) {
            lllIllIllIlIllIlIIllllIIl2.x = lllIllIllIlIllIlIIllllIIl2.IlIlllIIIIllIllllIllIIlIl;
            IlIlllIIIIllIllllIllIIlIl = false;
            this.lIIIIllIIlIlIllIIIlIllIlI = null;
        } else {
            lllIllIllIlIllIlIIllllIIl2.x = n / 2 - 185;
            this.currentScrollableElement = null;
            this.lIIIIllIIlIlIllIIIlIllIlI = lllIllIllIlIllIlIIllllIIl2;
        }
    }

    public static float getSmoothFloat(float f) {
        float f2 = f / (float)(Minecraft.debugFPS + 1);
        return Math.max(f2, 1.0f);
    }

    private CBModulePosition getModulePosition(AbstractModule cBModule) {
        for (CBModulePosition CBModulePosition : this.positions) {
            if (cBModule != CBModulePosition.module) continue;
            return CBModulePosition;
        }
        return null;
    }

    private void IlllIIIlIlllIllIlIIlllIlI(ScaledResolution scaledResolution, int n, int n2) {
        for (CBModulePosition CBModulePosition : this.positions) {
            if (CBModulePosition.module == null || CBModulePosition.module.getGuiAnchor() == null) continue;
            CBModulePosition.x = (float)n - CBModulePosition.module.getXTranslation();
            CBModulePosition.y = (float)n2 - CBModulePosition.module.getYTranslation();
        }
    }

    private void removePositionForModule(AbstractModule cBModule) {
        this.positions.removeIf(CBModulePosition -> CBModulePosition.module == cBModule);
    }

    static {
        IlIlllIIIIllIllllIllIIlIl = false;
    }

}
