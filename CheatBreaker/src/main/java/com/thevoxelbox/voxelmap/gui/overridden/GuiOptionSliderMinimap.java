package com.thevoxelbox.voxelmap.gui.overridden;

import com.thevoxelbox.voxelmap.MapSettingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

public class GuiOptionSliderMinimap
		extends GuiButton {
	public float sliderValue = 1.0F;
	public boolean dragging = false;
	private MapSettingsManager options;
	private EnumOptionsMinimap idFloat = null;

	public GuiOptionSliderMinimap(int par1, int par2, int par3, EnumOptionsMinimap par4EnumOptions, float par6,
	                              MapSettingsManager options) {
		super(par1, par2, par3, 150, 20, "");
		this.options = options;
		this.idFloat = par4EnumOptions;
		this.sliderValue = par6;
		this.displayString = this.options.getKeyText(par4EnumOptions);
	}

	public int getHoverState(boolean par1) {
		return 0;
	}

	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
		if (this.field_146125_m) {
			if (this.dragging) {
				this.sliderValue = ((par2 - (this.field_146128_h + 4)) / (this.field_146120_f - 8));
				if (this.sliderValue < 0.0F) {
					this.sliderValue = 0.0F;
				}
				if (this.sliderValue > 1.0F) {
					this.sliderValue = 1.0F;
				}
				this.options.setOptionFloatValue(this.idFloat, this.sliderValue);
				this.displayString = this.options.getKeyText(this.idFloat);
			}
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			drawTexturedModalRect(this.field_146128_h + (int) (this.sliderValue * (this.field_146120_f - 8)), this.field_146129_i, 0, 66, 4, 20);
			drawTexturedModalRect(this.field_146128_h + (int) (this.sliderValue * (this.field_146120_f - 8)) + 4, this.field_146129_i, 196, 66, 4, 20);
		}
	}

	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
		if (super.mousePressed(par1Minecraft, par2, par3)) {
			this.sliderValue = ((par2 - (this.field_146128_h + 4)) / (this.field_146120_f - 8));
			if (this.sliderValue < 0.0F) {
				this.sliderValue = 0.0F;
			}
			if (this.sliderValue > 1.0F) {
				this.sliderValue = 1.0F;
			}
			this.options.setOptionFloatValue(this.idFloat, this.sliderValue);
			this.displayString = this.options.getKeyText(this.idFloat);
			this.dragging = true;
			return true;
		}
		return false;
	}

	public void mouseReleased(int par1, int par2) {
		this.dragging = false;
	}
}
