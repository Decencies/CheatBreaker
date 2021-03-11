package com.cheatbreaker.client.module.type.keystrokes;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class KeystrokesModule
        extends AbstractModule {
    private final Setting showClicks;
    private final Setting showMovementKeys;
    private final Setting showSpacebar;
    public final Setting replaceNamesWithArrows;
    private final Setting textColor;
    private final Setting textColorPressed;
    private final Setting backgroundColor;
    private final Setting backgroundColorPressed;
    private final Setting boxSize;
    private final Setting gap;
    public final Setting fadeTime;
    private Key upKey;
    private Key leftKey;
    private Key rightKey;
    private Key downkey;
    private Key leftMouseKey;
    private Key rightMouseKey;
    private Key spaceBarKey;

    public KeystrokesModule() {
        super("Key Strokes");
        this.setDefaultAnchor(CBGuiAnchor.RIGHT_TOP);
        this.setDefaultTranslations(-70, 5);
        this.setDefaultState(false);
        this.showClicks = new Setting(this, "Show clicks").setValue(true);
        this.showMovementKeys = new Setting(this, "Show movement keys").setValue(true);
        this.showSpacebar = new Setting(this, "Show spacebar").setValue(false);
        this.replaceNamesWithArrows = new Setting(this, "Replace names with arrows").setValue(false);
        this.boxSize = new Setting(this, "Box size").setValue(18).setMinMax(10, 32);
        this.gap = new Setting(this, "Gap").setValue(1).setMinMax(1, 4);
        this.fadeTime = new Setting(this, "Fade Time").setValue(75).setMinMax(0, 100);
        this.textColor = new Setting(this, "Text Color").setValue(-1).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.textColorPressed = new Setting(this, "Text Color (Pressed)").setValue(-16777216).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.backgroundColor = new Setting(this, "Background Color").setValue(0x6F000000).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.backgroundColorPressed = new Setting(this, "Background Color (Pressed)").setValue(0x6FFFFFFF).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.initialize();
        this.setPreviewIcon(new ResourceLocation("client/icons/mods/wasd.png"), 55, 37);
        this.addEvent(GuiDrawEvent.class, this::onDraw);
    }

    private void onDraw(GuiDrawEvent drawEvent) {
        if (!this.isRenderHud()) {
            return;
        }
        GL11.glPushMatrix();
        this.scaleAndTranslate(drawEvent.getResolution());
        float f = 0.0f;
        float f2 = 0.0f;
        int textColor = this.textColor.getColorValue();
        int textColorPressed = this.textColorPressed.getColorValue();
        int backgroundColor = this.backgroundColor.getColorValue();
        int backgroundColorPressed = this.backgroundColorPressed.getColorValue();
        int gap = (Integer)this.gap.getValue();
        if ((Boolean) this.showMovementKeys.getValue()) {
            this.upKey.render(this.leftKey.getWidth() + gap, 0.0f, textColor, textColorPressed, backgroundColor, backgroundColorPressed);
            this.leftKey.render(0.0f, this.upKey.getHeight() + gap, textColor, textColorPressed, backgroundColor, backgroundColorPressed);
            this.downkey.render(this.leftKey.getWidth() + gap, this.upKey.getHeight() + gap, textColor, textColorPressed, backgroundColor, backgroundColorPressed);
            this.rightKey.render(
                    this.leftKey.getWidth() + this.downkey.getWidth() + gap*2, this.upKey.getHeight() + gap, textColor, textColorPressed, backgroundColor, backgroundColorPressed);
            //f = this.leftKey.getWidth() + this.downkey.getWidth() + this.rightKey.getWidth() + gap;
            f2 += this.upKey.getHeight() + gap*2 + this.downkey.getHeight();
        }
        if ((Boolean) this.showClicks.getValue()) {
            this.leftMouseKey.render(0.0f, f2, textColor, textColorPressed, backgroundColor, backgroundColorPressed);
            this.rightMouseKey.render(this.leftMouseKey.getWidth() + gap, f2, textColor, textColorPressed, backgroundColor, backgroundColorPressed);
            f = this.leftMouseKey.getWidth() + this.rightMouseKey.getWidth() + gap;
            f2 += this.rightMouseKey.getHeight() + gap;
        }
        if ((Boolean) this.showSpacebar.getValue()) {
            this.spaceBarKey.render(0.0f, f2, textColor, textColorPressed, backgroundColor, backgroundColorPressed);
            f2 += this.spaceBarKey.getHeight() + gap;
        }
        this.setDimensions(f, f2 < (float)18 ? (float)18 : f2 + 2.0f);
        GL11.glPopMatrix();
    }

    public void initialize() {

        int n = this.minecraft.gameSettings.keyBindForward.getKeyCode();
        int n2 = this.minecraft.gameSettings.keyBindLeft.getKeyCode();
        int n3 = this.minecraft.gameSettings.keyBindBack.getKeyCode();
        int n4 = this.minecraft.gameSettings.keyBindRight.getKeyCode();
        int boxSize = (Integer)this.boxSize.getValue();
        int gap = (Integer)this.gap.getValue();
        float fadeTime = (Integer) this.fadeTime.getValue();
        String w = Keyboard.getKeyName(n);
        String a = Keyboard.getKeyName(n2);
        String s = Keyboard.getKeyName(n3);
        String d = Keyboard.getKeyName(n4);
        float upKeyWidth = (float)this.minecraft.fontRenderer.getStringWidth(w) * (Float) this.scale.getValue();
        float leftKeyWidth = (float)this.minecraft.fontRenderer.getStringWidth(a) * (Float) this.scale.getValue();
        float downKeyWidth = (float)this.minecraft.fontRenderer.getStringWidth(s) * (Float) this.scale.getValue();
        float rightKeyWidth = (float)this.minecraft.fontRenderer.getStringWidth(d) * (Float) this.scale.getValue();
        int jump = this.minecraft.gameSettings.keyBindJump.getKeyCode();
        int attack = this.minecraft.gameSettings.keyBindAttack.getKeyCode();
        int use = this.minecraft.gameSettings.keyBindUseItem.getKeyCode();
        boolean bl = (Boolean)this.replaceNamesWithArrows.getValue();
        this.upKey = new Key(bl ? "▲" : (upKeyWidth > (float)boxSize ? w.substring(0, 1) : w), n, boxSize, boxSize, fadeTime);
        this.leftKey = new Key(bl ? "◀" : (leftKeyWidth > (float)boxSize ? a.substring(0, 1) : a), n2, boxSize, boxSize, fadeTime);
        this.downkey = new Key(bl ? "▼" : (downKeyWidth > (float)boxSize ? s.substring(0, 1) : s), n3, boxSize, boxSize, fadeTime);
        this.rightKey = new Key(bl ? "▶" : (rightKeyWidth > (float)boxSize ? d.substring(0, 1) : d), n4, boxSize, boxSize, fadeTime);
        float f5 = (this.leftKey.getWidth() + this.downkey.getWidth() + this.rightKey.getWidth() + gap) / 2.0f;
        this.leftMouseKey = new Key(boxSize < 14 ? "L" : "LMB", attack, f5, boxSize, fadeTime);
        this.rightMouseKey = new Key(boxSize < 14 ? "R" : "RMB", use, f5, boxSize, fadeTime);
        this.spaceBarKey = new Key(Keyboard.getKeyName(jump), jump, this.leftKey.getWidth() + this.downkey.getWidth() + this.rightKey.getWidth() + gap*2, (float)boxSize / 2, fadeTime);
    }
}
