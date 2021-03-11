package com.thevoxelbox.voxelmap.gui;

import com.thevoxelbox.voxelmap.MapSettingsManager;
import com.thevoxelbox.voxelmap.gui.overridden.GuiOptionButtonMinimap;
import com.thevoxelbox.voxelmap.gui.overridden.GuiScreenMinimap;
import com.thevoxelbox.voxelmap.interfaces.IVoxelMap;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;

public class GuiMinimapControls
		extends GuiScreenMinimap {
	protected String screenTitle = "Controls";
	private IVoxelMap master;
	private GuiScreen parentScreen;
	private MapSettingsManager options;
	private int buttonId = -1;

	public GuiMinimapControls(GuiScreen par1GuiScreen, IVoxelMap master) {
		this.parentScreen = par1GuiScreen;
		this.options = master.getMapOptions();
	}

	private int getLeftBorder() {
		return getWidth() / 2 - 155;
	}

	public void initGui() {
		int var2 = getLeftBorder();
		for (int var3 = 0; var3 < this.options.keyBindings.length; var3++) {
			getButtonList()
					.add(new GuiOptionButtonMinimap(var3, var2 + var3 % 2 * 160, getHeight() / 6 + 24 * (var3 >> 1),
							70,
							20, this.options.getOptionDisplayString(var3)));
		}
		getButtonList()
				.add(new GuiButton(200, getWidth() / 2 - 100, getHeight() / 6 + 168, I18nUtils.getString("gui.done")));
		this.screenTitle = I18nUtils.getString("controls.minimap.title");
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		for (int var2 = 0; var2 < this.options.keyBindings.length; var2++) {
			((GuiButton) getButtonList().get(var2)).displayString = this.options.getOptionDisplayString(var2);
		}
		if (par1GuiButton.id == 200) {
			getMinecraft().displayGuiScreen(this.parentScreen);
		} else {
			this.buttonId = par1GuiButton.id;
			par1GuiButton.displayString = ("> " + this.options.getOptionDisplayString(par1GuiButton.id) + " <");
		}
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		if (this.buttonId >= 0) {
			this.buttonId = -1;
		} else {
			super.mouseClicked(par1, par2, par3);
		}
	}

	protected void keyTyped(char par1, int par2) {
		if (this.buttonId >= 0) {
			if (par2 != 1) {
				this.options.setKeyBinding(this.buttonId, par2);
			} else if (this.buttonId != 0) {
				this.options.setKeyBinding(this.buttonId, 0);
			}
			((GuiButton) getButtonList().get(this.buttonId)).displayString = this.options
					.getOptionDisplayString(this.buttonId);
			this.buttonId = -1;
			KeyBinding.resetKeyBindingArrayAndHash();
		} else {
			super.keyTyped(par1, par2);
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		super.drawMap();
		drawDefaultBackground();
		drawCenteredString(getFontRenderer(), this.screenTitle, getWidth() / 2, 20, 16777215);
		int var4 = getLeftBorder();
		int var5 = 0;
		while (var5 < this.options.keyBindings.length) {
			boolean var6 = false;
			int var7 = 0;
			for (; ; ) {
				if (var7 < this.options.keyBindings.length) {
					if ((this.options.keyBindings[var5].getKeyCode() == 0) || (((var7 == var5) ||
					                                                            (this.options.keyBindings[var5]
							                                                             .getKeyCode() !=
					                                                             this.options.keyBindings[var7]
							                                                             .getKeyCode())) &&
					                                                           ((this.options.keyBindings[var5]
							                                                             .getKeyCode() !=
					                                                             this.options.game.gameSettings
							                                                             .keyBindings[var7]
							                                                             .getKeyCode()) ||
					                                                            (this.options.keyBindings[var5]
							                                                             .equals(this.options.game
									                                                             .gameSettings
									                                                             .keyBindings[var7])))
					)) {
						var7++;
					} else {
						var6 = true;
					}
				} else {
					if (var7 >= this.options.game.gameSettings.keyBindings.length) {
						break;
					}
					if ((this.options.keyBindings[var5].getKeyCode() != 0) &&
					    (this.options.keyBindings[var5].getKeyCode() ==
					     this.options.game.gameSettings.keyBindings[var7].getKeyCode()) &&
					    (!this.options.keyBindings[var5].equals(this.options.game.gameSettings.keyBindings[var7]))) {
						break;
					}
					var7++;
				}
			}
			var6 = true;
			label310:
			if (this.buttonId == var5) {
				((GuiButton) getButtonList().get(var5)).displayString = "§f> §e??? §f<";
			} else if (var6) {
				((GuiButton) getButtonList().get(var5)).displayString = ("§c" +
				                                                         this.options.getOptionDisplayString(var5));
			} else {
				((GuiButton) getButtonList().get(var5)).displayString = this.options.getOptionDisplayString(var5);
			}
			drawString(getFontRenderer(), this.options.getKeyBindingDescription(var5), var4 + var5 % 2 * 160 + 70 + 6,
					getHeight() / 6 + 24 * (var5 >> 1) + 7, -1);
			var5++;
		}
		drawCenteredString(getFontRenderer(), I18nUtils.getString("controls.minimap.unbind1"), getWidth() / 2,
				getHeight() / 6 + 115, 16777215);
		drawCenteredString(getFontRenderer(), I18nUtils.getString("controls.minimap.unbind2"), getWidth() / 2,
				getHeight() / 6 + 129, 16777215);

		super.drawScreen(par1, par2, par3);
	}
}
