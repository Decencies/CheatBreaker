package com.thevoxelbox.voxelmap;

import com.thevoxelbox.voxelmap.gui.overridden.EnumOptionsMinimap;
import com.thevoxelbox.voxelmap.util.CustomMob;
import com.thevoxelbox.voxelmap.util.CustomMobsManager;
import com.thevoxelbox.voxelmap.util.EnumMobs;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import net.minecraft.client.Minecraft;

public class RadarSettingsManager {
	public Minecraft game;
	public boolean show = true;
	public boolean showHostiles = true;
	public boolean showPlayers = true;
	public boolean showNeutrals = false;
	public boolean showPlayerNames = true;
	public boolean outlines = true;
	public boolean filtering = true;
	public boolean showHelmetsPlayers = true;
	public boolean showHelmetsMobs = true;
	public boolean randomobs = true;
	float fontScale = 1.0F;
	private boolean somethingChanged;

	public RadarSettingsManager() {
		this.game = Minecraft.getMinecraft();
	}

	public void loadSettings(File settingsFile) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(settingsFile));
			String sCurrentLine;
			while ((sCurrentLine = in.readLine()) != null) {
				String[] curLine = sCurrentLine.split(":");
				if (curLine[0].equals("Show Radar")) {
					this.show = Boolean.parseBoolean(curLine[1]);
				} else if (curLine[0].equals("Show Hostiles")) {
					this.showHostiles = Boolean.parseBoolean(curLine[1]);
				} else if (curLine[0].equals("Show Players")) {
					this.showPlayers = Boolean.parseBoolean(curLine[1]);
				} else if (curLine[0].equals("Show Neutrals")) {
					this.showNeutrals = Boolean.parseBoolean(curLine[1]);
				} else if (curLine[0].equals("Filter Mob Icons")) {
					this.filtering = Boolean.parseBoolean(curLine[1]);
				} else if (curLine[0].equals("Outline Mob Icons")) {
					this.outlines = Boolean.parseBoolean(curLine[1]);
				} else if (curLine[0].equals("Show Player Helmets")) {
					this.showHelmetsPlayers = Boolean.parseBoolean(curLine[1]);
				} else if (curLine[0].equals("Show Mob Helmets")) {
					this.showHelmetsMobs = Boolean.parseBoolean(curLine[1]);
				} else if (curLine[0].equals("Show Player Names")) {
					this.showPlayerNames = Boolean.parseBoolean(curLine[1]);
				} else if (curLine[0].equals("Font Scale")) {
					this.fontScale = Float.parseFloat(curLine[1]);
				} else if (curLine[0].equals("Randomobs")) {
					this.randomobs = Boolean.parseBoolean(curLine[1]);
				} else if (curLine[0].equals("Hidden Mobs")) {
					applyHiddenMobSettings(curLine[1]);
				}
			}
			in.close();
		} catch (Exception e) {
		}
	}

	private void applyHiddenMobSettings(String hiddenMobs) {
		String[] mobs = hiddenMobs.split(",");
		for (int t = 0; t < mobs.length; t++) {
			EnumMobs mob = EnumMobs.getMobByName(mobs[t]);
			if (mob != null) {
				mob.enabled = false;
			} else {
				CustomMobsManager.add(mobs[t], false);
			}
		}
	}

	public void saveAll(PrintWriter out) {
		out.println("Show Radar:" + Boolean.toString(this.show));
		out.println("Show Hostiles:" + Boolean.toString(this.showHostiles));
		out.println("Show Players:" + Boolean.toString(this.showPlayers));
		out.println("Show Neutrals:" + Boolean.toString(this.showNeutrals));
		out.println("Filter Mob Icons:" + Boolean.toString(this.filtering));
		out.println("Outline Mob Icons:" + Boolean.toString(this.outlines));
		out.println("Show Player Helmets:" + Boolean.toString(this.showHelmetsPlayers));
		out.println("Show Mob Helmets:" + Boolean.toString(this.showHelmetsMobs));
		out.println("Show Player Names:" + Boolean.toString(this.showPlayerNames));
		out.println("Font Scale:" + Float.toString(this.fontScale));
		out.println("Randomobs:" + Boolean.toString(this.randomobs));
		out.print("Hidden Mobs:");
		for (EnumMobs mob : EnumMobs.values()) {
			if ((mob.isTopLevelUnit) && (!mob.enabled)) {
				out.print(mob.name + ",");
			}
		}
		for (CustomMob mob : CustomMobsManager.mobs) {
			if (!mob.enabled) {
				out.print(mob.name + ",");
			}
		}
		out.println();
	}

	public String getKeyText(EnumOptionsMinimap par1EnumOptions) {
		String s = I18nUtils.getString(par1EnumOptions.getEnumString()) + ": ";
		if (par1EnumOptions.getEnumBoolean()) {
			boolean flag = getOptionBooleanValue(par1EnumOptions);
			if (flag) {
				return s + I18nUtils.getString("options.on");
			}
			return s + I18nUtils.getString("options.off");
		}
		return s;
	}

	public boolean getOptionBooleanValue(EnumOptionsMinimap par1EnumOptions) {
		switch (par1EnumOptions) {
			case SHOWRADAR:
				return this.show;
			case SHOWHOSTILES:
				return this.showHostiles;
			case SHOWPLAYERS:
				return this.showPlayers;
			case SHOWNEUTRALS:
				return this.showNeutrals;
			case SHOWPLAYERHELMETS:
				return this.showHelmetsPlayers;
			case SHOWMOBHELMETS:
				return this.showHelmetsMobs;
			case SHOWPLAYERNAMES:
				return this.showPlayerNames;
			case RADAROUTLINES:
				return this.outlines;
			case RADARFILTERING:
				return this.filtering;
			case RANDOMOBS:
				return this.randomobs;
		}
		throw new IllegalArgumentException("Add code to handle EnumOptionMinimap: " + par1EnumOptions.getEnumString() +
		                                   ". (possibly not a boolean)");
	}

	public void setOptionValue(EnumOptionsMinimap par1EnumOptions, int i) {
		switch (par1EnumOptions) {
			case SHOWRADAR:
				this.show = (!this.show);
				break;
			case SHOWHOSTILES:
				this.showHostiles = (!this.showHostiles);
				break;
			case SHOWPLAYERS:
				this.showPlayers = (!this.showPlayers);
				break;
			case SHOWNEUTRALS:
				this.showNeutrals = (!this.showNeutrals);
				break;
			case SHOWPLAYERHELMETS:
				this.showHelmetsPlayers = (!this.showHelmetsPlayers);
				break;
			case SHOWMOBHELMETS:
				this.showHelmetsMobs = (!this.showHelmetsMobs);
				break;
			case SHOWPLAYERNAMES:
				this.showPlayerNames = (!this.showPlayerNames);
				break;
			case RADAROUTLINES:
				this.outlines = (!this.outlines);
				break;
			case RADARFILTERING:
				this.filtering = (!this.filtering);
				break;
			case RANDOMOBS:
				this.randomobs = (!this.randomobs);
				break;
			default:
				throw new IllegalArgumentException(
						"Add code to handle EnumOptionMinimap: " + par1EnumOptions.getEnumString());
		}
		this.somethingChanged = true;
	}

	public boolean isChanged() {
		if (this.somethingChanged) {
			this.somethingChanged = false;
			return true;
		}
		return false;
	}
}
