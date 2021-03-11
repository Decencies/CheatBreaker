package com.thevoxelbox.voxelmap.gui;

import com.thevoxelbox.voxelmap.RadarSettingsManager;
import com.thevoxelbox.voxelmap.util.CustomMob;
import com.thevoxelbox.voxelmap.util.CustomMobsManager;
import com.thevoxelbox.voxelmap.util.EnumMobs;
import com.thevoxelbox.voxelmap.util.GLUtils;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

class GuiSlotMobs
		extends GuiSlot {
	final GuiMobs parentGui;
	private ArrayList<String> mobNames;
	private RadarSettingsManager options;

	public GuiSlotMobs(GuiMobs par1GuiMobs) {
		super(par1GuiMobs.options.game, par1GuiMobs.getWidth(), par1GuiMobs.getHeight(), 32,
				par1GuiMobs.getHeight() - 41 + 4, 18);
		this.parentGui = par1GuiMobs;
		this.options = this.parentGui.options;
		this.mobNames = new ArrayList();
		for (EnumMobs mob : EnumMobs.values()) {
			if ((mob.isTopLevelUnit) && (((mob.isHostile) && (this.options.showHostiles)) ||
			                             ((mob.isNeutral) && (this.options.showNeutrals)))) {
				this.mobNames.add(mob.name);
			}
		}
		for (CustomMob mob : CustomMobsManager.mobs) {
			if (((mob.isHostile) && (this.options.showHostiles)) || ((mob.isNeutral) && (this.options.showNeutrals))) {
				this.mobNames.add(mob.name);
			}
		}
		final Collator collator = I18nUtils.getLocaleAwareCollator();
		Collections.sort(this.mobNames, new Comparator<String>() {
			public int compare(String name1, String name2) {
				name1 = GuiSlotMobs.getTranslatedName(name1);
				name2 = GuiSlotMobs.getTranslatedName(name2);

				return collator.compare(name1, name2);
			}
		});
	}

	private static String getTranslatedName(String name) {
		name = StatCollector.translateToLocal("entity." + name + ".name");

		name = name.replaceAll("^entity.", "").replaceAll(".name$", "");
		return name;
	}

	protected int getSize() {
		return this.mobNames.size();
	}

	protected void elementClicked(int par1, boolean par2, int par3, int par4) {
		this.parentGui.setSelectedMob((String) this.mobNames.get(par1));

		int leftEdge = this.parentGui.getWidth() / 2 - 92 - 16;
		byte padding = 3;

		int width = 215;
		if ((this.field_148150_g >= leftEdge + width - 16 - padding) &&
		    (this.field_148150_g <= leftEdge + width + padding)) {
			this.parentGui.toggleMobVisibility();
		} else if (par2) {
			Mouse.next();
			this.parentGui.toggleMobVisibility();
			return;
		}
	}

	protected boolean isSelected(int par1) {
		return ((String) this.mobNames.get(par1)).equals(this.parentGui.selectedMobName);
	}

	protected int func_148138_e() {
		return getSize() * 18;
	}

	protected void drawBackground() {
		this.parentGui.drawDefaultBackground();
	}

	protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator, int par6, int par7) {
		String name = (String) this.mobNames.get(par1);
		boolean isHostile = false;
		boolean isNeutral = false;
		boolean isEnabled = true;
		EnumMobs mob = EnumMobs.getMobByName(name);
		if (mob != null) {
			isHostile = mob.isHostile;
			isNeutral = mob.isNeutral;
			isEnabled = mob.enabled;
		} else {
			CustomMob customMob = CustomMobsManager.getCustomMobByName(name);
			if (customMob != null) {
				isHostile = customMob.isHostile;
				isNeutral = customMob.isNeutral;
				isEnabled = customMob.enabled;
			}
		}
		int red = isHostile ? 255 : 0;
		int green = isNeutral ? 255 : 0;
		int color = -16777216 + (red << 16) + (green << 8) + 0;
		this.parentGui.drawCenteredString(this.parentGui.getFontRenderer(), getTranslatedName(name),
				this.parentGui.getWidth() / 2, par3 + 3, color);

		byte padding = 3;
		if ((this.field_148150_g >= par2 - padding) && (this.field_148162_h >= par3) &&
		    (this.field_148150_g <= par2 + 215 + padding) && (this.field_148162_h <= par3 + this.field_148149_f)) {
			String tooltip;
			if ((this.field_148150_g >= par2 + 215 - 16 - padding) && (this.field_148150_g <= par2 + 215 + padding)) {
				tooltip = isEnabled ? I18nUtils.getString("options.minimap.mobs.disable") :
						I18nUtils.getString("options.minimap.mobs.enable");
			} else {
				tooltip = isEnabled ? I18nUtils.getString("options.minimap.mobs.enabled") :
						I18nUtils.getString("options.minimap.mobs.disabled");
			}
			GuiMobs.setTooltip(this.parentGui, tooltip);
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GLUtils.img("textures/gui/container/inventory.png");
		int xOffset = isEnabled ? 72 : 90;
		int yOffset = 216;
		this.parentGui.drawTexturedModalRect(par2 + 198, par3 - 2, xOffset, yOffset, 16, 16);
	}
}
