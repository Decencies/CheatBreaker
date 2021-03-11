package com.thevoxelbox.voxelmap.gui;

import com.thevoxelbox.voxelmap.MapSettingsManager;
import com.thevoxelbox.voxelmap.gui.overridden.GuiScreenMinimap;
import com.thevoxelbox.voxelmap.interfaces.IVoxelMap;
import com.thevoxelbox.voxelmap.interfaces.IWaypointManager;
import com.thevoxelbox.voxelmap.util.GameVariableAccessShim;
import com.thevoxelbox.voxelmap.util.I18nUtils;
import com.thevoxelbox.voxelmap.util.Waypoint;
import java.util.Random;
import java.util.TreeSet;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;

public class GuiWaypoints
		extends GuiScreenMinimap
		implements GuiYesNoCallback {
	protected final MapSettingsManager options;
	protected final IWaypointManager waypointManager;
	private final GuiScreen parentScreen;
	protected String screenTitle = "Waypoints";
	protected boolean editClicked = false;
	protected Waypoint selectedWaypoint = null;
	protected Waypoint newWaypoint = null;
	private IVoxelMap master;
	private GuiSlotWaypoints waypointList;
	private GuiButton buttonEdit;
	private GuiButton buttonDelete;
	private boolean deleteClicked = false;
	private GuiButton buttonTeleport;
	private GuiButton buttonSortName;
	private GuiButton buttonSortCreated;
	private GuiButton buttonSortDistance;
	private GuiButton buttonSortColor;
	private boolean addClicked = false;
	private String tooltip = null;
	private Random generator = new Random();
	private boolean changedSort = false;

	public GuiWaypoints(GuiScreen parentScreen, IVoxelMap master) {
		this.master = master;
		this.parentScreen = parentScreen;
		this.options = master.getMapOptions();
		this.waypointManager = master.getWaypointManager();
	}

	static String setTooltip(GuiWaypoints par0GuiWaypoints, String par1Str) {
		return par0GuiWaypoints.tooltip = par1Str;
	}

	static GuiButton getButtonEdit(GuiWaypoints par0GuiWaypoints) {
		return par0GuiWaypoints.buttonEdit;
	}

	static GuiButton getButtonDelete(GuiWaypoints par0GuiWaypoints) {
		return par0GuiWaypoints.buttonDelete;
	}

	static GuiButton getButtonTeleport(GuiWaypoints par0GuiWaypoints) {
		return par0GuiWaypoints.buttonTeleport;
	}

	public void initGui() {
		int var2 = 0;
		this.screenTitle = I18nUtils.getString("minimap.waypoints.title");

		this.waypointList = new GuiSlotWaypoints(this);
		this.waypointList.func_148134_d(7, 8);

		this.options.getClass();
		getButtonList().add(this.buttonSortName = new GuiButton(2, getWidth() / 2 - 154, 34, 77, 20,
				I18nUtils.getString("minimap.waypoints.sortbyname")));
		this.options.getClass();
		getButtonList().add(this.buttonSortDistance = new GuiButton(3, getWidth() / 2 - 77, 34, 77, 20,
				I18nUtils.getString("minimap.waypoints.sortbydistance")));
		this.options.getClass();
		getButtonList().add(this.buttonSortCreated = new GuiButton(1, getWidth() / 2, 34, 77, 20,
				I18nUtils.getString("minimap.waypoints.sortbycreated")));
		this.options.getClass();
		getButtonList().add(this.buttonSortColor = new GuiButton(4, getWidth() / 2 + 77, 34, 77, 20,
				I18nUtils.getString("minimap.waypoints.sortbycolor")));

		getButtonList().add(this.buttonEdit = new GuiButton(-1, getWidth() / 2 - 154, getHeight() - 52, 100, 20,
				I18nUtils.getString("selectServer.edit")));
		getButtonList().add(this.buttonDelete = new GuiButton(-2, getWidth() / 2 - 50, getHeight() - 52, 100, 20,
				I18nUtils.getString("selectServer.delete")));
		getButtonList().add(this.buttonTeleport = new GuiButton(-3, getWidth() / 2 + 4 + 50, getHeight() - 52, 100, 20,
				I18nUtils.getString("minimap.waypoints.teleportto")));

		getButtonList().add(new GuiButton(-4, getWidth() / 2 - 154, getHeight() - 28, 100, 20,
				I18nUtils.getString("minimap.waypoints.newwaypoint")));
		getButtonList().add(new GuiButton(-5, getWidth() / 2 - 50, getHeight() - 28, 100, 20,
				I18nUtils.getString("menu.options")));
		getButtonList().add(new GuiButton(65336, getWidth() / 2 + 4 + 50, getHeight() - 28, 100, 20,
				I18nUtils.getString("gui.done")));

		boolean isSomethingSelected = this.selectedWaypoint != null;
		this.buttonEdit.enabled = isSomethingSelected;
		this.buttonDelete.enabled = isSomethingSelected;
		this.buttonTeleport.enabled = ((isSomethingSelected) && (canTeleport()));

		sort();
	}

	private void sort() {
		int sortKey = Math.abs(this.options.sort);
		boolean ascending = this.options.sort > 0;
		this.waypointList.sortBy(sortKey, ascending);
		String arrow = ascending ? "↑" : "↓";
		this.options.getClass();
		if (sortKey == 2) {
			this.buttonSortName.displayString = (arrow + " " + I18nUtils.getString("mco.configure.world.name") + " " +
			                                     arrow);
		} else {
			this.buttonSortName.displayString = I18nUtils.getString("mco.configure.world.name");
		}
		this.options.getClass();
		if (sortKey == 3) {
			this.buttonSortDistance.displayString = (arrow + " " +
			                                         I18nUtils.getString("minimap.waypoints.sortbydistance") + " " +
			                                         arrow);
		} else {
			this.buttonSortDistance.displayString = I18nUtils.getString("minimap.waypoints.sortbydistance");
		}
		this.options.getClass();
		if (sortKey == 1) {
			this.buttonSortCreated.displayString = (arrow + " " +
			                                        I18nUtils.getString("minimap.waypoints.sortbycreated") + " " +
			                                        arrow);
		} else {
			this.buttonSortCreated.displayString = I18nUtils.getString("minimap.waypoints.sortbycreated");
		}
		this.options.getClass();
		if (sortKey == 4) {
			this.buttonSortColor.displayString = (arrow + " " + I18nUtils.getString("minimap.waypoints.sortbycolor") +
			                                      " " + arrow);
		} else {
			this.buttonSortColor.displayString = I18nUtils.getString("minimap.waypoints.sortbycolor");
		}
	}

	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id > 0) {
				this.options.setSort(par1GuiButton.id);
				this.changedSort = true;
				sort();
			}
			if (par1GuiButton.id == -1) {
				editWaypoint(this.selectedWaypoint);
			}
			if (par1GuiButton.id == -2) {
				String var2 = this.selectedWaypoint.name;
				if (var2 != null) {
					this.deleteClicked = true;
					String var4 = I18nUtils.getString("minimap.waypoints.deleteconfirm");
					String var5 = "'" + var2 + "' " + I18nUtils.getString("selectServer.deleteWarning");
					String var6 = I18nUtils.getString("selectServer.deleteButton");
					String var7 = I18nUtils.getString("gui.cancel");
					GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6, var7,
							this.waypointManager.getWaypoints().indexOf(this.selectedWaypoint));
					getMinecraft().displayGuiScreen(var8);
				}
			}
			if (par1GuiButton.id == -3) {
				if (this.options.game.isIntegratedServerRunning()) {
					this.options.game.thePlayer.sendChatMessage("/ztp " + this.selectedWaypoint.name);
					getMinecraft().displayGuiScreen((GuiScreen) null);
				} else {
					if (this.selectedWaypoint.getY() > 0) {
						this.options.game.thePlayer.sendChatMessage(
								"/tp " + this.options.game.thePlayer.getCommandSenderName() + " " +
								this.selectedWaypoint.getX() + " " + this.selectedWaypoint.getY() + " " +
								this.selectedWaypoint.getZ());
						this.options.game.thePlayer.sendChatMessage(
								"/tppos " + this.selectedWaypoint.getX() + " " + this.selectedWaypoint.getY() + " " +
								this.selectedWaypoint.getZ());
					} else {
						this.options.game.thePlayer.sendChatMessage(
								"/tp " + this.options.game.thePlayer.getCommandSenderName() + " " +
								this.selectedWaypoint.getX() + " " +
								(this.options.game.thePlayer.dimension != -1 ? "128" : "64") + " " +
								this.selectedWaypoint.getZ());
						this.options.game.thePlayer.sendChatMessage("/tppos " + this.selectedWaypoint.getX() + " " +
						                                            (this.options.game.thePlayer.dimension != -1 ?
								                                             "256" : "64") + " " +
						                                            this.selectedWaypoint.getZ());
					}
					getMinecraft().displayGuiScreen((GuiScreen) null);
				}
			}
			if (par1GuiButton.id == -4) {
				addWaypoint();
			}
			if (par1GuiButton.id == -5) {
				getMinecraft().displayGuiScreen(new GuiWaypointsOptions(this, this.options));
			}
			if (par1GuiButton.id == 65336) {
				getMinecraft().displayGuiScreen(this.parentScreen);
			}
		}
	}

	public void confirmClicked(boolean par1, int par2) {
		if (this.deleteClicked) {
			this.deleteClicked = false;
			if (par1) {
				this.waypointManager.deleteWaypoint(this.selectedWaypoint);
				this.selectedWaypoint = null;
			}
			getMinecraft().displayGuiScreen(this);
		}
		if (this.editClicked) {
			this.editClicked = false;
			if (par1) {
				this.waypointManager.saveWaypoints();
			}
			getMinecraft().displayGuiScreen(this);
		}
		if (this.addClicked) {
			this.addClicked = false;
			if (par1) {
				this.waypointManager.addWaypoint(this.newWaypoint);
				setSelectedWaypoint(this.newWaypoint);
			}
			getMinecraft().displayGuiScreen(this);
		}
	}

	protected void setSelectedWaypoint(Waypoint waypoint) {
		this.selectedWaypoint = waypoint;
		boolean isSomethingSelected = this.selectedWaypoint != null;
		this.buttonEdit.enabled = isSomethingSelected;
		this.buttonDelete.enabled = isSomethingSelected;
		this.buttonTeleport.enabled = ((isSomethingSelected) && (canTeleport()));
	}

	protected void editWaypoint(Waypoint waypoint) {
		this.editClicked = true;
		getMinecraft().displayGuiScreen(new GuiScreenAddWaypoint(this.master, this, waypoint));
	}

	protected void addWaypoint() {
		this.addClicked = true;
		float r;
		float g;
		float b;
		if (this.waypointManager.getWaypoints().size() == 0) {
			r = 0.0F;
			g = 1.0F;
			b = 0.0F;
		} else {
			r = this.generator.nextFloat();
			g = this.generator.nextFloat();
			b = this.generator.nextFloat();
		}
		TreeSet<Integer> dimensions = new TreeSet();
		dimensions.add(Integer.valueOf(this.options.game.thePlayer.dimension));
		this.newWaypoint = new Waypoint("",
				this.options.game.thePlayer.dimension != -1 ? GameVariableAccessShim.xCoord() :
						GameVariableAccessShim.xCoord() * 8,
				this.options.game.thePlayer.dimension != -1 ? GameVariableAccessShim.zCoord() :
						GameVariableAccessShim.zCoord() * 8, GameVariableAccessShim.yCoord() - 1, true, r, g, b, "",
				this.master.getWaypointManager().getCurrentSubworldDescriptor(), dimensions);
		getMinecraft().displayGuiScreen(new GuiScreenAddWaypoint(this.master, this, this.newWaypoint));
	}

	protected void toggleWaypointVisibility() {
		this.selectedWaypoint.enabled = (!this.selectedWaypoint.enabled);
		this.waypointManager.saveWaypoints();
	}

	public void drawScreen(int par1, int par2, float par3) {
		super.drawMap();
		this.tooltip = null;
		this.waypointList.func_148128_a(par1, par2, par3);

		drawCenteredString(getFontRenderer(), this.screenTitle, getWidth() / 2, 20, 16777215);
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

	public boolean canTeleport() {
		boolean singlePlayer = this.options.game.isIntegratedServerRunning();
		if (singlePlayer) {
			return net.minecraft.server.MinecraftServer.getServer().worldServers[0].getWorldInfo()
					.areCommandsAllowed();
		}
		return true;
	}

	public void onGuiClosed() {
		if (this.changedSort) {
			super.onGuiClosed();
		}
	}
}
