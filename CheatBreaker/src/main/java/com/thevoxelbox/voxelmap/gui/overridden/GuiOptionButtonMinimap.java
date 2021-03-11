package com.thevoxelbox.voxelmap.gui.overridden;

import net.minecraft.client.gui.GuiButton;

public class GuiOptionButtonMinimap
		extends GuiButton {
	private final EnumOptionsMinimap enumOptions;

	public GuiOptionButtonMinimap(int par1, int par2, int par3, String par4Str) {
		this(par1, par2, par3, (EnumOptionsMinimap) null, par4Str);
	}

	public GuiOptionButtonMinimap(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
		this.enumOptions = null;
	}

	public GuiOptionButtonMinimap(int par1, int par2, int par3, EnumOptionsMinimap par4EnumOptions, String par5Str) {
		super(par1, par2, par3, 150, 20, par5Str);
		this.enumOptions = par4EnumOptions;
	}

	public EnumOptionsMinimap returnEnumOptions() {
		return this.enumOptions;
	}
}
