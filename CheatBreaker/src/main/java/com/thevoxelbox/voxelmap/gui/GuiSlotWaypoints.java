package com.thevoxelbox.voxelmap.gui;

import com.thevoxelbox.voxelmap.util.GLUtils;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import com.thevoxelbox.voxelmap.util.Waypoint;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

class GuiSlotWaypoints
		extends GuiSlot {
	final GuiWaypoints parentGui;
	private ArrayList<Waypoint> waypoints;

	public GuiSlotWaypoints(GuiWaypoints par1GuiWaypoints) {
		super(par1GuiWaypoints.options.game, par1GuiWaypoints.getWidth(), par1GuiWaypoints.getHeight(), 54,
				par1GuiWaypoints.getHeight() - 65 + 4, 18);
		this.parentGui = par1GuiWaypoints;
		this.waypoints = new ArrayList();
		for (Waypoint pt : this.parentGui.waypointManager.getWaypoints()) {
			if ((pt.inWorld) && (pt.inDimension)) {
				this.waypoints.add(pt);
			}
		}
	}

	protected int getSize() {
		return this.waypoints.size();
	}

	protected void elementClicked(int par1, boolean par2, int par3, int par4) {
		this.parentGui.setSelectedWaypoint((Waypoint) this.waypoints.get(par1));

		int leftEdge = this.parentGui.getWidth() / 2 - 92 - 16;
		byte padding = 3;

		int width = 215;
		if ((this.field_148150_g >= leftEdge + width - 16 - padding) &&
		    (this.field_148150_g <= leftEdge + width + padding)) {
			this.parentGui.toggleWaypointVisibility();
		} else if (par2) {
			Mouse.next();
			this.parentGui.editWaypoint(this.parentGui.selectedWaypoint);
			return;
		}
	}

	protected boolean isSelected(int par1) {
		return ((Waypoint) this.waypoints.get(par1)).equals(this.parentGui.selectedWaypoint);
	}

	protected int func_148138_e() {
		return getSize() * 18;
	}

	protected void drawBackground() {
		this.parentGui.drawDefaultBackground();
	}

	protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator, int par6, int par7) {
		Waypoint waypoint = (Waypoint) this.waypoints.get(par1);
		this.parentGui.drawCenteredString(this.parentGui.getFontRenderer(), waypoint.isAutomated ?
						EnumChatFormatting.GRAY + "(Server) " + EnumChatFormatting.RESET + waypoint.name : waypoint
						.name,
				this.parentGui.getWidth() / 2, par3 + 3, waypoint.getUnified());

		byte padding = 3;
		if ((this.field_148150_g >= par2 - padding) && (this.field_148162_h >= par3) &&
		    (this.field_148150_g <= par2 + 215 + padding) && (this.field_148162_h <= par3 + this.field_148149_f)) {
			String tooltip;
			if ((this.field_148150_g >= par2 + 215 - 16 - padding) && (this.field_148150_g <= par2 + 215 + padding)) {
				tooltip = waypoint.enabled ? I18nUtils.getString("minimap.waypoints.disable") :
						I18nUtils.getString("minimap.waypoints.enable");
			} else {
				tooltip = "X: " + waypoint.getX() + " Z: " + waypoint.getZ();
				if (waypoint.getY() > 0) {
					tooltip = tooltip + " Y: " + waypoint.getY();
				}
			}
			GuiWaypoints.setTooltip(this.parentGui, tooltip);
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GLUtils.img("textures/gui/container/inventory.png");
		int xOffset = waypoint.enabled ? 72 : 90;
		int yOffset = 216;
		this.parentGui.drawTexturedModalRect(par2 + 198, par3 - 2, xOffset, yOffset, 16, 16);
	}

	protected void sortBy(int sortKey, boolean ascending) {
		final int order = ascending ? 1 : -1;
		this.parentGui.options.getClass();
		if (sortKey == 1) {
			final ArrayList<Waypoint> masterWaypointsList = this.parentGui.waypointManager.getWaypoints();
			Collections.sort(this.waypoints, new Comparator<Waypoint>() {
				public int compare(Waypoint waypoint1, Waypoint waypoint2) {
					return Double.compare(masterWaypointsList.indexOf(waypoint1),
							masterWaypointsList.indexOf(waypoint2)) * order;
				}
			});
		} else {
			this.parentGui.options.getClass();
			if (sortKey == 3) {
				if (ascending) {
					Collections.sort(this.waypoints);
				} else {
					Collections.sort(this.waypoints, Collections.reverseOrder());
				}
			} else {
				this.parentGui.options.getClass();
				if (sortKey == 2) {
					final Collator collator = I18nUtils.getLocaleAwareCollator();
					Collections.sort(this.waypoints, new Comparator<Waypoint>() {
						public int compare(Waypoint waypoint1, Waypoint waypoint2) {
							return collator.compare(waypoint1.name, waypoint2.name) * order;
						}
					});
				} else {
					this.parentGui.options.getClass();
					if (sortKey == 4) {
						Collections.sort(this.waypoints, new Comparator<Waypoint>() {
							public int compare(Waypoint waypoint1, Waypoint waypoint2) {
								float hue1 = java.awt.Color
										.RGBtoHSB((int) (waypoint1.red * 255.0F), (int) (waypoint1.green * 255.0F),
												(int) (waypoint1.blue * 255.0F), null)[0];
								float hue2 = java.awt.Color
										.RGBtoHSB((int) (waypoint2.red * 255.0F), (int) (waypoint2.green * 255.0F),
												(int) (waypoint2.blue * 255.0F), null)[0];
								return Double.compare(hue1, hue2) * order;
							}
						});
					}
				}
			}
		}
	}
}
