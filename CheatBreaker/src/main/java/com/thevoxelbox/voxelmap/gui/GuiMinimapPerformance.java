package com.thevoxelbox.voxelmap.gui;

import com.thevoxelbox.voxelmap.MapSettingsManager;
import com.thevoxelbox.voxelmap.gui.overridden.EnumOptionsMinimap;
import com.thevoxelbox.voxelmap.gui.overridden.GuiOptionButtonMinimap;
import com.thevoxelbox.voxelmap.gui.overridden.GuiScreenMinimap;
import com.thevoxelbox.voxelmap.interfaces.IVoxelMap;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiMinimapPerformance
		extends GuiScreenMinimap {
	private static final EnumOptionsMinimap[] relevantOptions = {EnumOptionsMinimap.LIGHTING,
	                                                             EnumOptionsMinimap.TERRAIN,
	                                                             EnumOptionsMinimap.WATERTRANSPARENCY,
	                                                             EnumOptionsMinimap.BLOCKTRANSPARENCY,
	                                                             EnumOptionsMinimap.BIOMES,
	                                                             EnumOptionsMinimap.FILTERING,
	                                                             EnumOptionsMinimap.CHUNKGRID,
	                                                             EnumOptionsMinimap.BIOMEOVERLAY};
	protected String screenTitle = "Details / Performance";
	private IVoxelMap master;
	private GuiScreen parentScreen;
	private MapSettingsManager options;
	private int buttonId = -1;

	public GuiMinimapPerformance(GuiScreen par1GuiScreen, IVoxelMap master) {
		this.parentScreen = par1GuiScreen;
		this.options = master.getMapOptions();
	}

	private int getLeftBorder() {
		return getWidth() / 2 - 155;
	}

	public void initGui() {
		this.screenTitle = I18nUtils.getString("options.minimap.detailsperformance");

		int var1 = getLeftBorder();
		int var2 = 0;
		for (int t = 0; t < relevantOptions.length; t++) {
			EnumOptionsMinimap option = relevantOptions[t];

			String text = this.options.getKeyText(option);
			if (((option == EnumOptionsMinimap.WATERTRANSPARENCY) || (option == EnumOptionsMinimap
					.BLOCKTRANSPARENCY) ||
			     (option == EnumOptionsMinimap.BIOMES)) && (!this.options.multicore) &&
			    (this.options.getOptionBooleanValue(option))) {
				text = "§c" + text;
			}
			GuiOptionButtonMinimap var7 = new GuiOptionButtonMinimap(option.returnEnumOrdinal(), var1 + var2 % 2 * 160,
					getHeight() / 6 + 24 * (var2 >> 1), option, text);

			this.buttonList.add(var7);

			var2++;
		}
		this.buttonList
				.add(new GuiButton(200, getWidth() / 2 - 100, getHeight() / 6 + 168, I18nUtils.getString("gui.done")));
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if ((par1GuiButton.id < 100) && ((par1GuiButton instanceof GuiOptionButtonMinimap))) {
			this.options.setOptionValue(((GuiOptionButtonMinimap) par1GuiButton).returnEnumOptions(), 1);
			String perfBomb = "";
			if (((par1GuiButton.id == EnumOptionsMinimap.WATERTRANSPARENCY.ordinal()) ||
			     (par1GuiButton.id == EnumOptionsMinimap.BLOCKTRANSPARENCY.ordinal()) ||
			     (par1GuiButton.id == EnumOptionsMinimap.BIOMES.ordinal())) && (!this.options.multicore) &&
			    (this.options.getOptionBooleanValue(EnumOptionsMinimap.getEnumOptions(par1GuiButton.id)))) {
				perfBomb = "§c";
			}
			par1GuiButton.displayString = (perfBomb + this.options
					.getKeyText(EnumOptionsMinimap.getEnumOptions(par1GuiButton.id)));
		}
		if (par1GuiButton.id == 200) {
			getMinecraft().displayGuiScreen(this.parentScreen);
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		super.drawMap();
		drawDefaultBackground();
		drawCenteredString(getFontRenderer(), this.screenTitle, getWidth() / 2, 20, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
