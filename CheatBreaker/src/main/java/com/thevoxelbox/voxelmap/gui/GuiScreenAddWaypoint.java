package com.thevoxelbox.voxelmap.gui;

import com.thevoxelbox.voxelmap.gui.overridden.GuiScreenMinimap;
import com.thevoxelbox.voxelmap.interfaces.IColorManager;
import com.thevoxelbox.voxelmap.interfaces.IVoxelMap;
import com.thevoxelbox.voxelmap.interfaces.IWaypointManager;
import com.thevoxelbox.voxelmap.util.Dimension;
import com.thevoxelbox.voxelmap.util.GLUtils;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import com.thevoxelbox.voxelmap.util.Waypoint;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiScreenAddWaypoint
		extends GuiScreenMinimap {
	protected Dimension selectedDimension = null;
	protected Waypoint waypoint;
	IVoxelMap master;
	IWaypointManager waypointManager;
	IColorManager colorManager;
	private GuiWaypoints parentGui;
	private GuiSlotDimensions dimensionList;
	private String tooltip = null;
	private GuiTextField waypointName;
	private GuiTextField waypointX;
	private GuiTextField waypointZ;
	private GuiTextField waypointY;
	private GuiButton buttonEnabled;
	private boolean choosingColor = false;
	private float red;
	private float green;
	private float blue;
	private boolean enabled;
	private Random generator = new Random();

	public GuiScreenAddWaypoint(IVoxelMap master, GuiWaypoints par1GuiScreen, Waypoint par2Waypoint) {
		this.master = master;
		this.waypointManager = master.getWaypointManager();
		this.colorManager = master.getColorManager();
		this.parentGui = par1GuiScreen;
		this.waypoint = par2Waypoint;

		this.red = this.waypoint.red;
		this.green = this.waypoint.green;
		this.blue = this.waypoint.blue;
		this.enabled = this.waypoint.enabled;
	}

	static String setTooltip(GuiScreenAddWaypoint par0GuiWaypoint, String par1Str) {
		return par0GuiWaypoint.tooltip = par1Str;
	}

	public void updateScreen() {
		this.waypointName.updateCursorCounter();
		this.waypointX.updateCursorCounter();
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		getButtonList().clear();

		getButtonList().add(new GuiButton(0, getWidth() / 2 - 155, getHeight() / 6 + 168, 150, 20,
				I18nUtils.getString("addServer.add")));
		getButtonList().add(new GuiButton(1, getWidth() / 2 + 5, getHeight() / 6 + 168, 150, 20,
				I18nUtils.getString("gui.cancel")));
		this.waypointName = new GuiTextField(getFontRenderer(), getWidth() / 2 - 100, getHeight() / 6 + 0 + 13, 200,
				20);
		this.waypointName.setFocused(true);

		this.waypointName.setText(this.waypoint.name);
		this.waypointX = new GuiTextField(getFontRenderer(), getWidth() / 2 - 100, getHeight() / 6 + 41 + 13, 56, 20);
		this.waypointX.func_146203_f(128);
		this.waypointX.setText("" + this.waypoint.getX());
		this.waypointZ = new GuiTextField(getFontRenderer(), getWidth() / 2 - 28, getHeight() / 6 + 41 + 13, 56, 20);
		this.waypointZ.func_146203_f(128);
		this.waypointZ.setText("" + this.waypoint.getZ());
		this.waypointY = new GuiTextField(getFontRenderer(), getWidth() / 2 + 44, getHeight() / 6 + 41 + 13, 56, 20);
		this.waypointY.func_146203_f(128);
		this.waypointY.setText("" + this.waypoint.getY());
		getButtonList().add(
				this.buttonEnabled = new GuiButton(2, getWidth() / 2 - 101, getHeight() / 6 + 82 + 6, 100, 20,
						"Enabled: " + (this.waypoint.enabled ? "On" : "Off")));
		((GuiButton) getButtonList().get(0)).enabled = (this.waypointName.getText().length() > 0);

		this.dimensionList = new GuiSlotDimensions(this);
		this.dimensionList.registerScrollButtons(7, 8);
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 2) {
				this.waypoint.enabled = (!this.waypoint.enabled);
			}
			if (par1GuiButton.id == 1) {
				this.waypoint.red = this.red;
				this.waypoint.green = this.green;
				this.waypoint.blue = this.blue;
				this.waypoint.enabled = this.enabled;
				if (this.parentGui != null) {
					this.parentGui.confirmClicked(false, 0);
				} else {
					getMinecraft().displayGuiScreen(null);
				}
			} else if (par1GuiButton.id == 0) {
				this.waypoint.name = this.waypointName.getText();
				this.waypoint.setX(Integer.parseInt(this.waypointX.getText()));
				this.waypoint.setZ(Integer.parseInt(this.waypointZ.getText()));
				this.waypoint.setY(Integer.parseInt(this.waypointY.getText()));
				if (this.parentGui != null) {
					this.parentGui.confirmClicked(true, 0);
				} else {
					this.waypointManager.addWaypoint(this.waypoint);
					getMinecraft().displayGuiScreen(null);
				}
			}
		}
	}

	protected void keyTyped(char par1, int par2) {
		this.waypointName.textboxKeyTyped(par1, par2);
		this.waypointX.textboxKeyTyped(par1, par2);
		this.waypointZ.textboxKeyTyped(par1, par2);
		this.waypointY.textboxKeyTyped(par1, par2);
		if (par1 == '\t') {
			if (this.waypointName.isFocused()) {
				this.waypointName.setFocused(false);
				this.waypointX.setFocused(true);
				this.waypointZ.setFocused(false);
				this.waypointY.setFocused(false);
			} else if (this.waypointX.isFocused()) {
				this.waypointName.setFocused(false);
				this.waypointX.setFocused(false);
				this.waypointZ.setFocused(true);
				this.waypointY.setFocused(false);
			} else if (this.waypointZ.isFocused()) {
				this.waypointName.setFocused(false);
				this.waypointX.setFocused(false);
				this.waypointZ.setFocused(false);
				this.waypointY.setFocused(true);
			} else if (this.waypointY.isFocused()) {
				this.waypointName.setFocused(true);
				this.waypointX.setFocused(false);
				this.waypointZ.setFocused(false);
				this.waypointY.setFocused(false);
			}
		}
		if (par1 == '\r') {
			actionPerformed((GuiButton) getButtonList().get(0));
		}
		boolean acceptable = this.waypointName.getText().length() > 0;
		try {
			int x = Integer.parseInt(this.waypointX.getText());
			acceptable = acceptable;
		} catch (NumberFormatException e) {
			acceptable = false;
		}
		try {
			int z = Integer.parseInt(this.waypointZ.getText());
			acceptable = acceptable;
		} catch (NumberFormatException e) {
			acceptable = false;
		}
		try {
			int y = Integer.parseInt(this.waypointY.getText());
			acceptable = acceptable;
		} catch (NumberFormatException e) {
			acceptable = false;
		}
		((GuiButton) getButtonList().get(0)).enabled = acceptable;
		if (par2 == 1) {
			this.waypoint.red = this.red;
			this.waypoint.green = this.green;
			this.waypoint.blue = this.blue;
			this.waypoint.enabled = this.enabled;
		}
		super.keyTyped(par1, par2);
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		if (!this.choosingColor) {
			super.mouseClicked(par1, par2, par3);
			this.waypointName.mouseClicked(par1, par2, par3);
			this.waypointX.mouseClicked(par1, par2, par3);
			this.waypointZ.mouseClicked(par1, par2, par3);
			this.waypointY.mouseClicked(par1, par2, par3);
			if ((par1 >= getWidth() / 2 + 85) && (par1 <= getWidth() / 2 + 101) &&
			    (par2 >= getHeight() / 6 + 82 + 11) && (par2 <= getHeight() / 6 + 82 + 21)) {
				this.choosingColor = true;
			}
		} else if ((par1 >= getWidth() / 2 - 128) && (par1 <= getWidth() / 2 + 128) &&
		           (par2 >= getHeight() / 2 - 128) && (par2 <= getHeight() / 2 + 128)) {
			int color = this.colorManager.getColorPicker()
					.getRGB(par1 - (getWidth() / 2 - 128), par2 - (getHeight() / 2 - 128));
			this.waypoint.red = ((color >> 16 & 0xFF) / 255.0F);
			this.waypoint.green = ((color >> 8 & 0xFF) / 255.0F);
			this.waypoint.blue = ((color >> 0 & 0xFF) / 255.0F);
			this.choosingColor = false;
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		super.drawMap();
		this.tooltip = null;
		this.buttonEnabled.displayString = (I18nUtils.getString("minimap.waypoints.enabled") + " " +
		                                    (this.waypoint.enabled ? I18nUtils.getString("options.on") :
				                                     I18nUtils.getString("options.off")));
		drawDefaultBackground();
		this.dimensionList.drawScreen(par1, par2, par3);

		drawCenteredString(getFontRenderer(), (this.parentGui != null) && (this.parentGui.editClicked) ?
						I18nUtils.getString("minimap.waypoints.edit") : I18nUtils.getString("minimap.waypoints.new"),
				getWidth() / 2, 20, 16777215);

		drawString(getFontRenderer(), I18nUtils.getString("minimap.waypoints.name"), getWidth() / 2 - 100,
				getHeight() / 6 + 0, 10526880);
		drawString(getFontRenderer(), I18nUtils.getString("X"), getWidth() / 2 - 100, getHeight() / 6 + 41, 10526880);
		drawString(getFontRenderer(), I18nUtils.getString("Z"), getWidth() / 2 - 28, getHeight() / 6 + 41, 10526880);
		drawString(getFontRenderer(), I18nUtils.getString("Y"), getWidth() / 2 + 44, getHeight() / 6 + 41, 10526880);
		drawString(getFontRenderer(), I18nUtils.getString("minimap.waypoints.choosecolor"), getWidth() / 2 + 10,
				getHeight() / 6 + 82 + 11, 10526880);

		this.waypointName.drawTextBox();
		this.waypointX.drawTextBox();
		this.waypointZ.drawTextBox();
		this.waypointY.drawTextBox();
		GL11.glColor4f(this.waypoint.red, this.waypoint.green, this.waypoint.blue, 1.0F);

		GLUtils.disp(-1);
		drawTexturedModalRect(getWidth() / 2 + 85, getHeight() / 6 + 82 + 11, 0, 0, 16, 10);
		super.drawScreen(par1, par2, par3);
		if (this.choosingColor) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GLUtils.img(new ResourceLocation("voxelmap/images/colorPicker.png"));
			drawTexturedModalRect(getWidth() / 2 - 128, getHeight() / 2 - 128, 0, 0, 256, 256);
		}
		drawTooltip(this.tooltip, par1, par2);
	}

	public void setSelectedDimension(Dimension dimension) {
		this.selectedDimension = dimension;
	}

	public void toggleDimensionSelected() {
		if ((this.waypoint.dimensions.size() > 1) &&
		    (this.waypoint.dimensions.contains(Integer.valueOf(this.selectedDimension.ID))) &&
		    (this.selectedDimension.ID != Minecraft.getMinecraft().thePlayer.dimension)) {
			this.waypoint.dimensions.remove(new Integer(this.selectedDimension.ID));
		} else if (!this.waypoint.dimensions.contains(Integer.valueOf(this.selectedDimension.ID))) {
			this.waypoint.dimensions.add(new Integer(this.selectedDimension.ID));
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
