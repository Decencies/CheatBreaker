package com.thevoxelbox.voxelmap.gui;

import com.thevoxelbox.voxelmap.RadarSettingsManager;
import com.thevoxelbox.voxelmap.gui.overridden.GuiScreenMinimap;
import com.thevoxelbox.voxelmap.util.CustomMob;
import com.thevoxelbox.voxelmap.util.CustomMobsManager;
import com.thevoxelbox.voxelmap.util.EnumMobs;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiMobs
		extends GuiScreenMinimap {
	protected final RadarSettingsManager options;
	private final GuiScreen parentScreen;
	protected String screenTitle = "Select Mobs";
	protected String selectedMobName = null;
	private GuiSlotMobs mobsList;
	private GuiButton buttonEnable;
	private GuiButton buttonDisable;
	private String tooltip = null;

	public GuiMobs(GuiScreen parentScreen, RadarSettingsManager options) {
		this.parentScreen = parentScreen;
		this.options = options;
	}

	static String setTooltip(GuiMobs par0GuiWaypoints, String par1Str) {
		return par0GuiWaypoints.tooltip = par1Str;
	}

	public void initGui() {
		int var2 = 0;
		this.screenTitle = I18nUtils.getString("options.minimap.mobs.title");

		this.mobsList = new GuiSlotMobs(this);
		this.mobsList.func_148134_d(7, 8);

		getButtonList().add(this.buttonEnable = new GuiButton(-1, getWidth() / 2 - 154, getHeight() - 28, 100, 20,
				I18nUtils.getString("options.minimap.mobs.enable")));
		getButtonList().add(this.buttonDisable = new GuiButton(-2, getWidth() / 2 - 50, getHeight() - 28, 100, 20,
				I18nUtils.getString("options.minimap.mobs.disable")));
		getButtonList().add(new GuiButton(65336, getWidth() / 2 + 4 + 50, getHeight() - 28, 100, 20,
				I18nUtils.getString("gui.done")));

		boolean isSomethingSelected = this.selectedMobName != null;
		this.buttonEnable.enabled = isSomethingSelected;
		this.buttonDisable.enabled = isSomethingSelected;
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == -1) {
				setMobEnabled(this.selectedMobName, true);
			}
			if (par1GuiButton.id == -2) {
				setMobEnabled(this.selectedMobName, false);
			}
			if (par1GuiButton.id == 65336) {
				getMinecraft().displayGuiScreen(this.parentScreen);
			}
		}
	}

	protected void setSelectedMob(String mob) {
		this.selectedMobName = mob;
	}

	private boolean isMobEnabled(String selectedMobName2) {
		EnumMobs mob = EnumMobs.getMobByName(this.selectedMobName);
		if (mob != null) {
			return mob.enabled;
		}
		CustomMob customMob = CustomMobsManager.getCustomMobByName(this.selectedMobName);
		if (customMob != null) {
			return customMob.enabled;
		}
		return false;
	}

	private void setMobEnabled(String selectedMobName, boolean enabled) {
		for (EnumMobs mob : EnumMobs.values()) {
			if (mob.name.equals(selectedMobName)) {
				mob.enabled = enabled;
			}
		}
		for (CustomMob mob : CustomMobsManager.mobs) {
			if (mob.name.equals(selectedMobName)) {
				mob.enabled = enabled;
			}
		}
	}

	protected void toggleMobVisibility() {
		EnumMobs mob = EnumMobs.getMobByName(this.selectedMobName);
		if (mob != null) {
			setMobEnabled(this.selectedMobName, !mob.enabled);
		} else {
			CustomMob customMob = CustomMobsManager.getCustomMobByName(this.selectedMobName);
			if (customMob != null) {
				setMobEnabled(this.selectedMobName, !customMob.enabled);
			}
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		super.drawMap();
		this.tooltip = null;
		this.mobsList.func_148128_a(par1, par2, par3);
		drawCenteredString(getFontRenderer(), this.screenTitle, getWidth() / 2, 20, 16777215);
		boolean isSomethingSelected = this.selectedMobName != null;
		this.buttonEnable.enabled = ((isSomethingSelected) && (!isMobEnabled(this.selectedMobName)));
		this.buttonDisable.enabled = ((isSomethingSelected) && (isMobEnabled(this.selectedMobName)));
		super.drawScreen(par1, par2, par3);
		if (this.tooltip != null) {
			drawTooltip(this.tooltip, par1, par2);
		}
	}

	protected void drawTooltip(String par1Str, int par2, int par3) {
		if (par1Str != null) {
			int var4 = par2 + 12;
			int var5 = par3 - 12;
			int var6 = getFontRenderer().getStringWidth(par1Str);
			drawGradientRect(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
			getFontRenderer().drawStringWithShadow(par1Str, var4, var5, -1);
		}
	}
}
