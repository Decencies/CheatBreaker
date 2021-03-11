package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.event.type.RenderPreviewEvent;
import com.cheatbreaker.client.event.type.TickEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import com.cheatbreaker.client.ui.module.CBPositionEnum;
import com.cheatbreaker.client.ui.util.RenderUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.HashMap;

public class PotionStatusModule extends AbstractModule {

    public final Setting showInInventory;
    private final Setting showWhileTying;
    private final Setting showEffectName;
    private final Setting colorOptionsLabel;
    private final Setting nameColor;
    private final Setting durationColor;
    private final Setting blink;
    private final Setting blinkDuration;
    private final ResourceLocation location = new ResourceLocation("textures/gui/container/inventory.png");
    private int ticks = 0;

    public PotionStatusModule() {
        super("Potion Effects");
        this.setDefaultState(false);
        this.setDefaultAnchor(CBGuiAnchor.LEFT_MIDDLE);

        new Setting(this, "label").setValue("General Options");
        {
            this.showWhileTying = new Setting(this, "Show While Typing").setValue(true);
            this.showEffectName = new Setting(this, "Effect Name").setValue(true);
            this.showInInventory = new Setting(this, "Show Potion info in inventory").setValue(false);
        }
        new Setting(this, "label").setValue("Blink Options");
        {
            this.blink = new Setting(this, "Blink").setValue(true);
            this.blinkDuration = new Setting(this, "Blink Duration").setValue(10).setMinMax(2, 20);
            this.colorOptionsLabel = new Setting(this, "label").setValue("Color Options");
            this.nameColor = new Setting(this, "Name Color").setValue(-1).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.durationColor = new Setting(this, "Duration Color").setValue(-1).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        this.setPreviewIcon(new ResourceLocation("client/icons/mods/speed_icon.png"), 28, 28);

        this.addEvent(TickEvent.class, this::onTick);
        this.addEvent(RenderPreviewEvent.class, this::renderPreview);
        this.addEvent(GuiDrawEvent.class, this::renderReal);
    }

    private void onTick(TickEvent cBTickEvent) {
        ++this.ticks;
    }

    private void renderReal(GuiDrawEvent guiDrawEvent) {
        GL11.glPushMatrix();
        if ((Boolean) this.showWhileTying.getValue() || !this.minecraft.ingameGUI.getChatGUI().func_146241_e()) {
            GL11.glPushMatrix();
            this.scaleAndTranslate(guiDrawEvent.getResolution());
            CBPositionEnum position = this.getPosition();
            Collection<PotionEffect> collection = this.minecraft.thePlayer.getActivePotionEffects();
            if (collection.isEmpty()) {
                GL11.glPopMatrix();
                GL11.glPopMatrix();
                return;
            }
            int n = 0;
            int n2 = 0;
            int n3 = 22;
            for (PotionEffect potionEffect : collection) {
                Potion potion;
                String string;
                boolean shouldBlink = this.shouldBlink(potionEffect.getDuration());
                int n4 = 0;
                if ((Boolean) this.showEffectName.getValue()) {
                    string = I18n.format(potionEffect.getEffectName()) + this.getLevelName(potionEffect.getAmplifier());
                    n4 = this.minecraft.fontRenderer.getStringWidth(string) + 20;
                    if (position == CBPositionEnum.RIGHT) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", (int) width - n4, n, this.nameColor.getColorValue());
                    } else if (position == CBPositionEnum.LEFT) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", 20, n, this.nameColor.getColorValue());
                    } else if (position == CBPositionEnum.CENTER) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", (int) width / 2 - (n4 / 2) + 20, n, this.nameColor.getColorValue());
                    }
                    if (n4 > n2) {
                        n2 = n4;
                    }
                }
                string = Potion.getDurationString(potionEffect);
                int n5 = this.minecraft.fontRenderer.getStringWidth(string) + 20;
                if (shouldBlink) {
                    if (position == CBPositionEnum.RIGHT) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", (int) width - n5, n + ((Boolean) this.showEffectName.getValue() ? 10 : 5), this.durationColor.getColorValue());
                    } else if (position == CBPositionEnum.LEFT) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", 20, n + ((Boolean) this.showEffectName.getValue() ? 10 : 5), this.durationColor.getColorValue());
                    } else if (position == CBPositionEnum.CENTER) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", (int) width / 2 - (n5 / 2) + 20, n + ((Boolean) this.showEffectName.getValue() ? 10 : 5), this.durationColor.getColorValue());
                    }
                }
                if ((potion = Potion.potionTypes[potionEffect.getPotionID()]).hasStatusIcon()) {
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    this.minecraft.getTextureManager().bindTexture(this.location);
                    int n6 = potion.getStatusIconIndex();
                    if (position == CBPositionEnum.RIGHT) {
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(width - (float) 20, (float) n, (float) (n6 % 8 * 18), (float) (198 + n6 / 8 * 18), 18, 18);
                    } else if (position == CBPositionEnum.LEFT) {
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(0.0f, (float) n, (float) (n6 % 8 * 18), (float) (198 + n6 / 8 * 18), 18, 18);
                    } else if (position == CBPositionEnum.CENTER) {
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(width / 2.0f - (float) (n4 / 2), (float) n, (float) (n6 % 8 * 18), (float) (198 + n6 / 8 * 18), 18, 18);
                    }
                }
                if (n5 > n2) {
                    n2 = n5;
                }
                n += n3;
            }
            this.setDimensions(n2, n);
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    private void renderPreview(RenderPreviewEvent renderPreviewEvent) {
        if (!this.isRenderHud()) {
            return;
        }
        GL11.glPushMatrix();
        Collection<PotionEffect> collection = this.minecraft.thePlayer.getActivePotionEffects();
        if (collection.isEmpty()) {
            GL11.glPushMatrix();
            this.scaleAndTranslate(renderPreviewEvent.getResolution());
            HashMap<Integer, PotionEffect> hashMap = new HashMap<>();
            PotionEffect fireResistance = new PotionEffect(Potion.fireResistance.id, 1200, 3);
            PotionEffect speed = new PotionEffect(Potion.moveSpeed.id, 30, 3);
            hashMap.put(fireResistance.getPotionID(), fireResistance);
            hashMap.put(speed.getPotionID(), speed);
            collection = hashMap.values();
            CBPositionEnum position = this.getPosition();
            int n = 0;
            int n2 = 0;
            int n3 = 22;
            for (PotionEffect potionEffect : collection) {
                Potion potion;
                String string;
                boolean shouldBlink = this.shouldBlink(potionEffect.getDuration());
                int n4 = 0;
                if ((Boolean) this.showEffectName.getValue()) {
                    string = I18n.format(potionEffect.getEffectName()) + this.getLevelName(potionEffect.getAmplifier());
                    n4 = this.minecraft.fontRenderer.getStringWidth(string) + 20;
                    if (position == CBPositionEnum.RIGHT) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", (int) width - n4, n, this.nameColor.getColorValue());
                    } else if (position == CBPositionEnum.LEFT) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", 20, n, this.nameColor.getColorValue());
                    } else if (position == CBPositionEnum.CENTER) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", (int) width / 2 - (n4 / 2) + 20, n, this.nameColor.getColorValue());
                    }
                    if (n4 > n2) {
                        n2 = n4;
                    }
                }
                string = Potion.getDurationString(potionEffect);
                int n5 = this.minecraft.fontRenderer.getStringWidth(string) + 20;
                if (shouldBlink) {
                    if (position == CBPositionEnum.RIGHT) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", (int) width - n5, n + ((Boolean) this.showEffectName.getValue() ? 10 : 5), this.durationColor.getColorValue());
                    } else if (position == CBPositionEnum.LEFT) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", 20, n + ((Boolean) this.showEffectName.getValue() ? 10 : 5), this.durationColor.getColorValue());
                    } else if (position == CBPositionEnum.CENTER) {
                        this.minecraft.fontRenderer.drawStringWithShadow(string + "§r", (int) width / 2 - (n5 / 2) + 20, n + ((Boolean) this.showEffectName.getValue() ? 10 : 5), this.durationColor.getColorValue());
                    }
                }
                if ((potion = Potion.potionTypes[potionEffect.getPotionID()]).hasStatusIcon()) {
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    this.minecraft.getTextureManager().bindTexture(this.location);
                    int n6 = potion.getStatusIconIndex();
                    if (position == CBPositionEnum.RIGHT) {
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(width - (float) 20, (float) n, (float) (n6 % 8 * 18), (float) (198 + n6 / 8 * 18), 18, 18);
                    } else if (position == CBPositionEnum.LEFT) {
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(0.0f, (float) n, (float) (n6 % 8 * 18), (float) (198 + n6 / 8 * 18), 18, 18);
                    } else if (position == CBPositionEnum.CENTER) {
                        RenderUtil.lIIIIlIIllIIlIIlIIIlIIllI(width / 2.0f - (float) (n4 / 2), (float) n, (float) (n6 % 8 * 18), (float) (198 + n6 / 8 * 18), 18, 18);
                    }
                }
                if (n5 > n2) {
                    n2 = n5;
                }
                n += n3;
            }
            this.setDimensions(n2, n);
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    private boolean shouldBlink(float f) {
        if ((Boolean) this.blink.getValue() && f <= (float) ((Integer) this.blinkDuration.getValue() * 22)) {
            if (this.ticks > 20) {
                this.ticks = 0;
            }
            return this.ticks <= 10;
        }
        return true;
    }

    private String getLevelName(int level) {
        switch (level) {
            case 1: {
                return " II";
            }
            case 2: {
                return " III";
            }
            case 3: {
                return " IV";
            }
            case 4: {
                return " V";
            }
            case 5: {
                return " VI";
            }
            case 6: {
                return " VII";
            }
            case 7: {
                return " VIII";
            }
            case 8: {
                return " IX";
            }
            case 9: {
                return " X";
            }
        }
        if (level > 9) {
            return " " + level + 1;
        }
        return "";
    }

}
