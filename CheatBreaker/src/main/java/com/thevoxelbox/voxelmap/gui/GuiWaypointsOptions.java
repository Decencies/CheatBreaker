package com.thevoxelbox.voxelmap.gui;

import com.thevoxelbox.voxelmap.MapSettingsManager;
import com.thevoxelbox.voxelmap.gui.overridden.EnumOptionsMinimap;
import com.thevoxelbox.voxelmap.gui.overridden.GuiOptionButtonMinimap;
import com.thevoxelbox.voxelmap.gui.overridden.GuiOptionSliderMinimap;
import com.thevoxelbox.voxelmap.gui.overridden.GuiScreenMinimap;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiWaypointsOptions
		extends GuiScreenMinimap {
	private static final EnumOptionsMinimap[] relevantOptions = {EnumOptionsMinimap.WAYPOINTDISTANCE,
	                                                             EnumOptionsMinimap.DEATHPOINTS};
	private final GuiScreen parent;
	private final MapSettingsManager options;
	protected String screenTitle = "Radar Options";

	public GuiWaypointsOptions(GuiScreen parent, MapSettingsManager options) {
		this.parent = parent;
		this.options = options;
	}

	public void initGui() {
		int var2 = 0;

		this.screenTitle = I18nUtils.getString("options.minimap.waypoints.title");
		for (int t = 0; t < relevantOptions.length; t++) {
			EnumOptionsMinimap option = relevantOptions[t];
			if (option.getEnumFloat()) {
				float distance = this.options.getOptionFloatValue(option);
				if (distance < 0.0F) {
					distance = 10001.0F;
				}
				distance = (distance - 50.0F) / 9951.0F;
				getButtonList().add(new GuiOptionSliderMinimap(option.returnEnumOrdinal(),
						getWidth() / 2 - 155 + var2 % 2 * 160, getHeight() / 6 + 24 * (var2 >> 1), option, distance,
						this.options));
			} else {
				GuiOptionButtonMinimap var7 = new GuiOptionButtonMinimap(option.returnEnumOrdinal(),
						getWidth() / 2 - 155 + var2 % 2 * 160, getHeight() / 6 + 24 * (var2 >> 1), option,
						this.options.getKeyText(option));

				getButtonList().add(var7);
			}
			var2++;
		}
		getButtonList()
				.add(new GuiButton(200, getWidth() / 2 - 100, getHeight() / 6 + 168, I18nUtils.getString("gui.done")));
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if ((par1GuiButton.id < 100) && ((par1GuiButton instanceof GuiOptionButtonMinimap))) {
				this.options.setOptionValue(((GuiOptionButtonMinimap) par1GuiButton).returnEnumOptions(), 1);
				par1GuiButton.displayString = this.options
						.getKeyText(EnumOptionsMinimap.getEnumOptions(par1GuiButton.id));
			}
			if (par1GuiButton.id == 200) {
				getMinecraft().displayGuiScreen(this.parent);
			}
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		super.drawMap();
		drawDefaultBackground();
		drawCenteredString(this.fontRendererObj, this.screenTitle, getWidth() / 2, 20, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
