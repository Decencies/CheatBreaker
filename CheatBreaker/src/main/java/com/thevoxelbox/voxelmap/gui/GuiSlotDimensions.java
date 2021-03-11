package com.thevoxelbox.voxelmap.gui;

import com.thevoxelbox.voxelmap.gui.overridden.GuiSlotMinimap;
import com.thevoxelbox.voxelmap.interfaces.IDimensionManager;
import com.thevoxelbox.voxelmap.util.Dimension;
import com.thevoxelbox.voxelmap.util.GLUtils;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

class GuiSlotDimensions
		extends GuiSlotMinimap {
	final GuiScreenAddWaypoint parentGui;
	private IDimensionManager dimensionManager;

	public GuiSlotDimensions(GuiScreenAddWaypoint par1GuiWaypoints) {
		super(Minecraft.getMinecraft(), par1GuiWaypoints.getWidth(), par1GuiWaypoints.getHeight(),
				par1GuiWaypoints.getHeight() / 6 + 123 - 14, par1GuiWaypoints.getHeight() / 6 + 164 + 3, 18);
		this.parentGui = par1GuiWaypoints;
		setSlotWidth(175);
		setSlotXBoundsFromLeft((this.parentGui.getWidth() - this.slotWidth) / 2);

		setShowSelectionBox(false);
		setShowTopBottomBG(false);
		setShowSlotBG(false);
		this.dimensionManager = this.parentGui.master.getDimensionManager();
		scrollBy(this.dimensionManager.getDimensions().indexOf(this.dimensionManager
				.getDimensionByID(((Integer) this.parentGui.waypoint.dimensions.first()).intValue())) *
		         this.slotHeight);
	}

	protected int getSize() {
		return this.dimensionManager.getDimensions().size();
	}

	protected void elementClicked(int par1, boolean par2, int x, int y) {
		this.parentGui.setSelectedDimension((Dimension) this.dimensionManager.getDimensions().get(par1));

		int leftEdge = this.parentGui.getWidth() / 2 - this.slotWidth / 2;
		byte padding = 4;
		byte iconWidth = 16;

		int width = this.slotWidth;
		if ((this.mouseX >= leftEdge + width - iconWidth - padding) && (this.mouseX <= leftEdge + width)) {
			this.parentGui.toggleDimensionSelected();
		} else if (par2) {
			Mouse.next();
			this.parentGui.toggleDimensionSelected();
			return;
		}
	}

	protected boolean isSelected(int par1) {
		return ((Dimension) this.dimensionManager.getDimensions().get(par1)).equals(this.parentGui.selectedDimension);
	}

	protected int getContentHeight() {
		return getSize() * 18;
	}

	protected void drawBackground() {
	}

	protected void overlayBackground(int par1, int par2, int par3, int par4) {
	}

	protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator, int x, int y) {
		Dimension dim = (Dimension) this.dimensionManager.getDimensions().get(par1);
		String name = dim.name;
		if ((name.equals("notLoaded")) || (name.equals("failedToLoad"))) {
			name = "dimension " + dim.ID + "(" + Minecraft.getMinecraft().theWorld.provider.getClass().getSimpleName
					() +
			       ")";
		}
		this.parentGui
				.drawCenteredString(this.parentGui.getFontRenderer(), dim.name, this.parentGui.getWidth() / 2, par3
				                                                                                               + 3,
						16777215);

		byte padding = 4;
		byte iconWidth = 16;
		par2 = this.parentGui.getWidth() / 2 - this.slotWidth / 2;
		int width = this.slotWidth;
		if ((this.mouseX >= par2 + padding) && (this.mouseY >= par3) && (this.mouseX <= par2 + width + padding) &&
		    (this.mouseY <= par3 + this.slotHeight)) {
			String tooltip = null;
			if ((this.mouseX >= par2 + width - iconWidth - padding) && (this.mouseX <= par2 + width)) {
				tooltip = this.parentGui.waypoint.dimensions.contains(Integer.valueOf(dim.ID)) ?
						I18nUtils.getString("minimap.waypoints.dimension.applies") :
						I18nUtils.getString("minimap.waypoints.dimension.notapplies");
			} else {
				tooltip = null;
			}
			GuiScreenAddWaypoint.setTooltip(this.parentGui, tooltip);
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GLUtils.img("textures/gui/container/beacon.png");
		int xOffset = this.parentGui.waypoint.dimensions.contains(Integer.valueOf(dim.ID)) ? 91 : 113;
		int yOffset = 222;
		this.parentGui.drawTexturedModalRect(par2 + width - iconWidth, par3 - 2, xOffset, yOffset, 16, 16);
	}
}
