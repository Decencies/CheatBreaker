package com.cheatbreaker.client.module.type;

import com.cheatbreaker.client.config.Setting;
import com.cheatbreaker.client.event.type.GuiDrawEvent;
import com.cheatbreaker.client.module.AbstractModule;
import com.cheatbreaker.client.ui.module.CBGuiAnchor;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class CoordinatesModule extends AbstractModule {

    private final Setting mode;
    private final Setting showWhileTyping;
    private final Setting showCoordinates;
    private final Setting hideYCoordinate;
    private final Setting showDirection;
    private final Setting coordinatesColor;
    private final Setting customLine;
    private final Setting directionColor;

    public CoordinatesModule() {
        super("Coordinates");
        this.setDefaultAnchor(CBGuiAnchor.LEFT_TOP);
        this.setDefaultTranslations(0.0f, 0.0f);
        this.setDefaultState(false);
        new Setting(this, "label").setValue("General Options");
        this.showWhileTyping = new Setting(this, "Show While Typing").setValue(true);
        this.mode = new Setting(this, "Mode").setValue("Horizontal").acceptedValues("Horizontal", "Vertical");
        this.hideYCoordinate = new Setting(this, "Hide Y Coordinate").setValue(false);
        this.showCoordinates = new Setting(this, "Show Coordinates").setValue(true);
        this.showDirection = new Setting(this, "Direction").setValue(true);
        this.customLine = new Setting(this, "Custom Line").setValue("");
        new Setting(this, "label").setValue("Color Settings");
        this.coordinatesColor = new Setting(this, "Coordinates Color").setValue(-1).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.directionColor = new Setting(this, "Direction Color").setValue(-1).setMinMax(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.setPreviewLabel("(16, 65, 120) NW", 1.0f);
        this.addEvent(GuiDrawEvent.class, this::onRender);
    }

    public void onRender(GuiDrawEvent event) {
        if (!this.isRenderHud()) {
            return;
        }

        GL11.glPushMatrix();
        this.scaleAndTranslate(event.getResolution());
        int n = MathHelper.floor_double(this.minecraft.thePlayer.posX);
        int n2 = (int) this.minecraft.thePlayer.boundingBox.minY;
        int n3 = MathHelper.floor_double(this.minecraft.thePlayer.posZ);

        if (!this.minecraft.ingameGUI.getChatGUI().func_146241_e() || ((Boolean) this.showWhileTyping.getValue())) {
            int n4;
            String object;
            float f = 4;
            if (this.mode.getValue().equals("Horizontal")) {
                object = (Boolean) this.hideYCoordinate.getValue() ? (Boolean) this.showCoordinates.getValue() ? String.format("(%1$d, %2$d) ", n, n3) : "" : (Boolean) this.showCoordinates.getValue() ? String.format("(%1$d, %2$d, %3$d) ", n, n2, n3) : "";
                n4 = this.minecraft.fontRenderer.drawStringWithShadow(object, 0, 0, this.coordinatesColor.getColorValue());
            } else {
                n4 = 50;
                f = (Boolean) this.hideYCoordinate.getValue() ? 8.066038f * 1.1777778f : (float) 16;
                this.minecraft.fontRenderer.drawStringWithShadow("X: " + n, 0, 0, this.coordinatesColor.getColorValue());
                if (!((Boolean) this.hideYCoordinate.getValue())) {
                    this.minecraft.fontRenderer.drawStringWithShadow("Y: " + n2, 0, 12, this.coordinatesColor.getColorValue());
                }
                this.minecraft.fontRenderer.drawStringWithShadow("Z: " + n3, 0, (Boolean) this.hideYCoordinate.getValue() ? 12 : 24, this.coordinatesColor.getColorValue());
            }
            if (((Boolean)this.showDirection.getValue())) {
                String[] directions = new String[]{"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
                double d = MathHelper.wrapAngleTo180_double(this.minecraft.thePlayer.rotationYaw) + (double)180;
                d += 11.682692039868188 * (double)1.925926f;
                d %= 360;
                String string = directions[MathHelper.floor_double(d /= (double)45)];
                this.minecraft.fontRenderer.drawStringWithShadow(string, n4, (int) f - 4, this.directionColor.getColorValue());
                n4 += this.minecraft.fontRenderer.getStringWidth(string);
            }
            this.setDimensions(n4, (float) 18 + f);
        }
        GL11.glPopMatrix();
    }

}
