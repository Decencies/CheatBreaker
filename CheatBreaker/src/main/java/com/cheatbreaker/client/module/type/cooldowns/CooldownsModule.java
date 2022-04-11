package com.cheatbreaker.client.module.type.cooldowns;

import com.cheatbreaker.client.CheatBreaker;
import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.event.type.RenderPreviewEvent;
import com.cheatbreaker.client.event.type.TickEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import com.cheatbreaker.client.ui.module.CBModulePlaceGui;
import com.cheatbreaker.client.ui.module.CBModulesGui;
import com.google.common.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class CooldownsModule extends AbstractModule {

    private static final List<CooldownRenderer> real = new ArrayList<>();
    public final Setting colorTheme;
    private final Setting listMode;
    private final Setting coloredColor;
    private final List<CooldownRenderer> fake = new ArrayList<>();

    public CooldownsModule() {
        super("Cooldowns");
        this.setDefaultAnchor(CBGuiAnchor.MIDDLE_TOP);
        this.setDefaultTranslations(0.0f, 5);
        this.colorTheme = new Setting(this, "Color Theme").setValue("Bright").acceptedValues("Bright", "Dark", "Colored");
        this.listMode = new Setting(this, "List Mode").setValue("horizontal").acceptedValues("vertical", "horizontal");
        this.coloredColor = new Setting(this, "Colored color").setValue(-1).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.addEvent(TickEvent.class, this::onTick);
        this.addEvent(RenderPreviewEvent.class, this::renderPreview);
        this.addEvent(GuiDrawEvent.class, this::renderReal);
        this.setDefaultState(true);
    }

    public static void lIIIIlIIllIIlIIlIIIlIIllI(String name, long duration, int itemId) {
        for (CooldownRenderer renderer : real) {
            if (!renderer.IlllIIIlIlllIllIlIIlllIlI().equalsIgnoreCase(name) || renderer.IIIIllIIllIIIIllIllIIIlIl() != itemId)
                continue;
            renderer.lIIIIIIIIIlIllIIllIlIIlIl();
            renderer.lIIIIlIIllIIlIIlIIIlIIllI(duration);
            return;
        }
        real.add(new CooldownRenderer(name, itemId, duration));
    }

    public void onTick(TickEvent cBTickEvent) {
        if (!real.isEmpty()) {
            real.removeIf(CooldownRenderer::isTimeOver);
        }
        if (!fake.isEmpty()) {
            fake.removeIf(CooldownRenderer::isTimeOver);
        }
    }

    public void renderPreview(GuiDrawEvent guiDrawEvent) {
        if (!this.isRenderHud()) {
            return;
        }
        if (real.isEmpty()) {
            GL11.glPushMatrix();
            if (this.fake.isEmpty()) {
                fake.add(new CooldownRenderer("CombatTag", 283, 30000L));
                fake.add(new CooldownRenderer("EnderPearl", 368, 12000L));
            }
            this.scaleAndTranslate(guiDrawEvent.getResolution());
            float f = 1.0f / CheatBreaker.getInstance().getScaleFactor();
            GL11.glScalef(f, f, f);
            boolean bl = ((String) listMode.getValue()).equalsIgnoreCase("vertical");
            int n = 36;
            int n2 = 36;
            int n3 = bl ? n : fake.size() * n;
            int n4 = bl ? fake.size() * n2 : n2;
            this.setDimensions((int) ((float) n3 * f), (int) ((float) n4 * f));
            for (int i = 0; i < fake.size(); ++i) {
                CooldownRenderer cooldownRenderer2 = fake.get(i);
                if (((String) listMode.getValue()).equalsIgnoreCase("vertical")) {
                    cooldownRenderer2.lIIIIlIIllIIlIIlIIIlIIllI(this.colorTheme, this.width / 2.0f - (float) (n / 2), i * n2, this.coloredColor.getColorValue());
                    continue;
                }
                cooldownRenderer2.lIIIIlIIllIIlIIlIIIlIIllI(this.colorTheme, i * n, 0.0f, this.coloredColor.getColorValue());
            }
            GL11.glPopMatrix();
        }
    }

    public void renderReal(GuiDrawEvent guiDrawEvent) {
        if (!this.isRenderHud()) {
            return;
        }
        GL11.glPushMatrix();
        if (real.size() > 0) {
            this.scaleAndTranslate(guiDrawEvent.getResolution());
            float f = 1.0f / CheatBreaker.getInstance().getScaleFactor();
            GL11.glScalef(f, f, f);
            boolean bl = ((String) this.listMode.getValue()).equalsIgnoreCase("vertical");
            int n = 36;
            int n2 = 36;
            int n3 = bl ? n : real.size() * n;
            int n4 = bl ? real.size() * n2 : n2;
            this.setDimensions((int) ((float) n3 * f), (int) ((float) n4 * f));
            for (int i = 0; i < real.size(); ++i) {
                CooldownRenderer renderer = real.get(i);
                if (((String) this.listMode.getValue()).equalsIgnoreCase("vertical")) {
                    renderer.lIIIIlIIllIIlIIlIIIlIIllI(this.colorTheme, this.width / 2.0f - (float) (n / 2), i * n2, this.coloredColor.getColorValue());
                    continue;
                }
                renderer.lIIIIlIIllIIlIIlIIIlIIllI(this.colorTheme, i * n, 0.0f, this.coloredColor.getColorValue());
            }
        } else if (!(this.minecraft.currentScreen instanceof CBModulesGui) && !(this.minecraft.currentScreen instanceof CBModulePlaceGui)) {
            this.setDimensions(50, 24);
            this.scaleAndTranslate(guiDrawEvent.getResolution());
        }
        GL11.glPopMatrix();
    }

}
