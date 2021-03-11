package com.thevoxelbox.voxelmap.interfaces;

import com.thevoxelbox.voxelmap.MapSettingsManager;
import com.thevoxelbox.voxelmap.RadarSettingsManager;

public abstract interface IVoxelMap {
	public abstract IObservableChunkChangeNotifier getNotifier();

	public abstract MapSettingsManager getMapOptions();

	public abstract RadarSettingsManager getRadarOptions();

	public abstract IMap getMap();

	public abstract IRadar getRadar();

	public abstract IColorManager getColorManager();

	public abstract IWaypointManager getWaypointManager();

	public abstract IDimensionManager getDimensionManager();

	public abstract void setPermissions(boolean paramBoolean1, boolean paramBoolean2);

	public abstract void newSubWorldName(String paramString);

	public abstract void newSubWorldHash(String paramString);
}
