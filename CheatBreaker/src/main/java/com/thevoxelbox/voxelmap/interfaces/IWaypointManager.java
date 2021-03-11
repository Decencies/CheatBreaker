package com.thevoxelbox.voxelmap.interfaces;

import com.thevoxelbox.voxelmap.util.Waypoint;
import java.util.ArrayList;

public abstract interface IWaypointManager {
	public abstract ArrayList<Waypoint> getWaypoints();

	public abstract void deleteWaypoint(Waypoint paramWaypoint);

	public abstract void saveWaypoints();

	public abstract void addWaypoint(Waypoint paramWaypoint);

	public abstract void check2dWaypoints();

	public abstract void handleDeath();

	public abstract void loadWaypoints();

	public abstract void moveWaypointEntityToBack();

	public abstract void newSubWorldName(String paramString);

	public abstract void newSubWorldHash(String paramString);

	public abstract void newWorld(int paramInt);

	public abstract String getCurrentSubworldDescriptor();
}
