package com.thevoxelbox.voxelmap.gui;

import com.thevoxelbox.voxelmap.RadarSettingsManager;
import com.thevoxelbox.voxelmap.gui.overridden.EnumOptionsMinimap;
import com.thevoxelbox.voxelmap.gui.overridden.GuiOptionButtonMinimap;
import com.thevoxelbox.voxelmap.gui.overridden.GuiScreenMinimap;
import com.thevoxelbox.voxelmap.interfaces.IVoxelMap;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiRadarOptions
		extends GuiScreenMinimap {
	private static final EnumOptionsMinimap[] relevantOptions = {EnumOptionsMinimap.SHOWRADAR,
	                                                             EnumOptionsMinimap.RANDOMOBS,
	                                                             EnumOptionsMinimap.SHOWHOSTILES,
	                                                             EnumOptionsMinimap.SHOWNEUTRALS,
	                                                             EnumOptionsMinimap.SHOWPLAYERS,
	                                                             EnumOptionsMinimap.SHOWPLAYERNAMES,
	                                                             EnumOptionsMinimap.SHOWPLAYERHELMETS,
	                                                             EnumOptionsMinimap.SHOWMOBHELMETS,
	                                                             EnumOptionsMinimap.RADARFILTERING,
	                                                             EnumOptionsMinimap.RADAROUTLINES};
	private final GuiScreen parent;
	private final RadarSettingsManager options;
	protected String screenTitle = "Radar Options";
	private IVoxelMap master;

	public GuiRadarOptions(GuiScreen parent, IVoxelMap master) {
		this.parent = parent;
		this.options = master.getRadarOptions();
	}

	public void initGui() {
		int var2 = 0;

		this.screenTitle = I18nUtils.getString("options.minimap.radar.title");
		for (int t = 0; t < relevantOptions.length; t++) {
			EnumOptionsMinimap option = relevantOptions[t];

			GuiOptionButtonMinimap var7 = new GuiOptionButtonMinimap(option.returnEnumOrdinal(),
					getWidth() / 2 - 155 + var2 % 2 * 160, getHeight() / 6 + 24 * (var2 >> 1), option,
					this.options.getKeyText(option));

			getButtonList().add(var7);

			var2++;
		}
		for (Object buttonObj : getButtonList()) {
			if ((buttonObj instanceof GuiOptionButtonMinimap)) {
				GuiOptionButtonMinimap button = (GuiOptionButtonMinimap) buttonObj;
				if (!button.returnEnumOptions().equals(EnumOptionsMinimap.SHOWRADAR)) {
					button.enabled = this.options.show;
				}
				if ((button.returnEnumOptions().equals(EnumOptionsMinimap.SHOWPLAYERHELMETS)) ||
				    (button.returnEnumOptions().equals(EnumOptionsMinimap.SHOWPLAYERNAMES))) {
					button.enabled = ((button.enabled) && (this.options.showPlayers));
				} else if (button.returnEnumOptions().equals(EnumOptionsMinimap.SHOWMOBHELMETS)) {
					button.enabled = ((button.enabled) && ((this.options.showNeutrals) || (this.options
							                                                                       .showHostiles)));
				}
			}
		}
		getButtonList().add(new GuiButton(101, getWidth() / 2 - 155, getHeight() / 6 + 144 - 6, 150, 20,
				I18nUtils.getString("options.minimap.radar.selectmobs")));
		getButtonList()
				.add(new GuiButton(200, getWidth() / 2 - 100, getHeight() / 6 + 168, I18nUtils.getString("gui.done")));
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if ((par1GuiButton.id < 100) && ((par1GuiButton instanceof GuiOptionButtonMinimap))) {
				this.options.setOptionValue(((GuiOptionButtonMinimap) par1GuiButton).returnEnumOptions(), 1);
				par1GuiButton.displayString = this.options
						.getKeyText(EnumOptionsMinimap.getEnumOptions(par1GuiButton.id));
				for (Object buttonObj : getButtonList()) {
					if ((buttonObj instanceof GuiOptionButtonMinimap)) {
						GuiOptionButtonMinimap button = (GuiOptionButtonMinimap) buttonObj;
						if (!button.returnEnumOptions().equals(EnumOptionsMinimap.SHOWRADAR)) {
							button.enabled = this.options.show;
						}
						if ((button.returnEnumOptions() == EnumOptionsMinimap.SHOWPLAYERHELMETS) ||
						    (button.returnEnumOptions() == EnumOptionsMinimap.SHOWPLAYERNAMES)) {
							button.enabled = ((button.enabled) && (this.options.showPlayers));
						} else if (button.returnEnumOptions().equals(EnumOptionsMinimap.SHOWMOBHELMETS)) {
							button.enabled = ((button.enabled) &&
							                  ((this.options.showNeutrals) || (this.options.showHostiles)));
						}
					} else if ((buttonObj instanceof GuiButton)) {
						GuiButton button = (GuiButton) buttonObj;
						if (button.id == 101) {
							button.enabled = this.options.show;
						}
					}
				}
			}
			if (par1GuiButton.id == 101) {
				getMinecraft().displayGuiScreen(new GuiMobs(this, this.options));
			}
			if (par1GuiButton.id == 200) {
				getMinecraft().displayGuiScreen(this.parent);
			}
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		super.drawMap();
		drawDefaultBackground();
		drawCenteredString(getFontRenderer(), this.screenTitle, getWidth() / 2, 20, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
